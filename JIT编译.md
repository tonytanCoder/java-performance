## JIT编译

### 热点编译

通常只有一部分代码被经常执行，而应用的性能取决于这些代码执行得有多快，这些关键代码段就被称为应用得热点，代码执行得越多就被认为是越热。

 JVM执行代码时，并不会立即编译代码，有两个基本理由：

第一，如果代码只执行一次，编译完全就是浪费时间，对于只执行一次的代码，解释执行java字节码比先编译后执行的速度快。

第二是为了优化:JVM执行特定方法或循环的次数越多，它会越了解这段代码，这使得JVM在编译代码时可以做大量的优化。

### 选择编译器类型

有两种类的JIT编译器。分别为client和server。client编译器通常又称为C1，server编译器通常又称为C2编译器。两中编译器最主要的差别在于编译时机的不同，C1开启的时间要比C2要早。意味着在代码执行的开始阶段，C1要比C2快。但C2在编译代码时可以更好的进行优化。

  JVM可以在启动时用C1，随者代码变热使用C2,这种技术被称为分层编译。

### 编译器调优

#### 调优代码缓存

JVM编译代码时会在代码缓存中保留编译之后的汇编语言指令集。代码缓存的大小固定，一旦填满，JVM就不能编译更多代码了。

 通常C1编译器和分层编译时容易出现代码缓存填满的情况。

可通过 `jinfo -flag ReservedCodeCacheSize pid` 进行查看大小。

JVM启动的时候，Code Cache所需内存会被单独初始化，这时候Java堆还会被初始化，所以Code Cache和Java堆是两块独立内存区域。

通过 `-XX:ReservedCodeCacheSize`参数可以指定Code Cache的初始化大小，这个默认值在不同的JDK版本也不同。

#### 编译阈值

编译时基于两种JVM计数器的：方法调用计数器和方法中的循环回边计数器。回边实际上可看作是循环完成执行的次数。

JVM执行某个java方法时，会检查该方法的两种计数器总数，然后判断方法是否适合编译。如果适合就进入编译队列。

 但如果循环很长，或因包含程序逻辑永远不退出，又会怎么样呢。这种情况下JVM不等方法调用玩就会编译循环。所以循环每完成一轮，回边计数器就会增加并见检查是否符合编译要求。这种编译称为栈上替换（OSR），JVM会替换还在栈上的代码，循环的下次迭代就会执行得快得多。

标准编译由-XX:CompileThreshold=10000标志触发。

我们可以通过以下命令查看当前值：

C:\Users\tanzl>jinfo -flag CompileThreshold  972
-XX:CompileThreshold=10000

某些方法永远都不会被编译，这是为什么呢？并不是这些方法没有达到编译阈值，而是永远达不到，这是因为虽然计数器随着方法和循环的执行而增加，但他们也会随时间而减少。每种计数器的值都会周期性减少（特别时当JVM达到安全点时）。

#### 检测编译过程

通过参数-XX:-PrintCompilation可以开启编译日志。

%：编译为OSR

s：方法是同步的

！：方法有异常处理器

b：阻塞模式时发生的编译

n：为封装本地方法所发生的编译。

#### 高级编译器调优

##### 编译线程

代码的编译是通过后台编译的线程去编译的。JVM把有资格编译的方法或者循环代码放入到编译队列中，这个队列是一个优先级队列，调用次数越高，优化级越高。所以代码的编译是在后台独立进行的，不会阻塞我们的应用程序。一般情况下：client模式会启动一个编译线程,server模式启动两个编译线程，这些可以通过 -XX:CompileThreshold进行调整。

分层模式下如果是64位的虚拟机编译线程的总数目是根据处理器数量来调整的，当然也可以通常参数-XX:+CICompilerCount=N来强制指定。分层模式下client编译线程和server编译线程的比例是1 ：2。比如一个4核CPU有三个编译编译线程，则1个为client,两个为server。

需要注意的是如果你的系统CPU资源一直都是比较紧张，可以适当减少一下编译线程的数量，以防止编译线程和应用程序去竞争CPU，不过大多情况下都是不需要作任何调整的。

##### 内联

编译器做的最重要的优化就是方法内联。

什么时内联？简单通俗的讲就是把方法内部调用的其它方法的逻辑，嵌入到自身的方法中去，变成自身的一部分，之后不再调用该方法，从而节省调用函数带来的额外开支。

之所以出现方法内联是因为函数调用除了执行自身逻辑的开销外，还有一些不为人知的额外开销。**这部分额外的开销主要来自方法栈帧的生成、参数字段的压入、栈帧的弹出、还有指令执行地址的跳转**。

###### 参数

```
# 获取所有 inline 匹配的参数
java -XX:+PrintFlagsFinal -version | grep -i "inline"

# 如果生成的 native code 大小小于这个, 则 inline
-XX:InlineSmallCode=2000

# 最大允许进行 inline 的 byte code 大小. 超过的话, 会提示 too big
-XX:MaxInlineSize=35

# 经常被调用的方法的进行 inline 的最大 byte code 大小
-XX:FreqInlineSize=325

# 最大 inline 的调用层级. 超过的话, 会提示 inlining too deep
-XX:MaxInlineLevel=9

# 一个方法被 inline 的最小调用次数
-XX:MinInliningThreshold=250

# JVM 判断一个方法是否是 hot . 被调用 1W 次
## https://stackoverflow.com/questions/18345089/what-does-compilethreshold-tier2compilethreshold-tier3compilethreshold-and-tie
## java -XX:+PrintFlagsFinal -version | grep -i "CompileThreshold"
-XX:CompileThreshold=10000
```

jvm可以通过两个启动参数来控制字节码大小为多少的方法可以被内联：

- *-XX:MaxInlineSize*：能被内联的方法的最大字节码大小，默认值为35B，这种方法不需要频繁的调用。比如：一般pojo类中的getter和setter方法，它们不是那种调用频率特别高的方法，但是它们的字节码大小非常短，这种方法会在执行后被内联。
- *-XX:FreqInlineSize*：调用很频繁的方法能被内联的最大字节码大小，这个大小可以比MaxInlineSize大，默认值为325B（和平台有关，我的机器是64位mac）

###### 内联条件

一个方法如果满足以下条件就很可能被jvm内联。

1、热点代码。 如果一个方法的执行频率很高就表示优化的潜在价值就越大。那代码执行多少次才能确定为热点代码？这是根据编译器的编译模式来决定的。如果是客户端编译模式则次数是1500，服务端编译模式是10000。次数的大小可以通过-XX:CompileThreshold来调整。

2、方法体不能太大。jvm中被内联的方法会编译成机器码放在code cache中。如果方法体太大，则能缓存热点方法就少，反而会影响性能。

3、如果希望方法被内联，**尽量用private、static、final修饰**，这样jvm可以直接内联。如果是public、protected修饰方法jvm则需要进行类型判断，因为这些方法可以被子类继承和覆盖，jvm需要判断内联究竟内联是父类还是其中某个子类的方法。



##### 逃逸分析

在现代JVM中逃逸分析是默认开启的，得通过JVM参数-XX:-DoEscapeAnalysis来关掉它

###### 基于逃逸分析的优化

当判断出对象不发生逃逸时，编译器可以使用逃逸分析的结果作一些代码优化

- 将堆分配转化为栈分配。如果某个对象在子程序中被分配，并且指向该对象的指针永远不会逃逸，该对象就可以在分配在栈上，而不是在堆上。在有垃圾收集的语言中，这种优化可以降低垃圾收集器运行的频率。
- 同步消除。如果发现某个对象只能从一个线程可访问，那么在这个对象上的操作可以不需要同步。
- 分离对象或标量替换。如果某个对象的访问方式不要求该对象是一个连续的内存结构，那么对象的部分（或全部）可以不存储在内存，而是存储在CPU寄存器中。

##### TLAB

##### 逆优化

##### 分层编译级别

总共有5中编译级别：

0：解释代码

1：简单的C1编译代码

2：受限的C1的编译代码

3：完全C1编译代码

4：C2编译代码





https://club.perfma.com/article/304430

https://www.oracle.com/java/technologies/javase/vmoptions-jsp.html

https://docs.oracle.com/javase/8/embedded/develop-apps-platforms/codecache.htm

http://blog.lichengwu.cn/jvm/2014/02/21/jvm-bookmarks/

https://toutiao.io/posts/f5a3mz/preview

[http://ifeve.com/jvm%E4%BC%98%E5%8C%96%E4%B9%8B%E9%80%83%E9%80%B8%E5%88%86%E6%9E%90%E4%B8%8E%E5%88%86%E9%85%8D%E6%B6%88%E9%99%A4/](http://ifeve.com/jvm优化之逃逸分析与分配消除/)

https://zhuanlan.zhihu.com/p/59215831

https://www.cnblogs.com/flydean/p/jvm-escapse-tlab.html