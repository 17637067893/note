php 超文本预处理器 
运行原理

```
运行在服务器端的，内嵌在html中的脚本语言
版本信息 phpinfo();
```
文件格式
```
1 php文件以.php 结尾
2 编码格式为utf-8-BOM
3 一行代码末尾用 ; 结尾
4 <?php
```
变量命名
```
1 必须以$开头
2 不能以数字特殊字符开头，可以为中文_
3 严格区分大小写
```
引号
```
单引号字符串，双引号解释变量
连接字符串
$m = 小红;
'名字为:'.$m;
"名字为{$m}"
```
数据类型
```
标量 
整型integer 
浮点型
字符串
布尔

混合类型
数组 对象

特殊类型
空 null
资源 resource
变量为null的四种情况
1 变量未定义
2 变量定义为赋值
3 变量定义赋值为空
4 被unset();函数销毁的变量

判断为null的情况
is_null(); 四种情况返回ture 
isset();  变量存在且值不会null返回ture 
empty()  范围广   空对象 空数组  空字符串 0  null  空函数都返回ture
```
类型转换
```
gettype($m) //获取数据类型
var_dump($m); 输出值和类型

is_array();数组
is_string();字符串
is_bool();布尔
is_float();浮点型
is_object();
is_int();整型
is_numeric();数值
is_resource();资源
is_null();空
is_scalar();标量

类型转换

intval($m); 转为整型
strval()  转为字符串
floatval()转为浮点型
boolvai() 转为布尔类型
intval(null) = 0;
strval(null) = "";
boolval(null) = false;
```
系统常量
```
定义常量 define('变量名'，值)；名字为大写 值为标量
defined()判断变量名是否被定义过；
__FILE__(双下划线) 获取文件的位置 C:\wamp\www\define.php
__LINE__          获取当前的行
PHP_VERSION 获取php版本
__DIR__ 获取文件路径
__FUNCTION__ 获取函数名
pathinfo()  函数以数组的形式返回路劲信息
$path_parts = pathinfo(__FILE__);
$path_parts = pathinfo(__FILE__);
echo __FILE__,'<br />';                           C:\Apache24\htdocs\review.php
echo '路径名称'.$path_parts['dirname'],'<br/>';    C:\Apache24\htdocs
echo '文件全名'.$path_parts['basename'],'<br/>';   review.php
echo '文件名称'.$path_parts['filename'],'<br />';  review
echo '文件后缀'.$path_parts['extension'],'<br />'; php


M_PI 圆周率
mt_rand(1,6) //1-6的随机数
__MHTHOD__ //获取当前成员的方法名
__NAMESPACE__ //获取当前命名空间名字
__TRAIT__  获取当前TRAIT名字（多继承）
__CLASS__ 获取当前类名
```
运算符
```
算数运算 + - * / %(取余/求模)
比较运算符
> < >= <= == ===
逻辑运算符
|| 或 &&并且 ！取反
数值为0 = false
空字符串 || '0'= false
字符串 '0.000'为真
空数组 = false

$x=$x+1;
$x+=1;

$str = 'abc';
$str = $str.'d';
$str. = 'd';
执行运算符 ``
执行外部领
$str=`ipconfig`;
iconv()cmd为GBK编码编程用的是utf-8
iconv()函数
参数一 原来的编码
参数二 转换后的编码
参数三 变量
echo '<pre>';
echo iconv($str);
echo '<pre>';
```
流程控制
```
 = 赋值 ==比较  ===全比较
switch
<?php
 $test = 4;
switch($test){
	case 1:   
	case 2:
	case 3:echo '小于123都执行';break; //前边没有break小于3都执行这个代码
	case 5 :echo '等于5';break;
	default:echo '默认结果';break;
}

if else 分支
if($test==1){
    
}esle if($test==2){
    
}
```
循环
```
break关键字 终止循环 continue 跳过本次执行下一次循环
for 循环
for ($m=0;$m<5;$m++){
echo "第{$m}次输出 <br />";
}

while循环
$m = 0;
while($m<5){
	echo "第{$m}次循环<br />";
	$m++;
}

do while循环
$m = 0;
do{
    $m++;
    echo "这是第{$m}次<br />";
}while($m<10);

9X9乘法表

<?php
echo '<table width="800" height="200" border="1">';
     for($m=1;$m<=9;$m++){
		echo "<tr>";
		for($n=1;$n<$m;$n++){
			echo "<td>";
			echo $n.'x'.$m.'='.$n*$m;
			echo "</td>";
		}
		echo "</tr>"; 
		echo "<br />";
	 }
echo '</table>';
```
作用域
```
1 局部变量 函数内声明的变量
2 全局变量 函数外声明的变量
函数内外的变量不能相互使用！！！
3 可以通过超全局变量$GLOBALS['name']访问变量
超全局变量
外部变量和内部变量是同一个变量
$_GET $_POST $_FILE $_COOKIE $SESSION $GLOBALS

静态变量
static $m = 1; 
变量只会初始化一次，运行的结果会保存在内存中,再次调用时会用上次保存的结果
function sum(){
    static $num = 1;
    $num++;
    echo $num;
}

sum(); 2
sum();3
sum();4
```
限制函数类型
```
限制输入参数 整型  返回字符串
function sum(int $x,int $y):string{
    return $x+$y;
}
sum('1','2');

结构复制

```
文件上传
```
前端通过
  <form action="upload.php" method="post" enctype="multipart/form-data">
        <input type="file" name="fileName">
        <button>上传</button>
    </form>

服务器PHP
通过$_FILES接收上传
上传函数
move_uploaded_file()
参数一 文件临时路径
参数二 文件的保存的路径/文件名
```
php常用函数
```
数学函数
随机数
rand(min,max);
mt_rand(min,max);
小数
floor($m); 向下取整
ceil($m); 向上取整
round($m);向上取整
abs($m); 绝对值
pow(2,3) 2的三次方
sqrt(9) 平方根9
$arr=[1,2,3,45,6]
max($arr);
max(1,2,3,4,5) 最大数
min(1,2,3,4,5) 最小数
可以求数组中的最大值，也可求多个数的最大值
trim() 去两端空格
rtim() 去除右边空格
ltrim()去除左边空格
chop:trim()的别名
dirname("c:/testweb/home.php"); 返回路径的目录部分/c:/testweb
str_pad($str,20,'.') 把字符串填充为指定长度
str_repeat(".",13)指定重复使用的字符串
str_split();把字符串分成数组
strrev("123"); //321 反转数组
wordwrap($str,15) 对指定字符串进行拆行处理
str_shuffle("Hello Worl"); 随机打乱字符串
parse_str("id=23&name=John%20Adams", $myArray);
print_r($myArray);
strtolower("HELLO") 字符串转小写
strtoupper('abc') 字符串转大写
ucfirt() 字符串首字母大写
subtsr(); 截取字符串
ucwords() 字符串每个单词首字母大写
str_replace() 替换字符串 区分大小写
str_ireplace()字符串替换操作，不区分大小写

查找定位
strstr/strchr 返回首次出现到结尾的内容;
strrchr 返回最后一次出现到结尾的内容
stristr strstr忽略大写的版本；
strpos返回首次出现的位置;
stripos: strpos忽略大小写的版本
strrpos 返回最后一次出现的位置
哈希散列值
通过特殊的算法，将字符串转化为固定的十六进制的数字字符串
md5() 字符串转化为32位的哈希散列值
sha1()字符串转化为的40位的哈希散列值
```
引用文件
```
文件引入
include('另一个php文件');
引入错误时 警告错误，不影响后续代码运行
require('另一个php文件')
引入错误时，语法错误，后续代码不执行
include_once(' ');
require_once('');
多次引用文件只执行一次
```
数组
```
$arr = ['3'=>'a','b','c','d'];var_dump($arr); 
获取数组的个数count($arr);
数组转字符串join($arr,',')；
字符串转数组explod('-',$str);
必须是特殊字符串
$str=$str='1-2-4-4-5-4-6-4-1';
数组排序
sort($arr); 索引数组中的数值从小到大排列；
rsort($arr);索引数组中的数值从大到小排列
asort()   关联数组 数值从小到大
arsort() 关联数组 数值从大到小
数组是否存在某项
in_array("1",$arr);
数组是否存在某键位
array_key_exists('1',$arr);
根据数组中的值返回对应的键位,
如果多个键位存储的数值相同则返回第一个
array_search(1,$arr);
返回数组的键位值，组成新的数组；
array_keys($arr);
返回数组中的所有值组成新的数组
array_values($arr);
下标从3开始 关联数组 下标是我们指定的

$arr = [    'java' => 'a',    'html' => 'b',    'php' => 'mysql',    'js' => '效果']
怪异数组=>标准数组
双层foreach()循环
key value 位置颠倒
foreach ($arr as $key => $value) {
    foreach ($value as $keys => $values) {
        $newStr[$keys][$key]=$values;
    }
}
二维数组
$arr = [    'php'=>[        'a',        'b',        'c'    ],    'java'=>[        'e','f','g'    ]];

多维数组 一次类推数组内嵌套数组
$arr =
 [    'php'=>[        'a'=>[            'a',b,c        ],        'b'=>['1',2,3]    ],    
'java'=>[        'e'=>[1,2,3,4],        'f'=>[1,2,5,6]    ]]
```
数组的增删改查
```
$arr = ['a','b','c','d'];
增 $arr[$arr.length] = 'e';  arr_push($arr,'e');
首位添加 array_unshift($arr,1,2,3,4); //
首位删除 array_shift($arr);
删 unset($arr[2]);
该 $arr[1] = '修改';
查 $arr[1]   二维数组 $arr['php']['a']
$arr[]='最后添加';

```
foreach 遍历数组
```
$arr = ['a'=>'aa','b'=>'bb','c'=>'cc'];
foreach($arr as $key=> $value){
echo '索引为'.$key.'=='.$value.'<br />';
}
```
list遍历
```
$arr = ['a','b','c','d'];
list($a,$b,$c,$d,$e) = $arr;
echo $a,$b,$c,$d; //只
```
超全局数组
```
$_GET 获取表单提交的信息（通过url地址传输）
$_POST 获取表单提交的信息（url不显示信息）
$_REQUEST 可以获取get post的提交信息
$_SERVER 服务的配置信息
$_SERVER['REMOTE_ADDR'] ; //获取ip地址
$_SERVER['HTTP_REFERER']; 上级来源
```
错误处理
```
notice Warning 后续会执行 语句前加@可以屏蔽警告
Fatal error 后续代码不会执行
可以在配置文件中 diaplay_errors = Off关闭警告
error_log = '日志路径'
```
日期函数
```
$time = time();
date('Y-m-d H:i:s',$time); //与我们的时间差8小时
date_default_timezone_set('PRC');
$time = time();
date('Y-m-d H:i:s');
```
数据库
基本概念 
```
数据库是一个文件的集合，存储数据的仓库，本质是一个文件系统，按照特定的格式，把数据存储起来，用户可以对存储的数据进行增删改查
```
doc命令
```
mysql -uroot -p进入数据库
\q = quit 退出数据库
\c 结束命令

create database bbs; 增加一个数据库
drop database bbs; 删除数据库；
show databases; 查看数据库；
```
mysql常见的数据类型
```
基本概念
字节(byte) 一个字节存储8个bit 每个bit存储0或1
整型
1 tinyint:存储一个字节，一个字节等于8bit 每个bit可以存储0/1两种可能性多个tinyint类型可以存储2的8次方，也就是2的256中可能性从0开始计数，无符号也可以存储所以0-255，所以有符号局势-128~~127;
2 smallint 两个字节 2的16次方
可以存储0~65535，有符号则是-32768~32767
3 mediumint 3字节 2的3*8次方
4 int 4字节 2的4*8次方
5 bigint 8字节 2的8*8次方
unsigned和zerofill
unsigned修饰符只保存正直，即正值，mysql默认的是有符号的 这个修饰符紧跟数值类型后面
zerofill 修饰符规定0可以用来真补输出的值，使用这个修饰符可组织mysql存储负值，这个值需要配合tinyint smallint mediumint int 等字段的宽度指示器来用；
字字符串类型
 一个英文字节 占用一个字节
一个中文字符  占用二个字节
char  定长字符串
varchar 变长字符串
```
表单操作
```
use bbs //进入库 创建表
增加表 create table user(id int,name varchar(10),password varchar(15));
删除表 drop table user;
修改表 alter table user rename usertable

字段 
增加字段  alter table user add username varchar(5);
删除字段 alter table user drop username;
该 alter table user change name username varchar(5);
查看表
desc user;
查看所属的数据库
select database();
```
![image.png](https://upload-images.jianshu.io/upload_images/16514325-187ca2af01f8e76b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

数据库数据类型
```
整数类型             字节             范围
int/integer         4字节          （0,4294967295）
tinyint             1字节          （0,255）
smallint            2字节           （0,65535）
modiumint           3字节           （0,16777215）
bigint              8字节            (0，18 446 744 073 709 551 615)

浮点型               字节             范围
float(m,d)          4字节            单精度浮点型，m总个数，d小数位
double(m,d)         8字节            双精度浮点型，m总个数，d小数位

字符类型
var                 0-255字节       定长字符串 32密码
varchar             0-655355字节    变长字符串 用户名

时间类型
data                4字节           日期，格式：2014 09 18
time                3字节           HH：MM:SS
year                1 字节          YYYY

auto_increment
自动增加：只用于整型，可以设置起始值，默认为1；
常与后面primary key一起使用
创建表时在整数字段后加上：auto_increment=起始值 primary key
```
索引
```
优点 作用提高查询效率；降低表的更新速度其实索引也是一种表，保存着主键和索引字段，以及一个能将每个记录执行实际表的指针。虽然用户看不到但是能加速查询。缺点

普通索引（允许出现相同值）
alter table 表明 add index(字段名)；
alter table demo add index(age);

唯一索引  不可以出现相同值可以有NULL值
alter table 表名 add unique(字段名);
alter table demo add unique(age);
alter table demo drop 

全文索引 可以针对值中的某个单词，效率太低
alter table 表名 add fulltext(字段名)；
alter table demo add fulltext(address);

主键索引 不允许出现相同的值
表内只允许有一个
alter table 表名 add primary key(字段名);
alter table demo add primary key(id);
alter table demo drop primary key;

组合索引:将多个字段建到一个索引里，列值的组合必须唯一

查看索引
show index from 表名；
show index from demo;
```
数据的增删改查
```
添加数据
第一种 
insert into 表名 values() //对应数据对应类型
insert into demo values(1,'mm','1223',18,'ss','n');
第二种
insert into demo(key1,key2) values(val1,val2),(val3,val4);

查看结构
select * frim demo;
```
删除数据
```
delete from demo name = '修改后的值' where username = '修改前的位置';
修改多个值
update demo set name = 'name1',age = 15,password = 456 where id=3;
delete删除虽然会删除数据但是会保留数据的id下次新增会在次id之后+1
delete from user where id=4;
truncate删除全部数据 也会删除id
```
查找数据
```
select * from demo;查询全部数据；
select age from demo; 查询需要的数据；
select name,age from demo;查询多个数据；
查询去重
select distinct from demo; 多个只显示一个
范围查询
select * from demo where id between 4 and 5; id在4-5的数据
select * from demo where id!=4; 查询id不等于4的数据；<>=!
select * from demo where id in(3,4,5);查询id为3,4,5的数据；
select * from demo where age like '%7' 年龄个数为7的数据；
select * from demo order by age;按年龄升序排列
select * from demo order by age desc;年龄降序排序；
select * from demo limit 2,3;从第二条查询3个数据
select count(*) from demo; 查询与多杀条数据；
select name as username from demo;起别名（如果名字过长）
模糊查询
% 代替任意个字  _代替一个字
name中含有小关键字
select * from user where name LIKE "%小%";
分组查询 
查询的数据分组显示，如果数据有多条重复的只默认显示第一个
select * from user GROUP by name;
各类名字只会显示一条数据
聚合函数 count()对每个分组内的数据进行查询
count(字段) 查询字段中符合条件的所有数据
MAX(字段) 查询字段中所有数据中的最大值
MIX(字段) 查询字段中所有数据中的最小值
AVG(字段) 查询字段中所有数据中的平均值
SUM(字段) 查询字段中所有数据中的总和
查询每个密码的人数
select password,count(*) from user GROUP by password;
查询每个名字的年龄最大数
select age,MAX(age) from user GROUP by name;
注意 使用分组查询和聚合函数  select from 中间 最好只写分组字段和聚合函数，如果字段过多会有问题。默认显示每组中的第一条数据
对聚合函数count()约束条件使用having

排序查询
(对于分组查询没有效果)
升序排列
select * from user order by id asc;
降序排列 
select * from user order by id desc;
分组的数据按id升序排练
select * from user group by age order by id;
多个字段排序
先按照id在按照 age排序

select * from user order by id , age;
排序总结
select 
字段或聚合函数         设定的内容也就是查询的结果
from 表名                   表名或者是子查询
where                        字段的约束条件
group by                    分组查询
having                        聚合函数的约束条件
order by                     字段的排序方式asc   desc;
limit 起始键位，显示数量；
```
#分页查询
```
0 数据的键位，键位与Id无关，是从0开始的正整数
是数据的条数-1
select * from user limit 0,3;
每页显示的起始数据的键位，计算公式
参数1（当前页数-1)* 每页显示的数量
参数2 每页显示数据的数量 
```
#子查询 
将查询结果作为其他查询的条件
select * from user where age>(select age from user where id=6);
多表查询
```
内联查询

select 用户名，商品名 from 用户表 inner join 商品表 on 用户表的商品id = 商品表的id;
select username,goods from user inner join good on username.gid = goods.id;
查询用户买的商品；

左联查询  第一个表为准
select user.username from user left join goods on user.gid = goods.id;

右联查询  第二个表为准
select user.username from user right goods on user.gid = goods.id;

嵌套查询
查询买了商品的人
select * from user where gid in(select gid from goods);
```
![image.png](https://upload-images.jianshu.io/upload_images/16514325-a7a4a9afd16f4f62.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

php连接数据库
```
天龙八部

// 1 链接数据库
 $link = mysqli_connect('localhost','root');

//var_dump($link);
// 2判断是否链接成功
 if(!$link){
	 exit('mysql链接失败');
 }
// 3设置字符集
mysqli_set_charset($link,'utf8');
// 4选择数据库
mysqli_select_db($link,'bbs');

// 5准备sql语句
$sql = 'select * from user';
// 6发送sql 语句
$res = mysqli_query($link,$sql);
// var_dump($res);
// 7返回结果
$result = mysqli_fetch_assoc($res);
$result = mysqli_fetch_assoc($res);
$result = mysqli_fetch_assoc($res);
$result = mysqli_fetch_assoc($res);
var_dump($result);
//8关闭数据库
mysqli_close($link);
```
文件路径名
```
$file = '/www/htdocs/inc/lib.inc.php';
$name = pathinfo($file);
$name['filename'];    lib.inc
 $name['dirname'];   /www/htdocs/inc
 $name['basename'];   lib.inc.php
 $name['extension'];     php
```