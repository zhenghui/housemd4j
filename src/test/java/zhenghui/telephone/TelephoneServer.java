package zhenghui.telephone;

import zhenghui.main.Mobilephone;
import java.util.concurrent.CountDownLatch;

/**
 * user: zhenghui on 2015/8/16.
 * date: 2015/8/16
 * time :18:56
 * email: zhenghui.cjb@taobao.com
 */
public class TelephoneServer {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        new TelephoneServer().run();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        try {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new Mobilephone(Integer.parseInt(getPort()),countDownLatch).listen();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听端口号。默认54321
     */
    private String getPort(){
        return  "54321";
    }
}

