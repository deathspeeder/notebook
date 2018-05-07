
## 线程的创建
Java提供两种创建线程的方式：1.继承Thread类并实现run方法，2.实现Runnable接口。
```Java
public class ThreadDemo {
  public static void main(String[] args) {
    MyThread mt = new MyThread();
    mt.start();
    System.out.println(Thread.currentThread().getName() + " is running ...");
  }
}

class MyThread extends Thread {
  public void run() {
    System.out.println(Thread.currentThread().getName() + " is running ...");
  }
}
```

```Java
public class RunnableDemo {
  public static void main(String[] args) {
    Runnable runer = new Runnable() {
      @Override
      public void run() {
        System.out.println(Thread.currentThread().getName() + " is running ...");
      }
    };

    new Thread(runer).start();
    System.out.println(Thread.currentThread().getName() + " is running ...");
  }
}
```
