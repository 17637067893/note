Koa2是现在最流行的基于Node.js平台的web开发框架，它很小，但扩展性很强。

####生成文件夹
```
midir studyKoa
cd studyKoa

npm init -y

cnm install Koa --save

npm install koa-generator -g
创建项目
koa2 bbs

在文件里新建app.js

const Koa = require('koa');
const app = new Koa();
app.use(async (ctx) => {
ctx.body = "返回数据"
})

app.listen("3000",() => {
console.log("服务start")
})


接着cmd中运行 node app.js
```
####get请求
在koa2中GET请求通过request接收，但是接受的方法有两种：query和querystring。

query：返回的是格式化好的参数对象。
querystring：返回的是请求字符串。
```
//ctx表示上下午，也就是js运行环境
app.use(async (ctx) => {
  let url = ctx.url
  //get请求的参数都保存在request中
  let request = ctx.request
  let re_query = request.query;
  let  re_querystring = request.querystring
 ctx.body ={
url,
re_query,
re_querystring
 }
})


//还可以省略 request 直接从ctx中获取get请求参数
let url = ctx.url
let re_query = ctx.query
let re_querystring = ctx.querystring
```
####POST请求处理
post请求接受的参数为字符串类型
```
const Koa  = require('koa');
const app = new Koa();
app.use(async(ctx)=>{
    if(ctx.url==='/' && ctx.method==='GET'){
        //显示表单页面
        let html=`
            <h1>JSPang Koa2 request POST</h1>
            <form method="POST" action="/">
                <p>userName</p>
                <input name="userName" /><br/>
                <p>age</p>
                <input name="age" /><br/>
                <p>website</p>
                <input name="webSite" /><br/>
                <button type="submit">submit</button>
            </form>
        `;
        ctx.body=html;
    }else if(ctx.url==='/' && ctx.method==='POST'){
        let pastData=await parsePostData(ctx);
         ctx.body=pastData;
    }else{
        ctx.body='<h1>404!</h1>';
    }

});

function parsePostData(ctx){
    return new Promise((resolve,reject)=>{
        try{
            let postdata="";
   //原生监听有数据传入 收集数据
        ctx.req.addListener("data",(data) => {
            postData += data
        })
        //监听没有数据传入 输出数据
        ctx.req.on('end',(res,err) => {
            if(err){
                reject(err)
            }else{
                let parseData = parseQueryData(postData)
                resolve(parseData)
            }
        })
       }catch(error){
            reject(error);
        }
    });
}


function parseQueryStr(queryStr){
    let queryData={};
    let queryStrList = queryStr.split('&');
    console.log(queryStrList);
    for( let [index,queryStr] of queryStrList.entries() ){
        let itemList = queryStr.split('=');
        console.log(itemList);
        queryData[itemList[0]] = decodeURIComponent(itemList[1]);
    } 
    return queryData
}

app.listen(3000,()=>{
    console.log('[demo] server is starting at port 3000');
});
```
#### Koa-bodyparser中间件
可以把我们post的请求参数保存在ctx.request.body中
```
1 安装 cnpm install koa-bodyparser

const bodybodparser =require('kok-bodyparser');
app.use(bodybodparser ())

if(ctx.url == '/' && ctx.method === 'POST'){
			let pastData= ctx.request.body;
			ctx.body = pastData;
}


```
####原生js实现路由功能
```
const Koa = require('koa');
const app new Koa();
const fs = require("fs")

app.use(async(ctx) => {
let url = ctx.request.url;
let html = await route(url);
ctx.body = htm;
})


function render(url) {
return new Promise((res,rej) => {
let pageUrl = `./page/${url}`;
fs.readFile(pageUrl,'binary',(err,data) => {
if(err){
throw
}else{
res(data)
}
})
})
}

async function route (url){
	let page = '';
    switch(url){
	   case '/todo':
	   page = 'todo.html'
	   break;
	   case '/404':
	   page = '404.html'
	   break;
	   case '/index':
	   page='index.html'
	   break;
	   default:
	   page="404.html"
	   break;
   }
   console.log(page);
   let html = await render(page);
   return html;
}

app.listen("3000",()=>{
	console.log('start')
})

```
##路由
```
const Router = require('koa-router');
cosnt router = new Router;

		 //启动路由
router
	.get('/', function (ctx, next) {
   	 ctx.body='Hello';
	})
	.post('/todo',(ctx,next)=>{
 	   ctx.body='Todo'
	 });

app
	.use(router.routes())
	 //allowedMethods用在routes之后，作用是根据ctx.status设置response header
	.use(router.allowedMethods())

app.listen('3000',(err) => {
	if(err) { 
			 throw err
	}
	 console.log('koa2 serve start')
})

```
## 路由实现层级
从低级到高级逐步嵌套进去
```
const Router = require('koa-router');
cosnt router = new Router;

let home = new Router();
		 //启动路由
home
	.get('/jspang', function (ctx, next) {
   	 ctx.body='home/jspang';
	})
	.get('/todo',(ctx,next)=>{

let page = new Router();	 
page
.get('/jspang', function (ctx, next) {
	 ctx.body='page/jspange';
})


//装载所有子路由
//第一级
router.use('/home',home.routes(),home.allowedMethods());
router.use('/page',page.routes(),page.allowedMethods());

let routerss = new Router();
           //   当前层级      上册路由   
   routerss.use("/gen",router.routes(),router.allowedMethods())
   
//加载路由中间件
app.use(routerss.routes()).use(routerss.allowedMethods());


app.listen('3000',(err) => {
	if(err) { 
			 throw err
	}
	 console.log('koa2 serve start')
})


```
## 路由传参
通过ctx.query
```
router
        .get('/', () => {
     let data = ctx.query
    ctx.body = data
})
```
cookie
```
写入
ctx.cookies.set('name','熊明',{
				domain:'localhost' ,//域名
				maxAge:'10*60*1000',//有效期
				expires:'2050-5-20',//失效时间
				overwrite:true // 是否允许重写
			})
```
模板引擎
 koa使用模板需要借助中间件 koa-views
** 编写模板 **

安装好ejs模板引擎后，就可以编写模板了，为了模板统一管理，我们新建一个view的文件夹，并在它下面新建index.ejs文件。
```
<!DOCTYPE html>
<html>
<head>
    <title><%= title %></title>http://jspang.com/wp-admin/post.php?post=2760&action=edit#
</head>
<body>
    <h1><%= title %></h1>
    <p>EJS Welcome to <%= title %></p>
</body>
</html>

```
编写koa文件
````
const Koa = require('koa')
const views = require('koa-views')
const path = require('path')
const app = new Koa()

// 加载模板引擎
app.use(views(path.join(__dirname, './view'), {
  extension: 'ejs'
}))

app.use( async ( ctx ) => {
  let title = 'hello koa2'
// index 是路径 后边根变量
  await ctx.render('index', {
    title
  })
})

app.listen(3000,()=>{
    console.log('[demo] server is starting at port 3000');
})
```
Koa访问静态资源需要koa-statice插件
```
就可以通过地址栏访问资源
const static = require('koa-static')
app.use(static(
  path.join( __dirname,  staticPath)
))

```