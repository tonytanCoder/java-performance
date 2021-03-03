package benchmark.cpucache;

import com.example.javaperformance.cpucache.ArrayTraversing;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class ArrayTraversingBenchmark {

    int array[][];

    @Setup
    public void setup(BenchmarkParams params) {
        array=new int[100][100];
        for (int i=0;i<100;i++){
            for (int j=0;j<100;j++){
                array[i][j]=i+j;
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureThroughput() throws InterruptedException {
        ArrayTraversing.traversing1(array);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureThroughput2() throws InterruptedException {
        ArrayTraversing.traversing2(array);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ArrayTraversingBenchmark.class.getSimpleName())
                .threads(1)
                .forks(5)
                .build();

        new Runner(opt).run();
    }

}
