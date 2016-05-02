package grad.service;

import grad.database.*;
import grad.main.TagManager;
import grad.message.resp.Article;
import grad.message.resp.NewsMessage;
import grad.message.resp.TextMessage;
import grad.pojo.CommonButton;
import grad.servlet.BaiduMapAPI;
import grad.tools.*;
import grad.util.MessageUtil;
import grad.util.WeixinUtil;
import net.sf.json.JSONObject;


import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.soap.Addressing;
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

    public static List<Article> SearchBookDisplay(String Search_Book_Title) throws IOException {

        List<Article> articleList = new ArrayList<>();
        Book_State book_state = Database.getBook_StatebyTitle(Search_Book_Title);
        List<Book_State> book_stateList = Database.getBook_StateListbyTitle(Search_Book_Title);
        String BookinLib = "";

        for (Book_State book_stateLib: book_stateList) {
            if (book_stateLib.getBook_Statement_ID() == 0){
                BookinLib += BookinLib.equals("") ? Database.getLibraryName(book_stateLib.getBook_inLibrary_id()) : "；" + Database.getLibraryName(book_stateLib.getBook_inLibrary_id());
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

    public static List<Article> MemberRecordDisplay(String fromUserName) throws IOException {
        List<Article> articleList = new ArrayList<>();
//        int Record_ID = Database.getBorrow_Record(Borrow_Book_Index, Database.getMember_Info(fromUserName).getMember_ID()).getRecord_ID();
//        Borrow_Record borrow_record = Database.getBorrow_Record()_Record(5);

        Article articleBorrowRecord = new Article();
        articleBorrowRecord.setTitle("借阅记录");
        articleList.add(articleBorrowRecord);
        int Record_ID = 1;
        Borrow_Record borrow_record = Database.getBorrow_RecordbyRecord_ID(Record_ID);
        Member_Info member_info = Database.getMember_Info(fromUserName);
        for (; borrow_record != null; Record_ID++, borrow_record = Database.getBorrow_RecordbyRecord_ID(Record_ID)){
            if (borrow_record.getBorrow_Member_ID() == member_info.getMember_ID()){
                int Borrow_Book_ID = borrow_record.getBorrow_Book_ID();
                Book_State book_state = Database.getBook_StatebyBook_id(Borrow_Book_ID);
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
            articleLibraryInfo.setTitle("名字: " + LibraryInfo.get("name") +
                    "\n地址: " + LibraryInfo.get("address"));
            if (LibraryInfo.get("telephone") != null){
                articleLibraryInfo.setTitle(articleLibraryInfo.getTitle() +
                    "\n电话: " + LibraryInfo.get("telephone"));
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
        String currentTime;
        //获得当前时间
        //格式如下： 20160411-11:12:43
        Calendar calendar = Calendar.getInstance(); //获取当天日期
        SimpleDateFormat sdf = new SimpleDateFormat("20yyMMdd-HH:mm:ss");
        currentTime = sdf.format(calendar.getTime());

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


            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                //拿到用户发来的信息 去除用户回复信息的前后空格
                String content = requestMap.get("Content").trim();

                if (content.startsWith("Member")) { // Member 吕嘉铭 男 22 13611774556
                    String[] keywords = content.trim().split(" ");
                    try{
                        int tag = Database.MemberExist(fromUserName);

                        if (tag == 0){  // 第一次注册
                            // String Member_Name, String Member_Gender, int Member_Age, String Member_Mobile, String Member_RegisterTime, String Member_fromUserName, Boolean Member_Verification
                            Member_Info member_info = new Member_Info(
                                    keywords[1], keywords[2], Integer.parseInt(keywords[3]), keywords[4], db.getDate(0), fromUserName, false);
                            Database.Add(member_info);

                            int yzm = Database.getMember_Info(fromUserName).getMember_ID();
                            SendMsg_webchinese sendMsg = new SendMsg_webchinese();
                            sendMsg.send(keywords[4], yzm);
                            respContent = "尊敬的读者，请输入您收到的短信验证码，仿照格式: yzm 1";
                        } else if (tag == 1){ // 用户已登记，手机验证未通过
                            Database.UpdateMobile(fromUserName, keywords[4]);
                            int yzm = Database.getMember_Info(fromUserName).getMember_ID();
                            SendMsg_webchinese sendMsg = new SendMsg_webchinese();
                            sendMsg.send(keywords[4], yzm);
                            respContent = "尊敬的读者，请输入您收到的短信验证码，仿照格式: yzm 1";
                        } else if (tag == 2){ // 已登记，手机验证通过
                            if (db.getWoker_Info(fromUserName) != null){ TagManager.batchtagging(fromUserName, "Member"); respContent = "读者身份登录";}
                            else {respContent = "尊敬的读者，您已完成注册，请直接点击菜单中的\"读者证\"或\"Member\"进行查询，谢谢！";}
                        }
                    }catch (NumberFormatException e){ // Member开头但格式有误
                        respContent = "您输入的信息有误，请核对后重新输入！仿照格式: Member 张三 男 20 13112345678";
                    }
                }

                else if (content.startsWith("yzm")){ // yzm 1
                    String[] keywords = content.trim().split(" ");

                    if (keywords.length == 2){
                        String str_yzm = keywords[1];
                        int i_yzm = Integer.parseInt(str_yzm); // 获取验证码并转换成原型
                        int tag = Database.MemberExist(fromUserName);

                        if (tag == 0){
                            respContent = "尊敬的读者，您还没有输入您的基本信息吧！\n"
                                    + "请严格按照这个格式进行回复： \n"
                                    + "Member 姓名 性别 年龄 手机号\n"
                                    + "60秒内将会收到有验证码的短信。\n"
                                    + "到时请将验证码回复给微信平台，谢谢配合。";
                        } else if (tag == 1){
                            if (i_yzm == Database.getMember_Info(fromUserName).getMember_ID()){
                                Database.UpdateMember_Verification(fromUserName, true);
                                // 用户分组
                                TagManager.batchtagging(fromUserName, "Member");
                                respContent = "恭喜你验证成功！可以点击菜单中的【会员卡】了解个人动态!";
                            } else {
                                respContent = "尊敬的读者，验证码输入有误，请仔细核对！\n"
                                        + "或者再次按照以下格式进行回复： \n"
                                        + "Member 姓名 性别 年龄 手机号\n"
                                        + "60秒内将会收到有验证码的短信。\n"
                                        + "到时请将验证码回复给微信平台，谢谢配合。";
                            }
                        }
                        else { // 验证已通过
                            respContent = "尊敬的读者，您已完成注册，请直接点击菜单中的\"会员卡\"进行查询，谢谢！";
                        }
                    }
                    else{
                        respContent =  "验证码格式错误，请仿照格式: yzm 1回复，谢谢配合。";
                    }
                }

                else if (content.startsWith("Worker")){ // Worker 1 吕嘉铭
                    String[] keywords = content.trim().split(" ");
                    int tag = db.WorkerExist(keywords[1], keywords[2], fromUserName);

                    if (tag == 0) {
                        respContent = "您的员工号不符，请重新核实，谢谢配合。";
                    } else if (tag == 1) {
                        db.UpdateWorker_Verification(keywords[2], fromUserName);
                        TagManager.batchtagging(fromUserName, keywords[0]);
                        respContent = "成功与员工账号绑定，现能使用工作人员功能。";
                    } else if (tag == 2) {
                        respContent = "您的员工信息已录入，谢谢配合。";
                    }
                }

                else if (content.startsWith("Search") || content.startsWith("search")){ // 检索书本
                    String[] keywords = content.trim().split("\\s+");
                    Book_State book_state = Database.getBook_StatebyTitle(keywords[1]);
                    if (book_state == null){
                        respContent = "此书尚未录入";
                    } else {
                        articleList = SearchBookDisplay(keywords[1]);

                        // 设置图文消息个数
                        newsMessage.setArticleCount(articleList.size());
                        // 设置图文消息包含的图文集合
                        newsMessage.setArticles(articleList);
                        // 将图文消息对象转换成xml字符串
                        respMessage = MessageUtil.newsMessageToXml(newsMessage);

                        return respMessage;
                    }
                } else if (content.startsWith("Add") || content.startsWith("add")) {
                     // add 9781278973602 2
                    String[] keywords = content.trim().split(" ");
                    if (keywords.length == 3) {
                        String ADD_ISBN = keywords[1];
                        int Library_id = Integer.parseInt(keywords[2]);

                        // if (Database.getBookbyISBN(ADD_ISBN) == null) {
                        DouBanBook new_book = Return_BookInfo(ADD_ISBN);
                        //String Book_ISBN, String Book_Title, String Book_Category, String Book_Author,
                        //String Book_Publisher, String Book_PubTime, String Book_Price, int Book_inLibrary_id, String Book_Statement
                        Book_State book_state = new Book_State(
                                ADD_ISBN, new_book.getTitle(), new_book.getTags(), new_book.getAuthor(),
                                new_book.getPublisher(), new_book.getPubdate(), new_book.getPrice(), Library_id, "归还");
                        Database.Add(book_state);
                        articleList = SearchBookDisplay(new_book.getTitle());

                        // 设置图文消息个数
                        newsMessage.setArticleCount(articleList.size());
                        // 设置图文消息包含的图文集合
                        newsMessage.setArticles(articleList);
                        // 将图文消息对象转换成xml字符串
                        respMessage = MessageUtil.newsMessageToXml(newsMessage);

                        return respMessage;
                    } else {
                        respContent = "输入格式有误";
                    }
                }

                else {
                    respContent = getGreeting() + "，尊敬的读者" + emoji(0x1F604)
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

            } // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    respContent
                            = getGreeting() + "，尊敬的读者" + emoji(0x1F604) + "\n"
                            + "感谢关注图书馆！\n"
                            + "赶紧戳一戳下方按钮，来和我们互动吧！";

                } // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {

                } else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) { // 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    String eventKey = requestMap.get("EventKey");

                    if (eventKey.equals(CommonButton.KEY_LOGIN)) {

                        Member_Info member_info = Database.getMember_Info(fromUserName);
                        Worker_Info worker_info = Database.getWoker_InfobyfromUserName(fromUserName);

                        if(member_info == null){ // 用户尚未登记
                            respContent = "请输入\"Member 姓名 性别 年龄 手机号\"注册";

                        } else if(member_info.getMember_Verification() == false){ // 用户已登记，手机验证未通过
                            respContent = "尊敬的读者，请输入您收到的短信验证码，仿照格式: yzm 1\n"
                                    + "或者再次按照以下格式进行回复： \n"
                                    + "Member 姓名 性别 年龄 手机号\n"
                                    + "60秒内将会收到有验证码的短信。\n"
                                    + "到时请将验证码回复给微信平台，谢谢配合。";
                        } else if(worker_info != null){ //登录
                            TagManager.batchtagging(fromUserName, "Worker");
                            respContent = "员工" + worker_info.getWorker_ID() + "登录成功";
                        }
                        else { // 成功
                            TagManager.batchtagging(fromUserName, "Member");
                            MemberService.MemberTemplate(member_info);
                            return "";
                        }

                    } else if (eventKey.equals(CommonButton.KEY_MEMBERSHIP)) {
                        Member_Info member_info = Database.getMember_Info(fromUserName);
                        MemberService.MemberTemplate(member_info);
                        return "";
                    } else if (eventKey.equals(CommonButton.KEY_RECORD)) {
                        articleList = MemberRecordDisplay(fromUserName);

                        // 设置图文消息个数
                        newsMessage.setArticleCount(articleList.size());
                        // 设置图文消息包含的图文集合
                        newsMessage.setArticles(articleList);
                        // 将图文消息对象转换成xml字符串
                        respMessage = MessageUtil.newsMessageToXml(newsMessage);

                        return respMessage;

                    } else if (eventKey.equals(CommonButton.KEY_BORROW_BOOK)) {
                        String scanResult = requestMap.get("ScanResult");
                        int Book_Borrower_ID = db.getMember_Info(fromUserName).getMember_ID();
                        /*
                         * 借书
                         */
                        if (scanResult.startsWith("Book_Info")){ // Book_Info 5 剪刀石头布 1 艾尔法图书馆
                            String[] Book_State_Info = scanResult.trim().split(" ");
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

                                if (Return_cal.compareTo(calendar) != 1) {
                                    db.UpdateBook_State(Borrow_Book_ID, Book_Borrower_ID);
                                    BorrowService.BorrowTemplate(Borrow_Book_ID, fromUserName);
                                    return "";
                                } else {respContent = "《" + book_state.getBook_Title() + "》应于" + Return_Time + "归还，逾期不可续借";}
                            }

                        } else {respContent = "非馆藏书本";}
                    } else if (eventKey.equals(CommonButton.KEY_RETURN_BOOK)) {
                        String scanResult = requestMap.get("ScanResult");
                        String[] Book_State_Info = scanResult.trim().split(" ");
                        int Borrow_Book_ID = Integer.parseInt(Book_State_Info[1]);
                        if (db.getWoker_Info(fromUserName).getWorker_ID() != db.getBook_StatebyBook_id(Borrow_Book_ID).getBook_Borrower_ID()) {
                            db.ReturnBook(Borrow_Book_ID);
                            respContent = "归还成功";
                        } else {respContent = "归还失败";}
                    } else if (eventKey.equals(CommonButton.KEY_LOG_OFF)) {
                        TagManager.batchuntagging(fromUserName, "Member");
                        respContent = "退出成功";
                    } else if (eventKey.equals(CommonButton.KEY_WORK_OFF)) {
                        TagManager.batchuntagging(fromUserName, "Worker");
                        respContent = "退出成功";
                    } else if (eventKey.equals(CommonButton.KEY_BOOK)) {
                        respContent = "回复\"Search 书名\"查询您想要的书本!";
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
                    String scanResult = requestMap.get("ScanResult");
                    System.out.println("二维码信息: "+scanResult);
                    respContent = getGreeting();
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
