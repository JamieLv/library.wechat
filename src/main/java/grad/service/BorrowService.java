package grad.service;

import grad.database.Database;
import grad.database.Member;
import grad.database.Member_Record;
import grad.message.resp.TemplateData;
import grad.message.resp.TemplateMessage;
import grad.util.WeixinUtil;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jamie on 4/19/16.
 */
public class BorrowService {

    public static String Renew (int Borrow_Statement) {
        String Renew;
        if (Borrow_Statement == 1) {
            Renew = "是";
        } else{
            Renew = "否";
        }
        return Renew;
    }

    public static void BorrowTemplate(int Borrow_Book_id){
        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        String accessToken = WeixinUtil.
                getAccessToken(appId, appSecret)
                .getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" +
                accessToken;

        TemplateMessage BorrowInfo = new TemplateMessage();
        BorrowInfo.setTemplate_id("qrhK4Cp_0leLwHFKcbpI6z6r-V7fOcOtEQA4Jg0hKnI");
//        MemberInfo.setUrl("http://weixin.qq.com/download");
        BorrowInfo.setTopcolor("#000000");
        BorrowInfo.setTouser(Database.getMember_Record(Borrow_Book_id).getBorrower());
        Member_Record member_record = Database.getMember_Record(Borrow_Book_id);

        Map<String, TemplateData> data = new HashMap<>();

        TemplateData Title = new TemplateData();
        Title.setValue(Database.getBookbyBook_id(Borrow_Book_id).getTitle());
        Title.setColor("#FF0000");
        data.put("Title", Title);

        TemplateData Name = new TemplateData();
        Name.setValue(member_record.getBorrower());
        Name.setColor("#FF0000");
        data.put("Name", Name);

        TemplateData BorrowTime = new TemplateData();
        BorrowTime.setValue(member_record.getBorrow_Time());
        BorrowTime.setColor("#FF0000");
        data.put("BorrowTime", BorrowTime);  
        
        TemplateData ReturnTime = new TemplateData();
        ReturnTime.setValue(member_record.getReturn_Time());
        ReturnTime.setColor("#FF0000");
        data.put("ReturnTime", ReturnTime);

        TemplateData Renew = new TemplateData();
        Renew.setValue(Renew(member_record.getBorrow_Statement()));
        Renew.setColor("#FF0000");
        data.put("Renew", Renew);

        BorrowInfo.setData(data);
        String jsonNote = JSONObject.fromObject(BorrowInfo).toString();
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
