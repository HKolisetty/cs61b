package core.persistence;

import core.gameplay.Game;
import core.worldgen.terrain.RandomTerrain;
import core.worldgen.terrain.Terrain;
import core.worldgen.terrain.TutorialTerrain;
import tileengine.TETile;
import utils.FileUtils;

import static java.lang.Long.parseLong;
import static utils.FileUtils.readFile;

public class WorldSaver {
    static final int WIDTH = Game.WIDTH;
    static final int HEIGHT = Game.HEIGHT;
    Game game;
    public static void save(String input) {
        if (input.contains(":Q")) {
            saveWorld(input);
        }
    }

    public static void saveWorld(String savedWorld) {
        if (FileUtils.fileExists("./savedworld.txt")) {
            String oldWorld = FileUtils.readFile("./savedworld.txt");

            if (savedWorld.charAt(0) == 'L' || savedWorld.charAt(0) == 'l') {
                savedWorld = oldWorld + savedWorld;
            }
        }
        FileUtils.writeFile("./savedworld.txt", savedWorld);
    }
}
