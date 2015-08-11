package zhenghui.shell;

import zhenghui.shell.option.Flag;
import zhenghui.shell.option.Option;
import zhenghui.shell.option.Parameter;
import zhenghui.shell.option.exception.ConvertingException;
import zhenghui.shell.option.exception.UnknownOptionException;

import java.util.*;

/**
 * user: zhenghui on 2015/8/10.
 * date: 2015/8/10
 * time :22:01
 * email: zhenghui.cjb@taobao.com
 */
public class Command {

    /**
     * 命令名字
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 所有支持的flags
     */
    private List<Flag> flags = new ArrayList<Flag>();

    /**
     * 所有支持的options
     */
    private List<Option> options = new ArrayList<Option>();

    /**
     * 所有支持的参数
     */
    private List<Parameter> parameters = new ArrayList<Parameter>();

    /**
     * 入参列表
     */
    private Map<String,String> values = new HashMap<String, String>();


    public Command() {
        //默认添加help 的flag
        List<String> names = Arrays.asList("-h","--help");
        String desc = "show help infomation of this command.";
        addFlag(names,desc,false);
    }

    public String help(){
        StringBuilder sb = new StringBuilder();
        sb.append("Usage: ").append(name);
        if(!options.isEmpty()){
            sb.append("[OPTIONS]");
        }
        if(!parameters.isEmpty()){
            sb.append(" ");
            for(Parameter parameter:parameters){
                sb.append(parameter.getName()).append(" ");
            }
        }
        sb.append("\n\t");
        if(!options.isEmpty()){
            sb.append("\nOptions:\n");
            for(Option option : options){
                sb.append(option.toString()).append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * 获取对应的flag的值
     * @param name flag name
     */
    public boolean getFlag(String name) throws ConvertingException {
        String value = values.get(name);
        try{
            return Boolean.parseBoolean(value);
        }catch (Exception e){
            throw new ConvertingException( "Invalid " + name + " value: " + value);
        }
    }

    public String getOption(String name,String defaultValue) {
        String value = values.get(name);
        return value == null ? defaultValue : value;
    }

    public String getParameter(String name,String defaultValue){
        String value = values.get(name);
        return value == null ? defaultValue : value;
    }

    /**
     * 新增支持的flags
     */
    public void addFlag(List<String> names,String description,boolean defaultValue){
        //d不能为空
        if(names == null || names.isEmpty()){
            throw new IllegalArgumentException("At least one name should be given to option.");
        }
        //flag不能重复
        for(String name: names){
            for(Flag flag : flags){
                for(String flagName : flag.getNames()){
                    if(flagName.equals(name)){
                        throw new IllegalStateException(names + " have already been used in " + flag.getNames());
                    }
                }
            }
        }
        Flag flag = new Flag();
        flag.setNames(names);
        flag.setDefaultValue(defaultValue);
        flag.setDescription(description);
        flags.add(flag);
    }

    public void addOption(List<String> names,String desc,String defaultValue){
        //d不能为空
        if(names == null || names.isEmpty()){
            throw new IllegalArgumentException("At least one name should be given to option.");
        }
        //option不能重复
        for(String name: names){
            for(Option option : options){
                for(String optionName : option.getNames()){
                    if(optionName.equals(name)){
                        throw new IllegalStateException(names + " have already been used in " + option.getNames());
                    }
                }
            }
        }
        Option option = new Option();
        option.setDefaultValue(defaultValue);
        option.setDescription(desc);
        option.setNames(names);
        options.add(option);
    }

    /**
     * 新增参数
     * @param name 参数名
     * @param description 描述
     */
    public void addParameter(String name,String description){
        //判断parameter不能重复
        for(Parameter parameter:parameters){
            if(parameter.getName().equals(name)){
                throw new IllegalStateException(name + " have already been used");
            }
        }
        Parameter parameter = new Parameter();
        parameter.setName(name);
        parameter.setDescription(description);
        parameters.add(parameter);
    }



    private int index = 0;

    /**
     * 解析入参
     * @param args 参数
     */
    public void parse(String[] args) throws Exception {
        values.clear();
        read(Arrays.asList(args));
    }

    private void read(List<String> args) throws Exception{
        if(!args.isEmpty()){
            String head = head(args);
            //如果是 flag 或者option 类型 （flag和option类型都是以 “-”开头）
            if(head.matches("-[a-zA-Z-]+")){
                read(addOption(head, tail(args)));
            } else {
                read(addParameter(index,args));
                index ++;
            }
        }
    }

    /**
     * 新增参数
     * @param index 对应的可选参数序号
     * @param args 参数列表
     */
    private List<String> addParameter(int index, List<String> args) {
        if(index >= parameters.size()){
            return new ArrayList<String>();
        }
        values.put(parameters.get(index).getName(),head(args));
        return tail(args);
    }

    /**
     * 新增option 。包括 flag 和option 类型
     * @param head 操作符
     * @param strings 命令行后缀
     * @return 执行结束以后的命令行参数列表
     */
    private List<String> addOption(String head, List<String> strings) throws Exception{
        //如果对应的操作符在支持的flag操作符内
        for(Flag flag : flags){
            if(flag.getNames().contains(head)){
                values.put(head(flag.getNames()),"true");
                return strings;
            }
        }
        //如果对应的操作符在支持的option操作符内
        for(Option option : options){
            if(option.getNames().contains(head)){
                //如果是option类型，那么对应的命令行后缀必然跟着参数
                if(strings.isEmpty()){
                    throw new UnknownOptionException("unknown option");
                }
                values.put(head(option.getNames()), head(strings));
                return tail(strings);
            }
        }
        return strings;
    }

    private String head(List<String> list) {
        return list.get(0);
    }

    private List<String> tail(List<String> list) {
        return list.subList(1,list.size());
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
}
