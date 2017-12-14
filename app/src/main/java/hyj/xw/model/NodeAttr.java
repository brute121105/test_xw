package hyj.xw.model;

import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by asus on 2017/10/28.
 */

public class NodeAttr {
    private String childPath;
    private String text;
    private String desc;
    private String isClickable;
    private String isEditable;
    private String getChildCount;
    private String getClassName;
    private AccessibilityNodeInfo node;


    public String getIsClickable() {
        return isClickable;
    }

    public void setIsClickable(String isClickable) {
        this.isClickable = isClickable;
    }

    public String getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(String isEditable) {
        this.isEditable = isEditable;
    }

    public String getGetChildCount() {
        return getChildCount;
    }

    public void setGetChildCount(String getChildCount) {
        this.getChildCount = getChildCount;
    }

    public String getGetClassName() {
        return getClassName;
    }

    public void setGetClassName(String getClassName) {
        this.getClassName = getClassName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getChildPath() {
        return childPath;
    }

    public void setChildPath(String childPath) {
        this.childPath = childPath;
    }



    public AccessibilityNodeInfo getNode() {
        return node;
    }

    public void setNode(AccessibilityNodeInfo node) {
        this.node = node;
    }


}
