#### 结构
```
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    count:0,
    age:'18'
  },
  // methods 在mutation 改变state 同步函数不可以使用axios,第一个参数为state

  mutations: {
    add(state,num){
      this.state.count++
    },
    setNum(state,val){
      this.state.count=val
    }
  },
//相当于 计算属性
getters{
  bigage:function(){
 return state.age + 20;
 }
},
  actions: {
  },
  modules: {
  }
})
```
```
页面里可以通过 直接访问state中的数据
获取值 this.$store.state.count,
改变值 后边可以传参 如果有多个参数 可以传一个对象
 this.$store.commit("add",10)
```
### mapstate 辅助计算属性获取state中的值
```
import { mapState } from "vuex"
mapState 使用的两种方法

1 computer 映射为mapState整个对象 传入需要的属性
computer:mapState({
count:'count',
age:'age'，
num:(state) => {
return state.num + 10
}
})

2 mapState映射为comuter的部分属性，computer还可使用其他方法
import { mapState } from "state"
let mapStateObj = mapState({
count:'count',
age:'age'
})

export default{
data(){
return{
name:'小明'
}
},
computer(){
//vue自己的方法
name: function(){
    return '姓名' + name; 
    }
//映入mapState对象
...mapStateObj
}
}
```
###mapGetters
```
如果使用单个getters方法
this.$store.getters.bigage;

如果想要是引入多个getters方法
import {mapGetters} from 'vuex'
使用 方法同mapState

let mapGettersObj = {
bigage:'bigage'
}
在 computer中使用
computer:{
  ...mapGetters
}
```
####Action
```
相对于mutatons actions是异步的可以改变数据
actions:{
 actionsa:(context){
  context.commit("add")
}
}

可以直接使用 this.$store.dispatch('actionsa':{})
```