package com.ms.sorting;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class QuickSort extends BaseSort {

  public static void main(String[] args) {
    new QuickSort().run();
  }

  @Override
  public List<Integer> sort(List<Integer> elements) {
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
    result.addAll(sort(left));
    result.add(pivot);
    result.addAll(sort(right));
    return result;
  }

  @Override
  public Integer[] sortCompact(Integer[] elements) {
    return sortCompact(elements, 0, elements.length - 1);
  }

  public Integer[] sortCompact(Integer[] elements, int start, int end) {
    if (start < end) {
      int pivotIndex = partition(elements, start, end);
      sortCompact(elements, start, pivotIndex - 1);
      sortCompact(elements, pivotIndex + 1, end);
    }
    return elements;
  }

  private Integer partition(Integer[] elements, int start, int end) {
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

}
