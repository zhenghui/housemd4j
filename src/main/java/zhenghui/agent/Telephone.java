package zhenghui.agent;

import jline.TerminalFactory;
import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import zhenghui.shell.Command;
import zhenghui.shell.Shell;
import zhenghui.shell.print.PrintOut;
import zhenghui.shell.print.PrintOutFactory;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * User: zhenghui
 * Date: 15-8-13
 * Time: 下午3:19
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
    private Command toCommand(Class<Command> clazz,PrintOut printOut) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        //只有一个构造函数
        Constructor<Command> constructor = (Constructor<Command>) clazz.getConstructors()[0];

        return constructor.newInstance(instrumentation,printOut);
    }
}
