成员变量的默认值如下表：

类型	| 默认值
------| -------------
boolean	|False
byte	|0
short	|0
int	|0
long	|0L
char	|\u0000
float	|0.0f
double	|0.0d
object reference	|null

可以编写一个测试的类来验证。源代码 [InitializationWithDefaults.java](InitializationWithDefaults.java)

```Java
package com.javacodegeeks.advanced.construction;

public class InitializationWithDefaults {
    private boolean booleanMember;
    private byte byteMember;
    private short shortMember;
    private int intMember;
    private long longMember;
    private char charMember;
    private float floatMember;
    private double doubleMember;
    private Object referenceMember;

    public InitializationWithDefaults() {
        System.out.println( "booleanMember = " + booleanMember );
        System.out.println( "byteMember = " + byteMember );
        System.out.println( "shortMember = " + shortMember );
        System.out.println( "intMember = " + intMember );
        System.out.println( "longMember = " + longMember );
        System.out.println( "charMember = " +
            Character.codePointAt( new char[] { charMember }, 0  ) );
        System.out.println( "floatMember = " + floatMember );
        System.out.println( "doubleMember = " + doubleMember );
        System.out.println( "referenceMember = " + referenceMember );
    }

    public static main(String[] args) {
      final InitializationWithDefaults initializationWithDefaults = new InitializationWithDefaults();
    }
}
```

执行结果为

```
booleanMember = false
byteMember = 0
shortMember = 0
intMember = 0
longMember = 0
charMember = 0
floatMember = 0.0
doubleMember = 0.0
referenceMember = null
```
