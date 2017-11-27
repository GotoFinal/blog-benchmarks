cmake_minimum_required(VERSION 3.7)
project(JniReflectiveAccess)

set(CMAKE_CXX_STANDARD 14)
find_package(JNI REQUIRED)

if (JNI_FOUND)
    message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
endif()
include_directories(${JNI_INCLUDE_DIRS})

add_library(JniReflectiveAccess SHARED com_gotofinal_blog_tricks_ReflectiveAccessModulesNative.h com_gotofinal_blog_tricks_ReflectiveAccessModulesNative.cpp)