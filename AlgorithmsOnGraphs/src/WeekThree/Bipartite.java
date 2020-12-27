package WeekThree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Bipartite {
    private static int bipartite(ArrayList<Integer>[] adj) {
        int[] distances = new int[adj.length];
        boolean[] explored = new boolean[adj.length];
        int[] colors = new int[adj.length];

        Arrays.fill(distances, -1);
        Arrays.fill(colors, -1);

        for (int i = 0; i < explored.length; i++) {
            if (!explored[i]) {
                Queue<Integer> queue = new LinkedList<>();
                queue.add(i);
                distances[i] = 0;
                colors[i] = 0;


                while (!queue.isEmpty()) {
                    int v = queue.poll();
                    explored[v] = true;
                    for (int e : adj[v]) {
                        if (distances[e] == -1) {
                            queue.add(e);
                            distances[e] = distances[v] + 1;
                            colors[e] = distances[e] % 2;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < adj.length; i++) {
            for (int e : adj[i]) {
                if (colors[e] == colors[i]) return 0;
            }
        }

        return 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
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
        System.out.println(bipartite(adj));
    }
}
