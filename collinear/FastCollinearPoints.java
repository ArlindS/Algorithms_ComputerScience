import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lines = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("The array is null.");

        for (Point p : points)
            if (p == null)
                throw new IllegalArgumentException("The array contains null element.");

        Point[] copy = points.clone();
        Arrays.sort(copy);
        for (int i = 0; i < copy.length - 1; i++)
            if (copy[i].compareTo(copy[i + 1]) == 0)
                throw new IllegalArgumentException("Duplicate found.");

        for (int i = 0; i < points.length-3; i++) {
            Arrays.sort(copy);
            Arrays.sort(copy, copy[i].slopeOrder());

            for (int k = 0, m = 1, n = 2; n < copy.length; n++) {
                while (n < copy.length && copy[k].slopeTo(copy[m]) == copy[k].slopeTo(copy[n]))
                    n++;
                if (n-m >= 3 && copy[k].compareTo(copy[m]) < 0)
                    lines.add(new LineSegment(copy[k], copy[n-1]));
                m = n;
            }
        }

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
