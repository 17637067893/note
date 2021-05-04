![image-20201208204129269](G:\note\image\image-20201208204129269.png)

#### java学习路线

https://www.bilibili.com/read/cv5216534?spm_id_from=333.788.b_636f6d6d656e74.5

## JDBC



## sharding-JDBC分库分表



![image-20201214103504850](G:\note\image\image-20201214103504850.png)

分库分表就是为了解决由于数据量过大而导致数据库性能降低的问题，将原来独立的数据库拆分成若干数据库组成
，将数据大表拆分成若干数据表组成，使得单一数据库、单一数据表的数据量变小，从而达到提升数据库性能的目
的

#### 分库分表的方式

分库分表包括分库和分表两个部分，在生产中通常包括：垂直分库、水平分库、垂直分表、水平分表四种方式

#### 第一 垂直分表

按字段将表分为不同的表

![image-20201214104432447](G:\note\image\image-20201214104432447.png)

用户在浏览商品列表时，只有对某商品感兴趣时才会查看该商品的详细描述。因此，商品信息中商品描述字段访问
频次较低，且该字段存储占用空间较大，访问单个数据IO时间较长；商品信息中商品名称、商品图片、商品价格等
其他字段数据访问频次较高。
由于这两种数据的特性不一样，因此他考虑将商品信息表拆分如下：
将访问频次低的商品描述信息单独存放在一张表中，访问频次较高的商品基本信息单独放在一张表中。

![image-20201214104546350](G:\note\image\image-20201214104546350.png)

```
垂直分表定义：将一个表按照字段分成多表，每个表存储其中一部分字段
```

它带来的提升是：

它带来的提升是：
1.为了避免IO争抢并减少锁表的几率，查看详情的用户与商品信息浏览互不影响
2.充分发挥热门数据的操作效率，商品信息的操作的高效率不会被商品描述的低效率所拖累。

通常我们按以下原则进行垂直拆分:
1. 把不常用的字段单独放在一张表;
2. 把text，blob等大字段拆分出来放在附表中;
3. 经常组合查询的列放在一张表中

#### 第二 垂直分库

按业务将数据库拆分为不同的数据库

通过垂直分表性能得到了一定程度的提升，但是还没有达到要求，并且磁盘空间也快不够了，因为数据还是始终限
制在一台服务器，库内垂直分表只解决了单一表数据量过大的问题，但没有将表分布到不同的服务器上，因此每个
表还是竞争同一个物理机的CPU、内存、网络IO、磁盘。
经过思考，他把原有的SELLER_DB(卖家库)，分为了PRODUCT_DB(商品库)和STORE_DB(店铺库)，并把这两个库分
散到不同服务器，如下图：![image-20201214110019443](G:\note\image\image-20201214110019443.png)

由于商品信息与商品描述业务耦合度较高，因此一起被存放在PRODUCT_DB(商品库)；而店铺信息相对独立，因此
单独被存放在STORE_DB(店铺库)。
小明进行的这一步优化，就叫垂直分库。

垂直分库是指按照业务将表进行分类，分布到不同的数据库上面，每个库可以放在不同的服务器上，它的核心理念
是专库专用。

它带来的提升是：
解决业务层面的耦合，业务清晰
能对不同业务的数据进行分级管理、维护、监控、扩展等
高并发场景下，垂直分库一定程度的提升 IO、数据库连接数、降低单机硬件资源的瓶颈
垂直分库通过将表按业务分类，然后分布在不同数据库，并且可以将这些数据库部署在不同服务器上，从而达到多
个服务器共同分摊压力的效果，但是依然没有解决单表数据量过大的问题。

#### 第三 水平分库

垂直分库按业务划分，已经将业务不同划分为不同的库，但是随着数据量的增多，单个库还是遇到性能问题

经过垂直分库后，数据库性能问题得到一定程度的解决，但是随着业务量的增长，PRODUCT_DB(商品库)单库存储
数据已经超出预估。粗略估计，目前有8w店铺，每个店铺平均150个不同规格的商品，再算上增长，那商品数量得
往1500w+上预估，并且PRODUCT_DB(商品库)属于访问非常频繁的资源，单台服务器已经无法支撑。此时该如何
优化？

![image-20201214111321989](G:\note\image\image-20201214111321989.png)

也就是说，要操作某条数据，先分析这条数据所属的店铺ID。如果店铺ID为双数，将此操作映射至
RRODUCT_DB1(商品库1)；如果店铺ID为单数，将操作映射至RRODUCT_DB2(商品库2)。此操作要访问数据库名
称的表达式为RRODUCT_DB[店铺ID%2 + 1] 。
小明进行的这一步优化，就叫水平分库

#### 第四 水平分表

与水平分表一样都是解决单个表的数据量过多

![image-20201214112154253](G:\note\image\image-20201214112154253.png)

与水平分库的思路类似，不过这次操作的目标是表，商品信息及商品描述被分成了两套表。如果商品ID为双数，将
此操作映射至商品信息1表；如果商品ID为单数，将操作映射至商品信息2表。此操作要访问表名称的表达式为商品
信息[商品ID%2 + 1] 。
小明进行的这一步优化，就叫水平分表。

#### 分库分表的问题

第一 事务一致性问题

 由于分库分表把数据分布在不同库甚至 不同服务器，不可避免带来分布式事务问题。

第二 跨节点关联

垂直分库后[商品信息]和[店铺信息]不在一个数据库，甚至不在一台服务器，无法进行关联查询。

可将原关联查询分为两次查询，第一次查询的结果集中找出关联数据id，然后根据id发起第二次请求得到关联数
据，最后将获得到的数据进行拼装。

第三  .跨节点分页、排序函数

跨节点多库进行查询时，limit分页、order by排序等问题，就变得比较复杂了。需要先在不同的分片节点中将数据
进行排序并返回，然后将不同分片返回的结果集进行汇总和再次排序。

![image-20201214113833357](G:\note\image\image-20201214113833357.png)





在使用Max、Min、Sum、Count之类的函数进行计算的时候，与排序分页同理，也需要先在每个分片上执行相应
的函数，然后将各个分片的结果集进行汇总和再次计算，最终将结果返回。

第四 主键避重

在分库分表环境中，由于表中数据同时存在不同数据库中，主键值平时使用的自增长将无用武之地，某个分区数据
库生成的ID无法保证全局唯一。因此需要单独设计全局主键，以避免跨库主键重复问题

![image-20201214114017790](G:\note\image\image-20201214114017790.png)

#### Sharding-JDBC介绍

Sharding-JDBC的核心功能为数据分片 和 读写分离 ，通过 Sharding-JDBC，应用可以透明 的使用 jdbc访问已经分库
分表、读写分离的多个数据源，而不用关心数据源的数量以及数据如何分布。

## Javase

Java跨平台原理

![image-20201208105145946](G:\note\image\image-20201208105145946.png)

1 JRE

```
JRE Java Runtime Enviroment 运行环境
```
2 JDK![image-20201208105412786](G:\note\image\image-20201208105412786.png)

```
Java程序开发工具包，包括JRE和开发人员使用的工具
编译工具Javac.exe 运行环境 java.exe
```
3 运行程序

```
1 创建hello World.java文件
public class HelloWorld{
	public static void main(String[] args){
		System.out.println("456");
	}
}

2 编译 javac hello world.java
3 运行 java hello world //只跟类名
```
#### 常量![image-20201208113843534](G:\note\image\image-20201208113843534.png)

#### 数据类型

![image-20201208114710478](G:\note\image\image-20201208114710478.png)

#### 定义变量

```
// 声明类型 和 赋值
int a = 10

定义数据类型
long m = 100000; //数据过大 整数默认为整型

long m = 100000000L // 10000000L 表示L  long类型数据
```
#### 标识符定义规则

```
1 由字母，数字，下划线(_)和$组成
2 不能以数字开头
3 不能是关键字
4 区分大小写
5 驼峰命名法
```
#### 类型转换

![image-20201208120027383](G:\note\image\image-20201208120027383.png)



![image-20201208120420535](G:\note\image\image-20201208120420535.png)

#### 运算符

![image-20201208120559933](G:\note\image\image-20201208120559933.png)

![image-20201208123944515](G:\note\image\image-20201208123944515.png)

#### Scanner

```java
import java.util.Scanner;

public class ScannerDemo{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("输入第一个");
        int x = sc.nextInt();
        System.out.print("输入第二个");
        int  y = sc.nextInt();
        System.out.print("输入第三个");
        int z = sc.nextInt();
        int temp = x > y ? x : y;
        int res = temp > z ? temp : z;
        System.out.println("最大数字" + res);
    }
}
```



#### for循环

```
for (int i = 1; i <= 5; i++) {   
System.out.println(i);
    
} 

```
#### Random
![image-20201208131321478](G:\note\image\image-20201208131321478.png)

#### IDEA使用
![image-20201208153411987](G:\note\image\image-20201208153411987.png)

![image-20201208153438127](G:\note\image\image-20201208153438127.png)

<img src="G:\note\image\image-20201208153457339.png" alt="image-20201208153457339" style="zoom:50%;" />

<img src="G:\note\image\image-20201208153517632.png" alt="image-20201208153517632" style="zoom:50%;" />

<img src="G:\note\image\image-20201208153532624.png" alt="image-20201208153532624" style="zoom:50%;" />

<img src="G:\note\image\image-20201208153556127.png" alt="image-20201208153556127" style="zoom:50%;" />

<img src="G:\note\image\image-20201208153621366.png" alt="image-20201208153621366" style="zoom:50%;" />

<img src="G:\note\image\image-20201208153635230.png" alt="image-20201208153635230" style="zoom:50%;" />

<img src="G:\note\image\image-20201208153646237.png" alt="image-20201208153646237" style="zoom:50%;" />

![image-20201208153703146](G:\note\image\image-20201208153703146.png)

<img src="G:\note\image\image-20201208153727992.png" alt="image-20201208153727992" style="zoom:50%;" /><img src="G:\note\image\image-20201208153737955.png" alt="image-20201208153737955" /><img src="G:\note\image\image-20201208153737955.png" alt="image-20201208153737955" style="zoom:50%;" />

<img src="G:\note\image\image-20201208153807806.png" alt="image-20201208153807806" style="zoom:50%;" />

<img src="G:\note\image\image-20201208154407353.png" alt="image-20201208154407353" style="zoom:50%;" />

#### 模块操作

<img src="G:\note\image\image-20201208155426530.png" alt="image-20201208155426530" style="zoom:50%;" />

<img src="G:\note\image\image-20201208155444906.png" alt="image-20201208155444906" style="zoom: 50%;" />![image-20201208155506888](G:\note\image\image-20201208155506888.png)<img src="G:\note\image\image-20201208155444906.png" alt="image-20201208155444906" style="zoom: 33%;" />![image-20201208155506888](G:\note\image\image-20201208155506888.png)<img src="G:\note\image\image-20201208155444906.png" alt="image-20201208155444906" style="zoom: 50%;" />![image-20201208155506888](G:\note\image\image-20201208155506888.png)<img src="G:\note\image\image-20201208155444906.png" alt="image-20201208155444906" style="zoom: 33%;" />![image-20201208155506888](G:\note\image\image-20201208155506888.png)

![image-20201208155548760](G:\note\image\image-20201208155548760.png)

![image-20201208155603298](G:\note\image\image-20201208155603298.png)



2快捷键![image-20201208155618310](G:\note\image\image-20201208155618310.png)

![image-20201208155705421](G:\note\image\image-20201208155705421.png)

![image-20201208155723806](G:\note\image\image-20201208155723806.png)

![image-20201208155736473](G:\note\image\image-20201208155736473.png)

![image-20201208155751822](G:\note\image\image-20201208155751822.png)

![image-20201208155806833](G:\note\image\image-20201208155806833.png)

![image-20201208155820565](G:\note\image\image-20201208155820565.png)

![image-20201208155831785](G:\note\image\image-20201208155831785.png)

![image-20201208155845139](G:\note\image\image-20201208155845139.png)

![image-20201208155904647](G:\note\image\image-20201208155904647.png)

```
快速生成语句
	快速生成main()方法：psvm，回车
	快速生成输出语句：sout，回车


内容辅助键
	Ctrl+Alt+space(内容提示，代码补全等
```
#### 数组

![image-20201208161412287](G:\note\image\image-20201208161412287.png)

```
一个存储同类型的数据

1 定义
 int[] arr
 int arr[]
 
 动态初始话  //由系统默认给值
 int[] arr = new int[3]
 访问数据元素
 System.out.println(arr[0])
 
 
 静态初始化(明确每项的值系统决定长度)
 int[] arr = new int[]{1,2,3}
 
 简化形式
 int[] arr = {1,2,3,4}
 
 便利数组
 for(int i=0;i<arr.length;i++){
     System.out.println(arr[i])
 }
```
#### 方法

```
public class ArrDemo {
    public static void main(String[] args) {
        int num = fn(6,7,8);
        System.out.println("参数" + num + "个");
    }
    protected static int fn(int... arr){
        System.out.println(arr[0]);
        System.out.println(arr[1]);
        return  arr.length;
    }
}
```

方法重载 

![image-20201208165316643](G:\note\image\image-20201208165316643.png)

![image-20201208165443938](G:\note\image\image-20201208165443938.png)

![image-20201208171116367](G:\note\image\image-20201208171116367.png)

#### Debug

![image-20201208180903006](G:\note\image\image-20201208180903006.png)

#### 类和对象

```java
类 是对现实生活中一类具有共同属性和行为的事物的抽象，确定对象将会拥有的属性和行为
对象时根据类new 来的

创建类

public class Student {
    public String name="小明";
    public int age=20;
    public void study(){
        System.out.println("学习");
    }
}
实现对象
public class StudentDemo {
    public static void main(String[] args) {
        Student s = new Student();
        System.out.println(s.name);
        System.out.println(s.age);
        s.study();
    }
}


封装

针对private修饰的成员变量，如果需要被其他类使用，提供相应的操作
提供“get变量名()”方法，用于获取成员变量的值，方法用public修饰
提供“set变量名(参数)”方法，用于设置成员变量的值，方法用public修饰
```
![image-20201208184954426](G:\note\image\image-20201208184954426.png)

#### 成员变量 局部变量

![image-20201208184736368](G:\note\image\image-20201208184736368.png)

![image-20201208184622937](G:\note\image\image-20201208184622937.png)

#### this 关键字

![image-20201208185757860](G:\note\image\image-20201208185757860.png)

#### 构造方法

方法名 = 类名

![image-20201208192842268](G:\note\image\image-20201208192842268.png)

#### API 

```
API 应用程序编程接口

Java API JDK中提供的功能类

```
#### 字符串 String
```
 所有的双引号内的
 特点
 1 不可变，创建后不能被更改
 2 可以被共享
 
 构造方法
 
 // public String()创建空白字符串
  String s1 = new String();
  
  // public String(char[] chs) 根据字符数组内容，创建
  char[] chs = {'a','b','c'};
  String s2 = new String(chs); // abc
  
  // public String(byte[] bys)根据字节数组的内容创建
  byte[] bys = {97,98,99};
  String s3 = new String(bys) // abc
  
  
  // String s = "abc" 直接赋值创建
  String s = "abc";
  
  String 对象的特点
  通过new 关键字创建的字符串对象，每次new 都会开辟新的内存空间，有时虽然相同但是地址不同
  String s = "abc";只要字符相同 就是一个String对象
  
```
#### 字符串比较 

```java
 #### 字符串比较 ==
  基本类型  比较数值
  引用类型  比较地址是否相同
  字符串对象 如果比较内容是否相同使用 equals
   s1.equals(s2);  //s1 s2 都是对象类型String 比较内容是否相同
   
   遍历字符串
    String s = "12345";
    for(int i=0;i<s.length();i++){
        String ss = s.charAt(i);
        Systeam.out.prinln(ss);
    }
```



   #### StringBuilder

```
由于String的不可变性  StringBuilder 可变
创建StringBuilder 括号内可以传字符串初始值
StringBuilder str1 = new StringBuilder();
StringBuilder Str2 = new StringBuilder("123")

添加字符串 
str1.append("789");

翻转字符串  str4.reverse();

String转StringBuilder
StringBuilder str3 = new StringBuilder(str3)

StringBuilder 转 String
String str4 = Str3.toString();
```

#### ArrayList

   ```
  数组的长度是固定的 存储空间可变的存储模型 
  
创建集合
ArrayList<String> stuArr = new ArrayList<>();

集合长度   stuArr.size();

增 
stuArr.add("第一");
StuArr.add(0,"第0");

删
stuArr.remove(0);

改
stuArr.set(0,"修改");

查 
stuArr.get(0);
   ```
   #### 继承

   继承是面向对象的三大特征之一，可以使得子类具有父类得属性和方法，也可以在子类中重新定义，追加属性和方法

父类

```java
package com.继承;

public class Zi extends Fu{
    String ZiName = "子类";
    public  void ZiMethod(){
        System.out.println(super.FuName);
        System.out.println("子类方法");
    }
}

```

子类

```java
package com.继承;

//可以通过super访问父类属性
public class Zi extends Fu{
    String ZiName = "子类";
    public  void ZiMethod(){
        System.out.println(super.FuName);
        System.out.println("子类方法");
    }
}

```

测试类

   ```
package com.继承;

public class Demo {
    public static void main(String[] args) {
        Zi z = new Zi();
        System.out.println(z.FuName);
        System.out.println(z.ZiName);
        z.FuMethd();
        z.ZiMethod();
    }
}

   ```
 方法重写

```
子类有和父类相同方法名的方法
@Override
```

#### package

```
包 就是文件夹
作用 就是对文件的分类管理
```

<img src="G:\note\image\image-20201209141551728.png" alt="image-20201209141551728" style="zoom:50%;" />

<img src="G:\note\image\image-20201209142316668.png" alt="image-20201209142316668" style="zoom:50%;" />

  #### 权限修饰

![image-20201209143435204](G:\note\image\image-20201209143435204.png)

 #### final
![image-20201209144002380](G:\note\image\image-20201209144002380.png)

 #### static

![image-20201209144839487](G:\note\image\image-20201209144839487.png)

访问特点

![image-20201209144924442](G:\note\image\image-20201209144924442.png)

#### 多态
同一对象，在不同时刻表现出来的不同形态
多态的前提和体现

1 有继承/实现关心
2 有方法重写
3 有父类引用指向子类对象
多态成员访问特点

父类

```
public class Animal {
    public  int age = 10;
    public void  eat(){
        System.out.println("动物吃");
    }
}

```

子类

```
package com.demo3;

public class Cat extends Animal{
    public int age = 20;
    public int weight = 10;
    @Override
    public void eat(){
        System.out.println("猫吃鱼");
    }
    public void playGame(){
        System.out.println("猫捉迷藏");
    }
}

```

测试类

```
package com.demo3;
//导包
import com.demo1.Teacher;

public class Demo extends Teacher{
    public static void main(String[] args) {
        //父类         子类
        Animal cat = new Cat();
        //父类值
        System.out.println(cat.age);

        //方法
        //父类中有方法名才能用
      //  cat.playGame();
      
      
        //执行的是子类中的代码内容
        cat.eat();

    }
}
```



#### 抽象类

父类

```java
package com.AbstractDemo;

public abstract class  Animal {
    //抽象类 只给定 属性名 方法名 不给具体内容 想要使用自己去实现
    public String name = "动物";
    public void sleep(){
        System.out.println("动物睡觉");
    }
    public abstract void eat();
}

```

子类

```
package com.AbstractDemo;

public class Cat extends Animal {
    //重写抽象方法
    @Override
    public void eat(){
        System.out.println("猫吃鱼");
    }
}


或者

package com.AbstractDemo;
 //子类 继承抽象父类   本身也是抽象类 可以不用写抽象方法
public abstract class Dog extends Animal {

}

```

测试类

```
package com.AbstractDemo;

public class AnimalDemo {
    public static void main(String[] args) {
    
        //类似多态
        Animal a = new Cat();
        System.out.println(a.name);
        
        
        //子类重写了eat
        a.eat();
        //虽然子类没有方法 但是 继承父类中的sleep可以使用(特有的)
        a.sleep();
    }
}
```



#### 接口
```
public interface 接口名{
    
}
// 接口方法的种类
public interface MyInterface {
    void show1();

    //默认方法升级接口不影响其他代码
     default void show2(){
        //以后接口升级可以避免影响其他到代码
    }
    //静态方法 只能被接口名调用，不能被实现类调用
    static void show3(){

    }
    //私有方法
    private static void show4(){
         //公共方法代码
    }
}
类实现接口implements表示
public class 类名 implements 接口名{
    
}
接口子类
1 要么重写接口所有抽象方法
2 要么是抽象类

接口的成员特点
成员变量 
默认修饰符 public static final  //常量
构造方法
 没有构造方法
 
 成员方法
 只能是抽象方法
 默认修饰符 public abstract
```
#### 形参和返回值
![image-20201209194821615](G:\note\image\image-20201209194821615.png)

![image-20201209194839347](G:\note\image\image-20201209194839347.png)

![image-20201209194854947](G:\note\image\image-20201209194854947.png)

#### 内部类

<img src="G:\note\image\image-20201209202939562.png" alt="image-20201209202939562" style="zoom: 80%;" />

 //成员内部类

```java
public class A{
    //成员内部类
    public class B{
        public void show(){
            Systeam.out.prinln("成员内部类");
        }
    }
    //调用
    public void show(){
        B b = new B();
        b.show();
    }
}
```

成员内部类

```java
// 局部内部类
public class A{
    public void method(){
        class B{
            public void show(){
                Systeam.out.println("局部内部类")
            }
        }
        //调用
        B b = new B();
        b.show()
    }
}
```



```java
//匿名内部类
首先有个类或者接口，这里的类可以是具体类也可以是抽象类
public interface Jumpping{
    void jump();
}

public class Out{
    public void method(){
        //重写接口/抽象类
        new Jumpping(){
            @Override
            public void jump(){
                Systeam.out.println("匿名类");
            }
        }.jump();
        //如果多次调用
        Jumpping j = new Jumpping(){
            @Override
            public void jump(){
                Systeam.out.println("匿名类");
            }
        };
        //可以多次调用
        j.jump();
    }
}

```
内部类的作用

```
接口
public interface Jumpping {
    void jumpp();
}

一个操作类
public class JumppingOperator { 
需要这接口的实现类对象 以前我们需要 创建实现类
    public void show(Jumpping j){
        j.jumpp();
    }
}

//使用JumppingOperator类
public class JumppingDemo {
    public static void main(String[] args) {
        JumppingOperator jum = new JumppingOperator();
        
        //参数是一个Jumpping的实现类对象 现在用内部类 直接new 一个
        jum.show(new Jumpping(){
            @Override
            public void jumpp(){
                System.out.println("调高");
            }
        });
    }
}
```

#### 常用API

Math

```
  Math.abs() //绝对值
  System.out.println(Math.abs(-88)); //88
  Math.ceil(22.2)  23.0 返回右边的double数
  Math.floor(22.2)  22.0 返回左侧的double数
  Math.max(a,b)  返回最大值
  Math.min(a,b)  小值
  Math.pow(2.0,3.0) 2的3次方
  Math.random();  [0.0,10.0)
```

System

```
 System.exit(1) //推出JVM虚拟机
 System.currentTimeMillis(); 当前时间的毫秒值
```

Object类

```
所有类的最终父类
比较对象两个对象

首先要重写对象ALT+INS自动成
a.equals(b)
```

Arrays

```
int[] arr = {1,5,6,7,9,4,3};

Arrays.toString(arr) ;  

Arrays.sort(arr);
```

![image-20201209212259239](G:\note\image\image-20201209212259239.png)

Integer

```
获取Integer类型值
Integer num1 = Integer.valueOf("100012"); //100012

数字转字符串
String str1 = String.valueOf(100); //100

字符串转数字
int y = Integer.parseInt("1231");//
```

![image-20201209222321674](G:\note\image\image-20201209222321674.png)

```
  // 获取时间戳
        Date d1 = new Date();
        //定义规则
        SimpleDateFormat val = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        System.out.println(val.format(d1));   2020年12月09日 22:33:22

        //解析 从String 到 Date
        String str = "2020-04-08 11:22:33";
        SimpleDateFormat val2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        //如果异常 选中 alt+enter;
        Date dd = val2.parse(str);
        System.out.println(dd);
```
  Calendar类

```
        //某一时刻和一组日历字段之间的转换
        Calendar c = Calendar.getInstance();
//        System.out.println(c);

        c.add(Calendar.YEAR,+3); //三年后的今天
        
        c.set(2048,11,20);//设置日历时间
        
        int year=c.get(Calendar.YEAR);
        
        int month=c.get(Calendar.MONTH)+1;
        
        int date=c.get(Calendar.DATE);
        
        System.out.println(year+"年"+month+"月"+date);
```

#### 异常

![image-20201210105000779](G:\note\image\image-20201210105000779.png)

![image-20201210105029616](G:\note\image\image-20201210105029616.png)

![image-20201210105043304](G:\note\image\image-20201210105043304.png)

![image-20201210105109923](G:\note\image\image-20201210105109923.png)



![image-20201210105159676](G:\note\image\image-20201210105159676.png)

![image-20201210105216024](G:\note\image\image-20201210105216024.png)

![image-20201210105233057](G:\note\image\image-20201210105233057.png)

### 集合

![image-20201210105709573](G:\note\image\image-20201210105709573.png)

![image-20201210110815783](G:\note\image\image-20201210110815783.png)

集合迭代器 Iterator

```java
集合遍历

Collection<String> c = new ArrayList<String>();

c.add("A");
c.add("B");
c.add("C");
//创建迭代器
Interator<String> it  = c.iterator();
//遍历
while(it.hasNext()){
    String s = it.next();
    System.out.println(s);
}
常用方法
```
#### List集合
```
list属于Collection可使用Collection的方法
可以精确元素插入的位置，可以通过索引访问元素，

与set不同，元素可以 重复
List集合方法

add(int index,E element);

remove(int index);

set(int index,E element);

get(int index);

List特有迭代器ListIterator

ListIterator<String> it = c.listIterator();

ListIterator常用方法

E next();下一个元素
boolean hasNext();
E previous(); 放回上一个元素
boolean hasPrevious(); 是否有上一个元素

add(E e);插入元素 与Iterator()唯一区别遍历过程张可以添加元素
```
#### 增强for循环

![image-20201210121632273](G:\note\image\image-20201210121632273.png)

```
简化数组和Collection集合的遍历

int[] arr = {1,2,3,4};
for(int i:arr){
    System.out.prinln(i);
}
```
#### 常见数据结构

![image-20201210123049513](G:\note\image\image-20201210123049513.png)

![image-20201210123114015](G:\note\image\image-20201210123114015.png)

![image-20201210123131302](G:\note\image\image-20201210123131302.png)

![image-20201210123205432](G:\note\image\image-20201210123205432.png)

#### ArrayList LinkedList

![image-20201210123443986](G:\note\image\image-20201210123443986.png)

LinkedList特有的方法

![image-20201210124320691](G:\note\image\image-20201210124320691.png)

#### set集合

```
与list集合的区别
1 元素不重复
2 没有索引
3 不能使用for(int i=0;i<x;i++) 遍历
除此之外没有差别
如果存储对象的保证唯一性，对象内要重写 equals hashCode方法
```
#### HashSet

![image-20201210125914120](G:\note\image\image-20201210125914120.png)

#### 哈希值

![image-20201210125445250](G:\note\image\image-20201210125445250.png)

![image-20201210130818587](G:\note\image\image-20201210130818587.png)

![image-20201210135823796](G:\note\image\image-20201210135823796.png)

LinkedHashSet

![image-20201210140548646](G:\note\image\image-20201210140548646.png).

#### TreeSet

![image-20201210141250948](G:\note\image\image-20201210141250948.png)

自定义排序

 第一种  对象内部重写compareTo方法

```
@Override
    public int compareTo(Student d){
        /*
        0 元素重复不存
        整数  递增
        负数  递减
        */
//        按年龄排序
       int num = this.age-d.age;
       // 年龄相同比较名字
       int num2 = num == 0? this.name.compareTo(d.name):num;
        return num2;
    }
```

第二种

```
                                                                                 //传入比较器
TreeSet<Student> sArr = new TreeSet<Student>(new Comparator<Student>() {
    @Override
    public int compare(Student o1, Student o2) {
       int num =o1.age-o2.age;
       int num2 = num ==0 ?o1.name.compareTo(o2.name) : num;
       return num2;
    }
});
sArr.add(new Student("xishi",29));
sArr.add(new Student("wangzhaojun",28));
sArr.add(new Student("diaochan",30));
sArr.add(new Student("yangyuhuan",33));
sArr.add(new Student("bangyuhuan",33));
System.out.println(sArr);
```

#### 泛型![image-20201210150338717](G:\note\image\image-20201210150338717.png)

泛型类

![image-20201210150108585](G:\note\image\image-20201210150108585.png)

![image-20201210150127458](G:\note\image\image-20201210150127458.png)

泛型方法

![image-20201210150809308](G:\note\image\image-20201210150809308.png)

![image-20201210150926987](G:\note\image\image-20201210150926987.png)

泛型接口

```java
接口

public interface Interface<T> {
    void show(T t);
}

实现类
public class InterfaceImpl <T> implements Interface <T> {
    @Override
     public void show(T t){
        System.out.println(t);
    }
}

测试类
public class InterfaceDemo {
    public static void main(String[] args) {
        Interface<String> stc = new InterfaceImpl<String>();
        stc.show("你好");
    }
}

```



#### 类型通配符
```
List<?> list = new ArraysList<String>();
List<?> list = new ArraysList<Number>();

类型通配符上限：<? extends 类型>
List<? extends Number>：它表示的类型是Number或者其子类型
List<? extends Number> list2 = new ArrayList<Number>();
List<? extends Number> list3 = new ArrayList<Integer>();

类型通配符下限：<? super 类型>
List<? super Number>：它表示的类型是Number或者其父类型

List<? super Number> list4 = new ArrayList<Object>();
List<? super Number> list5 = new ArrayList<Object>();
```
#### 可变参数![image-20201210153215381](G:\note\image\image-20201210153215381.png)



```
public static int sum(String s,int...a){
   sout(s)
    int sum = 0;
    for(int i:a){
        sum+=i;
    }
    return sum;
}

调用
System.out.println(sum(1,2,3,4));
```
#### 可变参数的使用
```
public static <T> List<T> asList(T... a)：返回由指定数组支持的固定大小的列表
返回的集合不能做增删操作，可以做修改操作
        List<String> list = Arrays.asList("hello","world","java");
//        list.add("new");  报错
//        list.remove("java");// 报错
        list.set(1,"java");
        
        
        

```
### map
```
map 集合存储的是键值对  键值是唯一的
Map<String,String> map = new HashMap<String,String>();
添加/修改元素元素
map.put("1","a");

//根据键移除元素
map.remove("1");

//清除所有元素
map.clear();

//是否存在此键
map.containsKey("1");

// 是否存在此值
map.containsValue("a");

// 判断集合是否为空
map.isEmpty();

// 判断集合的长度
map.size();

//根据键获取元素
map.get("1");

// 根据所有键的集合
Set<String> keySet = map.keySet();

for(String key:keySet){
    System.out.println(key);
}

// 获取所有值的集合
Collection<String> values = map.values();

for(String value:values){
    System.out.println(value);
}

map集合遍历
方式一
//获取所有键
    Set<String> set = map.keySet();
    //遍历键的集合
    for(String key:set){
        // 根据键获取value
        String value = map.get(key);
        System.out.println(value);
    }
方式二
//获取键值对集合
Set<Map.Entry<String, String>> enterSet = map.entrySet();
//便利键值对集合
for(Map.Entry<String, String>  en :enterSet){
    //获取key
    String key = en.getKey();
    //获取value
    String value = en.getValue();
    System.out.println(key+","+value);
}
Set<Map.>
```
#### Collections
```
List<Integer> list = new ArrayList<Integer>();
list.add(20);
list.add(10);
list.add(40);
list.add(50);
System.out.println("原本数据"+list);

//排序数组
Collections.sort(list);
System.out.println(list);

//反转数组
Collections.reverse(list);
System.out.println(list);

//随机排列数组
Collections.shuffle(list);
System.out.println(list);

//多天条件排序
Collections.sort();

List<Student> list = new ArrayList<Student>();
Student s1 = new Student("小明",20);
Student s2 = new Student("小红",30);
Student s3 = new Student("小胖",50);
Student s4 = new Student("小花",30);

Collections.sort(list, new Comparator<Student>() {
    @Override
    public int compare(Student o1, Student o2) {
        int num = o1.getAge()-o2.getAge();
        int num2 = num==0?o1.getName().compareTo(o2.getName()):num;
        return num2;
    }
});
```
### IO流

![image-20201210225842631](G:\note\image\image-20201210225842631.png)

![image-20201210225903575](G:\note\image\image-20201210225903575.png)

```
创建指定文件
// 将路径名转坏为抽象的对象
 File file = new File("D:\\safe\\demo1.txt");
 File file2 = new File("D:\\safe","demo1\\txt");
 
 File f3 = new File("D:\\safe");
 File f4 = new File(f3,"demo1.txt");
//创建文件 返回true false
file.createNewFile()

创建指定目录
 // 创建指定文件夹
File f2 = new File("D:\\safe\\demo");
System.out.println(f2.mkdir());

// File f2 = new File("D:\\safe\\demo\\demo");
f2.mkdirs();

删除
delete(); 删除抽象路径名表示的文件或目录
如果目录有内容  应线删除其中的文件，再删除此目录
File f2 = new File("D:\\safe\\demo\\demo");
f2.delete();

File的获取和判断

File f2 = new File("D:\\safe\\demo\\demo");

f2.isDirectory(); //是否为目录

f2.diFile();   //是否为文件

f2.exists();   //是否存在

f2.getAbsolutePath();   //获取绝对路径字符串

f2.getPath();  将抽象路径名 转为路径字符串

f2.getName();  返回抽象路径名的文件或目录名称

list(); 获取目录文件夹下的文件字符串数组
String[] strArray = f2.list();
for(String str:strArray){
    sout(str);
}
listFiles();  返回对象数组
File[] fileArray = f2.listFiles();
for(File file:fileArray){
    if(file.isFile()){
        sout(file.getName());
    }
}

```

#### 递归
```
在方法中自己调用自己
1 递归出口:如果没有出口 会内存溢出
jc(int n){
    if(n ==1){
        return 1;
    }else(
      return n*(n-1);
    )
}
```
#### IO流

按照数据类型分类
  字节流
  字符流
  区别 如果数据通过记事本能打开不乱码  字符流
  如果乱码 就是字节流 
  如果不知道属于什么类型，就用字节流(万能的流)

![image-20201210185934590](G:\note\image\image-20201210185934590.png)

```
字节流数据类型
  InputStream 所有输入流的超累 第二个参数为true时表示追加内容
  OutputStream 所有输出流的超类
  //写入数据
  //创建输出流
        FileOutputStream fs = new FileOutputStream("demo1.txt",);
         // write(字节)
        fs.write(100); // d
        
        byte[] bys="abcd".getBytes();
        fs.write(bys);
        
         //开始位置
        fs.write(bys,2,3);
        //最后释放资源 关闭输出流 释放资源
        fs.close();
        
    

```
字节流读数据

```
        
  public class test3 {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("File\\a.txt");
        //定义字节数组 10个数据长度
        byte[] bys = new byte[1024];

        //读取数据，读取的结果为字节 存在bys中
        int len =fis.read(bys);

        // 把读取的字节转为字符串
        System.out.println(new String(bys,0,len));
        byte[] bys = new byte[1024];
        int len;
        while ((len=fis.read(bys))!= -1){
            System.out.println(new String(bys,0,len));
        }
        fis.close();
    }
}
```

字节流复制文件

```
public class CopImg {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("File\\Java学习路线图.png");
        FileOutputStream fos = new FileOutputStream("File\\复制学习路线.png");
        byte[] bys = new byte[1024];
        int len;
        // 如果len = -1 说明没有更多数据局
        while ((len=fis.read(bys))!=-1){
            fos.write(bys);
        }
        //释放资源
        fis.close();
        fos.close();
    }
}
```

#### buffered字节缓冲流

![image-20201210194059255](G:\note\image\image-20201210194059255.png)

```
字节缓冲输出流
BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:\\safe\\demo.txt"));
写数据
bos.write("hello\r\n".getBytes());
bos.close();

字节输出流
BufferedInputStream bix = new BufferedInputStream(new FileInputStream("D:\\safe\\demo1.txt"));
int by;
while((by=bis.read())!=-1){
    sout((char)by);
}
//释放资源
bis.close();
```
复制文件

![image-20201210200331609](G:\note\image\image-20201210200331609.png)

#### 字符串的编码 解码

```java
getBytes() 使用默认字符集  编码String
getBytes("编码名") 使用指定字符集将String　编码为字节

String()使用 默认字符集解码字节数组 组成新的String
String("编码集") 指定字符集  组成新String
```
#### 字符流

字符流复制文件

```java
public class demo2 {
    public static void main(String[] args) throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream("File\\demo1.txt"));
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("File\\copy.txt"));
        char[] chs = new char[1024];
        int len;
        while ((len = isr.read(chs))!=-1){
            osw.write(chs,0,len);
        }
        isr.close();
        osw.close();
    }
}
```

复制文件改进版

```java
public class copy {
    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("File\\demo1.txt");
        FileWriter fw = new FileWriter("File\\copy1.txt");
        char[] chs = new char[1024];
        int len;
        while ((len = fr.read(chs))!=-1){
            fw.write(chs);
        }
        fr.close();
        fw.close();
    }
}
```

字符缓冲流

```java
public class BufferCopy {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("File\\demo1.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("File\\copy2.txt"));
        char[] chs = new char[1024];
        int len;
        while ((len = br.read(chs))!=-1){
            bw.write(chs);
           //  bw.newLine(); 写入换行符
        }
        br.close();
        bw.close();
    }
}
```
复制多个文件

```java
package demo4;

import java.io.*;

public class CopyFile {
    public static void main(String[] args) throws IOException {
        //创建数据源
        File srcFolder = new File("G:\\code\\java\\file");
        //获取数据源文件夹名称
        String srcFolderName = srcFolder.getName();
        // 获取目的地文件
        File destFoler=new File("File",srcFolderName);

        File[] files = srcFolder.listFiles();
        //如果目标文件夹不存在 创建
        if(!destFoler.exists()){
            destFoler.mkdir();
        }
        for(File file:files){
            String destFileName=file.getName();
         File destFile = new File(destFoler,destFileName);
         copy(file,destFile);
        }
    }
    public static void copy(File src,File dest) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
        byte[] bys = new byte[1024];
        int len;
        while ((len=bis.read(bys))!=-1){
            bos.write(bys);
        }
        bis.close();
        bos.close();
    }
}

```

#### 字节打印流

```
// 使用指定的文件名创建新的打印流
        PrintStream ps = new PrintStream("Study\\ps.txt");
        //write会转码
        ps.write(98);
        ps.println(456445);
        ps.println("你好啊");
        ps.close();
```
字符打印流

```
   public static void copy2() throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("File\\pw.txt"),true);
        //不换行
        pw.print("fasdfasdf");
        //换行
        pw.println("fasdfasdf");
        pw.close();
    }
```

字符打印流复制文件

```java
public static void copy3() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("File\\pw.txt"));
//        BufferedWriter bw = new BufferedWriter(new FileWriter("File\\pwss.txt"));
        PrintWriter pw = new PrintWriter(new FileWriter("File\\pwss.txt"),true);
        String line;
        while ((line = br.readLine())!= null){
            pw.print(line);
        }
        pw.close();
        br.close();
    }
```

#### 对象序列化

![image-20201211141843901](G:\note\image\image-20201211141843901.png)

```
        //ObjectOutputStream 创建一个指定写入文件
//        ObjectOutputStream oss = new ObjectOutputStream(new FileOutputStream("Study\\outObject.txt"));
        //创建对象  序列化的对象要实现Serializable接口 只是一个标识！没有方法要重写
//        Student s1 = new Student("小明",20);
        //写入对象
//        oss.writeObject(s1);
        // 释放资源
//        oss.close();

        //对象反序列化
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Study\\outObject.txt"));
        //读取对象
        Object obj = ois.readObject();
        //对象转型
        Student s = (Student)obj;
        System.out.println(s.getAge()+s.getName());
        ois.close();
```
![image-20201211144732056](G:\note\image\image-20201211144732056.png)

```
public class Student implements Serializable {
    //对象的唯一标识，防止因对象修改 序列化反序列化过程出错
    private final long serivalVersionUID = 42L;
    private  String name;
//    被transient修饰变量不参与序列化
    private transient int age;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Student() {

    }
}
```



#### Properties最为map集合使用

Properties作为map对象

```
Properties prop = new Properties();
// 添加元素
prop.put("A","1");
prop.put("B","2");
//普通方法遍历元素
Set<Object> keySet = prop.keySet();
for(Object key:keySet){
    Object val = prop.get(key);
    System.out.println(val);
}
```

properties 特有的方法遍历元素

```
// properties 特有的方法遍历元素
//添加元素
prop.setProperty("D","4");
//获取值
prop.getProperty("D");
//获取键的集合
Set<String> names = prop.stringPropertyNames();
for(String str:names){
    String str1 = prop.getProperty(str);
    System.out.println(str+"值为:"+str1);
}
```

Properties 操作文件数据读取

![image-20201211150218299](G:\note\image\image-20201211150218299.png)

![image-20201211151130575](G:\note\image\image-20201211151130575.png)

```
Properties 操作文件数据读取
      //从文件中读取数据
        Properties prop = new Properties();
        //读取文件
        FileReader fr = new FileReader("Study\\copy.txt");
        
        prop.load(fr);
        fr.close();
        
        //获取count的值
        String count = prop.getProperty("count");
        
        //转为number
        int number = Integer.parseInt(count);
        
        //判断值
        if(number>3){
            System.out.println("count>3");
        }else{
            System.out.println("重新开始");
            Scanner sc = new Scanner(System.in);

            //number++
            number++;
            prop.setProperty("count",String.valueOf(number));
            FileWriter fw = new FileWriter("Study\\copy.txt");
            prop.store(fw,null);
            fw.close();
        }
```
#### 线程 进程

```
进程 正在运行的程序
 线程 进程中的单个顺序控制流
 单线程 一个进程只有一条执行路径
 多线程 一个进程有多个执行路径
```

![image-20201211155245649](G:\note\image\image-20201211155245649.png)

![image-20201211152410208](G:\note\image\image-20201211152410208.png)

![image-20201211152825936](G:\note\image\image-20201211152825936.png)

```
//线程类
public class MyThread extends Thread{
    @Override
    public void run(){
       for(int i = 0;i<100;i++){

           System.out.println(Thread.currentThread().getName()+","+i);
       }
    }
}


 
    MyThread myThread = new MyThread();
        MyThread myThread2 = new MyThread();
        
        myThread.setName("线程一");
        myThread2.setName("线程二");
        
        myThread.start();
        myThread2.start();
```

![image-20201211154028451](G:\note\image\image-20201211154028451.png)

```

 
 //获取线程的优先级10-1
 th1.getPriority();// 获取线程优先级
 th1.setPriority(5) //设置线程优先级
 
 th1.sleep(1000); //线程休眠1000毫秒
 th1.join(); //优先执行th1线程 这个执行完后再之心其他的线程
 
 th1.setDaemon(true); //降低此线程的优先级  先执行其他线程设置为守护线程，当都是为守护线程时JVM虚拟机退出
 
 
 start 方法 启动线程 用JVM调用此线程的run 方法
 
 // 线程调度方式
 1 分时调度模型 所有线程轮流使用的cpu
 2 抢占式调度模型  按优先级的使用cpu
```
Runable接口多线程

```
创建线程类
public class RunableDem implements Runnable {
    @Override
    public void run(){
        for(int i=0;i<20;i++){
            System.out.println(Thread.currentThread().getName()+"="+i);
        }
    }
}

public class MyRunable {
    public static void main(String[] args) {
        //创建Runable类的对象
        RunableDem my = new RunableDem();

        Thread t1 = new Thread(my,"A");
        Thread t2 = new Thread(my,"B");


//        开启线程
        t1.start();
        t2.start();
    }
}

```



![image-20201211160444794](G:\note\image\image-20201211160444794.png)

#### 同步代码块解决数据安全问题

卖票

![image-20201211163751036](G:\note\image\image-20201211163751036.png)

```
public class RunableDem implements Runnable {
    private  int tickets = 100;
    private Object obj = new Object();
    @Override
    public void run(){
        while (true){
//            同步代码块 只能有一个线程执行
            synchronized (obj){
                if(tickets>0){
                    try {
                        Thread.sleep(100);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() +"正在卖"+tickets+"张票");
                    tickets--;
                }
            }
        }
    }
}


测试
public class MyRunable {
    public static void main(String[] args) {
        //创建Runable类的对象
        RunableDem my = new RunableDem();

        Thread t1 = new Thread(my,"窗口1");
        Thread t2 = new Thread(my,"窗口2");
        Thread t3= new Thread(my,"窗口3");


//        开启线程
        t1.start();
        t2.start();
        t3.start();
    }
}

```



```
package demo15;

public class SellTicket implements Runnable{

    private int tickets=100;
    Object obj = new Object();
    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    @Override
    public void run() {
        while (true){
            //使用同步代码块解决数据安全问题
            synchronized (obj) {
                if (this.tickets > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "卖票");
                    tickets--;
                }
            }
        }
    }
}

```
![image-20201211165222568](G:\note\image\image-20201211165222568.png)

![image-20201211184157430](G:\note\image\image-20201211184157430.png)

```java
public class RunableDem implements Runnable {
    private  int tickets = 100;
    private Lock lock = new ReentrantLock();
    @Override
    public  void run(){
        while (true){
            try {
                lock.lock();
                if(tickets>0){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+"正在卖"+tickets+"张票");
                    tickets--;
                }
            }finally {
                lock.unlock();
            }

        }
    }
}
```



#### 生产者消费者模式
```
所谓生产者消费者问题，实际上主要是包含了两类线程
一类是生产者线程用于生产数据
一类是消费者线程用于消费数据
为了解耦生产者和消费者的关系，通常会采用共享的数据区域，就像是一个仓库
生产者生产数据之后直接放置在共享数据区中，并不需要关心消费者的行为
消费者只需要从共享数据区中去获取数据，并不需要关心生产者的行为
```
![image-20201211190127628](G:\note\image\image-20201211190127628.png)
```
案例

1 奶箱类Box 定义一个成员变量，表示X瓶奶提供存储牛奶和获取牛奶操作
2 生产者类Producer 实现Runnable接口重写run()方法调用存储牛奶的操作
3 消费者类Customer 实现Runnable 接口重写run()方法调用获取牛奶的操作
4 测试类 里面有main方法
   1 创建奶箱对象 这是共享数据区域
   2 创建生产者对象 把奶箱对象作为构造方法参数传递，因为这个类中要调用存储牛奶的操作
   3 消费者对象，参数为奶箱对象，要获取牛奶
   4 创建2个线程对象，把生产者 消费者对象作为构造方法参数传递
   4 启动线程
1 奶箱类
public class BoxDemo{
    //存储奶箱数
    private int milk;
    //是否有奶
    private boolean state = false;
    //获取牛奶
    public synchronized void getMilk() {
        //如果没有等待生产
        if(!state){
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //如果有就获取
        System.out.println("得到"+this.milk+"牛奶");
        //获取后该表状态
        state= false;
        //通知唤醒线程
        notifyAll();
    }
    // 生产牛奶
    public synchronized void setMilk(int milk){
        // 如果有奶准备消费
        if(state){
           try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 如果没有就生产
        this.milk = milk;
        System.out.println("存入"+this.milk+"牛奶");
        //生产完毕修改状态
        state=true;
        notifyAll();
    }
}

//生产者类
public class Producer implements  Runnable{
    //定义牛奶变量 表示X瓶牛奶
    private BoxDemo b;
    // 定义存储牛奶
    public Producer(BoxDemo b) {
        this.b = b;
    }
    @Override
    public void run(){
        for(int i=0;i<=30;i++){
            b.setMilk(i);
        }
    }
}
//消费者类
public class Customer implements Runnable {
    private BoxDemo b;

    public Customer(BoxDemo b) {
        this.b = b;
    }

    @Override
    public void run(){
        while (true){
            b.getMilk();
        }
    }
}

// 测试类
public class Box {
    public static void main(String[] args) {
        BoxDemo box=new BoxDemo();
        Customer cus = new Customer(box);
        Producer pro = new Producer(box);
        Thread t1 = new Thread(cus);
        Thread t2 = new Thread(pro);
        t1.start();
        t2.start();
    }
}

```
#### 网络编程
```
网络编程   在通信协议下 不同计算机间的数据交换
三要素
  IP地址 计算机的编号
  端口   计算中的应用程序表示
  协议   多台计算机的通讯规则
  
```
#### InetAddress 获取主机名 IP
```
//确定主机名
InetAddress address = InetAddress.getByName("192.168.0.100");
//获取主机名
String name1=address.getHostName();
//获取IP
String name2=address.getHostAddress();
System.out.println("主机名:"+name1);
System.out.println("主机IP:"+name2);
```
#### DUP数据发送接收

![image-20201211191210515](G:\note\image\image-20201211191210515.png)

发送数据

![image-20201211192813936](G:\note\image\image-20201211192813936.png)

```
发送端
public class SendDemo {
    public static void main(String[] args) throws IOException {
        //穿件发送端Socket对象
        DatagramSocket ds = new DatagramSocket();
        // 穿件数据
        byte[] bys = "hello.udp.nihaoma".getBytes();
        System.out.println(bys);
        DatagramPacket dp = new DatagramPacket(bys, bys.length, InetAddress.getByName("192.168.0.100"),10086);
        ds.send(dp);
        ds.close();
    }
```

接收数据![image-20201211192855001](G:\note\image\image-20201211192855001.png)



```
接收端
public class ReceiveDemo {
    public static void main(String[] args) throws IOException {
        //创建接收端Socket对象
        DatagramSocket ds = new DatagramSocket(10086);
        // 穿件接受数据包
        byte[] bys = new byte[1024];
        DatagramPacket dp = new DatagramPacket(bys, bys.length);
        //接受数据包
        ds.receive(dp);
        //获取缓冲区的数据
        byte[] datas = dp.getData();
        // 获取发送数据的长度并不是1024
        int len = dp.getLength();
        // 把字节数组转为字符串
        String str = new String(datas,0,len);
        System.out.println(str);
        //关闭接收端
        ds.close();
    }
}

```
#### TCP通信模型

![image-20201211191234549](G:\note\image\image-20201211191234549.png)

![image-20201211191246978](G:\note\image\image-20201211191246978.png)

```
客户端
public class ClientDemo {
    //TCP 发送数据步骤
//    1 创建客户端的Socket对象
//    2 获取输出流，写数据
//    3 释放资源
    public static void main(String[] args) throws IOException {
        //创建socket对象
        Socket s= new Socket("192.168.0.100",1000);
        //获取输出流写数据
        OutputStream os = s.getOutputStream();
        os.write("hello,tcp,我来了".getBytes());
        //释放资源
        s.close();
    }
}

    
接收端
public class ServerDemo {
    public static void main(String[] args) throws IOException {
        /*
        Tcp接收端步骤
        1 创建服务器接收端Socket对象
        2 获取输入流，读取数据，把数据显示在控制台
        3 释放资源
        * */

        //服务端接收对象
        ServerSocket ss = new ServerSocket(1000);
        //监听链接
        Socket s = ss.accept();

        //获取输入流，读取数据
        InputStream is = s.getInputStream();
        byte[] bys = new byte[1024];
        int len = is.read(bys);
        String data = new String(bys,0,len);
        System.out.println("接收的数据为:"+data);
        s.close();
        ss.close();
    }
}

```
#### Lambda表达式

![image-20201211215550583](G:\note\image\image-20201211215550583.png)

练习

```
定义接口 只有一个抽象函数
public interface Eatable {
    void eat();
}

测视类
public class EatableDemo {
    public static void main(String[] args) {
//        useEatable(new Eatable() {
//            @Override
//            public void eat() {
//                System.out.println("吃了吗");
//            }
//        });
        useEatable(()-> System.out.println("吃了吗"));
    }
    public static void useEatable(Eatable a){
        a.eat();
    }
}
```

带参数

```
public interface Eatable {
    void eat(String s);
}


public class EatableDemo {
    public static void main(String[] args) {
                             形参
        useEatable((String s)->{System.out.println(s+"吃了吗");});
    }
    public static void useEatable(Eatable a){
                  实参
        a.eat("参数");
    }
}


```

带返回值

```
public interface Eatable {
    int add(int x,int y);
}


public class EatableDemo {
    public static void main(String[] args) {
//                  形参
         useEatable((int x,int y)-> x+y);
    }
    public static void useEatable(Eatable a){
//                 实参
        int m =a.add(10,20);
        System.out.println("结果" + m);
    }
}
```



```
// 使用Lambda必须要有一个借口，必须只有一个抽象方法

// lambda必须有上下文环境，才能推导出lambda对应的借口
g根据局部变量赋值得知Lambda对应的借口
Runable r = ()->sout("可以");
根据调用方法的参数的值lambda的对应借口
new thread(()->sout("可以")).start();



useEatable((val1,val2)->{
            int val3=val1+val2;
            System.out.println(val3);
        });
        //省略模式
//        只有一个参数 省略小括号
//        代码块只有一个省略{}
        useEatable(param-> System.out.println(param));
        
        
```
#### 接口组成更新

![image-20201211222350478](G:\note\image\image-20201211222350478.png)

![image-20201211223027996](G:\note\image\image-20201211223027996.png)

```
public interface Eatable {
   void show1();
   void show2();
   //可以被重写  重写时不加default
   default void show3(){
       System.out.println("show3");
   }
}
```

接口静态方法

![image-20201211224304869](G:\note\image\image-20201211224304869.png)

接口中静态方法

![image-20201211224434306](G:\note\image\image-20201211224434306.png)

#### 方法引用

![image-20201211225057872](G:\note\image\image-20201211225057872.png)

```
public interface Converter {
    int convert(String s);
}


public class EatableDemo {
    public static void main(String[] args) {
        useConverter((String s)->{
            return Integer.parseInt(s);
        });
        //简化
        useConverter(Integer::parseInt);
    }
    private static void useConverter(Converter c){
        int number = c.convert("666");
        System.out.println(number);
    }
}

```

![image-20201212074215603](G:\note\image\image-20201212074215603.png)

```
 //Consumer
    public static void printInfo(String[] strArrays, Consumer<String> con1,Consumer<String> con2){
        for(String str:strArrays){
            con1.andThen(con2).accept(str);
        }
    }
    
String[] strArrays = {"林青霞,30","张曼玉,33"};
        printInfo(strArrays,(String str)->{
            System.out.print(str.split(",")[0]);
        },(String str)->{
            System.out.println(Integer.parseInt(str.split(",")[1]));
        });
//        lambda
        printInfo(strArrays,str -> System.out.print(str.split(",")[0]),str -> System.out.println(Integer.parseInt(str.split(",")[1])));
```

![image-20201212080830021](G:\note\image\image-20201212080830021.png)

```
public static boolean checkString(String s, Predicate<String> pre1, Predicate<String> pre2){
        return !pre.test(s);
        return pre1.and(pre2).test(s);  //结果&&
        return pre1.or(pre2).test(s);    // 结果 ||
    }
    
    
    //多条件判断 添加数组
    private static ArrayList<String> myFilter(String[] strArray,Predicate<String> pre1,Predicate<String> pre2){
        ArrayList<String> strings = new ArrayList<>();
        for(String str : strArray){
            if(pre1.and(pre2).test(str)){
                return strings.add(str);
            }
        }
    }
    
    myFilter(strArrays,s->s.split(",")[0].length()>3,s->Integer.parseInt(s.split(",")[1])>10);
```



```
1 引用类方法  类名::方法名
2 应用对象的实例方法  实例对象的名::方法名   "helloWorld"::toUpperCase
3 引用类的实例方法     类的实例名::方法名    String::substring
4 引用构造器          类名::new           Student::new

函数式接口
//函数式接口 只有一个抽象方法的接口
@FunctionalInterface
public interface MyInterface {
    void show1();
}

 函数式接口作为方法的参数
    如果方法的参数是一个函数式接口，我们可以使用Lambda表达式作为参数传递
     startThread(() -> System.out.println(Thread.currentThread().getName() + "线程启动了"));


函数式接口作为方法的返回值
如果方法的返回值是一个函数式接口，我们可以使用Lambda表达式作为结果返回
private static Comparator<String> getComparator() { 
return (s1, s2) -> s1.length() - s2.length(); 
       }




Supplier接口

package demo23;

import java.util.function.Supplier;

public class MyinterfaceDemo {
    public static void main(String[] args) {

        String s= getString(()->"66");
        System.out.println(s);
        int num = getInt(()->666);
        System.out.println(num);
    }
    private static String getString(Supplier<String> sup){
        return sup.get();
    }
    private static int getInt(Supplier<Integer> sup){
        return sup.get();
    }
}

Predicate接口
Predicate<T>：常用的四个方法
boolean test(T t)：对给定的参数进行判断(判断逻辑由Lambda表达式实现)，返回一个布尔值 
default Predicate<T> negate()：返回一个逻辑的否定，对应逻辑非
default Predicate<T> and(Predicate other)：返回一个组合判断，对应短路与
default Predicate<T> or(Predicate other)：返回一个组合判断，对应短路或
Predicate<T>接口通常用于判断参数是否满足指定的条件
String[] strArray = {"林青霞,30", "柳岩,34", "张曼玉,35", "貂蝉,31", "王祖贤,33"};
字符串数组中有多条信息，请通过Predicate接口的拼装将符合要求的字符串筛选到集合ArrayList中，并遍历ArrayList集合
同时满足如下要求：姓名长度大于2；年龄大于33

//返回boolean结果
private static boolean checkString(String s, Predicate<String> str){
    return str.test(s);
}

 boolean b1= checkString("nihao",(str)->str.length()>6);
        System.out.println(b1);
        
 //对结果去反
    private static boolean checkString1(String s, Predicate<String> str1){
        return str1.negate().test(s);
    }        
boolean b2= checkString1("nihao",(str)->str.length()>6);
        System.out.println(b2);
   
 //多条件判断       
  private static ArrayList<String> myFilter(String[] strArray,Predicate<String> pre1,Predicate<String> pre2){
     //定义集合
     ArrayList<String> array = new ArrayList<>();
     //遍历数组
        for(String str : strArray){
            if(pre1.and(pre2).test(str)){
                array.add(str);
            }
        }
        return  array;
    }      

 String[] strArray = {"林青霞,30","刘岩,34","张曼玉,35","王祖贤,33"};
        ArrayList<String> array = myFilter(strArray,s->s.split(",")[0].length()>2,s->Integer.parseInt(s.split(",")[1])>32);
        System.out.println(array);  // [张曼玉,35]

Function 

package demo26;

import java.util.function.Function;

public class FunctionDemo {
    /*
    Function<T,R> 常用方法
    R apply(T t):将次函数用于给定的参数
    default<V> function andThen(Function after):返回一个组合函数,首先将该函数应用于输入,然后将after用于结果
    Function<T,R> 对参数进行处理,转换返回一个新的值
    * */
    public static void main(String[] args) {
        covert("1000",(s)->Integer.parseInt(s));
        covert("10",Integer::parseInt);

        covert1(100,(i)->String.valueOf(i+99));


        convert2("100",s->Integer.parseInt(s),i->String.valueOf(i + 66));
    }
    //字符串转换为int 在控制台输出
    private static void covert(String s, Function<String,Integer> fun){
        int i = fun.apply(s);
        System.out.println(i);
    }
    //把int类型数据加上一个整数猴，转为字符串在控制台输出
    private static void covert1(int i,Function<Integer,String> fun){
        String s=fun.apply(i);
        System.out.println(s);
    }
    //定义一个方法 把一个字符串转换int类型，把int类型的数据加一个整数后，转为字符换
    private static void convert2(String s,Function<String,Integer> fun1,Function<Integer,String> fun2){
     //   Integer i = fun1.apply(s);
     //   String ss = fun2.apply(i);
     String ss =  fun1.andThen(fun2).apply(s);
        System.out.println(ss);
    }
}

```
#### 函数式接口

![image-20201211232031685](G:\note\image\image-20201211232031685.png)

```
@FunctionalInterface
public interface MyInterface {
    void show();
}


public class MyInterfaceDemo {
    public static void main(String[] args) {
        MyInterface my = () -> System.out.println("函数式接口");
        my.show();
    }
}

```

函数式接口作为参数

![image-20201211232537257](G:\note\image\image-20201211232537257.png)

```
public class MyInterfaceDemo {
    public static void main(String[] args) {
        startThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"线程启动了");
            }
        });
       // 函数式接口作为参数 -> 直接写成lambda
        startThread(()-> System.out.println(Thread.currentThread().getName()+"线程启动了"));
    }

    private static void startThread(Runnable r){
        new Thread(r).start();
    }
}
```

![image-20201211234142581](G:\note\image\image-20201211234142581.png)

#### 常用的函数式接口

![image-20201211234856916](G:\note\image\image-20201211234856916.png)

```java
public class SupplierDemo {
    public static void main(String[] args) {
        String s = getString(()->"adfasdf");
        System.out.println(s);

        Integer i = getInteger(()->13256456);
        System.out.println(i);

        int[] arr = {1,2,4,5};
        int m = getMax(()->{
            int max = arr[0];
            for(int x = 1;x<arr.length;x++){
                if(arr[i] > max){
                    max = arr[i];
                }
            }
            return  max;
        })
    }
    //获取字符串
    public static String getString(Supplier<String> s){
        return s.get();
    }
    //获取整数
    public static Integer getInteger(Supplier<Integer> i){
        return i.get();
    }
    //获取最大值
    public static Integer getMax(Supplier<Integer> m){
        return m.get();
    }
}

```



#### Stream流

![image-20201212083340786](G:\note\image\image-20201212083340786.png)

```
ArrayList<String> strings = new ArrayList<>();
        strings.add("123");
        strings.add("张敏");
        strings.add("张无忌");
        strings.add("小红");
             //stream()生成流
        strings.stream().filter(s->s.startsWith("张")).filter(s->s.length()>2).forEach(System.out::print);
```

![image-20201212083401797](G:\note\image\image-20201212083401797.png)

```
//        Collections体系的集合使用默认stream()生成流
        ArrayList<String> list = new ArrayList<>();
        Stream<String> listStream = list.stream();

        HashSet<String> set = new HashSet<>();
        Stream<String> setStream = set.stream();

        //map体系集合间接生成
        HashMap<String, Integer> map = new HashMap<>();
        //通过键的集合
        Stream<String> keyStream = map.keySet().stream();
        //通过值的集合
        Stream<Integer> valueStream = map.values().stream();
        //通过键值对的集合
        Stream<Map.Entry<String, Integer>> entryStream = map.entrySet().stream();

        //数组可以通过Stream接口的静态方法of(T... values)生成
        String[] strArray = {"hellow","world","java"};
        Stream<String> strArrayStream = Stream.of(strArray);
        Stream<Integer> intStream = Stream.of(10,20,30);
```



```
package Stream27;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("林青霞");
        list.add("张曼玉");
        list.add("王祖贤");
        list.add("刘岩");
        list.add("张敏");
        list.add("张无忌");
        
        //创建新集合存储张开头的元素
        ArrayList<String> list1 = new ArrayList<>();
        //创建新集合存储 张 长度为三 开头的元素
        ArrayList<String> list2 = new ArrayList<>();
        for(String s : list){
            if(s.startsWith("张")){
                list1.add(s);
                if(s.length()==3){
                    list2.add(s);
                }
            }
        }
        System.out.println(list1); // [张曼玉, 张敏, 张无忌]
        System.out.println(list2);  // [张曼玉, 张无忌]
        
        //Stream流改进 常见操作
        list.stream().filter(s->s.startsWith("张")).filter(s->s.length()==3).forEach(i-> System.out.println(i));
        
        //取出前三个
        list.stream().limit(3).forEach(System.out::println);
        
        //跳过前三个
        list.stream().skip(3).forEach(System.out::println);
        
        // 跳过前三个 取出剩下前二个
        list.stream().skip(3).limit(2).forEach(System.out::println);
        
        // 合并两个流
        Stream<String> list5 = list.stream().limit(3);
        Stream<String> list6 = list.stream().skip(2);
//        合并
        Stream.concat(list5,list6).forEach(System.out::println);


        //合并流并且元素不重复
//        Stream.concat(list5,list6).distinct().forEach(System.out::println);
        //sort排序
        list.stream().sorted().forEach(System.out::println);
        //自定义比较器
        list.stream().sorted((s1,s2)->{
          int num = s1.length()-s2.length();
          int num2 = num==0?s1.compareTo(s2):num;
          return num2;
        });
        
        
        //stream 流的收集方法
        Stream<String> listStream2 = list.stream().filter(s->s.length()==3);

        //把stream流操作的数据收集到list集合中并遍历
        List<String> names = listStream2.collect(Collectors.toList());
        for (String name:names){
            System.out.println(name);
        }

        //stream流收集到Set集合中
        Set<Integer> set = new HashSet<Integer>();
        set.add(10);
        set.add(20);
        set.add(30);
        set.add(40);
        //获取年龄大于25的流
        Stream<Integer> setStream = set.stream().filter(age->age>25);
        //把 Stream流收集到set集合并遍历
        Set<Integer> ages = setStream.collect(Collectors.toSet());
        for (Integer age : ages){
            System.out.println(age);
        }

        //定义一个字符串
        String[] strArray = {"林青霞,30","张曼玉,35","王祖贤,33","刘岩,25"};
        //获取字符串中年龄数据大于28的流
        Stream<String> arrayStream = Stream.of(strArray).filter(s->Integer.parseInt(s.split(",")[1])>28);
        //把Stream流操作的数据收集到Map集合并遍历，字符串做键,年龄做值
        Map<String,Integer> map = arrayStream.collect(Collectors.toMap(s->s.split(",")[0],s->Integer.parseInt(s.split(",")[1])));
        Set<String> keySet = map.keySet();
        for (String str:keySet){
            Integer val = map.get(str);
            System.out.println(val+","+val);
        }
        // Stream生成流的方式
        // 1 Collection 体系集合 使用默认方法Stream()生成流
        List<String> list4 = new ArrayList<String>();
        Stream<String> listStream = list4.stream();
        Set<String> set = new HashSet<String>();
        Stream<String> setStream = set.stream();

        //map体系的集合间接生成流
        Map<String,Integer> map = new HashMap<String, Integer>();
        Stream<String> keyStream = map.keySet().stream();
        Stream<Integer> valueStream = map.values().stream();
        Stream<Map.Entry<String,Integer>> entryStream = map.entrySet().stream();

        //数组通过Stream接口的静态方法of(T...values)生成流
        String[] strArray = {"hello","world","java"};
        Stream<String> strArrayStream1 = Stream.of("hello","world","java");
        Stream<String> strArrayStream2 = Stream.of(strArray);
        Stream<Integer> intStream = Stream.of(10,20,30);
    }
    
    //把流生成到集合中
    Set<Integer> ages = setStream.collect(Collections.toSet());
    
}

```
#### 类加载器

![image-20201212092734148](G:\note\image\image-20201212092734148.png)

![image-20201212092810004](G:\note\image\image-20201212092810004.png)

#### 反射

![image-20201212094725794](G:\note\image\image-20201212094725794.png)

![image-20201212094738952](G:\note\image\image-20201212094738952.png)

![image-20201212094806423](G:\note\image\image-20201212094806423.png)

```
 Student student = new Student("小明",20);
        //使用class获取对应类的class对象
        Class<Student> studentClass = Student.class;

        //使用getClass获取
        Class<? extends Student> aClass = student.getClass();
```

![image-20201212101010918](G:\note\image\image-20201212101010918.png)

```
  //获取字解码对象
        Class<Student> studentClass = Student.class;
        //获取多有public 构造函数
        Constructor<?>[] declaredConstructors = studentClass.getDeclaredConstructors();
        //获取所有公仔函数
        Constructor<?>[] constructors = studentClass.getConstructors();
        //获取单个构造函数
        Constructor<Student> constructor = studentClass.getConstructor();
        //创建对象
        Student student = constructor.newInstance();
        System.out.println(student);
```

![image-20201212104146437](G:\note\image\image-20201212104146437.png)

![image-20201212104239714](G:\note\image\image-20201212104239714.png)

```
package demo9;

import demo4.Student;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Stream;

public class test1 {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        //获取字解码对象
        Class<Student> studentClass = Student.class;
        //获取多有私有  构造函数
        Constructor<Student> declaredConstructor = studentClass.getDeclaredConstructor(String.class);
        //获取所有public函数
        Constructor<?>[] constructors = studentClass.getConstructors();
        //获取单个构造函数
        Constructor<Student> constructor = studentClass.getConstructor(String.class,int.class);

        //公共构造函数创建对象
        Student student = constructor.newInstance("小明",100);


        //使用私有构造函数 暴力反射
        declaredConstructor.setAccessible(true); //设为true取消访问检查，可以使用私有构造方法，构造队形
        System.out.println(declaredConstructor.newInstance("小明"));

        //获取公共成员变量
        Field[] fields = studentClass.getFields();
        //获取所有成员变量
        Field[] declaredFields = studentClass.getDeclaredFields();
        //获取公共单个成员变量e
//        Field field = studentClass.getField("add");

        //set 指定对象的参数 给student的field成员变量赋值
//        field.set(student,"小明");

        // 私有成员变量
        Field name = studentClass.getDeclaredField("name");
        name.setAccessible(true);
        name.set(studentClass,"姓名");


        //成员方法  获取所有公共成员方法
        Method[] methods = studentClass.getMethods();
        //获取所有成员方法
        Constructor<?>[] declaredConstructors = studentClass.getDeclaredConstructors();
        //获取单个方法
        Method m= studentClass.getMethod("show", String.class);
        m.invoke(studentClass,"show");
    }

}

```

## JavaWeb

#### XML

```
可扩展的标记语言
XML的主要作用
  1 用来保存数据
  2 作为项目或模块的配置文件
  3 网络传输数据的格式
  
  <?xml version="1.0" encoding="UTF-8"?>
<books>
    <book sn="SN12341232">
        <name>辟邪剑谱</name>
        <price>9.9</price>
        <author>班主任</author>
    </book>
    <book sn="SN12341231">
        <name>葵花宝典</name>
        <price>99.99</price>
        <author>班长</author>
    </book>
</books>

dom4j解析xml

```
解析XML

不管是 html 文件还是 xml 文件它们都是标记型文档，都可以使用 w3c 组织制定的 dom 技术来解析。

![image-20201212130853534](G:\note\image\image-20201212130853534.png)

导入jar包

![image-20201212132216064](G:\note\image\image-20201212132216064.png)

```

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.math.BigDecimal;
import java.util.List;

public class Dom4Test {
    public static void main(String[] args) throws DocumentException {
        Book bookObj = new Book();
        //穿件读取文件对象
        SAXReader saxReader = new SAXReader();
        //读取文件

        Document document = saxReader.read("G:\\code\\java\\demo3\\XML\\src\\demo1\\test1.xml");
        //获取根元素
        Element rootElement = document.getRootElement();
        //获取每个book元素
        List<Element> books = rootElement.elements("book");
        //遍历每个book
        for(Element book:books){
            //把标签对象转为标签字符串
            System.out.println(book.asXML());
            直接获取标签内容
            String nameText = book.elementText("name");
            System.out.println(nameText);
            //获取每个元素对象
            Element name = book.element("name");
            String text = name.getText();
            bookObj.setName(text);
            //获取价格
            Element price = book.element("price");
            String text1 = price.getText();
            bookObj.setPrice(new BigDecimal(text1));
            //获取作者
            Element author = book.element("author");
            String text2 = author.getText();
            bookObj.setAuthor(text2);
            //获取属性
            String sn = book.attributeValue("sn");
            bookObj.setSn(sn);
            System.out.println(bookObj);
        }
    }
}

```

#### javaWeb

什么是 JavaWeb

```
JavaWeb 是指，所有通过 Java 语言编写可以通过浏览器访问的程序的总称，叫 JavaWeb。
JavaWeb 是基于请求和响应来开发的。
```

#### Tomcat
```
1 前提配置好JAVA_HOME系统变量
2 点击 bin/startup.bat
3 关闭 bin/shutdown.bat
```
#### 部署项目

方法一

```
部署项目
在webapp目录下book文件夹 /index.html
访问 8009 http://localhost:8080/book/
```

方法二

![image-20201212142443009](G:\note\image\image-20201212142443009.png)

```
找到 Tomcat 下的 conf 目录\Catalina\localhost\ 下,创建如下的配置文件：
新建XML文件

<Context path="/book" docBase="G:\code\java\javaweb\book" />
```



IDEA创建动手 手托 托 html 页面到浏览器和在浏览器中输 页面到浏览器和在浏览器中输入 入 http://ip:端 端
口号 口号/ 工程名/访问的区别 访问的区别态web工程

![image-20201212142803209](G:\note\image\image-20201212142803209.png)

![image-20201212142817346](G:\note\image\image-20201212142817346.png)

#### IDEA整合tomcat服务器

<img src="G:\note\image\image-20201212143703196.png" alt="image-20201212143703196" style="zoom:50%;" />

![image-20201212143734160](G:\note\image\image-20201212143734160.png)

![image-20201212143757734](G:\note\image\image-20201212143757734.png)

![image-20201212143835815](G:\note\image\image-20201212143835815.png)

#### 创建web工程

1 创建模块

![image-20201212144106735](G:\note\image\image-20201212144106735.png)

web工程目录

![image-20201212145503361](G:\note\image\image-20201212145503361.png)

#### web项目配置

![image-20201212152650436](G:\note\image\image-20201212152650436.png)

![image-20201212152821607](G:\note\image\image-20201212152821607.png)

#### Servlet

```
1 Servlet 是javaEE规范之一
2 Servlet 是javaWeb三大组件之一 三大组件分别是Servlet程序，Filter过滤器Listener监听器
3 Servlet 是运行在服务器上的一个Java小程序，它可以接受客户端发送的请求，并响应数据给客户端
```
第一个Servlet程序

```
 1 创建类实现Servlet接口 

2 实现service方法 处理请求，并响应数据

3 在web.xml中配置servlet程序访问地址
```

第一 创建类实现Servlet接口 

![image-20201212154739593](G:\note\image\image-20201212154739593.png)

第二  实现方法



```java
//   创建类实现 Servlet接口
//   ALT+Enter自动实现所有方法
public class HelloServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("要被访问来了");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}

```

第三 web.xml  配置访问路径  

```java
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
version="4.0">
    
<!-- servlet 标签给 Tomcat 配置 Servlet 程序 -->
<servlet>
    
<!--servlet-name 标签 Servlet 程序起一个别名（一般是类名） -->
<servlet-name>HelloServlet</servlet-name>
    
<!--servlet-class 是 Servlet 程序的全类名 -->
<servlet-class>com.atguigu.servlet.HelloServlet</servlet-class>
</servlet>
    
<!--servlet-mapping 标签给 servlet 程序配置访问地址 -->
<servlet-mapping>
    
<!--servlet-name 标签的作用是告诉服务器，我当前配置的地址给哪个 Servlet 程序使用 -->
<servlet-name>HelloServlet</servlet-name>
    
<!--url-pattern 标签配置访问地址 <br/>
/ 斜杠在服务器解析的时候，表示地址为： http://ip:port/ 工程路径 <br/>
/hello 表示地址为： http://ip:port/ 工程路径 /hello <br/>
-->
<url-pattern>/hello</url-pattern>
</servlet-mapping>
</web-app>
```

Servlet的生命周期

1 执行构造器  (只在第一次访问时调用)

2 执行init方法 (只在第一次访问时调用)

3 执行service方法 （每次请求都会调用）

4 destroy 销毁方法 在web工程停止时候调用

```
public class HelloServlet implements Servlet {
    public HelloServlet() {
        System.out.println("构造函数");
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("hellow");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
```

#### Servlet-->get和post分类

```java
public class HelloServlet implements Servlet {
/**
* service 方法是专门用来处理请求和响应的
* @param servletRequest
* @param servletResponse
* @throws ServletException
* @throws IOException
*/
@Override
public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws
ServletException, IOException {
    System.out.println("3 service === Hello Servlet 被访问了");
    // 类型转换（因为它有 getMethod() 方法）
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    
    
    // 获取请求的方式
    String method = httpServletRequest.getMethod();
        if ("GET".equals(method)) {
        doGet();
        } else if ("POST".equals(method)) {
        doPost();
        }
    }
    
    
/**
* 做 get 请求的操作
*/
    public void doGet(){
        System.out.println("get 请求");
        System.out.println("get 请求");
    }
    
    
/**
* 做 post 请求的操作
*/
    public void doPost(){
        System.out.println("post 请求");
        System.out.println("post 请求");
    }
}
```

#### HttpServlet

1 创建类继承HttpServlet （Httpservlet是Servlet的子类）

2 重写doGet doPost方法

3 web.xml配置请求地址

```java
public class HelloServlet2 extends HttpServlet {
/**
* doGet （）在 get 请求的时候调用
* @param req
* @param resp
* @throws ServletException
* @throws IOException
*/
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
    IOException {
    System.out.println("HelloServlet2 的 的 doGet 方法");
    }
/**
* doPost （）在 post 请求的时候调用
* @param req
* @param resp
* @throws ServletException
* @throws IOException
*/
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
    IOException {
    System.out.println("HelloServlet2 的 的 doPost 方法");
    }
}
```

IDEA自动生成HttpServt文件
![image-20201212164158302](G:\note\image\image-20201212164158302.png)

在web.xml配置一下访问路径就好

Servlet的继承体系

![image-20201212165556635](G:\note\image\image-20201212165556635.png)

#### ServletConfig类的三大作用

在HttpServlet中可以获取到

```
ServletConfig servletConfig = getServletConfig();
```

1 获取Servlet程序的servlet-name的值

2 获取获取web.xml中当前Servlet表中的init-param中的值

3 获取ServletContext对象

```xml
<!-- servlet 标签给 Tomcat 配置 Servlet 程序 -->
<servlet>
    <!--servlet-name 标签 Servlet 程序起一个别名（一般是类名） -->
    <servlet-name>HelloServlet</servlet-name>
    <!--servlet-class 是 Servlet 程序的全类名 -->
    <servlet-class>com.atguigu.servlet.HelloServlet</servlet-class>
    
    <!--init-param 是初始化参数 -->
    <init-param>
        <!-- 是参数名 -->
        <param-name>username</param-name>
        <!-- 是参数值 -->
        <param-value>root</param-value>
    </init-param>
    
    <!--init-param 是初始化参数 -->
    <init-param>
        <!-- 是参数名 -->
        <param-name>url</param-name>
        <!-- 是参数值 -->
        <param-value>jdbc:mysql://localhost:3306/test</param-value>
    </init-param>
</servlet>
```

获取值

```java
 

@Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //servlet-name
        System.out.println(servletConfig.getServletName());
        
        //获取web.xml中配置的init-param参数
        System.out.println(servletConfig.getInitParameter("username"));
        System.out.println(servletConfig.getInitParameter("password"));

        //获取ServletContext对象
        System.out.println(servletConfig.getServletContext());
    }
```

#### ServletContext类

什么是 ServletContext?
1、ServletContext 是一个接口，它表示 Servlet 上下文对象
2、一个 web 工程，只有一个 ServletContext 对象实例。
3、ServletContext 对象是一个域对象。
4、ServletContext 是在 web 工程部署启动的时候创建。在 web 工程停止的时候销毁

什么是域对象?
域对象，是可以像 Map 一样存取数据的对象，叫域对象。
这里的域指的是存取数据的操作范围，整个 web 工程。

| 对象   | 存           | 取           | 删              |
| ------ | ------------ | ------------ | --------------- |
| Map    | put          | get          | remove          |
| 域对象 | setAttribute | getAttribute | removeAttribute |

ServletContext类的四个作用

1 获取web.xml中配置的上下文参数context-param

2 获取当前的工程路径,格式:/工程路径

3 获取工程的绝对路径

4 存储数据

1 获取web.xml中配置的上下文参数context-param

```xml
<!--context-param 是上下文参数 ( 它属于整个 web 工程 )-->
<context-param>
    <param-name>username</param-name>
    <param-value>context</param-value>
</context-param>
<!--context-param 是上下文参数 ( 它属于整个 web 工程 )-->
<context-param>
    <param-name>password</param-name>
    <param-value>root</param-value>
</context-param>
```



```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
ServletException, IOException {
    
// 1 、获取 web.xml 中配置的上下文参数 context-param
ServletContext context = getServletConfig().getServletContext();
    
String username = context.getInitParameter("username");
System.out.println("context-param 参数 username 的值是:" + username);
System.out.println("context-param 参数 password 的值是:" +
context.getInitParameter("password"));
    
// 2 、获取当前的工程路径，格式 : / 工程路径
System.out.println( " 当前工程路径:" + context.getContextPath() );
    
// 3 、获取工程部署后在服务器硬盘上的绝对路径
/**
* / 斜杠被服务器解析地址为 :http://ip:port/ 工程名 / 映射到 IDEA 代码的 web 目录 <br/>
*/
System.out.println(" 工程部署的路径是:" + context.getRealPath("/"));
System.out.println(" 工程下 css 目录的绝对路径是:" + context.getRealPath("/css"));
System.out.println(" 工程下 imgs 目录 1.jpg 的绝对路径是:" + context.getRealPath("/imgs/1.jpg"));
}
```

#### 常用的响应码

```
200 请求成功
302 请求重定向
404 页面找不到
500 服务器错误
```

#### MIME类型

```
MIME 是HTTP协议中的数据类型
```

![image-20201213074040010](G:\note\image\image-20201213074040010.png)

#### 获取请求参数

HttpServletRequest 常用的方法

| getRequestURI      | 请求资源路径             |
| ------------------ | ------------------------ |
| getRequestURL      | 请求的统一资源路径       |
| getHeader          | 请求头                   |
| getParameter       | 请求参数(参数只有一个值) |
| getParameterValues | 获取请求参数(参数多个值) |
| getMethos          | 获取请求方式             |

```java
// 获取请求参数
    String username = req.getParameter("username");
    String password = req.getParameter("password");
    String[] hobby = req.getParameterValues("hobby");
    System.out.println(" 用户名：" + username);
    System.out.println(" 密码：" + password);
    System.out.println(" 兴趣爱好：" + Arrays.asList(hobby));
```

#### 请求乱码

```
// 设置请求体的字符集为 UTF-8,在获取参数前使用
req.setCharacterEncoding("UTF-8");
```

#### 请求转发

![image-20201213085714442](G:\note\image\image-20201213085714442.png)

Servlet1

```java
// 获取请求的参数（办事的材料）查看
String username = req.getParameter("username");
System.out.println("在 在 Servlet1 （柜台 1 ）中查看参数（材料）：" + username);
// 给材料 盖一个章，并传递到 Servlet2 （柜台 2 ）去查看
req.setAttribute("key1"," 柜台 1 的章");
// 问路： Servlet2 （柜台 2 ）怎么走
/**
* 请求转发必须要以斜杠打头， / 斜杠表示地址为： http://ip:port/ 工程名 / , 映射到 IDEA 代码的 web 目录
<br/>
*
*/
RequestDispatcher requestDispatcher = req.getRequestDispatcher("/servlet2");
// RequestDispatcher requestDispatcher = req.getRequestDispatcher("http://www.baidu.com");
// 走向 Sevlet2 （柜台 2 ）
requestDispatcher.forward(req,resp);
```

Servlet2

```java
// 获取请求的参数（办事的材料）查看
String username = req.getParameter("username");
System.out.println("在 在 Servlet2 （柜台 2 ）中查看参数（材料）：" + username);
// 查看 柜台 1 是否有盖章
Object key1 = req.getAttribute("key1");
System.out.println(" 柜台 1 是否有章：" + key1);
// 处理自己的业务
System.out.println("Servlet2 务 处理自己的业务 ");
```

#### base标签的作用

```html
<!DOCTYPE html>
<html lang="zh_CN">
<head>
<meta charset="UTF-8">
<title>Title</title>
<!--base 标签设置页面相对路径工作时参照的地址
href 属性就是参数的地址值
-->
<base href="http://localhost:8080/07_servlet/a/b/">
</head>
<body>
这是 a 下的 b 下的 c.html 页面<br/>
<a href="../../index.html">跳回首页</a><br/>
</body>
</html>
```

#### HttpServletResponse

```
返回请求数据使用两个流(只能同时使用一个)
字节流 getOutputStream();  常用于下载(传递二进制数据)
字符路 getWriter();  常用语回传字符串

```

#### 响应乱码

解决响应中文乱码方案一（不推荐使用）：

```
// 设置服务器字符集为 UTF-8
resp.setCharacterEncoding("UTF-8");
// 通过响应头，设置浏览器也使用 UTF-8 字符集
resp.setHeader("Content-Type", "text/html; charset=UTF-8");


```

解决响应中文乱码方案二（推荐）：

```
// 它会同时设置服务器和客户端都使用 UTF-8 字符集，还设置了响应头
// 此方法一定要在获取流对象之前调用才有效
resp.setContentType("text/html; charset=UTF-8");
```

#### 请求重定向

方法一

```java
 servlet 01
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           //设置响应头
            resp.setStatus(302);
            //通过响应头设置新地址
            resp.setHeader("Location","http://localhost:8081/servlet02");
        }

servlet 02
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("cs2");
    }
```

方法一

```java
resp.sendRedirect("http://localhost:8081/servlet02");
```

#### javaEE的三层架构

![image-20201213103249713](G:\note\image\image-20201213103249713.png)

#### 连接数据库流程

第一 创建项目 文件夹

![image-20201213110650415](G:\note\image\image-20201213110650415.png)

```
web 层 com.atguigu.web/servlet/controller
service 层 com.atguigu.service Service 接口包
com.atguigu.service.impl Service 接口实现类
dao 持久层 com.atguigu.dao Dao 接口包
com.atguigu.dao.impl Dao 接口实现类
实体 bean 对象 com.atguigu.pojo/entity/domain/bean JavaBean 类
测试包 com.atguigu.test/junit
工具类 com.atguigu.utils
```

第二 编写数据库表对应的 JavaBean 对象。

```java
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
```

第三 创建表对应

```mysql
drop database if exists book;
create database book;
use book;
create table t_user(
`id` int primary key auto_increment,
`username` varchar(20) not null unique,
`password` varchar(32) not null,
`email` varchar(200)
);
insert into t_user(`username`,`password`,`email`) values('admin','admin','admin@atguigu.com');
select * from t_user;
```

第四 创建JdbcUtils工具类

<img src="G:\note\image\image-20201213121858083.png" alt="image-20201213121858083" style="zoom:50%;" />

导入Jar包

```
druid-1.1.9.jar
mysql-connector-java-5.1.7-bin.jar
以下是测试需要：
hamcrest-core-1.3.jar
junit-4.12.jar
```

第五  、在 src 源码目录下编写 jdbc.properties 属性配置文件：

```
username=root
password=root
url=jdbc:mysql://localhost:3306/book
driverClassName=com.mysql.jdbc.Driver
initialSize=5
maxActive=10
```

第六 编写 JdbcUtils 工具类：

```java

public class JdbcUtils {
    private static DruidDataSource dataSource;
static {
    try {
        Properties properties = new Properties();
        // 读取 jdbc.properties 属性配置文件
        InputStream inputStream =
        JdbcUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
        // 从流中加载数据
        properties.load(inputStream);
        // 创建 数据库连接 池
        dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
    } catch (Exception e) {
   	 e.printStackTrace();
    }
}
/**
* 获取数据库连接池中的连接
* @return 如果返回 null, 说明获取连接失败 <br/> 有值就是获取连接成功
*/
public static Connection getConnection(){
    Connection conn = null;
    try {
       conn = dataSource.getConnection();
    } catch (Exception e) {
       e.printStackTrace();
    }
    return conn;
}
/**
* 关闭连接，放回数据库连接池
* @param conn
*/
public static void close(Connection conn){
    if (conn != null) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
}
```

第六 JdbcUtils测试

```java
public class JdbcUtilsTest {
@Test
    public void testJdbcUtils(){
        for (int i = 0; i < 100; i++){
            Connection connection = JdbcUtils.getConnection();
            System.out.println(connection);
            JdbcUtils.close(connection);
        }
    }
}
```

#### 编写BaseDao

第一 引入DBUtils的jar包  commons-dbutils-1.3.jar

第二编写BaseDao:

```java
public abstract class BaseDao {
// 使用 DbUtils 操作数据库
private QueryRunner queryRunner = new QueryRunner();
/**
* update() 方法用来执行： Insert\Update\Delete 语句
*
* @return 如果返回 -1, 说明执行失败 <br/> 返回其他表示影响的行数
*/
public int update(String sql, Object... args) {
    Connection connection = JdbcUtils.getConnection();
    try {
       return queryRunner.update(connection, sql, args);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
       JdbcUtils.close(connection);
    }
     return -1;
}
/**
* 查询返回一个 javaBean 的 sql 语句
*
* @param type 返回的对象类型
* @param sql 执行的 sql 语句
* @param args sql 对应的参数值
* @param <T> 返回的类型的泛型
* @return
*/
public <T> T queryForOne(Class<T> type, String sql, Object... args) {
    Connection con = JdbcUtils.getConnection();
    try {
        return queryRunner.query(con, sql, new BeanHandler<T>(type), args);
    } catch (SQLException e) {
         e.printStackTrace();
    } finally {
         JdbcUtils.close(con);
    }
    return null;
}
/**
* 查询返回多个 javaBean 的 sql 语句
*
* @param type 返回的对象类型
* @param sql 执行的 sql 语句
* @param args sql 对应的参数值
* @param <T> 返回的类型的泛型
* @return
*/
public <T> List<T> queryForList(Class<T> type, String sql, Object... args) {
    Connection con = JdbcUtils.getConnection();
    try {
        return queryRunner.query(con, sql, new BeanListHandler<T>(type), args);
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        JdbcUtils.close(con);
    }
    return null;
}
/**
* 执行返回一行一列的 sql 语句
* @param sql 执行的 sql 语句
* @param args sql 对应的参数值
* @return
*/
public Object queryForSingleValue(String sql, Object... args){
    Connection conn = JdbcUtils.getConnection();
    try {
        return queryRunner.query(conn, sql, new ScalarHandler(), args);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        JdbcUtils.close(conn);
    }
    return null;
}
}
```

#### 编写UserDao测试

第一 UserDao 接口(确定业务)

```java
public interface UserDao {
    /**
* 根据用户名查询用户信息
* @param username 用户名
* @return 如果返回 null, 说明没有这个用户。反之亦然
*/
    public User queryUserByUsername(String username);
    /**
* 根据 用户名和密码查询用户信息
* @param username
* @param password
* @return 如果返回 null, 说明用户名或密码错误 , 反之亦然
*/
    public User queryUserByUsernameAndPassword(String username,String password);
    /**
* 保存用户信息
* @param user
* @return 返回 -1 表示操作失败，其他是 sql 语句影响的行数
*/
    public int saveUser(User user);
}
```

第二 UserDaoImpl 实现类：(实现业务方法)

```java
public class UserDaoImpl extends BaseDao implements UserDao {
    @Override
    public User queryUserByUsername(String username) {
        String sql = "select `id`,`username`,`password`,`email` from t_user where username = ?";
        return queryForOne(User.class, sql, username);
    }
    @Override
    public User queryUserByUsernameAndPassword(String username, String password) {
        String sql = "select `id`,`username`,`password`,`email` from t_user where username = ? and
            password = ?";
            return queryForOne(User.class, sql, username,password);
    }
    @Override
    public int saveUser(User user) {
        String sql = "insert into t_user(`username`,`password`,`email`) values(?,?,?)";
        return update(sql, user.getUsername(),user.getPassword(),user.getEmail());
    }
}
```

第三 UserDao 测试

```java
public class UserDaoTest {
    UserDao userDao = new UserDaoImpl();
    @Test
    public void queryUserByUsername() {
        if (userDao.queryUserByUsername("admin1234") == null ){
            System.out.println(" 用户名可用！");
        } else {
            System.out.println(" 用户名已存在！");
        }
    }
    @Test
    public void queryUserByUsernameAndPassword() {
        if ( userDao.queryUserByUsernameAndPassword("admin","admin1234") == null) {
            System.out.println(" 用户名或密码错误，登录失败");
        } else {
            System.out.println(" 查询成功");
        }
    }
    @Test
    public void saveUser() {
        System.out.println( userDao.saveUser(new User(null,"wzg168", "123456", "wzg168@qq.com")) );
    }
}
```

#### 编写UserService和测试

第一 UserService 接口

```java
public interface UserService {
    /**
* 注册用户
* @param user
*/
    public void registUser(User user);
    /**
* 登录
* @param user
* @return 如果返回 null ，说明登录失败，返回有值，是登录成功
*/
    public User login(User user);
    /**
* 检查 用户名是否可用
* @param username
* @return 返回 true 表示用户名已存在，返回 false 表示用户名可用
*/
    public boolean existsUsername(String username);
}
```

第二 UserServiceImpl 实现类

```java
public class UserServiceTest {
    UserService userService = new UserServiceImpl();
    @Test
    public void registUser() {
        userService.registUser(new User(null, "bbj168", "666666", "bbj168@qq.com"));
        userService.registUser(new User(null, "abc168", "666666", "abc168@qq.com"));
    }
    @Test
    public void login() {
        System.out.println( userService.login(new User(null, "wzg168", "123456", null)) );
    }
    @Test
    public void existsUsername() {
        if (userService.existsUsername("wzg16888")) {
            System.out.println(" 用户名已存在！");
        } else {
            System.out.println(" 用户名可用！");
        }
    }
}
```

第三 UserService 测试：

```java
public class UserServiceTest {
    UserService userService = new UserServiceImpl();
    @Test
    public void registUser() {
        userService.registUser(new User(null, "bbj168", "666666", "bbj168@qq.com"));
        userService.registUser(new User(null, "abc168", "666666", "abc168@qq.com"));
    }
    @Test
    public void login() {
        System.out.println( userService.login(new User(null, "wzg168", "123456", null)) );
    }
    @Test
    public void existsUsername() {
        if (userService.existsUsername("wzg16888")) {
            System.out.println(" 用户名已存在！");
        } else {
            System.out.println(" 用户名可用！");
        }
    }
}
```



##### jsp 
```
jsp  java server pages
用于页面的响应

jsp文件内容声明内容 可以整合到生成的类中
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: gg
  Date: 2020/10/14
  Time: 22:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
  <%--1 声明类属性--%>
<%!
private Integer id;
private String name;
%>

<%--声明statice代码块--%>
<%!
static {
    map = new HashMap<String,Object>();
    map.put("key1","value1");
    map.put("key2","value3");
}
%>
  <%--声明方法--%>
<%!
public int abd(){
    return 12;
}
%>
<%--声明内部类--%>
 <%!
     public static  class sum{
         private String name;
 }
 %>
</body>
</html>

表达式脚本(常用)
 表达式脚本格式<%=表达式%>

 作用  在jsp页面上输出数据

 特点
 1 所有表达式脚本都会被翻译到_jspService()方法中
 2 表达式都会被翻译为out.print()输出到页面上
3 、由于表达式脚本翻译的内容都在_jspService() 方法中,所以_jspService()方法中的对象都可以直接使用。
4、表达式脚本中的表达式不能以分号结束。


1输出
<%=12 %>
<%=12.21 %>
<%="输出字符串" %>
<%=map %>
<%=request.getParameter("username") %>

代码脚本

<%
 java语句
%>


<%--1. 代码脚本 ----if 语句 --%>
<%
int i = 13 ;
if (i == 12) {
%>
<h1>国哥好帅</h1>
<%
} else {
%>
<h1>国哥又骗人了！</h1>
<%
}
%>
```
#### jsp 九大内置对象
jsp 中的内置对象，是指 Tomcat 在翻译 jsp 页面成为 Servlet 源代码后，内部提供的九大对象，叫内置对象。
![image](190A0F31A4834278B9FEBB205E3FE415)
#### jsp 四大域对象
四个域对象分别是：
pageContext (PageContextImpl 类) 当前 jsp 页面范围内有效
request (HttpServletRequest 类)、 一次请求内有效
session (HttpSession 类)、 一个会话范围内有效（打开浏览器访问服务器，直到关闭浏览器）
application (ServletContext 类) 整个 web 工程范围内都有效（只要 web 工程不停止，数据都在）
域对象是可以像 Map 一样存取数据的对象。四个域对象功能一样。不同的是它们对数据的存取范围。
虽然四个域对象都可以存取数据。在使用上它们是有优先顺序的。
四个域在使用的时候，优先顺序分别是，他们从小到大的范围的顺序。
pageContext ====>>> request ====>>> session ====>>> application
```
<body>
<h1>scope.jsp 页面</h1>
<%
// 往四个域中都分别保存了数据
pageContext.setAttribute("key", "pageContext");
request.setAttribute("key", "request");
session.setAttribute("key", "session");
application.setAttribute("key", "application");
%>
pageContext 域是否有值：<%=pageContext.getAttribute("key")%> <br>
request 域是否有值：<%=request.getAttribute("key")%> <br>
session 域是否有值：<%=session.getAttribute("key")%> <br>
application 域是否有值：<%=application.getAttribute("key")%> <br>
<%
request.getRequestDispatcher("/scope2.jsp").forward(request,response);
%>
</body>
```
#### write和print输出
```
out.write() 输出字符串没有问题
out.print() 输出任意数据都没有问题（都转换成为字符串后调用的 write 输出）
```
#### jsp常用标签
```
静态包含
<%@ include file=""%>
<%@ include file="/include/footer.jsp"%>

动态包含
<jsp:include page=""></jsp:include>

<jsp:include page="/include/footer.jsp">
    <jsp:param name="username" value="bbj"/>
    <jsp:param name="password" value="root"/>
</jsp:include>

转发
<jsp:forward page=""></jsp:forward>

<jsp:forward page="/scope2.jsp"></js[:forward>

```
#### ServletContextListener监听器
```
 监听ServletContext 在tomcat启动时初始化 停止时销毁
 1 创建类 实现ServletContextListener接口
 public class ServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("初始化");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("销毁");
    }
}
2 web.xml中配置
<listener>
        <listener-class>com.atguigu.listener.ServletListener</listener-class>
    </listener>
3 重启服务器    
```
#### EL表达式
```
EL表达式用来代替jsp表示脚本 在页面输出数据
<%
request.setAttribute("key"," 值");
%>
表达式脚本输出 key 的值是：
<%=request.getAttribute("key1")==null?"":request.getAttribute("key1")%><br/>
EL 表达式输出 key 的值是：${key1}

当四个域都有相同key时按从小到大进行搜索
pageContext->request->session->pageContext
<body>
<%
// 往四个域中都保存了相同的 key 的数据。
request.setAttribute("key", "request");
session.setAttribute("key", "session");
application.setAttribute("key", "application");
pageContext.setAttribute("key", "pageContext");
%>
${ key }

El表达式输出各种属性
<%
Person person = new Person();
person.setName(" 国哥好帅！");
person.setPhones(new String[]{"18610541354","18688886666","18699998888"});
List<String> cities = new ArrayList<String>();
cities.add(" 北京");
cities.add(" 上海");
cities.add(" 深圳");
person.setCities(cities);
Map<String,Object>map = new HashMap<>();
map.put("key1","value1");
map.put("key2","value2");
map.put("key3","value3");
person.setMap(map);
pageContext.setAttribute("p", person);
%>
输出 Person：${ p }<br/>
输出 Person 的 name 属性：${p.name} <br>
输出 Person 的 pnones 数组属性值：${p.phones[2]} <br>
输出 Person 的 cities 集合中的元素值：${p.cities} <br>
输出 Person 的 List 集合中个别元素值：${p.cities[2]} <br>
输出 Person 的 Map 集合: ${p.map} <br>
输出 Person 的 Map 集合中某个 key 的值: ${p.map.key3} <br>
输出 Person 的 age 属性：${p.age} <br>
```
#### EL 表达式 表达式 —— 运算
![image](7AF38EB519254624800BF289897F7959)
![image](1F2732E772E24168AF12BD9D05CC85B4)
![image](B518F7B378494FE9BD2A88ADFED91BC3)

![image](C1A5CE27EDA54A96A59589A9E5A9BFED)
![image](A887288D4DB749A5954DEF3B3AF471E6)
EL 表达式11个隐藏对象
![image](C7EAB261286443E0A96521674FFF9CF5)

![image](567AF88D8297429FA8F2CB88D9C72DF1)
![image](260F0275E55E4969933A6659083BF811)
#### JSTL 标签库
jstl代替代码脚本
![image](9BC97D6FE46B4C01B3AA70D3C41D9054)
```
在 jsp 标签库中使用 taglib 指令引入标签库
CORE 标签库
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
XML 标签库
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
FMT 标签库
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
SQL 标签库
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
FUNCTIONS 标签库
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
```
使用步骤
```
1、先导入 jstl 标签库的 jar 包。
taglibs-standard-impl-1.2.1.jar
taglibs-standard-spec-1.2.1.jar
2、第二步，使用 taglib 指令引入标签库。
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```
core核心库的使用
```
<c:set />
作用： set 标签可以往域中保存数据
域对象 .setAttribute(key,value);
scope 属性设置保存到哪个域
page 表示 PageContext 域（默认值）
request 表示 Request 域
session 表示 Session 域
application 表示 ServletContext 域
var 属性设置 key 是多少
value 属性设置值
--%>
保存之前：${ sessionScope.abc } <br>
<c:set scope="session" var="abc" value="abcValue"/>
保存之后：${ sessionScope.abc } <br>


 <c:choose> <c:when> <c:otherwise>
 <%
request.setAttribute("height", 180);
%>
<c:choose>
<%-- 这是 html 注释 --%>
<c:when test="${ requestScope.height > 190 }">
<h2>小巨人</h2>
</c:when>
<c:when test="${ requestScope.height > 180 }">
<h2>很高</h2>
</c:when>
<c:when test="${ requestScope.height > 170 }">
<h2>还可以</h2>
</c:when>
<c:otherwise>
<c:choose>
<c:when test="${requestScope.height > 160}">
<h3>大于 160</h3>
</c:when>
<c:when test="${requestScope.height > 150}">
<h3>大于 150</h3>
</c:when>
<c:when test="${requestScope.height > 140}">
<h3>大于 140</h3>
</c:when>
<c:otherwise>
其他小于 140
</c:otherwise>
</c:choose>
</c:otherwise>
</c:choose>


 <c:forEach />
 <table border="1">
<c:forEach begin="1" end="10" var="i">
<tr>
<td>第${i}行</td>
</tr>
</c:forEach>
</table>

 遍历 Map 集合
 <%
Map<String,Object> map = new HashMap<String, Object>();
map.put("key1", "value1");
map.put("key2", "value2");
map.put("key3", "value3");
// for ( Map.Entry<String,Object> entry : map.entrySet()) {
// }
request.setAttribute("map", map);
%>
<c:forEach items="${ requestScope.map }" var="entry">
<h1>${entry.key} = ${entry.value}</h1>
</c:forEach>

遍历学生对象

<%--
items 表示遍历的集合
var 表示遍历到的数据
begin 表示遍历的开始索引值
end 表示结束的索引值
step 属性表示遍历的步长值
varStatus 属性表示当前遍历到的数据的状态
for （ int i = 1; i < 10; i+=2 ）
--%>

<c:forEach begin="2" end="7" step="2" varStatus="status" items="${requestScope.stus}" var="stu">
<tr>
<td>${stu.id}</td>
<td>${stu.username}</td>
<td>${stu.password}</td>
<td>${stu.age}</td>
<td>${stu.phone}</td>
<td>${status.step}</td>
</tr>
</c:forEach>
</table>
```
#### 文件上传

错误一 java.lang.NoClassDefFoundError: org/apache/commons/fileupload/FileItemFactory

```
运行servler报错

java.lang.NoClassDefFoundError: org/apache/commons/fileupload/FileItemFactory

或

java.lang.ClassNotFoundException: org.apache.commons.fileupload.FileItemFactory

程序编译通过但是报这个错，网上搜了一下，大部分说是包未正确导入，我的包是导入好的

原因：

(1) 在 \.metadata\.me_tcat7\webapps\baokaov2\WEB-INF\lib 下面 缺少这个包commons-fileupload-1.3.3.jar
(2) tomcat的lib下面缺少这个包commons-fileupload-1.3.3.jar
```



commons-fileupload.jar 赖 需要依赖 commons-io.jar 这个包，所以两个包我们都要引入。
个 第一步，就是需要导入两个 jar 包：
commons-fileupload-1.2.1.jar
commons-io-1.4.jar
commons-fileupload.jar 和 和 commons-io.jar 包中，我们常用的类有哪些？
ServletFileUpload 类，用于解析上传的数据。
FileItem 类，表示每一个表单项。

```
boolean ServletFileUpload.isMultipartContent(HttpServletRequest request);
判断当前上传的数据格式是否是多段的格式。
```

```
public List<FileItem> parseRequest(HttpServletRequest request)
解析上传的数据
```

```
boolean FileItem.isFormField()
判断当前这个表单项，是否是普通的表单项。还是上传的文件类型。
```

```
true 表示普通类型的表单项
false 表示上传的文件类型
```

```
String FileItem.getFieldName()
获取表单项的 name 属性值
```

```
void FileItem.write(file)   //写入上传文件
```

```
String FileItem.getName();   //获取上传文件的名字
```

```
String FileItem.getString()   获取当前表单相的值
```



```java
package com.atguigu.web;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class UploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write("你好");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //判断上传的数据是否是多段数据(只有多段数据才是文件上传的)
        resp.setContentType("text/html;charset=utf-8");
        if(ServletFileUpload.isMultipartContent(req)){
            //创建FileItemFactory工厂实现类
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            //创建用于解析上传数据的工具类ServleFileItemFactory
            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
            try {
                //解析上传的数据 得到每一个表单相FileItem
                List<FileItem> list = servletFileUpload.parseRequest(req);
                // 循环判断，每一个表单相，是普通型还是上传的文件
                for(FileItem fileItem:list){
                    if(fileItem.isFormField()){
                        //普通表单
                        System.out.println("name"+fileItem.getFieldName());
                        // 参数UTF-8解决乱码问题
                        System.out.println("value"+ fileItem.getString("UTF-8"));
                    }else{
                        //上传的文件
                        System.out.println("name"+fileItem.getFieldName());
                        //上传文件名
                        System.out.println("文件名"+fileItem.getName());
                        //写入本地
                        fileItem.write(new File("/static/img/"+fileItem.getName()));
                    }
                }
                resp.getWriter().write("你好");
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}

```
#### 文件下载
```java
package com.atguigu.servlet;

import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

public class Download extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        1、获取要下载的文件名
        String downloadFileName = "2.jpg";
//        2、读取要下载的文件内容 (通过ServletContext对象可以读取)
        ServletContext servletContext = getServletContext();
        // 获取要下载的文件类型
        String mimeType = servletContext.getMimeType("/file/" + downloadFileName);
        System.out.println("下载的文件类型：" + mimeType);
//        4、在回传前，通过响应头告诉客户端返回的数据类型
        resp.setContentType(mimeType);
//        5、还要告诉客户端收到的数据是用于下载使用（还是使用响应头）
        // Content-Disposition响应头，表示收到的数据怎么处理
        // attachment表示附件，表示下载使用
        // filename= 表示指定下载的文件名
        // url编码是把汉字转换成为%xx%xx的格式
        if (req.getHeader("User-Agent").contains("Firefox")) {
            // 如果是火狐浏览器使用Base64编码
            resp.setHeader("Content-Disposition", "attachment; filename==?UTF-8?B?" + new BASE64Encoder().encode("中国.jpg".getBytes("UTF-8")) + "?=");
        } else {
            // 如果不是火狐，是IE或谷歌，使用URL编码操作
            resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("中国.jpg", "UTF-8"));
        }
        /**
         * /斜杠被服务器解析表示地址为http://ip:prot/工程名/  映射 到代码的Web目录
         */
        InputStream resourceAsStream = servletContext.getResourceAsStream("/file/" + downloadFileName);
        // 获取响应的输出流
        OutputStream outputStream = resp.getOutputStream();
        //        3、把下载的文件内容回传给客户端
        // 读取输入流中全部的数据，复制给输出流，输出给客户端
        IOUtils.copy(resourceAsStream, outputStream);
    }
}

```
#### 使用反射优化方法
```
 String action="login";
    try {
        //生成login方法
        Method method = this.getClass().getDeclaredMethod(action,HttpServletRequest.class,HttpServletResponse.class)
        // 调用方法
        method.invoke(this,req,resp);
    } catch (Exception e) {
        e.printStackTrace();
    }
```
#### 抽取BaseServlet程序
```
public abstract class BaseServlet extends HttpServlet {
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
IOException {
String action = req.getParameter("action");
try {
// 获取 action 业务鉴别字符串，获取相应的业务 方法反射对象
Method method = this.getClass().getDeclaredMethod(action, HttpServletRequest.class,
HttpServletResponse.class);
// System.out.println(method);
// 调用目标业务 方法
method.invoke(this, req, resp);
} catch (Exception e) {
e.printStackTrace();
}
}
}
```
修改 UserServlet 程序继承 BaseServlet 程序。
![image](814DBA7BFD9A414BB6BB7C206F558BC5)

#### 数据封装和抽取BeanUtils的使用
```
BeanUtils工具类，一次性把所有请求参数注入到JavBean中。
使用时需要导入包
commons-beanutils-1.8.0.jar
commons-logging-1.1.1.jar

编写工具类
public class WebUtils {
   public static <T> T CopyParamToBean(Map value, T bean){
      try {
         BeanUtils.populate(bean,value);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return bean;
   }


调用工具类
 System.out.println("start");
    User user= WebUtils.CopyParamToBean(req.getParameterMap(),new User());
    resp.getWriter().write(user.toString());
```

#### Cookie

```java
1 创建cookie
    Cookie cookie = new Cookie("key1","value1");
response.addCookie(cookie);

2 获取cookie
    final Cookie[] cookies = request.getCookies();
for(Cookie c:cookies){
    if("key1".equals(c.getName())){
        System.out.println("找到了"+c.getValue());
    }
    System.out.println("name"+c.getName());
    System.out.println("value"+c.getValue());
}

3
    setMaxAge()
    * 正数 多少秒后 过期
    * 负数  浏览器一关 Cookit就会删除
    * 0  马上删除
    */
    final Cookie cookie = new Cookie("key2", "value2");
     cookie.setMaxAge(10);
     response.addCookie(cookie);
```

#### Session

1、Session 就一个接口（HttpSession）。
2、Session 就是会话。它是用来维护一个客户端和服务器之间关联的一种技术。
3、每个客户端都有自己的一个 Session 会话。
4、Session 会话中，我们经常用来保存用户登录之后的信息。

 如何创建 Session 和获取

```java
request.getSession()
第一次调用是：创建 Session 会话
之后调用都是：获取前面创建好的 Session 会话对象。
isNew(); 判断到底是不是刚创建出来的（新的）
true 表示刚创建
false 表示获取之前创建
每个会话都有一个身份证号。也就是 ID 值。而且这个 ID 是唯一的。
getId() 得到 Session 的会话 id 值。
    
    
req.getSession().setAttribute("key1", "value1");
Object attribute = req.getSession().getAttribute("key1");

Session 生命周期控制
    req.getSession().getMaxInactiveInterval();

    req.getSession().setMaxInactiveInterval(1800);
    Session 默认的超时时间长为 30 分钟。
    因为在Tomcat服务器的配置文件web.xml中默认有以下的配置，它就表示配置了当前Tomcat服务器下所有的Session
    超时配置默认时长为：30 分钟。
    <session-config>
    <session-timeout>30</session-timeout>
    </session-config>
```

#### Filter过滤器

Filter 过滤器它的作用是： 拦截请求，过滤响应。

Filter 过滤器的使用步骤：
1、编写一个类去实现 Filter 接口
2、实现过滤方法 doFilter()
3、到 web.xml 中去配置 Filter 的拦截路径

1. 创建filter

```java
public class AdminFilter implements Filter {
    /**
* doFilter 方法，专门用于拦截请求。可以做权限检查
*/
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain
                         filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpServletRequest.getSession();
        Object user = session.getAttribute("user");
        // 如果等于 null ，说明还没有登录
        if (user == null) {
            servletRequest.getRequestDispatcher("/login.jsp").forward(servletRequest,servletResponse);
            return;
        } else {
            // 让程序继续往下访问用户的目标资源
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
}
```

配置web.xml 中的配置：

```xml
<!--filter 标签用于配置一个 Filter 过滤器 -->
<filter>
    <!-- 给 filter 起一个别名 -->
    <filter-name>AdminFilter</filter-name>
    <!-- 配置 filter 的全类名 -->
    <filter-class>com.atguigu.filter.AdminFilter</filter-class>
</filter>
<!--filter-mapping 配置 Filter 过滤器的拦截路径 -->
<filter-mapping>
    <!--filter-name 表示当前的拦截路径给哪个 filter 使用 -->
    <filter-name>AdminFilter</filter-name>
    <!--url-pattern 配置拦截路径
/ 表示请求地址为： http://ip:port/ 工程路径 / 映射到 IDEA 的 web 目录
/admin/* 表示请求地址为： http://ip:port/ 工程路径 /admin/*
-->
    <url-pattern>/admin/*</url-pattern>
</filter-mapping>
```

2. 生命周期

   ```
   Filter 的生命周期包含几个方法
   1、构造器方法
   2、init 初始化方法
   第 1，2 步，在 web 工程启动的时候执行（Filter 已经创建）
   3、doFilter 过滤方法
   第 3 步，每次拦截到请求，就会执行
   4、destroy 销毁
   第 4 步，停止 web 工程的时候，就会执行（停止 web 工程，也会销毁 Filter 过滤器）
   ```

   FilterConfig类

   FilterConfig 类见名知义，它是 Filter 过滤器的配置文件类。
   Tomcat 每次创建 Filter 的时候，也会同时创建一个 FilterConfig 类，这里包含了 Filter 配置文件的配置信息。
   FilterConfig 类的作用是获取 filter 过滤器的配置内容
   1、获取 Filter 的名称 filter-name 的内容
   2、获取在 Filter 中配置的 init-param 初始化参数
   3、获取 ServletContext 对象

   ```java
   @Override
   public void init(FilterConfig filterConfig) throws ServletException {
       System.out.println("2.Filter 的 的 init(FilterConfig filterConfig) 初始化");
       // 1 、获取 Filter 的名称 filter-name 的内容
       System.out.println("filter-name 的值是：" + filterConfig.getFilterName());
       // 2 、获取在 web.xml 中配置的 init-param 初始化参数
       System.out.println(" 初始化参数 username 的值是 ：" + filterConfig.getInitParameter("username"));
       System.out.println(" 初始化参数 url 的值是：" + filterConfig.getInitParameter("url"));
       // 3 、获取 ServletContext 对象
       System.out.println(filterConfig.getServletContext());
   }
   ```

   web.xml 配置

   ```xml
   <!--filter 标签用于配置一个 Filter 过滤器 -->
   <filter>
       <!-- 给 filter 起一个别名 -->
       <filter-name>AdminFilter</filter-name>
       <!-- 配置 filter 的全类名 -->
       <filter-class>com.atguigu.filter.AdminFilter</filter-class>
       <init-param>
           <param-name>username</param-name>
           <param-value>root</param-value>
       </init-param>
       <init-param>
           <param-name>url</param-name>
           <param-value>jdbc:mysql://localhost3306/test</param-value>
       </init-param>
   </filter>
   ```

   FilterChain 过滤器链

   

![image-20201216155640526](G:\note\image\image-20201216155640526.png)

Filter 的拦截路径

```xml
--精确匹配 精确匹配
<url-pattern>/target.jsp</url-pattern>
以上配置的路径，表示请求地址必须为：http://ip:port/工程路径/target.jsp
--目录匹配 目录匹配
<url-pattern>/admin/*</url-pattern>
以上配置的路径，表示请求地址必须为：http://ip:port/工程路径/admin/*
--后缀名匹配 后缀名匹配
<url-pattern>*.html</url-pattern>
以上配置的路径，表示请求地址必须以.html 结尾才会拦截到
<url-pattern>*.do</url-pattern>
以上配置的路径，表示请求地址必须以.do 结尾才会拦截到
<url-pattern>*.action</url-pattern>
以上配置的路径，表示请求地址必须以.action 结尾才会拦截到
Filter 过滤器它只关心请求的地址是否匹配，不关心请求的资源是否存在！！
```

#### JSON

javaBean 和 和 json 的互转

```java
@Test
public void test1(){
    Person person = new Person(1," 国哥好帅!");
    // 创建 Gson 对象实例
    Gson gson = new Gson();
    // toJson 方法可以把 java 对象转换成为 json 字符串
    String personJsonString = gson.toJson(person);
    System.out.println(personJsonString);
    // fromJson 把 json 字符串转换回 Java 对象
    // 第一个参数是 json 字符串
    // 第二个参数是转换回去的 Java 对象类型
    Person person1 = gson.fromJson(personJsonString, Person.class);
    System.out.println(person1);
}
```

List 和 和 json 的互转

```java
@Test
public void test2() {
    List<Person> personList = new ArrayList<>();
    personList.add(new Person(1, " 国哥"));
    personList.add(new Person(2, " 康师傅"));
    Gson gson = new Gson();
    // 把 List 转换为 json 字符串
    String personListJsonString = gson.toJson(personList);
    System.out.println(personListJsonString);
    List<Person> list = gson.fromJson(personListJsonString, new PersonListType().getType());
    System.out.println(list);
    Person person = list.get(0);
    System.out.println(person);
}
```

map 和 和 json 的互转

```java
@Test
public void test3(){
    Map<Integer,Person> personMap = new HashMap<>();
    personMap.put(1, new Person(1, " 国哥好帅"));
    personMap.put(2, new Person(2, " 康师傅也好帅"));
    Gson gson = new Gson();
    // 把 map 集合转换成为 json 字符串
    String personMapJsonString = gson.toJson(personMap);
    System.out.println(personMapJsonString);
    // Map<Integer,Person> personMap2 = gson.fromJson(personMapJsonString, new
    PersonMapType().getType());
    Map<Integer,Person> personMap2 = gson.fromJson(personMapJsonString, new
                                                   TypeToken<HashMap<Integer,Person>>(){}.getType());
    System.out.println(personMap2);
    Person p = personMap2.get(1);
    System.out.println(p);
}
```

#### 拦截器

![image-20201222000942817](G:\note\image\image-20201222000942817.png)

![image-20201222002854926](G:\note\image\image-20201222002854926.png)

![image-20201222004020016](G:\note\image\image-20201222004020016.png)

#### 远程调用拦截器

![image-20210401074525214](G:\note\image\image-20210401074525214.png)

```java
@Configuration
public class GuliFeignConfig {
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // 1 ServletRequestAttributes 拿到刚进来的这个请求
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                // 获取老的氢气
                HttpServletRequest request = requestAttributes.getRequest();
                // 获取老请求的请求头
                String cookie = request.getHeader("Cookie");
                // 给新请求同步老请求的Cookie
                requestTemplate.header("Cookie",cookie);
            }
        };
    }
}
```

