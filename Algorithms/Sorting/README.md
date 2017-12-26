## 经典排序
传统意义上的排序是指，针对一个数组里的元素按照某种大小规则将其排序为一个递增的顺序，而且整个排序过程是在单线程里完成的。经典排序的最优平均时间复杂度为`O(n log_n)`，很多著名排序算法都是这样的时间复杂度，比如快速排序，堆排序，归并排序等。

### 即使所有都忘记也不能忘记快速排序（Quick sort）

排序算法很多，但对于经典排序而言，当别人问到最快的排序是什么时，我们应该立马告诉他是快速排序。从名字也能看出，它要是不快，干嘛叫快速？

快速排序的思想是“分治法”，即将一个复杂的问题分解成两个或者多个相似的问题逐个击破，然后将结果合并起来得到问题的答案。对于快速排序而言就是，选取一个元素作为分解点（Pivot），将所有要排序的元素按照分界点划分为两组，左边一组为小于分界点的元素，右边一组为大于的，这样整个数组的排序问题被分解为两个子数组的排序问题，当这两个子数组的排序问题解决后，整个数组的排序就是左边数组加上分界点在加右边数组。每个子数组的求解可使用递归的方式，假设定义整个排序问题为`S(n)`，选取的分界点为`p`，递归公式可以简单的写为`S(n)=S(left)+p+S(right)`，其中`S(left)`所有元素小于`p`，·S(right)·所有元素大于`p`。对于元素等于`p`的情况，可以随意归类为左边或者右边一组。

用Java写出来就是
```Java
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
```
这个实现很直观的反应了快速排序的思想，但这个实现用到了很多用于保存中间结果的List，这些List会占用空间，这些额外占用的空间的复杂度是`n log_n`。这个复杂度是怎么算出来的呢，首先，整个排序过程有`log_n`次迭代，每次迭代都创建了临时数组来保存元素，复杂度是`n`，乘起来就是`n log_n`。从维基百科可以看到快速排序的平均空间复杂度是`log_n`，显然这个实现在空间复杂度上并不是最优的，最优的实现应该是将这个实现用于保存的临时数组的空间变成常数值，即每次迭代用常数值的空间，这样才能做到`log_n`。写出来就是

```Java
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

  protected void swap(Integer[] elements, int i, int j) {
    Integer temp = elements[i];
    elements[i] = elements[j];
    elements[j] = temp;
  }
```
- 代码：[QuickSort.java](QuickSort.java)
- 参考：[维基百科Quick sort](https://en.wikipedia.org/wiki/Quicksort)

### 那些`O(n^2)`的简单粗暴排序

包括冒泡排序（Bubble sort），插入排序（Insertion sort），选择排序（Selection sort）以及这三者的其他变种。

冒泡排序完整遍历整个数组`n`次，每次遍历都比较相邻两个元素，如果顺序不对则进行交换。所以需要进行比较的次数是`n*(n-1)`次。这个排序算法主要用于讲解程序语言循环的作用，实际中因性能太差不会被用到。
```Java
  public Integer[] sort(Integer[] elements) {
    for (int i = 0; i < elements.length; i++) {
      for (int j = 1; j < elements.length; j++) {
        if (elements[j - 1] > elements[j])
          swap(elements, j - 1, j);
      }
    }
    return elements;
  }
```

插入排序从头到尾遍历数组，每次遍历把当前元素插入到已经遍历过的子数组中，该子数组一直保持排序状态。最优情况是输入数组已经排序，只需要做`n-1`次比较，不需要进行交换，复杂度为`O(n)`；最差情况是输入数组倒序，需要进行`1+2+3+...+(n-1)=n*(n-1)/2`次比较和交换，复杂度为`O(n^2)`；平均复杂度为`O(n^2)`。插入排序因为最优情况复杂度低，所以是简单排序里面最为有用的，常常被用于高效排序中子排序方法，比如快速排序分治的过程中，如果子过程的数组长度足够小的时候，再进行分治的效率并不会比直接使用插入排序高，这时便可以使用插入排序。
```Java
  public Integer[] sort(Integer[] elements) {
    for (int i = 0; i < elements.length; i++) {
      for (int j = i - 1; j >= 0; j--) {
        if (elements[j] > elements[j + 1])
          swap(elements, j, j + 1);
      }
    }
    return elements;
  }
```

选择排序跟插入排序类似，也是从头到尾遍历，已经遍历过的子数组是保持排序的。不同的是每次遍历会去未排序部分数组里找到最小元素放在当前位置。选择排序比插入排序差是因为即便是已经排序的数组，为了找到最小值，每次遍历还是会比较当前位置之后所有元素。其复杂度没有最优情况和最差情况，统统为`O(n^2)`。
```Java
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
```

- 代码：[BubbleSort.java](BubbleSort.java)
- 代码：[InsertionSort.java](InsertionSort.java)
- 代码：[SelectionSort.java](SelectionSort.java)
- 参考：[维基百科Bubble sort](https://en.wikipedia.org/wiki/Bubble_sort)
- 参考：[维基百科Insertion sort](https://en.wikipedia.org/wiki/Insertion_sort)
- 参考：[维基百科Selection sort](https://en.wikipedia.org/wiki/Selection_sort)

### 那些`O(nlog_n)`的高效排序

归并排序（Merge sort）将数组元素划分为`n`个链表，每个链表一个元素，然后重复地将所有链表两两合并直到合并成一个链表。最差情况`O(nlog_n)`，最好情况`O(n)`，平均`O(nlog_n)`。
```Java
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
```

堆排序
TODO

- 代码：[MergeSort.java](MergeSort.java)
- 参考：[维基百科Merge sort](https://en.wikipedia.org/wiki/Merge_sort)

## 分布式排序

## 参考
[维基百科](https://en.wikipedia.org/wiki/Sorting_algorithm)
