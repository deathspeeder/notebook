1. 我们能将 int 强制转换为 byte 类型的变量吗？如果该值大于 byte 类型的范围，将会出现什么现象？

是的，我们可以做强制转换，但是 Java 中 int 是 32 位的，而 byte 是 8 位的，所以，如果强制转化是，int 类型的高 24 位将会被丢弃，byte 类型的范围是
从 -128 到 127。我们都知道byte类型的取值范围是-128—127，如果我在代码中定义一个byte类型的变量a，并且给他赋值127，及byte a = 127。我再定义一个
byte b = a + 1；
很显然它的值会超出byte的取值范围，而它会变成-128,（任何整型类型的最大值+1，结果返回该类型的最小值。） 

2.JRE、JDK、JVM 及 JIT 之间有什么不同？

JRE 代表 Java 运行时（Java run-time），是运行 Java 应用所必须的。JDK 代表 Java 开发工具（Java development kit），是 Java 程序的开发工具，
如 Java 编译器，它也包含 JRE。JVM 代表 Java 虚拟机（Java virtual machine），它的责任是运行 Java 应用。JIT 代表即时编译（Just In Time compilation），当代码执行的次数超过一定的阈值时，会将 Java 字节码转换为本地代码，如，主要的热点代码会被准换为本地代码，这样有利大幅度提高 Java 应用的性能。


3.写一段代码在遍历 ArrayList 时移除一个元素

第一种错误的实现方式
  public  static void remove(ArrayList list,String str){
	  
	  for(int i=0;i<list.size();i++){
		  String s=(String) list.get(i);
		  if(s.equals(str)){
			  list.remove(s);
		  }
	  }	  
  }
  
第二种正确的倒序遍历的实现方式
  public  static void remove1(ArrayList list,String str){
  
	  for(int i=list.size()-1;i>=0;i--){
		  String s=(String) list.get(i);
		  if(s.equals(str)){
			  list.remove(s);
		  }
	  }
	  
  }
  
  第三种使用迭代器的遍历实现方式
  public static void remove2(ArrayList list,String str){
  
	  Iterator it=list.iterator();
	  while(it.hasNext()){
		  String s =(String) it.next();
		  if(s.equals(str)){
			  it.remove();
		  }
	  }
  }
  
  4.volatile 
  
  Java 语言提供了一种稍弱的同步机制,即volatile变量.用来确保将变量的更新操作通知到其他线程,保证了新值能立即同步到主内存,以及每次使用前立即从主内存刷新。 当把变量声明为volatile类型后,编译器与运行时都会注意到这个变量是共享的。volatile修饰变量,每次被线程访问时强迫其从主内存重读该值,修改后再写回共享内存。保证读取的可见性,对其他线程立即可见。由于不保证原子性,也就不能保证线程安全。由于及时更新，很可能导致另一线程访问最新变量值，无法跳出循环的情况。同时,volatile屏蔽了VM中必要的代码优化,效率上较低。另一个优点：禁止指令重排序（什么是指令重排序：是指CPU采用了允许将多条指令不按程序规定的顺序分开发送给各相应电路单元处理）
  在此解释一下Java的内存机制：

Java使用一个主内存来保存变量当前值，而每个线程则有其独立的工作内存。线程访问变量的时候会将变量的值拷贝到自己的工作内存中，这样，当线程对自己工作内存中的变量进行操作之后，就造成了工作内存中的变量拷贝的值与主内存中的变量值不同。

Java语言规范中指出：为了获得最佳速度，允许线程保存共享成员变量的私有拷贝，而且只当线程进入或者离开同步代码块时才与共享成员变量的原始值对比。

这样当多个线程同时与某个对象交互时，就必须要注意到要让线程及时的得到共享成员变量的变化。



5.接口与抽象类

一个子类只能继承一个抽象类,但能实现多个接口

抽象类可以有构造方法,接口没有构造方法

抽象类可以有普通成员变量,接口没有普通成员变量

抽象类和接口都可有静态成员变量,抽象类中静态成员变量访问类型任意，接口只能public static final(默认)

抽象类可以没有抽象方法,抽象类可以有普通方法,接口中都是抽象方法

抽象类可以有静态方法，接口不能有静态方法

抽象类中的方法可以是public、protected和默认;接口方法只有public


