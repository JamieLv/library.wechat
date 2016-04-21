package grad.service;

import grad.database.Member_Info;
import grad.message.resp.TemplateData;
import grad.message.resp.TemplateMessage;
import grad.util.WeixinUtil;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jamie on 4/15/16.
 */
public class MemberService {
    public static void MemberTemplate(Member_Info member_info){
        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        String accessToken = WeixinUtil.
                getAccessToken(appId, appSecret)
                .getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" +
                accessToken;

        TemplateMessage MemberInfo = new TemplateMessage();
        MemberInfo.setTemplate_id("Je7Hjl5L67Hy2EMjJBQ5PJIBU-CPG_k3e-tLN3a3k18");
        MemberInfo.setUrl("http://weixin.qq.com/download");
        MemberInfo.setTopcolor("#000000");
        MemberInfo.setTouser(member_info.getMember_fromUserName());

        Map<String, TemplateData> data = new HashMap<>();

        TemplateData Member_id = new TemplateData();
        Member_id.setValue(String.valueOf(member_info.getMember_ID()));
        Member_id.setColor("#FF0000");
        data.put("Member_id", Member_id);

        TemplateData Name = new TemplateData();
        Name.setValue(member_info.getMember_Name());
        Name.setColor("#FF0000");
        data.put("Name", Name);

        TemplateData Gender = new TemplateData();
        Gender.setValue(member_info.getMember_Gender());
        Gender.setColor("#FF0000");
        data.put("Gender", Gender);

        TemplateData Age = new TemplateData();
        Age.setValue(String.valueOf(member_info.getMember_Age()));
        Age.setColor("#FF0000");
        data.put("Age", Age);

        TemplateData Mobile = new TemplateData();
        Mobile.setValue(member_info.getMember_Mobile());
        Mobile.setColor("#FF0000");
        data.put("Mobile", Mobile);

        TemplateData RegisterTime = new TemplateData();
        RegisterTime.setValue(member_info.getMember_RegisterTime());
        RegisterTime.setColor("#FF0000");
        data.put("RegisterTime", RegisterTime);

        MemberInfo.setData(data);
        String jsonNote = JSONObject.fromObject(MemberInfo).toString();
        System.out.println("Note to be sent:\n" + jsonNote);

        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", jsonNote);
        int result = 0;
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                System.out.println(String.format("错误 errcode:{%s} errmsg:{%s}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg")));
            }
        }
        System.out.println("模板消息发送结果：" + result);
    }
}
