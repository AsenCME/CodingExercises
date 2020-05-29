package pgdp.streams;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Penguin {
  private List<Geo> locations;
  private String trackID;

  public Penguin(List<Geo> locations, String trackID) {
    this.locations = locations;
    this.trackID = trackID;
  }

  @Override
  public String toString() {
    return "Penguin{" + "locations=" + locations + ", trackID='" + trackID + '\'' + '}';
  }

  public List<Geo> getLocations() {
    return locations;
  }

  public String getTrackID() {
    return trackID;
  }

  public String toStringUsingStreams() {
    Comparator<Geo> compareLocations = new Comparator<Geo>() {
      @Override
      public int compare(Geo g1, Geo g2) {
        int comparisonLat = Double.compare(g2.latitude, g1.latitude);
        if (comparisonLat != 0)
          return comparisonLat;
        return Double.compare(g2.longitude, g1.longitude);
      }
    };
    String locString = locations.stream().sorted(compareLocations).map(g -> g.toString())
        .collect(Collectors.joining(", "));
    return String.format("Penguin{locations=[%s], trackID='%s'}", locString, trackID);
  }
}
