
String即是一串字符串。在Java中，String对象是不可变对象，不可变体现在它一旦被创建便不能被改变。这可以从String的源代码看出来，首先先看String的类声明
```Java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];
    ...
```

这里指明了String类是`final`的，即不能被继承。存储字符串的成员变量叫`value`，是一个`char`的数组，String所有public方法中除了构造函数之外，没有其他函数能修改`value`的值。这样一来保证了String对象一旦被创建，没有办法对其进行修改。

创建一个String对象有两种办法：
1. 创建String常量
2. 使用new来创建

```Java
String str1 = "Welcome";
```
这里`"Welcome"`是一个String常量，编译器会创建一个String对象值为这个常量，`=`将这个对象赋值给了变量`str1`，`str1`是一个指向这个String对象的引用。`str1`这个引用的内容（即`"Welcome"`）不能被修改，但是可以指向别的常量或者String对象，比如

```Java
String str1 = "Welcome";
str1 = "Hello World";
```

使用new来创建String对象比较少见，但是可行
```Java
String str2 = new String("Welcome");
```

## 存储
String在Java中是一个非常特殊的类，它的所有实例对象的值（即value）分配的内存统一在一片叫“常量池”的内存区域。在常量池中，相同的String常量（相同是指equals返回true）只会被创建一遍。

再回到String对象的两种创建方式，第一种`String str1 = "Welcome";`这行代码在执行时包含以下几步：
1. 编译器在常量池寻找值为"Welcome"的String对象，找不到则创建一个
2. 将这个在常量池的String对象的引用赋值给变量`str1`

这就很好解释如果再执行`String str3 = "Welcome";`便不会再创建新的String对象，而是将原来的常量池中的"Welcome"的引用赋值给`str3`。

再来看`String str2 = new String("Welcome");`这行代码为什么不常用，因为这行代码会创建一个新的String对象，而其值却是跟"Welcome"常量的值一样。看一下String对应这行的构造函数
```Java
public String(String original) {
    this.value = original.value;
    this.hash = original.hash;
}
```
该构造函数将`original`的值和哈希赋给新的String对象，即，通过new创建的String对象会在堆（Heap）里分配内存，但其值跟常量池中对应的String对象值是一样的而且是同一片内存，即value的内存在常量池中。

Java编译器还会对String的拼接做一些常量的优化，如
```Java
String str4 = "str";
String str5 = "ing";

String str6 = "str" + "ing";
String str7 = str4 + str5;
System.out.println(str6 == str7);//false

String str8 = "string";
System.out.println(str6 == str8);//true
```
这里为什么str6和str7不是同一个而str6和str8却是呢？答案是编译器如果能在编译阶段就能决定一个String对象的值那么会将String对象统一放到常量池中，如果只有在运行时（这里指有普通变量参与时）才能决定的String对象则不会放到常量池中。str6是由两个常量"str"和"ing"拼接，编译器能够在不执行这行代码的情况下就能决定str6的值是"string"，所以代码`String str6 = "str" + "ing";`对于编译器来讲等价于`String str6 = "string";`，这便解释了str6和str8是同一个对象。而`String str7 = str4 + str5;`不是由常量拼接起来，对编译器来讲需要去推演str4和str5的值是多少，所以在不执行这段代码的情况下不能决定str7的值，所以str7所指的对象会是一个新创建的对象，并不在常量池中。

字符串常量拼接有一种特殊的情况：
```Java
public static final String A = "ab"; // 常量A
public static final String B = "cd"; // 常量B
public static void main(String[] args) {
    String s = A + B;  // 将两个常量用+连接对s进行初始化
    String t = "abcd";
    if (s == t) {   
        System.out.println("s等于t，它们是同一个对象");   
    } else {   
        System.out.println("s不等于t，它们不是同一个对象");   
    }   
}
```
以上代码输出结果是"s等于t，它们是同一个对象"。A和B都是常量，值是固定的，因此s的值也是固定的，它在类被编译时就已经确定了。也就是说：`String s=A+B;` 等同于 `String s="ab"+"cd";`

但是
```Java
public static final String A; // 常量A
public static final String B;    // 常量B
static {   
A = "ab";   
B = "cd";   
}   
public static void main(String[] args) {   
    // 将两个常量用+连接对s进行初始化   
    String s = A + B;   
    String t = "abcd";   
    if (s == t) {   
        System.out.println("s等于t，它们是同一个对象");   
    } else {   
        System.out.println("s不等于t，它们不是同一个对象");   
    }
}
```
输出结果为"s不等于t，它们不是同一个对象"。A和B虽然被定义为常量，但是它们都没有马上被赋值。在运算出s的值之前，他们何时被赋值，以及被赋予什么样的值，都是个变数。因此A和B在被赋值之前，性质类似于一个变量。那么s就不能在编译期被确定，而只能在运行时被创建了。

运行时常量池具备动态性，Java语言并不要求常量一定只有编译期才能产生，也就是并非预置入Class文件中常量池的内容才能进入方法区运行时常量池，运行期间也可能将新的常量放入池中，这种特性被开发人员利用比较多的就是String类的intern()方法。String的intern()方法会查找在常量池中是否存在一份equal相等的字符串,如果有则返回该字符串的引用,如果没有则添加自己的字符串进入常量池。
```Java
public static void main(String[] args) {    
    String s1 = new String("计算机");
    String s2 = s1.intern();
    String s3 = "计算机";
    System.out.println("s1 == s2? " + (s1 == s2));
    System.out.println("s3 == s2? " + (s3 == s2));
}
```
输出结果为
```
s1 == s2? false
s3 == s2? true
```

## Unicode
在上一小节可以看到，String常量里是可以有中文的，Java的字符串使用的编码是Unicode，每个字符的长度通常是16 bit（0x10000 到 0x10FFFF 的Unicode会占用32 bit，详见[Unicode](http://docs.oracle.com/javase/tutorial/i18n/text/unicode.html)），每个字符可以用一个char类型表示。所以在和String中的每个字符打交道的时候要时刻注意字符的编码格式，比如需要将String转化为byte数组的时候需要指定编码：
```Java
import java.io.*;

public class StringConverter {
    public static String byteToHex(byte b) {
        // Returns hex String representation of byte b
        char hexDigit[] = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
        return new String(array);
    }

    public static void printBytes(byte[] array, String name) {
        for (int k = 0; k < array.length; k++) {
            System.out.println(name + "[" + k + "] = " + "0x" +
                    byteToHex(array[k]));
        }
    }

    public static void main(String[] args) {

        System.out.println(System.getProperty("file.encoding"));
        String original = new String("A" + "\u4f60" + "\u4eec"
                + "\u597d" + "C");

        System.out.println("original = " + original);
        System.out.println();

        try {
            byte[] utf8Bytes = original.getBytes("UTF8");
            byte[] defaultBytes = original.getBytes();

            String roundTrip = new String(utf8Bytes, "UTF8");
            System.out.println("roundTrip = " + roundTrip);

            System.out.println();
            printBytes(utf8Bytes, "utf8Bytes");
            System.out.println();
            printBytes(defaultBytes, "defaultBytes");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    } // main

}
```

输出
```
UTF-8
original = A你们好C

roundTrip = A你们好C

utf8Bytes[0] = 0x41
utf8Bytes[1] = 0xe4
utf8Bytes[2] = 0xbd
utf8Bytes[3] = 0xa0
utf8Bytes[4] = 0xe4
utf8Bytes[5] = 0xbb
utf8Bytes[6] = 0xac
utf8Bytes[7] = 0xe5
utf8Bytes[8] = 0xa5
utf8Bytes[9] = 0xbd
utf8Bytes[10] = 0x43

defaultBytes[0] = 0x41
defaultBytes[1] = 0xe4
defaultBytes[2] = 0xbd
defaultBytes[3] = 0xa0
defaultBytes[4] = 0xe4
defaultBytes[5] = 0xbb
defaultBytes[6] = 0xac
defaultBytes[7] = 0xe5
defaultBytes[8] = 0xa5
defaultBytes[9] = 0xbd
defaultBytes[10] = 0x43
```

## StringBuilder 和 StringBuffer
既然String是一个创建了就不能修改的对象，那有些场景下需要动态拼接出来一个字符串变得就不是那么高效了，比如
```Java
String num = "";
for (int i=0; i<10; i++) {
    num = num + Integer.valueOf(i).toString();
}
System.out.println(num);
```
这段代码企图将0到9十个数字拼接成"0123456789"，然而在整个过程中每次执行 `num = num + ...`时都会创建一个新的String对象，而最终这些中间对象并不会被使用。当这个重复次数过多时，频繁创建这些小对象就变得耗时耗内存了。

StringBuilder便很好的解决了这样的困境。StringBuilder正如其名，是用来build string的，上面拼接的代码可以写成
```Java
StringBuilder sb = new StringBuilder();
for (int i=0; i<10; i++) {
    sb.append(i);
}
System.out.println(sb.toString());
```

这段代码有什么不一样呢？首先一个StringBuilder内部存储字符串的是一个普通数组，如果不指定大小默认长度为16，调用append方法会将元素直接填入StringBuilder的数组的对应位置，不会构建String对象。最终调用toString()方法的时候才将最终的字符串数组生成一个String对象。

StringBuilder只能用于单线程的场景中，如果在多线程场景中使用，多个线程同时去append会有可能丢失或者覆盖一些元素，这就导致错误结果了。

StringBuffer是一个线程安全的可变字符串类，跟StringBuilder不同的是它保证了多线程的安全性，可以由多个线程同时使用。但也因为如此，它的效率比StringBuilder要低（因为需要做加锁和解锁的操作）。所以如果不涉及多线程场景，推荐使用StringBuilder。


引用前人的总结
[Java常量池理解与总结](http://www.jianshu.com/p/c7f47de2ee80)
[到底创建了几个String对象](http://t240178168.iteye.com/blog/1637649)
