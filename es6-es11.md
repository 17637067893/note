#### rest参数

```js
function add(...args){  //代替es5 arguments
    console.log(args);
}
add(1,2,3);
```

#### Symbol

Symbol 不是构造函数，所以不能使用new命令

let sy=Symbol()

可以传参数也可以不传参数作为描述值，参数类型可以是任意类型

调用一次Symbol都会创建一个对象，对象的内存地址不一样保证唯一性

使用场景

1 作为对象的key

2 利用不可枚举特性，作为类或对象的内部属性

```js
let sy1 = Symbol("kk");
let sy2 = Symbol("kk");
// console.log(sy1 == sy2); //false 每个sy  

//Symbol.for() 创建
let s1= Symbol.for("s"); //未创建时 创建
let s2 = Symbol.for("s");// 检测创建后直接返回
//   console.log(s1 ===s2);  //true

到此有七种数据结构 USONB
U undifined
S string Symbol
o Oject
n number Null
b bool
```

作用 唯一标识符，可用作对象的唯一属性值

#### 迭代器

ES5 之前集合 对象，数组 ES6 新增了 Map Set

所有ES6 同意了遍历的接口Iterator

就是一个接口对象 包含一个next()方法，会返回当前的值value 和 done是否迭代完成

js 有可迭代集合和不可迭代集合Array String Map Set内部有默认的迭代器，对于object没有默认迭代器的集合，想要使用for....of遍历 可以自定义[Symbol.iterator]函数

```js
let obj = {
   a: 1,
   b: 2
}
obj[Symbol.iterator] = function(){
   let  keys = Object.keys(obj) 
   let len = keys.length;
   let n = 0;
   // 返回对象 每次迭代会自动调用对象里面的next方法
   return {
       next() {
          //  返回值有value和done
          return n < len ? {
               value: { k: keys[n], v: obj[keys[n++]] },
               done: false 
          }: { done: true }
       }
   }
}

//自定义迭代器
let obj = {
    name:'student',
    arr:['a','b','c','d'],
    //实现这个iterator接口自定义遍历
    [Symbol.iterator](){
        let index=0;
        let _this = this;
        return{
            //next指向下一个遍历对象
            next:()=>{
                if(index<_this.arr.length){
                    index ++
                    return {value:_this.arr[index],done:false}
                }else{
                    return {value:undefined,done:true}
                }
            }
        }
    }
}

 for(let v of obj){
    console.log(v);
 }
```

#### 生成器

```js
针对异步编程
函数名前 添加 * 使用 yield分隔代码  每次调用next() 执行一段 yield
next()可以传入参数 作为下个yield的返回结果

function * add(arg){
    console.log("aaa");
    let res = yield "11111";
    console.log(res);
    console.log("bbb");
    yield "22222";
    console.log("ccc");
    yield "33333";
    console.log("ddd");
}

add();
console.log(add().next()); {value:'1111',done:false}
console.log(add().next());  {value:'2222',done:false}
console.log(add().next());

for(let v of add()){
    console.log(v);
}
```

#### set

```js
let arr1 = [1,1,2,3,4];
let s = new Set(arr);

Set.prototype.add = function (value) {
    // 对象底层的has方法 判断是否存在此属性
    if (this.has(value)) {
        return false;
    }
    //若果不存 就可以添加该值 key:value都为新增的值
    this.items[value] = value;
    return true
}

//交集
let arr3 = [1,2,3,4,5,3,21,2,4];
let arr4 = [4,5,6,3,2,1];
let res = [...new Set(arr3)].filter(item=>new Set(arr4).has(item));


//并集
let arr5 =  [... new Set([...arr3,...arr4])];
console.log(arr5);
```

#### map

```
map当做是增强版的Object   key不仅是字符串 可以是其他任意类型
```

#### Number.EPSILON

Number.EPSILON 是javaScript的最小精确度 2.220446049250313e-16

```js
function numberEqual(a,b){
      if(Math.abs(a-b)<Number.EPSILON){
          return true;
      }else{
          return false;
      }
  }
```

#### Object.is()

```js
Object.is(120,120); //true
Object.is(NaN,NaN); //true
```

#### arr.inclues()

```js
let arr5= [1,2,3,4];
console.log(arr5.includes(1));
console.log(arr5.includes(5));
```

#### Promise

promise异步解决方案，解决回调地狱的问题

```
Promise有三个状态 Pending,Fulfilled,rejected,一旦从pending转为其他状态就不会再发生更改
new Promise时会传入一个executor函数，函数内的代码会立即执行,运行到resolve时会把pending转为Fulfilled，并把接受的参数传入到then的第一个函数onFulfilled函数中,返回一个新的Promise(因为Promise的状态不可改变)链式调用，
如果excutor中运行到reject时会把pending转为rejected，并把接受的参数传入到then函数的第二个参数onRejected中并返回新的promise链式调用
如果运行的有错误还会通过try catch收集错误传入到reject中
```

Promise.all()

所有promise执行完毕后才执行回调

```jsx
function p1(){
    return new Promise((resolve,reject)=>{
        resolve("p1完成");
    })
}

function p2(){
    return new Promise((resolve,reject)=>{
        setTimeout(()=>{
           resolve("p2完成")
        },2000);
    })
}

function p3(){
    return new Promise((resolve,reject)=>{
        resolve("p3完成")
    });;
}

Promise.all([p1(),p2(),p3()])
.then((data)=>{
    console.log(data);
})
```

Promise.race()

第一个执行完

毕

```js
Promise.race([p1(),p2(),p3()])
.then(function(data){
    console.log(data);
})
```



#### async await

async就是Generator函数的语法糖

async 和Generator的改进

```
1 await后跟Promise对象或原始数据类型(会自动转入resolved) yield只能跟Thunk函数和Promise对象
2 async返回的Promise对象 而 Generator返回的Iterator迭代器对象
```

promise.all

![img](G:\note\image\1681656-20190629161315589-462215674.png)

async/await其实是基于Promise的。async函数其实是把Promise包装了一下。

```js
getConstant() {
   return 1
 }

 async getAsyncConstant() { 
  return 1
 }

 async getPromise() {
  return new Promise((resolved, rejected)=> {
    resolved(1)
  });
 }

 async test() {
  let a = 2
  let c = 1
  await getConstant();
  let d = 3
  await getPromise();
  let d = 4
  await getAsyncConstant();
  return 2
 }
```

上面的代码其实真正的在解析执行的时候是这样的：

```js
function getConstant() {
   return 1;
}

function getAsyncConstant() {
  return Promise.resolve().then(function () {
   return 1;
  });
}

function getPromise() {
  return Promise.resolve().then(function () {
   return new Promise((resolved, rejected) => {
    resolved(1);
   });
  });
}

  test() {
    return Promise.resolve().then(function () {
       let a = 2;
       let c = 1;
       return getConstant();
     }).then(function () {
       let d = 3;
       return getPromise();
     }).then(function () {
       let d = 4;
       return getAsyncConstant();
     }).then(function () {
       return 2;
     });
 }
```

**做个练习**

```js
 function getJson(){
      return new Promise((reslove,reject) => {
        setTimeout(function(){
          console.log(2)
          reslove(2)
        },2000)
      })
     }
    async function testAsync() {
       await getJson()
       console.log(3)
    }

    testAsync()
```

内部运行

```js
function getJson(){
    return new Promise((reslove,reject) => {
      setTimeout(function(){
        console.log(2)
        reslove()
      },2000)
    })
}

function testAsync() {
    return new Promise((reslove,reject) => {
        getJson().then(function (res) {
          console.log(3)
        })
    })
}

testAsync()
```

#### 对象扩展

```js
console.log(Object.keys(school));  //  ["a", "b", "c"]
console.log(Object.values(school));  //["1", "2", "3"]
console.log(Object.getOwnPropertyDescriptors(school));
```

#### Object.fromEntries

Object.entries的逆向操作

```js
const result = Object.fromEntries([
    ["name","小明"],
    ['xueke','学科']
])
```

#### 字符串左右去空格

```js
let str = "  123  ";
console.log(str.trimStart());
console.log(str.trimEnd());
```

#### flat flatMap

```js
const arr4 = [1,2,3,[4,5,[6,7,8]]];
console.log(arr4.flat(2));  // [1, 2, 3, 4, 5, 6, 7, 8] 参数2 为解析的深度
```

#### 批量异步任务

```js
//  p1 ,p2 不管 resolve 还是reject都返回 结果
//let res =  Promise.allSettled([p1,p2]);、  p1 ,p2 不管 resolve 还是reject都返回 结果
//let res =  Promise.all([p1,p2]); //两个都成功才返回结果
```

#### 动态导入

```js
btn.onClick = function(){
    import('./hell.js').then(res=>{

    })
}
```

#### globalThis

全局对象 globalThis 始终指向 全局对象

```
console.log(globalThis);
```

