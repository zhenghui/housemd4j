package zhenghui.shell.exception;

/**
 * user: zhenghui on 2015/8/10.
 * date: 2015/8/10
 * time :23:05
 * email: zhenghui.cjb@taobao.com
 * 操作符传入有误
 */
public class UnknownOptionException extends Exception {


    public UnknownOptionException(String message) {
        super(message);
    }

}
