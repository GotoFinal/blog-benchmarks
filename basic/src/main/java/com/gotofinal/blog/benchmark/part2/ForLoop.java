package com.gotofinal.blog.benchmark.part2;

import java.util.concurrent.ThreadLocalRandom;
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
public class ForLoop {
    // tokens used to simulate some operations inside loop, on my pc 1400 toknes is about 3000ns, like some simple string replace operation.
    static final int TOKENS = 1400;
    // would be better to generate new numbers for each test, but this seems to not make bigger difference, and I still need that PC for work, as then 60
    // benchmarks will run forever
    int[] random_100 = ThreadLocalRandom.current().ints(100).toArray();
    int[] random_10_000 = ThreadLocalRandom.current().ints(10_000).toArray();
    int[] random_1_000_000 = ThreadLocalRandom.current().ints(1_000_000).toArray();

    @Benchmark
    public void consumeCPU() {
        Blackhole.consumeCPU(TOKENS);
    }

    /*
     * ===================================================================================================
     * ===================================================================================================
     *
     * All this same tests with -XX:TieredStopAtLevel=1 and without additional operations
     *
     * ===================================================================================================
     * ===================================================================================================
     */

    /*
     * for-each loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachLoop_100() {
        long x = 0;
        for (int i : random_100) {
            x += i;
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachLoop_10000() {
        long x = 0;
        for (int i : random_10_000) {
            x += i;
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachLoop_1000000() {
        long x = 0;
        for (int i : random_1_000_000) {
            x += i;
        }
        return x;
    }

    /*
     * indexed for loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedLoop_100() {
        long x = 0;
        for (int i = 0, random_10Length = random_100.length; i < random_10Length; i++) {
            x += random_100[i];
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedLoop_10000() {
        long x = 0;
        for (int i = 0, random_1_000Length = random_10_000.length; i < random_1_000Length; i++) {
            x += random_10_000[i];
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedLoop_1000000() {
        long x = 0;
        for (int i = 0, random_10_000_000Length = random_1_000_000.length; i < random_10_000_000Length; i++) {
            x += random_1_000_000[i];
        }
        return x;
    }

    /*
     * try/catch indexed for loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedTryCatchLoop_100() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_100[i];
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedTryCatchLoop_10000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_10_000[i];
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedTryCatchLoop_1000000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_1_000_000[i];
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    /*
     * indexed for loop with jumping index inside loop body (moving backward on some condition etc) on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedLoop_withJumpingIndex_100() {
        long x = 0;
        for (int i = 0, random_10Length = random_100.length; i < random_10Length; i++) {
            x += random_100[i];
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedLoop_withJumpingIndex_10000() {
        long x = 0;
        for (int i = 0, random_1_000Length = random_10_000.length; i < random_1_000Length; i++) {
            x += random_10_000[i];
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedLoop_withJumpingIndex_1000000() {
        long x = 0;
        for (int i = 0, random_10_000_000Length = random_1_000_000.length; i < random_10_000_000Length; i++) {
            x += random_1_000_000[i];
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    /*
     * try/catch indexed for loop with jumping index inside loop body (moving backward on some condition etc) on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedTryCatchLoop_withJumpingIndex_100() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_100[i];
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedTryCatchLoop_withJumpingIndex_10000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_10_000[i];
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long no_ops__tiered_c1__forEachIndexedTryCatchLoop_withJumpingIndex_1000000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_1_000_000[i];
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }


    /*
     * ===================================================================================================
     * ===================================================================================================
     *
     * All this same tests with -XX:-TieredCompilation and without additional operations
     *
     * ===================================================================================================
     * ===================================================================================================
     */
    
    /*
     * for-each loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachLoop_100() {
        long x = 0;
        for (int i : random_100) {
            x += i;
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachLoop_10000() {
        long x = 0;
        for (int i : random_10_000) {
            x += i;
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachLoop_1000000() {
        long x = 0;
        for (int i : random_1_000_000) {
            x += i;
        }
        return x;
    }

    /*
     * indexed for loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedLoop_100() {
        long x = 0;
        for (int i = 0, random_10Length = random_100.length; i < random_10Length; i++) {
            x += random_100[i];
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedLoop_10000() {
        long x = 0;
        for (int i = 0, random_1_000Length = random_10_000.length; i < random_1_000Length; i++) {
            x += random_10_000[i];
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedLoop_1000000() {
        long x = 0;
        for (int i = 0, random_10_000_000Length = random_1_000_000.length; i < random_10_000_000Length; i++) {
            x += random_1_000_000[i];
        }
        return x;
    }

    /*
     * try/catch indexed for loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedTryCatchLoop_100() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_100[i];
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedTryCatchLoop_10000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_10_000[i];
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedTryCatchLoop_1000000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_1_000_000[i];
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    /*
     * indexed for loop with jumping index inside loop body (moving backward on some condition etc) on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedLoop_withJumpingIndex_100() {
        long x = 0;
        for (int i = 0, random_10Length = random_100.length; i < random_10Length; i++) {
            x += random_100[i];
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedLoop_withJumpingIndex_10000() {
        long x = 0;
        for (int i = 0, random_1_000Length = random_10_000.length; i < random_1_000Length; i++) {
            x += random_10_000[i];
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedLoop_withJumpingIndex_1000000() {
        long x = 0;
        for (int i = 0, random_10_000_000Length = random_1_000_000.length; i < random_10_000_000Length; i++) {
            x += random_1_000_000[i];
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    /*
     * try/catch indexed for loop with jumping index inside loop body (moving backward on some condition etc) on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedTryCatchLoop_withJumpingIndex_100() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_100[i];
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedTryCatchLoop_withJumpingIndex_10000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_10_000[i];
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long no_ops__tiered_c2__forEachIndexedTryCatchLoop_withJumpingIndex_1000000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_1_000_000[i];
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    /*
     * ===================================================================================================
     * ===================================================================================================
     *
     * All this same tests with -XX:TieredStopAtLevel=1 and WITH additional operations
     *
     * ===================================================================================================
     * ===================================================================================================
     */

    /*
     * for-each loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachLoop_100() {
        long x = 0;
        for (int i : random_100) {
            x += i;
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachLoop_10000() {
        long x = 0;
        for (int i : random_10_000) {
            x += i;
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachLoop_1000000() {
        long x = 0;
        for (int i : random_1_000_000) {
            x += i;
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    /*
     * indexed for loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedLoop_100() {
        long x = 0;
        for (int i = 0, random_10Length = random_100.length; i < random_10Length; i++) {
            x += random_100[i];
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedLoop_10000() {
        long x = 0;
        for (int i = 0, random_1_000Length = random_10_000.length; i < random_1_000Length; i++) {
            x += random_10_000[i];
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedLoop_1000000() {
        long x = 0;
        for (int i = 0, random_10_000_000Length = random_1_000_000.length; i < random_10_000_000Length; i++) {
            x += random_1_000_000[i];
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    /*
     * try/catch indexed for loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedTryCatchLoop_100() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_100[i];
                Blackhole.consumeCPU(TOKENS);
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedTryCatchLoop_10000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_10_000[i];
                Blackhole.consumeCPU(TOKENS);
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedTryCatchLoop_1000000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_1_000_000[i];
                Blackhole.consumeCPU(TOKENS);
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    /*
     * indexed for loop with jumping index inside loop body (moving backward on some condition etc) on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedLoop_withJumpingIndex_100() {
        long x = 0;
        for (int i = 0, random_10Length = random_100.length; i < random_10Length; i++) {
            x += random_100[i];
            Blackhole.consumeCPU(TOKENS);
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedLoop_withJumpingIndex_10000() {
        long x = 0;
        for (int i = 0, random_1_000Length = random_10_000.length; i < random_1_000Length; i++) {
            x += random_10_000[i];
            Blackhole.consumeCPU(TOKENS);
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedLoop_withJumpingIndex_1000000() {
        long x = 0;
        for (int i = 0, random_10_000_000Length = random_1_000_000.length; i < random_10_000_000Length; i++) {
            x += random_1_000_000[i];
            Blackhole.consumeCPU(TOKENS);
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    /*
     * try/catch indexed for loop with jumping index inside loop body (moving backward on some condition etc) on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedTryCatchLoop_withJumpingIndex_100() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_100[i];
                Blackhole.consumeCPU(TOKENS);
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedTryCatchLoop_withJumpingIndex_10000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_10_000[i];
                Blackhole.consumeCPU(TOKENS);
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:TieredStopAtLevel=1"})
    public long with_ops__tiered_c1__forEachIndexedTryCatchLoop_withJumpingIndex_1000000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_1_000_000[i];
                Blackhole.consumeCPU(TOKENS);
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    /*
     * ===================================================================================================
     * ===================================================================================================
     *
     * All this same tests with -XX:-TieredCompilation and WITH additional operations
     *
     * ===================================================================================================
     * ===================================================================================================
     */

    
    /*
     * for-each loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachLoop_100() {
        long x = 0;
        for (int i : random_100) {
            x += i;
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachLoop_10000() {
        long x = 0;
        for (int i : random_10_000) {
            x += i;
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachLoop_1000000() {
        long x = 0;
        for (int i : random_1_000_000) {
            x += i;
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    /*
     * indexed for loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedLoop_100() {
        long x = 0;
        for (int i = 0, random_10Length = random_100.length; i < random_10Length; i++) {
            x += random_100[i];
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedLoop_10000() {
        long x = 0;
        for (int i = 0, random_1_000Length = random_10_000.length; i < random_1_000Length; i++) {
            x += random_10_000[i];
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedLoop_1000000() {
        long x = 0;
        for (int i = 0, random_10_000_000Length = random_1_000_000.length; i < random_10_000_000Length; i++) {
            x += random_1_000_000[i];
            Blackhole.consumeCPU(TOKENS);
        }
        return x;
    }

    /*
     * try/catch indexed for loop on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedTryCatchLoop_100() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_100[i];
                Blackhole.consumeCPU(TOKENS);
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedTryCatchLoop_10000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_10_000[i];
                Blackhole.consumeCPU(TOKENS);
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedTryCatchLoop_1000000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_1_000_000[i];
                Blackhole.consumeCPU(TOKENS);
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    /*
     * indexed for loop with jumping index inside loop body (moving backward on some condition etc) on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedLoop_withJumpingIndex_100() {
        long x = 0;
        for (int i = 0, random_10Length = random_100.length; i < random_10Length; i++) {
            x += random_100[i];
            Blackhole.consumeCPU(TOKENS);
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedLoop_withJumpingIndex_10000() {
        long x = 0;
        for (int i = 0, random_1_000Length = random_10_000.length; i < random_1_000Length; i++) {
            x += random_10_000[i];
            Blackhole.consumeCPU(TOKENS);
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedLoop_withJumpingIndex_1000000() {
        long x = 0;
        for (int i = 0, random_10_000_000Length = random_1_000_000.length; i < random_10_000_000Length; i++) {
            x += random_1_000_000[i];
            Blackhole.consumeCPU(TOKENS);
            int r = ThreadLocalRandom.current().nextInt(6);
            if (r == 0) {
                i = Math.max(0, i - 5);
            } else if (r == 5) {
                i += 5;
            }
        }
        return x;
    }

    /*
     * try/catch indexed for loop with jumping index inside loop body (moving backward on some condition etc) on array of 10/1000/10_000_000 elements.
     */

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedTryCatchLoop_withJumpingIndex_100() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_100[i];
                Blackhole.consumeCPU(TOKENS);
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedTryCatchLoop_withJumpingIndex_10000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_10_000[i];
                Blackhole.consumeCPU(TOKENS);
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = {"-XX:-TieredCompilation"})
    public long with_ops__tiered_c2__forEachIndexedTryCatchLoop_withJumpingIndex_1000000() {
        long x = 0;
        try {
            for (int i = 0; ; i++) {
                x += random_1_000_000[i];
                Blackhole.consumeCPU(TOKENS);
                int r = ThreadLocalRandom.current().nextInt(6);
                if (r == 0) {
                    i = Math.max(0, i - 5);
                } else if (r == 5) {
                    i += 5;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return x;
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                          .include(ForLoop.class.getName())
                          .forks(1)
                          .build();

        new Runner(opt).run();
    }

}
