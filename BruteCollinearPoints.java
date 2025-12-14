import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("null argument");
        int n = points.length;
        Point[] copy = new Point[n];
        for (int i = 0; i < n; i++) {
            if (points[i] == null) throw new IllegalArgumentException("null point");
            copy[i] = points[i];
        }
        Arrays.sort(copy);
        // check duplicates
        for (int i = 1; i < n; i++) {
            if (copy[i].compareTo(copy[i - 1]) == 0) throw new IllegalArgumentException("repeated point");
        }

        ArrayList<LineSegment> segs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int l = k + 1; l < n; l++) {
                        Point p = copy[i];
                        Point q = copy[j];
                        Point r = copy[k];
                        Point s = copy[l];
                        double s1 = p.slopeTo(q);
                        double s2 = p.slopeTo(r);
                        double s3 = p.slopeTo(s);
                        if (Double.compare(s1, s2) == 0 && Double.compare(s1, s3) == 0) {
                            // endpoints: smallest and largest among the four
                            Point[] arr = new Point[] {p, q, r, s};
                            Arrays.sort(arr);
                            segs.add(new LineSegment(arr[0], arr[3]));
                        }
                    }
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
        // optional client for testing can be added by user
    }
}
