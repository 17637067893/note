## 文件读取
```
 用 于与文件系统进行交互
var fs = require("fs")

同步读取
var content = readFileSync(path,"utf-8")

异步读取
//无阻塞 速度快 性能高

let readFile = function(path){
 return new Promise((res,red)=>{
    fs.readFile(path,function(err,data)=>{
  if(err){ throw err} else{
 res(data.toString());
}
})
 }
```
## 文件写入
```异步写入
let write = function(path){
 fs.writeFile(path,"写入内容",{flag:'a'},(err)=>{
if(err){throw err}else{
console.log("写入成功")
}
})
}

按顺序写入
 let writePromise = function(path,centent){
     return new Promise((res,rej)=>{
         fs.writeFile(path,centent,{flag:'a',encodeing:'utf-8'},function(err){
             if(err){rej(err)}else{
                 res("success")
             }
         })
     })
 }

 let wr = async function(){
    await    writePromise("test.html","去哪了\n");
    await   writePromise("test.html","吃饭\n"); 
    await   writePromise("test.html","吃的什么\n");
    await    writePromise("test.html","蛋炒饭\n"); 
    
 }
 wr();
```
## 读取目录

```
let readFloder = function(path){
    return new Promise((res,rej)=>{
        fs.readdir(path,(err,files)=>{
            if(err){
                rej(err)
            }else{
                res(files)
            }
        })
    })
}
readFloder("../02文件写入")
.then((files)=>{
   
```
##流(stream)
Stream 是一个抽象数据接口，Node 中有很多对象实现了这个接口。例如，对http 服务器发起请求的request 对象就是一个 Stream，还有stdout（标准输出）。
Node.js，Stream 有四种流类型：

Readable - 可读操作。
Writable - 可写操作。
Duplex - 可读可写操作.
Transform - 操作被写入数据，然后读出结果。
所有的 Stream 对象都是 EventEmitter 的实例。常用的事件有：

data - 当有数据可读时触发。
end - 没有更多的数据可读时触发。
error - 在接收和写入过程中发生错误时触发。
finish - 所有数据已被写入到底层系统时触发。
####读取流

```
//引入模块
var fs = require("fs");

//每次读取的数据都存放进来
var data = '';

// 创建可读流
var readerStream = fs.createReadStream('input.txt');

// 设置编码为 utf8。
readerStream.setEncoding('UTF8');

// 处理流事件 --> data, end, and error
//较大文件每次读取一部分
readerStream.on('data', function(chunk) {
   data += chunk;
});

//文件读取结束输出存放的所有数据
readerStream.on('end',function(){
   console.log(data);
});

readerStream.on('error', function(err){
   console.log(err.stack);
});

console.log("程序执行完毕");
```
####写入流
```
var fs = require("fs");
var data = 'www.sxt.com';

// 创建一个可以写入的流，写入到文件 output.txt 中
var writerStream = fs.createWriteStream('output.txt');

// 使用 utf8 编码写入数据
writerStream.write(data,'UTF8');

// 标记文件末尾
writerStream.end();

// 处理流事件 --> data, end, and error
writerStream.on('finish', function() {
    console.log("写入完成。");
});

writerStream.on('error', function(err){
   console.log(err.stack);
});

console.log("程序执行完毕");
```
####管道流
提供了一个从输出流(读取流)到输入流(写入流)的机制，可用于文件的复制
```
var fs = require('fs')

//创建可读流
var readStream = createReadStream("a.txt")

//创建写入流
var writeStream = createWriteStream("b.txt")
//管道操作
//每次读取的a文件内容写入b文件
readStream.pipe(writeStream)
```
####链式流
链式流通过链接输出流到另外一个流并创建多个流操作链的机制。链式流一般用于管道操作
```
var fs = require('fs')

var zlib = require('zlib')

//压缩a文件 为b文件
fs.createReadStream("a.txt")
.pipe(zlib.createGzlip())
//  zlib.createGunzip()文件解压
.pipe(fs.createWriteStream("b.txt.gz"));

console.log("压缩完成");

```
####路径
```
let path = require("path");

//获取路径的后缀名
 let info = path.extname(str);

let arr = ['/ds','dfs','fasdf'];
let rePath = path.resolve(...arr); // 解构赋值
console.log(rePath)  //  /ds/dfs/fadff

// __dirname  获取文件的路径
let joinPath = path.join(__dirname,'wenjian','daima');
console.log(joinPath)   //
```
####URL
let url = require('url');
let httpUrl = "https://www.jianshu.com/writer#/notebooks/34584116/notes/69110747/preview";
console.log(url.parse(httpUrl));
```
Url {
  protocol: 'https:',
  slashes: true,
  auth: null,
  host: 'www.jianshu.com',
  port: null,
  hostname: 'www.jianshu.com',
  hash: '#/notebooks/34584116/notes/69110747/preview',
  search: null,
  query: null,
  pathname: '/writer',
  path: '/writer',
  href: 'https://www.jianshu.com/writer#/notebooks/34584116/notes/69110747/preview' }

```
爬虫 
1 爬斗图
网址：
https://www.doutula.com/article/list/?page=1
```
1 引入插件 cheerio 类似于jQuery能在服务端使用的插件
引入插件
const axios = require('axios');
const cheerio = require('cheerio');
const fs = require('fs');
const path  = require('path');
//  获取首页地址
let httpUrl = "https://www.doutula.com/article/list/?page=1";
axios.get(httpUrl).then((res) => {
let $ = cheerio.load(res.data);
// 找到我门要获取链接的元素
 $("#home .col-sm-g>a").each((index,item) => {
let url1 = $(item).attr("href");   // 获取链接
let title = $(item).find(".random_title").text();  // 获取标题
let reg = /(.*?)\d/;  //正则排除后边数字
title = reg.exec(title)[1];
  // 获取每组图片
   parsePage(rul1,title) 
})
})
async function parsePage(url,title){
let res = await axios.get(url);地址
let $ = cheerio.laod(res.data);
$(".pic-content .arttitle_des table img").each((index ,item) => {
// 图片地址
let imgUrl = $(item).attr("src");
// 获取扩展名
let lastName = path.extname(imgUrl)
// 图片拼接
let ws = fs.createWriteStream(`./img/${title}-${index}${lastName}`);
axios.get(imgUrl,{responseType:'stream'}).then((res) => {
res.data.pipe(ws);
})
})
}


```
####puppeteer
可以获取渲染到浏览器渲染页面的数据（不管前端渲染，后端渲染）

```
page.$()    相当于 document.querySelector  没有回调
page.$$ ()   相当于 document.querySelectorAll  没有回调
page.$eval()  可以获取属性
page.$$eval()  可以获取属性
1 npm install puppeteer 
引入插件
const puppeteer = require("puppeteer")

// 浏览器的各种配置
let config = {
 // 截图的大小
defaultViewport:{
width:1400,
height:800
},
// 关闭无图
headless:fase
}

(async () => {实例
//创建浏览器
let browser = await puppeteer.launch(config);
// 打开页面
const page = await brower.newPage();
// 发送网址
await page.goto(“https://www.dytt8.net/index.htm”)
// 获取浏览器渲染的元素内容
page.$$eval("#menu li a",(elements) => {
elements.forEach((item,index) => {
console.log(item.getAttribute("href"))
})
})
// 监听事件
page.on("console",(...ages) => {
console.log(args);
})
})();
```
http 传输的参数显示在地址栏
https 传输的参数加密的
```
http 超文本传输协议，tcp/IP 通信协议来传输数据

特点
1 简单  
2 灵活 允许任意类型的传输对象
3 无连接 每次连接只有一个请求，请求后断开连接
4 无状态 不会记以前传输信息
```
http请求到响应过程
```
1 根据输入的url DNS 服务器解析找到对应主机，使用默认端口 建立连接
2 根据URL 服务器到数据库查找需要的信息返回给前端
3 断开连接
4 渲染内容
```
#### http 模块
```
1 const http = require("http");
//创建服务
2 const server = http.createServer();
// 监听请求事件
3server.on("request",(req,res) => {
 // req 请求对象  // req.url   req.method
// res 返回对象

response.setHeader('Content-Type', 'text/html');
})
//监听端口
4  server.listen(3000,() => {
 console.log("server start")
})
```
###### NPM包上传

```
1 创建文件加  npm int 初始化
2 创建文件文件
3 命令行 npm login 登陆
4 npm publish 发布包
5 npm view less versions 查看所有版本
```

npm outdated 查看过期版本

![image-20210527080608294](G:\note\image\image-20210527080608294.png)

符号

```
^  "less": "^2.7.3"  锁定主版本号
~  "less": "~2.7.3"  锁定主版本号 和次版本号
"less": "2.7.3"  没有符号  锁定此版本号
"less": * 最新版本号
```

#### MongonDB

###### 数据类型

![image-20210527135104016](G:\note\image\image-20210527135104016.png)

1 

![image-20210527140506529](G:\note\image\image-20210527140506529.png)

```
db  查看当前数据库
show dbs  查看所有数据库
use music 创建/切换数据库
db.stats()  查看数据状态
db.dropDatabase() //删除当前数据库

db.createCollection("xiaoming") 创建集合  相当于表
db.getCollectoinNames()  //获取当前数据下的所有集合

db.user.insert([{name:"小明",age:"100"}]) //插入多条数据
db.user.find() //查询所有数据

db.user.update({name:"小明"},{$set：{age:9999}})  //修改语句 第一对象是修改的条件  后面是修改的内容 

db.user.update({name:"小明"}，{$inc:{age:100}})  //把当前age+100

db.user.update({name:"小明"}，{$inc:{age:1000},$set:{name:"小红"}}) //age+1000 name改为 小红

db.user.remove({age:10}) //删除
db.user.distinct("name") //查询去重后的数据
db.user.find({age:18})  //查询age=18 的数据
db.user.find({age:{$gt:18}})  //查询年龄大于18 $get >=
db.user.find({age:{$lt:18}})  //年龄小于 18    $lte <=
db.user.find({age:{$gte:26,$lte:36}})  age >=26 <= 36
db.user.find({name:/华/})  查询年龄中含有华的
db.user.find({name:/^华/}) 以华开头的name
db.user.find().limit(10).skip(5) //查询5-10之间的数据

```

#### 阻塞与 非阻塞

```
阻塞方法同步执行  非阻塞方法异步执行
```

#### 并发和吞吐量

```
在 Node.js 中 JavaScript 的执行是单线程的，因此并发性是指事件循环在完成其他工作后执行 JavaScript 回调函数的能力。任何预期以并行方式运行的代码必须让事件循环能够在非 JavaScript 操作（比如 I/O ）执行的同时继续运行。
```

