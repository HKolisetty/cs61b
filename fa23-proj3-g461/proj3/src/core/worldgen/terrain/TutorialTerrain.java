package core.worldgen.terrain;

import core.worldgen.Room;

import static utils.RandomUtils.uniform;


public class TutorialTerrain extends Terrain {

    public TutorialTerrain(int width, int height, Long seed) {
        super(width, height, seed);
    }


    // divides our world into small sections and draws rooms for each section
    public void drawRooms() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {

                int leftBound = sectionWidth * i;
                int lowerBound = sectionHeight * j;

                drawTutorialInSection(lowerBound, leftBound);
            }
        }
    }

    public void drawTutorialInSection(int lowerBound, int leftBound) {
        int numberOfRooms = uniform(seed, 2, 4);

        int minWidth = 6, maxWidth = 8;
        int minHeight = 4, maxHeight = 10;
        int blurFactor = -1;

        for (int i = 0; i < numberOfRooms; i++) {
            int left = uniform(seed,
                    leftBound + 1,leftBound + sectionWidth - minWidth + blurFactor);
            int bottom = uniform(seed,
                    lowerBound + 1, lowerBound + sectionHeight - minHeight + blurFactor);

            int width = uniform(seed, minWidth, maxWidth);
            int height = uniform(seed, minHeight, maxHeight);

            Room room = new Room(bottom, left, width, height, this, numRooms);

            int loopLimit = 0;
            while (room.roomIsOverlapping() && loopLimit < 10) {
                room.regenerate(leftBound, lowerBound, minWidth, maxWidth, minHeight, maxHeight, blurFactor);
                loopLimit++;
            }

            if (!room.roomIsOverlapping()) {
                rooms.map.put(numRooms, room);
                room.drawRoom();
                numRooms++;
            }
        }
    }

}
