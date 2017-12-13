## 经典排序
传统意义上的排序是指，针对一个数组里的元素按照某种大小规则将其排序为一个递增的顺序，而且整个排序过程是在单线程里完成的。经典排序的最优平均时间复杂度为`O(n log_n)`，很多著名排序算法都是这样的时间复杂度，比如快速排序，堆排序，归并排序等。

### 即使所有都忘记也不能忘记快速排序（QuickSort）

排序算法很多，但对于经典排序而言，当别人问到最快的排序是什么时，我们应该立马告诉他是快速排序。从名字也能看出，它要是不快，干嘛叫快速？

快速排序的思想是“分治法”，即将一个复杂的问题分解成两个或者多个相似的问题逐个击破，然后将结果合并起来得到问题的答案。对于快速排序而言就是，选取一个元素作为分解点（Pivot），将所有要排序的元素按照分界点划分为两组，左边一组为小于分界点的元素，右边一组为大于的，这样整个数组的排序问题被分解为两个子数组的排序问题，当这两个子数组的排序问题解决后，整个数组的排序就是左边数组加上分界点在加右边数组。每个子数组的求解可使用递归的方式，假设定义整个排序问题为`S(n)`，选取的分界点为`p`，递归公式可以简单的写为`S(n)=S(left)+p+S(right)`，其中`S(left)`所有元素小于`p`，·S(right)·所有元素大于`p`。对于元素等于`p`的情况，可以随意归类为左边或者右边一组。

用Java写出来就是
```Java
  public static List<Integer> quickSort(List<Integer> elements) {
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
    result.addAll(quickSort(left));
    result.add(pivot);
    result.addAll(quickSort(right));
    return result;
  }
```
这个实现很直观的反应了快速排序的思想，但这个实现用到了很多用于保存中间结果的List，这些List会占用空间，这些额外占用的空间的复杂度是`n log_n`。这个复杂度是怎么算出来的呢，首先，整个排序过程有`log_n`次迭代，每次迭代都创建了临时数组来保存元素，复杂度是`n`，乘起来就是`n log_n`。从维基百科可以看到快速排序的平均空间复杂度是`log_n`，显然这个实现在空间复杂度上并不是最优的，最优的实现应该是将这个实现用于保存的临时数组的空间变成常数值，即每次迭代用常数值的空间，这样才能做到`log_n`。写出来就是

```Java
  public static List<Integer> quickSortCompact(List<Integer> elements) {
    return Arrays.asList(quickSortCompact(elements.toArray(new Integer[0])));
  }

  public static Integer[] quickSortCompact(Integer[] elements) {
    return quickSortCompact(elements, 0, elements.length - 1);
  }

  public static Integer[] quickSortCompact(Integer[] elements, int start, int end) {
    if (start < end) {
      int pivotIndex = partition(elements, start, end);
      quickSortCompact(elements, start, pivotIndex - 1);
      quickSortCompact(elements, pivotIndex + 1, end);
    }
    return elements;
  }

  private static Integer partition(Integer[] elements, int start, int end) {
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

  private static void swap(Integer[] elements, int i, int j) {
    Integer temp = elements[i];
    elements[i] = elements[j];
    elements[j] = temp;
  }
```
- 代码：[QuickSort.java](QuickSort.java)
- 参考：[维基百科QuickSort](https://en.wikipedia.org/wiki/Quicksort)

## 分布式排序

## 参考
[维基百科](https://en.wikipedia.org/wiki/Sorting_algorithm)
