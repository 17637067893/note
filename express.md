1 安装

```
1 全局安装
npm install -g express express-generator 
2创建文件夹
npm init 
npm install express --save

node app.js启动项目

实例
let express = ('express ')
let app = express();
app.get('/',(req,res) => {
res.send('<h1 style='color: blue'>hello world</h1>')
})

app.listen('3000',(err) => {
if(err){
console.log(err)
}else{
console.log('server start')
}
})
```
####路由
```
//匹配 / 路径
app.get('/',(req,res) => {
	res.send(`<h1 style='color: blue'>hello world</h1>`);
})


// b? 表示b可有可无  (bn)?也可以用
app.get('/usb?sde',(req,res) => {
	res.send(`<h1 style='color: blue'>usb?sde</h1>`);
})

// b+ b有一个或多个
app.get('/usbd+sde',(req,res) => {
	res.send(`<h1 style='color: blue'>usbd+sde</h1>`);
})

// * 代表中间插入任意字符
app.get('/a*b',(req,res) => {
	res.send(`<h1 style='color: blue'>a*b</h1>`);
})

// 路径包含f
app.get('/f/',(req,res) => {
	res.send(`<h1 style='color: blue'>.*fly</h1>`);
})


// 动态路由
// [http://localhost:3000/users/1](http://localhost:3000/users/1)
app.get('/users/:userId/',(req,res,next) => {
	let userId = req.params.userId
	res.send(`<h1 style='color: blue'>${userId}</h1>`);
	console.log(userId)
	next();
},(req,res) => {
	console.log(req.params.userId+1)
})
```
####ejs
```
1 引入 let ejs = require('ejs');

app.set('views','views');   //设置文件目录
app.set('views engine','ejs');  // 设置默认的模板引擎
app.set('ejs',ejs._express); // 使用express 项目引擎

let obj = {
		title:'<h3>首页</h3>',
		articleName:'<h3>首页</h3>',
		sex:'2',
		list:['a','b','c','d']
	}
// 渲染的目标文件
res.render('index.ejs',obj)

ejs 文件内
1 字符串渲染 
<%= title %>  <%- title%> (会把标签转义出来)

2 条件渲染（<% 可以写js代码 %>）
<% if(sex == ’2‘){ %>
<h3><%= title1 %></h3>
<% }else{ %>
<h3><%= title2 %></h3>
<% } %>
3 循环渲染
let obj={
arr :result
}
res.render('index.ejs',obj)
<% arr.forEach((item) => { %>
 <%   <div><%= item %></div> %>
<% })  %>
```
####获取GET POST 提交的参数
```
GET 方式
app.get('/',(req,res) => {
console.log(req.qurey)
})
POST方式
解析post参数
app.use(express.urlencoded()) 
app.post('/',(req,res) => {
console.log(res.body)
})
```
####中间件
```
请求到服务器，在我们返回数据之间的可以对数据进行操作
let express = require('express');
app.use(function(req,res,next) => {
res.apend('')
console.log("中间件")
next()//必须要有
})



还有路由中间件 可以分模块
router1.js
let express = require('express');
let router1 = express.Router();

router1.use(function(req,res,next) => {
console.log('router1 的中间件')
})


router1.get('/',function(req,res) => {
console.log('router1/')
})

router1.get('/use',function(req,res) => {
console.log('router1/use')
})

module.export = router1



app.js中引入

let router1 = require('./router.js')
app.use('/router1',router1)

以后请求 /router/*****的都在router1.js中操作

```
404
```
var createError = require('http-errors');

找不到页面
app.use(function(req, res, next) {
  next(createError(404));
});

报错的时候
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});
```

cookie
```
设置cookie

domain: 域名  
name=value：键值对，可以设置要保存的 Key/Value，注意这里的 name 不能和其他属性项的名字一样 
maxAge： 最大失效时间（毫秒），设置在多少后失效 。
secure： 当 secure 值为 true 时，cookie 在 HTTP 中是无效，在 HTTPS 中才有效 。
Path： 表示 在那个路由下可以访问到cookie。
httpOnly：是微软对 COOKIE 做的扩展。如果在 COOKIE 中设置了“httpOnly”属性，则通过程序（JS 脚本、applet 等）将无法读取到COOKIE 信息，防止 XSS 攻击的产生 。
singed：表示是否签名cookie, 设为true 会对这个 cookie 签名，这样就需要用 res.signedCookies 而不是 res.cookies 访问它。被篡改的签名 cookie 会被服务器拒绝，并且 cookie 值会重置为它的原始值。

app.get('/setcookie',function(req,res){
	res.cookie('isLogin','true');
	res.send('cookie设置成功')
})
获取cookie
app.get('/admin',function(req,res){
	console.log(req.cookies) //cookies 加S
	if(req.cookies.isLogin == 'true'){
		res.send('登录成功')
	}else{
		res.send('登录失败')
	}
})
```

cookie 加密
```
const express = require('express')'
const cookieParser =  require('cookie-parser');
var app = express();
app.use(cookieParser('secret'));

设置加密cookie
app.get('/setcookie',function(req,res) => {
   res.cookie('name','xiaoming',{signed:true});
})

获取界面cookie

app.get('/getcookie',function(req,res) => {
   let name = res. signedCookies.name;
})


自己加密
node 安全模块crypto
const crypto = require('crypto');

function jiami(str){
	let salt = "adfasdfasdf"
	let obj = crypto.createHash('md5');
	str = str + salt;
	obj.update(str)
	return obj.digest('hex')
}
let name = ‘小明’

let result = jiami(name) // 获取加密后的结果
3.9
```
####session
```
1 安装 npm install express-session
2 引入  const session = require('express-session');
3 配置
app.use(session({
httpOnly:true,
maxAge:1000*60*60
}))

3 使用

app.use('/setSession',(req,res) => {
req.session.name = "小明"
})


app.use('/getSession',(req,res) => {
console.log(req.session.name)
})

app.use('/destory',(req,res) => {
req.session.destroy(() => {
console.log('session 销毁成功')
})  
})
```
####文件上传
```
1 下载插件  var multer  = require('multer')

2 设置文件夹
var createFolder = function(folder){
    try{
        fs.accessSync(folder); 
    }catch(e){
        fs.mkdirSync(folder);
    }  
};

var uploadFolder = './upload/';

createFolder(uploadFolder);

/ 通过 filename 属性定制
var storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, uploadFolder);    // 保存的路径，备注：需要自己创建
    },
    filename: function (req, file, cb) {
        // 将保存文件名设置为 字段名 + 时间戳，比如 logo-1478521468943
        cb(null, file.fieldname + '-' + Date.now() + file.originalname);  
    }
});

// 通过 storage 选项来对 上传行为 进行定制化
var upload = multer({ storage: storage })

// 单图上传
register.post('/upload', upload.single('logo'), function(req, res, next){
    var file = req.file;
    console.log(file)
	res.send('上传成功')
});



```
Token生成
```
1 express 中 安装jsonwebtoken插件
// 引入插件	
var jwt = require('jsonwebtoken')
// 引入自己设置的字段
var config = require('../public/jwtConfig.js')


登录成功后 生成token 并返回前端

app.post('/login',(req,res) => {
	if(true){
		let token = jwt.sign({
			id:'1',
			username:'小明'
		},config.secret)
		res.send({data:token})
	}
})

前端接受token存储在localStorage.setItem('Token':res.data)

axios 请求头设置token
const axios.defaults.headers.common['Authorization'] = Token


前端接受token存储在localStorage.setItem('Token':res.data)

axios 请求头设置token
const axios.defaults.headers.common['Authorization'] = Token

存储在redux中
前端也可以用 jwt-decode 插件 解析出Token
import jwtdeocde from 'jwt-deocde'
  jwtdeocde(localStorage.Token) // id 1 username:小明
把他存在reudx中
```