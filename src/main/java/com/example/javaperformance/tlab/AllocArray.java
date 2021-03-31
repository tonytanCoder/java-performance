package com.example.javaperformance.tlab;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3)
@Measurement(iterations = 3)
@Fork(3)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class AllocArray {
    @Benchmark
    public Object test() {
        final int size = 50_000_000;
        Object[] objects = new Object[size];
        for (int c = 0; c < size; c++) {
            objects[c] = new Object();
        }
        return objects;
    }

    public static void main(String[] args) throws RunnerException {
    Options opt =
        new OptionsBuilder()
            .include(AllocArray.class.getSimpleName())
            .jvmArgs("-XX:+UseTLAB","-XX:+DisplayVMOutputToStdout") // .addProfiler(WinPerfAsmProfiler.class).forks(1)-XX:-UseTLAB
            .build();

        new Runner(opt).run();
    }
}
