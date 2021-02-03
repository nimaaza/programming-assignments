import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TwoSum {
  private static ArrayList<Long> integers;
  private static HashMap<Long, Long> integersHashtable;

  private static boolean twoSumExists(Long t) {
    for (Long x : integers) {
      Long y = t - x;
      if (integersHashtable.containsKey(y) && !x.equals(y))
        return true;
    }

    return false;
  }

  public static void main(String[] args) throws IOException {
    integers = new ArrayList<>();
    integersHashtable = new HashMap<>();

    try (Scanner scanner = new Scanner(new FileInputStream("./2sum.txt"))) {
      while(scanner.hasNext()) {
        Long next = scanner.nextLong();
        integers.add(next);
        integersHashtable.put(next, next);
      }
    }

    System.out.println("This is going to take a while...");

    int count = 0;
    Long i =  -10_000L;

    while (i <= 10_000) {
      if (twoSumExists(i)) count += 1;

      if (count != 0 && count % 100 == 0)
        System.out.println("counting @ " + count + " with i=" + i);
      if (i % 1000 == 0)
        System.out.println("i @ " + i);

      i += 1;
    }

    System.out.println(count);
  }
}
