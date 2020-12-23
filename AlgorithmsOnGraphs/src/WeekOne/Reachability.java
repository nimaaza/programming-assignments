package WeekOne;

import java.util.ArrayList;
import java.util.Scanner;

public class Reachability {
    private static boolean[] explored;

    private static int reach(ArrayList<Integer>[] adj, int x, int y) {
        explored = new boolean[adj.length];

        explore(adj, x);

        return explored[y] ? 1 : 0;
    }

    private static void explore(ArrayList<Integer>[] adj, int x) {
        explored[x] = true;

        for (int y : adj[x]) {
            if (!explored[y])
                explore(adj, y);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adj[y - 1].add(x - 1);
        }
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(reach(adj, x, y));
    }
}
