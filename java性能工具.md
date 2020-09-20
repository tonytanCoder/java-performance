java监控工具

### JDK自带工具

#### jcmd 

##### 用途

发送诊断命令至正在活动的JVM。

##### 语法

Usage: jcmd <pid | main class> <command ...|PerfCounter.print|-f file>   or: jcmd -l   or: jcmd -h

- `h`：帮助。
- `l`：显示当前机器上的进程，包括进程id、主类全路径名、main方法参数，等同于`jps -ml`。
- `f`：从文件读取并执行命令。
- `PerfCounter.print`：打印性能计数器信息。
- `help`：显示当前JVM进程可用的命令。

##### 常用命令示例

获取帮助示例

C:\Users\tanzl>jcmd 20704 help
20704:
The following commands are available:
Compiler.CodeHeap_Analytics
Compiler.codecache
Compiler.codelist
Compiler.directives_add
Compiler.directives_clear
Compiler.directives_print
Compiler.directives_remove
Compiler.queue
GC.class_histogram
GC.class_stats
GC.finalizer_info
GC.heap_dump
GC.heap_info
GC.run
GC.run_finalization
JFR.check
JFR.configure
JFR.dump
JFR.start
JFR.stop
JVMTI.agent_load
JVMTI.data_dump
ManagementAgent.start
ManagementAgent.start_local
ManagementAgent.status
ManagementAgent.stop
Thread.print
VM.class_hierarchy
VM.classloader_stats
VM.classloaders
VM.command_line
VM.dynlibs
VM.flags
VM.info
VM.log
VM.metaspace
VM.native_memory
VM.print_touched_methods
VM.set_flag
VM.start_java_debugging
VM.stringtable
VM.symboltable
VM.system_properties
VM.systemdictionary
VM.uptime
VM.version
help

For more information about a specific command use 'help <command>'.

`jcmd pid Thread.print`打印线程信息

C:\Users\tanzl>jcmd 20704  Thread.print
20704:
2020-09-19 22:39:01
Full thread dump OpenJDK 64-Bit Server VM (11.0.5+10-b520.30 mixed mode, sharing):

Threads class SMR info:
_java_thread_list=0x000001be682effa0, length=28, elements={
0x000001be65f05000, 0x000001be65f06800, 0x000001be65f24000, 0x000001be65f25000,
0x000001be65f2a000, 0x000001be65f35000, 0x000001be65eea000, 0x000001be669e6000,
0x000001be669e7000, 0x000001be66f2c000, 0x000001be67685000, 0x000001be68260800,
0x000001be681b9800, 0x000001be6826b000, 0x000001be682b3000, 0x000001be68199000,
0x000001be68163000, 0x000001be68319000, 0x000001be68318000, 0x000001be68315800,
0x000001be68316800, 0x000001be68313800, 0x000001be68315000, 0x000001be68317800,
0x000001be6831a000, 0x000001be68314000, 0x000001be68343000, 0x000001be68344000
}

"Reference Handler" #2 daemon prio=10 os_prio=2 cpu=0.00ms elapsed=504.81s tid=0x000001be65f05000 nid=0x4d04 waiting on condition  [0x00000049f3eff000]
   java.lang.Thread.State: RUNNABLE
        at java.lang.ref.Reference.waitForReferencePendingList(java.base@11.0.5/Native Method)
        at java.lang.ref.Reference.processPendingReferences(java.base@11.0.5/Reference.java:241)
        at java.lang.ref.Reference$ReferenceHandler.run(java.base@11.0.5/Reference.java:213)

"Finalizer" #3 daemon prio=8 os_prio=1 cpu=0.00ms elapsed=504.81s tid=0x000001be65f06800 nid=0x3e78 in Object.wait()  [0x00000049f3ffe000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(java.base@11.0.5/Native Method)
        - waiting on <0x0000000081a86d88> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(java.base@11.0.5/ReferenceQueue.java:155)
        - waiting to re-lock in wait() <0x0000000081a86d88> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(java.base@11.0.5/ReferenceQueue.java:176)
        at java.lang.ref.Finalizer$FinalizerThread.run(java.base@11.0.5/Finalizer.java:170)

"Signal Dispatcher" #4 daemon prio=9 os_prio=2 cpu=0.00ms elapsed=504.80s tid=0x000001be65f24000 nid=0x680 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Attach Listener" #5 daemon prio=5 os_prio=2 cpu=0.00ms elapsed=504.80s tid=0x000001be65f25000 nid=0x39ac waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C1 CompilerThread0" #6 daemon prio=9 os_prio=2 cpu=781.25ms elapsed=504.80s tid=0x000001be65f2a000 nid=0x4b3c waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

`jcmd pid GC.class_histgram`:类统计信息，包括类名、实例个数、内存大小等

C:\Users\tanzl>jcmd 20704  GC.class_histogram
20704:

 num     #instances         #bytes  class name (module)

   1:         42058        3872864  [B (java.base@11.0.5)
   2:         40271         966504  java.lang.String (java.base@11.0.5)
   3:          7137         844520  java.lang.Class (java.base@11.0.5)
   4:          7935         698280  java.lang.reflect.Method (java.base@11.0.5)
   5:         17898         572736  java.util.concurrent.ConcurrentHashMap$Node (java.base@11.0.5)
   6:          9339         478352  [Ljava.lang.Object; (java.base@11.0.5)
   7:         10450         334400  java.util.HashMap$Node (java.base@11.0.5)
   8:          3182         329616  [I (java.base@11.0.5)
   9:          3235         298176  [Ljava.util.HashMap$Node; (java.base@11.0.5)
  10:          6743         269720  java.util.LinkedHashMap$Entry (java.base@11.0.5)
  11:         10238         238720  [Ljava.lang.Class; (java.base@11.0.5)
  12:           229         196240  [Ljava.util.concurrent.ConcurrentHashMap$Node; (java.base@11.0.5)
  13:           397         184768  [C (java.base@11.0.5)
  14:          2684         150304  java.util.LinkedHashMap (java.base@11.0.5)
  15:          8749         139984  java.lang.Object (java.base@11.0.5)
  16:          3081          98592  java.lang.invoke.LambdaForm$Name (java.base@11.0.5)
  17:          2024          97152  java.lang.invoke.MemberName (java.base@11.0.5)
  18:          2180          87200  java.lang.ref.SoftReference (java.base@11.0.5)
  19:          1621          77808  java.util.HashMap (java.base@11.0.5)
  20:          1167          74688  java.lang.Class$ReflectionData (java.base@11.0.5)
  21:          1032          74304  java.lang.reflect.Field (java.base@11.0.5)
  22:          1478          70944  sun.util.locale.LocaleObjectCache$CacheEntry (java.base@11.0.5)
  23:           865          69200  java.lang.reflect.Constructor (java.base@11.0.5)
  24:          1513          60520  java.lang.invoke.MethodType (java.base@11.0.5)
  25:           983          57000  [Ljava.lang.reflect.Method; (java.base@11.0.5)
  26:          1666          53312  java.lang.invoke.MethodType$ConcurrentWeakInternSet$WeakEntry (java.base@11.0.5)
  27:          1167          48696  [Ljava.lang.String; (java.base@11.0.5)
  28:          1942          46608  java.util.ArrayList (java.base@11.0.5)
  29:          1604          38496  java.lang.invoke.ResolvedMethodName (java.base@11.0.5)
  30:           754          36192  org.springframework.core.ResolvableType
  31:          1364          32736  sun.reflect.generics.tree.SimpleClassTypeSignature (java.base@11.0.5)
  32:           470          31960  [Ljava.lang.ref.SoftReference; (java.base@11.0.5)
  33:           452          28928  java.util.concurrent.ConcurrentHashMap (java.base@11.0.5)
  34:           414          27952  [Ljava.lang.invoke.LambdaForm$Name; (java.base@11.0.5)
  35:           824          26368  java.lang.invoke.DirectMethodHandle (java.base@11.0.5)
  36:          1364          25528  [Lsun.reflect.generics.tree.TypeArgument; (java.base@11.0.5)
  37:           738          23616  java.util.Locale (java.base@11.0.5)
  38:           737          23584  sun.util.locale.BaseLocale (java.base@11.0.5)
  39:           737          23584  sun.util.locale.BaseLocale$Key (java.base@11.0.5)
  40:           128          23552  org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader$ConfigurationClassBeanDefinition

- `jcmd pid GC.heap_dump dump.bin`：将堆信息导入到项目根目录下的dump.bin文件中，以供其他分析工具进行分析。

```
C:\Users\tanzl>jcmd 7484 GC.heap_dump dump.bin
7484:
Heap dump file created
```

#### jhat

##### 用途

**jhat** **(Java Heap Analyse Tool)** 是用来分析java堆的命令，可以将堆中的对象以**html**的形式展示，包括对象的数量、大小等信息，并支持对象查询语言 （OQL）。

##### 语法

Usage:  jhat [-stack <bool>] [-refs <bool>] [-port <port>] [-baseline <file>] [-debug <int>] [-version] [-h|-help] <file>

        -J<flag>          Pass <flag> directly to the runtime system. For
                          example, -J-mx512m to use a maximum heap size of 512MB
        -stack false:     Turn off tracking object allocation call stack.
        -refs false:      Turn off tracking of references to objects
        -port <port>:     Set the port for the HTTP server.  Defaults to 7000
        -exclude <file>:  Specify a file that lists data members that should
                          be excluded from the reachableFrom query.
        -baseline <file>: Specify a baseline object dump.  Objects in
                          both heap dumps with the same ID and same class will
                          be marked as not being "new".
        -debug <int>:     Set debug level.
                            0:  No debug output
                            1:  Debug hprof file parsing
                            2:  Debug hprof file parsing, no server
        -version          Report version number
        -h|-help          Print this help and exit
        <file>            The file to read

For a dump file that contains multiple heap dumps,
you may specify which dump in the file
by appending "#<number>" to the file name, i.e. "foo.hprof#3".

All boolean options default to "true"

##### 常用命令示例

导出堆文件

jmap -dump:format=b,file=D:/heap.hprof  20704

dump出来的内容进行分析，他会在服务器上开启一个端口作为web访问的入口，然后将解析出来的数据已web的形式提供访问

jhat D:/heap.hprof

#### jmap

##### 用途

JVM Memory Map，用于生成heap dump文件，可以使用-XX:+HeapDumpOnOutOfMemoryError参数来让虚拟机出现OOM的时候·自动生成dump文件。

jmap不仅能生成dump文件，还可以查询finalize执行队列、Java堆和永久代的详细信息，如当前使用率、当前使用的是哪种收集器等。

##### 语法

Usage:

  jmap [option] <pid>

​    (to connect to running process)

  jmap [option] <executable <core>

​    (to connect to a core file)

  jmap [option] [server_id@]<remote server IP or hostname>

​    (to connect to remote debug server)

where <option> is one of:

  <none>        to print same info as Solaris pmap

  -heap        to print java heap summary

  -histo[:live]    to print histogram of java object heap; if the "live"

​             suboption is specified, only count live objects

  -clstats       to print class loader statistics

  -finalizerinfo    to print information on objects awaiting finalization

  -dump:<dump-options> to dump java heap in hprof binary format

​             dump-options:

​              live     dump only live objects; if not specified,

​                    all objects in the heap are dumped.

​              format=b   binary format

​              file=<file> dump heap to <file>

​             Example: jmap -dump:live,format=b,file=heap.bin <pid>

  -F          force. Use with -dump:<dump-options> <pid> or -histo

​             to force a heap dump or histogram when <pid> does not

​             respond. The "live" suboption is not supported

​             in this mode.

  -h | -help      to print this help message

  -J<flag>       to pass <flag> directly to the runtime system

常用命令示例

- jmap pid #打印内存使用的摘要信息
- jmap –heap pid #java heap信息
- jmap -histo:live pid #统计对象count ，live表示在使用
- jmap -histo pid >mem.txt #打印比较简单的各个有多少个对象占了多少内存的信息，一般重定向的文件
- jmap -dump:format=b,file=mem.dat pid #将内存使用的详细情况输出到mem.dat 文件
  通过`jhat -port 7000 mem.dat`可以将mem.dat的内容以web的方式暴露到网络，访问`http://ip-server:7000`查看。

**注意事项**

1. jmap -dump

这个命令执行，JVM会将整个heap的信息dump写入到一个文件，heap如果比较大的话，就会导致这个过程比较耗时，并且执行的过程中为了保证dump的信息是可靠的，所以会暂停应用。

2. jmap -clstats （-permstat）

这个命令执行，JVM会去统计perm区的状况，这整个过程也会比较的耗时，并且同样也会暂停应用。

3. jmap -histo:live

这个命令执行，JVM会先触发gc，然后再统计信息。

上面的这三个操作都将对应用的执行产生影响，所以建议如果不是很有必要的话，不要去执行。

同时需要注意，jstack -l pid 由于会打印锁信息，因此也会触发gc，建议使用的时候更多时候使用 jstack pid。

#### jinfo

##### 用途

jinfo 是 JDK 自带的命令，可以用来查看正在运行的 java 应用程序的扩展参数，包括Java System属性和JVM命令行参数；也可以动态的修改正在运行的 JVM 一些参数。当系统崩溃时，jinfo可以从core文件里面知道崩溃的Java应用程序的配置信息

#### jstack

jstack主要用来查看某个Java进程内的线程堆栈信息。

#### jstat

提供GC和类装载活动的信息。可适用于脚本。



### 性能分析工具

#### 采样分析器

#### 探查分析器

#### 本地分析器

#### JMC

##### JFR（java飞行记录）

###### 开启飞行记录

要启用此功能, 必须使用通过 -XX:+UnlockCommercialFeatures -XX:+FlightRecorder 启动的 Java 7u4 或更高版本的 JVM。
