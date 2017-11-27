package com.gotofinal.blog.tricks;

import org.junit.Assert;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class ReflectiveAccessModulesNative {

    static {
        // we need to load our library
        System.loadLibrary("libJniReflectiveAccess");
    }

    public static void main(String[] args) throws Throwable {
        ArrayList<String> list = new ArrayList<>();
        list.add("abc");

        // this time we just need to get field, and use our native method to make it accessible
        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        Assert.assertFalse(elementDataField.canAccess(list));
        setAccessible(elementDataField, true);

        Assert.assertTrue(elementDataField.isAccessible());
        Assert.assertTrue(elementDataField.canAccess(list));

        Object[] elementData = (Object[]) elementDataField.get(list);
        Assert.assertSame("abc", elementData[0]); // NOTE: we can use ==/same, as "abc" is literal added to constant pool on compile time.
        System.out.println(Arrays.toString(elementData));
    }

    // our native method
    private static native void setAccessible(AccessibleObject accessibleObject, boolean value);
}
