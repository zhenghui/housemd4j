package zhenghui.shell.exception;

/**
 * user: zhenghui on 2015/8/11.
 * date: 2015/8/11
 * time :21:33
 * email: zhenghui.cjb@taobao.com
 * 类型转换错误
 */
public class ConvertingException extends RuntimeException {
    public ConvertingException(String message) {
        super(message);
    }
}
