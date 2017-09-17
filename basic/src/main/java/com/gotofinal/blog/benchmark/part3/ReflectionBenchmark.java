package com.gotofinal.blog.benchmark.part3;

import com.gotofinal.blog.benchmark.part3.test.Something;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

// Benchmark                                                                           Mode  Cnt  Score   Error  Units
// ReflectionBenchmark.normalDirectAccess                                              avgt   10  2,867 ± 0,142  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_access                            avgt   10  5,397 ± 0,125  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_final_access                      avgt   10  5,538 ± 0,317  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_final_handle_access               avgt   10  6,496 ± 0,412  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_final_handle_bound_access         avgt   10  5,522 ± 0,150  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_handle_access                     avgt   10  5,831 ± 0,204  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_handle_bound_access               avgt   10  5,498 ± 0,175  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_static_access                     avgt   10  4,758 ± 0,122  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_static_final_access               avgt   10  4,739 ± 0,120  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_static_final_handle_access        avgt   10  2,827 ± 0,058  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_static_final_handle_bound_access  avgt   10  2,859 ± 0,084  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_static_handle_access              avgt   10  5,408 ± 0,202  ns/op
// ReflectionBenchmark.somethingPrivateFieldAccessor_static_handle_bound_access        avgt   10  5,327 ± 0,265  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_access                             avgt   10  6,106 ± 1,180  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_final_access                       avgt   10  5,378 ± 0,145  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_final_handle_access                avgt   10  5,939 ± 0,160  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_final_handle_bound_access          avgt   10  6,125 ± 0,961  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_handle_access                      avgt   10  6,286 ± 0,659  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_handle_bound_access                avgt   10  5,441 ± 0,090  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_nonAccessible_access               avgt   10  6,622 ± 0,422  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_nonAccessible_final_access         avgt   10  6,414 ± 0,190  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_nonAccessible_static_access        avgt   10  5,790 ± 0,177  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_nonAccessible_static_final_access  avgt   10  5,810 ± 0,138  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_static_access                      avgt   10  4,840 ± 0,261  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_static_final_access                avgt   10  4,662 ± 0,146  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_static_final_handle_access         avgt   10  2,802 ± 0,082  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_static_final_handle_bound_access   avgt   10  2,765 ± 0,047  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_static_handle_access               avgt   10  5,424 ± 0,165  ns/op
// ReflectionBenchmark.somethingPublicFieldAccessor_static_handle_bound_access         avgt   10  5,091 ± 0,151  ns/op
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 4, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 20, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgsAppend = {"-XX:-TieredCompilation"})
public class ReflectionBenchmark {
    public static Something something = new Something();
    private Field somethingPublicFieldAccessor = Something.class.getDeclaredFields()[0];
    private Field somethingPublicFieldAccessor_nonAccessible = Something.class.getDeclaredFields()[0];
    private Field somethingPrivateFieldAccessor = Something.class.getDeclaredFields()[1];
    private final Field somethingPublicFieldAccessor_final = Something.class.getDeclaredFields()[0];
    private final Field somethingPublicFieldAccessor_nonAccessible_final = Something.class.getDeclaredFields()[0];
    private final Field somethingPrivateFieldAccessor_final = Something.class.getDeclaredFields()[1];
    private static Field somethingPublicFieldAccessor_static = Something.class.getDeclaredFields()[0];
    private static Field somethingPublicFieldAccessor_nonAccessible_static = Something.class.getDeclaredFields()[0];
    private static Field somethingPrivateFieldAccessor_static = Something.class.getDeclaredFields()[1];
    private static final Field somethingPublicFieldAccessor_static_final = Something.class.getDeclaredFields()[0];
    private static final Field somethingPublicFieldAccessor_nonAccessible_static_final = Something.class.getDeclaredFields()[0];
    private static final Field somethingPrivateFieldAccessor_static_final = Something.class.getDeclaredFields()[1];
    private MethodHandle somethingPublicFieldAccessor_handle;
    private MethodHandle somethingPrivateFieldAccessor_handle;
    private final MethodHandle somethingPublicFieldAccessor_final_handle;
    private final MethodHandle somethingPrivateFieldAccessor_final_handle;
    private static MethodHandle somethingPublicFieldAccessor_static_handle;
    private static MethodHandle somethingPrivateFieldAccessor_static_handle;
    private static final MethodHandle somethingPublicFieldAccessor_static_final_handle;
    private static final MethodHandle somethingPrivateFieldAccessor_static_final_handle;
    private MethodHandle somethingPublicFieldAccessor_handle_bound;
    private MethodHandle somethingPrivateFieldAccessor_handle_bound;
    private final MethodHandle somethingPublicFieldAccessor_final_handle_bound;
    private final MethodHandle somethingPrivateFieldAccessor_final_handle_bound;
    private static MethodHandle somethingPublicFieldAccessor_static_handle_bound;
    private static MethodHandle somethingPrivateFieldAccessor_static_handle_bound;
    private static final MethodHandle somethingPublicFieldAccessor_static_final_handle_bound;
    private static final MethodHandle somethingPrivateFieldAccessor_static_final_handle_bound;


    {
        somethingPublicFieldAccessor.setAccessible(true);
        somethingPrivateFieldAccessor.setAccessible(true);
        somethingPublicFieldAccessor_final.setAccessible(true);
        somethingPrivateFieldAccessor_final.setAccessible(true);
        try {
            somethingPublicFieldAccessor_handle = MethodHandles.lookup().unreflectGetter(somethingPublicFieldAccessor_nonAccessible);
            somethingPrivateFieldAccessor_handle = MethodHandles.lookup().unreflectGetter(somethingPrivateFieldAccessor);
            somethingPublicFieldAccessor_final_handle = MethodHandles.lookup().unreflectGetter(somethingPublicFieldAccessor_nonAccessible_final);
            somethingPrivateFieldAccessor_final_handle = MethodHandles.lookup().unreflectGetter(somethingPrivateFieldAccessor_final);
            somethingPublicFieldAccessor_handle_bound = somethingPublicFieldAccessor_handle.bindTo(something);
            somethingPrivateFieldAccessor_handle_bound = somethingPrivateFieldAccessor_handle.bindTo(something);
            somethingPublicFieldAccessor_final_handle_bound = somethingPublicFieldAccessor_final_handle.bindTo(something);
            somethingPrivateFieldAccessor_final_handle_bound = somethingPrivateFieldAccessor_final_handle.bindTo(something);
        } catch (Throwable e) {
            throw new InternalError(e);
        }
    }

    static {
        somethingPublicFieldAccessor_static.setAccessible(true);
        somethingPrivateFieldAccessor_static.setAccessible(true);
        somethingPublicFieldAccessor_static_final.setAccessible(true);
        somethingPrivateFieldAccessor_static_final.setAccessible(true);
        try {
            somethingPublicFieldAccessor_static_handle = MethodHandles.lookup().unreflectGetter(somethingPublicFieldAccessor_nonAccessible_static);
            somethingPrivateFieldAccessor_static_handle = MethodHandles.lookup().unreflectGetter(somethingPrivateFieldAccessor_static);
            somethingPublicFieldAccessor_static_final_handle = MethodHandles.lookup().unreflectGetter(somethingPublicFieldAccessor_nonAccessible_static_final);
            somethingPrivateFieldAccessor_static_final_handle = MethodHandles.lookup().unreflectGetter(somethingPrivateFieldAccessor_static_final);
            somethingPublicFieldAccessor_static_handle_bound = somethingPublicFieldAccessor_static_handle.bindTo(something);
            somethingPrivateFieldAccessor_static_handle_bound = somethingPrivateFieldAccessor_static_handle.bindTo(something);
            somethingPublicFieldAccessor_static_final_handle_bound = somethingPublicFieldAccessor_static_final_handle.bindTo(something);
            somethingPrivateFieldAccessor_static_final_handle_bound = somethingPrivateFieldAccessor_static_final_handle.bindTo(something);
        } catch (Throwable e) {
            throw new InternalError(e);
        }
    }

    @Benchmark
    public Object normalDirectAccess() throws Throwable {
        return something.publicObjectField;
    }

    @Benchmark
    public String somethingPublicFieldAccessor_access() throws Throwable {
        return (String) somethingPublicFieldAccessor.get(something);
    }
    @Benchmark
    public String somethingPublicFieldAccessor_nonAccessible_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_nonAccessible.get(something);
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor.get(something);
    }

    @Benchmark
    public String somethingPublicFieldAccessor_final_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_final.get(something);
    }
    @Benchmark
    public String somethingPublicFieldAccessor_nonAccessible_final_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_nonAccessible_final.get(something);
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_final_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor_final.get(something);
    }

    @Benchmark
    public String somethingPublicFieldAccessor_static_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_static.get(something);
    }
    @Benchmark
    public String somethingPublicFieldAccessor_nonAccessible_static_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_nonAccessible_static.get(something);
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_static_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor_static.get(something);
    }

    @Benchmark
    public String somethingPublicFieldAccessor_static_final_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_static_final.get(something);
    }

    @Benchmark
    public String somethingPublicFieldAccessor_nonAccessible_static_final_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_nonAccessible_static_final.get(something);
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_static_final_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor_static_final.get(something);
    }

    @Benchmark
    public String somethingPublicFieldAccessor_handle_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_handle.invokeExact(something);
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_handle_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor_handle.invokeExact(something);
    }

    @Benchmark
    public String somethingPublicFieldAccessor_final_handle_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_final_handle.invokeExact(something);
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_final_handle_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor_final_handle.invokeExact(something);
    }

    @Benchmark
    public String somethingPublicFieldAccessor_static_handle_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_static_handle.invokeExact(something);
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_static_handle_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor_static_handle.invokeExact(something);
    }

    @Benchmark
    public String somethingPublicFieldAccessor_static_final_handle_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_static_final_handle.invokeExact(something);
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_static_final_handle_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor_static_final_handle.invokeExact(something);
    }

    @Benchmark
    public String somethingPublicFieldAccessor_handle_bound_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_handle_bound.invokeExact();
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_handle_bound_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor_handle_bound.invokeExact();
    }

    @Benchmark
    public String somethingPublicFieldAccessor_final_handle_bound_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_final_handle_bound.invokeExact();
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_final_handle_bound_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor_final_handle_bound.invokeExact();
    }

    @Benchmark
    public String somethingPublicFieldAccessor_static_handle_bound_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_static_handle_bound.invokeExact();
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_static_handle_bound_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor_static_handle_bound.invokeExact();
    }

    @Benchmark
    public String somethingPublicFieldAccessor_static_final_handle_bound_access() throws Throwable {
        return (String) somethingPublicFieldAccessor_static_final_handle_bound.invokeExact();
    }

    @Benchmark
    public String somethingPrivateFieldAccessor_static_final_handle_bound_access() throws Throwable {
        return (String) somethingPrivateFieldAccessor_static_final_handle_bound.invokeExact();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ReflectionBenchmark.class.getName())
                .build();

        new Runner(opt).run();
    }
}
