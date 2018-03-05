package hyj.xw.hook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */

public class ClazzAttribute {
    private String clsName;
    private List<String> fieldName = new ArrayList<String>();
    private List<String> methodName = new ArrayList<String>();
    private List<String> clsdName = new ArrayList<String>();

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public List<String> getFieldName() {
        return fieldName;
    }

    public void setFieldName(List<String> fieldName) {
        this.fieldName = fieldName;
    }

    public List<String> getMethodName() {
        return methodName;
    }

    public void setMethodName(List<String> methodName) {
        this.methodName = methodName;
    }

    public List<String> getClsdName() {
        return clsdName;
    }

    public void setClsdName(List<String> clsdName) {
        this.clsdName = clsdName;
    }
}
