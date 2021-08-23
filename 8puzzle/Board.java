import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final char[] tiles;
    private final int dim;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // CHECK IF CONTAINS RIGHT NUMBERS
        this.tiles = new char[tiles.length * tiles.length];
        int counter = 0;
        this.dim = tiles.length;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++)
                this.tiles[counter++] = (char) tiles[i][j];
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dim + "\n");
        int counter = 0;
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                int n = tiles[counter++];
                s.append(String.format("%2d ", n));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return this.dim;
    }

    // number of tiles out of place
    public int hamming() {
        int ham = 0;
        for (int i = 0; i < this.tiles.length; i++) {
            int n = this.tiles[i];
            if (n != i+1 && n > 0) {
                ham++;
            }
        }
        return ham;
    }

    private int findMD(int i, int num) {
        int row = (num - 1) / this.dim;
        int column = (num - 1) % this.dim;
        int row1 = i / this.dim;
        int column1 = i % this.dim;
        return Math.abs(row - row1) + Math.abs(column - column1);
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int md = 0;
        int counter = 1;
        for (int i = 0; i < this.tiles.length; i++) {
            int n = this.tiles[i];
            if (n != counter && n > 0)
                md += findMD(i, n);
            counter++;
        }
        return md;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dim != this.dim) return false;
        for (int i = 0; i < this.tiles.length; i++) {
                if (this.tiles[i] != that.tiles[i])
                    return false;
        }
        return true;
    }

    private int[][] numArray(char[] input) {
        int k = 0;
        int[][] arr = new int[this.dim][this.dim];
        for (int i = 0; i < this.dim; i++)
            for (int j = 0; j < this.dim; j++) {
                arr[i][j] = input[k];
                k++;
            }
        return arr;
    }

    private ArrayList<Integer> getNeigh(int[][] board) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                if (board[i][j] == 0) {
                    arr.add(i);
                    arr.add(j);
                    if (j + 1 < this.dim) {
                        arr.add(i);
                        arr.add(j + 1);
                    }
                    if (j - 1 >= 0) {
                        arr.add(i);
                        arr.add(j - 1);
                    }
                    if (i + 1 < this.dim) {
                        arr.add(i + 1);
                        arr.add(j);
                    }
                    if (i - 1 >= 0) {
                        arr.add(i - 1);
                        arr.add(j);
                    }
                }
            }
        }
        return arr;
    }

    private int[][] swap(int[][] b, int originalX, int originalY, int newX, int newY) {
        int[][] newB = new int[b.length][b[0].length];
        for (int i = 0; i < b.length; i++)
            newB[i] = Arrays.copyOf(b[i], b[i].length);
        newB[originalX][originalY] = b[newX][newY];
        newB[newX][newY] = b[originalX][originalY];
        return newB;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neigh = new ArrayList<>();
        int[][] input = numArray(this.tiles);
        ArrayList<Integer> numNeigh = getNeigh(input);
        for (int i = 2; i < numNeigh.size(); i += 2) {
            neigh.add(new Board(
                    swap(input, numNeigh.get(0), numNeigh.get(1), numNeigh.get(i),
                         numNeigh.get(i + 1))));
        }
        return neigh;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin;
        int[][] input = numArray(this.tiles);
        if (input[0][0] != 0 && input[0][1] != 0)
            twin = new Board(swap(input, 0, 0, 0, 1));
        else
            twin = new Board(swap(input, 1, 0, 1, 1));
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] board = { { 8, 1, 3, 9 }, { 4, 0, 2, 12 }, { 7, 6, 5, 11 }, { 10, 13, 15, 14} };
        int[][] board2 = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };

        Board b = new Board(board);
        Board c = new Board(board2);
        StdOut.println(c.toString());
        StdOut.println("Hamming: " + c.hamming());
        StdOut.println("Manhattan: " +  c.manhattan());
        StdOut.println("Is goal: " + c.isGoal());

        for (Board x : c.neighbors()) {
            StdOut.println(x.toString());
        }
        StdOut.println(b.twin());
        StdOut.println(8 / 3);
        StdOut.println(8 % 3);
    }
}
