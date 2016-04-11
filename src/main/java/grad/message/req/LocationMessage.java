/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.message.req;

/**
 * 位置信息(用户请求)
 *
 * @author smile
 */
public class LocationMessage extends BaseMessage {

    //地理位置的经度
    private String Location_X;
    //地理位置的纬度
    private String Location_Y;
    //地图缩放的大小
    private String Scale;
    //地理位置的信息
    private String Label;

    public void setLocation_X(String Location_X) {
        this.Location_X = Location_X;
    }

    public void setLocation_Y(String Location_Y) {
        this.Location_Y = Location_Y;
    }

    public void setScale(String Scale) {
        this.Scale = Scale;
    }

    public void setLabel(String Label) {
        this.Label = Label;
    }

    public String getLocation_X() {
        return Location_X;
    }

    public String getLocation_Y() {
        return Location_Y;
    }

    public String getScale() {
        return Scale;
    }

    public String getLabel() {
        return Label;
    }

}
