#### dart

1 需要安装dart的SDK

2 vscode中安装插件 dart code runder

新建index.dart  右键 code runer

```
main() {
  print("你好");
}
```

#### 声明变量

```dart
变量声明
1 var 声明不带类型 可以自动推导
var str="你好";

2 变量类型声明
  String str="你好";

常量声明
const  声明时就要赋值 不能改变
const str="常量";


final 声明时可以不赋值 运行时再赋值
final str3 = new DateTime.now(); //运行时才赋值
```

#### 数据类型

######  整型

```
int = 100;
```

字符串

```
String name="小明";

多行字符串
String txt = """
第一行
第二行
""";

字符串拼接
String str1 = "$name $txt";
String str2 = name +""+txt;
```

###### 布尔

````
bool show = true;
````

###### 集合

list

```
List arr = ["小明",20];

指定集合类型
var arr1 = <String>["小明","小红"];

常用属性
length 长度
reversed 翻转
isEmpty 为空
isNotEmpty 不为空

常用方法
add()
addAll 拼接数组
indexOf 查找 //不存在 返回 -1
fillRange(startIndex,endIndex,value) 修改
remove 删除
removeAt  根据索引删除
insert(index,value);
insertAll(index,list)；
toList() //其他类型转List
join()  //list转字符串
split()  字符串转list
list.forEach
map
```

set

//也具有集合的方法

```
无序不重复
var s = new Set();
s.add()
```

###### Maps

```
Map obj = {"name":"小明","age":20};

obj["name"];
obj.keys()  获取所有key
obj.values() 获取所有value
obj.isEmpty()  为空
obj.isNotEmpty() 不为空
obj.addAll({"a":10,"b":20});
obj.containsValue("age") //true
obj.forEach((k,v) => print('${k}: ${v}'));
obj.map((key,value)=>{print("$key  $value");})
```

###### 类

```dart
class Person {
  String name = "小明";
  int age = 23;
  //构造函数
  Person(name, age) {
    this.name = name;
    this.age = age;
  }
  String getInfo() {
    return this.name;
  }
}

链式操作
    var p = new Person("小明", 20);
  p
    ..name = "小胖"
    ..age = 30
    ..getInfo();


继承
class NewPerson extends Person {
  NewPerson(name, age) : super(name, age);
}
```

抽象方法

```dart
abstract class Animal {
  late String name;
  say();
  eat() {
    print("抽象方法");
  }
}

class Dog extends Animal {
  @override
  say() {
    print("说话");
  }
}
```

接口

```
abstract class A {
  saya();
}

abstract class B {
  sayb();
}

class C implements A, B {
  @override
  saya() {
    // TODO: implement saya
    throw UnimplementedError();
  }

  @override
  sayb() {
    // TODO: implement sayb
    throw UnimplementedError();
  }
}
```

###### mixins

一个类继承多个类

```dart
class A {
  saya(){}
}

class B {
  sayb(){}
}

class C with A, B {}
```

###### 泛型

```
T getData<T>(T value) {
  return value;
}
```



###### 判断类型

```
//类型判断
var str = "123456";
if (str is String) {
print("string类型");
} else if (str is int) {
print("整数类型");
}
```

#### 运算

```
  int a = 13;
  int b = 5;
  a + b;
  a - b;
  a * b;
  a / b;  除
  a % b;   取余
  a ~/ b;  取整
  
  ??== 如果b不为空就不赋值 如果为空就赋值
  int b;
  b?? = 23;
  
  a+=10;
```

###### 三目运算

```
var name=true?"小明":"小红";
```

###### 类型转换

```
var num="20"；
 
int.parse(num)；  //装整型
double.parse("20.333")；  //double
var n = 100;
n.toString(); //字符串

//判断字符串是否为空
str.isEmpty
str==null
str.isNaN
```

#### 循环语句

for

```dart
for (int i = 0; i <= 10; i++) {
    print(i);
}
```

while

```
int i=1;
while(i<10){
  print(i);
  i++;
}
```

#### 函数

```
int getNum(){
var num = 123;
return num;
}

getNum();

String user(String name;int age;sex="男"){}
```

#### 创建项目

1 按照官网搭建环境
2 创建文件
flutter create myapp
cd myapp
3 打开模拟器后
flutter run

###### 颜色

```
Colors.yellow
Color.fromRGBO(244, 233, 121, 0.5)
```

第一个组件

```dart
import "package:flutter/material.dart";

void main() {
  runApp(new Center(child: MyApp()));
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    //MaterialApp根组件
    return MaterialApp(
      //进入app的主页面
      home: Scaffold(
        //顶部导航
        appBar: AppBar(title: Text("标题")),
        //内容区域
        body: Center(
          child: Text('你好flutter',
              textDirection: TextDirection.ltr, //字体左排
              style: TextStyle(
                  fontSize: 40.0, color: Color.fromRGBO(244, 233, 121, 0.5))),
        ),
      ),
      //主题颜色
      theme: ThemeData(primarySwatch: Colors.yellow),
    );
  }
}

```

#### 组件

###### Text

```dart
Text(
                  "你好",
                //左对齐
                textAlign: TextAlign.center,
                //从左排序
                textDirection: TextDirection.ltr,
                //溢出显示
                overflow: TextOverflow.clip,
                //最大行
                maxLines: 2,
                // 样式
                style: TextStyle(fontSize: 30),
              )
```

###### TextStyle

```dart
TextStyle(
                    // 字体大小
                    fontSize: 30,
                    //字体加粗
                    fontWeight: FontWeight.bold,
                    //颜色
                    color: Colors.blue,
                    // 文本行与行的高度，作为字体大小的倍数（取值1~2，如1.2）
                    height: 2,
                    // none 不显示装饰线条，underline 字体下方，overline 字体上方，lineThrough穿过文字
                    decoration: TextDecoration.none),
              )
```

###### 输入框

[(3条消息) Flutter TextField详解_yechaoa-CSDN博客_flutter textfield](https://blog.csdn.net/yechaoa/article/details/90906689)

```dart
class Demo extends StatefulWidget {
  const Demo({Key? key}) : super(key: key);

  @override
  _DemoState createState() => _DemoState();
}

class _DemoState extends State<Demo> {
  TextEditingController controller = TextEditingController();
  String _textStr = "默认";
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    controller = TextEditingController();
    //获取值
    controller.addListener(() {
      setState(() {
        _textStr = controller.text;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Text(this._textStr),
          TextField(
            //密码框
            // obscureText: true,
            controller: controller,
            maxLength: 2,
            maxLines: 2,
            onTap: () {},
            decoration: InputDecoration(
                icon: Icon(Icons.pedal_bike),
                hintText: "请输入内容",
                border: OutlineInputBorder()),
          ),
          SizedBox(
            height: 20,
          )
        ],
      ),
    );
  }
}

```

###### checkbox

```dart
class Demo extends StatefulWidget {
  const Demo({Key? key}) : super(key: key);

  @override
  _DemoState createState() => _DemoState();
}

class _DemoState extends State<Demo> {
  bool selected = false;
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Text("${this.selected}"),
          SizedBox(
            height: 20,
          ),
          Checkbox(
              value: this.selected,
              activeColor: Colors.amber,
              onChanged: (value) {
                setState(() {
                  this.selected = value!;
                });
              }),
          CheckboxListTile(
              value: this.selected,
              secondary: const Icon(Icons.alarm_on),
              title: const Text("标题一"),
              subtitle: Text("标题二"),
              onChanged: (val) {
                setState(() {
                  this.selected = val!;
                });
              })
        ],
      ),
    );
  }
}

```

###### radio

```dart

class Demo extends StatefulWidget {
  const Demo({Key? key}) : super(key: key);

  @override
  _DemoState createState() => _DemoState();
}

class _DemoState extends State<Demo> {
  int selected = 1;

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Text("${this.selected == 1 ? '男' : '女'}"),
          SizedBox(
            height: 20,
          ),
          Text("男"),
          Radio(
              value: 1,
              groupValue: this.selected,
              onChanged: (int? val) {
                print(val);
                setState(() {
                  this.selected = val!;
                });
              }),
          Text("女"),
          Radio(
              value: 2,
              groupValue: this.selected,
              onChanged: (int? val) {
                print(val);
                setState(() {
                  this.selected = val!;
                });
              }),
          RadioListTile(
            value: 1,
            onChanged: (int? value) {
              setState(() {
                this.selected = value!;
              });
            },
            groupValue: this.selected,
            title: Text("一级标题"),
            subtitle: Text("二级标题"),
            secondary: Icon(Icons.camera),
            selected: this.selected == 1,
          ),
          RadioListTile(
            value: 2,
            onChanged: (int? value) {
              setState(() {
                this.selected = value!;
              });
            },
            groupValue: this.selected,
            title: Text("一级标题"),
            subtitle: Text("二级标题"),
            secondary: Icon(Icons.palette),
            selected: this.selected == 2,
          ),
        ],
      ),
    );
  }
}
```

###### Switch

```dart
class Demo extends StatefulWidget {
  const Demo({Key? key}) : super(key: key);

  @override
  _DemoState createState() => _DemoState();
}

class _DemoState extends State<Demo> {
  late bool selected;

  @override
  void initState() {
    super.initState();
    selected = false;
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Text("${this.selected == 1 ? '男' : '女'}"),
          SizedBox(
            height: 20,
          ),
          Switch(
              value: this.selected,
              activeColor: Colors.red,
              activeTrackColor: Colors.green,
              activeThumbImage: NetworkImage(
                  "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3868620627,2694438302&fm=58"),
              onChanged: (val) {
                setState(() {
                  this.selected = val;
                });
              }),
          Text(
            "三：SwitchListTile",
            style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20),
          ),
          SizedBox(
            width: 200,
            child: SwitchListTile(
              // 是否选中 是否打开
              value: this.selected,
              // 当打开关闭的时候的回调
              onChanged: (val) {
                setState(() {
                  this.selected = val;
                });
              },
              // 选中时 滑块的颜色
              activeColor: Colors.red,
              // 选中时 滑道的颜色
              activeTrackColor: Colors.black,
              // 选中时 滑块的图片
//              activeThumbImage: AssetImage("images/hashiqi.jpg"),
              // 未选中时 滑块的颜色
              inactiveThumbColor: Colors.green,
              // 未选中时 滑道的颜色
              inactiveTrackColor: Colors.amberAccent,
              // 未选中时 滑块的颜色
              inactiveThumbImage: AssetImage("images/hashiqi.jpg"),
              // 标题
              title: Text("标题"),
              // 副标题 在标题下面的
//              subtitle: Text("副标题"),
              // 是不是三行， true 时： subtitle 不能为null， false时可以为 null
//              isThreeLine: true,
              // 如果为 true ，则 text 和 icon 都用 activeColor 时的color
//              selected: true,
              // 是否垂直密集居中
              dense: true,
              // 左边的一个东西
              secondary: Icon(Icons.access_time),
            ),
          ),
        ],
      ),
    );
  }
}

```



###### Container

```dart
Container(
    width: 100,
    height: 100,
    //文字居中
    alignment: Alignment.center,
    //外部填充
    margin: EdgeInsets.only(top: 30, left: 120),
    padding: EdgeInsets.only(bottom: 20, right: 30),
    //背景颜色
    // color: Colors.blue,
    decoration: BoxDecoration(
        color: Colors.yellow,
        //圆角
        borderRadius: BorderRadius.all(Radius.circular(8)),
        //边框颜色
        border: Border.all(color: Colors.pink, width: 2.0),
        //背景渐变
        gradient: RadialGradient(
            colors: [Colors.red, Colors.orange],
            center: Alignment.topLeft,
            radius: .98),
        //阴影颜色
        boxShadow: [
            BoxShadow(
                color: Colors.black45,
                offset: Offset(2.0, 2.0),
                blurRadius: 4.0)
        ]),
    child: Text("container"),
)
```

###### Padding

```dart
Padding(
    // padding: EdgeInsets.only(left: 5, right: 5, top: 5, bottom: 5),
    padding: EdgeInsets.all(10),
    child: Text("padding组件"),
));
```

###### Row

```dart
Row({
    Key key,
    MainAxisAlignment mainAxisAlignment = MainAxisAlignment.start,//将子Widget放置在什么位置
    MainAxisAlignment.start，从左边开始布局
        MainAxisAlignment.end，从右边开始布局
        MainAxisAlignment.center，从中间开始布局
        MainAxisAlignment.spaceBetween，相邻两个widget之间的距离相等
        MainAxisAlignment.spaceAround，子widget平均分配空间，最左最又的组件离边的边距，为两个widget边距的一半，具体请自行设置查看效果
        MainAxisAlignment.spaceEvenly，子widget平均分配空间，包括最左最右的widget离边的空间
        MainAxisSize mainAxisSize = MainAxisSize.max,//设置Row在主轴上应该占据多少空间
    CrossAxisAlignment crossAxisAlignment = CrossAxisAlignment.center,//子元素应该如何沿着横轴放置，默认中间对齐
    CrossAxisAlignment.satrt//设置子元素上边对齐
        CrossAxisAlignment.end//设置子元素下边对齐
        CrossAxisAlignment.stretch//每个子元素的上下对齐Row的上下边，相当于是拉伸操作
        CrossAxisAlignment.baseline,//相当于CrossAxisAlignment.start,但是需要配合textBaseline，不然会报错
    TextDirection textDirection,//设置子widget的左右显示方位，只有在crossAxisAlignment为start、end的时候起作用；
    VerticalDirection verticalDirection = VerticalDirection.down,//设置垂直方向上的方向，通常用于Column中,在Row中使用的话，会影响子widget是上边距对齐，还是下边距对齐，跟 CrossAxisAlignment.end， CrossAxisAlignment.start相互影响，选择使用
    TextBaseline textBaseline,//配合CrossAxisAlignment.baseline一起使用
    List<Widget> children = const <Widget>[],//存放一组子widget
}

    Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: <Widget>[
            Icon(Icons.opacity),
            Icon(Icons.settings),
            Container(
                color: Colors.redAccent,
                width: 100.0,
                height: 100.0,
                child: Text('data'),
            ),
            Icon(Icons.ondemand_video),
        ],
    )
```

###### Expanded

```dart
Expanded(
    flex: 2,
    child: Container(
        alignment: Alignment.center,
        height: 20,
        color: Colors.blue,
    ),
)
```

###### Image

```dart
Image(
    // image:AssetImage(),
    image: NetworkImage(
        "https://cdn2.jianshu.io/assets/default_avatar/9-cceda3cf5072bcdd77e8ca4f21c40998.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/120/h/120"), //: 加载网络图片
    width: 100.0,
    height: 100.0,
    alignment: Alignment.center, //对其方式
    // fit: BoxFit.contain, 将图片的内容完整居中显示，通过按比例缩小
    //      BoxFit.cover：按比例放大图片的
    //      BoxFit.fill：把图片不按比例放大/缩小到组件的大小显示
    //      BoxFit.scaleDown：如果图片宽高大于组件宽高，则让图片内容完全居中显示
    repeat: ImageRepeat.repeat,
    color: Color(0xFFFFFF00),
)
    
头像
    CircleAvatar(
    backgroundImage: NetworkImage(
        "https://cdn2.jianshu.io/assets/default_avatar/9-cceda3cf5072bcdd77e8ca4f21c40998.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/120/h/120"),
    radius: 30,

图片裁剪
    
    ClipRRect(
        child: Image.network(
            imgUrl,
            scale: 8.5,
            fit: BoxFit.cover,
        ),
        borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20),
            topRight: Radius.circular(20),
            bottomLeft: Radius.circular(20),
            bottomRight: Radius.circular(20),
        ),
    )    
```



###### ListTitle

![image-20210607134016352](G:\note\image\image-20210607134016352.png)

```dart
ListTile(
    //左侧icon
    leading: new Icon(Icons.phone),
    右侧icon
    trailing: new Icon(Icons.arrow_forward_ios),
    //内容区域
    contentPadding: EdgeInsets.symmetric(horizontal: 20.0),
    enabled: true,
    onTap: () => print("被点击了"),
    onLongPress: () => print("被长按了"),
    title: Text("标题"),
    subtitle: Text("二级标题"),
),
```

###### ListView

```dart
ListView(
    //水平
    scrollDirection: Axis.vertical,
    //是否倒序
    reverse: false,
    children: [
        ListTile(
            title: Text("标题"),
            subtitle: Text("二级标题"),
        ),
        ListTile(
            title: Text("标题"),
            subtitle: Text("二级标题"),
        ),
    ],
```

###### ListView.builder

```dart
ListView.builder(
    itemCount: 10,
    padding: EdgeInsets.all(10.0),
    itemBuilder: (context, index) {
        return ListTile(
            title: Text("第 $index 个"),
        );
    }));
```

###### ListView.separated

![image-20210607135148603](G:\note\image\image-20210607135148603.png)

```dart
ListView.separated(
        itemCount: 10,
        padding: EdgeInsets.all(10.0),
        itemBuilder: (context, index) {
          return ListTile(
            title: Text("第 $index 个"),
          );
        },
        separatorBuilder: (context, index) {
          return Divider(color: Colors.red);
        },
      ),
```

###### 循环创建列表

```dart
class ListComponent extends StatelessWidget {
  List<Widget> _getList() {
    List<Widget> list = [];
    for (int i = 0; i < 20; i++) {
      list.add(ListTile(title: Text("第 ${i + 1} 个列表")));
    }
    return list;
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: ListView(
        children: _getList(),
      ),
    );
  }
}
```

###### GridView.builder

```dart
GridView.builder(
    //子Item的个数
    itemCount: 20,
    //生成item
    itemBuilder: (context, index) {
        return ListTile(
            title: Text("第 ${index} 题"),
        );
    },
    //布局方式
    gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
        //主方向 间隔
        mainAxisSpacing: 12,
        // 副轴间隔
        crossAxisSpacing: 12,
        // item 宽高比
        childAspectRatio: 2.1,
        // 每列数
        crossAxisCount: 4),
),
```

###### GridView.count

```dart
class ListComponent extends StatelessWidget {
    List<Widget> _getList() {
        List<Widget> list = [];
        for (int i = 0; i < 20; i++) {
            list.add(ListTile(title: Text("第 ${i + 1} 个列表")));
        }
        return list;
    }

    @override
    Widget build(BuildContext context) {
        return Container(
            child: GridView.count(
                //水平子Widget之间间距
                crossAxisSpacing: 10.0,
                //垂直子Widget之间间距
                mainAxisSpacing: 30.0,
                //GridView内边距
                padding: EdgeInsets.all(10.0),
                //一行的Widget数量
                crossAxisCount: 2,
                //子Widget宽高比例
                childAspectRatio: 2.0,
                //子Widget列表
                children: _getList()));
    }
}

```

###### 自定义组件

```dart
class IconContainer extends StatelessWidget {
  double size = 30.0;
  Color color = Colors.red;
  IconData icon;
  IconContainer(this.size, this.color, this.icon);
  @override
  Widget build(BuildContext context) {
    return Container(
      height: 100.0,
      width: 100.0,
      color: Colors.red,
      child: Center(
        child: Icon(this.icon, color: this.color, size: this.size),
      ),
    );
  }
}

调用
IconContainer(20.0, Colors.black, Icons.download),
```

###### Stack

```dart
Stack(
    //Alignment(0,0)代表居中，Alignment(-1,-1)表示左上，Alignment(0,-1)表示中上，Alignment(1,-1)表示右上，Alignment(1,1)表示右下
    alignment: Alignment(0, 0),
    children: [
        Container(
            color: Colors.blue,
            width: 100.0,
            height: 100.0,
        ),
        Text("测试")
    ],
));
```

定位

```dart
Stack(
    //Alignment(0,0)代表居中，Alignment(-1,-1)表示左上，Alignment(0,-1)表示中上，Alignment(1,-1)表示右上，Alignment(1,1)表示右下
    alignment: Alignment(0, 0),
    children: [
        Container(
            color: Colors.blue,
            width: 200.0,
            height: 200.0,
        ),
        Text("测试"),
        Align(
            alignment: Alignment.topLeft,
            child: Icon(
                Icons.account_circle,
                size: 40,
                color: Colors.red,
            )),
        Positioned(
            left: 10,
            top: 10,
            width: 60,
            height: 60,
            child: Container(
                color: Colors.black,
            ),
        )
    ],
));
```

###### AspectRatio

```dart
AspectRatio(
    aspectRatio: 3 / 1,
    child: Container(
        color: Colors.red,
    ),
));
```

###### Card

```dart
Colors.blueAccent,
          //z轴的高度，设置card的阴影
          shadowColor: Colors.amber,
          elevation: 20.0,
          //设置shape，这里设置成了R角
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.all(Radius.circular(20.0)),
          ),
          //对Widget截取的行为，比如这里 Clip.antiAlias 指抗锯齿
          clipBehavior: Clip.antiAlias,
          semanticContainer: false,
          child: Container(
            color: Colors.deepPurpleAccent,
            width: 200,
            height: 150,
            alignment: Alignment.center,
            child: Text(
              "Card",
              style: TextStyle(fontSize: 28, color: Colors.white),
            ),
          ),
        )
```

###### 按钮

```dart
TextButton(
    child: Text("爱你"),
    onPressed: () {print("55");},
),

OutlinedButton(onPressed: (){}, child: Text("爱你")),

ElevatedButton(
    onPressed: (){
        this.setState(() {
            num++;
        });
        print(num);
    }, child: Text("爱你"),
    style: ButtonStyle(backgroundColor: MaterialStateProperty.all(Color(0xffEDFCF5)),foregroundColor:MaterialStateProperty.all(Color(000))),)

    IconButton(
    icon: Icon(Icons.error),
    onPressed: () {
        print("object");
    }),
```

###### wrapp

```dart
Wrap(
          // 主轴上的item距离
          spacing: 20,
          //交叉轴上空间之间的距离
          runSpacing: 20,
          alignment: WrapAlignment.start,
          children: [
            Container(
              width: 100,
              height: 100,
              color: Colors.black,
              child: Text("按钮"),
            ),
            Container(
              width: 100,
              height: 100,
              color: Colors.black,
              child: Text("按钮"),
            ),
            Container(
              width: 100,
              height: 100,
              color: Colors.black,
              child: Text("按钮"),
            )
          ],
        )
```

###### 动态组件

```dart
class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  int num=1;
  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Text("${num}"),
        TextButton(
          child: Text("爱你"),
            onPressed: () {
              this.setState(() {
                num++;
              });
              },
        )],
    ),
      );
  }
}
```

###### Scaffold

```dart
Scaffold(
    appBar: AppBar(
        //导航颜色
        backgroundColor: Colors.green,
        //左侧按钮
        leading: IconButton(
            icon: Icon(Icons.eject),
            onPressed: () {
                print("按钮图标");
            },
        ),
        title: Text("MenuPage"),
        //右侧按钮
        actions: [IconButton(onPressed: () {}, icon: Icon(Icons.search))],
    ),
    body: Container(
        child: Text("MenuPage"),
    ),
);
```

###### 顶部导航栏

```dart
顶部导航
	需要将Scaffold组件包裹在DefaultTabController组件内
	

DefaultTabController(
    length: 2, //配置顶部tab的数量
    child:Scaffold(
      appBar: AppBar(
          title: Text(this.title2),
          bottom: TabBar(   顶部导航栏TabBar在bottom中设置
            tabs: <Widget>[    导航栏bar
              Tab(text:"顶部1"),   bar中的文字标签内容
              Tab(text:"顶部2")
            ],
         ),
       ),
      body: TabBarView(  点击bar对应的内容,第一个组件对应第一个bar,第二个对应第二个,以此类推
        children: <Widget>[
        
            Text("第一个bar内容")
            Home()
        ],
      ),
    ),
);


在导航栏中嵌套顶部导航栏设置

	比如已经设置底部导航栏再设置其中页面的顶部导航栏
	
		出现问题:
			因为底部导航栏已经使用过Scaffold设置了顶部栏目主题信息,再使用顶部导航栏
			时候还要使用Scaffold设置,这就导致了会有两个顶部栏目，即两个标题
		
		解决方案:
			将顶部导航栏的内容放置自身Scaffold的title中,这样导航栏信息就会出现在底部导航栏设置的Scaffold的下面
		
		如下:
		 DefaultTabController(
		      length: 2,
		      child: Scaffold(
		      
		        appBar: AppBar(
		        
		          title: Row(   //因为title接收组件,故在title中设置导航栏
		            children: <Widget>[
		              Expanded(
		                child: TabBar(
		                  tabs: <Widget>[
		                    Tab(text: '分类1'),
		                    Tab(text: '分类2',)
		                  ],
		                )
		              )
		            ],
		          ),
		          
		        ),
		         body:TabBarView(
		            children: <Widget>[
		              Text('分类111'),
		              Text('分类222')
		            ],
		          ),
		      ),
		    );


顶部导航栏参数配置
	在TabBar中与tabs同级设置
	
    indicatorColor: Colors.red,  bar的下划线指示器选中颜色
    isScrollable: true,  导航栏是否可以滑动
    indicatorWeight:  bar的下划线指示器的高度
    indicatorPadding: ,  bar的下划线指示器的padding
    labelColor: Colors.red,  标签文字颜色
    indicatorSize: TabBarIndicatorSize.label, 下划线指示器与标签文字等宽,默认为tab与bar等宽
    labelStyle: , 标签文字样式
    labelPadding: , 标签文字padding 
    unselectedLabelColor: Colors.green,   未选中文字标签颜色
    unselectedLabelStyle: ,  未选中文字标签样式
```

###### [TabController定义顶部tab切换](https://www.cnblogs.com/yuyujuan/p/11026724.html)

通过DefaultTabController组件实现了AppBar里面的顶部导航切换，但是在项目中有数据请求，上拉加载更多等操作的时候，前面的写法，就不是很方便操作，因此，在flutter里面，还提供了一个用于实现顶部导航的组件：tabController

要使用tabController组件，就必须是在一个继承StatefulWidget的动态组件，并且还要实现SingleTickerProviderStateMixin这个类，

实例化TabController，实例化的时候需要传入两个参数，其中第一个是固定写法，第二个代表Tab个数。

![img](G:\note\image\1304208-20190615101547710-823057787.png)

```dart
import 'package:flutter/material.dart';

class TabBarControllerPage extends StatefulWidget {
  TabBarControllerPage({Key key}) : super(key: key);

  _TabBarControllerPageState createState() => _TabBarControllerPageState();
}

class _TabBarControllerPageState extends State<TabBarControllerPage> with SingleTickerProviderStateMixin {
  TabController _tabController;

  @override
  void initState() {  
    super.initState();
    _tabController=new TabController(
      vsync: this,
      length: 2
    );
  }  

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("TabBarControllerPage"),
        bottom: TabBar(
          controller: this._tabController, 
          tabs: <Widget>[
            Tab(text:"热销"),
            Tab(text:"推荐"),
          ],
        ),
      ),
      body: TabBarView(
        controller: this._tabController, 
        children: <Widget>[
          Center(child: Text("热销")),
          Center(child: Text("推荐"))
        ],
      ),
    );
  }
}
```

###### BottomNavigationBar

点击tab接受对应index 根据index渲染页面的List

```dart
class TabsPage extends StatefulWidget {

  @override
  _TabsPageState createState() => _TabsPageState();
}

class _TabsPageState extends State<TabsPage> {
  int currentIndex = 0;
  List listTabs = [
    Text("home1"),
    Text("home2"),
    Text("home3"),
  ];
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("标题"),
      ),
      body: this.listTabs[this.currentIndex],
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: this.currentIndex,
        iconSize: 30.0,
        type: BottomNavigationBarType.fixed,
        onTap: (index){
          setState(() {
            this.currentIndex = index;
          });
        },
        items: [
          BottomNavigationBarItem(icon: Icon(Icons.home),title:Text("首页1")),
          BottomNavigationBarItem(icon: Icon(Icons.category),title:Text("首页2")),
          BottomNavigationBarItem(icon: Icon(Icons.home),title:Text("首页3")),
        ],
      ),
    );
  }
}
```

###### 侧边栏

```dart
class Tabs extends StatefulWidget {
  // Tabs({Key key}) : super(key: key);
  // _TabsState createState() => _TabsState();
  final index;
  Tabs({Key? key, this.index = 0}) : super(key: key);

  _TabsState createState() => _TabsState(this.index);
}

class _TabsState extends State<Tabs> {
  // int _currentIndex=0;
  int _currentIndex = 0;
  _TabsState(index) {
    this._currentIndex = index;
  }
  List _pageList = [
    Text("第1页"),
    Text("第2页"),
    Text("第3页"),
  ];
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Flutter Demo"),
      ),
      body: this._pageList[this._currentIndex],
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: this._currentIndex, //配置对应的索引值选中
        onTap: (int index) {
          setState(() {
            //改变状态
            this._currentIndex = index;
          });
        },
        iconSize: 36.0, //icon的大小
        fixedColor: Colors.red, //选中的颜色
        type: BottomNavigationBarType.fixed, //配置底部tabs可以有多个按钮
        items: [
          BottomNavigationBarItem(icon: Icon(Icons.home), title: Text("首页")),
          BottomNavigationBarItem(
              icon: Icon(Icons.category), title: Text("分类")),
          BottomNavigationBarItem(icon: Icon(Icons.settings), title: Text("设置"))
        ],
      ),
      drawer: Drawer(
          child: Column(
        children: <Widget>[
          Row(
            children: <Widget>[
              Expanded(
                // child: DrawerHeader(
                //   child: Text('你好'),
                //   decoration: BoxDecoration(
                //     color:Colors.yellow,
                //     image: DecorationImage(
                //       image: NetworkImage("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3230740943,2194698121&fm=27&gp=0.jpg"),
                //       fit:BoxFit.cover,
                //     ),
                //   ),
                // ),
                child: UserAccountsDrawerHeader(
                  currentAccountPicture: CircleAvatar(
                    backgroundImage: NetworkImage(
                        "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1547205045,3791549413&fm=27&gp=0.jpg"),
                  ),
                  accountName: Text('侧边栏'),
                  accountEmail: Text('12345678@qq.com'),
                  decoration: BoxDecoration(
                      image: DecorationImage(
                    image: NetworkImage(
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3230740943,2194698121&fm=27&gp=0.jpg"),
                    fit: BoxFit.cover,
                  )),
                ),
              ),
            ],
          ),
          ListTile(
              leading: CircleAvatar(child: Icon(Icons.home)),
              title: Text('我的主页')),
          Divider(), //一根线的效果
          ListTile(
            leading: CircleAvatar(child: Icon(Icons.people)),
            title: Text('用户中心'),
            onTap: () {
              Navigator.of(context).pop(); //隐藏侧边栏
              Navigator.pushNamed(context, '/user'); //路由跳转
            },
          ),
          Divider(),
          ListTile(
              leading: CircleAvatar(child: Icon(Icons.settings)),
              title: Text('设置中心')),
        ],
      )),
    );
  }
}

```



###### ListView 

```
import 'package:flutter/material.dart';

void main ()=>runApp(MyApp(
  //传入数据                  长度
  items:new List<String>.generate(10000,(i)=>'Item $i')
));

class MyApp extends StatelessWidget{
  // 接受参数
  final List<String>items;
  MyApp({Key key,@required this.items}):super(key:key);
  @override
  Widget build(BuildContext context){
    return MaterialApp(
      title:'动态列表',
      home:Scaffold(
        appBar:new AppBar(
          title:new Text('标题'),
        ),
        //动态列表
        body:new ListView.builder(
          padding: const EdgeInsets.all(20),
           //列表长度
          itemCount:items.length,
          //每条内容
          itemBuilder:(context,index){
            return new ListTile(
              title:new Text('标题${items[index]}')
            );
          },
        )
      )
    );
  }
}
```
###### 时间

```
1 获取当前时间
var now = new DateTime.now(); //  2021-06-08 00:28:46.473818
now.microsecondsSinceEpoch //时间戳

//格式化库
date_format: ^2.0.2

import 'package:date_format/date_format.dart';


formatDate(DateTime.now(), [yyyy, '-', mm, '-', dd]) //根据日期
print(formatDate(DateTime.fromMicrosecondsSinceEpoch(1623112126473818),[yyyy, '-', mm, '-', dd])); //根据时间戳格式化
```

#### 基本路由

```dart
//一个按钮
 onPressed: () {
     //跳转页面
     Navigator.of(context).push(MaterialPageRoute(builder: (context) {
         //需要跳转的页面  传递参数
         return HomePage(title: "title");
     }));
 },
    
//返回按钮
onPressed:(){
    Navigator.of(context).pop();
},


//接受参数
class HomePage extends StatelessWidget {
    
  String title;
  HomePage({this.title = "HomePage"});
    
  @override
  Widget build(BuildContext context) {
    print(this.title);
    return Scaffold(
      appBar: AppBar(
        title: Text("homepage"),
      ),
      body: Container(
        child: Text(this.title),
      ),
    );
  }
}
```

#### 替换路由

```
Navigator.of(context).pushReplacementNamed("/register");
```

#### 返回跟路由

```dart
 //返回到指定根路由
Navigator.of(context).pushAndRemoveUntil(
    MaterialPageRoute(builder: (context) => new Tabs(index: 2,)),
    (route) => route == null);
```



#### 返回按钮传值

1 重写按钮

```dart
appBar: AppBar(
    title: Text("homepage"),
    leading: IconButton(
        icon: Icon(Icons.arrow_back),
        onPressed: () {
            Navigator.of(context).pop("返回消息");
        },
    ),
),
```

2 使用WillPopScope组件

```dart
WillPopScope(
    child:Scaffold(),//包括其他组件
    OnWillPop:(){
        Navigator.of(context).pop("title")
            return Future.value(true)
    }
)
```

#### 命名路由

```dart
创建路由配置
final routes = {
  "/home": (context) => HomePage(),
  '/MenuPage': (context, {arguments}) => MenuPage(arguments: arguments),
};

//固定写法
var onGenerateRoute = (RouteSettings settings) {
  // 统一处理
  final String? name = settings.name;
  final Function? pageContentBuilder = routes[name];
  if (pageContentBuilder != null) {
    if (settings.arguments != null) {
      final Route route = MaterialPageRoute(
          builder: (context) =>
              pageContentBuilder(context, arguments: settings.arguments));
      return route;
    } else {
      final Route route =
          MaterialPageRoute(builder: (context) => pageContentBuilder(context));
      return route;
    }
  }
};

点击按钮传参
Navigator.pushNamed(context, '/MenuPage', arguments: "你好")
//接受参数
class MenuPage extends StatelessWidget {
  final arguments;
  MenuPage({this.arguments});
  @override
  Widget build(BuildContext context) {
    print(this.arguments);
    return Scaffold(
      appBar: AppBar(
        title: Text("MenuPage"),
      ),
      body: Container(
        child: Text("MenuPage"),
      ),
    );
  }
}
```
#### 动态组件传参

```dart
1 添加路由
'/product': (context, {arguments}) => ProductPage(arguments: arguments),

2 页面跳转
Navigator.pushNamed(context, '/product', arguments: {"id": 1000});

3 接受参数
通过构造函数层层传递
class ProductPage extends StatefulWidget {
  final arguments;
  const ProductPage({this.arguments, Key? key}) : super(key: key);

  @override
  _ProductPageState createState() =>
      _ProductPageState(arguments: this.arguments);
}

class _ProductPageState extends State<ProductPage> {
  var arguments;
  _ProductPageState({this.arguments});
  @override
  void initState() {
    super.initState();
    print(this.arguments);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("商品页面"),
      ),
      body: Container(
        child: Text("商品页面"),
      ),
    );
  }
}
```



底部导航切换页面显示

```
//子页面模板
import 'package:flutter/material.dart';

class HomeScreen extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return Scaffold(
      appBar:AppBar(
        title:Text('HomeScreen'),
      ),
      body:Center(
        child:Text('home页面')
      )
    );
  }
}


import 'package:flutter/material.dart';
import './homescreen.dart';
import './homescreen1.dart';
import './homescreen2.dart';
// import './homescreen3.dart';
// import './homescreen4.dart';


// 这是动态页面  extends StatefulWidget
class BottomNavBar extends StatefulWidget {
  @override
  _BottomNavBarState createState() => _BottomNavBarState();
}

class _BottomNavBarState extends State<BottomNavBar> {
  final _BottomNavigationColor = Colors.blue;
  //设定选中栏的索引
  int _currentIndex = 0;
  // 设定选中底部的对应的页面
  List<Widget> list = List();
  @override 
  void initState(){
    // 其中的元素为组件
    list
        ..add(HomeScreen())
        ..add(HomeScreen1())
        ..add(HomeScreen2());
    super.initState();    
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // 根据选中的值县显示页面
      body:list[_currentIndex],
      //底部导航栏
      bottomNavigationBar:BottomNavigationBar(
        // 元素列表
        items:[
          BottomNavigationBarItem(
            icon: Icon(
              Icons.home,
              color:_BottomNavigationColor,
            ),
            title:Text(
              'Home',
              style:TextStyle(color:_BottomNavigationColor),
            ),
          ),
          BottomNavigationBarItem(
            icon: Icon(
              Icons.home,
              color:_BottomNavigationColor,
            ),
            title:Text(
              'Home',
              style:TextStyle(color:_BottomNavigationColor),
            ),
          ),
          BottomNavigationBarItem(
            icon: Icon(
              Icons.business,
              color:_BottomNavigationColor,
            ),
            title:Text(
              'business',
              style:TextStyle(color:_BottomNavigationColor),
            ),
          ),
        ],
        currentIndex:_currentIndex,
        onTap: (int index){
          setState(() {
            // 根据选中元素 设定index值
            _currentIndex = index;
              print(_currentIndex);
          });
        },
        selectedItemColor: Colors.green[800],
        type: BottomNavigationBarType.fixed,
      ),
    );
  }
}

//跳转按钮
RaisedButton(onPressed: (){Navigator.pushNamed(context, '/page1',arguments: {"url":'/page1'});},child: Text('page1')),
RaisedButton(onPressed: (){Navigator.pushNamed(context, '/page2',arguments: {"url":'/page2'});},child: Text('page2')),
RaisedButton(onPressed: (){Navigator.pushNamed(context, '/page3',arguments: {"url":'/page3'});},child: Text('page3')),

页面接受参数
import 'package:flutter/material.dart';

class Page1 extends StatelessWidget {
  final Map arguments;
  Page1({this.arguments});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title:Text('page1')
      ),
      body: Center(
        child: Text(arguments['url']),
      ),
    );
  }
}


```
底部不规则导航
```
import 'package:flutter/material.dart';
import './pageView.dart';

class BottomNavBar extends StatefulWidget {
 
  @override
  _BottomNavBarState createState() => _BottomNavBarState();
}

class _BottomNavBarState extends State<BottomNavBar> {
   //生命List组件 放回Widget
  List <Widget> _pageView;
  int _index = 0;
  //重写初始化状态
  @override
  void initState() {
    super.initState();
    _pageView = List();
    _pageView..add(PageComponView('home'))..add(PageComponView('桃心'));
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title:Text('底部不规则导航')
      ),
      body: _pageView[_index],
      floatingActionButton: FloatingActionButton(
        onPressed: (){
            Navigator.of(context).push(MaterialPageRoute(builder:(BuildContext context){
              return PageComponView("new page");
            }));
        },
        tooltip: '长按显示',
        child: Icon(
          Icons.add,
          color: Colors.white,
        ),
      ),
      floatingActionButtonLocation:FloatingActionButtonLocation.centerDocked,
      bottomNavigationBar: BottomAppBar(
        color: Colors.lightBlue,
        shape: CircularNotchedRectangle(),
        child: Row(
          //横向尽可能填满
          mainAxisSize: MainAxisSize.max,
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: <Widget>[
            IconButton(
              icon: Icon(Icons.home),
              color: Colors.white,
              onPressed: (){
                setState(() {
                  _index=0;
                });
              },
            ),
            IconButton(
              icon: Icon(Icons.favorite),
              color: Colors.white,
              onPressed: (){
                setState(() {
                  _index = 1;
                });
              },
            ),
          ],
        ),
      ),
    );
  }
}




import 'package:flutter/material.dart';


//动态组件接受参数 StatefulWidget
class PageComponView extends StatefulWidget {
  String _title;
  PageComponView(this._title);
  @override
  _PageComponViewState createState() => _PageComponViewState();
}

class _PageComponViewState extends State<PageComponView> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        // 小写 widget
        title:Text(widget._title),
      ),
      body: Center(
        child:Text(widget._title),
      ),
    );
  }
}
```
动画效果
```
import 'package:flutter/material.dart';
import './custom_Route.dart';
class FirstPage extends StatelessWidget{
  @override 
  Widget build(BuildContext context){
    return Scaffold(
      backgroundColor: Colors.blue,
      appBar: AppBar(
        title:Text('第一页',
        style: TextStyle(fontSize:36)),
        elevation: 4.0,
      ),
      body: Center(
        child:MaterialButton(
          onPressed: (){
            // 使用动画组件跳转页面
            Navigator.of(context).push(CustomRoute(SecondPage()));
           },
          child:Icon(
            Icons.navigate_next,size: 64,
            color: Colors.white,
          )
        )  
      ),
    );
  }
}

class SecondPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.pinkAccent,
      appBar: AppBar(
        title:Text('secondPage',style:TextStyle(fontSize:40)),
        backgroundColor:Colors.pinkAccent,
        leading:Container(),
        elevation: 80.0,
      ),
      body: Center(
        child: MaterialButton(
          onPressed: ()=>{
          Navigator.of(context).pop(),
        },
        child:Icon(
          Icons.navigate_before,
          color:Colors.white,
          size:64.0
        )
        ),
      ),
    );
  }
}


动画组件封装


//动画效果
import 'package:flutter/material.dart';

class CustomRoute extends PageRouteBuilder{
  final Widget widget;
  // 重写路由方法
  CustomRoute(this.widget)
    :super(
       // transitionDuration：设置动画持续的时间，建议再1和2之间。
      transitionDuration:const Duration(milliseconds: 400),
      pageBuilder:(
        BuildContext context,
        Animation<double> animation1,
        Animation<double> animation2){
          return widget;
        },
       transitionsBuilder:(
          BuildContext context,
          Animation<double> animation1,
          Animation<double> animation2,
          Widget child){
            //FadeTransition:渐隐渐现过渡效果，主要设置opactiy（透明度）属性，值是0.0-1.0。
            // return FadeTransition(
            //   opacity: Tween(begin:0.0,end :1.0)
            //       .animate(CurvedAnimation(
            //       parent:animation1,
                   // curve: 设置动画的节奏，也就是常说的曲线，Flutter准备了很多节奏，通过改变动画取消可以做出很多不同的效果。
            //    

          //缩放动画 主要设置 scale
          // return ScaleTransition(
          //   scale: Tween(begin:0.0,end :1.0)
          //     .animate(CurvedAnimation(
          //     parent: animation1,
          //     curve: Curves.fastOutSlowIn
          //   )),
          //   child: child,
          // );

          //旋转加缩放路由动画
          // return RotationTransition(
          //   turns: Tween(begin: 0.0,end:1.0)
          //   .animate(CurvedAnimation(
          //     parent:animation1,
          //     curve:Curves.fastOutSlowIn
          //   )),
          //   child: ScaleTransition(scale: Tween(begin:0.0,end:1.0)
          //   .animate(CurvedAnimation(
          //     parent:animation1,
          //     curve:Curves.fastOutSlowIn
          //   )),
          //   child: child,
          //   ),
          // );

          //作用滑动动画
          // return SlideTransition(
          //   position: Tween<Offset>(begin: Offset(-1.0, 0.0),
          //    end:Offset(0.0, 0.0))
          //   .animate(CurvedAnimation(
          //     parent:animation1,
          //     curve:Curves.fastOutSlowIn
          //   )),
          //   child: child,
          // );
        }  

    ); 
}
```
模糊玻璃
```
import 'package:flutter/material.dart';
import 'dart:ui';

class FrostedGlassDemo extends StatelessWidget{
  @override 
  Widget build(BuildContext context){
    return Scaffold(
      body: Stack( // 重叠的Stack Widget 实现重叠
       children: <Widget>[
         ConstrainedBox(  //约束盒子组件，添加额外的限制条件到 child上。
          constraints: const BoxConstraints.expand(), //限制条件，可扩展的。
          child:Image.network('https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1545738629147&di=22e12a65bbc6c4123ae5596e24dbc5d3&imgtype=0&src=http%3A%2F%2Fpic30.photophoto.cn%2F20140309%2F0034034413812339_b.jpg')
    
         ),
         Center(
           child: ClipRRect( // 裁切长方形
            child: BackdropFilter( //背景过滤
              filter: ImageFilter.blur(sigmaX: 3.0,sigmaY: 3.0), //图片模糊过滤，横向竖向都设置5.0
              child: Opacity(//透明控件
               opacity: 0.5,
               child: Container(
                 width: 500,
                 height: 700,
                 decoration: BoxDecoration(color:Colors.grey.shade200), //盒子装饰器，进行装饰，设置颜色为灰色
                 child: Center(
                   child: Text(
                     '王红旗',
                     style: Theme.of(context).textTheme.bodyText2,
                   ),
                 ),
               ),
              ),
            ),
           ),
         )
       ],
      ),
    );
  }
}
```
保存页面状态
```
import 'package:flutter/material.dart';
import './keepAliveView.dart';
// with是dart的关键字，意思是混入的意思，就是说可以将一个或者多个类的功能添加到自己的类无需继承这些类， 避免多重继承导致的问题。
// 需要注意的是with后边是Mixin，而不是普通的Widget，这个初学者比较爱犯错误。需要强调一下。

class KeepAliveDemo extends StatefulWidget {
  @override
  _KeepAliveDemoState createState() => _KeepAliveDemoState();
}

class _KeepAliveDemoState extends State<KeepAliveDemo> with SingleTickerProviderStateMixin {
  TabController _controller;
  @override
  void initState(){
    super.initState();
    _controller = TabController(length: 3, vsync: this);
  }

  // 重写被释放方法，只释放tabController
  @override 
  void dispose(){
    _controller.dispose();
    super.dispose();
  }
   @override 
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title:Text('keep Alive'),
        // appBar添加按钮
        bottom:TabBar(
          controller:_controller,
          tabs:[
            Tab(icon:Icon(Icons.android)),
            Tab(icon:Icon(Icons.bug_report)),
            Tab(icon:Icon(Icons.favorite)),
          ],
        ),
      ),
      // tabBarView 添加事件
      body: TabBarView(
        controller: _controller,
        children: <Widget>[
          KeepAliveView(),
          KeepAliveView(),
          KeepAliveView(),
        ],
      ),
    );
  }
}


import 'package:flutter/material.dart';

class KeepAliveView extends StatefulWidget {
  @override
  _KeepAliveViewState createState() => _KeepAliveViewState();
}

//混入AutomaticKeepAliveClientMixin，这是保持状态的关键
//然后重写wantKeppAlive 的值为true。
class _KeepAliveViewState extends State<KeepAliveView> with AutomaticKeepAliveClientMixin{
  int _count = 0;
   //重写keepAlive 为 true 保存页面状态
   @override 
   bool get wantKeepAlive => true;
   // 生命事件
   void _incrementCount(){
     setState(() {
       _count++;
     });
   }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment:MainAxisAlignment.center,
          children:<Widget>[
            Text('加+1'),
            Text(
              '$_count',
              style: Theme.of(context).textTheme.headline3,
            )
          ]
        ),
      ),
      floatingActionButton:FloatingActionButton(
        onPressed: _incrementCount,
        tooltip: '长按',
        child: Icon(Icons.add),
      ),
    );
  }
}
```
搜索框
```
import 'package:flutter/material.dart';
import 'asset.dart';


class SearchBarDemo extends StatefulWidget {
  _SearchBarDemoState createState() => _SearchBarDemoState();
}

class _SearchBarDemoState extends State<SearchBarDemo> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar:AppBar(
        title:Text('SearchBarDemo'),
        actions:<Widget>[
          IconButton(
            icon:Icon(Icons.search),
            onPressed: (){
              //  showSearch(context:context,delegate: searchBarDelegate());
               showSearch(context:context,delegate: searchBarDelegate());
            }
            // showSearch(context:context,delegate: searchBarDelegate()),
          ),
        ]
      )
    );

  }
}

class searchBarDelegate extends SearchDelegate<String>{
  //重写右侧Xf方法清空输入框
  @override
  List<Widget> buildActions(BuildContext context){
    return [
      IconButton(
        icon:Icon(Icons.clear),
        // 清空输入框
        onPressed: ()=>query = "",)
      ];
  }
  // 左侧 销毁当前页面
  @override
  Widget buildLeading(BuildContext context) {
    return IconButton(
        icon: AnimatedIcon(
            icon: AnimatedIcons.menu_arrow, progress: transitionAnimation),
        onPressed: () => close(context, null));
  }

  // buildResults方法，是搜到到内容后的展现，因为我们的数据都是模拟的，所以我这里就使用最简单的
  @override
  Widget buildResults(BuildContext context) {
    return Container(
      width: 100.0,
      height: 100.0,
      child: Card(
        color: Colors.redAccent,
        child: Center(
          child: Text(query),
        ),
      ),
    );
  }
  
   
   // 自动匹配搜索的关键字 并推荐
   @override
  Widget buildSuggestions(BuildContext context) {
    final suggestionList = query.isEmpty
        ? recentSuggest
        : searchList.where((input) => input.startsWith(query)).toList();
    return ListView.builder(
        itemCount: suggestionList.length,
        itemBuilder: (context, index) => ListTile(
              title: RichText(
                  text: TextSpan(
                      // 匹配的加粗内容
                      text: suggestionList[index].substring(0, query.length),
                      style: TextStyle(
                          color: Colors.black, fontWeight: FontWeight.bold),
                      // 未匹配 不加粗内容 
                      children: [
                        TextSpan(
                            text: suggestionList[index].substring(query.length),
                            style: TextStyle(color: Colors.grey))
                      ]
                  )
                  ),
            ));
  }


}
```
流式布局
```
import 'package:flutter/material.dart';

class WrapDemo extends StatefulWidget {
  @override
  _WrapDemoState createState() => _WrapDemoState();
}

class _WrapDemoState extends State<WrapDemo> {
  List<Widget> list;
  //初始化状态 条件值
  @override
  void initState() { 
    super.initState();
    list = List<Widget>()..add(buildAddButton())..add(deleBtn());
  }
  @override
  Widget build(BuildContext context) {
    //获取屏幕的宽高
    final width = MediaQuery.of(context).size.width;
    final height = MediaQuery.of(context).size.height;

    return Scaffold(
      appBar: AppBar(
        title:Text('Wrap 流式布局'),
      ),
      body: Center(
        child: Opacity(
          opacity: 0.8,
          child: Container(
            width: width,
            height: height/2,
            color: Colors.blueAccent,
            //采用流式布局
            child: Wrap(
              children:list,
              //每个元素的间距
              // spacing: 30.0,
            ),
          ),
        ),
      ),
    );
  }
  Widget buildAddButton(){
    // GestureDetector它式一个Widget，但没有任何的显示功能，而只是一个手势操作，用来触发事件的。虽然很多Button组件是有触发事件的，比如点击，但是也有一些组件是没有触发事件的，比如：Padding、Container、Center这时候我们想让它有触发事件就需要再它们的外层增加一个
    return GestureDetector(
      onTap: (){
        if(list.length<9){
          setState(() {
            list.insert(list.length-2, buildPhoto());
          });
        }
      },
      child: Padding(
        padding: const EdgeInsets.all(8),
        child: Container(
          width: 80,
          height: 80,
          color: Colors.black38,
          child: Icon(Icons.add_a_photo),
        ),
      ),
    );
  }

  Widget deleBtn(){
    // GestureDetector它式一个Widget，但没有任何的显示功能，而只是一个手势操作，用来触发事件的。虽然很多Button组件是有触发事件的，比如点击，但是也有一些组件是没有触发事件的，比如：Padding、Container、Center这时候我们想让它有触发事件就需要再它们的外层增加一个
    return GestureDetector(
      onTap: (){
        if(list.length>2){
          setState(() {
            list.removeAt(list.length - 3);
          });
        }
      },
      child: Padding(
        padding: const EdgeInsets.all(8),
        child: Container(
          width: 80,
          height: 80,
          color: Colors.black38,
          child: Icon(Icons.delete),
        ),
      ),
    );
  }

  Widget buildPhoto(){
    return Padding(
      padding: const EdgeInsets.all(8),
      child: Container(
        width: 80,
        height: 80,
        color: Colors.amber,
        child: Center(
          child: Text('照片'),
        ),
      ),
    );
  }
}
```
#### ExpansionTile

![截屏2020-06-02下午12.32.19.png](https://upload-images.jianshu.io/upload_images/16514325-f6653ba7c69c237e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```
import 'package:flutter/material.dart';

class ExpansionTileDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title:Text('ExpansionTile控件')),
      body: Center(
        child: ExpansionTile(
           //  title:闭合时显示的标题，这个部分经常使用Text Widget。
          title:Text('Expansion Tile'),
          // 左侧图标
          leading: Icon(Icons.ac_unit),
          // 展开时 背景颜色
          backgroundColor: Colors.black12,
          // children: 子元素，是一个数组，可以放入多个元素
          children: <Widget>[
            ListTile(
              title:Text('list title'),
              subtitle:Text('subtitle')
            ),
             ListTile(
              title:Text('list title'),
              subtitle:Text('subtitle')
            )
          ],
          // 是否展开
          initiallyExpanded: false,
        ),
      ),
    );
  }
}
```
#### ExpansionPanelList
![截屏2020-06-02下午1.20.27.png](https://upload-images.jianshu.io/upload_images/16514325-33580cd8215cc7f7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
```
// ExpansionPanelList 必须放在可滚动的控件内
import 'package:flutter/material.dart';

//生命类控制展开闭合
class ExpandStateBean{
  var isOpen;
  var index;
  ExpandStateBean(this.index,this.isOpen);
}

class ExpansionPanelListDemo extends StatefulWidget {
  @override
  _ExpansionPanelListDemoState createState() => _ExpansionPanelListDemoState();
}

class _ExpansionPanelListDemoState extends State<ExpansionPanelListDemo> {
  var currentPanelIndex = -1;
  List<int> mlist; //控制索引
  List<ExpandStateBean> expandStateList; //展开状态的列表

//构造方法，调用这个类的时候自动执行 
  _ExpansionPanelListDemoState(){
    mlist = new List();
    expandStateList = new List();
    for(int i=0;i<20;i++){
      mlist.add(i);
      expandStateList.add(ExpandStateBean(i,false));
    }
  }
   // 控制展开闭合的方法
   _setCurrentIndex(int index,isExpand){
     setState(() {
       expandStateList.forEach((element) {
         //找到点击的改变选中状态
         if(element.index == index){
           element.isOpen = !isExpand;
         }
       });
     });
   }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title:Text('expansion panel list')),
      // 列表必须放在滚动空间内
      body: SingleChildScrollView(
        child: ExpansionPanelList(
          //便利数据生成列表
          children: mlist.map((index){
            return ExpansionPanel(
               headerBuilder: (context,isExpanded){
                return ListTile(
                  title:Text('This is No. $index')
                );
              },
              //列表数据
               body:ListTile(
                title:Text('expansion no.$index')
              ),
              isExpanded: expandStateList[index].isOpen
            );
          }).toList(),
          // 列表的事件
          expansionCallback: (index,bool){
            _setCurrentIndex(index, bool);
          },
        ),
      ),
    );
  }
}
```
 #### 波浪行
![截屏2020-06-02下午2.04.53.png](https://upload-images.jianshu.io/upload_images/16514325-98385003c658a4c9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```
import 'package:flutter/material.dart';

class HomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body:Column(
        children: <Widget>[
          ClipPath(
            clipper:BottomClipperTest(),
            child: Container(
              color:Colors.deepPurpleAccent,
              height: 200.0,
            ),
          )
        ],
      )
    );
  }
}

class BottomClipperTest extends CustomClipper<Path>{
  @override
    Path getClip(Size size) {
      // TODO: implement getClip
  //根据点连线
      var path = Path();
      path.lineTo(0, size.height-20);
      var firstControlPoint =Offset(size.width/4,size.height);
      var firstEndPoint = Offset(size.width/2.25,size.height-30);

      path.quadraticBezierTo(firstControlPoint.dx, firstControlPoint.dy, firstEndPoint.dx, firstEndPoint.dy);

      var secondControlPoint = Offset(size.width/4*3,size.height-80);
      var secondEndPoint = Offset(size.width,size.height-40);

      path.quadraticBezierTo(secondControlPoint.dx, secondControlPoint.dy, secondEndPoint.dx, secondEndPoint.dy);

      path.lineTo(size.width, size.height-40);
      path.lineTo(size.width, 0);
      return path;
    }
    @override
      bool shouldReclip(CustomClipper<Path> oldClipper) {
        // TODO: implement shouldReclip
        return false;
      }

}
```
#### 打开引用前的动画
![截屏2020-06-02下午2.47.56.png](https://upload-images.jianshu.io/upload_images/16514325-697b6fc3c929ee8b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
```
import 'package:flutter/material.dart';
import 'home_page.dart';

class SplashScreen extends StatefulWidget {
  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> with SingleTickerProviderStateMixin {
  AnimationController _controller;
  Animation _animation;

  void initState() { 
    super.initState();
     _controller = AnimationController(vsync:this,duration:Duration(milliseconds:3000));
      _animation = Tween(begin: 0.0,end:1.0).animate(_controller);
        /*动画事件监听器，
      它可以监听到动画的执行状态，
      我们这里只监听动画是否结束，
      如果结束则执行页面跳转动作。 */
      _animation.addStatusListener((status){
        if(status == AnimationStatus.completed){
          Navigator.of(context).pushAndRemoveUntil(MaterialPageRoute(
            builder:(context)=> MyHomePage()
          ), (route) => route==null);
        }
      });
      //播放动画
      _controller.forward();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }
  
  @override
  Widget build(BuildContext context) {
    return FadeTransition(opacity:_animation,
    child: Image.network('http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1208/15/c0/12924355_1344999165562.jpg',
    scale: 2.0,
    fit: BoxFit.cover,
    ),
    );
  }
}


import 'package:flutter/material.dart';

class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title:Text('首页')),
      body:Center(
        child: Text('我是首页')
      )
    );
  }
}
```
右划返回上一页
```
import 'package:flutter/cupertino.dart'; 
// 其实早都知道Flutter有两套UI模板，一套是material,另一套就是Cupertino。Cupertino主要针对的的就是IOS系统的UI，所以用的右滑返回上一级就是在这个Cupertino里。

class RightBackDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      child: Center(
        child: Container(
          height: 100.0,
          width:100.0,
          color: CupertinoColors.activeBlue,
          child: CupertinoButton(
            child: Icon(CupertinoIcons.add),
            onPressed: (){
              Navigator.of(context).push(
                //  cupertino.dart 自带效果
                CupertinoPageRoute(builder: (BuildContext context){
                  return RightBackDemo();
                })
              );
            },
          ),
        ),
      ),
    );
  }
}
```
长按提示
```
import 'package:flutter/material.dart';

class ToolTipDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title:Text('tool tips demo')),
      body: Center(
        child: Tooltip(
          child: Icon(Icons.pregnant_woman),
          message: "长按提示",
        ),
      ),
    );
  }
}
```
拖拽变色
```
import 'package:flutter/material.dart';

class DraggableWidget extends StatefulWidget {
  final Offset offset;
  final Color widgetColor;
  const DraggableWidget({Key key, this.offset, this.widgetColor}):super(key:key);
  _DraggableWidgetState createState() => _DraggableWidgetState();
}

class _DraggableWidgetState extends State<DraggableWidget> {
  Offset offset = Offset(0.0,0.0);
  @override
  void initState() {
    super.initState();
    offset = widget.offset;
  }

  @override
  Widget build(BuildContext context) {
   return Positioned(
     left: offset.dx,
     top:offset.dy,
     //Draggable控件负责就是拖拽，父层使用了Draggable，它的子元素就是可以拖动的，子元素可以实容器，可以是图片。用起来非常的灵活。
     child: Draggable(
       // data: 是要传递的参数，在DragTarget里，会接受到这个参数。当然要在拖拽控件推拽到
       data:widget.widgetColor,
       child: Container(
         width: 100,
         height: 100,
         color:widget.widgetColor,
       ),
       feedback:Container(
         width: 100.0,
         height: 100.0,
         color: widget.widgetColor.withOpacity(0.5),
       ),
       onDraggableCanceled: (Velocity velocity, Offset offset){
         setState(() {
            this.offset = offset;
         });
       },
     ),
   );
  }
}



import 'package:flutter/material.dart';

import 'draggable_widget.dart';

class DraggableDemo extends StatefulWidget {
  @override
  _DraggableDemoState createState() => _DraggableDemoState();
}

class _DraggableDemoState extends State<DraggableDemo> {
  Color _draggableColor = Colors.grey;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Stack(
      children: <Widget>[
        DraggableWidget(
          offset: Offset(80.0, 80.0),
          widgetColor: Colors.tealAccent,
        ),
        DraggableWidget(
          offset: Offset(180.0, 80.0),
          widgetColor: Colors.redAccent,
        ),
        Center(
          child: DragTarget(onAccept: (Color color,) {
            _draggableColor = color;
          }, builder: (context, candidateData, rejectedData) {
            return Container(
              width: 200.0,
              height: 200.0,
              color: _draggableColor,
            );
          }),
        )
      ],
    ));
  }
}
```
#### 侧边栏
![image.png](https://upload-images.jianshu.io/upload_images/16514325-ad694921ebebaeea.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


```
import 'package:flutter/material.dart';

class HomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('左侧边栏'),
      ),
      drawer: Drawer(
        child: Column(
          children:<Widget>[
            // DrawerHeader(
            //   decoration: BoxDecoration(
            //     color:Colors.amber,
            //     image:DecorationImage(
            //       image: NetworkImage("https://www.itying.com/images/flutter/2.png"), fit:BoxFit.cover
            //     )
            //   ),
            //   child: ListView(
            //     children:<Widget>[
            //       Text('头部')
            //     ]
            //   ),
            // ),
            UserAccountsDrawerHeader(
              accountName: Text('王红旗'),
              accountEmail: Text('8510@qq.com'),
              currentAccountPicture: CircleAvatar(
                backgroundImage: NetworkImage("https://www.itying.com/images/flutter/3.png"),
              ),
              decoration: BoxDecoration(
                color:Colors.yellow,
                image: DecorationImage(
                  image:NetworkImage("https://www.itying.com/images/flutter/2.png"), fit:BoxFit.cover 
                )
              ),
              otherAccountsPictures: <Widget>[
               Image.network("https://www.itying.com/images/flutter/5.png"), Image.network("https://www.itying.com/images/flutter/6.png")
              ],
            ),
            
            ListTile(
              title:Text('个人中心'),
              leading: CircleAvatar(
                child:Icon(Icons.people)
              ),
            ),
            Divider(),
            ListTile(
              title:Text('系统设置'),
              leading: CircleAvatar(
                child:Icon(Icons.settings)
              ),
            )
          ]
        ),
      ),
      endDrawer: Drawer(
        child:Text('右侧边栏')
      ),
    );
  }
}
```
#### 表单

```
import 'package:flutter/material.dart';

class FormDemo extends StatefulWidget {
  @override
  _FormDemoState createState() => _FormDemoState();
}

class _FormDemoState extends State<FormDemo> {
   // 初始化赋值
  var username = new TextEditingController();

  @override

  @override
  void initState() { 
    super.initState();
    username.text="初始值";
  }
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title:Text('表单组件'),
      ),
      // body:TextDemo(),
      body: Column(
        children: <Widget>[
          TextField(
            decoration: InputDecoration(
              hintText: '请输入'
            ),
            // 绑定默认值
            controller: username,
            onChanged: (value){
              username.text = value;
            },
          ),
          SizedBox(height: 20,),
          Container(
            // 宽度和外层容器相同 自适应
            width: double.infinity,
            child: RaisedButton(onPressed: (){
              print(username.text);
            },
            child: Text('获取值'),
            color: Colors.blue,
           ),
          )
        ],
      ),
    );
  }
}

// 各种输入框
class TextDemo extends StatefulWidget {
  @override
  _TextDemoState createState() => _TextDemoState();
}

class _TextDemoState extends State<TextDemo> {
  @override
  Widget build(BuildContext context) {
    return Column(
        children: <Widget>[
          TextField(),
          SizedBox(height: 20,),
          TextField(
            //最多几行
            maxLines: 2,
            decoration: InputDecoration(
              hintText: '请输入内容',
              // 外边加边框
              border: OutlineInputBorder()
            ),
          ),
          // 密码框
          SizedBox(height: 20,),
           TextField(
            // 密码样式
            obscureText: true,
            decoration: InputDecoration(
              hintText: '请输入内容',
              // 外边加边框
              border: OutlineInputBorder()
            ),
          ),
          SizedBox(height: 20,),
          TextField(
            decoration: InputDecoration(
              border: OutlineInputBorder(),
              icon: Icon(Icons.people),
              labelText: '用户名'
            ),
          )
        ],
      );
  }
}
```


#### 时间控件

```
// 获取当前时间 new DateTime.now();
// 转换成时间戳 now.millisecondsSinceEpoch
// 时间戳转时间 time.FromMillisecondsSinceEpoch
import 'package:flutter/material.dart';
import 'package:date_format/date_format.dart';
// import 'package:flutter_localizations/flutter_localizations.dart'; 
class DateDemo extends StatefulWidget {
  @override
  _DateDemoState createState() => _DateDemoState();
}

class _DateDemoState extends State<DateDemo> {
  var nowTime = DateTime.now();
  var milinsTime = DateTime.now().millisecondsSinceEpoch;
  var time1 = formatDate(DateTime.now(), [yyyy, '-', mm, '-', dd]);
  var showTime = "";
   var sf;
  // 选择日期
  _showDatePicker (){
  showDatePicker(context: context, initialDate:nowTime, firstDate: DateTime(1990), lastDate: DateTime(2100),locale: Locale('zh'))
  .then((res){
    // var time = formatDate(res, [yyyy, '年', mm, '月', dd]);
    print(res);
    setState(() {
      nowTime = res;
    });
  });
  }      
  var sss =formatDate(DateTime.now(),['hour:',hh,'minute:',mm]);
  var ssss =TimeOfDay(hour: 12, minute: 00);
 
  // 选择 分秒
  _showTimePicker(){
    showTimePicker(context: context, initialTime:TimeOfDay(hour: 12, minute: 00),)
    .then((val){
      setState(() {
        sf = val;
      });
    });
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('时间'),
        ),
        body: Container(
          width: double.infinity,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text('日期'),
              Text('$nowTime'),
              Text('时间戳'),
              Text('$milinsTime'),
              Text('格式时间'),
              Text('选择时间'),
               
              // 格式化时间
              Text('${formatDate(DateTime.now(), [
                yyyy,
                '年',
                mm,
                '月',
                dd,
                '日',
                HH,
                ':',
                nn,
                ':',
                ss
              ])}'),
              Text('${formatDate(nowTime, [yyyy, '年', mm, '月', dd])}',style: TextStyle(
                fontSize: 20,
                color: Colors.orange
              ),),
               Text('${formatDate(nowTime, [HH, ':', nn, ':', ss])}',style: TextStyle(
                fontSize: 20,
                color: Colors.orange
              ),),
              InkWell(
                child: Text('按钮'),
                onTap: () {
                  _showDatePicker();
                },
              ),
               Text('${sf}',style: TextStyle(
                fontSize: 20,
                color: Colors.yellow
              ),),
              InkWell(
                child: Text('按钮'),
                onTap: () {
                  _showTimePicker();
                },
              )
            ],
          ),
        ));
  }
}

```
#### 时间第三方库

```
import 'package:flutter/material.dart';
import 'package:date_format/date_format.dart';
import 'package:flutter_cupertino_date_picker/flutter_cupertino_date_picker.dart';

class OtherDateTime extends StatefulWidget {
  @override
  _OtherDateTimeState createState() => _OtherDateTimeState();
}

const String MIN_DATETIME = '2010-05-12';
const String MAX_DATETIME = '2021-11-25';
const String INIT_DATETIME = '2019-05-17';
String _format = 'yyyy-MMMM-dd';

class _OtherDateTimeState extends State<OtherDateTime> {
    DateTime _dateTime;
    DateTimePickerLocale _locale = DateTimePickerLocale.zh_cn;
    //  List<DateTimePickerLocale> _locales = DateTimePickerLocale.values;
    @override
    void initState() { 
      super.initState();
       _dateTime = DateTime.parse(INIT_DATETIME);
    }
   _showDatePicker(){
     DatePicker.showDatePicker(
      context,
      onMonthChangeStartWithFirstDate: true,
      pickerTheme: DateTimePickerTheme(
        //是否显示标题
        showTitle: true,
        confirm: Text('确定', style: TextStyle(color: Colors.blueAccent)),
        cancel: Text('取消', style: TextStyle(color: Colors.red)),
      ),
      minDateTime: DateTime.parse(MIN_DATETIME),
      maxDateTime: DateTime.parse(MAX_DATETIME),
      initialDateTime:DateTime.now(),
      dateFormat: _format,
      locale: _locale,
      onClose: () => print("----- onClose -----"),
      onCancel: () => print('onCancel'),
      // onChange: (dateTime, List<int> index) {
      //   print(dateTime);
      //   setState(() {
      //     _dateTime = dateTime;
      //   });
      // },
      onConfirm: (dateTime, List<int> index) {
        print(dateTime);
        setState(() {
          _dateTime = dateTime;
        });
      },
    );
   }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('第三方时间库'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment:MainAxisAlignment.center,
          children: <Widget>[
            Row(
              children: <Widget>[
                InkWell(
                  child:Text("${formatDate(_dateTime,[yyyy, '年', mm, '月', dd])}"),
                  onTap:(){
                    _showDatePicker();  
                  },
                ),
               
              ],
            )
          ],
        ),
      ),
    );
  }
}
```
#### 轮播图 swiper

```
import 'package:flutter/material.dart';
import 'package:flutter_swiper/flutter_swiper.dart';

class SwiperDemo extends StatefulWidget {
  @override
  _SwiperDemoState createState() => _SwiperDemoState();
}

class _SwiperDemoState extends State<SwiperDemo> {
  List<Map> imgList = [
    {'url': 'http://img.duoziwang.com/2018/18/06121555627672.jpg'},
    {'url': 'http://pic.feizl.com/upload/allimg/170615/19403Ac9-3.jpg'},
    {
      'url':
          'http://b-ssl.duitang.com/uploads/item/201610/31/20161031213203_i5KRs.jpeg'
    },
    {
      'url':
          'http://01.minipic.eastday.com/20170523/20170523175755_c29d808f4357be2b791c11c114d6944a_6.jpeg'
    },
    {'url': 'http://pic.feizl.com/upload/allimg/170615/0001223938-4.jpg'},
    {'url': 'http://pic.feizl.com/upload/allimg/180126/280r3xyjhd2q0q.jpg'},
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('轮播图'),
      ),
      body: Column(
        children: <Widget>[
          Container(
              child: AspectRatio(
                aspectRatio: 16/9,
                child: new Swiper(
                  itemBuilder: (BuildContext context, int index) {
                    return new Image.network(
                      imgList[index]['url'],
                      fit: BoxFit.fill,
                    );
                  },
                  autoplay: true,
                  itemCount: imgList.length,
                  containerWidth: double.infinity,
                  // containerHeight: 400,
                  itemWidth: 300,
                  // itemHeight: 300.0,
                  layout: SwiperLayout.STACK,
                  // 左右箭头
                  // control: new SwiperControl(),
                  // 下方原点
                  pagination: new SwiperPagination(),
                ),
              )),
        ],
      ),
    );
  }
}

```
#### 弹框 dialog

![image-20210608085221521](G:\note\image\image-20210608085221521.png)

```dart
// 确定取消 对话框
  _alertDialog ()async {
    var result = await showDialog(context: context,
     builder: (context) {
       return AlertDialog(
         title: Text('提示信息'),
         content: Text('内容部分'),
         actions: <Widget>[
           FlatButton(
             child: Text('确定'),
             onPressed: (){
               print('确定');
               Navigator.pop(context,'confirm');
             },
           ),
            FlatButton(
             child: Text('取消'),
             onPressed: (){
               print('取消');
               Navigator.pop(context,'cancel');
             },
           ),
         ],
         
       );
     },
    );
     print(result);
  }
```

simpleDialog

![image-20210608085350379](G:\note\image\image-20210608085350379.png)

```
    _simpleDialog ()async {
    var result = await showDialog(context: context,
     builder: (context) {
       return SimpleDialog(
         title: Text('选择内容'),
         children: <Widget>[
           SimpleDialogOption(
             child: Text('A'),
             onPressed: (){
               print('确定');
               Navigator.pop(context,'A');
             },
           ),
           Divider(),
           SimpleDialogOption(
             child: Text('B'),
             onPressed: (){
               print('确定');
               Navigator.pop(context,'B');
             },
           ),
            Divider(),
           SimpleDialogOption(
             child: Text('C'),
             onPressed: (){
               print('确定');
               Navigator.pop(context,'C');
             },
           ),
         ],
         
       );
     },
    );
     print(result);
  } 
```

bottom

![image-20210608085524795](G:\note\image\image-20210608085524795.png)

```
_modelBottomSheet() async{
    var result = showModalBottomSheet(context: context,
     builder:(context){
       return Container(
         height: 300,
         child: Column(
           children: <Widget>[
             ListTile(
               title:Text('A物品'),
               onTap: (){
                 Navigator.pop(context,'C');
               },
             ),
             Divider(),
              ListTile(
               title:Text('B物品'),
               onTap: (){
                 Navigator.pop(context,'C');
               },
             ),
             Divider(),
              ListTile(
               title:Text('C物品'),
               onTap: (){
                 Navigator.pop(context,'C');
                 
               },
             )
           ],
         ),
       );
     }
     );
```

####  数据请求
```dart
class GetData extends StatefulWidget {
  const GetData({Key? key}) : super(key: key);

  @override
  _GetDataState createState() => _GetDataState();
}

class _GetDataState extends State<GetData> {
  List list = [];
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    // this._getData();
    //获取数据
    this.getHttp();
  }

  _getData() async {
    var url = Uri.parse('http://jd.itying.com/api/pcate');
    var response = await http.get(url);
    // await http .post(url, body: {'name': 'doodle', 'color': 'blue'}); //get请求
    // post请求
    print('Response status: ${response.statusCode}');
    print(response.body is Map);
    if (response.statusCode == 200) {
      setState(() {
        this.list = json.decode(response.body)["result"];
      });
    }
  }

  void getHttp() async {
    try {
      var response = await Dio().get('http://jd.itying.com/api/pcate');
      print(response.data['result']);
      setState(() {
        this.list = response.data['result'];
      });
    } catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    print(this.list.length);
    return Container(
      child: this.list.length > 0
          ? ListView.builder(
              itemBuilder: (context, index) {
                return Text(
                  this.list[index]['title'],
                  style: TextStyle(fontSize: 50, color: Colors.green),
                );
              },
              itemCount: this.list.length,
            )
          : Text("数据加载中"),
    );
  }
}
```
#### DIO
```

  getHttp() async{
    Response response;
    Dio dio = Dio();
    response = await dio.get(url);
    print(response.data);
  }

```
##状态管理  Provide
1 引入    provide: ^1.0.2
```
1 创建 provide文件夹  创建自己的文件 counter.dart

import 'package:flutter/material.dart';
// 创建自己的类
class Counter with ChangeNotifier{
  int value = 0;

  // 修改值的方法
  increment(){
    value ++;
    // 通知听众值发生变化
    notifyListeners();
  }

}

// main.dart  2 中引入配置

// provide 
import 'package:provide/provide.dart';
import 'provide/counter.dart';


void main(){
 var providers = Providers();
 // 实例变量 
 var counter = Counter();
 providers.provide(Provider<Counter>.value(counter));


 // 加 S
 
  runApp(
    ProviderNode(
      providers: providers,
      child: MyApp(),
    ),
  );
}



// 3 使用
 Provide<Counter>(
                builder: (context, child, counter) => Text(
                    '${counter.value}',
                    style:TextStyle(fontSize: 40),
                  ),
            ),


修改数据方法

onPressed: (){
          Provide.value<Counter>(context).increment(); 
        },
```


##flutter 错误整理
1. 出现异常。
DioError (DioError [DioErrorType.DEFAULT]: SocketException: OS Error: Connection refused, errno = 111, address = 127.0.0.1, port = 54726)
```
原因 解决127.0.0.1或 localhost  指向模拟器自己，不是电脑

解决
1. ip换成局域网ip地址 192.168.1.100
```
路由配置
```
1 新建routers文件夹

新建router.dart文件

import 'package:flutter/material.dart';
// 引入路由插件
import 'package:fluro/fluro.dart';
// import '../pages/details_page.dart';
import '../pages/details_page.dart';
import './router_handler.dart';

class Routes {
  static String root = '/';
  // 路径需要跳转的路径
  static String detailsPage = '/detail';

  static void configureRoutes(Router router) {
    // 页面不存在
    router.notFoundHandler = new Handler(
        handlerFunc: (BuildContext context, Map<String, dynamic> params) {
      print('ERROR====>页面不存在');
    });
    // 配置路由详情页 路径               规则
    router.define(detailsPage, handler: detailsHandler);
    // 其他页面 类似
    // router.define(detailsPage, handler: detailsHandler);
  }
}
2  各路由跳转规则
新建 router_handler.dart

// import 'dart:js';

import 'package:flutter/material.dart';
// 引入路由
import 'package:fluro/fluro.dart';

import '../pages/details_page.dart';

Handler detailsHandler = Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
//  传递的参数
  String goodsId = params['id'].first;
  print('goodsid$goodsId');
  return DetailsPage(goodsId: goodsId);
});
3  路由静态化 不用每次 都要去 new 
import 'package:fluro/fluro.dart';

class Application{
  static Router router;
}
4 入口引入路由 main.dart
import 'package:flutter/material.dart';

// 引入路由
import 'package:fluro/fluro.dart';
import 'routers/application.dart';
import 'routers/router.dart';

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    //配置路由
    final router = Router();
    Routes.configureRoutes(router);
    // 静态化
    Application.router = router;
    return Container(
      child: MaterialApp(
        title: "小超市",
        debugShowCheckedModeBanner: false,
        // 配置路由
        onGenerateRoute: Application.router.generator,
        theme: ThemeData(primaryColor: Colors.greenAccent[200]),
        home: IndexPage(),
      ),
    );
  }
}

5 需要使用的地方跳转
import '../routers/application.dart';
 onTap: () {
            print(item['id']);
            Application.router.navigateTo(context, "/detail?id=${item['id']}");
          },
```
#### 数据持久化 shared_preferences
```
import 'dart:async';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class CartPage extends StatefulWidget {
  @override
  _CartPageState createState() => _CartPageState();
}

class _CartPageState extends State<CartPage> {
  // 设置总体数据
  List<String> testList = [];

  @override
  void initState() {
    super.initState();
    _show();
  }

  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: <Widget>[
          Container(
            height: 500,
            child: ListView.builder(
                itemCount: testList.length,
                itemBuilder: (context, index) {
                  return ListTile(
                    title: Text(testList[index]),
                  );
                }),
          ),
          RaisedButton(
            onPressed: () {
              _add();
            },
            child: Text('增加'),
          ),
          RaisedButton(
            onPressed: () {
              _clear();
            },
            child: Text('减少'),
          )
        ],
      ),
    );
  }

  //增加数据
  void _add() async {
    //初始化一个持久变量
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    // 每次改变的值
    String temp = '持久化';
    // 每次增减数据
    testList.add(temp);

    // 把testlist写入持久化
    prefs.setStringList('testInfo', testList);
    _show();
  }

  //显示数据
  void _show() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    setState(() {
      if (prefs.getStringList('testInfo') != null) {
        testList = prefs.getStringList('testInfo');
      }
    });
  }

  void _clear() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    //移除数据
    prefs.remove('testInfo');
    setState(() {
      testList = [];
    });
  }
}

```

#### Provider

1 引入依赖

2 创建provider文件

```dart
import 'package:flutter/cupertino.dart';

// ignore: unused_import
import 'package:provider/provider.dart';

class CountProvider extends ChangeNotifier {
  int _count = 0;

  //获取值
  get count => _count;
  //添加值
  add() {
    _count++;
    notifyListeners();
  }
}

```

3 main.dart导入

```dart
void main() => runApp(MultiProvider(
      providers: [
        ChangeNotifierProvider(
          create: (context) => CountProvider(), //一个文件一行
        )
      ],
      child: MyApp(),
    ));
```

4 获取 修改

```dart

//当前文件                 获取值的方法
Provider.of<CountProvider>(context).count.toString()

//修改值
context.read<CountProvider>().add();  //调用修方法
```

