package pgdp.adventuin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Kranzuin {
    public static final Kranzuin JULIAN = new Kranzuin();

    private Kranzuin() {
    };

    public int beantworteFragen(List<String> questions) {
        // Check if questions is empty
        if (questions.isEmpty())
            return Integer.MAX_VALUE;

        // Find relevant questions
        List<String> relevantQuestions = questions.stream().filter(x -> {
            if (x.contains("Blätter") || x.contains("Aufgaben") || x.contains("Anzahl"))
                return true;
            return false;
        }).collect(Collectors.toList());

        // Check if there are relevant questions
        if (relevantQuestions.isEmpty())
            return 15 + questions.stream().mapToInt(x -> x.length()).max().getAsInt();

        // Filter by relevant question containing numbers
        List<String> relevantQuestionsWithNumbers = relevantQuestions.stream().filter(x -> x.matches(".*\\d.*"))
                .collect(Collectors.toList());

        // If relevant questions do not contain any nymbers
        if (relevantQuestionsWithNumbers.isEmpty()) {
            return ThreadLocalRandom.current().ints(15, Integer.MAX_VALUE + 1).filter(randomNumber -> {
                return relevantQuestions.stream().mapToInt(x -> x.length()).anyMatch(len -> {
                    return randomNumber % len == 0;
                });
            }).findFirst().getAsInt();
        }

        // Map all questions to integers
        List<Integer> numbers = relevantQuestionsWithNumbers.stream().map(question -> {
            List<Integer> currentNums = Stream.of(question.split(" ")).filter(x -> x.matches(".*\\d.*"))
                    .map(x -> Integer.parseInt(x.replaceAll("\\D", ""))).collect(Collectors.toList());
            int min = currentNums.stream().min(Integer::compare).get();
            int max = currentNums.stream().max(Integer::compare).get();
            return (min + max) / 2;
        }).collect(Collectors.toList());

        // If all numbers are bigger than 14, then return the smallest of them
        if (numbers.stream().allMatch(x -> x > 14))
            return numbers.stream().min(Integer::compare).get();

        // If not all numbers are bigger than 14,
        // then there is at least one number that is smaller than 14
        // and we therefore output this
        return 1783;
    }
}