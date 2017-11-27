#include "com_gotofinal_blog_tricks_ReflectiveAccessModulesNative.h"

static jclass cachedClazz = nullptr;
static jmethodID cachedMethod = nullptr;

JNIEXPORT void JNICALL Java_com_gotofinal_blog_tricks_ReflectiveAccessModulesNative_setAccessible(JNIEnv *env, jclass clazz, jobject accessibleObject, jboolean value) {
    // we can also move preparing of method to separate method called once at load time to improve performance.
    if (cachedMethod == nullptr) {
        jclass cls = env->FindClass("java/lang/reflect/AccessibleObject"); // we need to to fetch AccessibleObject class
        // we should have method to remove that global ref when library is no longer needed, but we will skip this part here too
        cachedClazz = (jclass) env->NewGlobalRef(cls);
        // We need to get method from that class using name and signature, where Z -> boolean, so this is `boolean setAccessible0(boolean)`
        cachedMethod = env->GetMethodID(cls, "setAccessible0", "(Z)Z");
    }
    // and just call the method (and we are ignoring returned value)
    env->CallBooleanMethod(accessibleObject, cachedMethod, value);
}
