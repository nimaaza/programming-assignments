package WeekTwo;

import java.util.ArrayList;
import java.util.Scanner;

public class Acyclicity {
    private static final int CYCLIC = 1;
    private static final int ACYCLIC = 0;

    private static int dfs(int initialVertex, int currentVertex, ArrayList<Integer>[] adj, boolean[] visited) {
        if (adj[currentVertex].contains(initialVertex)) return CYCLIC;

        visited[currentVertex] = true;
        for (int next : adj[currentVertex]) {
            if (!visited[next]) return dfs(initialVertex, next, adj, visited);
        }

        return ACYCLIC;
    }

    private static int acyclic(ArrayList<Integer>[] adj) {
        for (int i = 0; i < adj.length; i++) {
            if (dfs(i, i, adj, new boolean[adj.length]) == CYCLIC) return CYCLIC;
        }

        return ACYCLIC;
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
        }
        System.out.println(acyclic(adj));
    }
}
