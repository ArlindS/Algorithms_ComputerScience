import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = 0;
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        if (args.length > 0) {
            try {
                k = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
            }
            // StdRandom.shuffle(args, 1, args.length);
            String s = " ";
            while (!StdIn.isEmpty()) {
                s = StdIn.readString();
                rq.enqueue(s);
            }
            for (int i = 0; i < k; i++) {
                StdOut.println(rq.dequeue());
            }
        }
    }
}
