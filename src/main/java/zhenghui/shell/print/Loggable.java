package zhenghui.shell.print;

/**
 * User: zhenghui
 * Date: 15-8-13
 * Time: 下午4:02
 * 日志打印
 */
public abstract class Loggable {

    private static final String CR = System.getProperty("line.separator");

    public abstract PrintOut getPrintOut();

    public void info(Object object){
        println("INFO : " + object);
    }

    public void warn(Object object){
        println("WARN : " + object);
    }

    public void error(Object object){
        println("ERROR : " + object);
    }

    public void print(Object object){
        PrintOut printOut = getPrintOut();
        if(printOut == null){
            printOut = PrintOutFactory.build(System.out);
        }
        printOut.print(object);
    }

    public void println(Object object){
        print(object+CR);
    }
}
