package zhenghui.telephone;

import jline.TerminalFactory;
import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import zhenghui.shell.Command;
import zhenghui.shell.Shell;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * user: zhenghui on 2015/8/16.
 * date: 2015/8/16
 * time :18:57
 * email: zhenghui.cjb@taobao.com
 */
public class TelephoneClient {

    public static void main(String[] args) {
        new Thread(new Telephone()).start();
    }
}

class Telephone implements Runnable {

    int port = 54321;

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
            /**
             * 结束命令
             */
            Command printCommand = new Command("print") {
                @Override
                public void run() {
                    println("hello world");
                }
            };
            shell.addCommand(printCommand);
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
}
