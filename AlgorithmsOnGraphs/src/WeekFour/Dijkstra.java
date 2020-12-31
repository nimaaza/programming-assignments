package WeekFour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Dijkstra {
    private static Integer[] listCost;

    private static long distance(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, int t) {
        listCost = new Integer[adj.length];
        Arrays.fill(listCost, Integer.MAX_VALUE);
        listCost[s] = 0;

        Queue<Integer> q = new PriorityQueue<>(adj.length, new Comparator<Integer>(){
            public int compare(Integer v1, Integer v2) {
                if (listCost[v1] - listCost[v2] > 0) return 1;
                if (listCost[v1] - listCost[v2] < 0) return -1;
                return 0;
            }
        });

        q.add(s);

        while(!q.isEmpty()) {
            int u = q.poll();
            for (int i = 0; i < adj[u].size(); i++) {
                int v = adj[u].get(i);
                int cost_u_v = cost[u].get(i);
                if (listCost[v] > listCost[u] + cost_u_v) {
                    listCost[v] = listCost[u] + cost_u_v;
                    q.remove(v);
                    q.add(v);
                }
            }
        }

        return listCost[t] == Integer.MAX_VALUE ? -1 : listCost[t];
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
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(distance(adj, cost, x, y));
    }
}

