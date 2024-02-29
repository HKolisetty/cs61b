package core.worldgen.terrain;

import core.gameplay.Avatar;
import core.worldgen.wiring.Pixel;
import core.worldgen.wiring.PixelMap;
import core.worldgen.wiring.Rooms;
import core.worldgen.Field;
import core.worldgen.Halls;
import core.worldgen.Lamp;
import core.worldgen.Room;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static core.worldgen.Room.calculateCenter;
import static tileengine.Tileset.*;
import static utils.RandomUtils.uniform;

public class Terrain {
    public TETile[][] tiles;
    public Random seed;
    public PixelMap pixels;
    public Rooms rooms;
    public Halls halls;
    public HashMap<Integer, Lamp> lamps;
    public int globalWidth, globalHeight;
    public int sectionWidth, sectionHeight;

    int rows = 2, cols = 4;

    int numRooms;
    Avatar avatar;


    public Terrain(int width, int height, Long seed) {
        this.seed = new Random(seed);
        pixels = new PixelMap();
        rooms = new Rooms();
        halls = new Halls(this);
        lamps = new HashMap<>();

        globalWidth = width;
        globalHeight = height;
        sectionWidth = globalWidth / cols;
        sectionHeight = globalHeight / rows;

        numRooms = 0;
        generateTerrain();
    }

    public void generateTerrain() {
        fillCanvas();
        drawRooms();
        drawHalls();
        placeAvatar();
        generateLights();
    }

    public TETile[][] tiles() {
        return tiles;
    }

    public Avatar avatar() {return avatar;}

    public void placeAvatar() {
        Integer randInt = uniform(seed, 0, numRooms);
        Room room = rooms.map.get(randInt);
        Integer[] center = calculateCenter(room);
        tiles[center[0]][center[1]] = AVATAR;
        avatar = new Avatar(this, center[0], center[1]);
    }

    // fill the canvas with NOTHING to prevent NullPointerException
    public void fillCanvas() {
        tiles = new TETile[globalWidth][globalHeight];
        for (int x = 0; x < globalWidth; x++) {
            for (int y = 0; y < globalHeight; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    // divides our world into small sections and draws rooms for each section
    public void drawRooms() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {

                int leftBound = sectionWidth * i;
                int lowerBound = sectionHeight * j;

                drawRoomsInSection(lowerBound, leftBound);
            }
        }
    }

    // draws rooms in section, based on randomized parameters
    // there are parameters for room dimensions and number of rooms
    public void drawRoomsInSection(int lowerBound, int leftBound) {
        int numberOfRooms = uniform(seed,
                6, 8);
        int minWidth = 5, maxWidth = 8;
        int minHeight = 4, maxHeight = 9;
        int blurFactor = -4;

        for (int i = 0; i < numberOfRooms; i++) {
            int left = uniform(seed,
                    leftBound + 1,leftBound + sectionWidth + blurFactor);
            int bottom = uniform(seed,
                    lowerBound + 1, lowerBound + sectionHeight + blurFactor);

            int width = uniform(seed, minWidth, maxWidth);
            int height = uniform(seed, minHeight, maxHeight);

            Room room = new Room(bottom, left, width, height, this, numRooms);

            rooms.map.put(numRooms, room);
            room.drawRoom();
            numRooms++;
        }
    }

    // draw halls between disconnected rooms
    public void drawHalls() {
        int partition = uniform(seed, numRooms/4, 3*numRooms/4);

        for (int i = 0; i < partition; i++) {
            if (!rooms.connections.connected(0, i)) {
                rooms.connections.union(0, i);
                halls.drawHall(rooms.map.get(0), rooms.map.get(i));
            }
        }

        if (!rooms.connections.connected(numRooms - 1, 0)) {
            rooms.connections.union(numRooms - 1, 0);
            halls.drawHall(rooms.map.get(numRooms - 1), rooms.map.get(0));
        }

        for (int i = numRooms - 1; i >= partition; i--) {
            if (!rooms.connections.connected(numRooms - 1, i)) {
                rooms.connections.union(numRooms - 1, i);
                halls.drawHall(rooms.map.get(numRooms - 1), rooms.map.get(i));
            }
        }
    }

    public void generateLights() {
        List<Integer> locations = new ArrayList<>();
        for (int i = 0; i < numRooms/2; i++) {
            Integer randInt = uniform(seed, 0, numRooms);
            locations.add(randInt);
        }

        for (Integer r: locations) {
            Room room = rooms.map.get(r);
            Integer[] center = calculateCenter(room);

            if (tiles[center[0]][center[1]] != AVATAR) {
                if (!lampClumped(center)) {
                    tiles[center[0]][center[1]] = LAMP;
                    lamps.put(NSquaredToNBijection(center[0], center[1]),
                            new Lamp(center[0], center[1], this));
                    updateWorldPixels(LAMP, center[0], center[1]);
                }
            }
        }
    }

    public void updateWorldPixels(TETile tile, int x, int y) {
        Pixel pixel = pixels.getPixel(x, y);
        Field field = pixel.field;
        pixels.addPixel(new Pixel(tile, field, x, y));
    }

    public static int NSquaredToNBijection(int x, int y) {
        if (x >= y) {
            return x * x + x + y;
        } else {
            return x + y * y;
        }
    }

    public boolean lampClumped(Integer[] location) {
        int xPos = location[0];
        int yPos = location[1];

        for (int i = xPos - 7; i <= xPos + 7; i++) {
            for (int j = yPos - 7; j <= yPos + 7; j++) {
                if (i < globalWidth && j < globalHeight && i > 0 && j > 0
                        && tiles[i][j] == LAMP) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean playerNextToLamp() {
        for (int i = avatar.xPos() - 2; i <= avatar.xPos() + 2; i++) {
            for (int j = avatar.yPos() - 2; j <= avatar.yPos() + 2; j++) {
                Pixel pixel = pixels.getPixel(i, j);
                if (pixel != null && pixel.tile == LAMP) {
                    return true;
                }
            }
        }
        return false;
    }

    public Lamp lampNextToPlayer() {
        Avatar avatar = avatar();
        for (int i = avatar.xPos() - 2; i <= avatar.xPos() + 2; i++) {
            for (int j = avatar.yPos() - 2; j <= avatar.yPos() + 2; j++) {
                Pixel pixel = pixels.getPixel(i, j);
                if (pixel != null && pixel.tile == LAMP) {
                    return lamps.get(NSquaredToNBijection(i, j));
                }
            }
        }
        return null;
    }

    public void processLampToggle() {
        if (playerNextToLamp()) {
            Lamp lamp = lampNextToPlayer();
            lamp.toggleLamp();
        }
    }
}
