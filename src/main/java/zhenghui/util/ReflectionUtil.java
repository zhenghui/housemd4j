package zhenghui.util;

/**
 * User: zhenghui
 * Date: 15-8-14
 * Time: 下午2:53
 */
public class ReflectionUtil {

    /**
     * 类信息
     * @param object 对应的类对象
     */
    public static String getOrForceToNativeString(Object object){
        if(object.toString().startsWith(object.getClass() + "@")){
            return object.toString();
        } else {
            return object.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(object));
        }
    }
}
