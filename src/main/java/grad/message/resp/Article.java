/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.message.resp;

/**
 * 图文信息
 *(这里之所以另外建立一个图文信息的类，是因为在ArticleMessage中可能有多条Aritcle)
 * @author smile
 */
public class Article extends BaseMessage{

    //标题
    private String Title;
    //描述
    private String Description;
    //图片链接
    private String PicUrl;
    //点击图片跳转的链接
    private String Url;

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public void setPicUrl(String PicUrl) {
        this.PicUrl = PicUrl;
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

    public String getPicUrl() {
        return PicUrl;
    }

    public String getUrl() {
        return Url;
    }

}
