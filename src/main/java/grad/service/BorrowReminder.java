package grad.service;

import grad.database.Book_State;
import grad.database.Database;
import grad.message.resp.TemplateData;
import grad.message.resp.TemplateMessage;
import grad.util.WeixinUtil;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jamie on 4/29/16.
 */
public class BorrowReminder {

    public static void BorrowReminderTemplate(String fromUserName) throws ParseException {
        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        String accessToken = WeixinUtil.
                getAccessToken(appId, appSecret)
                .getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" +
                accessToken;

        int Borrower_ID = Database.getMember_Info(fromUserName).getMember_ID();
        List<Book_State> book_stateList = Database.getBook_StatebyBorrower(Borrower_ID);
        String Borrow_Book_Title = "";
        String Return_Book_Time = "";

        for (Book_State book_state: book_stateList) {
            if (Database.Borrower_RemindNeed(fromUserName) != 1){
                Borrow_Book_Title += Borrow_Book_Title.equals("") ? book_state.getBook_Title() : "\n          " + book_state.getBook_Title();
                Return_Book_Time += Return_Book_Time.equals("") ? book_state.getBook_Return_Time() : "\n                  " + book_state.getBook_Return_Time();
            }
        }



        TemplateMessage BorrowInfo = new TemplateMessage();
        BorrowInfo.setTemplate_id("7GH8ubmKHjPZXFpqZBusX1Ge0Ox6fFjI300HB8hTn6s");
//        MemberInfo.setUrl("http://weixin.qq.com/download");
        BorrowInfo.setTopcolor("#000000");
        BorrowInfo.setTouser(fromUserName);

        Map<String, TemplateData> data = new HashMap<>();

        TemplateData Title = new TemplateData();
        Title.setValue(Borrow_Book_Title);
        Title.setColor("#FF0000");
        data.put("Title", Title);

        TemplateData Member_Name = new TemplateData();
        Member_Name.setValue(String.valueOf(Database.getMember_Info(fromUserName).getMember_Name()));
        Member_Name.setColor("#FF0000");
        data.put("Member_Name", Member_Name);

        TemplateData ReturnTime = new TemplateData();
        ReturnTime.setValue(Return_Book_Time);
        ReturnTime.setColor("#FF0000");
        data.put("Return_Time", ReturnTime);

        BorrowInfo.setData(data);
        String jsonNote = JSONObject.fromObject(BorrowInfo).toString();
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
