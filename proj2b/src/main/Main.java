package main;

import browser.NgordnetServer;
import ngrams.NGramMap;

public class Main {
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();

        String wordFile = "./data/ngrams/top_49887_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";
        NGramMap ngm = new NGramMap(wordFile, countFile);

        String synsets = "./data/wordnet/synsets.txt";
        String hyponyms = "./data/wordnet/hyponyms.txt";
        Graph graph = new Graph(synsets, hyponyms);

        hns.startUp();
        hns.register("history", new HistoryHandler(ngm));
        hns.register("historytext", new HistoryTextHandler(ngm));
        hns.register("hyponyms", new HyponymsHandler(ngm, graph));

        System.out.println("Finished server startup! Visit http://localhost:4567/ngordnet.html");
    }
}
