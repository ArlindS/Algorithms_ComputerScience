import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lines = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        if (points == null) throw new IllegalArgumentException("The array is null.");
        for (Point p : points)
            if (p == null)
                throw new IllegalArgumentException("The array contains null element.");

        // sort points so in order for line segment
        Point[] copy = points.clone();
        Arrays.sort(copy);

        for (int i = 0; i < copy.length - 1; i++)
            if (copy[i].compareTo(copy[i + 1]) == 0)
                throw new IllegalArgumentException("Duplicate found.");

        for (int i = 0; i < copy.length - 3; i++)
            for (int j = i + 1; j < copy.length - 2; j++)
                for (int k = j + 1; k < copy.length - 1; k++)
                    if (copy[i].slopeTo(copy[j]) == copy[i].slopeTo(copy[k]))
                        for (int n = k + 1; n < copy.length; n++)
                            if (copy[i].slopeTo(copy[j]) == copy[i].slopeTo(copy[n]))
                                lines.add(new LineSegment(copy[i], copy[n]));
    }


    // the number of line segments
    public int numberOfSegments() {
        return lines.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lines.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

