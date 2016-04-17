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

    public static final String KEY_MEMBERSHIP = "KEY_MEMBERSHIP";
    public static final String KEY_BORROW_BOOK = "KEY_BORROW_BOOK";
    public static final String KEY_RETURN_BOOK = "KEY_RETURN_BOOK";
    public static final String KEY_HELP = "KEY_HELP";

    public static final String KEY_BOOK = "KEY_BOOK";
    public static final String KEY_RESERVE_ROOM = "KEY_RESERVE_ROOM";
    public static final String KEY_BOOK_RECOMMAND = "KEY_BOOK_RECOMMAND";
    public static final String KEY_NEARBY = "KEY_NEARBY";

    public static final String KEY_ADVICE = "KEY_ADVICE";
    public static final String KEY_ADVICE_TRACK = "KEY_ADVICE_TRACK";
    public static final String KEY_WEBSITE = "KEY_WEBSITE";
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
