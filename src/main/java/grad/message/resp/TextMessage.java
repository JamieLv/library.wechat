/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package grad.message.resp;

/**
 *
 * Created by Jamie on 4/11/16.
 */
public class TextMessage extends BaseMessage {
    
    //内容
    private String Content;

    public void setContent(String content) {
        this.Content = content;
    }

    public String getContent() {
        return Content;
    }
    
    
}
