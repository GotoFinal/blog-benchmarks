package com.gotofinal.blog.benchmark.part2;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 4, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 4, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 20, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(jvmArgsAppend = {"-XX:-TieredCompilation"})
public class StreamBenchmark {
    int[] random_1_000_000 = new Random(123).ints(1_000_000).toArray();
    int[] random_100_000_000 = new Random(123).ints(100_000_000).toArray();

    @Benchmark
    public long intSumStream_1_000_000() {
        return IntStream.of(random_1_000_000).sum();
    }

    @Benchmark
    public long intSumStreamParallel_1_000_000() {
        return IntStream.of(random_1_000_000).parallel().sum();
    }

    @Benchmark
    public long intSumOldJava_1_000_000() {
        long sum = 0;
        for (int i : random_1_000_000) {
            sum += i;
        }
        return sum;
    }

    @Benchmark
    public long intSumStream_100_000_000() {
        return IntStream.of(random_100_000_000).sum();
    }

    @Benchmark
    public long intSumStreamParallel_100_000_000() {
        return IntStream.of(random_100_000_000).parallel().sum();
    }

    @Benchmark
    public long intSumOldJava_100_000_000() {
        long sum = 0;
        for (int i : random_100_000_000) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                          .include(StreamBenchmark.class.getName())
                          .forks(1)
                          .build();

        new Runner(opt).run();
    }
}
