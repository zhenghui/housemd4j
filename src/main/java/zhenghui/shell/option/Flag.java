package zhenghui.shell.option;

import java.util.ArrayList;
import java.util.List;

/**
 * user: zhenghui on 2015/8/10.
 * date: 2015/8/10
 * time :22:53
 * email: zhenghui.cjb@taobao.com
 *  参数类型 Flag
 */
public class Flag {

    /**
     * 名字列表 ，比如 -f -flag
     */
    private List<String> names = new ArrayList<String>();

    /**
     * 描述
     */
    private String description;

    /**
     * 默认值
     */
    private Boolean defaultValue;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }
}
