package core.worldgen.wiring;

import core.worldgen.Room;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.HashMap;

public class Rooms {
    public WeightedQuickUnionUF connections;
    public HashMap<Integer, Room> map;

    public Rooms(){
        connections = new WeightedQuickUnionUF(150);
        map = new HashMap<>();
    }
}
