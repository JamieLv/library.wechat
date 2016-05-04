package grad.service;

import grad.database.Book_State;
import grad.database.Database;
import grad.message.resp.TemplateData;
import grad.message.resp.TemplateMessage;
import grad.util.WeixinUtil;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jamie on 5/3/16.
 */
public class ReturnSuccess {

    public static void ReturnSuccessTemplate(int Book_ID, int Borrower_ID, String fromUserName) throws ParseException {
        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        String accessToken = WeixinUtil.
                getAccessToken(appId, appSecret)
                .getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" +
                accessToken;

        String Borrower_toUserName = Database.getMember_InfobyMember_ID(Borrower_ID).getMember_fromUserName();
        Book_State book_state = Database.getBook_StatebyBook_id(Book_ID);

        TemplateMessage ReturnSuccess = new TemplateMessage();
        ReturnSuccess.setTemplate_id("AyCYvZsW0qjL8PU053Y5r4QvFXSGSBn6uVc6__5icig");
        ReturnSuccess.setTopcolor("#000000");
        ReturnSuccess.setTouser(Borrower_toUserName);

        Map<String, TemplateData> data = new HashMap<>();

        TemplateData Title = new TemplateData();
        Title.setValue(book_state.getBook_Title());
        Title.setColor("#FF0000");
        data.put("Title", Title);

        TemplateData Member_Name = new TemplateData();
        Member_Name.setValue(Database.getMember_InfobyMember_ID(Borrower_ID).getMember_Name());
        Member_Name.setColor("#FF0000");
        data.put("Member_Name", Member_Name);

        TemplateData Worker_ID = new TemplateData();
        Worker_ID.setValue(String.valueOf(Database.getWoker_InfobyfromUserName(fromUserName).getWorker_ID()));
        Worker_ID.setColor("#FF0000");
        data.put("Worker_ID", Worker_ID);

        ReturnSuccess.setData(data);
        String jsonNote = JSONObject.fromObject(ReturnSuccess).toString();
//        System.out.println("Note to be sent:\n" + jsonNote);

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