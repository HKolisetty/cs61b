package core.gameplay;

import core.worldgen.wiring.Pixel;
import core.worldgen.terrain.Terrain;
import tileengine.TETile;

import java.util.ArrayList;

import static tileengine.LightedTiles.*;
import static tileengine.Tileset.*;

public class Avatar {
    Terrain world;
    TETile[][] tiles;
    int xPos;
    int yPos;
    ArrayList<TETile> floors;
    public Avatar(Terrain world, int xPos, int yPos) {
        this.world = world;
        this.tiles = world.tiles();
        this.xPos = xPos;
        this.yPos = yPos;
        floors = new ArrayList<>();
        addFloors();
    }

    public int xPos() {
        return xPos;
    }

    public int yPos() {
        return yPos;
    }
    public void addFloors() {
        floors.add(FLOOR);
        floors.add(LIGHTED_FLOOR);
        floors.add(LIGHTED_FLOOR1);
        floors.add(LIGHTED_FLOOR2);
        floors.add(LIGHTED_FLOOR3);
        floors.add(LIGHTED_FLOOR4);
        floors.add(LIGHTED_FLOOR5);
    }

    public void move(int xIncrement, int yIncrement) {
        if (floors.contains(tiles[xPos + xIncrement][yPos + yIncrement])) {
            Pixel pixel = world.pixels.getPixel(xPos,
                    yPos);

            tiles[xPos][yPos] = pixel.tile;

            xPos = xPos + xIncrement;
            yPos = yPos + yIncrement;

            tiles[xPos][yPos] = new TETile(AVATAR.getCharacter(), AVATAR.getTextColor(),
                    tiles[xPos][yPos].getBackgroundColor(),
                    "you");
        }
    }

}
