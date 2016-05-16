/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

final class Reflector {
    Reflector() {
    }

    public static Class getDependent(Field field) {
        ParameterizedType type = Reflector.getType(field);
        if (type != null) {
            return Reflector.getClass(type);
        }
        return null;
    }

    public static Class[] getDependents(Field field) {
        ParameterizedType type = Reflector.getType(field);
        if (type != null) {
            return Reflector.getClasses(type);
        }
        return new Class[0];
    }

    private static ParameterizedType getType(Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            return (ParameterizedType)type;
        }
        return null;
    }

    public static Class getReturnDependent(Method method) {
        ParameterizedType type = Reflector.getReturnType(method);
        if (type != null) {
            return Reflector.getClass(type);
        }
        return null;
    }

    public static Class[] getReturnDependents(Method method) {
        ParameterizedType type = Reflector.getReturnType(method);
        if (type != null) {
            return Reflector.getClasses(type);
        }
        return new Class[0];
    }

    private static ParameterizedType getReturnType(Method method) {
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            return (ParameterizedType)type;
        }
        return null;
    }

    public static Class getParameterDependent(Method method, int index) {
        ParameterizedType type = Reflector.getParameterType(method, index);
        if (type != null) {
            return Reflector.getClass(type);
        }
        return null;
    }

    public static Class[] getParameterDependents(Method method, int index) {
        ParameterizedType type = Reflector.getParameterType(method, index);
        if (type != null) {
            return Reflector.getClasses(type);
        }
        return new Class[0];
    }

    public static Class getParameterDependent(Constructor factory, int index) {
        ParameterizedType type = Reflector.getParameterType(factory, index);
        if (type != null) {
            return Reflector.getClass(type);
        }
        return null;
    }

    public static Class[] getParameterDependents(Constructor factory, int index) {
        ParameterizedType type = Reflector.getParameterType(factory, index);
        if (type != null) {
            return Reflector.getClasses(type);
        }
        return new Class[0];
    }

    private static ParameterizedType getParameterType(Method method, int index) {
        Type type;
        Type[] list = method.getGenericParameterTypes();
        if (list.length > index && (type = list[index]) instanceof ParameterizedType) {
            return (ParameterizedType)type;
        }
        return null;
    }

    private static ParameterizedType getParameterType(Constructor factory, int index) {
        Type type;
        Type[] list = factory.getGenericParameterTypes();
        if (list.length > index && (type = list[index]) instanceof ParameterizedType) {
            return (ParameterizedType)type;
        }
        return null;
    }

    private static Class getClass(ParameterizedType type) {
        Type[] list = type.getActualTypeArguments();
        if (list.length > 0) {
            return Reflector.getClass(list[0]);
        }
        return null;
    }

    private static Class[] getClasses(ParameterizedType type) {
        Type[] list = type.getActualTypeArguments();
        Class[] types = new Class[list.length];
        for (int i = 0; i < list.length; ++i) {
            types[i] = Reflector.getClass(list[i]);
        }
        return types;
    }

    private static Class getClass(Type type) {
        if (type instanceof Class) {
            return (Class)type;
        }
        return Reflector.getGenericClass(type);
    }

    private static Class getGenericClass(Type type) {
        if (type instanceof GenericArrayType) {
            return Reflector.getArrayClass(type);
        }
        return Object.class;
    }

    private static Class getArrayClass(Type type) {
        GenericArrayType generic = (GenericArrayType)type;
        Type array = generic.getGenericComponentType();
        Class entry = Reflector.getClass(array);
        if (entry != null) {
            return Array.newInstance(entry, 0).getClass();
        }
        return null;
    }

    public static String getName(String name) {
        int length = name.length();
        if (length > 0) {
            char[] array = name.toCharArray();
            char first = array[0];
            if (!Reflector.isAcronym(array)) {
                array[0] = Reflector.toLowerCase(first);
            }
            return new String(array);
        }
        return name;
    }

    private static boolean isAcronym(char[] array) {
        if (array.length < 2) {
            return false;
        }
        if (!Reflector.isUpperCase(array[0])) {
            return false;
        }
        return Reflector.isUpperCase(array[1]);
    }

    private static char toLowerCase(char value) {
        return Character.toLowerCase(value);
    }

    private static boolean isUpperCase(char value) {
        return Character.isUpperCase(value);
    }
}

