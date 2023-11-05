package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

import static com.google.common.truth.Truth.assertThat;

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
            ArrayList<Integer> topReturns = new ArrayList<>();
            for (String word : returnList) {
                double count;
                if (q.startYear() == 0 || q.endYear() == 0) {
                    TimeSeries ts = map.countHistory(word);
                    count = ts.data().stream().reduce(0.0, Double::sum);
                } else {
                    TimeSeries ts = map.countHistory(word, q.startYear(), q.endYear());
                    count = ts.data().stream().reduce(0.0, Double::sum);
                }
//                System.out.println("word: " + word + "\t" + count);
                if (topList.size() < q.k() && count > 0) {
                    topList.add(word);
                    topReturns.add((int) count);
                } else {
                    int minValue = 0;
                    if (topList.size() != 0) {
                        minValue = Collections.min(topReturns);
                    }
                    if (topList.size() >= q.k() && count > minValue && count > 0) {
                        topList.remove(topReturns.indexOf(minValue));
                        topReturns.remove((Integer) minValue);
                        topReturns.add((int) count);
                        topList.add(word);
                    }
                }

            }
            returnList = topList;
        }

        Collections.sort(returnList);

        return "[" + String.join(", ", returnList) + "]";
    }
}
