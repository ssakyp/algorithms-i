import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static class Node {
        Point2D p;
        RectHV rect;
        Node left; // lb
        Node right; // rt
        boolean vertical;

        Node(Point2D p, RectHV rect, boolean vertical) {
            this.p = p;
            this.rect = rect;
            this.vertical = vertical;
        }
    }

    private Node root;
    private int size;

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        root = insert(root, p, true, new RectHV(0, 0, 1, 1));
    }

    private Node insert(Node node, Point2D p, boolean vertical, RectHV rect) {
        if (node == null) {
            size++;
            return new Node(p, rect, vertical);
        }
        if (node.p.equals(p)) return node;
        if (node.vertical) {
            if (p.x() < node.p.x()) {
                RectHV r = new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax());
                node.left = insert(node.left, p, !node.vertical, r);
            } else {
                RectHV r = new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
                node.right = insert(node.right, p, !node.vertical, r);
            }
        } else {
            if (p.y() < node.p.y()) {
                RectHV r = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y());
                node.left = insert(node.left, p, !node.vertical, r);
            } else {
                RectHV r = new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
                node.right = insert(node.right, p, !node.vertical, r);
            }
        }
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) return false;
        if (node.p.equals(p)) return true;
        if (node.vertical) {
            if (p.x() < node.p.x()) return contains(node.left, p);
            else return contains(node.right, p);
        } else {
            if (p.y() < node.p.y()) return contains(node.left, p);
            else return contains(node.right, p);
        }
    }

    public void draw() {
        StdDraw.setPenRadius(0.01);
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;
        // draw splitting line
        StdDraw.setPenRadius();
        if (node.vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            double x = node.p.x();
            StdDraw.line(x, node.rect.ymin(), x, node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            double y = node.p.y();
            StdDraw.line(node.rect.xmin(), y, node.rect.xmax(), y);
        }
        // draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();
        draw(node.left);
        draw(node.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("null argument");
        List<Point2D> res = new ArrayList<>();
        range(root, rect, res);
        return res;
    }

    private void range(Node node, RectHV rect, List<Point2D> res) {
        if (node == null) return;
        if (!rect.intersects(node.rect)) return;
        if (rect.contains(node.p)) res.add(node.p);
        range(node.left, rect, res);
        range(node.right, rect, res);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        if (root == null) return null;
        return nearest(root, p, root.p, root.p.distanceSquaredTo(p));
    }

    private Point2D nearest(Node node, Point2D query, Point2D best, double bestDist) {
        if (node == null) return best;
        double rectDist = node.rect.distanceSquaredTo(query);
        if (rectDist >= bestDist) return best;
        double d = node.p.distanceSquaredTo(query);
        if (d < bestDist) {
            bestDist = d;
            best = node.p;
        }
        Node first, second;
        if (node.vertical) {
            if (query.x() < node.p.x()) { first = node.left; second = node.right; }
            else { first = node.right; second = node.left; }
        } else {
            if (query.y() < node.p.y()) { first = node.left; second = node.right; }
            else { first = node.right; second = node.left; }
        }
        best = nearest(first, query, best, bestDist);
        bestDist = best.distanceSquaredTo(query);
        best = nearest(second, query, best, bestDist);
        return best;
    }

    public static void main(String[] args) {
        // optional testing
    }
}
