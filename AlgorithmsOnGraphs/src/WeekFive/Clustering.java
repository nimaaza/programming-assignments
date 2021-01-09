package WeekFive;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Clustering {
    // union-find data structure
    private static int[] initialize(int n) {
        int[] union = new int[n];
        for (int i = 0; i < n; i++) union[i] = i;
        return union;
    }

    private static int[] sizes(int n) {
        int[] size = new int[n];
        for (int i = 0; i < n; i++) size[i] = 1;
        return size;
    }

    private static int find(int[] union, int i) {
        while (i != union[i]) i = union[i];
        return i;
    }

    private static void union(int[] union, int[] size, int i, int j) {
        int iParent = find(union, i);
        int jParent = find(union, j);

        if (iParent == jParent) return;

        if (size[iParent] >= size[jParent]) {
            union[jParent] = iParent;
            size[iParent] += size[jParent];
        } else {
            union[iParent] = jParent;
            size[jParent] += size[iParent];
        }
    }

    // clustering
    private static double distance(int[] x, int[] y, Integer[] v) {
        return Math.sqrt(Math.pow(x[v[0]] - x[v[1]], 2) + Math.pow(y[v[0]] - y[v[1]], 2));
    }

    private static double clustering(int[] x, int[] y, int k) {
        int[] union = initialize(x.length);
        int[] size = sizes(x.length);

        List<Integer[]> edges = new ArrayList<>();
        for (int i = 0; i < x.length - 1; i++) {
            for (int j = i + 1; j < x.length; j++) {
                edges.add(new Integer[]{i, j});
            }
        }
        edges.sort((e1, e2) -> {
            double e1Length = distance(x, y, e1);
            double e2Length = distance(x, y, e2);
            if (e1Length > e2Length) return 1;
            if (e1Length < e2Length) return -1;
            return 0;
        });

        double d = 0;

        for (int i = 0, n = x.length; i < edges.size(); i++) {
            Integer[] edge = edges.get(i);

            if (find(union, edge[0]) != find(union, edge[1])) {
                union(union, size, edge[0], edge[1]);
                n--;
            }

            if (n == k - 1) {
                edge = edges.get(i);
                d = distance(x, y, edge);
                break;
            }
        }

        return d;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();
        }
        int k = scanner.nextInt();
        System.out.println(clustering(x, y, k));
    }
}
