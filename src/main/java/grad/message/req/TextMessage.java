/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.message.req;

/**
 * 文本信息(用户请求)
 *
 * @author smile
 */
public class TextMessage extends BaseMessage {

    //消息内容

    private String Content;

    public void setContent(String content) {
        this.Content = content;
    }

    public String getContent() {
        return Content;
    }

}
