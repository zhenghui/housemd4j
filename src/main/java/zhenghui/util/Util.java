package zhenghui.util;

import jline.Terminal;

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
            if(path.contains("!")){
                path = path.substring(0,path.indexOf("!"));
            }
            //不支持windows，所以不考虑windows的情况
            path = path.replace("file:","");
        }
        return path;
    }

    public static void main(String[] args) {
        System.out.println(Util.getAgentJar(Terminal.class));
    }
}
