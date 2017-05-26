[Java Collections Framework Tutorials](http://beginnersbook.com/java-collections-tutorials/)

1、集合概述

现实生活中集合：很多事物凑在一起。

数学中的集合：具有共同属性的事物的总体。

Java中的集合类：是一种工具类，就像是容器，储存任意数量的具有共同属性的对象。在编程时，常常需要集中存放多个数据，当然我们可以使用数组来保存多个对象。但数组长度不可变化，一旦初始化数组时指定了数组长度，则这个数组长度是不可变的，如果需要保存个数变化的数据，数组就有点无能为力了；而且数组无法保存具有映射关系的数据，如成绩表：语文—79，数学—80，这种数据看上去像两个数组，但这个两个数组元素之间有一定的关联关系。

为了保存数量不确定的数据，以及保存具有映射关系的数据（也被称为关联数组），Java提供集合类。集合类主要负责保存其他数据，因此集合类也被称为容器类。所有容器类都位于Java.util包下。集合类和数组不一样，数组元素既可以是基本类型的值，也可以是对象（实际上保存的是对象的引用）；而集合里只能保存对象（实际上也是保存对象的引用，但通常习惯上认为集合里保存的是对象）。
List接口实现类

List接口继承了Collection接口，并对父接口进行了简单的扩充：同时List接口又有三个常用的实现类ArrayList、LinkedList和Vector。

1）ArrayList（数组线性表）

ArrayList数组线性表的特点为：用类似数组的形式进行存储，因此它的随机访问速度极快。ArrayList数组线性表的缺点为：不适合于在线性表中间频繁地进行插入和删除操作。因为每次插入和删除都需要移动数组中的元素。可以这样理解ArrayList就是基于数组的一个线性表，只不过数组的长度可以动态改变而已。

对于使用ArrayList的开发者而言，下面几点内容一定要注意啦，尤其找工作面试的时候经常会被问到。

①如果在初始化ArrayList的时候没有指定初始化长度的话，默认的长度为10，源码中就是这样设置的：

Java集合框架完全解析

②ArrayList在增加新元素的时候如果超过了原始的容量的话，ArrayList扩容的方案为：上一次的容量*1.5+1。代码如下（Java1.8中ArrayList的扩容代码已经变了，这里暂时没有更新）：

Java集合框架完全解析

③ArrayList是线程不安全的，在多线程的情况下不要使用。如果一定在多线程使用List，可以使用Vector，因为Vector和ArrayList基本一致，区别在于Vector中的绝大部分方法都使用了同步关键字修饰，这样在多线程的情况下不会出现并发错误，还有就是它们的扩容方案不同，ArrayList是扩容方案是：原始容量*3/2+1，而Vector允许设置默认的增长长度，Vector的默认扩容方式为原来的2倍。切记Vector是ArrayList的多线程的一个替代品。

④ArrayList实现遍历的几种方法

Java集合框架完全解析

2）LinkedList（链式线性表）

LinkedList的链式线性表的特点为：适用于需要在链表中间频繁进行插入和删除操作的场合。LinkedList的链式线性表的缺点为：随机访问速度较慢。查找一个元素需要从头开始一个一个的找。LinkedList是用双向循环链表实现的。对于使用LinkedList的开发者而言，下面几点内容一定要注意啦，尤其找工作面试的过程时候经常会被问到。

①简述LinkedList和ArrayList的区别和联系。

ArrayList是实现了基于动态数组的数据结构，LinkedList是基于链表的数据结构。ArrayList数组线性表的特点为：类似数组的形式进行存储，内存连续，因此它的随机访问速度极快。ArrayList数组线性表的缺点为：不适合于在线性表中间需要频繁进行插入和删除操作。因为每次插入和删除都需要移动数组中的元素。LinkedList的链式线性表的特点为：适合于在链表中间需要频繁进行插入和删除操作。LinkedList的链式线性表的缺点为:随机访问速度较慢。查找一个元素需要从头开始一个一个的找。

②LinkedList的内部实现是怎样的

对于这个问题，最好看一下jdk中LinkedList的源码。这样会醍醐灌顶的。LinkedList的内部是用基于双向循环链表的结构来实现的。在LinkedList中有一个类似于C语言中结构体的Entry内部类。在Entry的内部类中包含了前一个元素的地址引用和后一个元素的地址引用类似于C语言中指针

③LinkedList不是线程安全的

注意LinkedList和ArrayList一样也不是线程安全的，如果要在多线程并发环境中使用LinkedList，需要在要求同步的方法上加上同步关键字synchronized。

3）Vector（向量）

Vector和ArrayList几乎是完全相同的，唯一的区别在于Vector是同步类(synchronized)，即线程安全的。因此，开销就比ArrayList要大，正常情况下，大多数的Java程序员使用ArrayList而不是Vector，因为同步完全可以由程序员自己来控制。

引申：线程安全就是多线程访问时，采用了加锁机制，当一个线程访问该类的某个数据时，进行保护，其他线程不能进行访问直到该线程读取完，其他线程才可使用。不会出现数据不一致或者数据污染。线程不安全就是不提供数据访问保护，有可能出现多个线程先后更改数据造成所得到的数据是脏数据。
HashTable和HashMap区别主要集中在线程安全性、同步（synchronization）和速度上，分别有以下几点：

1）继承层次不同，二者都实现了Map接口，但HashTable继承了Dictionary类，而HashMap继承了AbstractMap类。

Java集合框架完全解析

2）在HashTable中，key和value都不允许出现null值。在HashMap中，null可以作为键，这样的键只有一个；可以有一个或多个键所对应的值为null。当get()方法返回null值时，既可以表示HashMap中没有该键，也可以表示该键所对应的值就是null。因此，在HashMap中不能由get()方法来判断HashMap中是否存在某个键，而应该用containsKey()方法来判断。

3）HashMap是非synchronized，而HashTable是synchronized，这意味着HashTable是线程安全的，多个线程可以共享一个HashTable；而如果程序员没有手工进行正确的同步的话，多个线程是不能共享HashMap的。由于HashTable是线程安全的也是synchronized，所以在单线程环境下它比HashMap要慢。如果你不需要同步，只应用于单线程，那么使用HashMap性能要好过HashTable。HashMap可以通过下面的语句进行同步：

Java集合框架完全解析

4）哈希值的使用不同，Hashtable直接使用对象的HashCode，根据key值计算index的代码是这样的：

Java集合框架完全解析

当hash数组的长度较小，并且Key的hashCode低位数值分散不均匀时，不同的hash值计算得到相同下标值的几率较高。HashMap不直接使用对象的HashCode，而是重新计算hash值，而且用与运算代替了求模运算，代码如下：

Java集合框架完全解析

这种计算方式优于HashTable，通过对Key的hashCode做移位运算和位的与运算，使其能更广泛地分散到数组的不同位置上去。

5）HashTable中hash数组默认大小是11，初始化时可以指定initial capacity（数组初始长度），扩容方式是old*2+1。HashMap中hash数组的默认大小是16，而且长度始终保持为2的n次方，初始化时同样可以指定initial capacity（数组初始长度），若不是2的次方，HashMap将选取第一个大于initial capacity的2n次方值作为其初始长度。

6）遍历方式的内部实现上不同。HashTable与HashMap都使用了Iterator。而由于历史原因（HashTable继承了Dictionary类），HashTable还使用了Enumeration的方式。一般单线程情况下，HashMap能够比HashTable工作得更好、更快，主要得益于它的散列算法，以及没有作线程同步。应用程序一般在更高的层面上实现了保护机制，而不是依赖于这些底层数据结构的同步，因此，HashMap能够在大多数应用中满足需要。推荐使用HashMap，如果需要同步，可以使用同步工具类将其转换成支持同步的HashMap。

