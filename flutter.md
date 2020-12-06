##一起皆组件，对象用() 包裹

1 按照官网搭建环境
2 创建文件
flutter create myapp
cd myapp
3 打开模拟器后
flutter run
// 子元素自动适配屏幕
AspectRatio(
 aspectRatio: 16/9,
child:
)


// 宽度满屏
widith:double.infinity

## 动态组件接受参数
```
class OrderContentList extends StatefulWidget {

int type;定义个type 属性

OrderContentList(

  this.type

);

  @override

  _OrderContentListState createState() => _OrderContentListState();

}



在void initState() {

    // TODO: implement initState

    super.initState();

    print('${widget.type}'); 在initSatte中 widget.type可以取出来

  }
```


Text组件
```
//Widget 组件
// Scaffold 脚手架
import 'package:flutter/material.dart';

void main ()=>runApp(MyApp());

class MyApp extends StatelessWidget{
@override
Widget build(BuildContext context){
 return MaterialApp(
  title:'Text Widget' ,
 home:new Scaffold(
// 头部组件
appBar:new AppBar(
title:new Text('头部组件')
)
// 身体组件
body:Center(
// 所有属性在Text()写
child:Text('这是文字组件',
//文字左右居中
textAlign:TextAlign.center  //left  right
//最多显示行数
maxLines:'1',
//超出样式   默认
overflow:TextOverflow.cip  // ellipsis  。。。  fade 渐变
文字样式
style:TextStyle(
//字体
fontSize:25,
color:Color.fromARGB(255,255,125,125),
//下划线
decoration:TextDecoration.underline,
//下划线风格
decorationStyle:TextDecorationStyle.dashed , // solid 
)
)
)

)
)
}
}


```
Container 组件相当于 div
```
// Container 组件 => div

import 'package:flutter/material.dart';

void main () => runApp(MyApp());

class MyApp extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return MaterialApp(
      title:'主题',
      home:new Scaffold(
        appBar:new AppBar(
          title:new Text('导航栏'),
        ),
        body:Center(
          // 相当于 div 不设置大小 默认满屏宽高 100%
          child:Container(
            child:new Text('这是Container组件',style:TextStyle(
              fontSize:25,
              color:Color.fromRGBO(255, 125, 0, 1),
            )),
            //Container 子组件的对齐方式
            //alignment:Alignment.center, //居中 centerLeft  centerRight
            alignment:Alignment.topLeft, //底部居中 bottomRight bottomLeft topLeft topRight
            width: 500,
            height:500,
            // 背景色 Colors
            // color:Colors.tealAccent
            // 0x 后边开始  FF表示透明度 16进制  之后的556600 代表RGB色值
            //  color: const Color(0xbb556600),
            //背景渐变 不能与color同时存在
            decoration: new BoxDecoration(
              // RadialGradient 中心渐变    LinearGradient线性渐变
              gradient:const LinearGradient(
              //   begin: Alignment.topLeft,  开始位置
              //  end: Alignment.bottomRight,  结束位置
                colors:[Colors.lightBlue,Colors.greenAccent,Colors.purple],
                // titleMode:TitleMode.repeated,
              ),
                //border                             color:Colors.red   const Color(0xbb556600)
              border:Border.all(width: 5,color:Colors.red),
            ),
             //padding值 四周
              //padding: const EdgeInsets.all(30),
              padding:const EdgeInsets.fromLTRB(10,40,50,100),
              // margin 
              margin:const EdgeInsets.fromLTRB(20,40,80,100),
            
          )
        )
      )
    );
  }
}

```
Image组件
```
import 'package:flutter/material.dart';

void main => runApp(MyApp());
lass MyApp extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return MaterialApp(
      title:'Img Widget',
      home: new Scaffold(
        appBar:new AppBar(
          title: new Text('Img组件学习')
        ),
        body:Center(
          child:Container(
            // 图片地址
            child:new Image.network('http://t7.baidu.com/it/u=3204887199,3790688592&fm=79&app=86&f=JPEG?w=4610&h=2968',
             //填充方式
            fit:BoxFit.fill,
            color:Colors.blue,
            // 颜色覆盖到图片上
            colorBlendMode:BlendMode.modulate,
            repeat: ImageRepeat.repeat, //图片重复方向
            //颜色混合
            width: 50.0,
            height: 100.0,
            ),
            height: 300,
            width: 300,
            // padding: const EdgeInsets.all(10),
            margin: const EdgeInsets.all(30),
            // color: Colors.indigoAccent,
          )
        )
      ),
    );
  }
}
```
ListView 列表组件
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
网格组件 GridView
```
import 'package:flutter/material.dart';

void main ()=> runApp(MyApp());

class MyApp extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return MaterialApp(
      title:'gridView组件',
      home:Scaffold(
        appBar:new AppBar(
          title: new Text('gridView组件'),
        ),
        
        body:GridViewCount(),
        // body:OilGridView(), 
      ),
    );
  }
}

// 另一种写法
class OilGridView extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return GridView(
      gridDelegate:SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 3,
        // 纵轴间距
        mainAxisSpacing: 2,
        // 横轴间距
        crossAxisSpacing: 3,
        // 宽 高 比
        childAspectRatio: 0.7,
      ),
       children: <Widget>[
            new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',
            fit:BoxFit.cover),
            new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',
            fit:BoxFit.cover),
            new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',
            fit:BoxFit.cover),
            new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',
            fit:BoxFit.cover),
            new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',
            fit:BoxFit.cover),
             new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',
             fit:BoxFit.cover),
              new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',
              fit:BoxFit.cover),
               new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',
               fit:BoxFit.cover),
                new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',
                fit:BoxFit.cover),
          ],
    );
  }
}


//横轴具有最大个数
class GridViewCount extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return GridView.count(
          // 四周的间距
          padding: const EdgeInsets.all(10),
          //横轴的数量
          crossAxisCount: 4,
          //纵轴的间距
          mainAxisSpacing: 20,
          //横轴的间距
          crossAxisSpacing: 10,
          // 宽 高 比
          childAspectRatio: 0.7,
          children: <Widget>[
            new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',fit:BoxFit.cover),
            new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',fit:BoxFit.cover),
            new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',fit:BoxFit.cover),
            new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',fit:BoxFit.cover),
            new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',fit:BoxFit.cover),
             new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',fit:BoxFit.cover),
              new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',fit:BoxFit.cover),
               new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',fit:BoxFit.cover),
                new Image.network('http://t9.baidu.com/it/u=4241966675,2405819829&fm=79&app=86&f=JPEG?w=1280&h=854',fit:BoxFit.cover),
          ],
        );
  }
}
```
Row 水平布局
```
import 'package:flutter/material.dart';

void main ()=> runApp(MyApp());

class MyApp extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return MaterialApp(
      title:'Row 布局',
      home:Scaffold(
        appBar:new AppBar(
          title:new Text('标题')
        ),
        body:Row3(),
      ),
    );
  }
}
//不灵活的水平布局
class Row1 extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return new Row(
          children: <Widget>[
            new RaisedButton(onPressed: (){},
            color:Colors.green[400],
            child:new Text('按钮1')
            ),
             new RaisedButton(
                onPressed: (){},
                color:Colors.redAccent,
                child:new Text('红色按钮')
              ),
            new RaisedButton(onPressed: null,
            color:Colors.blue,
            child:new Text('按钮')
            ),
             new RaisedButton(onPressed: null,
            color:Colors.blue,
            child:new Text('按钮')
            ),
             new RaisedButton(onPressed: null,
            color:Colors.blue,
            child:new Text('按钮')
            ),
          ],
        );
  }
}
//灵活的布局 自动充满一行
class Row2 extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return new Row(
      children: <Widget>[
        Expanded(
          child: new RaisedButton(onPressed: (){},
          color:Colors.greenAccent,
          child:new Text('按钮')
          ),
        ),
         Expanded(
          child: new RaisedButton(onPressed: (){},
          color:Colors.greenAccent,
          child:new Text('按钮')
          ),
        ),
         Expanded(
          child: new RaisedButton(onPressed: (){},
          color:Colors.greenAccent,
          child:new Text('按钮')
          ),
        ),
        Expanded(
          child: new RaisedButton(onPressed: (){},
          color:Colors.greenAccent,
          child:new Text('按钮')
          ),
        ),
      ],
    );
  }
}
// 灵活与不灵活一起
class Row3 extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return new Row(
      children: <Widget>[
        new RaisedButton(onPressed: (){},
        color:Colors.green[400],
        child:new Text("按钮")
        ),
        //这个固定其余充满空间
        Expanded(child:new RaisedButton(onPressed: (){},
        color:Colors.green[400],
        child:new Text("按钮")
        ),),
        new RaisedButton(onPressed: (){},
        color:Colors.green[400],
        child:new Text("按钮")
        ),
      ],
    );
  }
}
```
Column 垂直布局
```
// cross轴：cross轴我们称为幅轴，是和主轴垂直的方向。比如Row组件，那垂直就是幅轴，Column组件的幅轴就是水平方向的。
//不灵活的水平布局
class Column3 extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return new Column(
      //依据最宽的文字 其余的居中
      crossAxisAlignment: CrossAxisAlignment.center,
      // 竖直方向对齐
      mainAxisAlignment: MainAxisAlignment.center,
      // CrossAxisAlignment.star：居左对齐。
      // CrossAxisAlignment.end：居右对齐。
      // CrossAxisAlignment.center：居中对齐。
      children: <Widget>[
        Text('6666'),
        Text('6666777777'),
        Text('6666'),
        Text('6666'),
      ],
    );
  }
}
```
stack 层叠布局
```
import 'package:flutter/material.dart';

void main ()=> runApp(MyApp());

class MyApp extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    //适用于连个元素层叠
    var stack = new Stack(
      // 定位  x y  0到1 
      alignment:const FractionalOffset(0.5, 0.9),
      children: <Widget>[
        new CircleAvatar(
          backgroundImage:new NetworkImage('http://b-ssl.duitang.com/uploads/item/201703/26/20170326161532_aGteC.jpeg'),
          radius:100,
        ),
        new Container(
          decoration: new BoxDecoration(
            color:Colors.lightBlue[50],
          ),    
          padding: EdgeInsets.all(2),
          child: new Text('王红旗'),
        ),
      ],
    );
    return MaterialApp(
      title:'Stack层叠布局',
      home:Scaffold(
        appBar:new AppBar(
          title:new Text('stack层叠布局')
        ),
        body:Center(child:stack)
      )
    );
  }
}
```
Positioned 多个层叠绝对定位
```
import 'package:flutter/material.dart';

void main ()=> runApp(MyApp());

class MyApp extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    //适用于连个元素层叠
    var stack = new Stack(
      // 定位  x y  0到1 
      alignment:const FractionalOffset(0.5, 0.9),
      children: <Widget>[
        new CircleAvatar(
          backgroundImage:new NetworkImage('http://b-ssl.duitang.com/uploads/item/201703/26/20170326161532_aGteC.jpeg'),
          radius:100,
        ),
        // 相当于绝对定位
        new Positioned(
          top:140,
          left:80,
          child:new Text('name')
        ),
        new Positioned(
          top:160,
          left:85,
          child:new Text('age')
        ),
      ],
    );
    return MaterialApp(
      title:'Stack层叠布局',
      home:Scaffold(
        appBar:new AppBar(
          title:new Text('stack层叠布局')
        ),
        body:Center(child:stack)
      )
    );
  }
}
```
card 布局
```
import 'package:flutter/material.dart';

void main ()=> runApp(MyApp());

class MyApp extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    var card = new Card(
      child:new Column(
       children: <Widget>[
          ListTile(
          title:new Text('文不能'),
          subtitle: new Text('计数棒：17637067893'),
          // 左侧图标
          leading:new Icon(Icons.arrow_upward,color:Colors.amberAccent),
          //右侧图标
          // trailing: Icon(Icons.more_vert),
        ),
        //分割线
        new Divider(),
        ListTile(
          title:new Text('文不能'),
          subtitle: new Text('计数棒：17637067893'),
          leading:new Icon(Icons.arrow_upward,color:Colors.amberAccent)
        ),
        new Divider(),
        ListTile(
          title:new Text('文不能'),
          subtitle: new Text('计数棒：17637067893'),
          leading:new Icon(Icons.arrow_upward,color:Colors.amberAccent)
        ),
        new Divider(),
       ],
      ),
    );
    return MaterialApp(
      title:'卡片布局',
      home:Scaffold(
        appBar:new AppBar(
          title:new Text('卡片布局'),
        ),
        body:Center(child:card),
      ),
    );
  }
}
```
路由 Navigator
```
//  pushReplacementName 替换路径
// 返回根路径 
Navigator.of(context).pushAndRemoveUntil(
new MaterialPageRoute(builder:(context) => new Tabs),
(route)=>route == null
)

import 'package:flutter/material.dart';

void main(){
  runApp(MaterialApp(
    title:'导航演示1',
    home:new FirstScreen()
  ));
}

class FirstScreen extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return new Scaffold(
      appBar:AppBar(title:Text('导航页面')),
      body:Center(
        child: RaisedButton(
          child: Text('查看详情'),
          onPressed: (){
            Navigator.push(context,new MaterialPageRoute(
              builder: (context) => new SecondScreen())
            );
          },
        ),
      ),
    );
  }
}

class SecondScreen extends StatelessWidget{
  @override
  Widget build(BuildContext context){
    return Scaffold(
      appBar:AppBar(
        title:Text('详情页面'),
      ),
      body: Center(child:RaisedButton(
          child: Text('返回'),
          onPressed: (){
            Navigator.pop(context);
          },
      ),),
    );
  }
}
```
## 命名路由
```
main.dart中   使用


import './components/demo15/home.dart';
import './routes/Routes.dart';

class MyApp extends StatelessWidget {
    @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      //命名路由
      home:HomePage(),
   // 使用路由
      onGenerateRoute:onGenerateRoute
    );
  }
}


新建路由文件
import 'package:flutter/material.dart';
import '../components/demo15/pages/page1.dart';
import '../components/demo15/pages/page2.dart';
import '../components/demo15/pages/page3.dart';

// 配置页面路由
  final routes={
    '/page1':(context,{arguments})=>Page1(arguments:arguments),
    '/page2':(context,{arguments})=>Page2(arguments:arguments),
    '/page3':(context,{arguments})=>Page3(arguments:arguments),
  };

// 实现跳转
  var onGenerateRoute = (RouteSettings settings) {
    // 统一处理
    final String name = settings.name;
    final Function pageContentBuilder =routes[name];
    if (pageContentBuilder != null) {
      if (settings.arguments != null) {
        final Route route = MaterialPageRoute(
            builder: (context) => pageContentBuilder(context,
                arguments: settings.arguments));
        return route;
      } else {
        final Route route = MaterialPageRoute(
            builder: (context) => pageContentBuilder(context));
        return route;
      }
    }
  };
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
## ExpansionTile

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
## ExpansionPanelList
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
 ## 波浪行
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
## 打开引用前的动画
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
## 侧边栏
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
表单
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
## CheckBox
```
import 'package:flutter/material.dart';

class CheckBoxDemo extends StatefulWidget {
  @override
  _CheckBoxDemoState createState() => _CheckBoxDemoState();
}

class _CheckBoxDemoState extends State<CheckBoxDemo> {
  var flag = true;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        Row(
          children: <Widget>[
            Checkbox(value: flag, onChanged: (val){
              setState(() {
                flag = val;
              });
            },
            activeColor: Colors.red,)
          ],
        ),
        Row(
          children: <Widget>[
            Text(flag?'选中':'未选中')
          ],
        ),
        SizedBox(height: 50,),
        Divider(),
        CheckboxListTile(
          value: flag,
          onChanged: (v){
            setState(() {
              flag = v;
            });
          },
          title: Text('标题'),
          subtitle: Text('二级标题'),
          secondary: Icon(Icons.hdr_off),
        )
      ],
    ),
    );
  }
}
```

单选框
```
 RadioListTile(
                value:2,
                onChanged: (v){
                  setState(() {
                      this.sex=v;
                  });
                },
                groupValue:this.sex ,
                 title: Text("标题"),
                subtitle:Text("这是二级标题") ,
                secondary:Image.network('https://www.itying.com/images/flutter/1.png'),
                selected: this.sex==2,
            ),
             RadioListTile(
                value:2,
                onChanged: (v){
                  setState(() {
                      this.sex=v;
                  });
                },
                groupValue:this.sex ,
                 title: Text("标题"),
                subtitle:Text("这是二级标题") ,
                secondary:Image.network('https://www.itying.com/images/flutter/1.png'),
                selected: this.sex==2,
            ),

 Radio(
          //       value: 1,
          //       onChanged: (v){
          //         setState(() {
          //           sex = v;
          //         });
          //       },
          //       groupValue: sex,
          //     ),
          //     Text('美女'),
          //      Radio(
          //       value: 2,
          //       onChanged: (v){
          //         setState(() {
          //           sex = v;
          //         });
          //       },
          //       groupValue: sex,
          //     )
```
时间控件
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
时间第三方库

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
轮播图 swiper
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
弹框 dialog
```
import 'package:flutter/material.dart';

class DialogDemo extends StatefulWidget {
  @override
  _DialogDemoState createState() => _DialogDemoState();
}

class _DialogDemoState extends State<DialogDemo> {
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
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('弹框'),
      ),
      body: Center(

        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            RaisedButton(onPressed: (){
              _alertDialog();
            },child:Text('alertDialog')),
            RaisedButton(onPressed: (){
              _simpleDialog();
            },child:Text('_simpleDialog')),
            RaisedButton(onPressed: (){
              _modelBottomSheet();
            },child:Text('底部弹框'))
          ],
        ),
      ),
    );
  }
}
```
数据请求与渲染
##  http
```
import 'dart:convert';

import 'package:flutter/material.dart';
// import 'dart:convert' as convert;
import 'package:http/http.dart' as http;

// 数据请求回来的是JSON 字符串 要转换成Map类型才能用

//  json.encode(mapData) Map 转换成JSON 字符串

//  json.decode(JSONData)  Json 转 map

  // _getList(){
  //   // List<Widget> list=[];
  //   Widget content;
  //   content= ListView(
  //     children:_mapData.map((item){
  //     return ListTile(
  //           title: Text('${item['username']}'),
  //           subtitle:Text('${item['title']}') ,
  //         );
  //       }).toList(),
  //   );
  //   return content;
  // }


var url = "http://iwenwiki.com/api/FingerUnion/list.php";

class RequestDemo extends StatefulWidget {
  RequestDemo({Key key}) : super(key: key);

  @override
  _RequestDemoState createState() => _RequestDemoState();
}

class _RequestDemoState extends State<RequestDemo> {
  List _mapData=[];
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    _getDate();
  }

  _getDate() async {
    var result = await http.get(url);
    if (result.statusCode == 200) {
      setState(() {
         this._mapData = jsonDecode(result.body)['data'];
      });
    } else {
      print(result.statusCode);
    }
  }

  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('数据请求'),
      ),
      body:_mapData.length>0?ListView(
        children:_mapData.map((value){
          return ListTile(
            title: Text("${value['title']}"),
            leading: Image.network("${value['cover']}",fit: BoxFit.cover,),
          );
        }).toList()
      ):Text('数据加载中'),
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
## 数据持久化 shared_preferences
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