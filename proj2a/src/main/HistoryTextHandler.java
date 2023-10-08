package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    
    private NGramMap map;

    public HistoryTextHandler(NGramMap ngm) {
        this.map = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        String response = "";
        for (String i : words) {
            response += i + ": " + map.weightHistory(i, startYear, endYear) + "\n";
        }
        return response;
    }
}
