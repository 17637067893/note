#### 消息订阅发布

```
npm install pubsub-js
```

```react
消息发布者

PubSub.publishSync("user",{msg:"来电话了"})
```

在需要的地方接受消息

```react
PubSub.subscribe("user",(msg,data)=>{
  console.log(data);
})
```



#### 基本框架

```react
1 安装 react react-dom babel-standalone
//引入文件
<script src="./node_modules/react/umd/react.development.js"></script>
    <script src="./node_modules/react-dom/umd/react-dom.development.js"></script>
    <script src="./node_modules/babel-standalone/babel.min.js"></script>
</head>
<body>
    <!-- 安装 react  react-dom babel-standalone  -->
渲染节点
<div id="app"></div>    
<script type="text/babel">
let dom = <h2>hello</h2>

使用reactDOM.render 渲染函数
ReactDOM.render(dom,document.getElementById('app'))

</script>
</body>
```
#### props类型验证

 ```react
import React from 'react';
import propTypes from 'prop-types'

export default class PropsTypeDemo extends React.Component{
    //构造器 需要通过this.props访问prop需要创建 否则不需要构造器
    constructor(props){
        super(props)
        this.state = {
            num:'6'
        }
    }
    // 第二种 复用代码
     static propTypes = {
        number:propTypes.number.isRequired
    }
    render(){
        return(
            <div>
                {this.props.val}
            </div>
        )
    }
}
//第一种 接受的类型
PropsTypeDemo.propTypes = {
    val:propTypes.string,
    //毕传
    // val:propTypes.number.isRequired
}
如果没有传值使用默认值
PropsTypeDemo.defaultProps = {
    val:'默认值'
}
 ```
##### 组件父子传值

```
import React,{ Component } from 'react';

class Father extends Component{
	//定义组件状态
	constructor(props) {
	    super(props)  //继承绑定在当前组件上的属性
		this.state = {
			info:'自定义组件的状态'
		}
	}
	render(){
		return(<div className='father' style={{fontSize:'20px',color:'red'}} >
		这是父亲组件
		{this.props.send}
		<div>{this.props.txt}</div>
		<div>{this.state.info}</div>
		</div>)
	}
}


父组件引用时
<Father txt="666666" ></Father>
```
子传父
```
父组件定义函数 传给子组件
fn=(val)=>{
		this.state.msg += val;
		console.log(this.state.msg)
	}
	render(){
		return(
		  <div className="App">
			<Father send={this.fn} ></Father>
		  </div>
		)
	}

//子组件调用并传值
<button onClick = {()=>{this.props.send(6)}}>改变颜色</button>


<button onClick = {this.change}>改变颜色</button>
change=() => {
		 this.props.send(1);
		console.log(this.props)
	}
```

#### React的this问题

```react
import React from "react";

class Student extends React.Component{
    constructor(){
        super();
        this.state={
            name:"炎热",
            status:true
        }
        //在构造函数内部声明demo变量指向 change的堆内存
        // 这this.change为Student实例上没有change函数根据原型链 找到构造函数的change 再把demo变量的地址指向构造函数的change
        console.log(this);
        this.demo = this.change.bind(this);
    }
    change(){
        //这个change函数虽然在Student类内部单，但是他绑定的在onClick函数回调上，
        // 当我们点击的时候为window调用的由于react方法内部使用js的严格模式随意this为undinfined
        console.log(this);
    }
    render(){
        return(<h1 onClick={this.demo}></h1>)
    }
}

export default Student;
```



#### 控制元素显示隐藏

```js
class Father extends Component{
	//定义组件状态
	constructor(props) {
	    super(props)  //继承绑定在当前组件上的属性
		this.state = {
			info:'自定义组件的状态'
		}
	}
	change=()=>{
		
	}
	render(){
		let strClass=null
		if(this.props.send=='0'){
			strClass = ' hiden'
		}else{
			// active前加空格
			strClass = ''
		}
		return(
		<div className={"content" + strClass}>798</div>
		</div>)
	}
}
```
#### 绑定事件

1 通过OnXxx属性指定时间处理函数

2 使用的是自定义疯转底层是事件

3 通过event.target得到发生事件的DOM元素对象

```react
<button onClick = {this.change.bind(this)}>改变颜色</button>
change(){
    console.log(666)
}

<button onClick = {this.change}>改变颜色</button>
change=() => {
    console.log(666)
}
```
##### 条件渲染

```react
class Second extends Component{
    constructor(props){
        super(props)
        this.state={
            txt:"789",
            status:false
        }
    }
    render(){
        let dom=null;
        if(this.state.status){
            dom = (<Login></Login>)
        }else{
             dom = (<LoginOut></LoginOut>)
        }
        return (<div>
            {dom}
//也可以直接用三元运算符
{this.state.status ? <Login></Login> : <LoginOut></LoginOut>}
        </div>)
    }
}
```
#### 列表渲染

```react
show(index){
    console.log(index)
}
render(){
    let listArr = this.state.arr.map((Element,index) => {
        let ele = <div style
                      height:'30px',backgroundColor:'red'}}  onClick={()=>{this.show(Element)}} key={index}>{Element.item}</div>;
        return ele
    })
    return(<div>
            {listArr}
        </div>)
}
```
#### 发送请求

```
  componentDidMount(){
        axios.get('url')
    }
```

#### 路由

![image-20210524222913923](G:\note\image\image-20210524222913923.png)

```react
Router         总路由

link           路由导航
----to         切换的链接

Route          路由
----path       对应链接
----component  对应组件
NavLink   选中导航栏添加active class
activeClassName="selected" 更改选中class
exact     精确匹配路径
strict      严格模式，" / "

import React from 'react';
import {BrowserRouter as Router, Route ,Link} from 'react-router-dom'

function coma(props){
    // 接受单个参数
    console.log(props.match.params)
    return(<div>
        coma
    </div>)
}
function comb(props){
    // 接受对象
    console.log(props.location.query)
    return(<div>
        comb
    </div>)
}



class Home extends React.Component{

    change=()=>{
    }
    render(){
        return(<div>
            <Router>
                {/* 传递单个参数 */}
                <NavLink to="/coma/20" >跳转A</NavLink>
                {/* 传递对象 */}
                  query传递参数页面刷新丢失
                  state 页面刷新参数保存
                <NavLinkto={{pathname:'/comb',query:{a:'1',b:'2'}}} >跳转B</NavLink>
                       代表参数可有可无 不会报404
               <Route exact path='/coma/:id?' component={coma} />
               <Route exact path='/comb' component={comb} />
      <Route component={NotFound}></Route>
            </Router>
        </div>)
    }
}


export default Home;

//参数接受
 <NavLink activeClassName="selected" exact to="/page1?a=1&b=2">page1</NavLink>

[http://localhost:3000/page1?a=1&b=2](http://localhost:3000/page1?a=1&b=2)

  let param = new URLSearchParams(props.location.search)
可以获取指定的参数
  console.log(param.get("a"))

let param = querystring.parse(props.location.search);
  console.log(param)  {?a: "1", b: "2"}


重定向
可以给用户去跳转页面
 <Redirect from="/amin" to="/page2"></Redirect>

```
#### 路由跳转之后的props 可以使用
跳转
this.props.history.push("/page2",{a:'ssss'})
![image.png](https://upload-images.jianshu.io/upload_images/16514325-e51e685b7704dcb8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
离开页面前的提示

```react
<Prompt when={!!this.state.val} message={"有数据没有保存"}></Prompt>
                <input type="text" value={this.state.val} onChange={(e)=>{this.setState({val:e.target.value})}} name="" id=""/>
```

setState
```react
如果要更新视图必须调用setState
 <button onClick={this.change}>用户名</button>
                    <input value={this.state.title} placeholder="用户名"></input>

 change=()=>{
        //不会更新页面
         this.state.title="你好"
         //正确会更新页面
        this.setState({
            title:"你好吗"
        })
    }



 add=()=>{
        this.setState({
            num:this.state.num+1
        },()=>{
            //可以获取当前的值
            console.log(this.state.num)
        })
        //获取不到当前的值，只能获取上次的值
        console.log(this.state.num)
    }
```

#### ref 获取元素

```react
import React from 'react';

export default class formDom extends React.Component{
    constructor(props){
        super(props)
        //  绑定this
        this.inputDom = React.createRef();
    }
    
    componentDidMount(){
        console.log(this.inputDom.current.style.fontSize="25px")
        console.log(this.inputDom .current)
    }
    render(){
        return(<div>
                    {/* 绑定元素 */}
                <input ref={this.inputDom}></input>
        </div>)
    }
}
```
#### 受控组件()

```js
元素的值来自于state 如要想要各边值要写对应的数据调用setState

import React from 'react';

export default class formDom extends React.Component{
    constructor(props){
        super(props)
        this.state = {
            value:'默认'
        }
    }
    sub = (e)=>{
        e.preventDefault();
        // 组织冒泡
      //  e.stopPropagation()
        console.log("sub")
        console.log(this.state.value)
    }
    change =(e)=>{
        // 调用setState
        this.setState({
            value:e.target.value
        })
    }
    render(){
        return(<div>
            <form onSubmit={this.sub}>
                <button > 提交</button>
                {/* 绑定onChange */}
                <input onChange={this.change} value={this.state.value}></input>
            </form>
        </div>)
    }
}
```
#### 非受控组件

```js
不用绑定onChange 调用setState
import React from 'react';

export default class formDom extends React.Component{
    constructor(props){
        super(props)
        //  绑定this
        this.inputDom = React.createRef();
    }
    getValue=()=>{
        // 通过ref获取元素的属性
        console.log(this.inputDom.current.value)
    }
    render(){
        return(<div>
                    {/* 元素的值可以随意改变 */}
                <input ref={this.inputDom}></input>
                <button onClick={this.getValue} >按钮</button>
        </div>)
    }
}

```
##### 跨域解决

```js
1 原生js jsonp

<script>
    var script = document.createElement('script');
    script.type = 'text/javascript';

    // 传参一个回调函数名给后端，方便后端返回时执行这个在前端定义的回调函数
    script.src = 'http://www.domain2.com:8080/login?user=admin&callback=handleCallback';
    document.head.appendChild(script);

    // 回调执行函数
    function handleCallback(res) {
        alert(JSON.stringify(res));
    }
 </script>

2 项目设置代理
开发环境
1 package  添加   "proxy":"http://iwenwiki.com"
// 就可以请求
Axios('/api/FingerUnion/list.php')
.then(res => {
    this.setState({
        list:res.data.data
    })
})


3 设置中间件
1 安装   http-proxy-middleware

src下设置  setupProxy.js
const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
    app.use('/api', createProxyMiddleware({
        target: 'http://iwenwiki.com',
        changeOrigin: true,
    })
    );
};


Axios('/api/FingerUnion/list.php')
.then(res => {
    this.setState({
        list:res.data.data
    })
})
```
#### 请求封装

```js
src下创建
utils/https.js

const axios = require('axios');
const qs = require('qs');

export function httpGet(url){
return axios.get(url)
}


z
export function httpPost (url){
const result = axios(url,{
methos:"POST",
headers:{
"Content-Type":'application/x-www-form-urlencoded',
'Accept':'application/json,text/plain,*/*'
},
body:qs.stringify(params)
})
return result
}


创建 api/index
import {httpGet,httpPost} from '../utils/https'
let baseurl = "http://iwenwiki.com";
const api = {
  getList(url){
   return httpGet(baseUrl + url)
}
}
export default api
```

#### 组件优化

```react
<h2>组件优化</h2>
<h3>1 路由有事件监听 定时器 离开页面应把事件清除掉</h3>
<h2>2 父组件内使用setState导致页面重新渲染子组件也会跟着渲染
    解决 在子组件内使用 
    shouldComponentUpdate(nextProps,nexState){
        if(nextProps.num == this.props.num){
            return false
        }else{
            return true
        }
    }
</h2>

Component 不会对数据比较
PureComponent 会对数据进行比较渲染页面
```
#### 高阶组件

```js
import React from 'react';

// 高阶组件 ：参数是组件返回值也是组件的函数
const withFetch = (ComposeComponent) =>{
    return class extends React.Component{
        render (){
            return(
                <ComposeComponent {...this.props}></ComposeComponent>
            )
        }
    }
}


class Demo extends React.Component{
    render(){
        return(
            <div>
               {this.props.data}
            </div>
        )
    }
}

const WithFetch = withFetch(Demo)
class Demo4 extends React.Component{
    render(){
        return(
            <div>
                <WithFetch data={"小明"}></WithFetch>
            </div>
        )
    }
}

export default Demo4;
```
#### 高阶组件的应用

```js
import React from 'react'; 
export const withFetch = (url) => (View) => {
    return class extends React.Component{
        constructor(props){
            super(props);
            this.state = {
                loading:true,
                data:null
            }
        }
        componentDidMount(){
            fetch(url)
            .then(res => res.json())
            .then(data => {
                this.setState({
                    loading:false,
                    data:data
                });
            })
        }
        render(){
            if(this.state.loading){
                return(
                    <div>loadding</div>
                )
            }else{
                return <View data = {this.state.data}></View>
            }
        }
    }
}




import React from 'react';
import withFetch from './withFetch'

//第二个参数出入一个视图
const Banner = withFetch("www")(props => {
    return(
        <div>
            <p>
                {props.data.Banner[0].title}
            </p>
        </div>
    )
})

export default Banner



```
#### 错误处理

```react
import React from 'react';

export default class ErrorBoundary extends React.Component{
    state = {
        hasError:false,
        error:null,
        errorInfo:null
    }
    // 子元素发生错误时触发
    componentDidCatch(error,errorInfo){
        this.state({
            hasError:true,
            error:error,
            errorInfo:errorInfo
        })
    }
    render(){
        if(this.state.hasError){
        return <div>{this.props.render(this.state.error,this.state.errorInfo)}</div>
        }
        return this.props.children
    }
}


包裹组件发生错误时 显示错误信息
<ErrorBoundary render = {(error,errorInfo) => <p>{'组件发生错误'}</p>}>
<Demo></Demo>
</ErrorBoundary>
```

##### 权限登录

```js
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

存储在redux中
前端也可以用 jwt-decode 插件 解析出Token
import jwtdeocde from 'jwt-deocde'
  jwtdeocde(localStorage.Token) // id 1 username:小明
把他存在reudx中

利用高阶组件实现权限

import react from 'react';
import {connect} from 'react-redux';

import {withRouter} from 'react-router-dom';

export default function(ComposedComponent){
	class Authenticate extends React.Component{
		componentWillMount(){
			//判断是否登录
			if(!this..isLogin){
				this.props.history.push('/login')
			}
		}
	}
	componentWillUpdate(nextProps){
		if(!nextProps.isLogin){
			this.props.history.push('/login')
		}
	}
	render(){
		return{
			<ComposedComponent {...this.propss}></ComposedComponent>
		}
	}
	const mapStateToProps = (state) => {
		return {
			isLogin:state.isLogin
		}
	}
	return  withRouter(connect(mapStateToProps,null)(Authenticate))
}




路由使用
Info组件

<Route path="/info" componet={Authenticate(Info)}></Route>
```
#### 上拉加载更多

```react
getboundingclientrect().top获取元素距离顶部的距离
//获取视图的高度
const winHeight = document.documentElement.clientHeight
//加载更多距离顶部的距离
const ItemTop = this.load.current.getboundingclientrect().top


if(ItemTop < winHeight){
请求数据
}
组件封装




import React from "react"

export default class LoadMore extends React.Component {

    constructor() {
        super();
        this.load = React.createRef();
    }


    /**
     * 监听页面滚动事件
     */

    componentDidMount() {
        const _this = this;
        let timer = null;
        // 获取页面高度
        const winHeight = document.documentElement.clientHeight;
        window.onscroll = function (event) {
            // getBoundingClientRect:对象
            if (timer) {
                clearTimeout(timer);
            }
            timer = setTimeout(function(){
                if (_this.load.current.getBoundingClientRect().top < winHeight) {
                    // 该加载数据的时候
                    _this.props.onLoadMore();
                }
            },100)
        }

    }

    render() {
        return (
            <div ref={this.load}>
                加载更多
            </div>
        )
    }
}


使用时

子传父事件
<LoadMore   onLoadMore={外部请求数据事件}></LoadMore >
```
#### 数据还在请求的时候

```
我们跳转到其他页面！导致接受的数据的data不存在会保存
所以在
componentWillUnMount(){
    this.setState = (state,callback) => {
   return;
 }
}
```
#### 选中元素切换样式

```react
import React from "react"
import "./style.less"

export default class Tabs extends React.Component {

    constructor(){
        super();
        this.state = {
            currentIndex:0
        }
    }

    /**
     * 内容非固定，获取具体的div结构
     *     参数：<div></div>
     * 
     * <tabs>
     *     <tab tabname="标题1">
     *         <div></div>
     *     </tab>
     *     <tab tabname="标题2">
     *         <div></div>    
     *     </tab>
     * </tabs>
     * 
     * 获取结构的方案:props.children
     * 
     * <div>标题<div>
     * <div>内容</div>
     * 
     */

    check_title_index(index){
        // 0 1
        return index === this.state.currentIndex ? "Tab_title active" : "Tab_title"
    }   

    check_item_index(index){
        return index === this.state.currentIndex ? "show" : "hide"
    }

    tabHandler(index) {
        this.setState({
            currentIndex:index
        })
    }

    render() {
        return (
            <div>
                <div className="Tab_title_wrap">
                    {
                        React.Children.map(this.props.children, (element, index) => {
                            return (
                                <div className={this.check_title_index(index) } onClick={ this.tabHandler.bind(this,index) }>
                                    {element.props.tabname}
                                </div>
                            )
                        })
                    }
                </div>
                <div className="Tab_item_wrap">
                    {
                        React.Children.map(this.props.children, (element, index) => {
                            return (
                                <div className={ this.check_item_index(index) }>{element.props.children}</div>
                            )
                        })
                    }
                </div>
            </div>
        )
    }
}
```
#### redux

安装redux react-redux redux-thunk

创建store

````js
import {createStore,applyMiddleware} from "redux";
import {composeWithDevTools} from "redux-devtools-extension"
import reducer from "./reducer.js";
import thunk from "redux-thunk";
//根据reducer床架store
const store = createStore(reducer,composeWithDevTools(applyMiddleware(thunk)));
export default store;
````

2创建store

```js

// reducer接受之前的状态 state 修改state的type
const initState={
    number:10
}
const reducer = function(state=initState,action){
    console.log('====================================');
    console.log(state,action);
    console.log('====================================');
    switch(action.type){
        case "add":
            return {...state,number:state.number+action.data}
        case "reduce":
            return {...state,number:state.number-action.data}    
        default:
            return state;    
    }
}
export default reducer;
```

3 创建action

用于修改store的数据

```js

// 生成action对象 = store.dispatch('action', payload)

//同步任务 返回一个对象
export const createIncrementAction= (data)=>({type:"add",data});


export const reduceIncrementAction= (data)=>({type:"reduce",data});

//异步action返回一个function 不是一个对象没有type action属性 store没法处理需要中间件 redux-thunk
export const reduceIncrementAsyncAction= (data)=>{
    return (dispatch)=>{
        setTimeout(()=>{
            dispatch(reduceIncrementAction(data))
        },1000)
    }
}

```

根组件传入store

```js
import { Provider } from "react-redux";
<Provider store={store}>
      <App />
  </Provider>
```

在组件里修改数据

```react
import React from 'react';
import {connect} from "react-redux"
import {createIncrementAction,reduceIncrementAction} from "../redux/actions.js"
class About extends React.Component{
    constructor(props){
        super(props);
        console.log(this.props);
    }
    render(){
        return(
            <>
             <h2>{this.props.obj}</h2>
             <button onClick={()=>{this.props.add(1)}}>add</button>
             <button onClick={()=>{this.props.reduce(1)}}>reduce</button>
            </>
           
        );
    }
}

//state的属性映射到props里
const mapStateToProps = (state,props)=>{
    return{
        obj:state.number
    }
}

//把 dispatch映射到props上
// const mapDispatchToProps = (dispatch,props)=>{
//     return{
//         add:(data)=>dispatch(createIncrementAction(data)),
//         reduce:(data)=>dispatch(reduceIncrementAction(data))
//     };
// }
//简写
const mapDispatchToProps={
    add:createIncrementAction, //提供一个acton 内部自动dispatch分发
    reduce:reduceIncrementAction
}
export default connect(mapStateToProps,mapDispatchToProps)(About);
```





 

#### react HOOK

###### useState

```react
强化了 函数式组件，是函数式组件也可以有状态

第一 useState
import React,{useState} from 'react';


function App() {
  const [number,setNumber] = useState(0)
  function reduce(){
    // 修改为1000
    // setNumber(1000)
    //数值加+1
    setNumber(number+1)
//数组的话
setArr([arr,...arr1])
  }
  return (
    <div className="App">
      <div>number:{number}</div>
      <button onClick={()=>setNumber(number+1)}>增加</button>
      <button onClick={reduce}>减少</button>
    </div>
  );
}

export default App;
```
######  Effect
```react
//相当于 componentDidMount
useEffect(()=>{

},[])
//相当于 componentDidMount  componentDidUpdate
useEffect(()=>{
})

相当于 componentDidMount 只有count发生变化才会执行componentDidUpdate
useEffect(()=>{

},[count])


//相当于 componentDidMount componentDidUpdate componentWillUnMount
useEffect(()=>{

    return function(){
     相当于 componentWillUnMount
    }
})

使用

import React,{useState,useEffect} from 'react';

// 无副作用
function DemoEffect (){
    const [count,setCount] = useState(10)
    // const [timer,setTimer] = useState(null)
    //useDeffect 代替 componentWillMount  componentDidUpdate
    useEffect(()=>{
        let  timer = setTimeout(()=>{
            setCount(count + 1)
        },1000)
        console.log(count)
        //清除 定时器 事件 请求
        return function(){
            clearTimeout(timer)
        }
    })
    function changCount(){
        setCount(count + 1)
    }
    return(
        <div>
            <button onClick={changCount}>按钮</button>
            {count}
        </div>
    )
}

export default DemoEffect;
传入数据

import React,{useState,useEffect} from 'react'

const useInputValue = (initalValue) =>{
    const [value,setValue] = useState(initalValue);
    return{
        value,
        onChange:e => setValue(e.target.value);
    }
}

//传入数据
const TodoForm = ({eventHandle}) => {
    const text = userInputValue('');
    const password = userInputValue('');
    function onSubmitHandler(){
        eventHandle(text)
    }
    return (
        <form onSubmit={onSubmitHandler}>
            <input type="text" {...text}/>
            <input type="text" {...password} />
        </form>
    )
}
```
###### useCallback

```react

import React,{useState,useCallback} from 'react';

const Demo1 =() => {
    const [count,setCount] =  useState(0)
    const [count1,setCount1] = useState(0)
    return(
        <div>
            <div>count   ==={count}</div>
            <div>count1 ==  {count1}</div>
            <button onClick={()=>{setCount(count+1)}}>按钮</button>
            {/*   */}
            <button onClick={useCallback(()=>{setCount1(count1+1)},[count])}>按钮1</button>
        </div>
    )

}

export default Demo1

```

###### useReducer的使用

```react
import React,{useState,useReducer} from 'react';

const initState = {
    count:0
}

const reducer = function(initState,action){
    switch(action.type){
        case 'add':
            return {count:StaticRange.count + 1}
            break
        case 'decrement':
            return {count:StaticRange.count - 1}
        default:
            throw Error();        
    }
}

function Counter(){
    const [state,dispatch] = useReducer(reducer,initState);
    return(
        <div>
            Count {state.count}
            <button onClick={()=>{dispatch({type:'add'})}}>+</button>
            <button onClick={()=>{dispatch({type:'decrement'})}}>-</button>
        </div>
    )
}

export default Counter
```

###### context

跨越组件层级直接传递变量，实现共享

```js
父组件
import React, { useState , createContext } from 'react';

const CountContext = createContext()

function Example(){

    const [ count , setCount ] = useState(0);

    return (
        <div>
            <p>You clicked {count} times</p>
            <button onClick={()=>{setCount(count+1)}}>click me</button>
    
            <CountContext.Provider value={count}>
            </CountContext.Provider>

        </div>
    )
}
export default Example;


子组件
import React, { useState, useContext } from 'react';

function Counter(){
    const count = useContext(CountContext)    //一句话就可以得到count
    return (<h2>{count}</h2>)
}
```

![image-20210525082108388](G:\note\image\image-20210525082108388.png)

#### JSX 本质

```
jsx JSX的本质即React.createElement函数，React.createElement 即 h 函数，根据标签和属性,子节点解析返回vnode 然后通过patch挂载到对应的节点
```

#### 合成事件

```properties
DOM事件会冒泡到document上，document会生成一个统一的React event，在合成事件层会派发事件（通过target可以获取到哪个元素触发的事件，找到这个元素所在组件这个）

为什么要合成事件机制？

更好的兼容性和跨平台
挂在到document，减少内存消耗，避免频繁解绑
方便事件的统一管理（如事物机制）
```

#### redux-thunk

```js
1、redux-thunk 重写了 store.dispatch

2、并且内部保存了 原来的 store.dispatch

3、当你调用了 store.dispatch 的时候

4、通过判断你 调用 dispatch 传入的参数类型来判断要进行什么操作

5、当你传入的是函数，那么就执行你传入的函数，并把 重写后的 dispatch 继续传给你，让你可以继续套娃

6、当你传入的是对象，那么就直接调用 原dispatch，然后传入的你对象，完成 reducer 操作
```

