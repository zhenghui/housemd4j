package zhenghui.shell.print;

import jline.console.ConsoleReader;

import java.io.IOException;
import java.io.OutputStream;

/**
 * User: zhenghui
 * Date: 15-8-13
 * Time: 下午3:49
 */
public class PrintOutFactory {

    public static PrintOut build(final OutputStream outputStream) {
        return new PrintOut() {
            @Override
            public void print(Object object) {
                try {
                    outputStream.write(object.toString().getBytes());
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static PrintOut build(final ConsoleReader consoleReader){
        return new PrintOut() {
            @Override
            public void print(Object object) {
                try {
                    consoleReader.print(object.toString());
                    consoleReader.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
