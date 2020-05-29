package pgdp.adventuin;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pgdp.color.RgbColor;

public final class AdventuinParty {

    public static Map<HatType, List<Adventuin>> groupByHatType(List<Adventuin> party) {
        // Use Collectors to group by HatType
        return party.stream().collect(Collectors.groupingBy(Adventuin::getHatType));
    }

    public static void printLocalizedChristmasGreetings(List<Adventuin> party) {
        // Sort by height and print greeting
        party.stream().sorted(Adventuin::compareByHeight)
                .forEach(p -> System.out.println(p.getLanguage().getLocalizedChristmasGreeting(p.getName())));
    }

    public static Map<HatType, List<Adventuin>> getAdventuinsWithLongestNamesByHatType(List<Adventuin> party) {
        return groupByHatType(party).entrySet().stream().map(group -> {

            // Getting max length of name
            // Then I'm filtering by it
            // Then I return a simple entry, that I use to build the map in collectors
            int maxLen = group.getValue().stream().map(Adventuin::getName).mapToInt(String::length).max().getAsInt();
            List<Adventuin> value = group.getValue().stream().filter(p -> p.getName().length() == maxLen)
                    .collect(Collectors.toList());

            return new AbstractMap.SimpleEntry<HatType, List<Adventuin>>(group.getKey(), value);

        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<Integer, Double> getAverageColorBrightnessByHeight(List<Adventuin> party) {

        // Custom helper class to hold id of group and brightness
        class Helper {
            int id;
            double brightness;

            public Helper(int _id, double _b) {
                id = _id;
                brightness = _b;
            }
        }

        // Map the list from penguins to helper
        // Group id is 100 for all penguins between 95 and 104
        // Brightness is also individually calculated
        return party.stream().map(p -> {

            int id = Math.round((float) p.getHeight() / 10) * 10;
            RgbColor color = p.getColor().toRgbColor8Bit();
            double brightness = (0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue()) / 255;
            return new Helper(id, brightness);

        }).collect(Collectors.groupingBy(h -> h.id)).entrySet().stream().map(entry -> {
            // Here I group them by the id I created
            // And get the average brightness for each group
            // And create a simple map entry that I later use to build the map

            double avgBrightness = entry.getValue().stream().mapToDouble(helper -> helper.brightness).average()
                    .getAsDouble();
            return new AbstractMap.SimpleEntry<Integer, Double>(entry.getKey(), avgBrightness);

        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<HatType, Double> getDiffOfAvgHeightDiffsToPredecessorByHatType(List<Adventuin> party) {
        return groupByHatType(party).entrySet().stream().map(entry -> {
            // Already grouped, so I start work
            // New list holding all penguins with the same hat that I wanna dance

            HatType key = entry.getKey();
            List<Adventuin> list = entry.getValue();

            // Contains average differences for this group for negative and positive values
            Map<Integer, Double> diffs = list.stream().map(p -> {
                // Compare in pairs
                // I HAD to use list indecies here, otherwise I can't get the next one
                // Get the next, if the overflows, get the first
                // Get height difference and modify the stream

                int index = list.indexOf(p);
                Adventuin other = null;
                if (index + 1 >= list.size())
                    other = list.get(0);
                else
                    other = list.get(index + 1);
                return p.getHeight() - other.getHeight();

            }).collect(Collectors.groupingBy((Integer p) -> {
                // Group by the difference
                // If it is negative, id is -1
                // Positive - 1
                // Otherwise - 0

                int value = p.intValue();
                if (value < 0)
                    return -1;
                else if (value > 0)
                    return 1;
                return 0;

            })).entrySet().stream().map(diff -> {
                // We have a Map<Integer, List<Intger>>
                // Reduce the list to am averge value
                // Create a simple entry of Integer, Double
                // Use entry to build map

                double avgDiffInGroup = diff.getValue().stream().mapToDouble(i -> i).average().getAsDouble();
                return new AbstractMap.SimpleEntry<Integer, Double>(diff.getKey(), avgDiffInGroup);

            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            // Create a new simple map entry
            // Key is HatType
            // Value is the absolute difference between negative and positive averages
            return new AbstractMap.SimpleEntry<HatType, Double>(key, Math.abs(diffs.get(-1) - diffs.get(1)));

        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
