在计算机科学中，内存泄漏指由于疏忽或错误造成程序未能释放已经不再使用的内存。内存泄漏并非指内存在物理上的消失，而是应用程序分配某段内存后，由于设计错误，导致在释放该段内存之前就失去了对该段内存的控制，从而造成了内存的浪费。（[维基百科](https://zh.wikipedia.org/wiki/%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F)）

在 C/C++ 语言中用户程序需要手动管理分配的内存，如果没有正确及时的释放内存，内存泄露就会发生，甚至很容易减少可用内存的数量从而降低计算机的性能，最终，在最糟糕的情况下，过多的可用内存被分配掉导致全部或部分设备停止正常工作，或者应用程序崩溃。

提供自动内存管理的编程语言如Java、C#、VB.NET以及LISP，都不能避免内存泄漏。自动内存管理的语言一般会提供垃圾回收机制，但这种机制 **只会回收那些没有被引用的对象**。用户程序一旦创建了一些对象（尤其是通过第三方库创建）而没有显示地申明不再使用（比如关闭数据库连接）或者一直持有其引用但程序逻辑不会再使用到了，内存泄露便发生了。

Java内存泄露的常见场景如下

## 可变静态成员变量
**静态成员变量是不会被垃圾回收机制回收的**，在静态成员变量中创建了对象而不再使用便是一种泄露。比如单例模式的实现方式——饿汉便可能是这种情况，饿汉模式首先创建单例，保存在静态成员变量中，然而这个单例类并不一定会被用到。所以从最佳实践来讲，应避免使用可变的静态成员变量；静态成员变量只用于存储常量。

```Java
class MemorableClass {
    static final ArrayList list = new ArrayList(100);
}
```
以上代码创建了一个初始化长度为100的ArrayList，如果这个集合以后不会被使用或者使用量远小于100，则是一种不合理。

## ThreadLocal 变量
ThreadLocal 变量 保存的值只有随着其对应的线程消亡时才会被标记回收，如果对应线程不能正常结束，而也没有显示地删除ThreadLocal 变量，内存泄露便会发生。

更多信息参考[详解](http://blog.xiaohansong.com/2016/08/06/ThreadLocal-memory-leak/)

## ClassLoader
通过ClassLoader来动态创建对象的时候，如果对象正好使用了ThreadLocal变量，当这个对象不再会被使用时很可能会泄露，比如

 1. 创建一个一直运行的线程（或者使用一个线程池）
 2. 该线程通过ClassLoader加载一个类
 3. 该类分配一大段内存（比如 `new byte[1000000]`）在静态成员变量中储存一个对这段内存的引用，然后将对象自身的引用储存在ThreadLocal变量中（分配大内存不是必须的，但会加速内存泄露）
 4. 线程清除掉被加载的类以及用于加载的ClassLoader
 5. 重复以上几步

传统的J2EE应用服务器（比如Tomcat）往往会出现这样的内存泄露，当应用服务重复部署新的应用的时候，每次部署一个新的ClassLoader回去加载应用的类，如果应用大代码中像以上描述的那样使用了ThreadLocal变量，应用服务器很快便会因内存泄露而崩溃；这时不得不重启。

## 未关闭的资源
未关闭的文件流，网络连接流，数据库连接等并不会被标记为可以回收，因为底层实现任然保存这些连接，底层实现并不知道用户是因为忘记关闭还是故意不关闭以后还会用。当这样的未关闭连接数量增加时内存泄露会变得越来越明显。

```Java
try {
    BufferedReader br = new BufferedReader(new FileReader(inputFile));
    ...
    ...
} catch (Exception e) {
    e.printStacktrace();
}
```

```Java
try {
    Connection conn = ConnectionFactory.getConnection();
    ...
    ...
} catch (Exception e) {
    e.printStacktrace();
}
```

## JNI 内存泄露
Java本机接口（JNI）内存泄漏特别困难，很难找到。 JNI用于从Java调用本地代码。 此本机代码可以处理，调用和创建Java对象。 在本地方法中创建的每个Java对象都将以本地引用的方式开始，这意味着在本机方法返回之前，对象将被引用。 我们可以说本机方法引用Java对象，所以除非本机方法永远运行，否则不会有问题。 在某些情况下，即使在本地调用结束后，您也想保留创建的对象。 要实现这一点，您可以确保它被其他Java对象引用，也可以将本地引用更改为全局引用。 全局引用是GC根，并且永远不会被删除，直到它被本地代码显式释放。

## 参考
- http://stackoverflow.com/questions/6470651/creating-a-memory-leak-with-java
- https://www.dynatrace.com/resources/ebooks/javabook/memory-leaks/
- https://www.ibm.com/developerworks/library/j-leaks/
