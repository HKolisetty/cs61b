package core.persistence;

import core.gameplay.Avatar;
import core.gameplay.Game;
import core.worldgen.terrain.RandomTerrain;
import core.worldgen.terrain.Terrain;
import core.worldgen.terrain.TutorialTerrain;
import tileengine.TETile;

import static java.lang.Long.parseLong;
import static utils.FileUtils.readFile;

public class WorldLoader {
    static final int WIDTH = Game.WIDTH;
    static final int HEIGHT = Game.HEIGHT;
    public static void processSavedMoves(Terrain savedTerrain) {
        String savedWorld = readFile("./savedworld.txt");
        processMoveString(savedTerrain, savedWorld);
    }

    public static Terrain getSavedTerrain() {
        String savedWorld = readFile("./savedworld.txt");
        Long seed = processSeed(savedWorld);
        Terrain world = new Terrain(WIDTH, HEIGHT, seed);
        return world;
    }

    public static Terrain getSavedTutorial() {
        String savedWorld = readFile("./savedworld.txt");
        Long seed = processSeed(savedWorld);
        Terrain world = new TutorialTerrain(WIDTH, HEIGHT, seed);
        return world;
    }

    public static Terrain getSavedRandom() {
        String savedWorld = readFile("./savedworld.txt");
        Long seed = processSeed(savedWorld);
        Terrain world = new RandomTerrain(WIDTH, HEIGHT, seed);
        return world;
    }

    public static Long processSeed(String input) {
        String seed = "";
        int counter = 0;
        
        while (counter < input.length()
                && !isNumber(String.valueOf(input.charAt(counter)))) {
            counter++;
        }

        while (counter < input.length()
                && isNumber(String.valueOf(input.charAt(counter)))) {
            seed = seed + input.charAt(counter);
            counter++;
        }
        return parseLong(seed);
    }

    public static TETile[][] processMoveString(Terrain world, String input) {
        Avatar avatar = world.avatar();

        int counter = 0;

        if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
            while (input.charAt(counter) != 'S' && input.charAt(counter) != 's') {
                counter++;
            }
            counter++;
        }

        while (counter < input.length() && input.charAt(counter) != ':') {
            processMove(world, input.charAt(counter));
            counter++;
        }
        return world.tiles();
    }

    public static void processMove(Terrain world, char move) {
        Avatar avatar = world.avatar();
        if (move == 'w') {
            avatar.move(0, 1);
        }
        if (move == 'a') {
            avatar.move(-1, 0);
        }
        if (move == 's') {
            avatar.move(0, -1);
        }
        if (move == 'd') {
            avatar.move(1, 0);
        }
        if (move == 'i') {
            world.processLampToggle();
        }
    }

    public static boolean isNumber(String str) {
        if (str.isEmpty()) {
            return false;
        }
        try {
            int i = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
