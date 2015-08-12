package zhenghui.shell.completer;

import jline.console.completer.Completer;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zhenghui
 * Date: 15-8-12
 * Time: 下午7:32
 *
 * 命令自动补全
 */
public class CommandCompleter implements Completer {

    private List<String> commandNames;

    public CommandCompleter(List<String> commandNames) {
        this.commandNames = commandNames;
    }

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        String[] arrays = buffer.substring(0,cursor).split("\\s+",2);
        if(arrays.length == 0){
            candidates.addAll(commandNames);
            return cursor;
        } else if(arrays.length == 1){
            String prefix = arrays[0];
            candidates.addAll(getCommondNamesStartWith(prefix));
            return cursor - prefix.length();
        } else if(arrays.length == 2){
//            String n = arrays[0];
//            String p = arrays[1];
            //todo 参数的自动补全，懒得做了。。。
            return -1;

        }
        return 0;
    }

    private List<String> getCommondNamesStartWith(String prefix){
        List<String> result = new ArrayList<String>();
        for(String commandName:commandNames){
            if(commandName.startsWith(prefix)){
                result.add(commandName);
            }
        }
        return result;
    }
}
