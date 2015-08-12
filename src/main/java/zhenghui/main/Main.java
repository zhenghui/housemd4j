package zhenghui.main;

import zhenghui.shell.Command;

/**
 * user: zhenghui on 2015/8/12.
 * date: 2015/8/12
 * time :22:37
 * email: zhenghui.cjb@taobao.com
 * 入口
 */
public class Main extends Command {

    public Main() {
        super("housemd4j");
        super.setDescription("a runtime diagnosis tool of JVM.");
    }

    public static void main(String[] args) {
        prepare();
    }

    /**
     * 做一些准备工作
     */
    private static void prepare() {


    }

    @Override
    public void run() {

    }
}
