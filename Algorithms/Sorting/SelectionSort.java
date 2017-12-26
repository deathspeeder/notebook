package com.ms.sorting;

public class SelectionSort extends SimpleSort {

  public static void main(String[] args) {
    new SelectionSort().run();
  }

  @Override
  public Integer[] sort(Integer[] elements) {
    for (int i = 0; i < elements.length; i++) {
      Integer minValue = elements[i];
      int minValuePosition = i;
      for (int j = i + 1; j < elements.length; j++) {
        if (elements[j] < minValue) {
          minValue = elements[j];
          minValuePosition = j;
        }
      }
      swap(elements, i, minValuePosition);
    }
    return elements;
  }
}
