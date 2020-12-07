#### 什么是服务端渲染

通过服务端响应把HTML节点前端，由浏览器解析HTML节点生成页面

#### 流行框架有什么问题

Vuejs,ReactJs,SPA(单页面应用),首屏加载慢，SEO

#### 为什么需要服务端渲染

国内搜索引擎只能解析HTML，不能解析js。

国外谷歌搜索引擎可以解析js

#### 简单介绍

1. 直观的基于页面的路由系统(支持动态路由)
2. 在可能情况下自动静态优化页面
3. 自动拆分可加快页面加载速度
4. 内置css支持，并支持任何CSS-in-JS库
5. 基于Webpack的开发环境,支持热模块替换

#### 新建项目

安装依赖

```
yarn add next react react-dom
```

#### 添加启动命令

```
"script":{
"dev":"next -p 8080",
"build":"next build",
"start":"next start"
}
dev 以开发模式运行Nextjs   -p 8080 可以修改端口
build 运行打包
start 服务端启动Nextjs
```

#### pages文件夹下自动分配路由

```
pages/index.js   http://localhost
page/cmp/new.js  http://localhost/cmp/new.js
```

#### publick 存在资源![image-20201207120344825](G:\note\image\image-20201207120344825.png)

```
 资源默认路径为public下
<img src="./image/bg.jpg" alt=""/>
```

#### 脚手架生成项目

````js
yarn create next-app
npx create-next-app
后面会让输入项目名称
````

#### Link跳转

href 添加跳转路径(默认history的push模式)

可以是字符串"/about?abc=123"

```javascript
 import Link from 'next/link'
 <Link href="/about" as="aboutttt" replace>
  <a>about</a>
 </Link>
```

可以是对象

```javascript
import Link from 'next/link'
<Link href={{pathname:'/about',query:{name:'abc'}}}>
<button>index</button>
</Link>
```

#### React.forwardRef

```jsx
import Link from 'next/link'
import React,{useState} from 'react' 

const RedLink = React.forwardRef((props,ref)=>{
  return (
    <a href={props.href}  ref={ref}>click me</a>
  )
})

export default ()=> {
  const [count,setCount] = useState(0);
  return (
    <div>
      <Link href="/about" passHref >
        <RedLink></RedLink>
      </Link>
    </div>
  )
}
```

#### js跳转

useRouter

```javascript
import {useRouter} from 'next/router';
export default ()=>{
  const router = useRouter();
   return (
     <div>
       <h1>index</h1>
       {/* 字符串 */}
       <button onClick={()=>{router.push('/about')}}>about</button>
       <button onClick={()=>{router.push({pathname:'/about'})}}>about</button>
     </div>
   )
}
```

Router

````javascript
import Router from 'next/router';
export default ()=>{
   return (
     <div>
       <h1>index</h1>
       {/* 字符串 */}
       <button onClick={()=>{Router.push('/about')}}>about</button>
       <button onClick={()=>{Router.push({pathname:'/about'})}}>about</button>
     </div>
   )
}
````

withRouter

```jsx
import {withRouter} from 'next/router';
const Index= ({router})=>{
  let goAbout = function(val){
    console.log(val)
  }
   return (
     <div>
       <h1>index</h1>
       {/* 字符串 */}
       <button onClick={()=>{goAbout(6666)}}>about</button>
       <button onClick={()=>{router.push({pathname:'/about',query:{name:'小明'}})}}>about</button>
     </div>
   )
}
export default withRouter(Index)
```

#### 接受参数

```
获取路由对象，router.query
import Router from 'next/router'
export default ()=>{
  console.log(Router.query)
   return (
    <div>
        <h1>about</h1>
    </div>
  )
}
```

#### 预加载(生产模式有效)

 1 预加载是性能优化技术

2 所有需要的资源提前请求加载到本地，以后从缓存读取资源

方式一  Link 的属性prefetch

```
<Link href="/" prefetch>
   <a>pageIndex</a>
</Link>
```

方式二 Router的方式

```
Router.prefetch('/about')
```

#### css引入

全局引入

```
import "./index.css"
```

模块引入

```
import css from './index.module.css'
<H2 className={css.color}></h2>
```

#### less引入

```
yarn add @zeit/next-less less
```

在根路径创建配置文件next.config.js

```
//配置后重启项目
const withLess = rquire("@zeit/next-less")
module.exports = withLess({

})
```

组件

```
import './style.less'

<div className="example"></div>
```

模块用法

next.config.js

````
//配置后重启项目
const withLess = require('@zeit/next-less')
module.exports = withLess({
cssModules:true
})
````

组件

```
import css from './style.less'
<div className={css.xx}></div>
```

