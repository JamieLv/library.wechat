/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.message.req;

/**
 *
 * Created by Jamie on 4/11/16.
 */
public class ImageMessage extends BaseMessage {

    //图片的链接
    private String PicUrl;

    public void setPicUrl(String PicUrl) {
        this.PicUrl = PicUrl;
    }

    public String getPicUrl() {
        return PicUrl;
    }

}
