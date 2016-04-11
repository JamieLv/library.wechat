/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.message.req;

/**
 * ?接消息(用户请求)
 *
 * @author smile
 */
public class LinkMessage extends BaseMessage {

    //标题
    private String Title;

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getUrl() {
        return Url;
    }
    //描述
    private String Description;
    //链接
    private String Url;

}
