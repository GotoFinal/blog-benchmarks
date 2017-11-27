package com.gotofinal.blog.tricks;

import org.junit.Assert;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ReflectiveAccessModulesInstrumentation {

    public static void main(String[] args) throws Throwable {
        ArrayList<String> list = new ArrayList<>();
        list.add("abc");

        Field elementDataField = ArrayList.class.getDeclaredField("elementData");
        Assert.assertFalse(elementDataField.canAccess(list));
        elementDataField.setAccessible(true);

        Assert.assertTrue(elementDataField.isAccessible());
        Assert.assertTrue(elementDataField.canAccess(list));

        Object[] elementData = (Object[]) elementDataField.get(list);
        Assert.assertSame("abc", elementData[0]); // NOTE: we can use ==/same, as "abc" is literal added to constant pool on compile time.
        System.out.println(Arrays.toString(elementData));
    }

}
