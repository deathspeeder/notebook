
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

public class MyThread extends Thread {
  public void run() {
    System.out.println(Thread.currentThread().getName() + " is running ...");
  }
}
```
这里创建类一个`MyThread`类，实现run()方法，输出当前线程的名字。main方法中，新建`MyThread`类的实例后调用了实例的start()方法，该方法会将新的线程状态置为RUNNABLE，新的线程会在被调度到后开始执行。

```Java
public class RunnableDemo {
  public static void main(String[] args) {
    Runnable runner = new Runnable() {
      @Override
      public void run() {
        System.out.println(Thread.currentThread().getName() + " is running ...");
      }
    };

    new Thread(runner).start();
    System.out.println(Thread.currentThread().getName() + " is running ...");
  }
}
```
这里实现Runnable接口后，匿名实例`runner`只说明了如何在新的线程中运行（即run方法），而没有说明以哪个线程去运行。新建一个`Thread`类的实例，将`runner`作为参数传入，并调用线程的start()方法，`runner`所描述的工作才会在新的线程中执行。

这两种方式如何选择呢？当一个类已经继承了别的类了，要将此类当作一个线程，那么就只能实现Runnable接口；而选择将一个类继承Thread类的时候，这个类的名字最好取为一个能发出动作或者执行操作的名词，比如 `Calculator extends Thread` 说明`Calculator`这个类能执行某种操作，比如calculate。

## 线程的状态
Java 线程一般分为5个状态： NEW，RUNNABLE，RUNNING，BLOCKED/WAITING， DEAD。
 - NEW： 当一个Thread类被创建后还未调用start() 之前，线程处于NEW状态；
 - RUNNABLE： 线程调用start()方法过后，调度器还没有选中线程进行执行之前；
 - RUNNING： 线程调度器选中线程，线程开始执行；
 - BLOCKED/WAITING：线程被阻塞或者被挂起；
 - DEAD： 线程被销毁。
 
## 线程同步

在堆上分配内存的对象的成员变量可以被多个线程同时访问，当这些线程可以执行写操作时，这样的共享内存位置就变成了临界区资源。访问临界区资源的代码即为临界代码区，临界代码区需要通过同步机制才能保证临界区资源修改的正确性。

```Java

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SyncDemo {
  public static void main(String[] args) {
    new SyncDemo().demo();
  }

  public void demo() {
    Bank bank = new Bank();
    List<Saver> savers = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      Saver saver = new Saver(bank);
      savers.add(saver);
      saver.start();
    }

    List<Spender> spenders = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      Spender spender = new Spender(bank);
      spenders.add(spender);
      spender.start();
    }

    try {
      for (Saver s : savers) {
        s.join();
      }
      for (Spender s : spenders) {
        s.join();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println(bank.getTotalAmount());
  }

  class Bank {
    private double totalAmount = 0;
    private Random random = new Random();

    public void withdraw(double amount) {
      double newAmount = totalAmount - amount;
      try {
        Thread.sleep((long) Math.random() * 100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      totalAmount = newAmount;
    }

    public void deposit(double amount) {
      double newAmount = totalAmount + amount;
      try {
        Thread.sleep((long) Math.random() * 100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      totalAmount = newAmount;
    }

    public double getTotalAmount() {
      return totalAmount;
    }
  }

  class Saver extends Thread {
    private Bank bank;

    public Saver(Bank bank) {
      this.bank = bank;
    }

    public void run() {
      bank.deposit(100);
    }
  }

  class Spender extends Thread {
    private Bank bank;

    public Spender(Bank bank) {
      this.bank = bank;
    }

    public void run() {
      bank.withdraw(100);
    }
  }
}

```

