package pgdp.streams;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataScience {
  public static Stream<Penguin> getDataByTrackId(Stream<PenguinData> stream) {
    return stream.collect(Collectors.groupingBy(PenguinData::getTrackID)).entrySet().stream().map(entry -> {
      List<Geo> locations = entry.getValue().stream().map(data -> {
        return data.geom;
      }).collect(Collectors.toList());
      return new Penguin(locations, entry.getKey());
    });
  }

  public static void outputPenguinStream() {
    List<Penguin> data = getDataByTrackId(CSVReading.processInputFile())
        .sorted(Comparator.comparing(Penguin::getTrackID)).collect(Collectors.toList());
    Supplier<Stream<Penguin>> supplier = () -> data.stream();

    long count = supplier.get().count();
    String result = supplier.get().map(p -> p.toStringUsingStreams()).reduce("", (total, next) -> total += next + '\n');

    System.out.println(count);
    System.out.println(result);
  }

  public static void outputLocationRangePerTrackid(Stream<PenguinData> stream) {
    stream.collect(Collectors.groupingBy(PenguinData::getTrackID)).entrySet().stream()
        .sorted(Comparator.comparing(Entry::getKey)).forEach(entry -> {
          double minLong = entry.getValue().stream().mapToDouble(d -> d.geom.longitude).min().getAsDouble();
          double maxLong = entry.getValue().stream().mapToDouble(d -> d.geom.longitude).max().getAsDouble();
          double avgLong = entry.getValue().stream().mapToDouble(d -> d.geom.longitude).average().getAsDouble();

          double minLat = entry.getValue().stream().mapToDouble(d -> d.geom.latitude).min().getAsDouble();
          double maxLat = entry.getValue().stream().mapToDouble(d -> d.geom.latitude).max().getAsDouble();
          double avgLat = entry.getValue().stream().mapToDouble(d -> d.geom.latitude).average().getAsDouble();

          System.out.println(entry.getKey());
          System.out.println("Min Longitude: " + minLong + " Max Longitude: " + maxLong + " Avg Longitude: " + avgLong
              + " Min Latitude: " + minLat + " Max Latitude: " + maxLat + " Avg Latitude: " + avgLat);
        });
  }

  public static void main(String[] args) {
    outputLocationRangePerTrackid(CSVReading.processInputFile());
  }
}