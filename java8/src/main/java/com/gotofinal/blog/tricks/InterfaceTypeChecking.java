package com.gotofinal.blog.tricks;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.expr.Cast;
import javassist.expr.ExprEditor;

import java.util.ArrayList;
import java.util.List;

class Executor {
    static List<Duck> ducks = new ArrayList<>();

    public static void run() {
        duck((Duck) (Object) "some string");
        duck((Duck) (Number) 12);
        duck((Duck) (Object) Executor.class);
        duck(new Duck() {});
        for (Duck duck : ducks) {
            System.out.println("Duck: " + duck + " {" + duck.getClass().getName() + "}");
        }
    }

    public static void duck(Duck arg) {
        System.out.println("Called duck with " + arg + " {" + arg.getClass().getName() + "} is this a duck: " + (arg instanceof Duck));
        ducks.add(arg);
// java.lang.IncompatibleClassChangeError: Class <arg class> does not implement the requested interface com.gotofinal.blog.tricks.Duck
//        arg.quack();
    }
}
interface Duck { default void quack() {} }

public class InterfaceTypeChecking {
    public static void main(String[] args) throws Exception {
        makeUnsafe("com.gotofinal.blog.tricks.Executor");

        // it is important not to use this class before magic above, as javassist can only apply changes to not yet loaded classes -
        // as it just get bytecode of not yet loaded class, change it, and load it - so java will not try to load original class anymore.
        Executor.run();
    }

    public static void makeUnsafe(String className) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass originalClass = pool.getCtClass(className);
        originalClass.instrument(new ExprEditor() {
            @Override
            public void edit(Cast c) throws CannotCompileException {
                c.replace("{$_ = $1;}"); // who needs casting?
            }
        });
        originalClass.toClass(); // this will apply all edits
    }
}
