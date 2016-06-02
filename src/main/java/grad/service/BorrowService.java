package grad.service;

import grad.database.Borrow_Record;
import grad.database.Database;
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
        if (Borrow_Statement <= 4) {
            Renew = "是";
        } else {
            Renew = "否";
        }
        return Renew;
    }

    public static void BorrowTemplate(int Borrow_Book_id, String fromUserName){
        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        String accessToken = WeixinUtil.
                getAccessToken(appId, appSecret)
                .getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" +
                accessToken;


        String Book_Title = Database.getBook_StatebyBook_id(Borrow_Book_id).getBook_Title();
        String Borrow_Book_Time = Database.getBook_StatebyBook_id(Borrow_Book_id).getBook_Borrow_Time();
        String Return_Book_Time = Database.getBook_StatebyBook_id(Borrow_Book_id).getBook_Return_Time();
        int Borrow_Statement_ID = Database.getBook_StatebyBook_id(Borrow_Book_id).getBook_Statement_ID();

        TemplateMessage BorrowInfo = new TemplateMessage();
        BorrowInfo.setTemplate_id("51Rs2tIHdSDp7i0vI72U8SiJgct5dGYlK9QOo_wZ3bA");
//        MemberInfo.setUrl("http://weixin.qq.com/download");
        BorrowInfo.setTopcolor("#000000");
        BorrowInfo.setTouser(fromUserName);

        Map<String, TemplateData> data = new HashMap<>();

        TemplateData Title = new TemplateData();
        Title.setValue(Book_Title);
        Title.setColor("#FF0000");
        data.put("Title", Title);

        TemplateData Member_id = new TemplateData();
        Member_id.setValue(String.valueOf(Database.getMember_Info(fromUserName).getMember_ID()));
        Member_id.setColor("#FF0000");
        data.put("Member_id", Member_id);

        TemplateData BorrowTime = new TemplateData();
        BorrowTime.setValue(Borrow_Book_Time);
        BorrowTime.setColor("#FF0000");
        data.put("Borrow_Time", BorrowTime);
        
        TemplateData ReturnTime = new TemplateData();
        ReturnTime.setValue(Return_Book_Time);
        ReturnTime.setColor("#FF0000");
        data.put("Return_Time", ReturnTime);

        TemplateData Renew = new TemplateData();
        Renew.setValue(Borrow_Book_Time != Database.getDate(0) ? Renew(Borrow_Statement_ID) : Renew(Borrow_Statement_ID) + "\n\n温馨提醒：借书当日不可续借");
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
