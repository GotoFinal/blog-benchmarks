package com.gotofinal.blog.benchmark.part2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 4, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 3, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 20, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(jvmArgsAppend = {"-XX:-TieredCompilation"})
public class StreamBenchmark2 {
    static final int SIZE = 1_000_000;
    Random random = new Random(123);
    Map<UUID, Person> workerMap = Stream.generate(() -> new Person(random)).limit(SIZE).collect(Collectors.toMap(p -> p.uuid, p -> p));

    @Benchmark
    public void advancedOperationsStream(Blackhole blackhole) {
        Collection<Person> people = workerMap.values().stream()
                                        .filter(person -> ((person.name.equals("Kate") || person.name.equals("Anna")) && (person.age <= 15)))
                                        .sorted(Comparator.comparingInt((Person p) -> p.age).thenComparing(p -> p.name).thenComparing(p -> p.uuid))
                                        .collect(Collectors.toList());
        blackhole.consume(people);
        if (people.size() != 83541) {
            throw new AssertionError();
        }
        // NOTE: .flatMap(p -> p.accounts.stream().filter(a -> a.money <= 0)) will be SLOWER
        List<Account> accounts = people.stream().flatMap(p -> p.accounts.stream()).filter(a -> a.money <= 0).collect(Collectors.toList());
        if (accounts.size() != 25117) {
            throw new AssertionError();
        }
        blackhole.consume(accounts);
    }

    @Benchmark
    public void advancedOperationsStreamParallel(Blackhole blackhole) {
        Collection<Person> people = workerMap.values().parallelStream()
                                        .filter(person -> ((person.name.equals("Kate") || person.name.equals("Anna")) && (person.age <= 15)))
                                        .sorted(Comparator.comparingInt((Person p) -> p.age).thenComparing(p -> p.name).thenComparing(p -> p.uuid))
                                        .collect(Collectors.toList());
        blackhole.consume(people);
        if (people.size() != 83541) {
            throw new AssertionError();
        }
        List<Account> accounts = people.parallelStream().flatMap(p -> p.accounts.stream().filter(a -> a.money <= 0)).collect(Collectors.toList());
        if (accounts.size() != 25117) {
            throw new AssertionError();
        }
        blackhole.consume(accounts);
    }

    @Benchmark
    public void advancedOperationsOldJava(Blackhole blackhole) {
        List<Person> people = new ArrayList<>(SIZE);
        for (Person person : workerMap.values()) {
            if ((person.name.equals("Kate") || person.name.equals("Anna")) && (person.age <= 15)) {
                people.add(person);
            }
        }
        people.sort((a, b) -> {
            int c = Integer.compare(a.age, b.age);
            if (c != 0) {
                return c;
            }
            c = a.name.compareTo(b.name);
            if (c != 0) {
                return c;
            }
            return a.uuid.compareTo(b.uuid);
        });
        if (people.size() != 83541) {
            throw new AssertionError();
        }
        blackhole.consume(people);
        List<Account> accounts = new ArrayList<>();
        for (Person person : people) {
            for (Account account : person.accounts) {
                if (account.money <= 0) {
                    accounts.add(account);
                }
            }
        }
        if (accounts.size() != 25117) {
            throw new AssertionError();
        }
        blackhole.consume(accounts);
    }

    static String[] names = {"Steve", "Kate", "Anna", "Brajanek", "( ͡º ͜ʖ͡º)", "Faggot"};

    static class Person {
        final UUID uuid = UUID.randomUUID();
        final String name;
        final int age;
        final Collection<Account> accounts;

        Person(Random random) {
            this.name = names[random.nextInt(names.length)];
            this.age = random.nextInt(60) + 1;
            int i = random.nextInt(4);
            this.accounts = new ArrayList<>(i);
            for (int x = 0; x < i; x++) {
                accounts.add(new Account(this, random));
            }
        }

        @Override
        public String toString() {
            return "{" + name + " (age: " + age + ")}";
        }
    }

    static class Account {
        final UUID uuid = UUID.randomUUID();
        final Person owner;
        final double money;

        Account(Person owner, Random random) {
            this.owner = owner;
            this.money = 4000 - random.nextInt(5000);
        }

        @Override
        public String toString() {
            return "Account{" + money + "$, of: " + owner + "}";
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                          .include(StreamBenchmark2.class.getName())
                          .forks(1)
                          .build();

        new Runner(opt).run();
    }
}
