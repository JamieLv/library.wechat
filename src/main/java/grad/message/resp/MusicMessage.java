/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.message.resp;

/**
 * 音乐消息(服务器回应)
 *
 * @author smile
 */
public class MusicMessage extends BaseMessage {

    //音乐
    private Music Music;

    public void setMusic(Music music) {
        this.Music = music;
    }

    public Music getMusic() {
        return Music;
    }

}
