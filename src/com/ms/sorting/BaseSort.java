package com.ms.sorting;

import java.util.Arrays;
import java.util.List;

public abstract class BaseSort implements Sort {
  public void run() {

    List<Integer> case1 = Arrays.asList(new Integer[] { 4, 5, 6, 1, 6, 3, 2, 7, 5 });

    List<Integer> case2 = Arrays.asList(new Integer[] { 3, 3, 3, 3 });

    sort("case 1", case1);
    sort("case 2", case2);
  }

  public void debugPrint(List<Integer> elements) {
    elements.stream().map(i -> i.toString()).reduce((i, j) -> i + "," + j).ifPresent(s -> System.out.println(s));
  }

  protected void sort(String name, List<Integer> elements) {
    System.out.println(name);
    debugPrint(elements);
    System.out.println("==== sort list");
    debugPrint(sort(elements));
    System.out.println("==== sort array");
    debugPrint(sortCompact(elements));
    System.out.println("====");
  }

  @Override
  public Integer[] sort(Integer[] elements) {
    return sort(Arrays.asList(elements)).toArray(new Integer[0]);
  }

  @Override
  public List<Integer> sortCompact(List<Integer> elements) {
    return Arrays.asList(sortCompact(elements.toArray(new Integer[0])));
  }

  @Override
  public Integer[] sortCompact(Integer[] elements) {
    return sort(elements);
  }

  protected void swap(Integer[] elements, int i, int j) {
    Integer temp = elements[i];
    elements[i] = elements[j];
    elements[j] = temp;
  }
}
