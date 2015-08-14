package zhenghui.util;

/**
 * User: zhenghui
 * Date: 15-8-14
 * Time: 下午2:26
 */
public class Util {

    /**
     * 获取对应类所在的地址
     * 只支持linux环境
     * @param clazz class
     */
    public static String getAgentJar(Class clazz){
        String path = clazz.getResource("").getFile();
        if (path != null) {
            path = path.replace("!/zhenghui/main/", "");
            //不支持windows，所以不考虑windows的情况
            path = path.replace("file:","");
        }
        return path;
    }
}
