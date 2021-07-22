grun自动化构建工具

##### 使用

1 全局安装

```
npm install -g grunt-cli
```

执行此命令等于把grunt加入到系统路径了，Grunt CLI的任务很简单：调用与`Gruntfile`在同一目录中 Grunt。

2 项目内安装grunt

```
npm install grunt --save-dev
```

3 项目根路径下创建Gruntfile.js文件

```js
module.exports = function (grunt) {
    //初始化配置grunt任务
    grunt.initConfig({
        concat: {
            options: {
                separator: ';' //合并的js以;分开
            },
            dist: {
                src: ['src/js/*.js'],
                dest: 'build/js/build.js'
            }
        },
        //压缩文件
        uglify: {
            my_target: {
                files: {
                    'build/js/build.min.js': ['build/js/build.js']
                }
            }
        },
        jshint: {
            ignore_warning: {
                options: {
                    '-W015': true,
                    jshintrc: '.jshintrc'
                },
                src: ['src/**/*.js'], //指定检查文件
            },
        },
        cssmin: {
            options: {
                mergeIntoShorthands: false,
                roundingPrecision: -1
            },
            target: {
                files: {
                    'build/css/build.min.css': ['src/css/*.css']
                }
            }
        },
        watch: {
            scripts: {
              files: ['src/js/*.js',"src/css/*.css"],
              tasks: ["jshint", "concat", "uglify","cssmin"],
              options: {
                spawn: false,  //false 更新修改的文件  true更新全部的文件
              },
            },
          },
    })
    // 2 加载插件任务
    grunt.loadNpmTasks("grunt-contrib-concat");
    grunt.loadNpmTasks("grunt-contrib-uglify");
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-watch');
    //3 注册构建任务            执行任务顺序同步的从左到右
    grunt.registerTask('default', ["jshint", "concat", "uglify","cssmin","watch"])
}
```

##### 常用插件

```
grunt-contrib-clean 清除打包生成的文件
grunt-contrib-concat 合并多个文件到一个文件
grunt-contrib-uglify 压缩js
grunt-contrib-cssmin  压缩合并css文件
grunt-contrib-jshint-javascript 语法检查
grunt-contrib-htmlmin 要说html文件
grunt-contrib-imagemin 压缩图片文件(无损)
grunt-contrib-copy 复制文件,文件夹
grunt-contrib-watch 监视文件变化 调用相应的任务重新执行
```

##### 合并js

1 安装插件

```
npm install grunt-contrib-concat --save-dev
```

2 配置

```js
module.exports = function(grunt){
    //初始化配置grunt任务
    grunt.initConfig({
        concat:{
            options:{
                separator:';' //合并的js以;分开
            },
            dist:{
                src:['src/js/*.js'],
                dest:'build/js/build.js'
            }
        }
    })
    // 2 加载插件任务
    grunt.loadNpmTasks("grunt-contrib-concat");
    //3 注册构建任务
    grunt.registerTask('default',[])
}
```

