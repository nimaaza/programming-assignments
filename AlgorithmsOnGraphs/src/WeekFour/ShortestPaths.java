package WeekFour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ShortestPaths {
    private static void dfs(ArrayList<Integer>[] adj, boolean[] explored, int s, int[] shortest) {
        explored[s] = true;
        shortest[s] = 0;

        for (int v : adj[s]) {
            if(!explored[v]) {
                dfs(adj, explored, v, shortest);
            }
        }
    }

    private static void shortestPaths(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, long[] distance, int[] reachable, int[] shortest) {
        distance[s] = 0;
        ArrayList<Integer> lastVertices = new ArrayList<>();
        boolean[] explored = new boolean[adj.length];

        dfs(adj, explored, s, shortest);
        Arrays.fill(shortest, 1);
        for (int i = 0; i < explored.length; i++) {
            if (explored[i]) reachable[i] = 1;
        }

        for (int i = 0; i < adj.length; i++) {
            for (int u = 0; u < adj.length; u++) {
                for (int vIndex = 0; vIndex < adj[u].size(); vIndex++) {
                    int v = adj[u].get(vIndex);
                    if (explored[v] && distance[v] > distance[u] + cost[u].get(vIndex)) {
                        distance[v] = distance[u] + cost[u].get(vIndex);
                        if (i == adj.length - 1) lastVertices.add(v);
                    }
                }
            }
        }

        for (int v : lastVertices) dfs(adj, new boolean[adj.length], v, shortest);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
        }
        int s = scanner.nextInt() - 1;
        long distance[] = new long[n];
        int reachable[] = new int[n];
        int shortest[] = new int[n];
        for (int i = 0; i < n; i++) {
            distance[i] = (long) Math.pow(10, 12);
            reachable[i] = 0;
            shortest[i] = 1;
        }
        shortestPaths(adj, cost, s, distance, reachable, shortest);
        for (int i = 0; i < n; i++) {
            if (reachable[i] == 0) {
                System.out.println('*');
            } else if (shortest[i] == 0) {
                System.out.println('-');
            } else {
                System.out.println(distance[i]);
            }
        }
    }
}
