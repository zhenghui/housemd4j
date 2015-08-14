package zhenghui.command;

import zhenghui.shell.Command;
import zhenghui.shell.print.PrintOut;
import zhenghui.util.ReflectionUtil;
import zhenghui.util.Util;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;

/**
 * User: zhenghui
 * Date: 15-8-14
 * Time: 下午2:05
 *
 * loaded 命令
 */
public class Loaded extends Command{

    private Instrumentation instrumentation;


    public Loaded(Instrumentation instrumentation,PrintOut printOut) {
        super("loaded");
        super.setDescription("display loaded classes information.");
        super.setPrintOut(printOut);
        this.instrumentation = instrumentation;

        //是否打印class loader信息
        this.addFlag(Arrays.asList("-h", "--classloader-hierarchies"),"display classloader hierarchies of loaded class.",false);

        //查询的class 名
        this.addParameter("name","class name without package name.");
    }

    @Override
    public void run() {
        String className = this.getParameter("name",null);
        if(className == null){
            warn("please input the class name");
            return;
        }
        Class[] classes = instrumentation.getAllLoadedClasses();
        for(Class clazz : classes){
            if(className.equals(clazz.getSimpleName())){
                println(clazz.getName()+" - " + Util.getAgentJar(clazz));
                if(this.getFlag("-h")){
                    layout(clazz.getClassLoader(),"- ");
                }
                return;
            }
        }
    }

    private void layout(ClassLoader classLoader,String lastIndents) {
        if(classLoader != null){
            String  indents = "\t" + lastIndents;
            println(indents + ReflectionUtil.getOrForceToNativeString(classLoader));
            layout(classLoader.getParent(),indents);
        }
    }
}
