对于静态变量、静态初始化块、变量、初始化块、构造器，它们的初始化顺序依次是（静态变量、静态初始化块）>（变量、初始化块）> 构造器。

可以通过以下代码验证

```Java
public class InitializationOrder {

    // 静态变量
    public static String staticField = "静态变量";
    // 变量
    public String field = "变量";

    // 静态初始化块
    static {
        System.out.println(staticField);
        System.out.println("静态初始化块");
    }

    // 初始化块
    {
        System.out.println(field);
        System.out.println("初始化块");
    }

    // 构造器
    public InitialOrderTest() {
        System.out.println("构造器");
    }

    public static void main(String[] args) {
        new InitializationOrder();
    }
}
```

执行结果为
```
静态变量
静态初始化块
变量
初始化块
构造器
```

如果类在继承的情况下则按照 父类（静态变量、静态初始化块）> 子类（静态变量、静态初始化块）> 父类（变量、初始化块）> 父类构造器 > 子类（变量、初始化块）> 子类构造器。
可以通过以下代码验证

```Java
class InitializationOrderParent {   
    // 静态变量   
    public static String pStaticField = "父类--静态变量";   
    // 变量   
    public String pField = "父类--变量";   

    // 静态初始化块   
    static {   
        System.out.println(pStaticField);   
        System.out.println("父类--静态初始化块");   
    }   

    // 初始化块   
    {   
        System.out.println(pField);   
        System.out.println("父类--初始化块");   
    }   

    // 构造器   
    public InitializationOrderParent() {   
        System.out.println("父类--构造器");   
    }   
}   

public class InitializationOrderChild extends InitializationOrderParent {   
    // 静态变量   
    public static String sStaticField = "子类--静态变量";   
    // 变量   
    public String sField = "子类--变量";   
    // 静态初始化块   
    static {   
        System.out.println(sStaticField);   
        System.out.println("子类--静态初始化块");   
    }   
    // 初始化块   
    {   
        System.out.println(sField);   
        System.out.println("子类--初始化块");   
    }   

    // 构造器   
    public InitializationOrderChild() {   
        System.out.println("子类--构造器");   
    }   

    // 程序入口   
    public static void main(String[] args) {   
        new InitializationOrderChild();   
    }   
}  
```

执行结果为

```
父类--静态变量
父类--静态初始化块
子类--静态变量
子类--静态初始化块
父类--变量
父类--初始化块
父类--构造器
子类--变量
子类--初始化块
子类--构造器
```

静态变量和静态初始化块之间、变量和初始化块之间的先后顺序取决于它们在类中出现的先后顺序。
http://www.tuicool.com/articles/zeamyeQ 
