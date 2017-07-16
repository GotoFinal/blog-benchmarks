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
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 20, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Threads(1)
public class ModuleVsAnd
{
    public static Random rand = ThreadLocalRandom.current();

    @Benchmark
    public int rand()
    {
        return rand.nextInt();
    }

    @Benchmark
    public boolean isDivBy2_and()
    {
        return ((rand.nextInt() & 1) == 0);
    }

    @Benchmark
    public boolean isDivBy2_mod()
    {
        return (rand.nextInt() % 2) == 0;
    }

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder().forks(1)
                              .include(ModuleVsAnd.class.getName())
                              .build();

        new Runner(opt).run();
    }
}