介绍

webpack 是一种前端资源构建工具，一个静态资源模块打包器

webpack 把前端多有资源文件(js/json/css/img/less/...)都做为模块处理，根据他们的依赖关系进行静态分析，打包生成相应的静态资源(bundle)

#### 核心概念

###### Entry 入口

```js
webpack 分析依赖关系的起点文件

 string "./src/index.js"
 单入口形成一个chunk 输出一个bundle文件 默认名称为main
 
 array ['./src/index.js','./src/add.js']
 多入口 所有入口文件最终形成一个chunk 输出一个bundle文件  只有HMR功能中让html热更新生效
 
 obj {index:'./src/index.js',test:'./src/test/js'}
 
 多入口
 有几个入口文件就形成几个chunk 输出几个bundle文件  chunk的名称为key
 
 特殊用法
  obj {index:['./src/index1.js','./src/index2.js'],test:'./src/test/js'}
```

###### output 出口

```
打包后的资源bundles输出位置
output:{
  //文件名(指定名称+目录)
  filename:'js/[name].js'，
  //输出文件目录
  path:resolve(__dirname,"build"),
  //所有引入资源前缀
  publicPath:'/'，
  chunkFilename:'js/[name]_chunk.js' //非入口chunk的名称
   //告诉webpack不适用箭头函数
   environment:{
   arrowFunction:false
   },
  library:'[name]', //整个库向外暴露的变量名    一般结合dll将库单独打包然后引入使用
  libraryTarget:'window'  //变量名添加到browser
  libraryTarget:'global'   // 变量名添加到node上
  libraryTarget:'commonjs'  // 变量名添加到common上
}
```

###### Loader

```
webpack本身只能解析js 需要loader去处理对应的非js文件   承担翻译工作
```

###### plugins

```
增强他的能力 打包优化 压缩  重新定义环境变量
```

###### Mode

![image-20210527204516556](G:\note\image\image-20210527204516556.png)

#### 案例 开发环境

1 下载依赖

```
npm install webpack webpack-cli -D
```

2 创建文件

![image-20210527210047370](G:\note\image\image-20210527210047370.png)

```
 开发环境   入口文件           出口文件         开发环境
webpack ./src/index.js -o ./build/build.js --mode=development

 生产环境
 webpack ./src/index.js -o ./build/ --mode=production
```

###### css less打包

使用 css-loader 处理css  下载 css-loader

使用less-loader 处理less文件   下载less-loader  less

```js
/**
 * 运行webpack的配置 模块化默认commonjs
 */

// resolve 采用拼接绝对路径的方法
const {resolve} = require("path")
module.exports = {
    //webpack配置
    entry:"./src/index.js",
    //输出
    output:{
        filename:"build.js",  //输出的文件名
        path:resolve(__dirname,"build"),
    },
    // loader配置
    module:{
        rules:[
            //详细的loader配置

            {   //配置哪些文件
                test:/\.css$/,
                //使用loader进行处理
                use:[
                    // use 数组中loader执行顺序从右到左，从下到上
                    // 创建style标签 把 css通过css-loader生成js资源插入进去，添加到header中生效
                    'style-loader',
                    // 将css文件变成CommonJS模块加载到js中,内容是样式字符串
                    'css-loader'
                ]
            },
            {
                test:/\.less$/,
                use:[
                    //生成style标签 将CommonJS中的css代码 插入进去 添加到header中生效
                    "style-loader",
                    //将css解析为commonjs中的代码
                    "css-loader",
                    //将less文件解析css文件
                    "less-loader"
                ]
            }

        ]
    },
    //plugin配置
    plugins:[

    ],
    //模式
    mode:"development",
    // mode:"production"


}
```

###### html 打包

使用plugins方法打包

1 下载  html-webpack-plugin

2 引入依赖

```
const htmlWebpackPlugin = require("html-webpack-plugin")
```

3 使用

```js
//plugin配置
plugins:[
    //下载 html-webpack-plugin
    // 功能 默认创建一个空的html  自动引入打包输出的所有资源(js/css)
    // 
    new htmlWebpackPlugin({
        // 由于默认生成空的html文件，我们给定模板 按照这个模板生成HTML 然后自定引入打包输出的资源（在index.html不用引入）
        template:'./src/index.html',
        // html 压缩
        //移除空格
        collapseWhitespace:true,
          //移除注释
          removeComments:true
    })
],
```

###### 图片打包

loader处理

```js
//处理图片资源
{   
    //下载url-loader file-loader
    test:/\.(jpg|png|gif)$/,
        loader:'url-loader',
            options:{
                //图片小于8kb处理为base64编码
                // 优点 减小请求数量  （减轻服务器压力）
                // 缺点 图片体积更大  （文件请求速度更慢）
                limit:8*1204,
                    //关闭es模块化解析
                    esModule:false,
                        // 图片打包后的名字为hash值过长
                        //取其中前10位加文件扩展名
                        name:'[hash:10].[ext]'
                //
            }
},
    //处理html中img图片引入,处理后能被url-loader进行处理
    // 因为url-loader 默认使用es6模块化解析 而 html-loader 引入图片是commonjs
    // 解析是会出现src 为[object Module]
    // 解决 关闭url-loader 的es6模块化 使用commonjs解析
    {  
        test:/\.html$/,
            loader:'html-loader'
    }
```

###### 打包其他资源

排除我们定义过loader的文件

在入口index.js引入需要的  import "./iconfont.css" 文件

```js
//打包其他资源（除了html/js/css资源以外的资源）
{
    exclude:/\.(css|less|js|html)$/,
        loader:'file-loader'
}
```

###### devServer

```js
npm install webpack@5.38.0 webpack-cli@4.7.0  webpack-dev-server@4.0.0-beta.3

package中配置命令也行  "start":"webpack serve --open chrome.exe"

  // mode:"production"
    //开发服务器devServer: 用来自动化（编译 打包 刷新浏览器）
    // 特点只会在内存中编译打包，不会有任何输出
    // 启动devServer指令为npx webpack-dev-server 自动西安筑包
devServer:{
         contentBase:resolve(__dirname,"build"), //运行变打包后的文件
         //监视contentBase目录下的所有文件 一旦文件变化 就reload
         watchContentBase:true,
         //不监视以下文件
         watchOptions:{
             ignored:/node_modules/
         },
         //启动gzip压缩
         compress:true,
         //端口号
         port:3000,
         //自动打开浏览器
         open:true,
         //开启HMR 功能  修改文件 只会更新当前模块的代码
         hot:true,
         //不要显示启动服务器日志信息
         clientLogLevel:'none'，
         //除了一些基本启动信息外 其他内容给都不要显示
         quiet:true,
         //如果出错了 不要全屏显示
         overlay:false,
         //解决跨域问题
         proxy:{
             '/api':{
                 target:'http://localhost:3000',
                 pathRewrite:{
                     "^/api":''
                 }
             }
         }
     },
```

output

```js
 //打包其他资源（除了html/js/css资源以外的资源）
            {
                exclude: /\.(css|less|png|js|html)$/,
                loader: 'file-loader',
                options:{
                    outputPath:'media/'
                }
            }
```

#### 样式 生产环境

默认css会打包进js文件中

抽离css

1 下载    mini-css-extract-plugin

2 配置依赖

```js
const MiniCssExtractPlugin = require("mini-css-extract-plugin")

loader中修改
{   //配置哪些文件
    test: /\.css$/,
        //使用loader进行处理
        use: [
            // use 数组中loader执行顺序从右到左，从下到上
            // 创建style标签 把 css通过css-loader生成js资源插入进去，添加到header中生效         
            // 替代 styles-loader 不在生成style标签
            MiniCssExtractPlugin.loader,
            // 将css文件变成CommonJS模块加载到js中,内容是样式字符串
            'css-loader'
        ]
},
                

    
plugin添加
new MiniCssExtractPlugin({
    filename:'css/main.css'
})
```

###### css兼容性处理

css-loader默认没有兼容性处理的

需要 postcss-loader处理兼容性 

postcss-preset-env 去读取json的browserslist属性 兼容性处理css

1 下载插件 postcss   postcss-loader postcss-preset-env

2 使用 loader

```js

{   //配置哪些文件
    test: /\.css$/,
        //使用loader进行处理
        use: [
            // use 数组中loader执行顺序从右到左，从下到上
            // 创建style标签 把 css通过css-loader生成js资源插入进去，添加到header中生效
            MiniCssExtractPlugin.loader,

            // cssloader默认没有做兼容性处理的
            'css-loader',
            //
            {                             // 默认为生产环境如要改为开发环境 
                loader: 'postcss-loader', //postcss-preset-env 去读取json的browserslist属性进行兼容性处理css
                ident: 'postcss',
                options: {
                    postcssOptions: {
                        plugins: () => [require("postcss-preset-env")()]
                    }
                }
            }

        ]
}
```

3 json中配置browserslist

```json
 "browserslist":{
    "development":[
      "last 1 chrome version",
      "last 1 firefox version",
      "last 1 safari version"
    ],
    "production":[
      ">0.2%",
      "not dead",
      "not op_mini all"
    ]
  }
```

4 默认为生产环境如要 修改为开发环境 修改环变量

```js
process.env.NODE_MODE="development"
```

###### 压缩css

1 下载插件   optimize-css-assets-webpack-plugin

2 使用

```js
const OptimizeCssAssetsWebpackPlugin = require("optimize-css-assets-webpack-plugin");

new OptimizeCssAssetsWebpackPlugin()
```

###### eslint代码检查

1 下载依赖

```shell
npm install eslint-loader eslint eslint-config-airbnb-base eslint-plugin-import
```

2 

```js
{
    test:/\.js$/,
        exclude:/node_modules/,  本地js不检查
            loader:'eslint-loader', 
                options:{
                    fix:true
                }
}
```

3 json中配置

```js
"eslintConfig":{
    "extends":"airbnb-base"
}
```

###### js兼容性处理

生产环境下 为自动压缩js代码

```js
{
    // js兼容性处理 需要下载 babel-loader @babel/core @babel/preset-env "core-js": "^2.6.5",
    //只能转换基本原发 高级promise不能转换
    test:'/\.js$/',
        exclude:/node_modules/,
            loader:'babel-loader',
                options:{
                    presets:[[
                        '@babel/preset-env',{
                        //按需加载
                        useBuiltIns:"usage",
                        //指定core-js版本
                        corejs:{
                            version:3
                        },
                        // 指定兼容到哪个版本浏览器
                        targets:{
                            chrome:'60',
                            firefox:'60',
                            ie:'9',
                            safari:'10',
                            edge:'17'
                        }
                    }
                             ]]
                }
}
```

#### webpack优化

###### HMR

```js
devServer:{
    // contentBase:resolve(__dirname,"build"), //运行变打包后的文件
    //启动gzip压缩
    compress:true,
        //端口号
        port:3000,
            //自动打开浏览器
            open:true,
                //开启HMR 功能  修改文件 只会更新当前模块的代码
                hot:true
}
```



```yaml
HMR：hot module replacement 热模块替换/ 模块热替换
作用 一个模块发生变化，只会重新打包这一个模块(而不是打包所有模块) 极大提升构建速度
样式文件 可以使用HMR 功能 -- style-loader 内部实现了
js文件 ：默认不使用HMR功能 --> 需要修改js代码，添加支持HMR功能的代码 只对非入口js文件处理
html文件默认使用HMR功能   html为入口文件 会连带打包其他模块
```
创建print.js

```js
function print(){
    const content = "hello print";
    console.log(content);
}

export default print;
```

在js代码底部加入

```js
if (module.hot) {
  // 一旦 module.hot 为true 说明开启了HMR功能 --> 让HMR 功能代码生效
  module.hot.accept('./print.js', () => {
    // 方法会监听print.js 文件的变化 一旦发生变化 其他模块不会重新打包构建
    // 会执行后面的回调函数
    print();
  });
}

```

###### source-map

建立打包后的代码和源代码的映射关系！把报错信息定位到到源代码的位置

mode下添加

```js
devtool:'source-map'
```

###### oneOf

每个文件只用一个loader处理

不能有两个loader处理相同的文件如果相同抽出一个放到oneOf外面

```js
 module:{
        rules:[
          oneOf:[
                // css处理
            {
                test:/\.css$/,
                use:[
                   ...commonCssLoader
                ]
            },
            {
                test:/\.less$/,
                use:[
                    ...commonCssLoader,
                    'less-loader'
                ]
            }
             ]
       ]
}      
```

###### 缓存

```js
{
    test:/\.js$/,
        exclude:/node_modules/,
            loader:'babel-loader',
                options:{
                    //开启babel缓存 第二次构建 会读取之前的缓存
                    cacheDirectory:true,
                        presets:[
                            [
                                '@babel/preset-env',{
                                    useBuiltIns:'usage',
                                    corejs:{version:3},
                                    targets:{
                                        chrome:'60',
                                        firefox:'50'
                                    }   
                                }
                            ]

                        ]
                }
},
```

如果把代码放到服务器上，浏览每次请求先看看本地有没有，如果没有就去服务器请求文件，如果有加载本地文件

如果我们要更新服务器代码！就要给文件名后添加一个变量，让我们每次发布的文件名不同，让浏览器去服务器请求最新的文件

```js
给我们的打包生成的文件名后添加contenthash值 
output:{
    filename:'main.[contenthash:10].js',
        path:resolve(__dirname,"build")
},

    new MiniCssExtractPlugin({
        filename:'css/main.[contenthash:10].css'
    }),
        
hash  每次打包生成新的随机值
chunkhash  属于相同的chunk生成相同的随机值    一个入口文件引入的其他资源  就是一个chuhk
contenthash 如果内容相同生成相同的随机值      
```

###### tree shaking

打包时候去除那些我们没有使用的代码

前提条件

```
1 必须使用ES6 模块化
2 开启production环境

副作用可能会把css / @babel/其他文件也给我去除
在package.json中配置
"sideEffects":["*.css"，"*.less"]  //规定不处理的文件
```

###### code split

webpack 打包默认生成一个js文件

分割js文件

```js
1 多入口
entry:{
   index:'./src/index.js',
   test:'./src/test.js'
}

2 webpac中的配置
 自动将node-modules中的代码打包成单独js
 自动分析入口文件中，有没有公共的文件，如果有单独打包成一个chunk
 optimization:{
     splitChunks:{
         chunks:'all'
     }，
     //将当前模块记录其他模块的hash单独打包成文一个runtime
     runtimeChunk:{
         name:entrypoint => `runtime-${entrypoint.name}`
     }，
     minimizer:[
         // npm install terser-webpack-plugin --save-dev
         new TerserWebpackPlugin({
             //开启缓存
             cache:true,
             //开启多进程打包
             parallel:true,
             //启动source-map
             sourceMap:true
         })
     ]

}
假如 a文件引入b文件 他们的打包后的文件名都添加contenthash值 ，如果b文件内容发生变化他的hash就变化同时a文件引入b文件的，所以a文件记录了b文件的hash值，b 文件变化 引起a文件也变化。所有我们要把hash存放到a文件的外部
```

懒加载与预加载

```js
懒加载  当文件使用时才加载
预加载 prefetch 会在使用之前，提前加载js文件在其他文件加载完毕后 再加载
正常加载  同时加载多个文件

document.getElementById('btn').onClick = function(){         //解构出test文件中的nul方法
   import(/* webpackChunkName:'test',webpackPrefetch:true */,'./test').then(({nul})=>{
      console.log(nul(4,5))
   })
}
```

###### PWA

渐进式网络开发程序

1 添加 Workbox

添加 workbox-webpack-plugin 插件，然后调整 `webpack.config.js` 文件：

```
npm install workbox-webpack-plugin --save-dev
```

2 **webpack.config.js**

![image-20210531150734807](G:\note\image\image-20210531150734807.png)

完成这些设置，再次执行 `npm run build`，看下会发生什么：

![image-20210531150827715](G:\note\image\image-20210531150827715.png)

注册 Service Worker

```
eslint不认识window navigator全局变量
解决 需要修改package.json中eslintConfig配置
"env":{
  "browser":true  //支持浏览器端变量
}
```



![image-20210531150851523](G:\note\image\image-20210531150851523.png)

###### 多进程打包

需要下载thread-loader

在需要开启多线程打包的loader前添加次loader 开启多线程

![image-20210531151851206](G:\note\image\image-20210531151851206.png)

###### externals

如果某些包是通过CDN 引入的

```
externals:{
  //库名   npm 中的包名
  jquery:'JQuery'  //打包时不打包jquery文件
}
```

###### dll

webpack 默认把node_modules中的打包生成一个js文件

创建webpack.dll.js文件

```js
const {resolve} = require('path');
const webpack = require("webpack");
module.exports = {
    entry:{
        //最终打包生成[name] -> jquery
        // ['jquery'] -> 要打包的库是jquery
        jquery:['jquery']
    },
    output:{
        filename:'[name].js',
        path:resolve(__dirname,"dll"),
        libaray:'[name]_[hash]' //打包的库里面向外暴露出去的内容叫什么名字
    },
    plugins:[
        //打包升恒一个manifest.json -> 提供和jquery映射
        new webpack.DllPlugin({
            name:'[name]_[hash]',
            path:resolve(__dirname,"dll/manifest.json ")
        })
    ]
}

```

webpack中相应配置

加入以下plugin

```js
        // npm install  add-asset-html-webpack-plugin
        // 告诉webpack哪些库不参与打包 同时使用的名称也的边~
        new webpack.DllReferencePlugin({
            manifest:resolve(__dirname,"dll/manifest.json ")
        }),
        //由于index.html中只引入index.js而webpack打包时没有打包jQuery文件，并在html中自动引入该资源
        new AddAssetHtmlWebpackPlugin({
            filepath:resolve(__dirname,"dll/jquery.js")
        })
```

#### resolve

```js
//解析模块的规则
resolve:{
    //配置解析模块路径别名
    alias:{
        $css:resolve(__dirname,"src/css")
    },
        //配置省略文件路径的后缀名
        extensions:['.js','.json','.jsx','.css'],
            //指定node_modules的位置 如果没有指定会一层层向上寻找
            modules: [path.resolve(__dirname, 'src'), 'node_modules']
}
```

#### webpack5

