package tileengine;

import java.awt.Color;
import java.util.Random;
import static utils.RandomUtils.uniform;
/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static TETile AVATAR = new TETile('☕', new Color(71, 101, 166), Color.black, "you");
    public static TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile LIGHTED_FLOOR = new TETile('·', new Color(236, 127, 87), Color.white,
            "floor");
    public static final TETile TEST_FLOOR = new TETile('❀', new Color(71, 101, 166), Color.white,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static TETile LAMP = new TETile('◎', Color.green, Color.black, "lamp");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");

    private Random seed;
    public Tileset(Random seed) {
        this.seed = seed;
    }

    public static void resetColors() {
        AVATAR = new TETile(AVATAR.getCharacter(), new Color(71, 101, 166), Color.black, "you");
        WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
                "wall");
        FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
                "floor");
        LAMP = new TETile('◎', Color.green, Color.black, "lamp");
    }

    public void randomTheme() {
        Tileset.FLOOR = randTile(FLOOR);
        Tileset.WALL = randTile(WALL);
        Tileset.LAMP = new TETile('◎', randColor(),
                FLOOR.getBackgroundColor(), "lamp");
        Tileset.AVATAR = randAvatar();
    }

    private Color randColor() {
       return new Color(uniform(seed, 0, 255), uniform(seed, 0, 255), uniform(seed, 0, 255));
    }
    private TETile randTile(TETile t) {
        return new TETile(
                t.character(),
                new Color(uniform(seed, 0, 255), uniform(seed, 0, 255), uniform(seed, 0, 255)),
                new Color(uniform(seed, 0, 255), uniform(seed, 0, 255), uniform(seed, 0, 255)),
                t.description());
    }
    private TETile randAvatar() {
        return new TETile(
                AVATAR.character(),
                new Color(uniform(seed, 0, 255), uniform(seed, 0, 255), uniform(seed, 0, 255)),
                FLOOR.getBackgroundColor(),
                AVATAR.description());
    }
}



