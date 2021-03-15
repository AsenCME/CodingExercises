package pgdp.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PinguTextCollection {
    private long nextId = 0;
    private final HashMap<Long, PinguText> cache = new HashMap<>();

    public PinguTextCollection() {
    }

    public PinguText add(String title, String author, String text) {
        var obj = new PinguText(nextId, title, author, text);
        cache.putIfAbsent(nextId, obj);
        nextId++;
        return obj;
    }

    public PinguText findById(long id) {
        return cache.getOrDefault(id, null);
    }

    public List<PinguText> getAll() {
        return cache.values().stream().sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .collect(Collectors.toList());
    }

    public Map<PinguText, Double> findPlagiarismFor(long id) {
        var user = findById(id);
        if (user == null)
            return null;

        var res = new HashMap<PinguText, Double>();
        for (var other : cache.values()) {
            if (other.getId() == id)
                continue;
            var sim = user.computeSimilarity(other);
            if (sim >= 0.001)
                res.putIfAbsent(other, sim);
        }
        return res;
    }
}
