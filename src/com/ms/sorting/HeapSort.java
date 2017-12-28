package com.ms.sorting;

public class HeapSort extends SimpleSort {

  public static void main(String[] args) {
    new HeapSort().run();
  }

  @Override
  public Integer[] sort(Integer[] elements) {
    for (int i = elements.length / 2 - 1; i >= 0; i--) {
      heapify(elements, i, elements.length);
    }

    for (int i = elements.length - 1; i > 0; i--) {
      swap(elements, 0, i);
      heapify(elements, 0, i);

    }
    return elements;
  }

  private void heapify(Integer[] elements, int i, int length) {
    int child;
    for (; 2 * i + 1 < length; i = child) {
      child = 2 * i + 1;
      if (child < length - 1 && elements[child + 1] > elements[child])
        ++child;
      if (elements[i] < elements[child])
        swap(elements, i, child);
      else
        break;
    }
  }
}
