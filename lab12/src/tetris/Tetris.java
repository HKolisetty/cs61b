package tetris;

        import edu.princeton.cs.algs4.In;
        import edu.princeton.cs.algs4.StdDraw;
        import tileengine.TETile;
        import tileengine.TERenderer;
        import tileengine.Tileset;

        import java.awt.*;
        import java.util.*;

/**
 *  Provides the logic for Tetris.
 *
 *  @author Erik Nelson, Omar Yu, Noah Adhikari, Jasmine Lin
 *
 *  render
 */

public class Tetris {

    private static int WIDTH = 10;
    private static int HEIGHT = 20;

    // Tetrominoes spawn above the area we display, so we'll have our Tetris board have a
    // greater height than what is displayed.
    private static int GAME_HEIGHT = 25;

    // Contains the tiles for the board.
    private TETile[][] board;

    // Helps handle movement of pieces.
    private Movement movement;

    // Checks for if the game is over.
    private boolean isGameOver;

    // The current Tetromino that can be controlled by the player.
    private Tetromino currentTetromino;

    // The current game's score.
    private int score = 0;

    /**
     * Checks for if the game is over based on the isGameOver parameter.
     * @return boolean representing whether the game is over or not
     */
    private boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Renders the game board and score to the screen.
     */
    private void renderBoard() {
        ter.renderFrame(board);
        if (auxFilled) {
            auxToBoard();
        } else {
            fillBoard(Tileset.NOTHING);
        }
        renderScore();
    }

    /**
     * Creates a new Tetromino and updates the instance variable
     * accordingly. Flags the game to end if the top of the board
     * is filled and the new piece cannot be spawned.
     */
    private void spawnPiece() {
        // The game ends if this tile is filled
        if (board[4][19] != Tileset.NOTHING) {
            isGameOver = true;
        }

        // Otherwise, spawn a new piece and set its position to the spawn point
        currentTetromino = Tetromino.values()[bagRandom.getValue()];
        currentTetromino.reset();
    }


    /**
     * Determines if a new frame should be rendered.
     * This estimates a 60 fps cap on the rendered window.
     */
    public boolean shouldRenderNewFrame() {
        if (frameDeltaTime() > 16) {
            resetFrameTimer();
            return true;
        }
        return false;
    }

    /**
     * Updates the board based on the user input. Makes the appropriate moves
     * depending on the user's input.
     */
    private void updateBoard() {
        // Grabs the current piece.
        Tetromino t = currentTetromino;
        if (actionDeltaTime() > 100) {
            movement.dropDown();
            resetActionTimer();
            Tetromino.draw(t, board, t.pos.x, t.pos.y);
            return;
        }
        if (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();
            if (key == 'a') {
                movement.tryMove(-1, 0);
            } else if (key == 's') {
                movement.tryMove(0, -1);
            } else if (key == 'd') {
                movement.tryMove(1, 0);
            } else if (key == 'q') {
                movement.rotateLeft();
            } else if (key == 'w') {
                movement.rotateRight();
            }
        }
        Tetromino.draw(t, board, t.pos.x, t.pos.y);


        // TODO: Implement interactivity, so the user is able to input the keystrokes to move
        //  the tile and rotate the tile. You'll want to use some provided helper methods here.


    }

    /**
     * Increments the score based on the number of lines that are cleared.
     *
     * @param linesCleared
     */
    private void incrementScore(int linesCleared) {
        // TODO: Increment the score based on the number of lines cleared.
        HashMap<Integer, Integer> dict = new HashMap<Integer, Integer>() {{
            put(1,100);
            put(2,300);
            put(3,500);
            put(4,800);
        }};
        score += dict.get((Integer) linesCleared);
    }

    /**
     * Clears lines/rows on the board that are horizontally filled.
     * Repeats this process for cascading effects and updates score accordingly.
     */
    private void clearLines() {
        // Keeps track of the current number lines cleared
        ArrayList<Integer> linesCleared = new ArrayList<>();

        for (int i = 0; i < HEIGHT; i++) {
            boolean filled = true;
            for (int j = 0; j < WIDTH; j++) {
                if (board[j][i] == Tileset.NOTHING) {
                    filled = false;
                    break;
                }
            }
            if (filled) {
                linesCleared.add(i);
            }
        }
        if (!linesCleared.isEmpty()) {
            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    if (linesCleared.contains(i)) {
                        board[j][i] = Tileset.NOTHING;
                    }
                }
            }
            for (int n = 0; n < linesCleared.size(); n++) {
                for (int i = 0; i < HEIGHT; i++) {
                    if (checkEmpty(i)) {
                        for (int j = 0; j < WIDTH; j++) {
                            board[j][i] = board[j][i+1];
                            board[j][i+1] = Tileset.NOTHING;
                        }
                    }
                }
            }
            incrementScore(linesCleared.size());
        }
        fillAux();
        renderBoard();

        // TODO: Check how many lines have been completed and clear it the rows if completed.

        // TODO: Increment the score based on the number of lines cleared.

    }
    private boolean checkEmpty(int row) {
        for (int j = 0; j < WIDTH; j++) {
            if (board[j][row] != Tileset.NOTHING) {
                return false;
            }
        }
        return true;
    }

    /**
     * Where the game logic takes place. The game should continue as long as the game isn't
     * over.
     */
    public void runGame() {
        resetActionTimer();
        resetFrameTimer();
        // TODO: First spawn a piece
        while (!isGameOver()) {
            spawnPiece();
            while (getCurrentTetromino() != null) {
                if (shouldRenderNewFrame()) {
                    updateBoard();
                    renderBoard();
                }
            }
            clearLines();
            renderBoard();
        }
        System.exit(0);
        // TODO: Set up your game loop. The game should keep running until the game is over.
        // Use helper methods inside your game loop, according to the spec description.


    }

    /**
     * Renders the score using the StdDraw library.
     */
    private void renderScore() {
        // TODO: Use the StdDraw library to draw out the score.
        StdDraw.setPenColor(255, 255, 255);
        StdDraw.text(7, 19, "Score: " + score);
        StdDraw.show();
    }

    /**
     * Use this method to run Tetris.
     * @param args
     */
    public static void main(String[] args) {
        long seed = args.length > 0 ? Long.parseLong(args[0]) : (new Random()).nextLong();
        Tetris tetris = new Tetris(seed);
        tetris.runGame();
    }

    /**
     * Everything below here you don't need to touch.
     */

    // This is our tile rendering engine.
    private final TERenderer ter = new TERenderer();

    // Used for randomizing which pieces are spawned.
    private Random random;
    private BagRandomizer bagRandom;

    private long prevActionTimestamp;
    private long prevFrameTimestamp;

    // The auxiliary board. At each time step, as the piece moves down, the board
    // is cleared and redrawn, so we keep an auxiliary board to track what has been
    // placed so far to help render the current game board as it updates.
    private TETile[][] auxiliary;
    private boolean auxFilled;

    public Tetris(long seed) {
        board = new TETile[WIDTH][GAME_HEIGHT];
        auxiliary = new TETile[WIDTH][GAME_HEIGHT];
        random = new Random(seed);
        bagRandom = new BagRandomizer(random, Tetromino.values().length);
        auxFilled = false;
        movement = new Movement(WIDTH, GAME_HEIGHT, this);

        ter.initialize(WIDTH, HEIGHT);
        fillBoard(Tileset.NOTHING);
        fillAux();
    }

    // Setter and getter methods.

    /**
     * Returns the current game board.
     * @return
     */
    public TETile[][] getBoard() {
        return board;
    }

    /**
     * Returns the current auxiliary board.
     * @return
     */
    public TETile[][] getAuxiliary() {
        return auxiliary;
    }


    /**
     * Returns the current Tetromino/piece.
     * @return
     */
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    /**
     * Sets the current Tetromino to null.
     * @return
     */
    public void setCurrentTetromino() {
        currentTetromino = null;
    }

    /**
     * Sets the boolean auxFilled to true;
     */
    public void setAuxTrue() {
        auxFilled = true;
    }

    /**
     * Fills the entire board with the specific tile that is passed in.
     * @param tile
     */
    private void fillBoard(TETile tile) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = tile;
            }
        }
    }

    /**
     * Copies the contents of the src array into the dest array using
     * System.arraycopy.
     * @param src
     * @param dest
     */
    private static void copyArray(TETile[][] src, TETile[][] dest) {
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dest[i], 0, src[0].length);
        }
    }

    /**
     * Copies over the tiles from the game board to the auxiliary board.
     */
    public void fillAux() {
        copyArray(board, auxiliary);
    }

    /**
     * Copies over the tiles from the auxiliary board to the game board.
     */
    private void auxToBoard() {
        copyArray(auxiliary, board);
    }

    /**
     * Calculates the delta time with the previous action.
     * @return the amount of time between the previous Tetromino movement with the present
     */
    private long actionDeltaTime() {
        return System.currentTimeMillis() - prevActionTimestamp;
    }

    /**
     * Calculates the delta time with the previous frame.
     * @return the amount of time between the previous frame with the present
     */
    private long frameDeltaTime() {
        return System.currentTimeMillis() - prevFrameTimestamp;
    }

    /**
     * Resets the action timestamp to the current time in milliseconds.
     */
    private void resetActionTimer() {
        prevActionTimestamp = System.currentTimeMillis();
    }

    /**
     * Resets the frame timestamp to the current time in milliseconds.
     */
    private void resetFrameTimer() {
        prevFrameTimestamp = System.currentTimeMillis();
    }

}
