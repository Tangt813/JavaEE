- Work once，work anywhere

## Characteristics

- Simple
- object oriented
- distributed
- multithreaded
- dynamic
- architecture neutral  **结构中立的**
- portable
- high performance
- robust 强健的
- secure   

方法调用完之后就没了，而变量，类还存在内存里

在继承中，构造函数不会继承，他会在构造子类的时候先请求父类的构造函数

> Declare a field in the subclass with the same name as the one in the 
> superclass, thus hiding it (not recommended)
>
> =在子类里声明的变量如果和父类一样，就会隐藏父类的变量

编译时，编译器只考虑父类的method，如果子类和父类的method返回值不一样，但main里的调用与父类的method返回值一样，那编译器不会在main里报错

```java
//main
Cat cat=new Kitty("kitty");
int a=cat.Speak();
```

```java
//Kitty
public float Speak()
{
    System.out.println("i am Kitty");
    return 0.5f;
}
```

```java
//Cat
public int Speak()
{
    System.out.println("i am Cat");
    return 0;
}
```

Java接口:

接口cat继承接口animal，animal里的方法会到cat里，如果animal里有default的method,接口cat可以：

​	1.不写此方法，用animal里的方法。

​	2.重写此方法，之后用cat里的此方法。

​	3.声明此方法，此方法变为抽象method，需要后续自己重写。

​	代码在main4包。（单例模式也在，private构造函数）

