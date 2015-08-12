import jline.console.ConsoleReader;

import java.io.IOException;

/**
 * user: zhenghui on 2015/8/12.
 * date: 2015/8/12
 * time :22:06
 * email: zhenghui.cjb@taobao.com
 */
public class Test10 {

    public static void main(String[] args) throws IOException {
        ConsoleReader reader = new ConsoleReader(System.in,System.out);
        reader.setPrompt("zhenghui> ");
        System.out.println(reader.readLine());
    }
}
