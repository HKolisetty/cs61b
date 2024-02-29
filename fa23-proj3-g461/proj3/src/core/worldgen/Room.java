package core.worldgen;

import core.worldgen.wiring.Pixel;
import core.worldgen.terrain.Terrain;
import tileengine.TETile;
import utils.RandomUtils;

import static tileengine.Tileset.*;

public class Room extends Field {
    // attributes
    int top, bottom, left, right;
    int ID;
    Terrain world;


    // initialize a rectangular Room with specified bottom-left corner coordinates
    // room is of specified width and height
    // top, right = exclusive boundaries;
    // bottom, left = inclusive boundaries
    public Room(int bottom, int left, int width, int height, Terrain world, int ID) {
        this.bottom = Math.min(bottom, world.globalHeight - 1);
        this.left = Math.min(left, world.globalWidth - 1);

        this.right = Math.min(left + width + 2, world.globalWidth - 1);
        this.top = Math.min(bottom + height + 2, world.globalHeight - 1);

        this.world = world;
        this.ID = ID;
    }

    public void drawRoom() {
        drawWalls();
        drawFloor();
    }

    // draw floor within the room
    // recall: right, top exclusive boundaries
    // left, bottom mark wall corner ->
    // have to go further in to draw floors
    public void drawFloor() {
        for (int x = left + 1; x < right - 1; x++) {
            for (int y = bottom + 1; y < top - 1; y++) {
                updateWorldPixels(FLOOR, x, y);
            }
        }
    }

    // draw four walls around the room
    // recall: right, top are exclusive boundaries
    public void drawWalls() {
        drawVerticalLine(left, bottom, top - bottom);
        drawVerticalLine(right - 1, bottom, top - bottom);

        drawHorizontalLine(left, bottom, right - left);
        drawHorizontalLine(left, top - 1, right - left);

    }

    // draw a vertical line from point (x, y) upwards
    // NOTE: height now implementation draws a wall; however we should generalize this.
    public void drawVerticalLine(int x, int y, int length) {
        for (int j = y; j < y + length; j++) {
            if (world.tiles[x][j] == FLOOR) {
                if (!CornerBlockage(x, j)) {
                    joinRoomsByPixel(x, j);
                }
            } else {
                updateWorldPixels(WALL, x, j);
            }
        }
    }

    // draw a horizontal line from point (x, y) rightwards
    // NOTE: height now implementation draws a wall; however we should generalize this.
    public void drawHorizontalLine(int x, int y, int length) {
        for (int i = x; i < x + length; i++) {
            if (world.tiles[i][y] == FLOOR) {
                if (!CornerBlockage(i, y)) {
                    joinRoomsByPixel(i, y);
                }
            } else {
                updateWorldPixels(WALL, i, y);
            }
        }
    }

    // update both visual display and pixel map
    public void updateWorldPixels(TETile tile, int x, int y) {
        world.tiles[x][y] = tile;
        world.pixels.addPixel(new Pixel(tile, this, x, y));
    }

    public void joinRoomsByPixel(int x, int y) {
        Pixel pixel = world.pixels.getPixel(x, y);
        Room overlappingRoom = (Room) pixel.field;
        world.rooms.connections.union(ID, overlappingRoom.ID);
    }

    // checks for weird edge case where technically rooms are "overlapping",
    // but their corners are right next to each other diagonally
    // so in reality the rooms are not connected
    public boolean CornerBlockage(int x, int y) {
        if (world.tiles[x+1][y] == WALL && world.tiles[x][y+1] == WALL) {
            return true;
        }
        if (world.tiles[x+1][y] == WALL && world.tiles[x][y-1] == WALL) {
            return true;
        }
        if (world.tiles[x-1][y] == WALL && world.tiles[x][y+1] == WALL) {
            return true;
        }
        if (world.tiles[x-1][y] == WALL && world.tiles[x][y-1] == WALL) {
            return true;
        }
        return false;
    }

    public boolean roomIsOverlapping() {
        if (verticalWallExists(left, bottom, top - bottom) ||
                verticalWallExists(right - 1, bottom, top - bottom) ||
                horizontalWallExists(left, bottom, right - left) ||
                horizontalWallExists(left, top - 1, right - left)) {
            return true;
        }
        return false;
    }

    public boolean verticalWallExists(int x, int y, int length) {
        for (int j = y; j < y + length; j++) {
            if (world.tiles[x][j] != NOTHING) {
                return true;
            }
        }
        return false;
    }

    public boolean horizontalWallExists(int x, int y, int length) {
        for (int i = x; i < x + length; i++) {
            if (world.tiles[i][y] != NOTHING) {
                return true;
            }
        }
        return false;
    }

    public void regenerate(int leftBound, int lowerBound, int minWidth, int maxWidth,
                           int minHeight, int maxHeight, int blurFactor) {
        left = RandomUtils.uniform(world.seed,
                leftBound + 1, leftBound + world.sectionWidth / 2 - minWidth);
        bottom = RandomUtils.uniform(world.seed,
                lowerBound + 1, lowerBound + world.sectionHeight / 2 - minHeight);


        int width = RandomUtils.uniform(world.seed, minWidth, maxWidth);
        int height = RandomUtils.uniform(world.seed, minHeight, maxHeight);

        right = Math.min(left + width + 2, world.globalWidth - 1);
        top = Math.min(bottom + height + 2, world.globalHeight - 1);

    }

    public static Integer[] calculateCenter(Room room) {
        int x = average(room.left, room.right);
        int y = average(room.top, room.bottom);
        Integer[] returnArray = {x, y};
        return returnArray;
    }

    public static int average(int n1, int n2) {
        return (n1 + n2) / 2;
    }

}
