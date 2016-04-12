/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.pojo;

/**
 * 普通按钮(子按钮)
 *
 * Created by Jamie on 4/11/16.
 */

public class CommonButton extends Button {

    public static final String KEY_BUS = "KEY_BUS";
    public static final String KEY_BORROW_UMBRELLA = "KEY_BORROW_UMBRELLA";
    public static final String KEY_RETURN_UMBRELLA = "KEY_RETURN_UMBRELLA";
    public static final String KEY_SIDE_BUS = "KEY_SIDE_BUS";
    public static final String KEY_WORKTIME = "KEY_WORKTIME";
    public static final String KEY_CONTACTS = "KEY_CONTACTS";
    public static final String KEY_SUE = "KEY_SUE";
    public static final String KEY_SUE_TRACK = "KEY_SUE_TRACK";
    public static final String KEY_JOIN_US = "KEY_JOIN_US";

    private String type;
    private String key;
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
