package zhenghui.shell;


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

        Command loadedCommand = new Command("loaded") {
            @Override
            public void run() {
                System.out.println(this.getParameter("-name",null));
            }
        };
        loadedCommand.addParameter("-name","class name without package name.");

        shell.addQuitCommand();
        shell.addHelpCommand();
        shell.addCommand(printCommand);
        shell.addCommand(loadedCommand);

        shell.interact();
    }
}
