package com.gotofinal.blog.tricks;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MyAgent {
    public static void premain(String arg, Instrumentation instrumentation) {
        String packageName = ArrayList.class.getPackageName();
        Module listModule = ArrayList.class.getModule();
        Module ourModule = ReflectiveAccessModulesInstrumentation.class.getModule();
        instrumentation.redefineModule(listModule, Set.of(), Map.of(), Map.of(packageName, Set.of(ourModule)), Set.of(), Map.of());
    }
}
