package knightworld;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Draws a world consisting of knight-move holes.
 */
public class KnightWorld {

    private TETile[][] tiles;
    // TODO: Add additional instance variables here

    public KnightWorld(int width, int height, int holeSize) {
        // TODO: Fill in this constructor and class, adding helper methods and/or classes as necessary to draw the
        //  specified pattern of the given hole size for a window of size width x height. If you're stuck on how to
        //  begin, look at the provided demo code!
        tiles = new TETile[height][width];
        Random RANDOM = new Random();
        int rand = RANDOM.nextInt(5);
        int rand1 = RANDOM.nextInt(4);
        int rand2 = RANDOM.nextInt(4);
        Map<Integer, Integer> cool = new HashMap<>() {{
            put(0, 2);
            put(1, 4);
            put(2, 1);
            put(3, 3);
            put(4, 0);
        }};
        for (int i = 0; i < Math.ceilDiv(height, holeSize); i++) {
            for (int j = 0; j < Math.ceilDiv(width, holeSize); j++) {
                for (int k = 0; k < holeSize; k++) {
                    for (int l = 0; l < holeSize; l++) {
                        if (i * holeSize + k >= height || j * holeSize + l >= width) {
                            continue;
                        }
                        if ((i - cool.get(j % 5) - rand) % 5 == 0) {
                            tiles[i * holeSize + k][j * holeSize + l] = Tileset.NOTHING;
                        } else {
                            tiles[i * holeSize + k][j * holeSize + l] = Tileset.LOCKED_DOOR;
                        }
                    }
                }
            }
        }
    }

    /** Returns the tiles associated with this KnightWorld. */
    public TETile[][] getTiles() {
        return tiles;
    }

    public static void main(String[] args) {
        // Change these parameters as necessary
        int width = 60;
        int height = 40;
        int holeSize = 4;

        KnightWorld knightWorld = new KnightWorld(width, height, holeSize);

        TERenderer ter = new TERenderer();
        ter.initialize(height, width);
        ter.renderFrame(knightWorld.getTiles());

    }
}
