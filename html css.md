css书写位置
```
1 内联 权限最高
<div style="color: red; "></div>
2 头部 权限中
3 外部引用  
 <link href="CSS文件的路径"  rel="stylesheet" />
```
选择器
```
标签选择器 div{}
类选择器 .box{}
id选择器 #box{}
交集选择器 pdiv{}
并集选择器 div,.one{}
后代div p{}
子代选择器div>p{}
伪类选择器
按照 lvha的顺序
:link 为访问的连接
:visited 已访问的连接
:hover 鼠标移动到链接上
:active 选定的链接
```

语义化标签
```
<title><title> 简单，描述性，唯一
<hn></hn> h1-h6分级标题
<header></header>  页眉通常包括网站标志、主导航、全站链接以及搜索框
<nav></nav>：标记导航，仅对文档中重要的链接群使用。
<progress></progress>：完成进度。可通过js动态更新value
<b></b>：出于实用目的提醒读者的一块文字，不传达任何额外的重要性 

作用 
1 提高代码可读性和可维护性
2 利于seo搜索引擎优化

```

##Flex 布局
 ```
flex-direction 元素排列的方向 row 一行 clouwn
  .box{
        display: flex;
        flex-direction: column;
      }
 ```
![image.png](https://upload-images.jianshu.io/upload_images/16514325-44f23dd41178481e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
flex-wrap 是否换行
```
默认 nowrap  不换行
wrap 换行
wrap-reverse 
```
![image.png](https://upload-images.jianshu.io/upload_images/16514325-713c099a70f7b2ad.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
flex-flow 是  flex-direction 与flex-wrap的集合属性
```
.box{
        display: flex;
        flex-flow: row wrap;
        justify-content:flex-start;
      }
```
![image.png](https://upload-images.jianshu.io/upload_images/16514325-c7f88ba2acdb2f00.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
```
 .box{
        display: flex;
        flex-flow: row wrap;
        justify-content:flex-end;
      }
```
![image.png](https://upload-images.jianshu.io/upload_images/16514325-7d998a370f7098c8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
```
.box{
        display: flex;
        flex-flow: row wrap;
        justify-content:center;
      }
```
![image.png](https://upload-images.jianshu.io/upload_images/16514325-49331854a75c4ef1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
```
.box{
        display: flex;
        flex-flow: row wrap;
        justify-content:space-between;
      }
```
![image.png](https://upload-images.jianshu.io/upload_images/16514325-aa4f81707a85c357.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
```
.box{
        display: flex;
        flex-flow: row wrap;
        justify-content:space-around;
        //每个项目两侧的间隔相等。所以，项目之间的间隔比项目与边框的间隔大一倍
      }
```
![image.png](https://upload-images.jianshu.io/upload_images/16514325-157043fae69728e2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
```
align-items属性
元素的对齐方法
```
align-items:flex-start; 在顶部对齐
align-items:flex-end;在底部对齐
align-items:center;在列中间对齐
align-items:baseline;第一行文字的基线对齐。
align-items:stretch(默认值)如果项目为设置高度或者高度为auto,将沾满整个容器的高度。
```
align-content
多个轴线的对齐方式，如果只有一条轴线，该属性不起作用
```
align-content:flex-start;起点对齐
align-content:flex-end;  底线对齐
align-content:center;中间对齐
align-content:space-between;两端对齐

##项目属性
order定义项目的排列顺序，数值越小，排列越靠前，默认为0
![image.png](https://upload-images.jianshu.io/upload_images/16514325-cd17907c9dbce32d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

flex-grow 定义项目的方法比例，默认为0  如果所有的项目的flex-grow属性都为1，则他们等分剩余空间，如果其中一个为2 则他大小为其他的二倍

flex-shrink 元素的缩小比例，默认为1，如果空间不足，该项目将缩小

flex-basis属性  定义元素的分配空间相当于 widt和height预约固定的空间，再把其他的空间分配给其他元素

flex flex-grow flex-shrink flex-basis的集合 默认  0 1 auto 后俩属性可选
快捷值 auto(1 1 auto) 和 none(0 0 none)。

align-self 允许单个睡醒与其他属性不一样的对齐方式，可覆盖align-items属性默认为auto,表示继承父元素的align-items属性，如果没有则等同于stretch属性

   ##字体设置
综合写法
```
书写顺序
{font:font-style font-weight font-size/line-height font-family}
风格 粗细 大小 行高 字体
line-height: 行高
text-align:对齐方式  left  right  center
text-indent 首行缩进
text-decoration 文本修饰
none  默认
underline 下划线
overline 上划线
line-through 中间

行高
如果 行高等于 高度 文字居中 
行高大于 高度 文字 偏下
行高小于高度 文字偏上
```
##颜色color
```
color 设置字体的颜色
```
##标签显示模式
```
块级元素
 div h1-h6 p ul 
特点 
1 单独占行
2 宽高默认100%
3  宽 高 margin padding 都可以设置

行内元素
 a b span  strong i 
不单独占位置 一般不可设置宽 高 对齐 padding和margin

行内块
img input ts 
1 有行内和块级的综合属性
2 相邻在一行 之间有空白
3 高度，行高、外边距以及内边距都可以控制。
```
切换模式
```
display:inline 
display:block
display:none  彻底消失
visible:hidden 透明
```
##css三大特性
```
层叠  多种CSS样式的叠加
继承 子标签会继承父标签的某些样式
优先级  经常出现两个或更多规则应用在同一元素上会出现优先级
```
![image.png](https://upload-images.jianshu.io/upload_images/16514325-1a45fa7cb2d618b6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
##北京
```
background-color   背景颜色
background-image 图片
background-repeat 是否平铺
background-postion 位置
background-attachment 固定滚动 sroll fixed
background-size
cover 自动缩放充满，溢出隐藏
contain 自动充满
```
##css三大模块
```
盒子模型 content+padding+border-margin
浮动  float
定位 position
```
margin
```
水平居中
.header{width:100px;margin:0 auto}
外边距合并
1 相邻块元素 marigin 去最大值
2 嵌套元素 内元素margin会溢出外margin
解决
  a 加 1边框
  b  外元素添加 overflow:hidden
  c 内元素+绝对定位
```
圆角
```
border-radius:50%;
```
盒子阴影
```
box-shadow:水平阴影 垂直阴影 模糊距离(虚实) 阴影尺寸(影子大小) 阴影颜色 内/外阴影
box-shadow: 5px 5px 3px 4px rgba(0, 0, 0, .4);
```
浮动
```
float:left; right
特性
脱离文档流，不占位置，会影响上一级
清除浮动
给父元素添加clear类
.clearfix:after{
content:" ";
visibility:hidden;
height:0;
display:block;
clear:both;
}
```
##定位
```
position:absolute
static 文档的默认位置
relative 
absolute 绝对定位（随父元素定位）
fixed 固定定位
1 如果父元素没有定位 以浏览器当前屏幕为准
```
z-index
```
1 值越大元素越在上
2 取值相同 后者在上
```
##overflow 溢出
```
visible 不剪切内容也不添加滚动
auto 超出滚动
hidden 查出隐藏
scroll 总是滚动
```
##鼠标样式 cursor
```
cursor :  default  小白 | pointer  小手  | move  移动  |  text  文本
```
轮廓
```
是绘制于元素周围的一条线，位于边框边缘的外围，可起到突出元素的作用
 outline : outline-color ||outline-style || outline-width 
去掉的    outline: 0;   或者  outline: none;
```
防止拖拽文本域resize：none
vertical-align 处置对齐
```
vertical-align : baseline |top |middle |bottom 
```
溢出文字隐藏
```
overflow:hidden;
text-overflow:clip;剪切    ellipsis 溢出显示(...)
```
## (sprite)css精灵图
```
将多个图片组合到一张图上，然后通过background-position:定位到对应位置，
减少请求次数
```
##字体图标
```
可以显示和图片一样的内容，但是体积小可以改变颜色，本质是字体，浏览器都支持
使用
1 声明样式
 @font-face {
  font-family: 'icomoon';
  src:  url('fonts/icomoon.eot?7kkyc2');
  src:  url('fonts/icomoon.eot?7kkyc2#iefix') format('embedded-opentype'),
    url('fonts/icomoon.ttf?7kkyc2') format('truetype'),
    url('fonts/icomoon.woff?7kkyc2') format('woff'),
    url('fonts/icomoon.svg?7kkyc2#icomoon') format('svg');
  font-weight: normal;
  font-style: normal;
}
2 给盒子使用字体
   span {
		font-family: "icomoon";
	}
3  盒子里面添加结构
span::before {
		 content: "\e900";
	}
或者  
<span></span>  
 
```
##SEO
```
SEO 搜索引擎优化 通过网站的优化，结构调整，提高网站的排名和曝光度
```
##2D变形 tansform
实现 位移 选装 清晰 缩放
```
transform-origin:left top; transform:rotate(45deg);
设置元素的原点为坐上角 然后顺时针旋转45度
translate(x,y)水平 竖直 移动
scale(x,y) 水平竖直缩放
```
##transition 动画
```
transition:属性，时间，速度，延迟；
transition width 10s ;
```
## animation
```
animation:名称 时间 速度 延迟 次数 循环;
@keyframes 名称{
0%{开始位置}
100%{结束}
}
infinite 无限循环
paused 暂停动画
animation-fill-mode:backwards 动画播放后回到原点forwards 播放完成后暂停到当前位置
```