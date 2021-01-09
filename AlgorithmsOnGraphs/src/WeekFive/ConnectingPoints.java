package WeekFive;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class ConnectingPoints {
    private static double getDistanceBetweenCities(int[] x, int[] y, int i, int j) {
        return Math.sqrt((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]));
    }

    private static double getTotalDistance(int[] x, int[] y, List<Integer[]> tree) {
        double distance = 0;

        for (Integer[] cities : tree) {
            distance += getDistanceBetweenCities(x, y, cities[0], cities[1]);
        }

        return distance;
    }

    private static double minimumDistance(int[] x, int[] y) {
        List<Integer> unexplored = new ArrayList<>();
        List<Integer[]> tree = new ArrayList<>();
        double[] distance = new double[x.length];
        int[] nearestCity = new int[x.length];
        Queue<Integer> queue = new PriorityQueue<>(x.length, (i, j) -> {
            if (distance[i] > distance[j]) return 1;
            if (distance[i] < distance[j]) return -1;
            return 0;
        });

        for (int i = 1; i < x.length; i++) {
            unexplored.add(i);
            distance[i] = getDistanceBetweenCities(x, y, i, 0);
            nearestCity[i] = 0;
            queue.add(i);
        }

        while(!queue.isEmpty()) {
            Integer nextVertex = queue.poll();
            tree.add(new Integer[]{nextVertex, nearestCity[nextVertex]});
            unexplored.remove(nextVertex);

            for(Integer v : unexplored) {
                double betterDistance = getDistanceBetweenCities(x, y, nextVertex, v);
                if (betterDistance < distance[v]) {
                    queue.remove(v);
                    distance[v] = betterDistance;
                    nearestCity[v] = nextVertex;
                    queue.add(v);
                }
            }
        }

        return getTotalDistance(x, y, tree);
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
        System.out.println(minimumDistance(x, y));
    }
}
