# java-performance
java performance


/**
 * -XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation
 * -XX:LogFile=E://log/jit/jit.log –XX:+PrintCodeCache  -XX:PrintAssembly -XX:ReservedCodeCacheSize=  -XX:+TieredCompilation
 * https://docs.oracle.com/javase/8/embedded/develop-apps-platforms/codecache.htm
 */



mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=org.openjdk.jmh -DarchetypeArtifactId=jmh-java-benchmark-archetype -DgroupId=org.sample -DartifactId=test19 -Dversion=1.19


-XX:+EliminateAllocations 开启标量替换参数

java -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+PrintFlagsFinal -version 查看jvm参数


逃逸分析本身并不是一种优化手段，只是为后续JVM优化提供数据。
通过逃逸分析确定该对象不会被外部访问，并且对象可以被进一步分解时，JVM不会创建该对象，而是将该对象成员变量分解若干个被这个方法使用的成员变量所代替，这些代替的成员变量在栈帧或寄存器上分配空间，这样就不会因为没有一大块连续空间导致对象内存不够分配。

标量 VS 聚合量
标量替换 ？ 那什么是标量 ？

标量： 不可被进一步分解的量，而JAVA的基本数据类型就是标量（比如int，long等基本数据类型以及reference类型等） 。

聚合量： 标量的对立就是可以被进一步分解的量，称之为聚合量。 在JAVA中对象就是可以被进一步分解的聚合量。








栈上分配：https://stackoverflow.com/questions/43002528/when-can-hotspot-allocate-objects-on-the-stack


















