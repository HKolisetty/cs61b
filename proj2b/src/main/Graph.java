package main;
import edu.princeton.cs.algs4.In;

import java.util.HashSet;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
    private TreeMap<Integer, String> intToWord;
    private TreeMap<Integer, ArrayList<Integer>> adjList;
    public TreeMap<String, ArrayList<Integer>> dupes;
    public Graph(String synsetsFileName, String hyponymsFileName) {
        intToWord = new TreeMap<>();
        adjList = new TreeMap<>();
        dupes = new TreeMap<>();
        In synsetsFile = new In(synsetsFileName);
        while (!synsetsFile.isEmpty()) {
            String nextLine = synsetsFile.readLine();
            String[] splitLine = nextLine.split(",");
            int id = Integer.parseInt(splitLine[0]);
            String word = splitLine[1];
            if (!intToWord.containsKey(id)) {
                intToWord.put(id, word);
                adjList.put(id, new ArrayList<>());
            }
            String[] wordlist = word.split("\\s+");
            for (String i : wordlist) {
                if (!dupes.containsKey(i)) {
                    dupes.put(i, new ArrayList<>());
                }
                dupes.get(i).add(id);
            }
        }
        In hyponymsFile = new In(hyponymsFileName);
        while (!hyponymsFile.isEmpty()) {
            String nextLine = hyponymsFile.readLine();
            int[] splitLine = Arrays.stream(nextLine.split(",")).mapToInt(Integer::parseInt).toArray();
            for (int i : Arrays.copyOfRange(splitLine, 1, splitLine.length)) {
                adjList.get(splitLine[0]).add(i);
            }
        }
    }

    public String getWord(int i) {
        return intToWord.get(i);
    }

    public HashSet<String> getChildren(int i) {
        HashSet<String> list = new HashSet<>();
        getChildrenHelper(list, i);
        return list;
    }

    private void getChildrenHelper(HashSet<String> list, int i) {
        String[] wordList = getWord(i).split("\\s+");
        list.addAll(Arrays.asList(wordList));
        if (adjList.get(i) != null) {
            for (int child : adjList.get(i)) {
                getChildrenHelper(list, child);
            }
        }
    }
}
