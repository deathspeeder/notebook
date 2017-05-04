Java提供了将基本类型自动转化为其对应类的对象以及反过来将基本类型对应类对象转换为基本类型的功能，前者叫装箱（boxing）后者叫拆箱（unboxing）。

基本类型和对应的装箱类型如下表

基本类型 | 装箱类型
--------|---------
boolean	| Boolean
byte | Byte
char | Character
float | Float
int | Integer
long | Long
short | Short
double | Double

## 自动装箱
自动装箱会发生在以下几种情况：

### 1. 参数传递
作为参数传递时，一个方法需要一个类，实际却传递了一个基本类型。

```Java
class AutoboxingExample1
{
    public static void myMethod(Integer num){
        System.out.println(num);
    }
    public static void main(String[] args) {
        // 自动装箱，2转化为Integer(2)
        myMethod(2);
    }
}
```

### 2. 赋值
将一个基本类型的值赋给一个类。
```Java
Integer inum = 3; //Assigning int to Integer: Autoboxing
Long lnum = 32L; //Assigning long to Long: Autoboxing
```

## 自动拆箱
跟自动装箱对应也有如下情况：

### 1. 参数传递
作为参数传递时，一个方法需要一个基本类型，实际却传递了一个类。
```Java
class UnboxingExample1
{
    public static void myMethod(int num){
        System.out.println(num);
    }
    public static void main(String[] args) {
        Integer inum = new Integer(100);
        // 自动拆箱，Integer(100)转化为100
        myMethod(inum);
    }
}
```

### 2. 赋值
将一个类的值赋给一个基本类型。
```Java
Integer inum = new Integer(5);
int num = inum; //unboxing object to primitive conversion
```
## 原理
自动装箱和拆箱都是编译器做的工作，在编译阶段就已经做好了。

对于自动装箱，代码`Integer number = 100;`会被编译器翻译成`Integer number = Integer.valueOf(100);`；而对于拆箱
```Java
Integer num2 = new Integer(50);
int inum = num2;
```
会被翻译成
```Java
Integer num2 = new Integer(50);
int inum = num2.intValue();
```

## 注意
不要在比较值的时候混合使用基本类型和对应的装箱类。对于基本类型使用`== < >`进行比较，而对于类使用`equals compareTo`方法进行比较。
