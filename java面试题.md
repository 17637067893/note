###### 1 面向对象和面向过程

面向对象概念

把现实生活中的事物的属性和行为，使用对象映射出来

面向过程

分析解决问题的步骤，然后一步步去实现它。

###### 2 封装继承多态

封装 把对象的属性和行为封装起来，只需要提供结果，外部不需要知道内部的具体实现

继承 主要为了代码的复用

多态 

同一个行为具有不同的表现形式

三个条件

```
1 继承
2 重写
3 父类引用指向子类对象

优点
1 建耦合
2 灵活性
3 可扩展性
```

实例

```java
/**
 * 支付抽象类或者接口
 */
public abstract class Pay {
    abstract public String pay();
}
/**
 * 支付宝支付
 */
public class AliPay extends Pay {
    @Override
    public String pay() {
        System.out.println("支付宝pay");
        return "success";
    }
}
/**
 * 微信支付
 */
public class WeixinPay extends Pay {
    @Override
    public String pay() {
        System.out.println("微信Pay");
        return "success";
    }
}
/**
 * 银联支付
 */
public class YinlianPay extends Pay {
    @Override
    public String pay() {
        System.out.println("银联支付");
        return "success";
    }
}

测试支付
public static void main(String[] args) {
 /**
      * 测试支付宝支付多态应用
      */
    Pay pay = new AliPay();
    pay.pay();
 /**
      * 测试微信支付多态应用
      */
    pay = new WeixinPay();
    pay.pay();
 /**
      * 测试银联支付多态应用
      */
    pay = new YinlianPay();
    pay.pay();
}

------>输出结果如下：

支付宝pay
微信Pay
银联支付
```

###### 3 ==和equals的区别

==

基本类型比较值是否相同

引用类型比较的是引用地址是否相同

```
String x = "string";
String y = "string";
String z = new String("string");
System.out.println(x==y); // true
System.out.println(x==z); // false
System.out.println(x.equals(y)); // true
System.out.println(x.equals(z)); // true
```

equals本质就是 == 只不过内部重写了equals方法 把 == 该为了值得比较

###### 4 instanceof

判断一个对象是否属于 一个类的子类 ，一个接口的实现类 接口实现类的子类

![image-20210621115208274](G:\note\image\image-20210621115208274.png)

```java
interface A{}
class B implements A{

}
class C extends B {

}
class InstanceofDemo {
    public static void main(String[] args){
        A a=null;
        boolean res;

        System.out.println("instanceoftest test case 1: ------------------");
        res = a instanceof A;
        System.out.println("a instanceof A: " + res);  // false


        System.out.println("/ninstanceoftest test case 2: ------------------");
        a=new B();

        res = a instanceof A;  // a引用的是A接口实现 类的实例，所以true
        System.out.println("a instanceof A: " + res);

        res = a instanceof B;  // a引用的是B类的实例，所以true
        System.out.println("a instanceof B: " + res);

        System.out.println("/ninstanceoftest test case 3: ------------------");
        B b2=new C();

        res = b2 instanceof A; // b2引用的是A接口实现类子类的实例，所以true
        System.out.println("b2 instanceof A: " + res);

        res = b2 instanceof B; // b2引用的是B类子类的实例，所以true
        System.out.println("b2 instanceof B: " + res);


        res = b2 instanceof C; // b2引用的就是C类的实例，所以true

        System.out.println("b2 instanceof C: " + res);
    }
}
```

###### 5 hashcode

相同对象hashcode一定相同，相同hashcode的对象不一定相等

```
hashcode 根据对象的地址或字符串的值或数字算出的int类型的值
hashcode作用作用提高查询效率
在散列表中(哈希表)  根据key value直接访问数据
向不重复集合中插入元素时，如果元素过多时 使用equals比较时 会逐个比较效率低，如果插入新的对象时 先调用对象的hashcode方法
获取hashcode值，集合的hashcode表判断是否已经有这个值，如果有再比较equals的值
```

6 抽象类和接口

抽象类

1 不能实例化，只能被继承

2 可以有抽象方法也可以没有

3 修饰符默认public

接口

1 可以实现多个

2 默认修饰符为 public static final

3 被default修饰的方法不能被实现 只能被子类实现对象调用

4 被static的方法只能通过接口名调用 

抽象类和接口的使用时机

```
1 一般使用接口比较多
2 如果想要代码的复用就使用抽象类
3 如果想要实现多重继承，使用接口
```

###### 6 IO流

```
1 按功能有  输入流，输出流
2 按类型 8位字节流 16位字符流 
```

#### 7 BIO NIO AIO

##### BIO 

同步阻塞式IO  客户端每次请求都会分配一个线程处理

代码实现

```java
客户端
    public class Client {
        public static void main(String[] args) throws IOException {
            // 1 创建socket对象请求服务端的链接
            Socket socket = new Socket("127.0.0.1", 9999);
            // 2 获取一个字节输入流
            OutputStream outputStream = socket.getOutputStream();
            //3 把字节输出流转换成打印流
            PrintStream printStream = new PrintStream(outputStream);
            printStream.println("服务端你好");
            printStream.flush();
        }
    }

服务端

    public class Server {
        public static void main(String[] args) throws IOException {
            System.out.println("服务器启动成功");
            //创建socket 监听端口注册服务
            ServerSocket serverSocket = new ServerSocket(9999);
            // 2 监听服务端的socket连接请求
            Socket accept = serverSocket.accept();
            // 3 从socket管道中得到一个字节输入流对象
            InputStream inputStream = accept.getInputStream();
            //4 把字节输入流转换成字符输入流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String msg;
            if((msg = bufferedReader.readLine())!=null){
                System.out.println("服务端接收到数据"+msg);
            }
        }
    }

```

单个客户端接收多个客户端

```java
客户端类
public class Client {
    public static void main(String[] args) throws IOException {
        // 1 创建socket对象请求服务端的链接
        Socket socket = new Socket("127.0.0.1", 9999);
        // 2 获取一个字节输入流
        OutputStream outputStream = socket.getOutputStream();
        //3 把字节输出流转换成打印流
        PrintStream printStream = new PrintStream(outputStream);
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("请输入");
            String s = scanner.nextLine();
            printStream.println(s);
            printStream.flush();
        }

    }
}

服务端类
public class Server {
    public static void main(String[] args) throws IOException {

        //接受多个客户端
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while (true){
                Socket accept = serverSocket.accept();
                //开启新的线程
                new ManyServer(accept).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
线程类
public class ManyServer extends Thread{
    private Socket socket;
    public ManyServer(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream  inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String msg;
            while ((msg = bufferedReader.readLine())!=null){
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

使用线程池伪异步

```
客户端
public class Client {
    public static void main(String[] args) throws IOException {
        // 1 创建socket对象请求服务端的链接
        Socket socket = new Socket("127.0.0.1", 9999);
        // 2 获取一个字节输入流
        OutputStream outputStream = socket.getOutputStream();
        //3 把字节输出流转换成打印流
        PrintStream printStream = new PrintStream(outputStream);
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("请输入");
            String s = scanner.nextLine();
            printStream.println(s);
            printStream.flush();
        }
    }
}

服务端
public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9999);
            SocketServerPool socketServerPool = new SocketServerPool(3, 10);
            while (true){
                //创建socket
                Socket accept = serverSocket.accept();
                // 把socket封装成任务对象
                ServerRunnableTarget serverRunnableTarget = new ServerRunnableTarget(accept);
                // 把任务对象交线程池
                socketServerPool.execute(serverRunnableTarget);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

任务队列
public class ServerRunnableTarget implements Runnable{
    private Socket socket;
    public ServerRunnableTarget(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String msg;
            while ((msg = bufferedReader.readLine())!=null){
                System.out.println(msg);
            }
        }catch ( Exception e){
            e.printStackTrace();
        }
    }
}

线程池
public class SocketServerPool {
    private ExecutorService executorService;

    /**
     *  public ThreadPoolExecutor(int corePoolSize,
     *                               int maximumPoolSize,
     *                               long keepAliveTime,
     *                               TimeUnit unit,
     *                               BlockingQueue<Runnable> workQueue)
     */
    public SocketServerPool(int max,int queuesize){
        executorService = new ThreadPoolExecutor(3, max, 120, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queuesize));
    }
    public void execute(Runnable target){
        executorService.execute(target);
    }
}
```

##### NIO

 同步非阻塞IO 

服务器开启一个线程管理多路复用器，客户端每次请求都会注册到多路复用器上，多路复用器轮询链接，有IO请求就处理

![image-20210621211451711](G:\note\image\image-20210621211451711.png)

NIO三大核心 

buffer channel selector

###### buffer

类似于数组的数据结构

七种类型

```
ByteBuffer
ShortBuffer
CharBuffer
IntBuffer
LongBuffer
FloatBuffer
DoubleBuffer
```

缓冲区的基本属性

```
1 容量
2 限制 缓冲区可以操作数据的大小
3 位置 下一次读写的位置
4 标记mark 可以指定操作的位置
5 重置reset
大小关系
0<=mark<=position<=limit<=capacity
```

buffer API

![image-20210622105558759](G:\note\image\image-20210622105558759.png)

![image-20210622105611542](G:\note\image\image-20210622105611542.png)

使用Buffer读写数据四个步骤

```
1 写入数据到buffer
2 调用flip方法转换为读取模式
3 从buffer中读取数据
4 调用buffer.clear()方法或者buffer.compact()方法清除缓冲区
```

Buffer两种类型

```
一种是直接内存(非堆内)allocateDirect创建 一种非直接内存(堆内)allocate创建
JVM 操作直接内存具有更高的性能，他直接使用本地系统的IO操作，而非直接内存，也就是堆内中，要先从进程内存复制到直接内存中，在利用本地IO处理
从数据流的角度非直接内存
本地IO->直接内存->非直接内存->直接内存->本地IO
直接内存中
本地IO->直接内存->本地IO
```

###### channel通道

channel表示IO源于目标打开的链接，channel类似与传统的流，只不过不能直接操作数据，channel只能与Buffer进行交互

NIO的通道与流的区别

1. 通道双向的可以同时读写，而流是单向的只能读或者写

2. 通道可以实现异步读取数据

   常见的Channel实现类

   1. FileChannel 用于读写映射和操作文件的通道
   2. DatagramChannel通过UDP读写网络中的数据通道
   3. SocketChannel通过TCP读写网络中的数据通道
   4. ServerSocketChannel 可以监听新进来的TCP连接，对每一个新进来的连接都会创建一个SocketChannel(ServerSocketChannel类似于ServerSocket SocketChannel类似于Socket)

FileChannel的常用方法

```
int read(ByteBuffer dst) 从Channel通道中读取数据到ByteBuffer
long read(ByteBuffer[] dsts) 将channel通道中的数据分散到ByteBuffer[]
int write(ByteBuffer[] srcs) 将ByteBuffer[]写输入Channel
long write(ByteBuffer[] srcs) 将ByteBuffer[]中的数据聚集到channel中
long position 返回此通道的位置
Finlechannel position(long p) 设置此通道的位置
long size() 返回此通道的文件的当前大小
FileChannel truncate(long s)将此通道的文件截取为指定大小
void force(boolean metaData) 强制将所有对此通道的文件更新写入到存储设备中
```

通道写入文件

```java
@Test
public void test1() throws IOException {
    //通过文件拿到通道
    FileOutputStream fileOutputStream = new FileOutputStream("src/test.txt");
    FileChannel channel = fileOutputStream.getChannel();
    //创建缓冲区并添加数据
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    buffer.put("这是通道".getBytes());

    //写入数据
    buffer.flip();
    channel.write(buffer);
    channel.close();
    System.out.println("写入成功");
}
```

通道读取文件

```java
@Test
public void test2() throws IOException {
    FileInputStream fileInputStream = new FileInputStream("src/test.txt");
    FileChannel channel = fileInputStream.getChannel();

    ByteBuffer buffer = ByteBuffer.allocate(1024);
    channel.read(buffer);
    buffer.flip();

    //读取数据                              //写入多少  读取多少
    String s = new String(buffer.array(), 0, buffer.remaining());
    System.out.println(s);
}
```

通道复制文件

```java
@Test
public void test3() throws IOException {
    FileInputStream f1= new FileInputStream("src/test.txt");
    FileOutputStream f2 = new FileOutputStream("src/test1.txt");
    FileChannel channel1 = f1.getChannel();
    FileChannel channel2 = f2.getChannel();

    ByteBuffer buffer = ByteBuffer.allocate(1024);
    while (true){
        buffer.clear();
        int read = channel1.read(buffer);
        if(read == -1){
            break;
        }
        buffer.flip();
        channel2.write(buffer);
    }
    f1.close();
    f2.close();
    System.out.println("复制完成");
}
```

分散和聚集

 分散 通道中的数据过多 分散到多个buffer中

聚集 多个buffer中的数据聚集到一个通道中

```java
@Test
public void test4() throws Exception {
    FileInputStream f1= new FileInputStream("src/test.txt");
    FileOutputStream f2 = new FileOutputStream("src/test1.txt");
    FileChannel channel1 = f1.getChannel();
    FileChannel channel2 = f2.getChannel();
    //定义多个缓冲区
    ByteBuffer buffer1 = ByteBuffer.allocate(4);
    ByteBuffer buffer2 = ByteBuffer.allocate(1024);
    ByteBuffer[] buffers = {buffer1,buffer2};
    //从通道中读取数据分散到多个缓冲区
    channel1.read(buffers);
    for (ByteBuffer buffer:buffers){
        buffer.flip();
        System.out.println(new String(buffer.array(),0,buffer.remaining()));
    }
    //从多个缓冲区聚集到通道中
    channel2.write(buffers);
    channel1.close();
    channel2.close();
    System.out.println("文件赋值完成");
}
```

TransFrom和TransferTo

```java
@Test
public void test5() throws Exception {
    FileInputStream f1= new FileInputStream("src/test.txt");
    FileOutputStream f2 = new FileOutputStream("src/test3.txt");
    FileChannel channel1 = f1.getChannel();
    FileChannel channel2 = f2.getChannel();
    // transferFrom 把channel1里的数据复制到channel2
    channel2.transferFrom(channel1,channel1.position(),channel1.size());
    // channel1.transferTo(channel1.position(),channel1.size(),channel2);
    channel1.close();
    channel2.close();
    System.out.println("复制完成");
}
```

###### selector选择器

selector是SelectableChannle对象的多路复用器，Selector可以同时监控多个SelectableChannel的IO状况，也就是说可以使用一个单独的线程管理多个Channel,selector是非阻塞IO的核心

![image-20210622135718010](G:\note\image\image-20210622135718010.png)

选择器的应用

创建选择器：通过Selector.open()方法创建一个Selector

```
Selector selector = Selector.open();
```

向选择器注册通道 SelectableChannel.register(Selector sel,int ops)

```
// 1 获取通道

```



AIO(NIO2) 异步非阻塞IO

服务器实现模式为一个有效请求一个线程，客户端的IO请求都是由OS先完成了在通知服务器应用去启动线程处理，一般使用与链接数较多链接时间较长的应用

应用场景分析

```
BIO 使用适用于链接数目比较小
NIO 适用于连接数目多且比较短的架构 如连天服务器 弹幕系统 
AIO 连接数目多且连接时间长的架构 比如相册服务器 充分调用OS参与并发操作
```

