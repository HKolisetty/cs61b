package core.worldgen;


import core.worldgen.wiring.Pixel;
import core.worldgen.terrain.Terrain;
import tileengine.TETile;

import static tileengine.Tileset.*;
import static utils.RandomUtils.bernoulli;

public class Halls {
    Terrain world;
    public Halls(Terrain world) {
        this.world = world;
    }
    public void drawHall(Room start, Room finish) {
        Integer[] startPoint = Room.calculateCenter(start);
        Integer[] endPoint = Room.calculateCenter(finish);

        // randomize between two options
        // first horizontal, then vertical
        // vs first vertical, then horizontal
        // drawing from start to finish
        if (bernoulli(world.seed)) {
            drawHorizontalHall(startPoint[0], endPoint[0], startPoint[1]);
            drawVerticalHall(startPoint[1], endPoint[1], endPoint[0]);
            drawCorner(endPoint[0], startPoint[1]);

        } else {
            drawVerticalHall(startPoint[1], endPoint[1], startPoint[0]);
            drawHorizontalHall(startPoint[0], endPoint[0], endPoint[1]);
            drawCorner(startPoint[0], endPoint[1]);
        }

    }

    // checks if corners of turning hallways are drawn
    public void drawCorner(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <=1; j++) {
                if (world.tiles[x + i][y + j] == NOTHING) {
                    world.tiles[x + i][y + j] = WALL;
                    updateWorldPixels(WALL, x + i, y + j);
                }
            }
        }
    }

    public void drawHorizontalHall(int x1, int x2, int y) {
        int start = Math.min(x1, x2);
        int end = Math.max(x1, x2);
        for (int i = start; i <= end; i++) {
            drawHorizontalHallSegment(i, y);
        }
    }


    public void drawVerticalHall(int y1, int y2, int x) {
        int start = Math.min(y1, y2);
        int end = Math.max(y1, y2);
        for (int j = start; j <= end; j++) {
            drawVerticalHallSegment(x, j);
        }
    }

    // checks if hall is being drawn within a room
    // i.e. if current tile is a FLOOR tile
    // if wall tiles aren't there, and we aren't encroaching on FLOOR tiles
    // draw walls for hallway
    public void drawVerticalHallSegment(int x, int y) {
        if (world.tiles[x][y] != FLOOR) {
            world.tiles[x][y] = FLOOR;
            updateWorldPixels(FLOOR, x, y);

            if (world.tiles[x-1][y] != FLOOR) {
                updateWorldPixels(WALL, x-1, y);
            }
            if (world.tiles[x+1][y] != FLOOR) {
                world.tiles[x+1][y] = WALL;
                updateWorldPixels(WALL, x+1, y);
            }
        }

    }

    // same as above but horizontal implementation
    public void drawHorizontalHallSegment(int x, int y) {
        if (world.tiles[x][y] != FLOOR) {
            world.tiles[x][y] = FLOOR;
            updateWorldPixels(FLOOR, x, y);

            if (world.tiles[x][y-1] != FLOOR) {
                world.tiles[x][y-1] = WALL;
                updateWorldPixels(WALL, x, y-1);
            }
            if (world.tiles[x][y+1] != FLOOR) {
                world.tiles[x][y+1] = WALL;
                updateWorldPixels(WALL, x, y+1);
            }
        }
    }


    public void updateWorldPixels(TETile tile, int x, int y) {
        world.tiles[x][y] = tile;
        world.pixels.addPixel(new Pixel(tile, null, x, y));
    }
}
