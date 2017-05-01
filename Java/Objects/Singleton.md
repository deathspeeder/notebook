当一个进程当中一个类只需要被创建一次或者一组静态方法只需要在一处定义时，单例模式是面向对象的常用解决方案。在单例模式中，一个类只允许创建一个实例对象。单例模式有很多实现方式，下面一一总结。

## 饿汉式
顾名思义，假设类是很饥饿的，类在被加载后初始化的时候创建实例。

```Java
public class Singleton{
    //类加载时就初始化
    private static final Singleton instance = new Singleton();

    private Singleton(){}
    public static Singleton getInstance(){
        return instance;
    }
}
```


## 懒汉式
饿汉式的问题是，假如有许多个单例的类都采用了这种方式，那么JVM在加载这些类的时候会花费较长的时间去创建单例对象。懒汉式可以解决这个问题，该方式在单例的对象需要时执行新建。
```Java
public class Singleton {
    private static Singleton instance;
    private Singleton (){}
    public static Singleton getInstance() {
     if (instance == null) {
         instance = new Singleton();
     }
     return instance;
    }
}
```
这段代码简单明了，在调用```getInstance```的时候，如果没有创建单例对象则创建，然后返回单例对象。看似完美了，但是在多线程的情况下会有问题：当多个线程同时调用```getInstance```的时候，可能有多余一个线程同时（这里的同时是指两个线程执行的间隔小到CPU几乎是先后执行了两个线程的这行代码）执行到```if(instance == null)```这一行，发现实例没有被创建，于是这些线程都去创建实例了，最终```instance```的值是最后创建的那个线程创建的实例对象，其他执行创建的线程创建的实例对象就变成了“野对象”，也就是没有引用指向他们，最终会被垃圾回收掉，但他们白白浪费CPU计算资源去创建对象。

懒汉式在多线程下首先是有上述这个问题的，但是能不能工作还要看这个单例的构造函数做了什么。假如构造函数执行一遍跟多遍没有区别（比如是空的），那么懒汉式在多线程下最差情况也就是多创建了几个“野对象”而已；但是如果这个构造函数执行了一些不能做多次的任务（比如读写文件资源），或者一些开销很大的任务（比如数据库初始化），那么懒汉式是会有很大问题的，甚至导致程序崩溃。

## 懒汉式，线程安全
懒汉式的多线程不安全问题的解决方法是让它安全。一种直白的方式是用 ```synchronized``` 关键字。
```Java
public class Singleton {
    private static Singleton instance;
    private Singleton (){}
    public static synchronized Singleton getInstance() {
      if (instance == null) {
          instance = new Singleton();
      }
      return instance;
  }
}
```

```synchronized``` 关键字保证了```getInstance```方法不会让两个以上的线程同时进去。看似问题完美解决了，但这个方式的主要问题是性能差。```synchronized```的实现方式是通过加锁解锁，每次一个线程需要进入方法时加互斥锁，如果加不上则阻塞等待，加上了就执行，执行完成后才释放锁。在多线程环境下，许多线程同时需要这个单例时会频繁调用```getInstance```，频繁的加锁和解锁会大幅降低性能。

仔细分析发现，真正需要加锁互斥的代码是对象新建这一行，也就是 ```instance = new Singleton()```，同步操作只需要在第一次调用时才被需要，即第一次创建单例实例对象时。这就引出了双重检验锁。

## 双重检验锁
双重检验锁模式（double checked locking pattern），是一种使用同步块加锁的方法。程序员称其为双重检查锁，因为会有两次检查 ```instance == null```，一次是在同步块外，一次是在同步块内。为什么在同步块内还要再检验一次？因为可能会有多个线程一起进入同步块外的 if，如果在同步块内不进行二次检验的话就会生成多个实例了。

```Java
public static Singleton getSingleton() {
    if (instance == null) {                         //Single Checked
        synchronized (Singleton.class) {
            if (instance == null) {                 //Double Checked
                instance = new Singleton();
            }
        }
    }
    return instance;
}
```

这段代码看起来很完美，很可惜，它是有问题。主要在于```instance = new Singleton()```这句，这并非是一个原子操作，事实上在 JVM 中这句话大概做了下面 3 件事情。
 - 给 instance 分配内存
 - 调用 Singleton 的构造函数来初始化成员变量
 - 将instance对象指向分配的内存空间（执行完这步 instance 就为非 null 了）

但是在 JVM 的即时编译器中存在指令重排序的优化。也就是说上面的第二步和第三步的顺序是不能保证的，最终的执行顺序可能是 1-2-3 也可能是 1-3-2。如果是后者，则在 3 执行完毕、2 未执行之前，被线程二抢占了，这时 instance 已经是非 null 了（但却没有初始化），所以线程二会直接返回 instance，然后使用，然后顺理成章地报错。

解决办法只需要将 instance 变量声明成 volatile 就可以了。

```Java
public class Singleton {
    private volatile static Singleton instance; //声明成 volatile
    private Singleton (){}
    public static Singleton getSingleton() {
        if (instance == null) {                         
            synchronized (Singleton.class) {
                if (instance == null) {       
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

}
```

使用 volatile 的主要原因是禁止指令重排序优化。也就是说，在 volatile 变量的赋值操作后面会有一个内存屏障（生成的汇编代码上），读操作不会被重排序到内存屏障之前。比如上面的例子，取操作必须在执行完 1-2-3 之后或者 1-3-2 之后，不存在执行到 1-3 然后取到值的情况。从「先行发生原则」的角度理解的话，就是对于一个 volatile 变量的写操作都先行发生于后面对这个变量的读操作（这里的“后面”是时间上的先后顺序）。

但是特别注意在 Java 5 以前的版本使用了 volatile 的双检锁还是有问题的。其原因是 Java 5 以前的 JMM （Java 内存模型）是存在缺陷的，即时将变量声明成 volatile 也不能完全避免重排序，主要是 volatile 变量前后的代码仍然存在重排序问题。这个 volatile 屏蔽重排序的问题在 Java 5 中才得以修复，所以在这之后才可以放心使用 volatile。

这个双重检验锁的方式看起来很是笨拙，通过静态初始化的饿汉式则看起来简洁很多了，但是饿汉式的创建方式在一些场景中将无法使用：譬如 Singleton 实例的创建是依赖参数或者配置文件的，在 getInstance() 之前必须调用某个方法设置参数给它，那样这种单例写法就无法使用了。

## 静态内部类
跟饿汉式相似的还有静态内部类方法。
```Java
public class Singleton {  
    private static class SingletonHolder {  
        private static final Singleton INSTANCE = new Singleton();  
    }  
    private Singleton (){}  
    public static final Singleton getInstance() {  
        return SingletonHolder.INSTANCE;
    }  
}
```

这种方式比饿汉式的优越之处在于他是懒加载的，也就是说在调用```getInstance```的时候类```SingletonHolder```才会被加载，这时单例对象才会被创建。这种方式也是推荐的工程实用方式。

## 枚举
用枚举写单例实在太简单了！这也是它最大的优点。下面这段代码就是声明枚举实例的通常做法。但很少有人这么去实现单例。
```Java
public enum EasySingleton{
    INSTANCE;
}
```


## 参考
[10 Singleton Pattern Interview questions in Java](http://javarevisited.blogspot.com/2011/03/10-interview-questions-on-singleton.html)
