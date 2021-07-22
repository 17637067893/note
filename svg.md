#### 概述

svg 可缩放矢量图

XML 来描述二维图形和绘图程序的语言

#### 例子

```svg
     版本号          是否含有对外部文件的引用
	<?xml version="1.0" standalone="no"?>
    引用外部SVG DTD
	<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
	svg 代码块  宽度    高度                         命名空间
	<svg width="100%" height="100%" version="1.1" xmlns="http://www.w3.org/2000/svg">
         圆形    中心坐标         半径    边框颜色        宽度            内部填充颜色
		<circle cx="100" cy="50" r="40" stroke="black" stroke-width="2" fill="red" />
	</svg>
```

#### 引用方式

1 <embed>

```
<embed src="rect.svg" width="300" height="100" 
type="image/svg+xml"
pluginspage="http://www.adobe.com/svg/viewer/install/" />

pluginspage 属性指向下载插件的 URL。
```

2 object

```
<object data="rect.svg" width="300" height="100" 
type="image/svg+xml"
codebase="http://www.adobe.com/svg/viewer/install/" />

codebase 属性指向下载插件的 URL。
```

3 iframe

```
<iframe src="rect.svg" width="300" height="100">
</iframe>
```

#### 图形

##### 矩形

```xml

<rect width="300" height="100" style="fill:rgb(0,0,255);stroke-width:1;stroke:rgb(0,0,0)" />
透明
<rect x="20" y="200" width="250" height="250"style="fill:blue;stroke:pink;stroke-width:5;opacity:0.5"/>

圆角
<rect x="20" y="20" rx="20" ry="20" width="250"height="100" style="fill:red;stroke:black;stroke-width:5;opacity:0.5"/>
```

##### 圆形

```xml
<circle cx="100" cy="300" r="30" stoke="black" stroke-width="2" fill="blue" />
```

##### 椭圆

```xml
<ellipse cx="100" cy="420" rx="50" ry="80"style="fill:green;stoke:black;stroke-width:2"  />
```

##### 线段

```xml
<line id="line" x1="300" y1="300" x2="500" y2="500" style="stroke:rgb(99,99,99);stroke-width:2"/>
```

##### 多边形

```xml
<polygon points="220,100 300,210 170,250"style="fill:#cccccc;stroke:#000000;stroke-width:1"/>
```

##### 折线

```
<polyline points="0,0 0,20 20,20 20,40 40,40 40,60"style="fill:white;stroke:red;stroke-width:2"/>
```

##### 路径

```
<path d="M250 150 L150 350 L350 350 Z" />
```

#### 渐变

#### 滤镜

```xml
<defs>
    <filter id="Gaussian_Blur">
    <feGaussianBlur in="SourceGraphic" stdDeviation="3" />
    </filter>
    </defs>
    <ellipse cx="200" cy="150" rx="70" ry="40"
    style="fill:#ff0000;stroke:#000000;
    stroke-width:2;filter:url(#Gaussian_Blur)"/>
```

#### 其他标签

##### use

````xml
<circle id="myCircle" cx="100" cy="100" r="50" fill="green"></circle>
<use href="#myCircle" x="0" y="100"> </use>
````

##### g

```xml
<g id="myG">
    <circle id="myCircle" cx="100" cy="100" r="50" fill="green"></circle>
    <circle id="myCircle" cx="300" cy="100" r="50" fill="green"></circle>
    <circle id="myCircle" cx="200" cy="300" r="100" fill="green"></circle>
 </g>
```

#### 环形进度条

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <style>
        .text{
            text-anchor: middle;
            dominant-baseline:middle;
        }
        body{
            text-align: center;
        }
    </style>
</head>
<body>
    <svg xmlns:xlink="http://www.w3.org/200/svg" height="700" width="700">
       底色圆环
       <circle cx="350" cy="350" r="300" fill="none" stroke="grey" stroke-width="40" stroke-linecap="round" />
       进度条
       <circle class="progress" transform="rotate(-90,350,350)" cx="350" cy="350" r="300"
       fill="none"
       stroke="red"
       stroke-width="40"
       stroke-linecap="round"
       stroke-dashoffset="0"  
       stroke-dasharray="0,10000"
         />
         <!-- stroke-dashoffset：实线虚线绘制的起点距路径开始的距离 -->
         <text class="text" x="350" y="350" font-size="200" fill="red">36</text>
    </svg>
</body>
<script type="text/javascript" charset="utf-8">
    var progressDom = document.querySelector(".progress");
    var textDom = document.querySelector(".text");

    function rotateCircle(persent){
        //圆环总长度
        var circleLength = Math.floor(2 * Math.PI * parseFloat(progressDom.getAttribute("r")));
        // 进度对应的长度
        var value = persent*circleLength/100;

        var red = 255+parseInt((0-255)/100*persent);
        var green = 0+parseInt((191-0)/100*persent);
        var blue = 0 + parseInt((255-0)/100*persent);

        // 设置进度条
        progressDom.setAttribute("stroke-dasharray",value + ",10000");
        progressDom.setAttribute("stroke",`rgb(${red},${green},${blue})`);

        //设置文本颜色
        textDom.innerHTML = persent+'%';
        textDom.setAttribute("fill",`rgb(${red},${green},${blue})`);
    }

    let num = 0;
    setInterval(()=>{
        num++;
        if(num>=100){
            num=0
        }
        rotateCircle(num)
    },30)
 </script>
</html>
```

#### js操作SVG

```html
<body>
    <button class="btn">按钮</button>
    <svg width="1000" height="1000">
        <rect class="rect" x="10" y="10" width="200" height="100"></rect>
    </svg>
    
</body>
<script type="text/javascript">
    let rect = document.querySelector(".rect")
    let btn = document.querySelector(".btn")
    btn.onclick=function(){
        console.log([rect]);
       let width = parseInt(rect.getAttribute("width"));
       let height = parseInt(rect.getAttribute("height"));
       rect.style.fill = "green";
       rect.setAttribute("width",width+10)
       rect.setAttribute("height",height+10)
    }
</script>
```

#### svg 绘图原理

```
svg 是一种xml格式的矢量图，图形都是根据标签来实现的 如果图形变化浏览器重绘图形 可以绑定事件保存为.svg文件一般画小图标
```

#### svg 不失真原理

```
矢量图存储的是一个结构数据，例如方向 角度 位置 比例，如果大小变化 根据这些结构数据重新绘制
```

