package pgdp.collections;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PenguinColony {

    private HashSet<Penguin> penguins;

    public PenguinColony(HashSet<Penguin> penguins) {
        this.penguins = penguins;
    }

    public HashSet<Penguin> getPenguins() {
        return penguins;
    }

    public void setPenguins(HashSet<Penguin> penguins) {
        this.penguins = penguins;
    }

    public void uniteColonies(PenguinColony otherColony) {
        penguins.addAll(otherColony.getPenguins());
        otherColony.penguins.clear();
    }

    public PenguinColony splitColony(Predicate<? super Penguin> pred) {
        HashSet<Penguin> rejects = penguins.stream().filter(pred).collect(Collectors.toCollection(HashSet::new));
        penguins.removeAll(rejects);
        return new PenguinColony(rejects);
    }

    public Penguin findFirstFriend(LinkedList<Penguin> penguinFriends) {
        for (Penguin p : penguinFriends)
            if (penguins.contains(p))
                return p;
        return null;
    }

    public boolean canFeedPenguinsWithProperty(Predicate<? super Penguin> pred, Set<Fish> fishes) {
        return penguins.stream().filter(pred).allMatch((Penguin p) -> {
            return fishes.contains(p.getFavoriteFish());
        });
    }

    public int computeSum(Function<? super Penguin, Integer> fun) {
        return penguins.stream().map(fun).reduce(0, (sum, next) -> sum + next);
    }

}
