package grad.service;

import grad.database.*;
import grad.main.TagManager;
import grad.main.UserManager;
import grad.message.resp.Article;
import grad.message.resp.NewsMessage;
import grad.message.resp.TextMessage;
import grad.pojo.CommonButton;
import grad.servlet.BaiduMapAPI;
import grad.tools.*;
import grad.util.MessageUtil;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static grad.tools.RetrieveDocumentByURL.Return_BookInfo;
import static grad.tools.RetrieveDocumentByURL.Return_BookPicURL;

/**
 *
 * Created by Jamie on 4/11/16.
 */
public class CoreService {

    public static String getGreeting() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour >= 6 && hour < 8) {
            greeting = "早上好";
        } else if (hour >= 8 && hour < 11) {
            greeting = "上午好";
        } else if (hour >= 11 && hour < 13) {
            greeting = "中午好";
        } else if (hour >= 13 && hour < 18) {
            greeting = "下午好";
        } else {
            greeting = "晚上好";
        }
        return greeting;
    }

    public static List<Article> SubscribeGreeting(String fromUserName) {
        List<Article> articleList = new ArrayList<>();
        Subscriber_Info subscriber_info = Database.getSubscriber_Info(fromUserName);

        Article articleSubscriberGreeting = new Article();
        articleSubscriberGreeting.setTitle(subscriber_info.getNickname() + "，" + getGreeting());
        articleList.add(articleSubscriberGreeting);

        Article articleSubscriberInfo = new Article();
        articleSubscriberInfo.setTitle("性别：" + subscriber_info.getSubscriber_Sex() +
                "\n所在地：" + subscriber_info.getSubscriber_Country() + " " + subscriber_info.getSubscriber_Province() + " " + subscriber_info.getSubscriber_City());
        articleSubscriberInfo.setPicUrl(subscriber_info.getSubscriber_HeadImgURL());
        articleList.add(articleSubscriberInfo);

        return articleList;
    }

    public static String Register(String fromUserName, String[] keywords) throws IOException {
        String respContent = "请求处理异常，请稍候尝试！";
        try {
            int tag = Database.MemberExist(fromUserName);

            if (tag == 0) {  // 第一次注册
                // String Member_Name, String Member_Gender, int Member_Age, String Member_Mobile, String Member_RegisterTime, String Member_fromUserName, Boolean Member_Verification
                Member_Info new_member_info = new Member_Info(
                        keywords[0], keywords[1], Integer.parseInt(keywords[2]), keywords[3], Database.getDate(0), fromUserName, false);
                Database.Add(new_member_info);

                int yzm = Database.getMember_Info(fromUserName).getMember_ID();
                SendMsg_webchinese sendMsg = new SendMsg_webchinese();
                sendMsg.send(keywords[3], yzm);
                respContent = "尊敬的读者，请输入您收到的短信验证码，仿照格式: yzm 1";
            } else if (tag == 1) { // 用户已登记，手机验证未通过
                Database.UpdateMobile(fromUserName, keywords[3]);
                int yzm = Database.getMember_Info(fromUserName).getMember_ID();
                SendMsg_webchinese sendMsg = new SendMsg_webchinese();
                sendMsg.send(keywords[3], yzm);
                respContent = "尊敬的读者，请输入您收到的短信验证码，仿照格式: \"yzm 1\"";
            } else if (tag == 2) { // 已登记，手机验证通过
                respContent = "尊敬的读者，您已完成注册，请点击\"登录/注册\"按钮进行登录，出现读者证后请稍等10分钟，之后可进行正常操作，谢谢配合。";
            }
        } catch (NumberFormatException e) { // 格式有误
            respContent = "尊敬的读者，您输入的信息有误，请核对后重新输入！仿照格式: \"张三 男 20 13112345678\"。\n" +
                    "我们将发送验证短信至您填写的手机号，所以请务必填写正确的手机号，谢谢配合。";
        }
        if (keywords[0].equals("yzm")) { // yzm 1
            if (keywords.length == 2) {
                String str_yzm = keywords[1];
                int i_yzm = Integer.parseInt(str_yzm); // 获取验证码并转换成原型
                int tag = Database.MemberExist(fromUserName);
                if (tag == 0) {
                    respContent = "尊敬的读者，您还没有输入您的基本信息吧！\n"
                            + "请严格按照这个格式进行回复： \n"
                            + "姓名 性别 年龄 手机号\n"
                            + "60秒内将会收到有验证码的短信。\n"
                            + "到时请将验证码回复给微信平台，谢谢配合。";
                } else if (tag == 1) {
                    if (i_yzm == Database.getMember_Info(fromUserName).getMember_ID()) {
                        Database.UpdateMember_Verification(fromUserName, true);
                        respContent = "恭喜您验证成功！请再次点击\"登录/注册\"按钮进行登录，出现读者证后请稍等10分钟，之后可进行正常操作，谢谢配合。";
                    } else {
                        respContent = "尊敬的读者，验证码输入有误，请仔细核对！\n"
                                + "或者再次按照以下格式进行回复： \n"
                                + "姓名 性别 年龄 手机号\n"
                                + "60秒内将会收到有验证码的短信。\n"
                                + "到时请将验证码回复给微信平台，谢谢配合。";
                    }
                } else { // 验证已通过
                    respContent = "尊敬的读者，您已完成注册，请点击\"登录/注册\"按钮进行登录，出现读者证后请稍等10分钟，之后可进行正常操作，谢谢配合。";
                }
            } else {
                respContent = "验证码格式错误，请仿照格式: \"yzm 1\"回复，谢谢配合。";
            }
        }
        return respContent;
    }

    public static List<Article> SearchBookDisplay(String Search_Book_Title) throws IOException {

        List<Article> articleList = new ArrayList<>();
        Book_State book_state = Database.getBook_StatebyTitle(Search_Book_Title);
        List<Book_State> book_stateList = Database.getBook_StateListbyTitle(Search_Book_Title);
        String BookinLib = "";

        for (Book_State book_stateLib: book_stateList) {
            if (book_stateLib.getBook_Statement_ID() == 0){
                BookinLib += BookinLib.equals("") ?
                        Database.getLibrarInfo(book_stateLib.getBook_inLibrary_id()).getLibrary_Name() :
                        "；" + Database.getLibrarInfo(book_stateLib.getBook_inLibrary_id()).getLibrary_Name();
            }
        }

        Article articleBOOK = new Article();
        articleBOOK.setTitle("书名: 《" + Search_Book_Title + "》");
        articleBOOK.setPicUrl(Return_BookPicURL(book_state.getBook_ISBN()));
        articleBOOK.setUrl("https://www.baidu.com/s?ie=UTF-8&wd=" + Search_Book_Title);
        articleList.add(articleBOOK);

        Article articleISBN = new Article();
        articleISBN.setTitle("ISBN: " + book_state.getBook_ISBN());
        articleList.add(articleISBN);

        Article articleCATALOG = new Article();
        articleCATALOG.setTitle("类别: " + book_state.getBook_Category());
        articleList.add(articleCATALOG);

        Article articleAUTHOR = new Article();
        articleAUTHOR.setTitle("作者: " + book_state.getBook_Author());
        articleList.add(articleAUTHOR);

        Article articlePUBLISHER = new Article();
        articlePUBLISHER.setTitle("出版商: " + book_state.getBook_Publisher());
        articleList.add(articlePUBLISHER);

        Article articlePUBTIME = new Article();
        articlePUBTIME.setTitle("发行时间: " + book_state.getBook_PubTime());
        articleList.add(articlePUBTIME);

        Article articleBOOKSTATEMENT = new Article();
        articleBOOKSTATEMENT.setTitle("存书状态: " + BookinLib);
        articleList.add(articleBOOKSTATEMENT);

        return articleList;
    }

    public static List<Article> AddBookDisplay(Book_State new_book_state) throws IOException {

        List<Article> articleList = new ArrayList<>();

        Article articleBOOK = new Article();
        articleBOOK.setTitle("书本编号: " + new_book_state.getBook_id() + " 书名: 《" + new_book_state.getBook_Title() + "》");
        articleBOOK.setPicUrl(Return_BookPicURL(new_book_state.getBook_ISBN()));
        articleBOOK.setUrl("https://www.baidu.com/s?ie=UTF-8&wd=" + new_book_state.getBook_Title());
        articleList.add(articleBOOK);

        Article articleISBN = new Article();
        articleISBN.setTitle("ISBN: " + new_book_state.getBook_ISBN());
        articleList.add(articleISBN);

        Article articleCATALOG = new Article();
        articleCATALOG.setTitle("类别: " + new_book_state.getBook_Category());
        articleList.add(articleCATALOG);

        Article articleAUTHOR = new Article();
        articleAUTHOR.setTitle("作者: " + new_book_state.getBook_Author());
        articleList.add(articleAUTHOR);

        Article articlePUBLISHER = new Article();
        articlePUBLISHER.setTitle("出版商: " + new_book_state.getBook_Publisher());
        articleList.add(articlePUBLISHER);

        Article articlePUBTIME = new Article();
        articlePUBTIME.setTitle("发行时间: " + new_book_state.getBook_PubTime());
        articleList.add(articlePUBTIME);

        Article articleBOOKSTATEMENT = new Article();
        articleBOOKSTATEMENT.setTitle("存书状态: " + Database.getLibrarInfo(new_book_state.getBook_inLibrary_id()).getLibrary_Name());
        articleList.add(articleBOOKSTATEMENT);

        return articleList;
    }

    public static List<Article> MemberRecordDisplay(String fromUserName) throws IOException {
        List<Article> articleList = new ArrayList<>();
        Article articleBorrowRecord = new Article();
        articleBorrowRecord.setTitle("借阅记录");
        articleList.add(articleBorrowRecord);
        List<Book_State> book_stateList = Database.getBook_StatebyBorrower(Database.getMember_Info(fromUserName).getMember_ID());
        for (Book_State book_state: book_stateList) {
            String Borrow_Book_Title = book_state.getBook_Title();
            String Borrow_Book_ISBN = book_state.getBook_ISBN();
            String Book_Borrow_Time = book_state.getBook_Borrow_Time();
            String Book_Return_Time = book_state.getBook_Return_Time();

            Article articleBorrowRecordInput = new Article();
            articleBorrowRecordInput.setTitle("书名: " + Borrow_Book_Title + "\n"
                    + "借阅时间: " + Book_Borrow_Time + "\n"
                    + "归还时间: " + Book_Return_Time);
            articleBorrowRecordInput.setPicUrl(Return_BookPicURL(Borrow_Book_ISBN));
            articleList.add(articleBorrowRecordInput);
        }
        return articleList;
    }

    public static List<Article> NearbyLibrary(String Location_X, String Location_Y) throws IOException {
        List<Article> articleList = new ArrayList<>();
        String region = BaiduMapAPI.testPost(Location_X, Location_Y).get("city");

        Article articleNearbyLibrary = new Article();
        articleNearbyLibrary.setTitle("附近的图书馆");
        articleList.add(articleNearbyLibrary);
        List<JSONObject> resultsList = BaiduMapAPI.getLibraryfrom(Location_X, Location_Y);
        for (JSONObject LibraryInfo: resultsList){
            Article articleLibraryInfo = new Article();
            articleLibraryInfo.setTitle("名字：" + LibraryInfo.get("name") +
                    "\n地址：" + LibraryInfo.get("address"));
            if (LibraryInfo.get("telephone") != null){
                articleLibraryInfo.setTitle(articleLibraryInfo.getTitle() +
                    "\n电话：" + LibraryInfo.get("telephone"));
            }
            //http://api.map.baidu.com/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving&region=西安&output=html //调起百度PC或Web地图，展示“西安市”从（lat:34.264642646862,lng:108.95108518068 ）“我家”到“大雁塔”的驾车路线。
            articleLibraryInfo.setUrl("http://api.map.baidu.com/direction?" +
                    "origin=" + Location_X + "," + Location_Y +
                    "&destination=" + LibraryInfo.get("name") +
                    "&region=" + region +
                    "&mode=walking&output=html");
            articleList.add(articleLibraryInfo);
        }

        return articleList;
    }

    public static String processRequest(HttpServletRequest request) {
        String respMessage = null;
        Database db = new Database();
//        String currentTime;
//        //获得当前时间
//        //格式如下： 20160411-11:12:43
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")); //获取当天日期
//        SimpleDateFormat sdf = new SimpleDateFormat("20yyMMdd-HH:mm:ss");
//        currentTime = sdf.format(calendar.getTime());

        try {
            // 默认返回的文本消息内容
            String respContent = "请求处理异常，请稍候尝试！";

            // 处理微信发来请求
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 时间
            String createTime = requestMap.get("CreateTime");

            // 默认回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);

            // 创建图文信息
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setFuncFlag(0);

            List<Article> articleList;

            Subscriber_Info subscriber_info = db.getSubscriber_Info(fromUserName);
            Member_Info member_info = db.getMember_Info(fromUserName);
            Worker_Info worker_info = db.getWoker_InfobyfromUserName(fromUserName);
            if (subscriber_info != null) {
                db.UpdateSubscriber_Info(fromUserName);
            }

            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                //拿到用户发来的信息 去除用户回复信息的前后空格
                String content = requestMap.get("Content").trim();
                String[] keywords = content.trim().split(" ");
                switch (subscriber_info.getSubscriber_Function()) {
                    case "register":
                     // 吕嘉铭 男 22 13611774556
                        respContent = Register(fromUserName, keywords);
                        break;

                    case "searchbook":
                        if (db.getBook_StatebyTitle(content) == null) {
                            respContent = "此书尚未录入";
                        } else {
                            articleList = SearchBookDisplay(content);

                            // 设置图文消息个数
                            newsMessage.setArticleCount(articleList.size());
                            // 设置图文消息包含的图文集合
                            newsMessage.setArticles(articleList);
                            // 将图文消息对象转换成xml字符串
                            respMessage = MessageUtil.newsMessageToXml(newsMessage);

                            return respMessage;
                        }
                        break;

                    case "addbook": // Library_ID + scanresult
                        try {
                            if (keywords.length == 1) {
                                int Library_id = Integer.parseInt(keywords[0]);
                                if (db.getLibrarInfo(Library_id) != null) {
                                    db.UpdateWorker_Coefficient(fromUserName, Library_id);
                                    respContent = "存入" + db.getLibrarInfo(Library_id).getLibrary_Name();
                                } else { respContent = "图书馆不存在，请输入正确的图书馆代号"; }
                            } else {
                                respContent = "输入格式有误。请先输入\"图书馆代号\"再扫描图书条形码。";
                            }
                        } catch (NumberFormatException e) {
                            respContent = "请输入图书馆代号";
                        }

                        break;

                    case "supervisor":
                        if (content.startsWith("Deletemember") || content.startsWith("deletemember")){
                            Member_Info del_member_info = db.getMember_InfobyMember_ID(Integer.parseInt(keywords[1]));
                            db.Del(del_member_info);
                            respContent = "读者\n" + del_member_info.getMember_ID() + " " + del_member_info.getMember_Name() + " " + del_member_info.getMember_Mobile() + "\n删除成功";
                        } else if (content.startsWith("Addworker") || content.startsWith("addworker")){
                            // addworker worker_name worker_gender worker_age worker_mobile worker_duty
                            Worker_Info new_worker_info = new Worker_Info(keywords[1], keywords[2], Integer.parseInt(keywords[3]), keywords[4], keywords[5]);
                            db.Add(new_worker_info);
                            respContent = "员工\n" + keywords[1] + " " + keywords[2] + " " + Integer.parseInt(keywords[3]) + " " + keywords[4] + " " + keywords[5] + "\n添加成功";
                        } else if (content.startsWith("Deleteworker") || content.startsWith("deleteworker")){
                            Worker_Info del_worker_info = db.getWoker_InfobyWorker_Name(keywords[1]);
                            db.Del(del_worker_info);
                            respContent = "员工\n" + del_worker_info.getWorker_ID() + " " + del_worker_info.getWorker_Name() + " " + del_worker_info.getWorker_Duty() + "\n删除成功";
                        } else if (content.startsWith("Duty") || content.startsWith("duty")) {
                            int Worker_ID = Integer.parseInt(keywords[1]);
                            db.UpdateWorker_Duty(Worker_ID, keywords[2]);
                            respContent = "员工" + db.getWoker_Info(Worker_ID).getWorker_Name() + "更改为" + keywords[2] + "权限";
                        } else if (content.equals("Help") || content.equals("help") || content.equals("H") || content.equals("h")){
                            respContent = "使用帮助\n" +
                                    "删除读者：Deletemember 读者ID\n" +
                                    "添加职工：Addworker 职工姓名 职工性别 职工年龄 职工手机号 职工职能\n" +
                                    "更改员工职能：Duty 职工编号 职工职能\n" +
                                    "删除职工：Deleteworker 职工姓名";
                        } else { respContent = "回复Help进行查询功能。"; }
                        break;

                    default:
                        respContent = getGreeting() + "，尊敬的用户" + emoji(0x1F604)
                                + "\n您的留言我们已经收到，并在24小时内回复您。";
                }

                if (content.startsWith("Worker") || content.startsWith("worker")) { // Worker 1 吕嘉铭
                    int tag = db.WorkerExist(keywords[1], keywords[2], fromUserName);
                    if (tag == 0) {
                        respContent = "您的员工号不符，请重新核实，谢谢配合。";
                    } else if (tag == 1) {
                        db.UpdateWorker_Verification(keywords[2], fromUserName);
                        respContent = db.getMember_Info(fromUserName) != null && db.getWoker_InfobyfromUserName(fromUserName) != null ?
                                "成功与员工账号绑定，输入\"Worker 员工编号 员工姓名\"即可登录。" :
                                "成功与员工账号绑定，请点击\"登录/注册\"按钮进行登录。";
                    } else if (tag == 2) {
                        if (worker_info.getWorker_Duty().equals("还书管理员")) {
                            TagManager.batchtagging(fromUserName, "ReturnWorker");
                            respContent = "还书员工【" + worker_info.getWorker_Name() + "】登录成功";
                        } else if (worker_info.getWorker_Duty().equals("增书管理员")) {
                            TagManager.batchtagging(fromUserName, "AddWorker");
                            respContent = "增书员工【" + worker_info.getWorker_Name() + "】登录成功";
                        }
                    }
                } else if (content.equals("Member") || content.equals("member")) {

                    if (db.MemberExist(fromUserName) == 2) {
                        TagManager.batchtagging(fromUserName, "Member");
                        MemberService.MemberTemplate(member_info);
                        return "";
                    } else if (db.MemberExist(fromUserName) == 1){

                    } else {
                            respContent = getGreeting() + "，尊敬的用户" + emoji(0x1F604)
                                    + "\n您还没有输入您的基本信息吧！请点击\"登录/注册\"按钮进行注册，谢谢配合。";
                    }

                } else if (content.equals("Addbook") || content.equals("addbook")) {
                    db.UpdateSubscriber_Function(subscriber_info.getSubscriber_ID(), "addbook");
                    respContent = subscriber_info.getSubscriber_Function().equals("addbook") ? "增添书本功能关闭" : "增添书本功能开启";

                } else if (content.equals(931014) && worker_info.getWorker_Duty().equals("超级管理员")) {
                    db.UpdateSubscriber_Function(subscriber_info.getSubscriber_ID(), "supervisor");
                    respContent = subscriber_info.getSubscriber_Function().equals("supervisor") ? "超级管理员模式关闭" : "超级管理员模式开启";

                } else {
                    respContent = getGreeting() + "，尊敬的用户" + emoji(0x1F604)
                        + "\n您的留言我们已经收到，并在24小时内回复您。";
                }

            } // 图片消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                respContent = "我喜欢你发的图片！";

            } // 地理位置消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {

                String Location_X = requestMap.get("Location_X");
                String Location_Y = requestMap.get("Location_Y");

                articleList = NearbyLibrary(Location_X, Location_Y);
                // 设置图文消息个数
                newsMessage.setArticleCount(articleList.size());
                // 设置图文消息包含的图文集合
                newsMessage.setArticles(articleList);
                // 将图文消息对象转换成xml字符串
                respMessage = MessageUtil.newsMessageToXml(newsMessage);

                return respMessage;
            } // 链接消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "你发送的是链接消息哦！";

            } // 音频消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "你发送的是音频消息哦！";

                String voice = requestMap.get("Recognition").trim();
                String text = requestMap.get("MsgType");
                Map<String, String> map = new HashMap<>();
                String creat_time = requestMap.get("CreateTime");


                respContent = voice + "\n" + text + "\n" + creat_time;

            } // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    JSONObject User_Info = UserManager.getUser_Info(fromUserName);
                    if (db.getSubscriber_Info(fromUserName) == null) {
                        String Subscriber_Sex = "未知";
                        if ((int) User_Info.get("sex") == 1) {
                            Subscriber_Sex = "男";
                        } else if ((int) User_Info.get("sex") == 2) {
                            Subscriber_Sex = "女";
                        }

                        Subscriber_Info new_subscriber_info = new Subscriber_Info(
                                1, (String) User_Info.get("openid"), (String) User_Info.get("nickname"), Subscriber_Sex, (String) User_Info.get("language"),
                                (String) User_Info.get("city"), (String) User_Info.get("province"), (String) User_Info.get("country"), (String) User_Info.get("headimgurl"), "Subscribe");
                        db.Add(new_subscriber_info);
                    } else {
                        db.UpdateSubscriber_Info(fromUserName);
                    }

                    articleList = SubscribeGreeting(fromUserName);
                    // 设置图文消息个数
                    newsMessage.setArticleCount(articleList.size());
                    // 设置图文消息包含的图文集合
                    newsMessage.setArticles(articleList);
                    // 将图文消息对象转换成xml字符串
                    respMessage = MessageUtil.newsMessageToXml(newsMessage);

                    return respMessage;

                } // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    db.UpdateUnSubscriber_Info(fromUserName);
                    System.out.println(subscriber_info.getNickname() + "退订");

                } else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) { // 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    String eventKey = requestMap.get("EventKey");

                    int tag = 0; // 既不是读者也不是职工
                    if (member_info != null && worker_info == null) {tag=1;} // 是读者不是职工
                    else if (member_info == null && worker_info != null) { // 是职工不是读者
                        if (worker_info.getWorker_Duty().equals("还书管理员")) {
                            tag = 21;
                        } else if (worker_info.getWorker_Duty().equals("增书管理员")) {
                            tag =22;
                        }
                    }
                    else if (member_info != null && worker_info != null) {tag=3;} // 是职工也是读者

                    if (eventKey.equals(CommonButton.KEY_LOGIN)) {
                        int Subscriber_ID = subscriber_info.getSubscriber_ID();
                        db.UpdateSubscriber_Function(Subscriber_ID, "register");
                        switch (tag){
                            case 0:
                                respContent = "请输入\"姓名 性别 年龄 手机号\"注册";
                                break;
                            case 1:
                                if(member_info.getMember_Verification() == false){ // 用户已登记，手机验证未通过
                                respContent = "尊敬的读者，请输入您收到的短信验证码，仿照格式: yzm 1\n"
                                        + "或者再次按照以下格式进行回复： \n"
                                        + "Member 姓名 性别 年龄 手机号\n"
                                        + "60秒内将会收到有验证码的短信。\n"
                                        + "到时请将验证码回复给微信平台，谢谢配合。";
                                } else { // 成功
                                    db.UpdateSubscriber_Function(Subscriber_ID, "register");
                                    TagManager.batchtagging(fromUserName, "Member");
                                    MemberService.MemberTemplate(member_info);
                                    return "";
                                }
                                break;
                            case 21:
                                db.UpdateSubscriber_Function(Subscriber_ID, "register");
                                TagManager.batchtagging(fromUserName, "ReturnWorker");
                                respContent = "还书员工【" + worker_info.getWorker_Name() + "】登录成功";
                                break;
                            case 22:
                                db.UpdateSubscriber_Function(Subscriber_ID, "register");
                                TagManager.batchtagging(fromUserName, "AddWorker");
                                respContent = "增书员工【" + worker_info.getWorker_Name() + "】登录成功";
                                break;
                            case 3:
                                db.UpdateSubscriber_Function(Subscriber_ID, "register");
//                                TagManager.batchtagging(fromUserName, "Reader+Worker");
                                respContent = "若要以读者身份登录，请回复Member或member进行登录。\n\n" +
                                        "若要以员工身份登录，请回复\"Worker 员工号 员工姓名\"。\n\n" +
                                        "登录后10分钟内菜单功能将会开启。";
                                break;
                            default:
                                respContent = "按键功能出错，我们正在抢救。";
                        }

                    } else if (eventKey.equals(CommonButton.KEY_MEMBERSHIP)) {
                        MemberService.MemberTemplate(member_info);
                        return "";
                    } else if (eventKey.equals(CommonButton.KEY_RECORD)) {
                        if (db.getBook_StatebyBorrower(member_info.getMember_ID()) == null){
                            respContent = "尊敬的读者，您目前没有未归还的书本。\n欢迎您至附近的图书馆借阅。";
                        } else {
                            articleList = MemberRecordDisplay(fromUserName);

                            // 设置图文消息个数
                            newsMessage.setArticleCount(articleList.size());
                            // 设置图文消息包含的图文集合
                            newsMessage.setArticles(articleList);
                            // 将图文消息对象转换成xml字符串
                            respMessage = MessageUtil.newsMessageToXml(newsMessage);

                            return respMessage;
                        }
                    } else if (eventKey.equals(CommonButton.KEY_LOG_OFF)) {
                        switch (tag){
                            case 1:
                                TagManager.batchuntagging(fromUserName, "Member");
                                respContent = "退出成功";
                                break;
                            case 21:
                                TagManager.batchuntagging(fromUserName, "ReturnWorker");
                                respContent = "退出成功";
                                break;
                            case 22:
                                TagManager.batchuntagging(fromUserName, "AddWorker");
                                respContent = "退出成功";
                                break;
                            case 3:
                                TagManager.batchuntagging(fromUserName, "Member");
                                TagManager.batchuntagging(fromUserName, "ReturnWorker");
                                TagManager.batchuntagging(fromUserName, "AddWorker");
                                respContent = "退出成功";
                                break;
                            default:
                                respContent = "退出失败";
                        }
                    } else if (eventKey.equals(CommonButton.KEY_BOOK)) {
                        db.UpdateSubscriber_Function(subscriber_info.getSubscriber_ID(), "searchbook");
                        respContent = subscriber_info.getSubscriber_Function().equals("searchbook") ? "书本查询功能已经关闭，可点击按钮重新开启。" : "回复\"书名\"查询您想要的书本！再次点击可关闭查询功能。";
                    } else if (eventKey.equals(CommonButton.KEY_RESERVE_ROOM)) {
                        respContent = "22！";
                    } else if (eventKey.equals(CommonButton.KEY_BOOK_RECOMMEND)) {
                        respContent = "23！";
                    } else if (eventKey.equals(CommonButton.KEY_ADVICE)) {
                        respContent = "31！";
                    } else if (eventKey.equals(CommonButton.KEY_JOIN_US)) {
                        respContent = "34！";
                    }
                } else if (eventType.equals(MessageUtil.EVENT_TYPE_SCANCODE_WAITMSG)) {
                    String eventKey = requestMap.get("EventKey");
                    String scanResult = requestMap.get("ScanResult");
                    String[] Book_State_Info = scanResult.trim().split(" ");
                    if (eventKey.equals(CommonButton.KEY_BORROW_BOOK)) {
                        int Book_Borrower_ID = member_info.getMember_ID();
                        /*
                         * 借书
                         */
                        if (scanResult.startsWith("Book_Info")){ // Book_Info 5 剪刀石头布 1 艾尔法图书馆
                            int Borrow_Book_ID = Integer.parseInt(Book_State_Info[1]);
                            Book_State book_state = db.getBook_StatebyBook_id(Borrow_Book_ID);
                            if (book_state.getBook_Statement_ID() == 0) { // 借书
                                db.UpdateBook_State(Borrow_Book_ID, Book_Borrower_ID);
                                if (db.Borrow_RecordExist(Borrow_Book_ID, Book_Borrower_ID)) {
                                    db.UpdateBorrow_Record(Borrow_Book_ID, Book_Borrower_ID);
                                } else {
                                    Borrow_Record new_borrow_record = new Borrow_Record(Borrow_Book_ID, Book_Borrower_ID, 1);
                                    db.Add(new_borrow_record);
                                }
                                BorrowService.BorrowTemplate(Borrow_Book_ID, fromUserName);
                                return "";
                            } else { // 续借
                                String Return_Time = book_state.getBook_Return_Time();
                                SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
                                Date Return_Date = SDF.parse(Return_Time);
                                Calendar Return_cal = Calendar.getInstance();
                                Return_cal.setTime(Return_Date);
                                Calendar StartofRenew = (Calendar) Return_cal.clone();
                                StartofRenew.add(Calendar.DATE, -7);
                                String str_StartofRenew = SDF.format(StartofRenew.getTime());

                                if (calendar.compareTo(Return_cal) * calendar.compareTo(StartofRenew) != 1) {
                                    db.UpdateBook_State(Borrow_Book_ID, Book_Borrower_ID);
                                    db.UpdateBorrow_Record(Borrow_Book_ID, Book_Borrower_ID);
                                    BorrowService.BorrowTemplate(Borrow_Book_ID, fromUserName);
                                    return "";
                                } else {
                                    respContent = Return_cal.compareTo(calendar) == -1 ?
                                            "《" + book_state.getBook_Title() + "》应于" + Return_Time + "归还，逾期不可续借。"
                                            : "续借只能在归还时间前7天操作，谢谢配合。\n" +
                                            "《" + book_state.getBook_Title() + "》最早于" + str_StartofRenew + "可以续借。";
                                }
                            }
                        } else {respContent = "非馆藏书本。";}
                    } else if (eventKey.equals(CommonButton.KEY_RETURN_BOOK)) {
                        try {
                            int Borrow_Book_ID = Integer.parseInt(Book_State_Info[1]);
                            if (member_info.getMember_ID() != db.getBook_StatebyBook_id(Borrow_Book_ID).getBook_Borrower_ID()) {
                                ReturnSuccess.ReturnSuccessTemplate(Borrow_Book_ID, db.getBook_StatebyBook_id(Borrow_Book_ID).getBook_Borrower_ID(), fromUserName);
                                db.ReturnBook(Borrow_Book_ID);
                                respContent = "归还成功";
                            } else if (member_info.getMember_ID() == db.getBook_StatebyBook_id(Borrow_Book_ID).getBook_Borrower_ID()) {
                                respContent = "工作人员不能给本人进行还书操作";
                            }
                        }
                        catch (Exception e) {
                            respContent = "归还失败 " + scanResult;
                            e.printStackTrace();
                        }
                    } else if (eventKey.equals(CommonButton.KEY_ADD_BOOK)) {
                        if (subscriber_info.getSubscriber_Function().equals("addbook") && worker_info != null) {
                            String[] Add_Book_Scanresult = scanResult.split(",");
                            String Add_Book_ISBN = Add_Book_Scanresult[1];
                            System.out.println(Add_Book_ISBN);
                            DouBanBook new_book = Return_BookInfo(Add_Book_ISBN);

                            Book_State new_book_state = new Book_State(
                                    Add_Book_ISBN, new_book.getTitle(), new_book.getTags(), new_book.getAuthor(),
                                    new_book.getPublisher(), new_book.getPubdate(), new_book.getPrice(), worker_info.getWorker_Coefficient(), "归还");
                            Database.Add(new_book_state);
                            articleList = AddBookDisplay(new_book_state);

                            // 设置图文消息个数
                            newsMessage.setArticleCount(articleList.size());
                            // 设置图文消息包含的图文集合
                            newsMessage.setArticles(articleList);
                            // 将图文消息对象转换成xml字符串
                            respMessage = MessageUtil.newsMessageToXml(newsMessage);

                            return respMessage;
                        } else if (worker_info == null) {
                            respContent = "您不是书本管理员，不可使用此功能。";
                        } else {
                            respContent = "增添书本功能未开启，输入\"addbook\"可开启功能，再次输入可关闭。";
                        }
                    }
                    else {
                        System.out.println("二维码信息: " + scanResult);
                        respContent = getGreeting();
                    }
                }
            }

            textMessage.setContent(respContent);
            respMessage = MessageUtil.textMessageToXml(textMessage);
        }
         catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }

    /**
     * emoji表情转换(hex -> utf-16)
     *
     * @param hexEmoji
     * @return
     */
    public static String emoji(int hexEmoji) {
        return String.valueOf(Character.toChars(hexEmoji));
    }

}