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
 * ��Ӧ��agent��
 */
public class Duck {

    public static void agentmain(String arguments, Instrumentation instrumentation) throws Exception {
        //�Ѵ���Ĳ����ÿո�ָ�,�ܹ���Ϊ4����.(ÿ��������Ӧ��˵����1.2.3��˵��.�Ѿ��������)
        String[] parts = arguments.split("\\s+", 4);
        //agent����jar,ת��URL
        URL agentJar = new File(parts[0]).toURI().toURL();
        String telephoneClassName = parts[1];
        //terminal�ļ����˿ں�.��telephone��ʹ�õ�.
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

        //������commond��Ӧ����,ͨ�����洴����classloader��loader����.
        Class<?>[] commandClasses = loadClasses(parts[3].split("\\s+"), classLoader);

        //ͨ������,����һ��telephoneClass��ʵ��.ע�����ﴫ��Ĳ���.instrumentation, port, commandClasses
        Runnable executor = (Runnable) classLoader.loadClass(telephoneClassName)
                .getConstructor(Instrumentation.class, int.class, Class[].class)
                .newInstance(instrumentation, port, commandClasses);

        //���������߳�,ִ��telephoneClass.
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
