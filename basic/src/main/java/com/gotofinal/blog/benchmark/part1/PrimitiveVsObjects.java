package com.gotofinal.blog.benchmark.part1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 20, time = 5, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 20, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Threads(1)
public class PrimitiveVsObjects {
    public static final int TEST_SIZE = 1_000_000;

    @Benchmark
    public BigDecimal objectPerformance() {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        Collection<Long> longs = new ArrayList<>(TEST_SIZE);
        for (int i = 0; i < TEST_SIZE; i++) {
            longs.add(current.nextLong());
        }
        BigDecimal sum = new BigDecimal(0);
        for (Long aLong : longs) {
            sum = sum.add(new BigDecimal(aLong));
        }
        return sum;
    }

    @Benchmark
    public BigDecimal primitivesPerformance() {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        long[] longs = new long[TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; i++) {
            longs[i] = current.nextInt();
        }
        BigDecimal sum = new BigDecimal(0);
        for (long aLong : longs) {
            sum = sum.add(new BigDecimal(aLong));
        }
        return sum;
    }

    @Benchmark
    public long objectPerformanceLong() {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        Collection<Long> longs = new ArrayList<>(TEST_SIZE);
        for (int i = 0; i < TEST_SIZE; i++) {
            longs.add(current.nextLong());
        }
        long sum = 0;
        for (long aLong : longs) {
            sum += aLong;
        }
        return sum;
    }

    @Benchmark
    public long primitivesPerformanceLong() {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        long[] longs = new long[TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; i++) {
            longs[i] = current.nextInt();
        }
        long sum = 0;
        for (long aLong : longs) {
            sum += aLong;
        }
        return sum;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().forks(1)
                .include(PrimitiveVsObjects.class.getName())
                .build();

        new Runner(opt).run();
    }
}
