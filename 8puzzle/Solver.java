import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Board initial;
    private boolean solved;
    private final int move;
    private final SearchNode lastNode;

    private class SearchNode implements Comparable<SearchNode> {
        private final int moves;
        private final Board board;
        private final int priority;
        private final SearchNode previous;

        public SearchNode(Board b, int m, SearchNode p) {
            moves = m;
            board = b;
            previous = p;
            priority = m + b.manhattan();
        }

        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        this.solved = false;
        boolean twinSolved = false;
        MinPQ<SearchNode> priorityQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPriorityQ = new MinPQ<SearchNode>();

        this.initial = initial;
        priorityQ.insert(new SearchNode(initial, 0, null));
        twinPriorityQ.insert(new SearchNode(initial.twin(), 0, null));
        int count = 1;

        while (!solved && !twinSolved) {
            if (priorityQ.min().board.isGoal())
                break;
            else if (twinPriorityQ.min().board.isGoal())
                break;

            SearchNode toDelete = priorityQ.delMin();
            for (Board neigh : toDelete.board.neighbors()) {
                if (toDelete.previous != null && !neigh.equals(toDelete.previous.board))
                    priorityQ.insert(new SearchNode(neigh, toDelete.moves + 1, toDelete));
                else if (toDelete.previous == null)
                    priorityQ.insert(new SearchNode(neigh, toDelete.moves + 1, toDelete));
            }

            SearchNode toDeleteTwin = twinPriorityQ.delMin();
            for (Board neigh : toDeleteTwin.board.neighbors()) {
                if (toDeleteTwin.previous != null && !neigh.equals(toDeleteTwin.previous.board))
                    twinPriorityQ.insert(new SearchNode(neigh, toDeleteTwin.moves + 1, toDeleteTwin));
                else if (toDelete.previous == null)
                    twinPriorityQ.insert(new SearchNode(neigh, toDeleteTwin.moves + 1, toDeleteTwin));
            }
        }

        if (priorityQ.min().board.isGoal()) this.solved = true;
        if (twinPriorityQ.min().board.isGoal()) this.solved = false;
        this.move = priorityQ.min().moves;
        lastNode = priorityQ.min();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solved;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return this.move;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> result = new Stack<Board>();
        SearchNode current = this.lastNode;
        while (current.previous != null) {
            result.push(current.board);
            current = current.previous;
        }
        result.push(this.initial);
        return result;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
