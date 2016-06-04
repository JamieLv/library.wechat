package grad.main;

import grad.database.Book_State;
import grad.database.Database;
import grad.database.Subscriber_Info;
import grad.message.resp.Article;
import grad.servlet.BaiduMapAPI;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static grad.service.CoreService.getGreeting;
import static grad.tools.RetrieveDocumentByURL.Return_BookPicURL;

/**
 * Created by Jamie on 5/8/16.
 */
public class ArticleManager {

    // 问候
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

    // 搜索书本
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

    // 加书
    public static List<Article> AddBookDisplay(Book_State new_book_state, int new_Book_ID) throws IOException {

        List<Article> articleList = new ArrayList<>();

        Article articleBOOK = new Article();
        articleBOOK.setTitle("书本编号: " + new_Book_ID + " 书名: 《" + new_book_state.getBook_Title() + "》");
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

    // 删书
    public static List<Article> DelBookDisplay(Book_State new_book_state, int del_book_id) throws IOException {

        List<Article> articleList = new ArrayList<>();

        Article articleBOOK = new Article();
        articleBOOK.setTitle("待删书本编号: " + del_book_id + " 书名: 《" + new_book_state.getBook_Title() + "》");
        articleBOOK.setPicUrl(Return_BookPicURL(new_book_state.getBook_ISBN()));
        articleBOOK.setUrl("https://www.baidu.com/s?ie=UTF-8&wd=" + new_book_state.getBook_Title());
        articleList.add(articleBOOK);

        Article articleISBN = new Article();
        articleISBN.setTitle("ISBN: " + new_book_state.getBook_ISBN());
        articleList.add(articleISBN);

        Article articleBOOKSTATEMENT = new Article();
        articleBOOKSTATEMENT.setTitle("存书状态: " + Database.getLibrarInfo(new_book_state.getBook_inLibrary_id()).getLibrary_Name());
        articleList.add(articleBOOKSTATEMENT);

        Article articleINSTRUCTION = new Article();
        articleINSTRUCTION.setTitle("是否确认删除：是Y，否N。");
        articleList.add(articleINSTRUCTION);

        return articleList;
    }

    // 读者证
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

    // 附近的图书馆
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

}
