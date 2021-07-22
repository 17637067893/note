```js
/**
 * - prototype: 原型
 * __proto__: 原型链
 * 
 * 从属关系
 * prototype --> 函数的一个属性:对象 {}
 * __proto__  ———> 对象想Object的一个属性: 对象{}
 * 
 * 对象的__proto_保存该对象的构造函数的prototype
 */

function Test(){
    this.a = 1;
}

Test.prototype.b = 2;

Object.prototype.c = 3;
const test = new Test();
console.log(test);  // Test {a: 1}
console.log(test.__proto__  == Test.prototype); // true
console.log(test.contructor == Test);//true
console.log(test.__proto__ == Test.prototype);  //true
console.log(Test.prototype.__proto__ == Object.prototype);
console.log(Object.prototype.__proto__);  //到顶层了 null 他不是由任何构造函数创建而来的到了


//顶层都是Function.prototype
console.log(Test.__proto__ == Function.prototype); //true
console.log(Function.__proto__ == Function.prototype) //true 底层规定
console.log(Object.__proto__ == Function.prototype); //true 万物底层都是Function

// test ==> Test {a: 1}
//查看自己本身有没有a属性
console.log(test.hasOwnProperty('a')); //true
console.log(test.hasOwnProperty('b')); //false
//查看原型链是否有某属性
console.log("a" in test);  // true
console.log("b" in test); //true
console.log("c" in test); //true
```

##### 防抖函数

```
function debouce(fn,delay){
  let timer = null;
  return function(){
    clearTimeout(timer)
    timer=setTimeout(()=>{
    fn.apply(this,arguments);
    },delay)
  }
}
```

##### 节流函数

```
function throttle(fn,delay){
   let canRun = true;
   return function(){
      if(!canRun) return;
      canRun = false;
      setTimeout(()=>{
      fn.apply(this,arguments)
      canRun = true;
      },delay)
   }
}
```

##### 单例模式

点击登录后弹出一个登录弹框，即使再次点击，也不会再出现一个相同的弹框

如果用户打开了一个音乐，又想打开一个音乐，那么之前的播放界面就会自动关闭，切换到当前的播放界面。

es5 实现

```
var Singleton = function(name) {
    this.name = name;
    //一个标记，用来判断是否已将创建了该类的实例
    this.instance = null;
}
// 提供了一个静态方法，用户可以直接在类上调用
Singleton.getInstance = function(name) {
    // 没有实例化的时候创建一个该类的实例
    if(!this.instance) {
        this.instance = new Singleton(name);
    }
    // 已经实例化了，返回第一次实例化对象的引用
    return this.instance;
}    
```

es6 实现

```js
class Singleton {
    constructor(name) {
        this.name = name;
        this.instance = null;
    }
    // 构造一个广为人知的接口，供用户对该类进行实例化
    static getInstance(name) {
        if(!this.instance) {
            this.instance = new Singleton(name);
        }
        return this.instance;
    }
}
```

```js
var a = Singleton.getInstance('sven1');
var b = Singleton.getInstance('sven2');
// 指向的是唯一实例化的对象
console.log(a === b);
```

##### v8垃圾回收机制

![image-20210605185309807](G:\note\image\image-20210605185309807.png)

##### 手写Promise

第一步 resolve reject 修改status 传值

第二步  接受fn函数 并返回new MyPromise()

```js
var that;

let config = {
    status: null,
    resolveParam: null,
    rejectParam: null
};

function MyPromise(callback) {
    that = this;
    callback(that.resolve, that.reject);
}

MyPromise.prototype = {
    constructor: MyPromise,
    resolve(param) {
        config.rejectParam = null;
        config.resolveParam = param;
        config.status = "PENDING";
    },
    reject(param) {
        config.rejectParam = param;
        config.resolveParam = null;
        config.status = "PENDING";
    },
    then(_fn) {
        // resolve
        if (config.resolveParam != null) {
            config.status = 'RESOLVED';

            try {
                _fn(config.resolveParam)
            } catch (err) {
                config.rejectParam = err;
                that.catch(() => { })
            }
        }
        return new MyPromise(() => { });
    },
    catch(_fn) {
        //reject
        if (config.rejectParam != null) {
            config.status = 'REJECTED';
            _fn(config.rejectParam);
        }
        return new MyPromise(() => { })
    },
    finally(_fn) {
        //初始化配置
        config = {
            status: null,
            resolveParam: null,
            rejectParam: null
        };
        _fn();
    }
}

let f1 = function(num){
    return new MyPromise((resolve,reject)=>{
        if(num < 10) resolve(num);
        else if (num === 11) reject(num);
    })
}

f1(11).then(res => {
    console.log("then1",res);
}).then(res => {
    console.log("then2",res);
}).catch(err=>{
    console.log("catch",err);
})
```

##### 手写map

```js
Array.prototype.my_map =  function(fn){
    let resArr = [];
    let that = this;
    for(let i=0;i<arr.length;i++){
        resArr.push(fn.call(that,arr[i],i));
    }

    return resArr;
}

const arr = [1,2,3,4];

let newArr =  arr.my_map((item,index,_arr)=>{
    console.log(this,item,index,_arr);
    return item*2;
})
console.log(newArr);
```

##### 手写filter

```js
function filter(arr,fn){
    let newArr = [];
    for(let i=0;i<arr.length;i++){
        if(fn(arr[i],i,arr)){
            newArr.push(arr[i])
        }
    }
    return newArr
}
```

##### 手写reduce

```js
function reduce(callback,prev){
    for(let i=0;i<this.length;i++){
        if(typeof prev == "undefined"){
            prev = callback(this[i],this[i+1],i+1,this)
        }else{
            prev = callback(prev,this[i],i,this);
        }
    }
    return prev;
}
```

##### 深拷贝

```js
function deepClone(obj){
    let cloneObj;
    //判断当前是简单类型数据直接复制
    if(obj && typeof obj !== 'object'){
        cloneObj = obj;
    }else if(obj && typeof obj === 'object'){
        //检测输入的是数组还是对象
        cloneObj = Array.isArray(obj)?[]:{};
        for(let key in obj){
            if(obj.hasOwnProperty(key)){
                if(obj[key] && typeof obj[key] == 'object'){
                    cloneObj[key] = deepClone(obj[key])
                }else{
                    cloneObj[key] = obj[key]
                }
            }
        }
    }
    return cloneObj;
}
```

##### 函数克理化

```js
function add(){
    let arr = [...arguments];
    var _adder = function(){
        arr.push(...arguments);
        return _adder;
    }
    _adder.toString=function(){
        return arr.reduce((a,b)=>{
            return a+b;
        })
    }
    return _adder;
}
console.log(add(1)(2)(3))                // 6
console.log(add(1, 2, 3)(4))             // 10
console.log(add(1)(2)(3)(4)(5))          // 15
console.log(add(2, 6)(1))
```

##### 发布订阅者

```js
   class PubSub {
            constructor() {
                //保存事件
                this.event = {};
            }
            //订阅事件
            subscribe(eventname, fun) {
                if (!this.event.hasOwnProperty(eventname)) {
                    this.event[eventname] = [];
                }
                if (typeof fun == 'function') {
                    this.event[eventname].push(fun);
                }
            }
            //发布
            publish(eventname, arg) {
                if (this.event.hasOwnProperty(eventname)) {
                    this.event[eventname].map((item) => {
                        item.call(null, arg);
                    })
                }
            }
            //移除订阅
            removeEvent(eventName, fun, arg) {
                if (this.event.hasOwnProperty(eventName)) {
                    this.event[eventName].map((item, index) => {
                        if (item == fun) {
                            this.event[eventName].splice(index, 1);
                            item.call(null, arg);
                        }
                    });
                }
            }
        }

        const util = new PubSub();

        function notice(params) {
            console.log(params);
        }
        // 订阅事件
        util.subscribe("event", notice);

        // 发布事件
        util.publish("event", "订阅成功");

        // 移除订阅
        setTimeout(() => {
            util.unSubscribe("event", notice, "已取消订阅");
        }, 3000);
```

RESTFUAL APi

```
用URL来唯一标识网络资源，用methods来标识对资源的操作方式
```

