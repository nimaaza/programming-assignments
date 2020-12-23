package WeekTwo;

import java.util.ArrayList;
import java.util.Scanner;


public class StronglyConnected {
  private static int[] vertextOrdering;
  private static int currentOrder;

  private static ArrayList<Integer>[] reverseGraph(ArrayList<Integer>[] adj) {
    ArrayList<Integer>[] reverse = new ArrayList[adj.length];
    for (int i = 0; i < reverse.length; i++) {
      reverse[i] = new ArrayList<>();
    }

    for (int i = 0; i < adj.length; i++) {
      for (int e : adj[i]) {
        reverse[e].add(i);
      }
    }

    return reverse;
  }

  private static void vertexPostProcessing(int s) {
    vertextOrdering[currentOrder] = s;
    currentOrder += 1;
  }

  private static void dfs(ArrayList<Integer>[] adj, boolean[] explored, int s, boolean doPostProcessing) {
    explored[s] = true;

    for (int e : adj[s]) {
      if (!explored[e]) dfs(adj, explored, e, doPostProcessing);
    }

    if (doPostProcessing) vertexPostProcessing(s);
  }

  private static int numberOfStronglyConnectedComponents(ArrayList<Integer>[] adj) {
      ArrayList<Integer>[] reverseAdj = reverseGraph(adj);

      boolean[] explored = new boolean[adj.length];
      vertextOrdering = new int[adj.length];
      currentOrder = 0;

      for (int i =  0; i < explored.length; i++) {
        if (!explored[i]) dfs(reverseAdj, explored, i, true);
      }


      explored = new boolean[adj.length];
      int cc = 0;

      for (int i = vertextOrdering.length - 1; i >= 0; i--) {
        if (!explored[vertextOrdering[i]]) {
          dfs(adj, explored, vertextOrdering[i], false);
          cc += 1;
        }
      }

      return cc;
  }

  public static void main(String[] args) {
      Scanner scanner = new Scanner(System.in);
      int n = scanner.nextInt();
      int m = scanner.nextInt();
      ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
      for (int i = 0; i < n; i++) {
          adj[i] = new ArrayList<>();
      }
      for (int i = 0; i < m; i++) {
          int x, y;
          x = scanner.nextInt();
          y = scanner.nextInt();
          adj[x - 1].add(y - 1);
      }
      System.out.println(numberOfStronglyConnectedComponents(adj));
  }
}
