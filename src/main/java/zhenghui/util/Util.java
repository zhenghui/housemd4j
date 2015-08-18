package zhenghui.util;

import jline.Terminal;

import java.net.URL;
import java.security.CodeSource;

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
        CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
        URL url = null;
        if(codeSource == null){
            url = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class");
        } else {
            url = codeSource.getLocation();
        }
        return url.toString().substring(url.toString().indexOf("file:")+5);
//        String path = clazz.getResource("").getFile();
//        if (path != null) {
//            if(path.contains("!")){
//                path = path.substring(0,path.indexOf("!"));
//            }
//            //不支持windows，所以不考虑windows的情况
//            path = path.replace("file:","");
//        }
//        return path;
    }

    public static void main(String[] args) {
        System.out.println(Util.getAgentJar(Terminal.class));
    }
}
