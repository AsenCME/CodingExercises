package pgdp.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @implNote The type "S" is the type of the segments
 * @implNote The type T is the segmentable object itself
 */
public class Tree<S, T extends Segmentable<S>> implements Iterable<T> {
    RootNode root = new RootNode();
    private final Comparator<S> comparator;

    Tree(Comparator<S> comp) {
        this.comparator = comp;
    }

    <R> R map(Function<T, R> leafMapper, BiFunction<S, List<R>, R> innerNodeMapper) {
        return root.map(leafMapper, innerNodeMapper);
    }

    @Override
    public Iterator<T> iterator() {
        var it = this.root.iterator();
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public T next() {
                return it.next();
            }
        };
    }

    public <A extends TreeNode> void addInOrder(List<A> list, A newNode) {
        var added = false;
        for (int j = 0; j < list.size(); j++) {
            // add only if less than
            var res = this.comparator.compare(newNode.getSegment(), list.get(j).getSegment());
            if (res < 0) {
                list.add(j, newNode);
                added = true;
                return;
            }
        }
        if (!added)
            list.add(newNode);
    }

    public boolean add(T value) {
        if (value == null)
            return false;

        var nodeCreated = false;
        var segments = value.getSegments();
        InnerNode currentNode = root;
        for (int i = 0; i < segments.size(); i++) {
            var segment = segments.get(i);
            if (i == segments.size() - 1) { // check leaves
                if (!nodeCreated) { // if no new nodes created then it might be in tree
                    var isInInner = currentNode.getInnerNodeChildren().stream()
                            .filter(x -> x.getSegment().equals(segment)).findFirst().isPresent();
                    if (isInInner) // segmentable already in tree
                        return false;
                    var nextNode = currentNode.getLeafNodeChildren().stream()
                            .filter(x -> x.getSegment().equals(segment)).findFirst();
                    if (nextNode.isPresent()) // segmentable already in tree
                        return false;
                }

                var newLeaf = new LeafNode(segment, value);
                this.addInOrder(currentNode.getLeafNodeChildren(), newLeaf);
                return true;
            } else { // check inner nodes
                var innerNodes = currentNode.getInnerNodeChildren();
                Optional<InnerNode> nextNode = Optional.empty();
                if (!nodeCreated)
                    nextNode = innerNodes.stream().filter(x -> x.getSegment().equals(segment)).findFirst();
                if (nextNode.isPresent()) // exists
                    currentNode = nextNode.get();
                else { // create new node
                    var newNode = new InnerNode(segment);
                    this.addInOrder(innerNodes, newNode);
                    currentNode = newNode;
                    nodeCreated = true;
                }
            }
        }
        return false;
    }

    abstract class TreeNode implements Iterable<T> {
        private final S segment;

        TreeNode(S segment) {
            this.segment = segment;
        }

        public S getSegment() {
            return segment;
        }

        boolean isRoot() {
            return false;
        }

        boolean isInner() {
            return false;
        }

        boolean isLeaf() {
            return false;
        }

        abstract <R> R map(Function<T, R> leafMapper, BiFunction<S, List<R>, R> innerNodeMapper);
    }

    class RootNode extends InnerNode {
        RootNode() {
            super(null);
        }

        @Override
        boolean isRoot() {
            return true;
        }
    }

    class LeafNode extends TreeNode {
        private final T value;

        LeafNode(S segment, T value) {
            super(segment);
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        @Override
        boolean isLeaf() {
            return true;
        }

        @Override
        public Iterator<T> iterator() {
            var hasIterated = new AtomicBoolean(false);
            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    if (hasIterated.get())
                        return false;
                    return true;
                }

                @Override
                public T next() {
                    if (hasIterated.get())
                        return null;
                    hasIterated.set(true);
                    return value;
                }
            };
        }

        @Override
        <R> R map(Function<T, R> leafMapper, BiFunction<S, List<R>, R> innerNodeMapper) {
            return leafMapper.apply(this.value);
        }
    }

    class InnerNode extends TreeNode {
        private final List<InnerNode> innerNodeChildren = new ArrayList<>();
        private final List<LeafNode> leafNodeChildren = new ArrayList<>();

        InnerNode(S segment) {
            super(segment);
        }

        public List<InnerNode> getInnerNodeChildren() {
            return innerNodeChildren;
        }

        public List<LeafNode> getLeafNodeChildren() {
            return leafNodeChildren;
        }

        @Override
        boolean isInner() {
            return true;
        }

        @Override
        public Iterator<T> iterator() {
            var innerNodes = this.innerNodeChildren.iterator();
            var leafNodes = this.leafNodeChildren.iterator();
            var innerDone = new AtomicBoolean(false);
            var leafDone = new AtomicBoolean(false);
            var ref = new AtomicReference<Iterator<T>>(null);
            if (innerNodes.hasNext())
                ref.set(innerNodes.next().iterator());
            else if (leafNodes.hasNext())
                ref.set(leafNodes.next().iterator());

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    if (ref.get() == null)
                        return false;

                    if (ref.get().hasNext())
                        return true;

                    if (!innerDone.get() && innerNodes.hasNext()) {
                        ref.set(innerNodes.next().iterator());
                        return true;
                    } else {
                        innerDone.set(true);
                        if (leafNodes.hasNext()) {
                            ref.set(leafNodes.next().iterator());
                            return true;
                        } else {
                            leafDone.set(true);
                            ref.set(null);
                            return false;
                        }
                    }
                }

                @Override
                public T next() {
                    return ref.get().next();

                }
            };
        }

        @Override
        <R> R map(Function<T, R> leafMapper, BiFunction<S, List<R>, R> innerNodeMapper) {
            var mappedNodes = this.innerNodeChildren.stream().map(x -> x.map(leafMapper, innerNodeMapper))
                    .collect(Collectors.toList());
            mappedNodes
                    .addAll(leafNodeChildren.stream().map(x -> leafMapper.apply(x.value)).collect(Collectors.toList()));
            return innerNodeMapper.apply(this.getSegment(), mappedNodes);
        }
    }
}
