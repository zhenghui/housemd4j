package zhenghui.shell;

import jline.console.ConsoleReader;
import zhenghui.shell.completer.CommandCompleter;
import zhenghui.shell.exception.QuitException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: zhenghui
 * Date: 15-8-12
 * Time: 下午7:24
 * 模拟shell命令，对对应的命令作出反应
 */
public class Shell {

    /**
     * shell 名。
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     *
     */
    private List<Command> commandList = new ArrayList<Command>();

    /**
     * jline console reader
     */
    private ConsoleReader reader;

    public Shell(String name) throws IOException {
        reader = new ConsoleReader(System.in,System.out);
        this.name = name;
    }

    /**
     * 开始交互
     */
    public void interact() throws Exception {
        /**
         * 命令行提示符
         */
        String prompt = name + "> ";
        reader.setPrompt(prompt);
        reader.addCompleter(new CommandCompleter(getCommandNames()));

        //解析
        parse(reader.readLine());

        reader.shutdown();
    }

    private List<String> getCommandNames(){
        List<String> result = new ArrayList<String>();
        for(Command command : commandList){
            result.add(command.getName());
        }
        return result;
    }

    /**
     * 解析执行输入的命令
     * @param line 命令行输入行数据
     * @throws IOException
     */
    private void parse(String line) throws Exception {
        if(line == null){
            return;
        }
        String[] array = line.trim().split("\\s+");
        try{
            String head = array[0];
            if(head.length() > 0){
                run(head,tail(array));
            }
        }catch (QuitException e){
            return;
        }
        parse(reader.readLine());
    }

    /**
     * 执行对应的命令
     * @param commandName 命令名
     * @param arguments 参数
     */
    private void run(String commandName,String[] arguments) throws Exception {
        for(Command command : commandList){
            if(command.getName().equals(commandName)){
                command.parse(arguments);
                command.run();
                return;
            }
        }
        //如果没有这个命令
        System.out.println("Unknown command: "+commandName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addCommand(Command command) {
        if(command != null){
            commandList.add(command);
        }
    }

    private String head(String[] arrays){
        if(arrays.length > 0){
            return arrays[0];
        } else {
            return null;
        }
    }

    private String[] tail(String[] arrays){
        int length = arrays.length;
        if(length > 1){
            String[] temp = new String[length -1];
            System.arraycopy(arrays,1,temp,0, length -1);
            return temp;
        } else {
            return new String[]{};
        }
    }

}
