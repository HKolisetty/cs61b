package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import org.knowm.xchart.XYChart;
import plotting.Plotter;

import java.lang.reflect.Array;
import java.util.*;

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

        for (HashSet i : lists) {
            list.retainAll(i);
        }
        ArrayList<String> returnList = new ArrayList<>();
        returnList.addAll(list);
        if (q.k() > 0) {
            ArrayList<String> topList = new ArrayList<>();
            ArrayList<Integer> topReturns = new ArrayList<>();
            for (String word : returnList) {
                int count = map.countHistory(word, q.startYear(), q.endYear()).size();
                if (topList.size() < q.k()) {
                    topList.add(word);
                    topReturns.add(count);
                } else {
                    int minValue = Collections.min(topReturns);
                    if (count > minValue) {
                        topList.remove(topReturns.indexOf(minValue));
                        topReturns.add(count);
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
