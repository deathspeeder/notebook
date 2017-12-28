package com.ms.sorting;

public class InsertionSort extends SimpleSort {

  public static void main(String[] args) {
    new InsertionSort().run();
  }

  @Override
  public Integer[] sort(Integer[] elements) {
    for (int i = 0; i < elements.length; i++) {
      for (int j = i - 1; j >= 0; j--) {
        if (elements[j] > elements[j + 1])
          swap(elements, j, j + 1);
      }
    }
    return elements;
  }
}
