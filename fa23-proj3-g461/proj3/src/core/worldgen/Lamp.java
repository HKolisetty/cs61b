package core.worldgen;

import core.worldgen.wiring.Pixel;
import core.worldgen.terrain.Terrain;
import tileengine.TETile;

import java.util.ArrayList;

import static tileengine.Tileset.*;
import static tileengine.LightedTiles.*;

public class Lamp {
    int xPos;
    int yPos;
    Terrain world;
    TETile[][] tiles;
    boolean lampIsOn;
    static ArrayList<TETile> litTiles = new ArrayList<>();

    public void buildTileList() {
        litTiles.add(LIGHTED_FLOOR1);
        litTiles.add(LIGHTED_FLOOR2);
        litTiles.add(LIGHTED_FLOOR3);
        litTiles.add(LIGHTED_FLOOR4);
        litTiles.add(LIGHTED_FLOOR5);
    }

    public Lamp(int xPos, int yPos, Terrain world) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.world = world;
        this.tiles = world.tiles;
        lampIsOn = false;
        buildTileList();
    }

    public boolean lampIsOn() {
        return lampIsOn;
    }

    public void toggleLamp() {
        if (lampIsOn()) {
            turnOff();
            lampIsOn = false;
        } else {
            turnOn();
            lampIsOn = true;
        }
    }

    public void turnOff() {
        for (int i = xPos - 3; i <= xPos + 3; i++) {
            for (int j = yPos - 3; j <= yPos + 3; j++) {
                if (!outOutBounds(i, j)) {
                    Pixel pixel = world.pixels.getPixel(i, j);
                    if (pixel != null && pixel.tile == LAMP) {
                        tiles[i][j] = new TETile('◎', LAMP.getTextColor(),
                                FLOOR.getBackgroundColor(), "lamp");
                    }
                    if (pixel != null && litTiles.contains(pixel.tile)) {
                        if (litTiles.contains(tiles[i][j])) {
                            tiles[i][j] = FLOOR;
                        }
                        if (tiles[i][j].description() == "you") {
                            tiles[i][j] = new TETile(AVATAR.getCharacter(), AVATAR.getTextColor(),
                                    FLOOR.getBackgroundColor(), "you");;
                        }
                        updateWorldPixels(FLOOR, i, j);
                    }
                }
            }
        }
    }

    public void turnOn() {
        lightSquare(3, LIGHTED_FLOOR3);
        lightSquare(2, LIGHTED_FLOOR2);
        lightSquare(1, LIGHTED_FLOOR1);

    }

    public void lightSquare(int width, TETile litFloor) {
        for (int i = xPos - width; i <= xPos + width; i++) {
            for (int j = yPos - width; j <= yPos + width; j++) {
                lightPixel(i, j, litFloor);
            }
        }
    }

    public Room lampRoom() {
        Pixel pixel = world.pixels.getPixel(xPos, yPos);
        return (Room) pixel.field;
    }

    public void lightPixel(int i, int j, TETile litFloor) {
        if (outOutBounds(i, j)) {
            return;
        }
        Pixel pixel = world.pixels.getPixel(i, j);
        if (pixel != null && pixel.tile == LAMP) {
            tiles[i][j] = new TETile('◎', LAMP.getTextColor(),
                    litFloor.getBackgroundColor(), "lamp");
        }
        if (pixel != null && (pixel.tile == FLOOR || litTiles.contains(pixel.tile))) {
            if (tiles[i][j] == FLOOR || litTiles.contains(tiles[i][j])) {
                tiles[i][j] = litFloor;
            }
            if (tiles[i][j].description() == "you") {
                tiles[i][j] = new TETile(AVATAR.getCharacter(), AVATAR.getTextColor(),
                        litFloor.getBackgroundColor(), "you");
            }
            updateWorldPixels(litFloor, i, j);
        }
    }

    public boolean outOutBounds(int i, int j) {
        if (i < 0 || i >= world.globalWidth) {
            return true;
        }
        if (j < 0 || j >= world.globalHeight) {
            return true;
        }
        return false;
    }

    public void updateWorldPixels(TETile tile, int x, int y) {
        world.pixels.addPixel(new Pixel(tile, lampRoom(), x, y));
    }

}
