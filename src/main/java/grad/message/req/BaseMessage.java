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
public class BaseMessage {  
    // 开发者微信号  
    private String ToUserName;  
    // 发送方帐号（一个OpenID）  
    private String FromUserName;  
    // 消息创建时间 （整型）  
    private long CreateTime;  
    // 消息类型（text/image/location/link）  
    private String MsgType;  
    // 消息id，64位整型  
    private long MsgId;  
  
    public String getToUserName() {  
        return ToUserName;  
    }  
  
    public void setToUserName(String toUserName) {  
        ToUserName = toUserName;  
    }  
  
    public String getFromUserName() {  
        return FromUserName;  
    }  
  
    public void setFromUserName(String fromUserName) {  
        FromUserName = fromUserName;  
    }  
  
    public long getCreateTime() {  
        return CreateTime;  
    }  
  
    public void setCreateTime(long createTime) {  
        CreateTime = createTime;  
    }  
  
    public String getMsgType() {  
        return MsgType;  
    }  
  
    public void setMsgType(String msgType) {  
        MsgType = msgType;  
    }  
  
    public long getMsgId() {  
        return MsgId;  
    }  
  
    public void setMsgId(long msgId) {  
        MsgId = msgId;  
    }  
} 