package zhenghui.shell;

import jline.NoInterruptUnixTerminal;
import jline.Terminal;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * user: zhenghui on 2015/8/12.
 * date: 2015/8/12
 * time :22:06
 * email: zhenghui.cjb@taobao.com
 */
public class TerminalTest {

    public static void main(String[] args) throws Exception {
//        ConsoleReader reader = new ConsoleReader(System.in,System.out);
//        reader.setPrompt("zhenghui> ");
//        System.out.println(reader.readLine());

        Terminal terminal = new NoInterruptUnixTerminal();
        terminal.init();
        OutputStream outputStream = terminal.wrapOutIfNeeded(System.out);
        InputStream inputStream = terminal.wrapInIfNeeded(System.in);

        while (true){
            int available = inputStream.available();
            if(available >0){
                byte[] bytes = new byte[available];
                inputStream.read(bytes);
                outputStream.write(bytes);
                outputStream.write("\n".getBytes());
                outputStream.flush();
            }
        }

    }
}
