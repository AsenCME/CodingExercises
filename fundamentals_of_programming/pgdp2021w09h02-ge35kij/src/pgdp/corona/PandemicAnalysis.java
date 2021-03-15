package pgdp.corona;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PandemicAnalysis {

    public static void main(String[] args) {
        String filename = "RKI_COVID19.csv";
        Dataset dataset = new Dataset("dataset/" + filename);
        var res = totalCasesBySexByAgeGroup(dataset.stream().filter(x -> x.getState().equals("Bayern")),
                Entry::getDeath);
        System.out.println(res);

        // dataset.stream().forEach(System.out::println);
    }

    public static int totalCases(Stream<Entry> entryStream, CaseReport.Getter getter) {
        return entryStream.mapToInt(x -> getter.get(x).count).sum();
    }

    public static int newCases(Stream<Entry> entryStream, CaseReport.Getter getter) {
        return entryStream.map(x -> getter.get(x))
                .filter(x -> x.type == CaseReport.Type.NEW || x.type == CaseReport.Type.CORRECTION)
                .mapToInt(x -> x.count).sum();
    }

    public static int activeInfections(Stream<Entry> entryStream) {
        return entryStream.mapToInt(x -> x.getInfection().count - (x.getDeath().count + x.getRecovery().count)).sum();
    }

    public static List<String> safestStates(Stream<Entry> entryStream) {
        var groups = entryStream.collect(Collectors.groupingBy(x -> x.getState(),
                Collectors.summingInt(x -> x.getInfection().count - (x.getDeath().count + x.getRecovery().count))));
        return groups.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(x -> x.getKey())
                .collect(Collectors.toList());
    }

    public static LocalDate firstDate(Stream<Entry> entryStream) {
        return entryStream.map(x -> x.getReportingDate()).sorted().findFirst().orElse(null);
    }

    private static LocalDate lastDate(Stream<Entry> entryStream) {
        return entryStream.map(x -> x.getReportingDate()).sorted((a, b) -> b.compareTo(a)).findFirst().orElse(null);
    }

    public static Map<LocalDate, Integer> newInfectionsByDate(Stream<Entry> entryStream) {
        return entryStream.collect(Collectors.groupingBy(x -> x.getReportingDate(),
                Collectors.summingInt(x -> x.getInfection().type == CaseReport.Type.NEW ? x.getInfection().count : 0)));
    }

    public static int[] dailyNewInfections(Stream<Entry> entryStream) {
        var list = entryStream.collect(Collectors.toList());
        var firstDate = firstDate(list.stream());
        var lastDate = lastDate(list.stream());
        if (firstDate == null || lastDate == null)
            return null;
        return firstDate.datesUntil(lastDate).mapToInt(x -> list.stream()
                .filter(entry -> entry.getReportingDate().equals(x) && entry.getInfection().type == CaseReport.Type.NEW)
                .mapToInt(entry -> entry.getInfection().count).sum()).toArray();
    }

    public static double avgDailyNewInfections(Stream<Entry> entryStream) {
        var arr = dailyNewInfections(entryStream);
        if (arr == null)
            return 0;
        return IntStream.of(arr).average().orElse(0);
    }

    public static Map<String, Map<Character, Integer>> totalCasesBySexByAgeGroup(Stream<Entry> entryStream,
            CaseReport.Getter getter) {
        return entryStream.collect(Collectors.groupingBy(x -> x.getAgeGroup(),
                Collectors.groupingBy(x -> x.getSex(), Collectors.summingInt(x -> getter.get(x).count))));
    }
}
