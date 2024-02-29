package core.gameplay;

import java.awt.*;

import static edu.princeton.cs.algs4.StdDraw.*;
import static edu.princeton.cs.algs4.StdDraw.text;

public class SeedEntry {
    public static String seedEntryPage() {
        clear(new Color(79, 75, 75));
        text(Game.WIDTH /2, Game.HEIGHT /2, "Enter a random number.. when you are done," +
                "press S");
        text(Game.WIDTH /2, Game.HEIGHT /2 - 10, "once you are in the world press R to return to Menu");
        text(Game.WIDTH /2, Game.HEIGHT /2 - 13, "if you encounter a light, press i");

        boolean startGame = false;
        String seedString = "";

        while (!startGame) {
            show();

            if (hasNextKeyTyped()) {
                char typed = nextKeyTyped();

                // start game if user types s with a non-empty seed
                if ((typed == 's' || typed == 'S') && seedString != "") {
                    startGame = true;
                    clear();
                    return seedString;
                }

                // concatenates number to seed
                else if (Game.isNumber(typed)) {
                    if (seedString.length() < 16) {
                        seedString = seedString + typed;
                        renderSeed(seedString);
                    }
                }

                // backspace
                else if (typed == '\b') {
                    if (seedString != "") {
                        seedString = seedString.substring(0, seedString.length() - 1);
                        renderSeed(seedString);
                    }
                }

                else if (typed == 'q' || typed == 'Q') {
                    Game.quit();
                }
            }
        }
        return seedString;
    }

    public static void renderSeed(String seedString) {
        clear(new Color(79, 75, 75));
        text(Game.WIDTH /2, Game.HEIGHT /2, "Enter a random number.. when you are done," +
                "press S");
        text(Game.WIDTH /2, Game.HEIGHT /2 - 5, seedString);
        text(Game.WIDTH /2, Game.HEIGHT /2 - 10, "once you are in the world press R to return to Menu");
        text(Game.WIDTH /2, Game.HEIGHT /2 - 13, "if you encounter a light, press i");
    }

    public static String seedEntryTutorialPage() {
        String seed = seedEntryPage();
        return seed + "T";
    }

    public static String seedEntryRandomPage() {
        String seed = seedEntryPage();
        return seed + "B";
    }
}
