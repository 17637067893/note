#### 声明周期

![image-20210604105334738](G:\note\image\image-20210604105334738.png)



#### vite与webpack

vite 相对于 webpack块的原因

webpack把所有模块打包成bundle里 

vite 按需打包，请求具体模块 才会打包相应资源



#### 安装项目

```properties
yarn create @vitejs/app <project-name>
cd <project-name>
yarn
yarn dev
```

#### setup

setup 不能使用 this, data

里面可以组合方法和声明周期，防止页面过长时在data,computed methods寻找数据不方便 在beforeCreated之前执行 

###### 1 基本数据处理

```js
import {reactive,computed,onMounted, onUnmounted} from 'vue'
setup(){
    // reactive存放响应数据
   const user = reactive({
     name:"小明",
      age: 20,
      //计算属性
      doubleAge:computed(()=> user.age*10),
      sum:1
    })
    // 其他逻辑处理
    let timer;
    onMounted(()=>{
      timer = setInterval(()=>{
        user.sum++;
      },1000);
    })
    onUnmounted(()=>{
      clearInterval(timer);
    })
    
   // 所有数据的必须return
    return {user};
  }
```

###### 2 抽离业务

```js
import {reactive,computed,onMounted, onUnmounted} from 'vue'

export default{
  props: {
    msg:String
  },
    
  setup(){
      //引入user处理
    const user = userCounter();
    return {user};
  }
}

//抽出user相关的逻辑处理
function userCounter(){
   const user = reactive({
     name:"小明",
      age: 20,
      doubleAge:computed(()=> user.age*10),
      sum:1
    })
    // 其他逻辑处理
    let timer;
    onMounted(()=>{
      timer = setInterval(()=>{
        user.sum++;
      },1000);
    })
    onUnmounted(()=>{
      clearInterval(timer);
    })
    return user;
}
```

#### ref函数

ref 和 reactive 一样都是实现响应式数据的方法

由于 reactive 必须传递一个对象，所以导致我们再企业开发中，如果我们只想让某个变量实现响应式的时候非常麻烦，所以 Vue3 就提供了 ref 方法，实现对简单值的监听

ref 底层的本质还是 reactive 系统会自动根据我们给 ref 传入的值将他转换成 ref(xx) —— reactive({value: xx})

```
1、在 VUE 的模板中使用 ref 的值不需要通过 value 获取 （Vue 会通过自动给 ref 的值加上 .value）

2、在 js 中使用 ref 的值必须使用 .value 获取
```



######  1 引入ref

```
import { ref } from 'vue'
```

2 测试

```html
 <div>
     <h2>定时器:{{sum}}</h2>  
 </div>
```



```properties
 setup(){
    //导入user
    const user = userCounter();
    let count = ref(0); //单值具有响应式 改变count的值时
    
    // return {...user};  直接解构赋值 数据没有响应式特性 定时器改变sum的值页面不发生改变
    
    return {...toRefs(user),count};
  }
  
  
  //抽出user相关的逻辑处理
function userCounter(){
   const user = reactive({
     name:"小明",
      age: 20,
      doubleAge:computed(()=> user.age*10),
      sum:1
    })
    // 其他逻辑处理
    let timer;
    onMounted(()=>{
      timer = setInterval(()=>{
        user.sum++;
        console.log(user.sum)
      },1000);
    })
    onUnmounted(()=>{
      clearInterval(timer);
    })
 
    return user;
}
```

3 结合DOM ref给元素赋值

```properties
<h2 ref="des"></h2>

 setup(){
    let {sum} = userCounter();
    
    // 变量名和ref内的值相同
    const des = ref(null);

    watch(sum,(newVal,oldVal)=>{
      //给元素赋值
      des.value.textContent = newVal+oldVal
      console.log(newVal+"==>"+oldVal);
    })
    
    return {sum,des}
 }

```

#### teleport 传送门

可以把我们写好的弹框 消息提示 传送到我们需要的位置显示

需要弹框的组件

```html
<template>
  <div>
    <h2>teleport挂载元素</h2>
      需要弹框的位置
    <div id="toast"></div>
  </div>
  <ModalDemo></ModalDemo>
</template>

<script>
import ModalDemo from './ModalDemo.vue';
export default{
  components:{ModalDemo}
}
</script>

<style scoped>
</style>
```

弹框组件

```html
<template>
    <div>
        <button @click="onOff = true">打开弹框</button>
    </div>
    <teleport to="#toast">
        <!-- 把这里边的内容挂载到相应的指定的地方 -->
        <div v-if="onOff">
            挂在到body的弹框
             <button @click="onOff = false">关闭弹框</button>
            </div>
    </teleport>
</template>
<script>
export default {
    name:"ModalDemo",
    data() {
        return {
          onOff:false
        };
    }
};
</script>
```

#### Fragments

 可以有多个根组件

```html
<template>
   <header></header>
   <main></main>
   <footer></footer>
</template>
```

#### 自定义渲染器

custom render

#### 全局api改为实例方法

![image-20210517172815083](G:\note\image\image-20210517172815083.png)

```js
createApp(App)
.component("comp",{render(){
    return h("div","这是app.component")
}})
.mount('#app')

```

```
 <comp></comp>
```

#### api摇树优化

vue2不少global-api是作为静态函数直接挂在构造函数上的，例如Vue.nextTick(),如果我们从未再代码中使用过他们，就会形成所谓的dead code,这类global-api造成的dead code无法使用webpack的tree-shaking排除掉

```js
import Vue from 'vue';

Vue.nextTick(()=>{
// something something DOm
})
```

Vue3 中做了相应的变化，将他们抽取成独立函数，打包工具的摇树优化可以将这些dead code排除掉

```js
import {nextTick} from 'vue'

nextTick(()=>{
// something something 
})
```

#### v-model变化

```js
<template>
   <div>{{couter}}</div>
   传入子组件的值
   <EmitComponent v-model:num="couter"></EmitComponent>
</template>

<script>
import EmitComponent from './EmitComponent.vue';
export default{
  components:{EmitComponent},
  data(){
    return{
      couter:100
    }
  }
}
</script>

```

```js
<template>
    <h2>{{num}}</h2>
    <div @click="$emit('update:num',num+1)">按钮</div>
</template>

<script>
export default {
    props:{
        num:{
            type:Number,
            default:10
        }
    }
};
```

#### 异步组件

异步键要求使用defineAsyncComponent方法创建

由于vue3中函数式组件必须为纯函数，异步组件定义时有如下变化：

1 必须明秋使用defineAsyncComponent 包裹

2 component 选项重命名为loader

3 Loader函数不再接受resolve和reject且必须放回一个promise

定义一个异步组件

```vue
<template>
    <div >异步组件</div>
</template>

<script>
</script>
```

```vue
<template>
   <EmitComponent></EmitComponent>
</template>

<script>
import {defineAsyncComponent} from "vue";
export default{
  components:{EmitComponent:defineAsyncComponent(()=>import('./EmitComponent.vue'))},
}
</script>

```

#### 自定义组件白名单

webpack构建时 

vue-loader由编译器，设置他提供的compilerOptions即可： vue.config.js

```js
rules:[
    {
        test:/\vue$/,
        use:'vie-loader',
        options:{
            compilerOptions:{
                siCustomeElement:tag => tag == 'com-element'//组件名
            }
        }
    }
]
```

如果使用vite构建 在vite.config.js配置 vueCompilerOptions

```js
module.exports = {
 vueCompileOptions:{
   isCustomeElement:tag => tag === 'com-element'//组件名
 }
}
```

#### 自定义指令

```vue
createApp(App)
//自定指令
.directive("highlight",{
    beforeMount (el,binding,vnode) {
        el.style.background = binding.value;
    }
})
.mount('#app')
```

```
<template>
    <div v-highlight="'red'">异步组件</div>
</template>
```

![image-20210517184709076](G:\note\image\image-20210517184709076.png)

#### vue-router 4

安装

```
 npm install vue-router@next
```

创建路由

```
import { createApp } from 'vue'
import { createRouter, createWebHashHistory, createWebHistory } from 'vue-router';
import HelloWorld from './components/HelloWorld.vue'
import Dashboard from "./components/Dashboard.vue"
import App from './App.vue'

const router =createRouter({
    // history:createWebHashHistory(),
    // history:createWebHistory(),
    routes:[
        {path:'/',component: Dashboard},
        {path:'/helloword',component: HelloWorld}
    ]
})
router.addRoute({
    path:"/about",
    name:'about',
    component:()=>import("./components/about.vue")
})

createApp(App)
.use(router)
.mount('#app')
```

跳转传参

```vue
<template>
   <button @click="goBack">返回上一页</button>
</template>

<script>
import {defineAsyncComponent, watch} from "vue";
import {onBeforeRouteLeave, onBeforeRouteUpdate, useRoute, useRouter} from "vue-router"
// import Todos from './Todos'
export default{
  methods:{
    goBack(){
    }
  },
  setup(){
    //路由跳转
   const router= useRouter();
    function goBack(){
      router.back();
    }
    
    //获取路由参数
    const route = useRoute();
    watch(
      ()=>route.query,
      (query)=>{
        console.log('====================================');
        console.log(query);
        console.log('====================================');
      }
    );
    //路由守卫
    onBeforeRouteUpdate((to,from)=>{

    })
     //路由守卫
    onBeforeRouteLeave((to,from)=>{
    })
    return {goBack}
  }
}
</script>
```

#### keep-alive

```html
<keep-alive>
  <router-view></router-view>
</keep-alive>

修改为

<router-view v-slot="{User}">
<keep-alive>
  <component :is="User"></component>    
</keep-alive>
</router-view>
```

#### watch

这样使用watch时有一个特点，就是当值第一次绑定的时候，不会执行监听函数，只有值发生改变才会执行。如果我们需要在最初绑定值的时候也执行函数，则就需要用到immediate属性。

普通的uwatch方法无法监听到对象内部属性的改变 需要deep属性对对象进行深度监听。

#### 源码

###### mustache模板引擎

![image-20210603090326800](G:\note\image\image-20210603090326800.png)

#### 虚拟dom和diff算法

![image-20210605095129814](G:\note\image\image-20210605095129814.png)

### ![image-20210603155511754](G:\note\image\image-20210603155511754.png)

![image-20210603155529074](G:\note\image\image-20210603155529074.png)

#### 响应式原理

![image-20210603191811055](G:\note\image\image-20210603191811055.png)



```
Obserser订阅者
Dep 收集依赖
watcher观察者
当Vue创建实例时会遍历data属性利用Object.defineProperty,为每个属性添加getter,getter方法数据劫持，在getter时进行依赖，setter时派发更新，当数据更新时通过getter方法里的dep.notify()方法通知wather去对比更新视图；


缺点
操作数组不方便
如果对象嵌套属性 需要深层次的遍历
```

vue3.0

```
通过proxy 数据代理 实现set get方法里边视同reflect操作对象
```

nextick

```
Vue更新DOM是异步执行的当数据发生变化时，vue将开启一个异步更新队列，视图需要等队列的数据变化完成后再同意进行更新视图

对性能进行优化
防止每次改变data都会马上更新dom

视同
修改数据后立刻得到ODM解构
vue.nextTick(()=>{

},context)
```

Vue 组件data必须是函数

```
new Vue()实例data是一个对象，但是组件中data必须是一个函数
组件是可以复用，js对象又是引用关系，如果组件data是一个对象会相互污染
```

