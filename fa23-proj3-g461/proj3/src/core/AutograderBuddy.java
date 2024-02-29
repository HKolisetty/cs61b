package core;

import core.worldgen.terrain.Terrain;
import tileengine.TETile;
import tileengine.Tileset;

import static core.persistence.WorldLoader.*;
import static core.persistence.WorldSaver.*;
import static tileengine.Tileset.AVATAR;

public class AutograderBuddy {
    static final int WIDTH = 80;
    static final int HEIGHT = 45;

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {
        TETile[][] tiles;

        if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
            Long seed = processSeed(input);
            Terrain world = new Terrain(WIDTH, HEIGHT, seed);
            tiles = world.tiles();
            processMoveString(world, input);
            save(input);
            return tiles;
        } else if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
            Terrain world = getSavedTerrain();
            processSavedMoves(world);
            tiles = world.tiles();
            processMoveString(world, input);
            save(input);
            return tiles;
        } else {
            return null;
        }
    }

    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.LIGHTED_FLOOR.character()
                || t.character() == AVATAR.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
