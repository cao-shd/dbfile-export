package space.caoshd.common.util;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.lang.reflect.Constructor;

public class GroovyUtils {

    public static GroovyClassLoader groovyClassLoader = new GroovyClassLoader();

    @SuppressWarnings("unchecked")
    public static <T> T loadClassByText(String text) throws Exception {
        Class<?> clazz = groovyClassLoader.parseClass(text);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return (T) constructor.newInstance();
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadClassByFile(File file) throws Exception {
        Class<?> clazz = groovyClassLoader.parseClass(file);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return (T) constructor.newInstance();
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadClassByName(String name) throws Exception {
        Class<?> clazz = groovyClassLoader.loadClass(name);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return (T) constructor.newInstance();
    }

}
