package core.worldgen.wiring;

import core.worldgen.Field;
import tileengine.TETile;


// stores each drawn pixel, with its coordinates, tile and the "field" it belongs to
// fields are individual rooms, outdoor spaces
public class Pixel {
    public TETile tile;
    public Field field;
    public int x;
    public int y;

    public Pixel(TETile tile, Field field, int x, int y) {
        this.tile = tile;
        this.field = field;
        this.x = x;
        this.y = y;
    }
}
