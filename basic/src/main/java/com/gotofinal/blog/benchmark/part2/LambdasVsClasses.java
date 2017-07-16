package com.gotofinal.blog.benchmark.part2;

import java.util.concurrent.TimeUnit;
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
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 20, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(jvmArgsAppend = {"-XX:-TieredCompilation"})
public class LambdasVsClasses {

    Runnable CACHED_ANONYMOUS = new Runnable() {
        @Override
        public void run() {
            Blackhole.consumeCPU(1);
        }
    };
    Runnable CACHED_LAMBDA = () -> Blackhole.consumeCPU(1);

    @Benchmark
    public void normalClass(Blackhole blackhole) {
        someTaskConsumer(blackhole, new NormalClass());
    }

    @Benchmark
    public void normalClassCached(Blackhole blackhole) {
        someTaskConsumer(blackhole, NormalClass.CACHED);
    }

    @Benchmark
    public void nestedClass(Blackhole blackhole) {
        someTaskConsumer(blackhole, new NestedClass());
    }

    @Benchmark
    public void nestedClassCached(Blackhole blackhole) {
        someTaskConsumer(blackhole, NESTED_CACHED);
    }

    @Benchmark
    public void anonymousWithoutData(Blackhole blackhole) {
        someTaskConsumer(blackhole, new Runnable() {
            @Override
            public void run() {
                Blackhole.consumeCPU(1);
            }
        });
    }

    @Benchmark
    public void anonymousWithData(Blackhole blackhole) {
        someTaskConsumer(blackhole, new Runnable() {
            @Override
            public void run() {
                Blackhole.consumeCPU(1);
                blackhole.consume(blackhole);
            }
        });
    }

    @Benchmark
    public void anonymousCached(Blackhole blackhole) {
        someTaskConsumer(blackhole, CACHED_ANONYMOUS);
    }

    @Benchmark
    public void lambdaWithoutData(Blackhole blackhole) {
        someTaskConsumer(blackhole, () -> Blackhole.consumeCPU(1));
    }

    @Benchmark
    public void lambdaWithData(Blackhole blackhole) {
        someTaskConsumer(blackhole, () -> {
            Blackhole.consumeCPU(1);
            blackhole.consume(blackhole);
        });
    }

    @Benchmark
    public void lambdaCached(Blackhole blackhole) {
        someTaskConsumer(blackhole, CACHED_LAMBDA);
    }

    public void someTaskConsumer(Blackhole blackhole, Runnable runnable) {
        Blackhole.consumeCPU(1);
        runnable.run();
        // to prevent additional optimizations
        blackhole.consume(runnable);
        Blackhole.consumeCPU(1);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                          .include(LambdasVsClasses.class.getName())
                          .forks(1)
                          .build();

        new Runner(opt).run();
    }

    static class NormalClass implements Runnable {
        static NormalClass CACHED = new NormalClass();

        @Override
        public void run() {
            Blackhole.consumeCPU(1);
        }
    }

    NestedClass NESTED_CACHED = new NestedClass();

    class NestedClass implements Runnable {

        @Override
        public void run() {
            Blackhole.consumeCPU(1);
        }
    }
}
