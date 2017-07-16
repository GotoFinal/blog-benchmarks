package com.gotofinal.blog.benchmark.part1;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 20, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class RandomTest
{
    Random              random            = new Random();
    Random              threadLocalRandom = ThreadLocalRandom.current();
    ThreadLocal<Random> cached            = ThreadLocal.withInitial(Random::new);

    @Benchmark
    public long useCachedThreadLocalRandom()
    {
        return threadLocalRandom.nextLong();
    }

    @Benchmark
    public long useThreadLocalRandom()
    {
        return ThreadLocalRandom.current().nextLong();
    }

    @Benchmark
    public long useNewRandom()
    {
        return new Random().nextLong();
    }

    @Benchmark
    public long useSingleRandom()
    {
        return random.nextLong();
    }

    @Benchmark
    @Threads(8)
    public long useSingleRandomMultiThread()
    {
        return random.nextLong();
    }

    @Benchmark
    @Threads(8)
    public long useCachedRandomMultiThread()
    {
        return cached.get().nextLong();
    }

    @Benchmark
    @Threads(8)
    public long useNewRandomMultiThread()
    {
        return new Random().nextLong();
    }

    @Benchmark
    @Threads(8)
    public long useThreadLocalRandomMultiThread()
    {
        return ThreadLocalRandom.current().nextLong();
    }

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder()
                              .include(RandomTest.class.getName())
                              .forks(1)
                              .build();

        new Runner(opt).run();
    }

}