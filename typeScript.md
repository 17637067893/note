2020/11/28

```
1 js封装 继承 多态

2 call apply bind
3 原型链 闭包
4 跨域解决
5 this指向

如果返回值是一个对象，那么this指向的就是那个返回的对象，如果返回值不是一个对象那么this还是指向函数的实例。

这个函数中包含多个对象，尽管这个函数是被最外层的对象所调用，this指向的也只是它上一级的对象

6 函数函数柯里化
7 事件绑定的第三个参数
8 维数组  具有length属性和索引但是不具备数组的所有方法 可以用Array.from转为数组
9 reduce 
10 虚拟DOM js模拟的DOM结构 
```

给javaScript赋予了类型，扩展了类型

#### 环境

```
npm i typescript -g 
创建 .ts文件

执行命令  tsc xxx.ts
```

#### 基本类型

![image-20210601131011612](G:\note\image\image-20210601131011612.png)

#### 基本变量

```
//  变量:类型  =  值
let age:number =100;


//声明变量时 自动检测对变量进行复制
let c = false;

let a:10;  //等于常量 a=10
```

联合类型

```
// 联合类型
let b:"male" | "female"; //b  可以有多个值
```

#### unknown类型

```js
let m; //默认类型为any
let n:unknown;
let x:string;
m =x;  //any可以接受任意其他类型
 //  any不可以接受其他类型

 // 1  判断unknown 的类型再赋值
// if(typeof n === "string"){
//     x = n;
// }

// 2 类型断言 用来告诉解析器变量的实际类型
x = n as string;
x = <string> n;
```

#### 函数

```js
//函数类型声明
function sum(a:number,b:number):number{
    return a+b;
}
console.log(sum(10,20));

//函数
let  h:(a:number,b:number)=>number

//函数类型接口
interface encrypt{
    (key:string,value:string):string;
}

var md5:encrypt = function(key:string,value:string):string{
    return key + value;
}
       

```

#### 对象

```js
//Object
let d:object;
//用来指定对象中包含哪些属性
let e:{name:string};
let f:{name:string,age?:number}
let g:{name:string,[propName:string]:any};//除了name其他属性都不做限制

// 定义竖向key的类型
interface UserObj{
    [index:string]:string
}

let obj:UserObj = {name:'liuli',age:'33'}
console.log(obj.age);     
```

#### 数组

```js
// 数组
let i:string[]; //字符串数组
let j:Array<number>;

// 元祖  长度数组固定
let o: [string,string];

o = ['hellow',"world"];

定义数组索引类型
interface UserArray{
    [index:number]:string
}
let arr:UserArray = ['aaa','bbb'];
console.log(arr[1]);
```

#### 枚举

```js
//enum 枚举 把所坑你情况都列出来
enum Gender{
    Male = 0,
    Female = 1
}

let p = {
    name:"对悟空",
    gender:Gender.Male
}
```

#### 定义数据类型

```js
//定义类型
type myType = {name:string,age:number};

let user:myType;
```

#### 类

```js
class Person{
    name:string = "孙悟空";
    //只读属性
   readonly age:number = 18;
   // 静态属性
    static address:string = "河南";
  //方法 如果是static开头为静态方法
  say(){
      console.log("大家好");
  }
}
```

#### 泛型

```js

//泛型
function fn<T>(a:T):T{
    console.log();
    return a;
}
fn<string>("小明");
fn(18)


interface Inter{
    length:number;
}
//类型为实现泛型的类
function fn2<T extends Inter>(a:T):number{
    return a.length;
}
```

#### 自动编译

```
//自动编译命令
tsc app.ts -w
```

tsconfig.json

```
tsc --init在根目录下生成tsconfig.json文件
```

配置

复制到vscode中打开查看

```js
{
    //编译器的选项
    "compilerOptions": {

        // 编译后的es版本
        "target": "es5",                                /* Specify ECMAScript target version: 'ES3' (default), 'ES5', 'ES2015', 'ES2016', 'ES2017', 'ES2018', 'ES2019', 'ES2020', 'ES2021', or 'ESNEXT'. */

            //模块化
            "module": "commonjs",                           /* Specify module code generation: 'none', 'commonjs', 'amd', 'system', 'umd', 'es2015', 'es2020', or 'ESNext'. */

                //用来指定项目中使用的库
                // "lib": ["dom","es6"],                                   /* Specify library files to be included in the compilation. */

                //allowJs用来指定是否允许编译JS文件，默认false,即不编译JS文件
                // "allowJs": true,                             /* Allow javascript files to be compiled. */

                //是否检查其中错误
                // "checkJs": true,                             /* Report errors in .js files. */

                // sx代码用于的开发环境
                // "jsx": "preserve",                           /* Specify JSX code generation: 'preserve', 'react-native', 'react', 'react-jsx' or 'react-jsxdev'. */

                // declaration用来指定是否在编译的时候生成相的d.ts声明文件，如果设为true,编译每个ts文件之后会生成一个js文件和一个声明文件，但是declaration和allowJs不能同时设为true
                // "declaration": true,                         /* Generates corresponding '.d.ts' file. */

                // declarationMap用来指定编译时是否生成.map文件 
                // "declarationMap": true,                      /* Generates a sourcemap for each corresponding '.d.ts' file. */

                // socuceMap用来指定编译时是否生成.map文件
                // "sourceMap": true,                           /* Generates corresponding '.map' file. */

                //outFile用于指定输出文件合并为一个文件，只有设置module的值为amd和system模块时才支持这个配置
                // "outFile": "./",                             /* Concatenate and emit output to single file. */

                // outDir用来指定输出文件夹，值为一个文件夹路径字符串，输出的文件都将放置在这个文件夹
                // "outDir": "./dist",                              /* Redirect output structure to the directory. */

                // rootDir用来指定编译文件的根目录，编译器会在根目录查找入口文件
                // "rootDir": "./",                             /* Specify the root directory of input files. Use to control the output directory structure with --outDir. */

                // composite是否编译构建引用项目
                // "composite": true,                           /* Enable project compilation */
                // "tsBuildInfoFile": "./",                     /* Specify file to store incremental compilation information */

                // removeComments用于指定是否将编译后的文件注释删掉，设为true的话即删除注释，默认为false
                // "removeComments": true,                      /* Do not emit comments to output. */
                // noEmit不生成编译文件
                // "noEmit": true,                              /* Do not emit outputs. */
                // importHelpers指定是否引入tslib里的复制工具函数，默认为false
                // "importHelpers": true,                       /* Import emit helpers from 'tslib'. */
                // 当target为"ES5"或"ES3"时，为"for-of" "spread"和"destructuring"中的迭代器提供完全支持
                // "downlevelIteration": true,                  /* Provide full support for iterables in 'for-of', spread, and destructuring when targeting 'ES5' or 'ES3'. */
                // isolatedModules指定是否将每个文件作为单独的模块，默认为true，他不可以和declaration同时设定
                // "isolatedModules": true,                     /* Transpile each file as a separate module (similar to 'ts.transpileModule'). */

                /* Strict Type-Checking Options */
                // trict用于指定是否启动所有类型检查，如果设为true这回同时开启下面这几个严格检查，默认为false
                "strict": true,                                 /* Enable all strict type-checking options. */
                    // noImplicitAny如果我们没有一些值设置明确类型，编译器会默认认为这个值为any类型，如果将noImplicitAny设为true,则如果没有设置明确的类型会报错，默认值为false
                    // "noImplicitAny": true,                       /* Raise error on expressions and declarations with an implied 'any' type. */
                    // "strictNullChecks": true,                    /* Enable strict null checks. */
                    // strictFunctionTypes用来指定是否使用函数参数双向协变检查
                    // "strictFunctionTypes": true,                 /* Enable strict checking of function types. */
                    // strictBindCallApply设为true后对bind、call和apply绑定的方法的参数的检测是严格检测
                    // "strictBindCallApply": true,                 /* Enable strict 'bind', 'call', and 'apply' methods on functions. */
                    // strictPropertyInitialization设为true后会检查类的非undefined属性是否已经在构造函数里初始化，如果要开启这项，需要同时开启strictNullChecks,默认为false
                    // "strictPropertyInitialization": true,        /* Enable strict checking of property initialization in classes. */
                    // 当this表达式的值为any类型的时候，生成一个错误
                    // "noImplicitThis": true,                      /* Raise error on 'this' expressions with an implied 'any' type. */
                    // alwaysStrict指定始终以严格模式检查每个模块，并且在编译之后的JS文件中加入"use strict"字符串，用来告诉浏览器该JS为严格模式
                    // "alwaysStrict": true,                        /* Parse in strict mode and emit "use strict" for each source file. */

                    /* Additional Checks */
                    // "noUnusedLocals": true,                      /* Report errors on unused locals. */
                    // "noUnusedParameters": true,                  /* Report errors on unused parameters. */
                    // "noImplicitReturns": true,                   /* Report error when not all code paths in function return a value. */
                    // "noFallthroughCasesInSwitch": true,          /* Report errors for fallthrough cases in switch statement. */
                    // "noUncheckedIndexedAccess": true,            /* Include 'undefined' in index signature results */
                    // "noImplicitOverride": true,                  /* Ensure overriding members in derived classes are marked with an 'override' modifier. */
                    // "noPropertyAccessFromIndexSignature": true,  /* Require undeclared properties from index signatures to use element accesses. */

                    /* Module Resolution Options */
                    // moduleResolution用于选择模块解析策略，有"node"和"classic"两种类型
                    // "moduleResolution": "node",                  /* Specify module resolution strategy: 'node' (Node.js) or 'classic' (TypeScript pre-1.6). */
                    // baseUrl用于设置解析非相对模块名称的基本目录，相对模块不会受到baseUrl的影响
                    // "baseUrl": "./",                             /* Base directory to resolve non-absolute module names. */
                    // paths用于设置模块名到基于baseUrl的路径映射
                    // "paths": {},                                 /* A series of entries which re-map imports to lookup locations relative to the 'baseUrl'. */
                    // "rootDirs": [],                              /* List of root folders whose combined content represents the structure of the project at runtime. */
                    // typeRoots用来指定声明文件或文件夹的路径列表，如果指定了此项，则只有在这里列出的声明文件才会被加载
                    // "typeRoots": [],                             /* List of folders to include type definitions from. */
                    // types用于指定需要包含的模块，只有在这里列出的模块的声明文件才会被加载
                    // "types": [],                                 /* Type declaration files to be included in compilation. */
                    // "allowSyntheticDefaultImports": true,        /* Allow default imports from modules with no default export. This does not affect code emit, just typechecking. */
                    // esModuleInterop通过导入内容创建命名空间，实现CommonJS和ES模块之间的互操作性
                    "esModuleInterop": true,                        /* Enables emit interoperability between CommonJS and ES Modules via creation of namespace objects for all imports. Implies 'allowSyntheticDefaultImports'. */
                        // 不把符号链接解析为真实路径，具体可以了解下webpack和node.js的symlink相关知识
                        // "preserveSymlinks": true,                    /* Do not resolve the real path of symlinks. */
                        // allowSyntheticDefaultImports, 用来指定允许从没有默认导出的模块中默认导入
                        // "allowUmdGlobalAccess": true,                /* Allow accessing UMD globals from modules. */

                        /* Source Map Options */
                        // sourceRoot用于指定调试器应该找到TypeScript文件而不是源文件的位置，这个值会被写进.map文件里
                        // "sourceRoot": "",                            /* Specify the location where debugger should locate TypeScript files instead of source locations. */
                        // mapRoot用于指定调试器找到映射文件而非生成文件的位置，指定map文件的根路径，该选项会影响.map文件中的sources属性
                        // "mapRoot": "",                               /* Specify the location where debugger should locate map files instead of generated locations. */
                        // inlineSourceMap指定是否将map文件内容和js文件编译在一个同一个js文件中，如果设为true,则map的内容会以//#soureMappingURL=开头，然后接base64字符串的形式插入在js文件底部
                        // "inlineSourceMap": true,                     /* Emit a single file with source maps instead of having a separate file. */
                        // inlineSources用于指定是否进一步将ts文件的内容也包含到输出文件中
                        // "inlineSources": true,                       /* Emit the source alongside the sourcemaps within a single file; requires '--inlineSourceMap' or '--sourceMap' to be set. */

                        /* Experimental Options */
                        // experimentalDecorators用于指定是否启用实验性的装饰器特性
                        // "experimentalDecorators": true,              /* Enables experimental support for ES7 decorators. */
                        // "emitDecoratorMetadata": true,               /* Enables experimental support for emitting type metadata for decorators. */

                        /* Advanced Options */
                        "skipLibCheck": true,                           /* Skip type checking of declaration files. */
                            "forceConsistentCasingInFileNames": true        /* Disallow inconsistently-cased references to the same file. */
    },
        //指定编译的文件位置
        "include": [
            "src/**/*"
        ],
            //判处文件
            "exclude": [
                "node_modules",
                "**/*.spec.ts"
            ],
                //要继承的其他tsconfig文件
                // "extends": "./configs/base",
                // 一个对象数组，指定要引用的项目
                "references":[],
                    // compileOnSave如果设为true,在我们编辑了项目文件保存的时候，编辑器会根据tsconfig.json的配置更新重新生成文本，不过这个编辑器支持
                    "compileOnSave":true
}

```

#### webpack配置typescript

1 解析loader

```js
rules:[
    {test: /\.ts$/, use:'ts-loader' }, //位置不用加引号
]
```

2 模块引用

```
m1.ts中    export const name:string = "小明";


index.ts 引用他
import {name} from "./m1"

console.log(name);


如果引用模块需要配置  否则打包会报错
resolve:{
  extensions:[".ts",'.js']
}
```

#### 迭代器

for... in 迭代对象的key

```
let list = [4, 5, 6];

for (let i in list) {
    console.log(i); // "0", "1", "2",
}
```

for... of 迭代对象的value

```
for (let i of list) {
    console.log(i); // "4", "5", "6"
}
```

#### 模块

命名空间 内部模块 主要用于组织代码避免命名冲突

模块 ts外部模块的冲突  代码服复用

命名空间

```js
namespace A{
    //将Person暴露出去
    export class Person{
      name:string;
      age:number;
      constructor(name:string,age:number){
          this.name = name;
          this.age = age;
      }
  } 
}

var p = new A.Person("小明",10);

```

模块化

```js
export namespace A{
    //将Person暴露出去
    export class Person{
      name:string;
      age:number;
      constructor(name:string,age:number){
          this.name = name;
          this.age = age;
      }
  } 
}

在新文件中引入
var p = new A.Person("小明",10);
```

#### 装饰器

就是一种方法 可以注入到类，方法，属性参数上来扩展 类 属性 方法 参数的功能

```js
//普通装饰器 不能传参
//这里的params就是person的实例对象
function logClass(params:any){
    console.log(params);
    params.prototype.grade = "优秀" //添加到原型上
    params.prototype.run = function(){
        console.log("你好");
        
    }
}

@logClass
class Person{
    name:string;
    age:number;
    constructor(name:string,age:number){
        this.name = name;
        this.age = age;
    }
    say(){

    }
}
var p = new Person("小明",10);
```

###### 装饰器传参

```js
//params 是装饰器参数
function logClass(params:string){
    // fn是Person的实例对象
    return function(fn:any){
        console.log("装饰器参数:"+params);
        console.log("person实例对象:"+fn);
        
    }
}

@logClass("传个参数")
class Person{
    name:string;
    age:number;
    constructor(name:string,age:number){
        this.name = name;
        this.age = age;
    }
    say(){

    }
}
var p = new Person("小明",10);
```

###### 重载构造方法

```js
//重载类里面的方法   target为Person的实例对象
function logClass(target:any){
    //返回class 
    return class extends target{
        name:string = "重载构造函数";
        say(){
            console.log("重载say方法"); 
        }
    }
}

@logClass
class Person{
    name:string;
    constructor(name:string){
        this.name = name;
    }
    say(){

    }
}
var p = new Person("小明");
console.log(p);
console.log(p.name);
console.log(p.say());
```

###### 属性装饰器

```js


//重载类里面的方法   target为Person的实例对象
function logClass(target:any){
    //返回class 
    return class extends target{
        name:string = "重载构造函数";
        say(){
            console.log("重载say方法"); 
        }
    }
}




//val属性装饰器内值
function logProperty(val:string){
     // target 为Person的实例对象  attr为属性的key
     //如果构造函数内直接赋值name则无法修改了
  return function(target:any,attr:string){
      target[attr] = val;
  }
}
class Person{
    @logProperty("修改属性")
    name:string | undefined;
    constructor(){
    // constructor(name:string){
        // this.name = name;
    }
    getName(){
        // console.log(this.name);
    }
}
// var p = new Person("小明");
var p = new Person();
console.log(p);
console.log(p.name);
p.getName()

```

###### 方法装饰器

```js
/**
 * 方法装饰器
 * 用来监视，修改，替换方法定义
 * 参数
 * 1 对静态成员来说是类的构造函数，对于实例成员来说是类的原型对象
 * 2 成员的名字
 * 3 成员的属性描述
 */
 function logMethod(params:any){
    return function(target:any,methodName:any,desc:any){
        console.log(target);
        console.log(methodName);
        console.log(desc);
        console.log(params);
        // 通过target 可以扩展Person的实例对象
        //添加属性
        target.age = 100;
        target.say=function(){
            console.log("你好")
        }
        //修改方法
        target[methodName] = function(){
            console.log("修改方法");
            
        }
    }
}

class Person{
    @logProperty("修改属性")
    name:string | undefined;
    constructor(){
    }
    @logMethod("参数")
    getName(){
    }
}
var p = new Person();
console.log(p);

p.getName()
```

所有装饰器执行属性

属性装饰器==>方法装饰器==>方法参数装饰器==》类装饰器