package zhenghui;

import java.util.Arrays;
import java.util.List;

/**
 * user: zhenghui on 2015/8/4.
 * date: 2015/8/4
 * time :22:02
 * email: zhenghui.cjb@taobao.com
 */
public class Test {

    public static void main(String[] args) {
        System.out.println("hello world");

        List<String> list = Arrays.asList("abc","bcd");
        System.out.println(list.subList(1,list.size()));
    }
}
