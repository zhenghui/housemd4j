package zhenghui.shell;

import java.util.Arrays;
import java.util.Collections;

/**
 * User: zhenghui
 * Date: 15-8-12
 * Time: 下午8:38
 * command test
 */
public class CommandTest {

    /**
     * 用 -f  --single-value option value 作为参数执行
     */
    public static void main(String[] args) throws Exception {
        Command command = new Command("TestCommand") {
            @Override
            public void run() {
                System.out.println("it is my test command");
            }
        };
        //新增支持的flag option parameter
        command.addFlag(Arrays.asList("-f", "--flag"),"enable flag",false);
        command.addOption(Collections.singletonList("--single-value"),"set single value","value");
        command.addParameter("param","set param");
        //解析参数
        command.parse(args);

        if(command.getFlag("-h")){
            System.out.println(command.help());
        } else {
            if(command.getFlag("-f")){
                System.out.println("enable flags");
            }
            System.out.println(command.getOption("--single-value",""));
            System.out.println(command.getParameter("param",""));
        }

    }
}
