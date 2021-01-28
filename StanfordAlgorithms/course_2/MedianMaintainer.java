import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class MedianMaintainer {
  private static PriorityQueue<Integer> low = new PriorityQueue<>();
  private static PriorityQueue<Integer> high = new PriorityQueue<>();

  private static Integer add(Integer n) {
    Integer maxOfLow;

    if (low.isEmpty()) maxOfLow = Integer.MAX_VALUE;
    else maxOfLow = -low.peek();

    if (n < maxOfLow)
      low.add(-n);
    else
      high.add(n);

    if ((low.size() + high.size()) % 2 == 0) {
      if (low.size() > high.size())
        high.add(-low.poll());
      else if (low.size() < high.size())
        low.add(-high.poll());
    } else {
      if (low.size() > high.size() + 1)
        high.add(-low.poll());
      else if (low.size() < high.size() + 1)
        low.add(-high.poll());
    }

    return -low.peek();
  }

  public static void main(String[] args) throws FileNotFoundException {
    Scanner scanner = new Scanner(new FileInputStream("./Median.txt"));
    int mediansSum = 0;

    while (scanner.hasNextInt())
      mediansSum += add(scanner.nextInt());

    System.out.println(mediansSum % 10000);
    scanner.close();
  }
}
