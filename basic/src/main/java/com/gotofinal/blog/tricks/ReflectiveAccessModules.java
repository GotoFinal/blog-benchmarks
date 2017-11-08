package com.gotofinal.blog.tricks;

import org.junit.Assert;
import sun.misc.Unsafe;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class ReflectiveAccessModules {
    public static void main(String[] args) throws Throwable {
        ArrayList<String> list = new ArrayList<>();
        list.add("abc");

        // we only need to call it once in our whole app and then store that function somewhere safe
        Consumer<AccessibleObject> setAccessible = doUnsafeMagic();

        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
//        elementDataField.setAccessible(true);
        setAccessible.accept(elementDataField);
        Object[] elementData = (Object[]) elementDataField.get(list);
        Assert.assertSame("abc", elementData[0]); // NOTE: we can use ==/same, as "abc" is literal added to constant pool on compile time.
        System.out.println(Arrays.toString(elementData));
    }

    public static Consumer<AccessibleObject> doUnsafeMagic() throws Throwable {
        // first we just need to get instance of unsafe, you can get shared static instance or just create own one:
        Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
        unsafeConstructor.setAccessible(true);
        Unsafe unsafe = unsafeConstructor.newInstance();

        // now we need to get our method that we want to edit:
        Method setAccessible = AccessibleObject.class.getDeclaredMethod("setAccessible0", boolean.class);

        // now we need to get field where modifiers of method are stored, and use unsafe to find offset from object header to this field:
        Field methodModifiers = Method.class.getDeclaredField("modifiers");
        long methodModifiersOffset = unsafe.objectFieldOffset(methodModifiers);

        // and now we set this modifiers field for our method to new value - just simple public modifier.
        unsafe.getAndSetInt(setAccessible, methodModifiersOffset, Modifier.PUBLIC);

        // and now we can prepare our function as simple reflections invoke call:
        return obj -> {
            try {
                setAccessible.invoke(obj, true);
            } catch (Exception e) {
                throw new RuntimeException(e); // you definitely should do this in a different way :D
            }
        };
    }
}
