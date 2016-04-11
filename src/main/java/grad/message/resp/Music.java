/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.message.resp;

/**
 *音乐的定义
 * (这里为什么要多定义个music)
 * @author smile
 */
public class Music extends BaseMessage {

    //音乐名称
    private String Title;
    //音乐描述
    private String Description;
    //音乐链接
    private String MusicUrl;
    //高品质音乐链接，wifi环境下优先使用
    private String HQMusicUrl;

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public void setMusicUrl(String MusicUrl) {
        this.MusicUrl = MusicUrl;
    }

    public void setHQMusicUrl(String HQMusicUrl) {
        this.HQMusicUrl = HQMusicUrl;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getMusicUrl() {
        return MusicUrl;
    }

    public String getHQMusicUrl() {
        return HQMusicUrl;
    }

}
