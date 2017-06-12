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
