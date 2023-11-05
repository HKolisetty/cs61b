package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class HyponymsHandler extends NgordnetQueryHandler {
    private NGramMap map;
    private Graph graph;

    public HyponymsHandler(NGramMap ngm, Graph graph) {
        this.map = ngm;
        this.graph = graph;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        HashSet<String> list = new HashSet<>();
        HashSet<HashSet<String>> lists = new HashSet<>();

        for (String word : words) {
            HashSet<String> wordList = new HashSet<>();
            HashSet<HashSet<String>> wordLists = new HashSet<>();
            for (int id : graph.dupes.get(word)) {
                wordLists.add(graph.getChildren(id));
            }
            for (HashSet<String> i : wordLists) {
                wordList.addAll(i);
            }
            lists.add(wordList);
            list = wordList;
        }

        for (HashSet<String> i : lists) {
            list.retainAll(i);
        }
        ArrayList<String> returnList = new ArrayList<>(list);
        if (q.k() > 0) {
            ArrayList<String> topList = new ArrayList<>();
            ArrayList<Double> topReturns = new ArrayList<>();
            HashMap<String, Double> hi = new HashMap<>();
            for (String word : returnList) {
                double count;
                if (q.startYear() == 0 || q.endYear() == 0) {
                    TimeSeries ts = map.countHistory(word);
                    count = ts.data().stream().reduce(0.0, Double::sum);
                } else {
                    TimeSeries ts = map.countHistory(word, q.startYear(), q.endYear());
                    count = ts.data().stream().reduce(0.0, Double::sum);
                }
                if (count > 0) {
                    if (topList.size() < q.k()) {
                        topList.add(word);
                        topReturns.add(count);
                    } else {
                        double minValue = Collections.min(topReturns);
                        if (count > minValue) {
                            topList.add(word);
                            topReturns.add(count);
                            topList.remove(topReturns.indexOf(minValue));
                            topReturns.remove(topReturns.indexOf(minValue));
                        }
                    }
                }
            }
            returnList = topList;
        }

        Collections.sort(returnList);

        return "[" + String.join(", ", returnList) + "]";

    }
}
