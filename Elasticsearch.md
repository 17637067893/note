#### Elasticsearch

###### 介绍

elasticsearch



##### Term Index

B-Tree通过减少磁盘寻道次数来提高查询性能，Elasticsearch也是采用同样的思路，直接通过内存查找term，不读磁盘，但是如果term太多，term dictionary也会很大，放内存不现实，于是有了**Term Index**，就像字典里的索引页一样，A开头的有哪些term，分别在哪页，可以理解term index是一颗树：

term的一些前缀。通过term index可以快速地定位到term dictionary的某个offset，然后从这个位置再往后顺序查找。

所以term index不需要存下所有的term，而仅仅是他们的一些前缀与Term Dictionary的block之间的映射关系，再结合FST(Finite State Transducers)的压缩技术，可以使term index缓存到内存中。从term index查到对应的term dictionary的block位置之后，再去磁盘上找term，大大减少了磁盘随机读的次数。



**传统数据库** 

 数据存储打磁盘上，由于磁盘的特性(顺序性/随机性),遍历读取的数据 获得我们想要的数据

**elasticsearch** 

把数据存到内存中，通过倒排索引，为了解决数据量过大 term index(索引)只会存储的前缀和磁盘的映射信息在利用FST压缩技术把他存储到内存中
读取时 通过内容去内存中找到， 数据在磁盘的位置，直接磁盘位置读取，大大减少磁盘随机读取次数

