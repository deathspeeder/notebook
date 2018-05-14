## 线程池
Java的线程实际映射到操作系统的线程，线程间上下文切换也是由操作系统完成的。如果一个进程频繁的创建大量轻量级线程，那么用于线程创建和销毁的时间跟线程真正运行的时间比起来将变得非常可观，整个并发系统的效率变得低效。线程池是一种通过管理一些可重用线程来执行并发任务的编程模式，其目的是降低线程创建和销毁的资源开销。线程池通常维护一个任务优先队列已经一定数量的可重用线程，当任务提交到线程池时，首先进入队列，只有当可重用线程有闲置位置时，才允许排队任务占用一个可重用线程进行执行。

[Thread Pool Demo Pic]

## Java 中的 Executors， Executor 以及 ExecutorService
Executors是Java中创建和管理Executor已经ExecutorService的工厂类和工具类，比如

```Java
    Executor executor = Executors.newSingleThreadExecutor();
    executor.execute(new Runnable() {
      @Override
      public void run() {
        System.out.println(Thread.currentThread().getName() + " is running ...");
      }
    });
```

Executor接口用于表示一个能执行任务的类，该接口中只定义了一个接口方法

```Java
    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if command is null
     */
    void execute(Runnable command);
```

而ExecutorService则是用于表示一个能执行任务并且能控制任务的执行过程已经结束的细节的接口，ExecutorService是一个Executor

```Java
public interface ExecutorService extends Executor {
  ...
  <T> Future<T> submit(Callable<T> task);
  ...
  Future<?> submit(Runnable task);
  ...
}
```

ExecutorService可以接受一个Callable，比如

```Java
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    Future<String> future = executorService.submit(new Callable<String>() {
      @Override
      public String call() throws Exception {
        return "Hello World";
      }
    });

    String result = future.get();
```

Callable 跟 Runnable的区别在于，Callable可以有返回值以及可以抛出异常
```Java
public interface Callable<V> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    V call() throws Exception;
}
```

## ExecutorService 的关闭
默认情况下 ExecutorService 不会自动关闭，这会让程序在JVM中保持运行状态不会退出。shutdown() 方法使得 ExecutorService 停止接受新的任务，在现有任务执行完成后，ExecutorService 会自动退出。
```Java
executorService.shutdown();
```

而 shutdownNow() 方法让ExecutorService立即退出，但对于正在运行的线程并不能保证正常退出；该方法还会返回所有等待执行的任务：
```Java
List<Runnable> notExecutedTasks = executorService.shutDownNow();
```

一种推荐的关闭 ExecutorService 的方法是，首先调用shutdown()方法停止接受新的任务，然后等待一段时间，再调用shutdownNow()方法强制停止：
```Java
executorService.shutdown();
try {
    if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
        executorService.shutdownNow();
    } 
} catch (InterruptedException e) {
    executorService.shutdownNow();
}
```

## ScheduledExecutorService
ScheduledExecutorService 接口提供的方法可以使任务在设定时间延时后启动，也可以让任务周期性的执行。该接口提供如下一些方法：
 - `schedule (Callable task, long delay, TimeUnit timeunit)`
 - `schedule (Runnable task, long delay, TimeUnit timeunit)`
 - `scheduleAtFixedRate (Runnable, long initialDelay, long period, TimeUnit timeunit)`
 - `scheduleWithFixedDelay (Runnable, long initialDelay, long period, TimeUnit timeunit)`

例如
```Java
ScheduledExecutorService scheduledExecutorService =
        Executors.newScheduledThreadPool(5);

ScheduledFuture scheduledFuture =
    scheduledExecutorService.schedule(new Callable() {
        public Object call() throws Exception {
            System.out.println("Executed!");
            return "Called!";
        }
    },
    5,
    TimeUnit.SECONDS);

System.out.println("result = " + scheduledFuture.get());

scheduledExecutorService.shutdown();
```

创建了一个在5秒钟过后执行的任务。

scheduleAtFixedRate() 方法能创建一个周期性执行的任务，该任务会在初始延时 `initialDelay` 之后开始执行，随后每隔 `period` 周期执行一次。如果任务在执行过程中抛出异常，那么该任务不会再被周期执行；如果任务执行时间超过了周期间隔 `period` ，那么下一次执行会等待当期任务完成后立即开始，同一时间只会有一个任务在执行。

scheduleWithFixedDelay() 跟 scheduleAtFixedRate() 相似，除了时间间隔 `period` 是以上一次任务执行结束的时间开始算起，间隔 `period` 后再开始新的一个周期。

同样，在不使用 ScheduledExecutorService 时，需要将其关闭，否则即便所有线程都结束了，ScheduledExecutorService 也不会退出。

## Folk/Join 线程池
Folk/Join 线程池是 Java 7 引入的用于处理能递归的分解为子任务的多线程任务；在多核CPU上有着性能的优势。详见 [Fork and Join: Java Can Excel at Painless Parallel Programming Too!](http://www.oracle.com/technetwork/articles/java/fork-join-422606.html)

