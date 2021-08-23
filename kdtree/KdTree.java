import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private final Point2D point;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node left;        // the left subtree
        private Node right;        // the right subtree

        public Node(Point2D p, RectHV r) {
            if (r == null) {
                r = new RectHV(0, 0, 1, 1);
            }
            point = p;
            rect = r;
            left = null;
            right = null;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    private Node addX(Node x, Point2D p, RectHV r) {
        if (x == null) {
            size++;
            return new Node(p, r);
        }
        if (x.point.equals(p)) return x;

        int cmp = Point2D.X_ORDER.compare(x.point, p);
        RectHV rect;
        if (cmp > 0) {
            if (x.left == null)
                rect = new RectHV(r.xmin(), r.ymin(), x.point.x(), r.ymax());
            else
                rect = x.left.rect;
            x.left = addY(x.left, p, rect);
        }
        else {
            if (x.right == null)
                rect = new RectHV(x.point.x(), r.ymin(), r.xmax(), r.ymax());
            else
                rect = x.right.rect;
            x.right = addY(x.right, p, rect);
        }
        return x;
    }

    private Node addY(Node x, Point2D p, RectHV r) {
        if (x == null) {
            size++;
            return new Node(p, r);
        }
        if (x.point.equals(p)) return x;

        int cmp = Point2D.Y_ORDER.compare(x.point, p);
        RectHV rect;
        if (cmp > 0) {
            if (x.left == null)
                rect = new RectHV(r.xmin(), r.ymin(), r.xmax(), x.point.y());
            else
                rect = x.left.rect;
            x.left = addX(x.left, p, rect);
        }
        else {
            if (x.right == null)
                rect = new RectHV(r.xmin(), x.point.y(), r.xmax(), r.ymax());
            else
                rect = x.right.rect;
            x.right = addX(x.right, p, rect);
        }
        return x;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null exception");
        if (isEmpty()) {
            root = addX(root, p, null);
        }
        else root = addX(root, p, root.rect);
    }

    private boolean contains(Node r, Point2D p, boolean order) {
        if (r == null) return false;
        if (r.point.equals(p)) return true;
        if (order) {
            if (Point2D.X_ORDER.compare(r.point, p) > 0)
                return contains(r.left, p, false);
            else return contains(r.right, p, false);
        }
        else {
            if (Point2D.Y_ORDER.compare(r.point, p) > 0)
                return contains(r.left, p, true);
            else return contains(r.right, p, true);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null exception");
        return contains(root, p, true);
    }

    private void draw(Node x, boolean vert) {
        if (x.left != null) draw(x.left, !vert);
        if (x.right != null) draw(x.right, !vert);

        // draw the point first
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(x.point.x(), x.point.y());

        // draw the line
        double xmin, ymin, xmax, ymax;
        if (vert) {
            StdDraw.setPenColor(StdDraw.RED);
            xmin = x.point.x();
            xmax = x.point.x();
            ymin = x.rect.ymin();
            ymax = x.rect.ymax();
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            ymin = x.point.y();
            ymax = x.point.y();
            xmin = x.rect.xmin();
            xmax = x.rect.xmax();
        }
        StdDraw.setPenRadius();
        StdDraw.line(xmin, ymin, xmax, ymax);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.rectangle(0.5, 0.5, 0.5, 0.5);
        if (isEmpty()) return;
        draw(root, true);
    }

    private void range(Node x, RectHV rect, ArrayList<Point2D> q) {
        if (x == null)
            return;
        if (rect.contains(x.point))
            q.add(x.point);
        if (x.left != null && rect.intersects(x.left.rect))
            range(x.left, rect, q);
        if (x.right != null && rect.intersects(x.right.rect))
            range(x.right, rect, q);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("null exception");
        ArrayList<Point2D> q = new ArrayList<>();
        range(root, rect, q);
        return q;
    }

    private Point2D nearest(Node x, Point2D p, Point2D minPoint, boolean xTrue) {
        Point2D min = minPoint;
        if (x == null) return min;
        if (p.distanceSquaredTo(x.point) < p.distanceSquaredTo(min))
            min = x.point;

        // choose the side that contains the query point first
        if (xTrue) {
            if (x.point.x() < p.x()) {
                min = nearest(x.right, p, min, !xTrue);
                if (x.left != null
                        && (min.distanceSquaredTo(p)
                        > x.left.rect.distanceSquaredTo(p)))
                    min = nearest(x.left, p, min, !xTrue);
            }
            else {
                min = nearest(x.left, p, min, !xTrue);
                if (x.right != null
                        && (min.distanceSquaredTo(p)
                        > x.right.rect.distanceSquaredTo(p)))
                    min = nearest(x.right, p, min, !xTrue);
            }
        }
        else {
            if (x.point.y() < p.y()) {
                min = nearest(x.right, p, min, !xTrue);
                if (x.left != null
                        && (min.distanceSquaredTo(p)
                        > x.left.rect.distanceSquaredTo(p)))
                    min = nearest(x.left, p, min, !xTrue);
            }
            else {
                min = nearest(x.left, p, min, !xTrue);
                if (x.right != null
                        && (min.distanceSquaredTo(p)
                        > x.right.rect.distanceSquaredTo(p)))
                    min = nearest(x.right, p, min, !xTrue);
            }
        }
        return min;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null exception");
        if (isEmpty()) return null;
        return nearest(root, p, root.point, true);
    }

    public static void main(String[] args) {
        KdTree k = new KdTree();
        k.insert(new Point2D(0.7, 0.2));
        k.insert(new Point2D(0.5, 0.4));
        k.insert(new Point2D(0.2, 0.3));
        k.insert(new Point2D(0.4, 0.7));
        k.insert(new Point2D(0.9, 0.6));

        Point2D p = new Point2D(0.3, 0.7);
        StdOut.println(k.size);
        StdOut.println(k.contains(p));
    }
}
