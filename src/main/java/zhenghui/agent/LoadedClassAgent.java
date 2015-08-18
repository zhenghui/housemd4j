package zhenghui.agent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

/**
 * User: zhenghui
 * Date: 15-8-18
 * Time: ÉÏÎç11:48
 */
public class LoadedClassAgent {

    public static void agentmain(String args, Instrumentation inst)throws Exception {

        File file = new File("/tmp/housemd4j/error.log");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(args == null ? "null" : "not null");
        fileWriter.close();

//        System.out.println("LoadedClassAgent agentmain attach...");
//
//        if(args == null || "".equals(args)){
//            args = "/home/admin/load_class_agent_temp.txt";
//        }
//        System.getProperties().setProperty("monitor.conf.loadAgentFile",args);
//
//        List<String> msgList = new ArrayList<String>();
//        for (Class clazz :inst.getAllLoadedClasses()){
//            msgList.add(clazz.getName());
//        }
//
//        File file = new File(args);
//        writeToFile(msgList,file);
//
//        System.out.println("LoadedClassAgent agentmain end...");
    }

    private static void  writeToFile(List<String> msgList,File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        for(String msg : msgList){
            fileWriter.write(msg);
            fileWriter.write('\r');
            fileWriter.write('\n');
        }
        fileWriter.close();
    }
}
