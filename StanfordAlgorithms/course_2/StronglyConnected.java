import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class StronglyConnected {
  private static int[] vertexOrdering;
  private static int currentOrder;
  private static int[] sccs;

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
    vertexOrdering[currentOrder] = s;
    currentOrder += 1;
  }

  private static void dfs(ArrayList<Integer>[] adj, boolean[] explored, int s, int cc, boolean doPostProcessing, boolean markSCC) {
    explored[s] = true;

    for (int e : adj[s]) {
      if (!explored[e]) dfs(adj, explored, e, cc, doPostProcessing, markSCC);
    }

    if (doPostProcessing) vertexPostProcessing(s);
    if (markSCC) sccs[s] = cc;
  }

  private static String numberOfStronglyConnectedComponents(ArrayList<Integer>[] adj) {
      ArrayList<Integer>[] reverseAdj = reverseGraph(adj);

      boolean[] explored = new boolean[adj.length];
      vertexOrdering = new int[adj.length];
      currentOrder = 0;

      for (int i =  0; i < explored.length; i++) {
        if (!explored[i]) dfs(reverseAdj, explored, i, 0, true, false);
      }

      explored = new boolean[adj.length];
      sccs = new int[adj.length];
      int cc = 0;

      for (int i = vertexOrdering.length - 1; i >= 0; i--) {
        if (!explored[vertexOrdering[i]]) {
          dfs(adj, explored, vertexOrdering[i], cc,false, true);
          cc += 1;
        }
      }

      Arrays.sort(sccs);
      int[] counts = new int[cc];
      int c = 1;
      for (int i = 1, j = 0; i < sccs.length; i++) {
          if (sccs[i] == sccs[i - 1]) {
              c++;
          } else {
              counts[j] = c;
              j++;
              c = 1;
          }
      }

      Arrays.sort(counts);
      String s = "";
      for (int i = 1; i <= 5; i++) {
          s += counts[counts.length - i];
          if (i < 5) s += ",";
      }

      return s;
  }

  public static void main(String[] args) throws FileNotFoundException {
      Scanner scanner = new Scanner(new FileInputStream("./SCC.txt"));
      int n = 875714;
      int m = 5105042;
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
