package zhenghui.agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * user: zhenghui on 2015/8/12.
 * date: 2015/8/12
 * time :22:25
 * email: zhenghui.cjb@taobao.com
 * 对应的agent类
 */
public class Duck {

    public static void agentmain(String arguments, Instrumentation instrumentation) throws Exception {
        //把传入的参数用空格分割,总共分为4部分.(每个参数对应的说明看1.2.3的说明.已经很清楚了)
        String[] parts = arguments.split("\\s+", 4);
        //agent所在jar,转成URL
        URL agentJar = new File(parts[0]).toURI().toURL();
        String telephoneClassName = parts[1];
        //terminal的监听端口号.给telephone类使用的.
        int port = Integer.parseInt(parts[2]);

        ClassLoader classLoader = new URLClassLoader(new URL[]{agentJar}) {
            @Override
            protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                Class<?> loadedClass = findLoadedClass(name);
                if (loadedClass != null) return loadedClass;

                try {
                    Class<?> aClass = findClass(name);
                    if (resolve) resolveClass(aClass);
                    return aClass;
                } catch (Exception e) {
                    return super.loadClass(name, resolve);
                }
            }
        };

        //把所有commond对应的类,通过上面创建的classloader给loader进来.
        Class<?>[] commandClasses = loadClasses(parts[3].split("\\s+"), classLoader);

        //通过反射,创建一个telephoneClass的实例.注意这里传入的参数.instrumentation, port, commandClasses
        Runnable executor = (Runnable) classLoader.loadClass(telephoneClassName)
                .getConstructor(Instrumentation.class, int.class, Class[].class)
                .newInstance(instrumentation, port, commandClasses);

        //创建幽灵线程,执行telephoneClass.
        Thread thread = new Thread(executor, "HouseMD-Duck");
        thread.setDaemon(true);
        thread.start();
    }

    private static Class<?>[] loadClasses(String[] classNames, ClassLoader classLoader) throws ClassNotFoundException {
        Class<?>[] classes = (Class<?>[]) Array.newInstance(Class.class, classNames.length);
        for (int i = 0; i < classes.length; i++) {
            classes[i] = Class.forName(classNames[i], false, classLoader);
        }
        return classes;
    }
}
