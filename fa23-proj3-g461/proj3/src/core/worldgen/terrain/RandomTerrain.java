package core.worldgen.terrain;

import core.worldgen.wiring.PixelMap;
import core.worldgen.wiring.Rooms;
import core.worldgen.Halls;
import tileengine.Tileset;

import java.util.HashMap;
import java.util.Random;

public class RandomTerrain extends Terrain {
    public RandomTerrain(int width, int height, Long seed) {
        super(width, height, seed);
        this.seed = new Random(seed);
        pixels = new PixelMap();
        rooms = new Rooms();
        halls = new Halls(this);
        lamps = new HashMap<>();
        new Tileset(this.seed).randomTheme();

        globalWidth = width;
        globalHeight = height;
        sectionWidth = globalWidth / cols;
        sectionHeight = globalHeight / rows;

        numRooms = 0;

        generateTerrain();
    }
}
