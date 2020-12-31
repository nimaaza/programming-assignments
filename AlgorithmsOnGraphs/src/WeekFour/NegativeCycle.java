package WeekFour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class NegativeCycle {
    private static int negativeCycle(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost) {
        int[] listCost = new int[adj.length];
        Arrays.fill(listCost, 100000);
        listCost[0] = 0;

        boolean relaxed = false;

        for (int i = 1; i <= adj.length; i++) {
            relaxed = false;
            for (int u = 0; u < adj.length; u++) {
                for (int v = 0; v < adj[u].size(); v++) {
                    int costV = listCost[adj[u].get(v)];
                    int costU = listCost[u];
                    int costUtoV = cost[u].get(v);
                    if (costV > costU + costUtoV) {
                        listCost[adj[u].get(v)] = costU + costUtoV;
                        relaxed = true;
                    }
                }
            }
        }

        return relaxed ? 1 : 0;
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
        System.out.println(negativeCycle(adj, cost));
    }
}
