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
        String Borrow_Book_Title = null;
        String Return_Book_Time = null;

        for (Book_State book_state: book_stateList) {
            String Return_Time = book_state.getBook_Return_Time();
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
            Date Return_Date = SDF.parse(Return_Time);
            Calendar Return_cal = Calendar.getInstance();
            Return_cal.setTime(Return_Date);
            Calendar StartofRemind = (Calendar) Return_cal.clone();
            StartofRemind.add(Calendar.DATE, -7);
            Calendar EndofRemind = (Calendar) Return_cal.clone();
            EndofRemind.add(Calendar.DATE, 7);

            String Today = Database.getDate(0);
            Date Today_Date = SDF.parse(Today);
            Calendar Today_cal = Calendar.getInstance();
            Today_cal.setTime(Today_Date);

            if (Today_cal.compareTo(StartofRemind) * Today_cal.compareTo(EndofRemind) != 1){
                Borrow_Book_Title += Borrow_Book_Title == null ? book_state.getBook_Title() : "\n" + book_state.getBook_Title();
                Return_Book_Time += Return_Book_Time == null ? book_state.getBook_Return_Time() : "\n" + book_state.getBook_Return_Time();
            }
        }



        TemplateMessage BorrowInfo = new TemplateMessage();
        BorrowInfo.setTemplate_id("i0NdPgIFXYzRiuWJiVHxWzhfXpU7LQvNXmTcvZf_bN4");
//        MemberInfo.setUrl("http://weixin.qq.com/download");
        BorrowInfo.setTopcolor("#000000");
        BorrowInfo.setTouser(fromUserName);

        Map<String, TemplateData> data = new HashMap<>();

        TemplateData Title = new TemplateData();
        Title.setValue(Borrow_Book_Title);
        Title.setColor("#FF0000");
        data.put("Title", Title);

        TemplateData Member_id = new TemplateData();
        Member_id.setValue(String.valueOf(Database.getMember_Info(fromUserName).getMember_ID()));
        Member_id.setColor("#FF0000");
        data.put("Member_id", Member_id);

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
