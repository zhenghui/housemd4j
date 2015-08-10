package zhenghui.shell;

import zhenghui.shell.option.Flag;
import zhenghui.shell.option.Option;
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
     * 所有支持的flags
     */
    private List<Flag> flags = new ArrayList<Flag>();

    /**
     * 所有支持的options
     */
    private List<Option> options = new ArrayList<Option>();

    /**
     * 入参列表
     */
    private Map<String,String> values = new HashMap<String, String>();

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
            }
        }
    }

    /**
     * 新增参数
     * @param index
     * @param args
     * @return
     */
    private List<String> addParameter(int index, List<String> args) {
        return null;
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

}
