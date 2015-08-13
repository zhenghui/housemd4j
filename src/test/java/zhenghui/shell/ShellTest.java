package zhenghui.shell;

import zhenghui.shell.exception.QuitException;

/**
 * User: zhenghui
 * Date: 15-8-12
 * Time: 下午8:40
 * shell test
 */
public class ShellTest {

    public static void main(String[] args) throws Exception {
        Shell shell = new Shell("zhenghui",null);

        /**
         * 结束命令
         */
        Command printCommand = new Command("print") {
            @Override
            public void run() {
                System.out.println("hello world");
            }
        };

        shell.addQuitCommand();
        shell.addHelpCommand();
        shell.addCommand(printCommand);

        shell.interact();
    }
}
