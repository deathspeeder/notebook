package com.ms.sorting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MergeSort extends BaseSort {

  public static void main(String[] args) {
    new MergeSort().run();
  }

  @Override
  public List<Integer> sort(List<Integer> elements) {
    List<List<Integer>> lists = new ArrayList<>(elements.size());
    Iterator<Integer> it = elements.iterator();
    while (it.hasNext()) {
      List<Integer> list = new ArrayList<>(1);
      list.add(it.next());
      lists.add(list);
    }
    return mergeToOne(lists);
  }

  private List<Integer> mergeToOne(List<List<Integer>> lists) {
    while (lists.size() > 1) {
      lists = merge(lists);
    }
    return lists.get(0);
  }

  private List<List<Integer>> merge(List<List<Integer>> lists) {
    List<List<Integer>> newLists = new ArrayList<>();

    Iterator<List<Integer>> it = lists.iterator();
    while (it.hasNext()) {
      List<Integer> temp = it.next();
      if (it.hasNext()) {
        temp = merge(temp, it.next());
      }
      newLists.add(temp);
    }
    return newLists;
  }

  private List<Integer> merge(List<Integer> a, List<Integer> b) {
    List<Integer> merged = new ArrayList<>();
    if (a.size() == 0)
      return b;
    if (b.size() == 0)
      return a;

    Integer inta = a.get(0);
    Integer intb = b.get(0);
    if (inta < intb) {
      merged.add(inta);
      merged.addAll(merge(a.subList(1, a.size()), b));
    } else {
      merged.add(intb);
      merged.addAll(merge(a, b.subList(1, b.size())));
    }
    return merged;
  }
}
