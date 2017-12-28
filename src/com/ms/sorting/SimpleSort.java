package com.ms.sorting;

import java.util.Arrays;
import java.util.List;

public abstract class SimpleSort extends BaseSort {
  @Override
  public List<Integer> sort(List<Integer> elements) {
    return Arrays.asList(sort(elements.toArray(new Integer[0])));
  }

  @Override
  public abstract Integer[] sort(Integer[] elements);
}
