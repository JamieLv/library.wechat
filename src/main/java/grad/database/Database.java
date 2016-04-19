package grad.database;

import grad.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jamie on 4/15/16.
 */
public class Database {


    /*
    *
    * 判断是否存在
     */

    // 判断会员是否存在
    public static int MemberExist(String fromUserName){
        int exist = 0; // 没有存在在名单中

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Member");
        List<Member> member_list = query.list();
        session.getTransaction().commit();

        for(Member m :member_list) {
            if(m.getFromUserName().equals(fromUserName) && m.getMember_Verification()){
                exist = 2; // 存在于名单中且通过验证
            } else if(m.getFromUserName().equals(fromUserName)) {
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

//
//    public static boolean AddBorrow_Display(Borrow_Display borrow_display){
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        session.beginTransaction();
//        session.save(borrow_display);
//        session.getTransaction().commit();
//
//        return true;
//    }

//
//    // 增加新书
//    public static boolean AddBook(Book book){
//
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        session.beginTransaction();
//        session.save(book);
//        session.getTransaction().commit();
//
//        return true;
//    }
//
//    // 增加新书状态
//    public static boolean AddBook_Statement(Book_State book_state){
//
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        session.beginTransaction();
//        session.save(book_state);
//        session.getTransaction().commit();
//
//        return true;
//    }

    /*
     *
     * 改
     */
    public static boolean UpdateMobile(String fromUserName, String Mobile){
        int Member_id = getMember(fromUserName).getMember_id();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Member member = (Member)session.get(Member.class, Member_id);
        member.setMobile(Mobile);
        session.getTransaction().commit();

        return true;
    }

    public static boolean UpdateMember_Verification(String fromUserName, boolean verify){
        int Member_id = getMember(fromUserName).getMember_id();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Member member = (Member)session.get(Member.class, Member_id);
        member.setMember_Verification(verify);
        session.getTransaction().commit();

        return true;
    }

    public static boolean UpdateMember_Record(int Borrow_Book_id, String request){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Member_Record member_record = (Member_Record)session.get(Member_Record.class, Borrow_Book_id);
        if (request.contentEquals("Return_Book")){
            member_record.setBorrow_Statement(0);
        } else if (request.startsWith("Book_Library_Info")) {
            member_record.setBorrow_Statement(2);
        }
        session.getTransaction().commit();

        return true;
    }

    public static boolean UpdateMember_Record_Return_Time(int Borrow_Book_id) throws ParseException {

        String Return_Time = getMember_Record(Borrow_Book_id).getReturn_Time();

        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
        Date date = SDF.parse(Return_Time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 14);

        String New_Return_Time = SDF.format(calendar.getTime());

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Member_Record member_record = (Member_Record)session.get(Member_Record.class, Borrow_Book_id);
        member_record.setReturn_Time(New_Return_Time);

        return true;
    }


    /*
     *
     * 查
     */

    // 查书本
    public static Book getBookbyTitle(String title){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Book where Title = '%s'", title));
        Book book = null;
        if (query.list().size() > 0) {
            book = (Book) query.list().get(0);
        }
        session.getTransaction().commit();

        return book;
    }

    public static Book getBookbyISBN(String ISBN){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Book where ISBN = '%s'", ISBN));
        Book book = null;
        if (query.list().size() > 0) {
            book = (Book) query.list().get(0);
        }
        session.getTransaction().commit();

        return book;
    }

    public static Book getBookbyBook_id(int Book_id){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Book where Book_id = '%s'", Book_id));
        Book book = null;
        if (query.list().size() > 0) {
            book = (Book) query.list().get(0);
        }
        session.getTransaction().commit();

        return book;
    }

    public static Book_State getBook_StatebyISBN(String ISBN){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Book_State where ISBN = '%s'", ISBN));
        Book_State book_state = null;
        if (query.list().size() > 0) {
            book_state = (Book_State) query.list().get(0);
        }
        session.getTransaction().commit();

        return book_state;
    }

    // 查会员
    public static Member getMember(String fromUserName){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Member where fromUserName = '%s'", fromUserName));
        Member member = null;
        if (query.list().size() > 0) {
            member = (Member) query.list().get(0);
        }
        session.getTransaction().commit();

        return member;
    }

    // 查会员ID
//    public static int getMember_id(String fromUserName){
//        int Member_id = 0;
//
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        session.beginTransaction();
//        Query query = session.createQuery("from Member");
//        List<Member> member_list = query.list();
//        session.getTransaction().commit();
//
//        for(Member m: member_list) {
//            if(m.getFromUserName().equals(fromUserName)){
//                Member_id = m.getMember_id();
//                break;
//            }
//        }
//        return  Member_id;
//    }

    // 查图书馆名字
    public static String getLibrary_Name(int Library_id){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Library where Library_id = '%s'", Library_id));
        Library library  = null;
        if (query.list().size() > 0) {
            library = (Library) query.list().get(0);
        }
        session.getTransaction().commit();

        return library.getLibrary_Name();
    }

    // 查借书状态/记录
    public static Member_Record getMember_Record(int Book_id){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Member_Record where Book_id = '%s'", Book_id));
        Member_Record member_record = null;
        if (query.list().size() > 0) {
            member_record = (Member_Record) query.list().get(0);
        }
        session.getTransaction().commit();

        return member_record;
    }

    public static Member_Record getMember_RecordbyUser(int Book_id, String fromUserName){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Member_Record where Book_id = '%s' and Borrower = '%s'", Book_id, fromUserName));
        Member_Record member_record = null;
        if (query.list().size() > 0) {
            member_record = (Member_Record) query.list().get(0);
        }
        session.getTransaction().commit();

        return member_record;
    }

}
