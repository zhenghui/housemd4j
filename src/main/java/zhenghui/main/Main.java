package zhenghui.main;

import com.sun.tools.attach.VirtualMachine;
import zhenghui.shell.Command;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;

/**
 * user: zhenghui on 2015/8/12.
 * date: 2015/8/12
 * time :22:37
 * email: zhenghui.cjb@taobao.com
 * 入口
 */
public class Main extends Command {

    public Main() {
        super("housemd4j");
        super.setDescription("a runtime diagnosis tool of JVM.");
        prepare();
    }

    public static void main(String[] args) {
        new Main().run();
    }

    /**
     * 做一些准备工作
     */
    private void prepare() {
        /**
         * add print version flag
         */
        this.addFlag(Arrays.asList("-v","--version"),"show version.",false);

        this.addParameter("pid","id of process to be diagnosing.");
    }

    @Override
    public void run() {
        try {
            if (ManagementFactory.getOperatingSystemMXBean().getName().toLowerCase().contains("window")) {
                throw new IllegalStateException("Sorry, Windows is not supported now.");
            }
            //如果只是打印版本信息
            if(this.getFlag("-v")){
                System.out.println("v"+getVersion());
                return;
            }
            System.out.println("Welcome to HouseMD "+getVersion());
            VirtualMachine virtualMachine = VirtualMachine.attach(this.getParameter("pid","0"));
            virtualMachine.loadAgent(getAgentJar());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getVersion() throws IOException {
        JarInputStream stream = new JarInputStream(new FileInputStream(getAgentJar()));
        try {
            Attributes attributes = stream.getManifest().getMainAttributes();
            return attributes.getValue(Attributes.Name.SIGNATURE_VERSION);
        } finally {
            stream.close();
        }
    }

    /**
     * 获取agent jar包所在的地址
     *
     */
    private String getAgentJar() {
        String path = this.getClass().getResource("").getFile();
        if (path != null) {
            path = path.replace("!/zhenghui/main/", "");
            //不支持windows，所以不考虑windows的情况
            path = path.replace("file:","");
        }
        return path;
    }

}
