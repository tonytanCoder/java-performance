package com.example.javaperformance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.JavaFlightRecorderProfiler;
import org.openjdk.jmh.profile.WinPerfAsmProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 标量替换 通过-XX:+EliminateAllocations可以开启标量替换， -XX:+PrintEliminateAllocations查看标量替换情况
 * 标量是指不可分割的量，如java中基本数据类型和reference类型，相对的一个数据可以继续分解，称为聚合量；
 *
 * 如果把一个对象拆散，将其成员变量恢复到基本类型来访问就叫做标量替换；
 *
 * 如果逃逸分析发现一个对象不会被外部访问，并且该对象可以被拆散，那么经过优化之后，并不直接生成该对象，而是在栈上创建若干个成员变量；
 */
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3,jvmArgs = "-XX:+UnlockCommercialFeatures")
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class ScalarReplacement {

    int x;

    @Benchmark
    public int single() {
        MyObject o = new MyObject(x);
        return o.x;
    }

    static class MyObject {
        final int x;
        public MyObject(int x) {
            this.x = x;
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ScalarReplacement.class.getSimpleName())
                .forks(1).jvmArgs("-XX:+UnlockCommercialFeatures ").addProfiler(JavaFlightRecorderProfiler.class ) //.addProfiler(WinPerfAsmProfiler.class)
                .build();

        new Runner(opt).run();
    }
}
