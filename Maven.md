#### Maven

构建（build），是面向过程的(从开始到结尾的多个步骤)，涉及到多个环节的协同工作。

##### 构建过程的几个主要环节

①清理：删除以前的编译结果，为重新编译做好准备。
②编译：将Java源程序编译为字节码文件。
③测试：针对项目中的关键点进行测试，确保项目在迭代开发过程中关键点的正确性。
④报告：在每一次测试后以标准的格式记录和展示测试结果。
⑤打包：将一个包含诸多文件的工程封装为一个压缩文件用于安装或部署。Java 工程对应 jar 包，Web
工程对应war包。
⑥安装：在Maven环境下特指将打包的结果——jar包或war包安装到本地仓库中。
⑦部署：将打包的结果部署到远程仓库或将war包部署到服务器上运行

##### maven核心概念:

①POM ： 一个文件 名称是pom.xml ,  pom翻译过来叫做项目对象模型。 
         maven把一个项目当做一个模型使用。控制maven构建项目的过程，管理jar依赖。

②约定的目录结构 ： maven项目的目录和文件的位置都是规定的。

③坐标 ： 是一个唯一的字符串，用来表示资源的。

④依赖管理 ： 管理你的项目可以使用jar文件

⑤仓库管理（了解） ：你的资源存放的位置

⑥生命周期 (了解) ： maven工具构建项目的过程，就是生命周期。
⑦插件和目标（了解）：执行maven构建的时候用的工具是插件
⑧继承
⑨聚合

#### 安装 Maven 环境

1、 确保安装了 java 环境:maven 本身就是 java 写的，所以要求必须安装 JDK。
查看 java 环境变量：echo %JAVA_HOME%
2、 下载并解压 maven 安装程序：
http://maven.apache.org/download.cgi
3、 配置 Maven 的环境变量：
MAVEN_HOME=d:/apache-maven-3.3.9 或者 M2_HOME=d:/apache-maven-3.3.9
path=%MAVEN_HOME%/bin; 或者%M2_HOME%/bin;
4、 验证是否安装成功:
mvn –v

#### 目录结构

![image-20201222084648490](G:\note\image\image-20201222084648490.png)



```

说明：Hello:根目录，也就是工程名
src：源代码
main：主程序
java：主程序的 java 源码
resources：主程序的配置文件
 test：测试程序
java：测试程序的 java 源码
resources：测试程序的配置文件
pom.xml：Maven 工程的核心配置文件。
一般情况下，我们习惯上采取的措施是：约定>配置>编码
maven 的 pom.xml 记录的关于构建项目的各个方面的设置，maven 从 pom.xml 文件开始，按照助约定的
工程目录编译，测试，打包，部署，发布项目。
```

#### Maven 的常用命令

Maven 对所有的功能都提供相对应的命令，要想知道 maven 都有哪些命令，那要看 maven 有哪些功能。
一开始就跟大家说了，maven 三大功能：管理依赖、构建项目、管理项目信息。管理依赖，只需要声明就可以自
动到仓库下载；管理项目信息其实就是生成一个站点文档，一个命令就可以解决，最后再说；那 maven 功能的
主体其实就是项目构建。
Maven 提供一个项目构建的模型，把编译、测试、打包、部署等都对应成一个个的生命周期阶段，并对
每一个阶段提供相应的命令，程序员只需要掌握一小堆命令，就可以完成项目的构建过程。
mvn clean 清理(会删除原来编译和测试的目录，即 target 目录，但是已经 install 到仓库里的包不会删除)
mvn compile 编译主程序(会在当前目录下生成一个 target,里边存放编译主程序之后生成的字节码文件)
mvn test-compile
编译测试程序(会在当前目录下生成一个 target,里边存放编译测试程序之后生成的字节码文件)
mvn test 测试(会生成一个目录surefire-reports，保存测试结果)
mvn package
打包主程序(会编译、编译测试、测试、并且按照 pom.xml 配置把主程序打包生成 jar 包或者 war 包)
mvn install 安装主程序(会把本工程打包，并且按照本工程的坐标保存到本地仓库中)
mvn deploy 部署主程序(会把本工程打包，按照本工程的坐标保存到本地库中，并且还会保存到私服仓库中。
还会自动把项目部署到 web 容器中)。
`注意：执行以上命令必须在命令行进入 pom.xml 所在目录！`

#### pom.xml 初识：

![image-20201222092022148](G:\note\image\image-20201222092022148.png)

#### 仓库

一 本地仓库

存在于当前电脑上,默认存放在~\.m2\repository中,为本机上所有的Maven工程服务。你也可以
通过Maven的配置文件Maven_home/conf/settings.xml中修改本地仓库所在的目录。
~ 是用户的主目录，windows系统中是 c：/user/登录系统的用户名

二 远程仓库，分为为全世界范围内的开发人员提供服务的中央仓库、为全世界范围内某些特定的用户提供服务的
中央仓库镜像、为本公司提供服务自己架设的私服。中央仓库是maven默认的远程仓库，其地址
是:http://repo.maven.apache.org/maven2/

#### IDEA整合Maven

![image-20201222101703281](G:\note\image\image-20201222101703281.png)

![image-20201222101748901](G:\note\image\image-20201222101748901.png)

#### IDEA创建Maven项目

![image-20201222131040088](G:\note\image\image-20201222131040088.png)

![image-20201222131050478](G:\note\image\image-20201222131050478.png)

pom.xml 加入依赖

```xml
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

#### 依赖管理

![image-20201222132127151](G:\note\image\image-20201222132127151.png)

![image-20201222131158319](G:\note\image\image-20201222131158319.png)

全局变量

在 Maven 的 pom.xml 文件中，<properties>用于定义全局变量，POM 中通过${property_name}的形式引用变量的值。
定义全局变量：

```
<properties>
<spring.version>4.3.10.RELEASE</spring.version>
</properties>
```

引用全局变量：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>${spring.version}</version>
</dependency>
```

#### Maven 系统采用的变量：

```xml
<properties>
<maven.compiler.source>1.8</maven.compiler.source> 源码编译 jdk 版本
<maven.compiler.target>1.8</maven.compiler.target> 运行代码的 jdk 版本
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding> 项目构建使用的编码，避免中文乱
码
<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding> 生成报告的编码
</properties>
```

#### 指定资源位置

src/main/java 和 src/test/java 这两个目录中的所有*.java 文件会分别在 comile 和 test-comiple 阶段被编译，编
译结果分别放到了 target/classes 和 targe/test-classes 目录中，但是这两个目录中的其他文件都会被忽略掉，如果需
要把 src 目录下的文件包放到 target/classes 目录，作为输出的 jar 一部分。需要指定资源文件位置。以下内容放到
<buid>标签中。

```xml
<build>
    <resources>
    <resource>
        <directory>src/main/java</directory><!--所在的目录-->
        <includes><!--包括目录下的.properties,.xml 文件都会扫描到-->
        <include>**/*.properties</include>
        <include>**/*.xml</include>
        </includes>
        <!—filtering 选项 false 不启用过滤器， *.property 已经起到过滤的作用了 -->
        <filtering>false</filtering>
    </resource>
    </resources>
</build>
```

