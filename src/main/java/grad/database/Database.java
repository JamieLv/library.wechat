package grad.database;

import grad.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import java.lang.reflect.Member;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jamie on 4/15/16.
 */
public class Database {

    public static String getDate(int addDays) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.add(Calendar.DATE, addDays);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat SDF = new SimpleDateFormat(pattern);
        String str_calendar = SDF.format(calendar.getTime());
        return str_calendar;
    }

    /*
    *
    * 判断是否存在
     */

    // 判断会员是否存在
    public static int MemberExist(String Member_fromUserName){
        int exist = 0; // 没有存在在名单中

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Member_Info");
        List<Member_Info> member_info_list = query.list();
        session.getTransaction().commit();

        for(Member_Info member_info :member_info_list) {
            if(member_info.getMember_fromUserName().equals(Member_fromUserName) && member_info.getMember_Verification()){
                exist = 2; // 存在于名单中且通过验证
            } else if(member_info.getMember_fromUserName().equals(Member_fromUserName)) {
                exist = 1; // 存在于名单中
            }
        }
        return  exist;
    }


    /*
    *
    * 增
     */

    // 增加一条数据
    public static boolean Add(Object obj){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(obj);
        session.getTransaction().commit();

        return true;
    }


    /*
     *
     * 改
     */

    // 更新手机号
    public static boolean UpdateMobile(String Member_fromUserName, String Member_Mobile){
        int Member_ID = getMember_Info(Member_fromUserName).getMember_ID();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Member_Info member_info = (Member_Info) session.get(Member_Info.class, Member_ID);
        member_info.setMember_Mobile(Member_Mobile);
        session.getTransaction().commit();

        return true;
    }

    // 更新用户验证状态
    public static boolean UpdateMember_Verification(String Member_fromUserName, boolean verify){
        int Member_ID = getMember_Info(Member_fromUserName).getMember_ID();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Member_Info member_info = session.get(Member_Info.class, Member_ID);
        member_info.setMember_Verification(verify);
        session.getTransaction().commit();

        return true;
    }

    // 更新书本借阅情况
    public static boolean UpdateBook_State(int Book_ID, String request, String fromUserName) throws ParseException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Book_State book_state = session.get(Book_State.class, Book_ID);
        if (request.contentEquals("Return_Book")){
            book_state.setBook_Statement("归还");
        } else if (request.startsWith("Book_Info")) {
            if (book_state.getBook_Statement().equals("归还")) { // 书未被借, 用户发出借书请求
                book_state.setBook_Borrow_Time(getDate(0));
                book_state.setBook_Return_Time(getDate(30));
                book_state.setBook_Statement("于" + getDate(30) + "归还");
                book_state.setBook_Statement_ID(1);
            } else if (book_state.getBook_Borrower_ID().equals(fromUserName)) { // 用户已经借过书
                String Return_Time = book_state.getBook_Return_Time();
                SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
                Date date = SDF.parse(Return_Time);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, 14);
                String New_Return_Time = SDF.format(calendar.getTime());
                book_state.setBook_Statement("于" + New_Return_Time + "归还");
                book_state.setBook_Statement_ID(book_state.getBook_Statement_ID()+1);
            }
        }
        session.getTransaction().commit();
        return true;
    }

    /*
     *
     * 查
     */

    // 查书本

    public static Book_State getBook_StatebyBook_id(int Book_id){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Book_State where Book_id = '%s'", Book_id));
        Book_State book_state = null;
        if (query.list().size() > 0) {
            book_state = (Book_State) query.list().get(0);
        }
        session.getTransaction().commit();

        return book_state;
    }

    public static Book_State getBook_StatebyBook_ISBN(int Book_ISBN){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Book_State where Book_ISBN = '%s'", Book_ISBN));
        Book_State book_state = null;
        if (query.list().size() > 0) {
            book_state = (Book_State) query.list().get(0);
        }
        session.getTransaction().commit();

        return book_state;
    }

    public static Book_State getBook_StatebyTitle(String title){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Book_State where Book_Title = '%s'", title));
        Book_State book_state = null;
        if (query.list().size() > 0) {
            book_state = (Book_State) query.list().get(0);
        }
        session.getTransaction().commit();

        return book_state;
    }



    // 查会员
    public static Member_Info getMember_Info(String Member_fromUserName){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Member_Info where Member_fromUserName = '%s'", Member_fromUserName));
        Member_Info member_info = null;
        if (query.list().size() > 0) {
            member_info = (Member_Info) query.list().get(0);
        }
        session.getTransaction().commit();

        return member_info;
    }


    // 查借书状态/记录
    public static Borrow_Record getBorrow_Record(int Borrow_Book_ID, int Borrow_Member_ID){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Borrow_Record where Borrow_Book_ID = '%s' and Borrow_Member_ID", Borrow_Book_ID, Borrow_Member_ID));
        Borrow_Record borrow_record = null;
        if (query.list().size() > 0) {
            borrow_record = (Borrow_Record) query.list().get(0);
        }
        session.getTransaction().commit();

        return borrow_record;
    }

//    public static List<Borrow_Record> getBorrow_RecordbyUser(String fromUserName){
//        int Borrow_Member_ID = Integer.parseInt(fromUserName);
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        session.beginTransaction();
//        Query query = session.createQuery(String.format("from Borrow_Record where Borrow_Member_ID", Borrow_Member_ID));
//        List<Borrow_Record> borrow_record_list = query.list();
//        session.getTransaction().commit();
//
//        return borrow_record_list;
//    }

    public static Borrow_Record getBorrow_RecordbyUser(String fromUserName){
        int Borrow_Member_ID = Integer.parseInt(fromUserName);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Borrow_Record where Borrow_Member_ID", Borrow_Member_ID));
        Borrow_Record borrow_record = null;
        if (query.list().size() > 0) {
            borrow_record = (Borrow_Record) query.list().get(0);
        }
        session.getTransaction().commit();

        return borrow_record;
    }
}
