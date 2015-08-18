package zhenghui.agent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
public class Duck5 {

    public static void agentmain(String arguments, Instrumentation instrumentation) throws Exception {
        try{
            File file = new File("/tmp/housemd4j/error.log");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(arguments);
            fileWriter.close();
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
                    .getConstructor(Instrumentation.class, Integer.class, Class[].class)
                    .newInstance(instrumentation, port, commandClasses);

            //创建幽灵线程,执行telephoneClass.
            Thread thread = new Thread(executor, "HouseMD-Duck");
            thread.setDaemon(true);
            thread.start();
        }catch (Throwable throwable){
            File file = new File("/tmp/housemd4j/error3.log");
            writeToFile(throwable,file);
        }

    }

    private static Class<?>[] loadClasses(String[] classNames, ClassLoader classLoader) throws ClassNotFoundException {
        Class<?>[] classes = (Class<?>[]) Array.newInstance(Class.class, classNames.length);
        for (int i = 0; i < classes.length; i++) {
            classes[i] = Class.forName(classNames[i], false, classLoader);
        }
        return classes;
    }

    public static void main(String[] args) throws Exception {
        String strs = "/home/zhenghui/workspace/housemd4j/housemd4j.jar zhenghui.agent.Telephone 54321 zhenghui.command.Loaded";
//        Duck.agentmain(strs,null);
//        while (true){}
        File file = new File("/tmp/housemd4j/error.log");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("null");
        fileWriter.close();
    }

    private static void  writeToFile(Throwable throwable,File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(throwable.getClass().getName()+" : " + throwable.getLocalizedMessage() +"\r\n");
        StackTraceElement[] stackElements = throwable.getStackTrace();//通过Throwable获得堆栈信息
        for (StackTraceElement stackElement : stackElements) {
            fileWriter.write(stackElement.getClassName() + "\t");
            fileWriter.write(stackElement.getFileName()+"\t");
            fileWriter.write(stackElement.getLineNumber() + "\t");
            fileWriter.write(stackElement.getMethodName() +"\t\n");
        }
        fileWriter.close();
    }
}
