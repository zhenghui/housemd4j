package zhenghui.agent;

import jline.TerminalFactory;
import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import zhenghui.shell.Command;
import zhenghui.shell.Shell;
import zhenghui.shell.print.PrintOut;
import zhenghui.shell.print.PrintOutFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * User: zhenghui
 * Date: 15-8-13
 * Time: 下午3:19
 * agent中启动的客户端类，接受到前台传入的命令并进行处理，然后返回结果
 */
public class Telephone implements Runnable {

    private Instrumentation instrumentation;

    private Integer port;

    private Class<Command>[] classes;

    public Telephone(Instrumentation instrumentation, Integer port, Class<Command>[] classes) {
        this.instrumentation = instrumentation;
        this.port = port;
        this.classes = classes;
    }


    @Override
    public void run() {
        Socket socket = null;
        FileHistory history = null;
        try {
            socket = new Socket("localhost",port);
            ConsoleReader reader = new ConsoleReader(socket.getInputStream(),socket.getOutputStream());
            history = new FileHistory(new File("/tmp/housemd4j/.history"));
            reader.setHistory(history);

            Shell shell  = new Shell("housemd4j", reader);
            shell.setDescription("a runtime diagnosis tool of jvm.");
            //新增help 命令
            shell.addHelpCommand();
            //新增结束命令
            shell.addQuitCommand();
            //新增命令
            for(Class<Command> clazz : classes){
                shell.addCommand(toCommand(clazz,PrintOutFactory.build(reader)));
            }

            shell.interact();

        }catch (Exception e) {
            try {
                writeToFile(e,new File("/tmp/housemd4j/error.log"));
            } catch (IOException ignored) {
            }
        } finally {
            TerminalFactory.reset();
            if(history != null){
                try {
                    history.flush();
                } catch (IOException ignore) {
                }
            }
            if(socket != null){
                try {
                    socket.shutdownOutput();
                    socket.shutdownInput();
                    socket.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    /**
     * 用反射创建clazz对应的对象
     * @param clazz 命令对应的class对象
     * @param printOut 数初六
     * @return 对应的命令对象
     */
    private Command toCommand(Class<Command> clazz,PrintOut printOut) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<Command> constructor = clazz.getConstructor(Instrumentation.class,PrintOut.class);

        return constructor.newInstance(instrumentation,printOut);
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
