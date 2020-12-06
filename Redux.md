redux组成
 State 状态
1 服务器返回的state
2 当前组件的state
3 全局的state

Action 
1 本质是一个JS对象
2 必须包含type属性
3 只是描述了有事情发生，并没有描述如何去更新state

Reducer
1 本质是函数
2 响应发送过来的action 
3 接受两个参数第一初始化state 第二 发送过来的action 
4 必须有return 返回值

Store
把action 和reducer关联到一起
通过createStore 来构建store
通过subcribe来注册监听

1 创建仓库
```
import {createStore} from 'redux';
import reducer from './reducer'
const store = createStore(reducer,window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());

在需要的页面引入
export default store



2 创建reducer
//初始化的state
const defaultState = {
    list:[
        {a:'A',b:'2'},
        {a:'A',b:'2'},
        {a:'A',b:'2'},
        {a:'A',b:'2'}
    ],
    fn:{}
};

export default(state = defaultState,action) => {
    console.log(action)
    switch(action.type){
        case 'add':
            state.list.push(action.obj)
            break;
        case 'delete':
             state.list.pop();
            break;
    }
//结构对象 返回新的 和原来不是同一个
    return {...state}
}


3 页面使用
  add = ()=> {
        store.dispatch({type:'add',obj:{a:'8',b:'28'}})
    //调用页面渲染
        this.setState({})
    }
    delete=() => {
        store.dispatch({type:'delete'})
//调用页面渲染
        this.setState({})
    }
```
















React-redux 概述
1 Provider
 ```
Provider 包裹在跟组件最外层，使所有的子组件都可以拿到state

Provider 接受store最为props，然后通过context往下传递，这样任何组件都可以通过context获取到store
 ```
react-redux

```
1 安装 react-redux
2 跟组件包裹传值  不用每个组件都要引入store仓库
import {Provider} from 'react-redux';
import store from './store/index'

   <Provider store={store}>
           <ReduxExampler></ReduxExampler>
      </Provider>
3 需要使用store的组件

import {connect} from 'react-redux'
let _this;

import React from 'react';
import {connect} from 'react-redux'
let _this;
class ReduxExampler extends React.Component{
    constructor(props){
        super(props)
        //设置全局_this
        _this=this
        this.state = {
        }
    }

    render(){
  
        let arr=[]
        // 用this.props代替this.state
        this.props.list.forEach((element,index) => {
            let ele = <div key={index}>
                {element.a}
                {element.b}
            </div>
            arr.push(ele)
        });
        console.log(arr)
        return(<div>
               {/* 用this.props.add代替分发事件 */}
            <button onClick = {this.props.add}>++</button>
            <button onClick = {this.props.delete}>---</button>
            <button onClick = {this.props.show}>输出</button>
           <div>{arr}</div>
        </div>)
    }
}
// state映射到props
const stateToProps = (state) => {
    return{
//映射list
        list:state.list
    }
}
// dispatch映射到props
const dispatchToProps = (dispatch) => {
    return {
        add:()=> {
        dispatch({type:'add',obj:{a:'8',b:'28'}})
        _this.setState({})
    },
    delete:() => {
        dispatch({type:'delete'})
        _this.setState({})
    },
    show:()=>{
        dispatch({type:'show'})
    }
    }
}

export default connect(stateToProps,dispatchToProps)(ReduxExampler)


```
中间件
```
// 引入函数   
import {createStore,applyMiddleware} from 'redux';
import reducer from './reducer'



//中间件
const logger = store => next => action => {
    console.log('dispatch->',action);
    let result = next(action);
    console.log('next state ->',store.getState());
    return result;
  }
  
let store = createStore(reducer,applyMiddleware(logger))

export default store
```