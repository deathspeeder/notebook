Java所有类的父类Object类拥有如下重要的方法：

方法 |	描述
-----|------
protected Object clone() |	克隆对象
protected void finalize() | 垃圾回收在销毁对象前调用
boolean equals(Object obj) | 用于判断两个对象相等
int hashCode() | 返回对象的hash值
String toString() | 返回一个字符串表示对象
void notify() | 唤醒等待在对象上的线程
void notifyAll() | 唤醒所有等待在对象上的线程
void wait() | 让线程阻塞知道notify被调用

## equals 方法 和 ==
在比较对象时，操作符 `==` 比较的是两个对象的引用地址，只有当两个引用指向同一个对象时才相等。而 `equals` 方法允许用户自定义对象的比较方法。比如
```Java
final String str1 = new String( "bbb" );
System.out.println( "Using == operator: " + ( str1 == "bbb" ) );
System.out.println( "Using equals() method: " + str1.equals( "bbb" ) );
```
的运行结果为
```
Using == operator: false
Using equals() method: true
```

从需求的角度来看，比较两个字符串当然是当二者中所有字符一一对应即为相等，所以应该用 `equals` 方法来判断其相等性。我们可以看看`String`类的`equals`方法的实现方式：
```Java
public boolean equals(Object anObject) {
    if (this == anObject) {
        return true;
    }
    if (anObject instanceof String) {
        String anotherString = (String)anObject;
        int n = value.length;
        if (n == anotherString.value.length) {
            char v1[] = value;
            char v2[] = anotherString.value;
            int i = 0;
            while (n-- != 0) {
                if (v1[i] != v2[i])
                    return false;
                i++;
            }
            return true;
        }
    }
    return false;
}
```
这段代码简单明了：首先比较了二者的引用是不是一个，其次判断是不是`String`对象，如果是再次逐一比较各个字符。这种`equals`的实现方式是一种常见的工程实用方式。

定义一个`equals`方法按照最佳实践应该满足如下标准：
1. 自反性，对象`x`必须和自己相等，即`x.equals(x)`返回`true`
2. 对称性，如果对象`x`等于`y`，那么`y`也等于`x`，即`x.equals(y)` 和 `y.equals(x)`都返回`true`
3. 传递性，如果对象`x`等于`y`，`y`等于`z`，那么`x`也等于`z`，即`x.equals(y)`和`y.equals(z)`都返回`true`，那么`x.equals(z)`返回`true`
4. 一致性，多次调用`equals`方法返回的结果必须一致，除非用于比较的属性发生了变化
5. 不等于`null`，`equals(null)`返回`false`

很容易验证`String`的`equals`满足以上五点，所以是一个好的定义。

`Object`类的`equals`方法跟`==`等价，言下之意即，如果一个类没有定义`equals`方法，那么用`equals`方法判断相等性就等价于用`==`，即比较二者的引用地址。`Object.equals`定义如下：
```Java
public boolean equals(Object obj) { return (this == obj); }
```

## equals 实现
`equals`方法的实现需要根据对象表示的数据结构来确定，一般地，会将对象的所有成员变量作为计算依据，也会排除一些无关紧要的成员变量。比如在上文提到的`String`类中，`equals`方法就是计算每个字符的相等性。假设有一个类叫`Person`，它有三个成员叫做`firstName, lastName, email`，那么`equals`方法最好就是比较这三个成员变量是否一一相等，比如
```Java
public class Person {
    private final String firstName;
    private final String lastName;
    private final String email;

    public Person( final String firstName, final String lastName, final String email ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // Step 0: Please add the @Override annotation, it will ensure that your
    // intention is to change the default implementation.
    @Override
    public boolean equals( Object obj ) {
        // Step 1: Check if the 'obj' is null
        if ( obj == null ) {
            return false;
        }

        // Step 2: Check if the 'obj' is pointing to the this instance
        if ( this == obj ) {
            return true;
        }

        // Step 3: Check classes equality. Note of caution here: please do not use the
        // 'instanceof' operator unless class is declared as final. It may cause
        // an issues within class hierarchies.
        if ( getClass() != obj.getClass() ) {
            return false;
        }

        // Step 4: Check individual fields equality
        final Person other = (Person) obj;
        if ( email == null ) {
            if ( other.email != null ) {
                return false;
            }
        } else if( !email.equals( other.email ) ) {
            return false;
        }

        if ( firstName == null ) {
            if ( other.firstName != null ) {
                return false;
            }
        } else if ( !firstName.equals( other.firstName ) ) {
            return false;
        }

        if ( lastName == null ) {
            if ( other.lastName != null ) {
                return false;
            }
        } else if ( !lastName.equals( other.lastName ) ) {
            return false;
        }

        return true;
    }
}
```


## hashCode 方法
`hashCode`方法的作用是生成一个hash值，在用于键值（Key-Value）对储存的时候获得更快的随机存取速度。最常见的应用就是数据结构`HashMap`和`HashSet`，hash使得元素的随机访问时间复杂度是O(1)，不用hash而去访问一个元素则是O(n)，因为需要遍历一遍集合。

在Java中，如果一个对象覆盖（override）了`equals`方法，那么最好也覆盖`hashCode`方法，并且如果两个对象`equals`返回`true`，那么他们的`hashCode`返回也应该一样，因为在hash相关的数据结构中二者会被同时用到，接下来会详细描述。

[哈希表](https://zh.wikipedia.org/wiki/%E5%93%88%E5%B8%8C%E8%A1%A8)是根据键（Key）而直接访问在内存存储位置的数据结构。

举例说明哈希表的原理，假设有一些字符串需要组织成一个集合：`"Shanghai", "Beijing", "Chongqing", "Guangzhou", "Hangzhou", "Chengdu", "Zhengzhou"`。最简单的方式是直接放在一个数组，
```Java
String[] array = new String[] {"Shanghai", "Beijing", "Chongqing", "Guangzhou", "Hangzhou", "Chengdu", "Zhengzhou"};
```
这时假如需要读取某一个元素，必须遍历整个数组才能确定某个元素是否包含在数组中，即时间复杂度 O(n)，当需要存储的元素变得很多，查询变得很频繁时，这样的时间复杂度将是一个很严重的性能瓶颈。有什么办法加快这种随机访问呢，那就是哈希表。

在哈希表中，每个元素通过一个哈希方法得到一个哈希值，哈希值一般是一个int类型。哈希表定义了一个初始容量，假定为`n`，哈希表的初始存储是一个长度为`n`的数组，将每个元素的哈希值对`n`取余，就能得到一个取值范围为`[0, n)`的余数（如果取余运算得到一个负数，可以将该负数加上`n`后再次取余得到对应的正数）。哈希表将该余数作为元素的存储位置标号，把元素储存到对应标号的数组位置。比如，对于以上讨论的字符串，取`n=8`，求出其对应的标号：
```Java
String[] array = new String[] {"Shanghai", "Beijing", "Chongqing", "Guangzhou", "Hangzhou", "Chengdu", "Zhengzhou"};
int mod = 8;
for (String s : array) {
    System.out.println((s.hashCode() % mod + mod) % mod);
}
```
输出结果为
```
3
6
4
0
6
2
4
```

以此为标号存储这些元素的效果看起来就是

位置 | 存储
-----|-----
0 | "Guangzhou"
1 |
2 | "Chengdu"
3 | "Shanghai"
4 | "Chongqing" "Zhengzhou"
5 |
6 | "Beijing" "Hangzhou"
7 |

从存储的结果可以看出两个事实：
1. 有的位置可能没有存储任何元素（比如位置1,5,7）
2. 有的位置存储了不止一个元素（比如位置4,6，这种情况叫做位置冲突）

哈希表这样的存储结构将加快元素访问速度，最好的情况是所有元素分配在不通位置上，当访问一个元素时，只要通过其哈希值取余就能得到其位置然后直接访问，复杂度为 O(1)；最差的情况是所有元素被哈希到了同一个位置，通过哈希取余得到位置后哈希表还要逐一比较该位置上所有元素，这跟直接用数组存储没什么区别了，复杂度为 O(n) 。

以上哈希表的例子可以看出，即便是7个元素存储在容量为8的哈希表中也发生了两处位置冲突。位置冲突越多，访问元素的速度就越慢，哈希表效率就越差。为了获得较高的命中率，哈希表应该避免存储过多的元素，这在哈希表的原理中叫做负载因子。初始化一个哈希表需要两个参数，一是容量，二是负载因子。负载因子是一个0到1的小数，表示当元素的数量达到容量的多少比例计算超负载了。负载因子一般取0.75，因为超过这个值后，哈希表性能将大幅下降，详见[HashTable Wiki](https://en.wikipedia.org/wiki/Hash_table)。当哈希表的元素量比例达到负载因子时，哈希表将进行一次扩容，即加大容量然后重新将所有元素计算哈希值并归位。

从哈希表的原理可知要存储一个对象，需要两个方法，一是哈希方法，而是位置冲突了的比较方法。在Java中`hashCode`方法便是用于生成一个对象的哈希值的，而`equals`方法则是比较对象的。到此可以知道为什么在覆盖了`equals`方法最好也覆盖`hashCode`方法了，这是因为`hashCode`方法使得用哈希表存储对象的时候大幅提升访问速度。在Java中运用了哈希表的数据结构主要有`HashMap`，`HashSet`。

从哈希表的原理还可以得出，两个不同对象的`hashCode`结果可能会相同，但是二者`equals`返回一定是`false`。

## hashCode 实现
跟`equals`方法类似，`hashCode`也会将重要的成员变量拿来计算哈希值，首先可以看看`String`类的`hashCode`如何实现的
```Java
public int hashCode() {
    int h = hash;
    if (h == 0 && value.length > 0) {
        char val[] = value;

        for (int i = 0; i < value.length; i++) {
            h = 31 * h + val[i];
        }
        hash = h;
    }
    return h;
}
```

`String`的字符存储在`value`成员变量中，成员变量`hash`是一个哈希值的缓存（如果计算过hash的值了，那么直接使用，否则计算哈希值存储到hash；可以缓存哈希值是因为String是一个final的类，其内部的字符是不会改变的，所以其哈希值也不会改变，只需要计算一次）。计算哈希值的方式是将哈希值乘以31，然后逐一加入字符的值。这是一种非常常见而且实用高效的方式。原因是
1. 31是一个素数，逐一相乘加入各个成员变量，最终得到的哈希值出现重合的可能性低；
2. 与31相乘可以被编译器优化，因为`31 * h`等价于`(h << 5) - h`，而后者的运算速度比前者的乘法要快很多。

Java提供了一些便捷方式来实现`equals`和`hashCode`，比如可以将`Person`类的这两个方法写成
```Java
@Override
public boolean equals( Object obj ) {
    if ( obj == null ) {
        return false;
    }

    if ( this == obj ) {
        return true;
    }

    if ( getClass() != obj.getClass() ) {
        return false;
    }

    final PersonObjects other = (PersonObjects) obj;
    if( !Objects.equals( email, other.email ) ) {
        return false;
    } else if( !Objects.equals( firstName, other.firstName ) ) {
        return false;            
    } else if( !Objects.equals( lastName, other.lastName ) ) {
        return false;            
    }

    return true;
}

@Override
public int hashCode() {
    return Objects.hash( email, firstName, lastName );
}
```

可以查看Objects的源码发现其hash方法的实现：
```Java
// Objects.java
public static int hash(Object... values) {
    return Arrays.hashCode(values);
}

// Arrays.java
public static int hashCode(Object a[]) {
    if (a == null)
        return 0;

    int result = 1;

    for (Object element : a)
        result = 31 * result + (element == null ? 0 : element.hashCode());

    return result;
}
```

可以看到`Objects.hash`的实现方式正是上文讨论的逐一乘以31的方式。

## 应用

`equals`方法主要在比较两个对象时用到，`hashCode`主要在哈希表相关数据结构中用到，详见`HashSet`，`HashMap`。
