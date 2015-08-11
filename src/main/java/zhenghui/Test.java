package zhenghui;

import zhenghui.shell.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * user: zhenghui on 2015/8/4.
 * date: 2015/8/4
 * time :22:02
 * email: zhenghui.cjb@taobao.com
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Command command = new Command();
        //新增支持的flag option parameter
        command.addFlag(Arrays.asList("-f","--flag"),"enable flag",false);
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
