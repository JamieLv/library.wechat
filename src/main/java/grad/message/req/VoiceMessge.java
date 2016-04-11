/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.message.req;

/**
 *
 * @author smile
 */
public class VoiceMessge extends BaseMessage {

    //媒体的ID
    private String MediaId;
    //音乐的格式
    private String Format;

    public void setMediaId(String MediaId) {
        this.MediaId = MediaId;
    }

    public void setFormat(String Format) {
        this.Format = Format;
    }

    public String getMediaId() {
        return MediaId;
    }

    public String getFormat() {
        return Format;
    }

}
