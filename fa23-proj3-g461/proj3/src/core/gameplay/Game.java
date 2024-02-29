package core.gameplay;

import core.worldgen.terrain.*;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.*;
import utils.FileUtils;

import java.awt.*;

import static core.persistence.WorldLoader.*;
import static edu.princeton.cs.algs4.StdDraw.*;
import static java.awt.Color.white;
import static java.lang.Long.parseLong;
import static java.lang.Math.floor;
import static tileengine.Tileset.NOTHING;

public class Game {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 45;
    TERenderer ter;
    Long seed;
    Terrain world;
    String seedString;
    String savedWorld;
    boolean readyToTerminate = false;

    public Game() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        displayGame();
    }

    public void displayGame() {
        seedString = displayMenu();
        savedWorld = savedWorld + seedString + "S";
        generateWorld(seedString);
        gameLoop();
    }

    public void displayLoadedGame(String contents) {
        if (contents.contains("BB")) {
            world = getSavedRandom();
        } else if (contents.contains("TT")) {
            world = getSavedTutorial();
        } else {
            world = getSavedTerrain();
        }
        processMoveString(world, contents);
        gameLoop();
    }

    public String hoveredTileDescription() {
        TETile tile = hoveredTile();
        String tileName = tile.description();
        return tileName;
    }

    public TETile hoveredTile() {
        int x = (int) floor(mouseX());
        int y = (int) floor(mouseY());

        if (x > 0 && y > 0 && x < WIDTH && y < HEIGHT) {
            TETile tile = world.tiles[x][y];
            return tile;
        }
        return NOTHING;
    }

    public void processText() {
        setPenColor(white);
        text(WIDTH - 2, HEIGHT - 1, hoveredTileDescription());
        setPenColor();
    }

    public  void gameLoop() {
        while (true) {
            clear(Color.BLACK);
            ter.drawTiles(world.tiles());
            processText();
            processKeys();
            show();
        }
    }

    public  void processKeys() {
        Avatar avatar = world.avatar();
        if (hasNextKeyTyped()) {
            char key = nextKeyTyped();
            if (key == ':') {
                readyToTerminate = true;
            }
            if ((key == 'q' || key == 'Q') && readyToTerminate) {
                saveGame();
                quit();
            }
            if (key == 'w') {
                avatar.move(0, 1);
                savedWorld = savedWorld + key;
            }
            if (key == 'a') {
                avatar.move(-1, 0);
                savedWorld = savedWorld + key;
            }
            if (key == 's') {
                avatar.move(0, -1);
                savedWorld = savedWorld + key;
            }
            if (key == 'd') {
                avatar.move(1, 0);
                savedWorld = savedWorld + key;
            }
            if (key == 'i') {
                world.processLampToggle();
                savedWorld = savedWorld + key;
            }
            // user wants to render a new world
            if (key == 'r') {
                displayGame();
            }
        }
    }

    public void saveGame() {
        if (FileUtils.fileExists("./savedworld.txt")) {
            String oldWorld = FileUtils.readFile("./savedworld.txt");
            if (savedWorld.charAt(0) == 'L') {
                savedWorld = oldWorld + savedWorld;
            }
        }
        FileUtils.writeFile("./savedworld.txt", savedWorld);
    }

    public void generateWorld(String seedStr) {
        if (isTutorial(seedStr)) {
            seedStr = processed(seedStr);
            seed = parseLong(seedStr);
            world = new TutorialTerrain(WIDTH, HEIGHT, seed);
        } else if (isRandom(seedStr)) {
            seedStr = processed(seedStr);
            seed = parseLong(seedStr);
            world = new RandomTerrain(WIDTH, HEIGHT, seed);
        } else {
            seed = parseLong(seedStr);
            world = new Terrain(WIDTH, HEIGHT, seed);
        }
    }

    public void updatePrefix(String prefix) {
        savedWorld = prefix;
        Tileset.resetColors();
        clear(white);
    }

    public void loadMenuInterface() {
        clear(new Color(135, 88, 150));
        Font font = new Font("Arial", Font.BOLD, 50);
        StdDraw.setFont(font);
        text(WIDTH / 2, HEIGHT - 10, "David's World");
        setFont(new Font("Sans Serif", Font.BOLD, 20));
        text(WIDTH / 2, HEIGHT / 2, "Press N for a new Game");
        text(WIDTH / 2, HEIGHT / 2 - 3, "Press Q for quit");
        text(WIDTH / 2, HEIGHT / 2 - 5, "Press T for tutorial (61B Normal World-Gen)");
        text(WIDTH / 2, HEIGHT / 2 - 7, "Press L to load previous world");
        text(WIDTH / 2, HEIGHT / 2 - 9, "Press B for random colors");
        setFont(new Font("Sans Serif", Font.BOLD, 15));
    }

    public String displayMenu() {
        loadMenuInterface();
        show();

        while (true) {
            show();
            if (hasNextKeyTyped()) {
                char key = nextKeyTyped();
                if (key == 'n' || key == 'N') {
                    updatePrefix("N");
                    return SeedEntry.seedEntryPage();
                } else if (key == 'q' || key == 'Q') {
                    Game.quit();
                } else if (key == 't' || key == 'T') {
                    updatePrefix("TT");
                    return SeedEntry.seedEntryTutorialPage();
                } else if (key == 'b' || key == 'B') {
                    updatePrefix("BB");
                    return SeedEntry.seedEntryRandomPage();
                } else if (key == 'l' || key == 'L') {
                    if (FileUtils.fileExists("./savedworld.txt")) {
                        updatePrefix("L");
                        displayLoadedGame(FileUtils.readFile("./savedworld.txt"));
                    }
                }
            }
        }
    }

    // check if character is a number
    public static boolean isNumber(char charNum) {
        String strNum = String.valueOf(charNum);
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void quit() {
        System.exit(0);
    }

    public static boolean isTutorial(String seedString) {
        if (seedString.contains("T")) {
            return true;
        }
        return false;
    }

    public static boolean isRandom(String seedString) {
        if (seedString.contains("B")) {
            return true;
        }
        return false;
    }

    public static String processed(String seedString) {
        seedString = seedString.substring(0, seedString.length() - 1);
        return seedString;
    }
}
