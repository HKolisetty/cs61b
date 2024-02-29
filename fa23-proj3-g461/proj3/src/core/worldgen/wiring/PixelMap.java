package core.worldgen.wiring;

import java.util.HashMap;

public class PixelMap {
    HashMap<Integer, Pixel> pixels;

    public PixelMap() {
        pixels = new HashMap<>();
    }

    public void addPixel(Pixel pixel) {
        Integer index = NSquaredToNBijection(pixel.x, pixel.y);
        pixels.put(index, pixel);
    }

    public Pixel getPixel(int x, int y) {
        Integer index = NSquaredToNBijection(x, y);
        return pixels.get(index);
    }

    // Szudzik's function maps N^N (where N := natural numbers) to N
    // we use it to generate unique hashcodes for x, y coordinates
    public int NSquaredToNBijection(int x, int y) {
        if (x >= y) {
            return x * x + x + y;
        } else {
            return x + y * y;
        }
    }
}
