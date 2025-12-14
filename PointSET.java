import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private final TreeSet<Point2D> set;

    public PointSET() {
        set = new TreeSet<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        return set.contains(p);
    }

    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("null argument");
        List<Point2D> res = new ArrayList<>();
        for (Point2D p : set) {
            if (rect.contains(p)) res.add(p);
        }
        return res;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument");
        if (set.isEmpty()) return null;
        Point2D best = null;
        double bestDist = Double.POSITIVE_INFINITY;
        for (Point2D q : set) {
            double d = q.distanceSquaredTo(p);
            if (d < bestDist) { bestDist = d; best = q; }
        }
        return best;
    }

    public static void main(String[] args) {
        // optional unit testing can be done by user
    }
}
