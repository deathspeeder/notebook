
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class QuickSort {

  public static void debugPrint(List<Integer> elements) {
    elements.stream().map(i -> i.toString()).reduce((i, j) -> i + "," + j).ifPresent(s -> System.out.println(s));
  }

  public static void run(String name, List<Integer> elements) {
    System.out.println("case " + name);
    debugPrint(elements);
    System.out.println("====");
    debugPrint(quickSort(elements));
    System.out.println("====");
    debugPrint(quickSortCompact(elements));
    System.out.println("====");
  }

  public static void main(String[] args) {
    List<Integer> case1 = Arrays.asList(new Integer[] { 4, 5, 6, 1, 6, 3, 2, 7, 5 });

    List<Integer> case2 = Arrays.asList(new Integer[] { 3, 3, 3, 3 });

    run("case 1", case1);
    run("case 2", case2);
  }

  public static List<Integer> quickSort(List<Integer> elements) {
    if (elements.size() <= 1) {
      return elements;
    }
    Integer pivot = elements.get(0);
    List<Integer> left = new LinkedList<>();
    List<Integer> right = new LinkedList<>();
    Iterator<Integer> it = elements.iterator();
    it.next(); // skip the pivot
    while (it.hasNext()) {
      Integer i = it.next();
      if (i < pivot) {
        left.add(i);
      } else { // put element equal to pivot to right
        right.add(i);
      }
    }
    List<Integer> result = new LinkedList<>();
    result.addAll(quickSort(left));
    result.add(pivot);
    result.addAll(quickSort(right));
    return result;
  }

  public static List<Integer> quickSortCompact(List<Integer> elements) {
    return Arrays.asList(quickSortCompact(elements.toArray(new Integer[0])));
  }

  public static Integer[] quickSortCompact(Integer[] elements) {
    return quickSortCompact(elements, 0, elements.length - 1);
  }

  public static Integer[] quickSortCompact(Integer[] elements, int start, int end) {
    if (start < end) {
      int pivotIndex = partition(elements, start, end);
      quickSortCompact(elements, start, pivotIndex - 1);
      quickSortCompact(elements, pivotIndex + 1, end);
    }
    return elements;
  }

  private static Integer partition(Integer[] elements, int start, int end) {
    Integer pivot = elements[start];
    Integer i = start;
    for (Integer j = start + 1; j <= end; j++) {
      if (elements[j] < pivot) {
        i++;
        swap(elements, i, j);
      }
    }
    swap(elements, start, i);
    return i;
  }

  private static void swap(Integer[] elements, int i, int j) {
    Integer temp = elements[i];
    elements[i] = elements[j];
    elements[j] = temp;
  }

}
