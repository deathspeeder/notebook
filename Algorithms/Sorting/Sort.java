package com.ms.sorting;

import java.util.List;

public interface Sort {
  List<Integer> sort(List<Integer> elements);
  Integer[] sort(Integer[] elements);
  List<Integer> sortCompact(List<Integer> elements);
  Integer[] sortCompact(Integer[] elements);
}
