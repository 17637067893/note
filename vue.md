####   声明周期

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

###### 1 vuex的使用

```vue
<template>
  <div class="hello">
    <h2>{{name}}</h2>
    <button @click="changeName">改变名字</button>
  </div>
</template>

<script lang="ts">
import {defineComponent,computed } from 'vue'
import {useStore} from "vuex"
export default defineComponent({
  setup(){
    //引入store
    const store = useStore();
    //改变值
    const changeName=()=>{
      store.commit('changeName', "小胖")
    }
    return {
      name:computed(()=>store.state.name),
      changeName,
    }
  }
})
</script>
```



###### 2 ref

```js
<template>
  <div class="hello">
    <h2>{{age}}</h2>
    <button @click="changeAge">改变年龄</button>
  </div>
</template>

<script lang="ts">
import {defineComponent,ref} from 'vue'
export default defineComponent({
  setup(){
    const age = ref(0)
    function changeAge(){
      age.value ++
    }
    return {
      age,
      changeAge
    }
  }
})
```

###### 3 toRef

```vue
<template>
  <div class="hello">
    <h2>{{newObj}}</h2>
    <button @click="change">改变年龄</button>
  </div>
</template>

<script lang="ts">
import {defineComponent,toRef} from 'vue'
export default defineComponent({
 setup(){
    let obj = {name : 'alice', age : 12};
   // 修改通过toRef创建的响应式数据元数据也会改变，但是ui不会更新
    let newObj= toRef(obj, 'name');
    function change(){
      newObj.value = 'Tom';
      console.log(obj,newObj)
    }
    return {newObj,change}
 }
})
</script>
```

ref和toRef的区别

```
1 ref 本质是拷贝，修改响应式数据不会影响元数据，界面会刷新
2 toRef 是引用关心 修改数据元数据也会改变但不会刷新视图
3 toRef接受两个参数 ref一个参数
toRefs  多个属性都变成响应式数据，并且要求响应式数据和原始数据关联，并且更新响应式数据的时候不更新界面
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

#### globalProperties

```js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

const app = createApp(App);
// app.config.globalProperties 等于以前Vue.property
//挂载属性
app.config.globalProperties.name="小明"

app.use(store).use(router).mount('#app')


//组件使用
<script lang="ts">
import {defineComponent,getCurrentInstance} from 'vue'
export default defineComponent({
 setup(){
   //获取vue的实例
   let {ctx} = getCurrentInstance();
     //获取name
   console.log(ctx.name);
   return {
   }
 }
})
</script>

```

#### 源码

###### mustache模板引擎

![image-20210603090326800](G:\note\image\image-20210603090326800.png)

#### 虚拟dom和diff算法

![image-20210605095129814](G:\note\image\image-20210605095129814.png)

### ![image-20210603155511754](G:\note\image\image-20210603155511754.png)

![image-20210603155529074](G:\note\image\image-20210603155529074.png)

#### 响应式原理

###### vue2.0

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

###### vue3.0

```
使用了proxy代理进行数据劫持，Reflect修改数据 在setter 中使用track收集依赖 setter时通过trigger派发更新
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

##### computed 和 watcher

computer 原理

```
在initComputed的时候，会将compotuted的每个key添加一个watcher，watcher的getter方法就是我们写的函数，当依赖变化时 将lazy设置为true，并不计算值，然后将computed的key通过设置defineProperty getter setter设置到vm上，让组件的渲染watcher 收集依赖，当依赖变化时，触发渲染watcher更新方法，会判断dirty 如果true计算更新 如果为false直接取值
```

##### 兄弟组件传值

![image-20210606104809186](G:\note\image\image-20210606104809186.png)

#### 自定义 customRef

```js
<template>
<div>
  <p>{{obj}}</p>
  <button @click="inc">button</button>
</div>
</template>

<script>
import { customRef } from 'vue';

// customRef用于 自定义ref
// 自定义 ref 需要提供参数传值
function myRef(value) {
    // 自定义 ref 需要提供 customerRef 返回值
    // customer 需要提供一个函数作为参数
    // 该函数默认带参数 track 和 trigger ，都是方法。
    return customRef((track, trigger) => {
      return {
        // customer 需要提供一个对象 作为返回值
        // 该对象需要包含 get 和 set 方法。
        get() {
          // track 方法放在 get 中，用于提示这个数据是需要追踪变化的
          track();
          console.log('get', value);
        
          return value;
        },
        // set 传入一个值作为新值，通常用于取代 value
        set(newValue) {
          console.log('set', newValue);
          value = newValue;
          // 记得触发事件 trigger,告诉vue触发页面更新
          trigger();
        }
      }
    })
}

export default {
  name: 'App',
  setup() {
　　　　// let obj = ref(18); // reactive({value: 18})
// 应用上面的自定义 ref ，使用方案和之前的 ref 是类似的。
    const obj = myRef(123);
    function inc() {
      obj.value += 1;
    }

    return {
      obj,
      inc
    };
  }
}
</script>
```

#### shallowRef

shallowRef只有value的值是响应式的会更新页面

```vue
<template>
  <div class="hello">
    <h2>{{count}}</h2>
    <h2>{{user.age}}</h2>
    <button @click="change">改变年龄</button>
  </div>
</template>

<script lang="ts">
import {defineComponent,customRef,shallowRef} from 'vue'
export default defineComponent({
 setup(){
   
   //会更页面
  const count= shallowRef(18)
  //不会更新页面
  const user= shallowRef({age:18})
  function change(){
    count.value++
    user.value.age++
  }
   return {
     count,
     user,
     change,
   }
 }
})
</script>
```

#### triggerRef

```vue
<template>
  <div class="hello">
    <h2>{{user.age}}</h2>
    <button @click="change">改变年龄</button>
  </div>
</template>

<script lang="ts">
import {defineComponent,triggerRef,shallowRef} from 'vue'
export default defineComponent({
 setup(){
  //不会更新页面
  const user= shallowRef({age:18})
  function change(){
    user.value.age++;
    //强制更新页面
    triggerRef(user)
  }
   return {
     user,
     change,
   }
 }
})
</script>
```

#### watch

```vue
<template>
  <div class="hello">
    <h2>{{count}}</h2>
    <button @click="change">改变年龄</button>
  </div>
</template>

<script lang="ts">
import {defineComponent,ref,watch} from 'vue'
export default defineComponent({
 setup(){
   const count = ref(0)
   const count = ref({num:0})
   //监听ref的值
   watch(count,(newValue,oldValue)=>{
     //监听 reactive的值
   watch(()=>count.num),(newValue,oldValue)=>{
     console.log(`原值为${oldValue}`)
     console.log(`新值为${newValue}`)
   })
   setTimeout(() => {
     count.value++
   }, 2);

   function change () {
     
   }
   return {
     count,
     change,
   }
 }
})
</script>
```

#### watchEffect

1 不需手动传入依赖

2 每次初始化时自动执行一次回调函数自动获取依赖

3 无法获取原值，只能得到变化后的值

```vue
<template>
  <div class="hello">
    <h2>{{state.count}}</h2>
    <button @click="change">改变年龄</button>
  </div>
</template>

<script lang="ts">
import {defineComponent,reactive,watchEffect} from 'vue'
export default defineComponent({
 setup(){
   const state = reactive({count:0,name:'ss'})
   watchEffect(()=>{
     console.log(state.count)
     console.log(state.name)
   })
   setTimeout(() => {
     state.count++;
     state.name="www"
   }, 2);
   let change=()=>{}
   return {
     state,
     change,
   }
 }
})
</script>
```

#### provide inject

```vue
上层组件
<template>
  <div id="nav">
    <h3>{{title}}</h3>
    <button @click="setTitle">同时改变title</button>
  </div>
</template>+
<script>
	import { ref, provide } from 'vue'
	export default {
		setup() {
			let title = ref('这个要传的值')
			provide('title', title); // provide的第一个为名称，第二个值为所需要传的参数
			let setTitle = () => {
				title.value = '点击后，title会变成这个'; // 点击后都会有响应式哦！
			}
			return {
				title,
				setTitle
			}
		}
	}
</script>

//在不知道要多少层的子组件下调用参数

<template>
  <div class="hello">
    <h4>{{title}}父组件点击之后这个title也会跟着变化哦</h4>
  </div>
</template>
<script>
	import { inject } from 'vue'
	export default {
		let title = inject('title'); // inject的参数为provide过来的名称
		return {
			title
		}
	}
</script>
```

#### vuex原理

```
vuex就是一个类或者是对象,上面有个.install方法 install 方法中有mixin方法,实现了在before之前调用vuexInit方法 给Vue实例上挂载$store = store属性
由于vue的插件机制 Vue.use()时会调用install方法执行后续操作

vuex是响应式的原因是 会把state方法vue实例的data中,利用data的响应式原理

getter mutations actions

```

#### History模式Hash模式

```
1 hash 模式只会发送#前的链接发送给服务端 后端不需要处理页面刷新不会404   二维码的时候会自动过滤掉#后面的参数啊

2 history 会发送一整条url  服务器不认识报错404 后端配置 并做匹配全部路径的路由  
```

#### 路由传参的区别

```js
router.push({name:'About',params:{id:100}}) //刷新消失
router.push({path:'/about',query:{id:100}}) //刷新不消失
```

#### vue监听数组

```
1 已经存在的元素可以直接被监听到 新增的元素不会被监听到
2 监听的方式 是重写了素组的七个方法，执行这些方法时调用deo.notify通知视图更新
3 vue3.0 使用了proxy代理已经决绝了这种问题
```

#### Vue异步更新DOM

```
1 修改数据调用getter 触发water 把书友的watcher封装到到一个队列中
2 然后执行nextTick传入flushSchedulerQueue回调对watcher做一些优先级处理，
3 由于nextTick内封装了浏览器的异步API,所以就会异步调用watcher更新视图
```

#### watcher deep=true

```
如果为true 会调用 traverse，对对象进行深度的递归收集依赖，只要依赖发生变化就更新视图
```

