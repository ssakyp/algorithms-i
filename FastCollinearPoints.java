import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("null argument");
        int n = points.length;
        Point[] copy = new Point[n];
        for (int i = 0; i < n; i++) {
            if (points[i] == null) throw new IllegalArgumentException("null point");
            copy[i] = points[i];
        }
        Arrays.sort(copy);
        for (int i = 1; i < n; i++) {
            if (copy[i].compareTo(copy[i - 1]) == 0) throw new IllegalArgumentException("repeated point");
        }

        ArrayList<LineSegment> segs = new ArrayList<>();
        Point[] aux = new Point[n];
        for (int i = 0; i < n; i++) aux[i] = copy[i];

        for (int i = 0; i < n; i++) {
            Point origin = copy[i];
            // sort by slope with respect to origin
            Arrays.sort(aux, origin.slopeOrder());
            int count = 1;
            double prevSlope = Double.NEGATIVE_INFINITY;
            for (int j = 1; j < n; j++) {
                double slope = origin.slopeTo(aux[j]);
                if (j == 1 || Double.compare(slope, prevSlope) != 0) {
                    // new slope group starts at j
                    if (count >= 3) {
                        // points from j-count .. j-1 are collinear with origin
                        Point[] group = new Point[count + 1];
                        group[0] = origin;
                        for (int t = 0; t < count; t++) group[t + 1] = aux[j - count + t];
                        Arrays.sort(group);
                        if (origin.compareTo(group[0]) == 0) {
                            segs.add(new LineSegment(group[0], group[group.length - 1]));
                        }
                    }
                    count = 1;
                    prevSlope = slope;
                } else {
                    count++;
                }
            }
            // check last run
            if (count >= 3) {
                int j = n;
                Point[] group = new Point[count + 1];
                group[0] = origin;
                for (int t = 0; t < count; t++) group[t + 1] = aux[j - count + t];
                Arrays.sort(group);
                if (origin.compareTo(group[0]) == 0) {
                    segs.add(new LineSegment(group[0], group[group.length - 1]));
                }
            }
        }

        segments = segs.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }

    public static void main(String[] args) {
        // optional client
    }
}
