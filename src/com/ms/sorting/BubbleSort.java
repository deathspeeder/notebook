package com.ms.sorting;

public class BubbleSort extends SimpleSort {

  public static void main(String[] args) {
    new BubbleSort().run();
  }

  @Override
  public Integer[] sort(Integer[] elements) {
    for (int i = 0; i < elements.length; i++) {
      for (int j = 1; j < elements.length; j++) {
        if (elements[j - 1] > elements[j])
          swap(elements, j - 1, j);
      }
    }
    return elements;
  }
}
