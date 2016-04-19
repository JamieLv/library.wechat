package grad.service;

import grad.database.*;
import grad.message.resp.Article;
import grad.message.resp.NewsMessage;
import grad.message.resp.TextMessage;
import grad.pojo.CommonButton;
import grad.tools.*;
import grad.util.MessageUtil;


import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
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
        Book book = Database.getBookbyTitle(Search_Book_Title);
//        Book_State book_state = Database.getBook_StatebyISBN(book.getISBN());

        Article articleBOOK = new Article();
        articleBOOK.setTitle("书名: 《" + book.getTitle() + "》");
        articleBOOK.setPicUrl(Return_BookPicURL(book.getISBN()));
        articleBOOK.setUrl(Return_BookPicURL(book.getISBN()));
        articleList.add(articleBOOK);

        Article articleISBN = new Article();
        articleISBN.setTitle("ISBN: " + book.getISBN());
        articleList.add(articleISBN);

        Article articleCATALOG = new Article();
        articleCATALOG.setTitle("类别: " + book.getCatalog());
        articleList.add(articleCATALOG);

        Article articleAUTHOR = new Article();
        articleAUTHOR.setTitle("作者: " + book.getAuthor());
        articleList.add(articleAUTHOR);

        if (book.getTranslator() != null) {
            Article articleTRANSLATOR = new Article();
            articleTRANSLATOR.setTitle("译者: " + book.getTranslator());
            articleList.add(articleTRANSLATOR);
        }

        Article articlePUBLISHER = new Article();
        articlePUBLISHER.setTitle("出版商: " + book.getPublisher());
        articleList.add(articlePUBLISHER);

        Article articleISSUETIME = new Article();
        articleISSUETIME.setTitle("发行时间: " + book.getIssueTime());
        articleList.add(articleISSUETIME);

//        Article articlePRICE = new Article();
//        articlePRICE.setTitle("价格: " + book.getPrice());
//        articleList.add(articlePRICE);

//        Article articleBOOKSTATEMENT = new Article();
//        articleBOOKSTATEMENT.setTitle("存书状态: " + book_state.getLibrary_Name());
//        articleList.add(articleBOOKSTATEMENT);

            return articleList;
    }

    public static String getDate(int addDays) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.add(Calendar.DATE, addDays);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat SDF = new SimpleDateFormat(pattern);
        String str_calendar = SDF.format(calendar.getTime());
        return str_calendar;
    }


    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    public static String processRequest(HttpServletRequest request) {
        String respMessage = null;
        String currentTime;
        //获得当前时间
        //格式如下： 20160411-11:12:43
        Calendar calendar = Calendar.getInstance(); //获取当天日期
        SimpleDateFormat sdf = new SimpleDateFormat("20yyMMdd-HH:mm:ss");
        currentTime = sdf.format(calendar.getTime());

        try {
            // 默认返回的文本消息内容
            String respContent = "请求处理异常，请稍候尝试！";

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
            // 日期
//            Date now = new Date();
//            String pattern = "yyyy-MM-dd";
//            SimpleDateFormat SDF = new SimpleDateFormat(pattern);
//            String CurrentDate = SDF.format(now);

            // 默认回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);

            //创建图文信息
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setFuncFlag(0);


            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                //拿到用户发来的信息 去除用户回复信息的前后空格
                String content = requestMap.get("Content").trim();

                List<Article> articleList;

                if (content.startsWith("Member")) { // Member 吕嘉铭 男 22 13611774556
                    String[] keywords = content.trim().split(" ");
                    try{

                        int tag = Database.MemberExist(fromUserName);

                        if (tag == 0){  // 第一次注册
                            Member member = new Member(
                                    keywords[1], keywords[2], Integer.parseInt(keywords[3]), keywords[4], getDate(0), fromUserName, false);
                            Database.Add(member);

                            int yzm = Database.getMember(fromUserName).getMember_id();
                            SendMsg_webchinese sendMsg = new SendMsg_webchinese();
                            sendMsg.send(keywords[4], yzm);
                            respContent = "尊敬的读者，请输入您收到的短信验证码，仿照格式: yzm 1";
                        } else if (tag == 1){ // 用户已登记，手机验证未通过
                            Database.UpdateMobile(fromUserName, keywords[4]);
                            int yzm = Database.getMember(fromUserName).getMember_id();
                            SendMsg_webchinese sendMsg = new SendMsg_webchinese();
                            sendMsg.send(keywords[4], yzm);
                            respContent = "尊敬的读者，请输入您收到的短信验证码，仿照格式: yzm 1";
                        } else if (tag == 2){ // 已登记，手机验证通过
                            respContent = "尊敬的读者，您已完成注册，请直接点击菜单中的\"会员卡\"进行查询，谢谢！";
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
                            if (i_yzm == Database.getMember(fromUserName).getMember_id()){
                                Database.UpdateMember_Verification(fromUserName, true);
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

                else if (content.startsWith("Search") || content.startsWith("search")){ // 检索书本
                    String[] keywords = content.trim().split("\\s+");
                    Book book = Database.getBookbyTitle(keywords[1]);
                    if (book == null){
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

                        if (Database.getBookbyISBN(ADD_ISBN) == null) {

                            DouBanBook new_book = Return_BookInfo(ADD_ISBN);

                            // String ISBN, String Title, String Catalog, String Author, String Translator, String Publisher, String IssueTime, String Price
                            Book book = new Book(
                                    ADD_ISBN, new_book.getTitle(), new_book.getTags(), new_book.getAuthor(), new_book.getTranslator(),
                                    new_book.getPublisher(), new_book.getPubdate(), new_book.getPrice());
                            Database.Add(book);
                            // int Book_id, String ISBN, String Title, int Library_id, String Library_Name, String Statement, String Borrower
                            Book_State book_state = new Book_State(book.getBook_id(), ADD_ISBN, book.getTitle(), Library_id, Database.getLibrary_Name(Library_id), "归还", null);
                            Database.Add(book_state);

                            respContent = "添加成功" + book.getBook_id() + book.getTitle() + book.getAuthor() + book_state.getLibrary_Name();
                        } else {
                            respContent = "此书已录入";
                        }
                    }
                    else {
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
                respContent = "你发的是地理位置哦！";


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
                            = getGreeting() + "，尊敬的读者:)\n"
                            + "感谢关注图书馆！\n"
                            + "赶紧戳一戳下方按钮，来和我们互动吧！";

                } // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {

                } else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) { // 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    String eventKey = requestMap.get("EventKey");

                    if (eventKey.equals(CommonButton.KEY_MEMBERSHIP)) {

                        Member member = Database.getMember(fromUserName);

                        if(member == null){ // 用户尚未登记
                            respContent = "请输入\"Member 姓名 性别 年龄 手机号\"注册";

                        } else if(member.getMember_Verification() == false){ // 用户已登记，手机验证未通过
                            respContent = "尊敬的读者，请输入您收到的短信验证码，仿照格式: yzm 1\n"
                                    + "或者再次按照以下格式进行回复： \n"
                                    + "Member 姓名 性别 年龄 手机号\n"
                                    + "60秒内将会收到有验证码的短信。\n"
                                    + "到时请将验证码回复给微信平台，谢谢配合。";
                        } else { // 成功
                            MemberService.MemberTemplate(member);
                            return "";
                        }

                    } else if (eventKey.equals(CommonButton.KEY_RETURN_BOOK)) {
                        respContent = "13！";
                    } else if (eventKey.equals(CommonButton.KEY_HELP)) {
                        respContent = "14！";
                    } else if (eventKey.equals(CommonButton.KEY_BOOK)) {
                        respContent = "回复\"Search 书名\"查询您想要的书本!";
                    } else if (eventKey.equals(CommonButton.KEY_RESERVE_ROOM)) {
                        respContent = "22！";
                    } else if (eventKey.equals(CommonButton.KEY_BOOK_RECOMMEND)) {
                        respContent = "23！";
                    } else if (eventKey.equals(CommonButton.KEY_NEARBY)) {
                        respContent = "24！";
                    } else if (eventKey.equals(CommonButton.KEY_ADVICE)) {
                        respContent = "31！";
                    } else if (eventKey.equals(CommonButton.KEY_ADVICE_TRACK)) {
                        respContent = "32！";
                    } else if (eventKey.equals(CommonButton.KEY_JOIN_US)) {
                        respContent = "34！";
                    }

                } else if (eventType.equals(MessageUtil.EVENT_TYPE_SCANCODE_WAITMSG)) {
                    String scanResult = requestMap.get("ScanResult");
                    try{
                        if (scanResult.startsWith("Book_Library_Info")){ // Book_Library_Info 5 剪刀石头布 1 艾尔法图书馆
                            String[] Book_Library_Info = scanResult.trim().split(" ");
                            int Borrow_Book_id = Integer.parseInt(Book_Library_Info[1]);
//                        int Borrow_Book_Library_id = Integer.parseInt(Book_Library_Info[3]);
//                        Member_Record(int Member_id, int Book_id, String Borrow_Catalog, String Borrow_Time, String Return_Time, int Borrow_Statement, String Borrower)
                            if (Database.getMember_Record(Borrow_Book_id, fromUserName) == null
                                    || Database.getMember_Record(Borrow_Book_id, fromUserName).getBorrow_Statement() == 0) { // 这个用户没有借过这本书, 或者已经归还
                                Member_Record new_borrow_record = new Member_Record(
                                        Database.getMember(fromUserName).getMember_id(), Borrow_Book_id, Database.getBookbyBook_id(Borrow_Book_id).getCatalog(),
                                        getDate(0), getDate(14), 1, fromUserName);
                                Database.Add(new_borrow_record);
                                respContent = Database.getBookbyBook_id(Borrow_Book_id).getTitle() + "可从" + getDate(0) + "借至" + getDate(14);
                            } else if (Database.getMember_Record(Borrow_Book_id, fromUserName).getBorrow_Statement() == 1){ // 这个用户借了这本书, 但是没有续借过
                                Database.UpdateMember_Record(fromUserName, scanResult);
                                Database.getMember_Record(Borrow_Book_id, fromUserName).setReturn_Time(getDate(7));
                                respContent = Database.getBookbyBook_id(Borrow_Book_id).getTitle() + "可从" + Database.getMember_Record(Borrow_Book_id, fromUserName).getBorrow_Time() + "借至" + getDate(7);
                            } else if (Database.getMember_Record(Borrow_Book_id, fromUserName).getBorrow_Statement() == 2){ // 这个用户借了这本书, 且续借过
                                respContent = "您已续借过，请于" + Database.getMember_Record(Borrow_Book_id, fromUserName).getReturn_Time() + "还书，谢谢";
                            }

                        } else if (scanResult.contentEquals("Return_Book")){ // 还书
                            Database.UpdateMember_Record(fromUserName, scanResult);
                        }
                        else {
                            respContent = getGreeting() + scanResult;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
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
