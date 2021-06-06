#### rest参数

```js
function add(...args){  //代替es5 arguments
    console.log(args);
}
add(1,2,3);
```

#### Symbol

1数为一个字符串 作为描述 或者 作为字符串的时候使用 

```js
let sy1 = Symbol("kk");
let sy2 = Symbol("kk");
// console.log(sy1 == sy2); //false 每个sy  

//Symbol.for() 创建
let s1= Symbol.for("s");
let s2 = Symbol.for("s");
//   console.log(s1 ===s2);  //true

到此有七种数据结构 USONB
U undifined
S string Symbol
o Oject
n number Null
b bool
```

#### 迭代器

```js
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
console.log(add().next());
console.log(add().next());
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
map当做是增强版的map   key不仅是字符串 可以是其他任意类型
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

#### async await

```js
async function fn(){
//await后接Promise对象 res= Promise中的resolve结果 为成功返回或失败的
   let res = await new Promise((resolve,reject)=>{
           resolve("成功")
        // reject("成功")
    })
  };
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

