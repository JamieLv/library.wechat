package grad.database;

import com.sun.org.apache.regexp.internal.RE;
import grad.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import java.awt.print.Book;
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
        List<Member_Info> member_infoList = query.list();
        session.getTransaction().commit();

        for(Member_Info member_info: member_infoList) {
            if(member_info.getMember_fromUserName().equals(Member_fromUserName) && member_info.getMember_Verification()){
                exist = 2; // 存在于名单中且通过验证
            } else if(member_info.getMember_fromUserName().equals(Member_fromUserName)) {
                exist = 1; // 存在于名单中
            }
        }
        return  exist;
    }

    // 判断工作人员是否存在
    public static int WorkerExist(String Worker_ID, String Worker_Name, String Worker_fromUserName){
        int exist = 0; // 没有存在在名单中
        int ID = Integer.parseInt(Worker_ID);

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Worker_Info");
        List<Worker_Info> worker_infoList = query.list();
        session.getTransaction().commit();

        for(Worker_Info worker_info: worker_infoList) {
            if(worker_info.getWorker_fromUserName().equals(Worker_fromUserName) && worker_info.getWorker_Verification()){
                exist = 2;
            } else if(getWoker_Info(Worker_Name).getWorker_ID() == ID){
                exist = 1;
            }
        }
        return  exist;
    }

    // 判断用户是否借阅过该书
    public static boolean Borrow_RecordExist(int Borrow_Book_ID, int Book_Borrower_ID){
        boolean exist = false;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Borrow_Record");
        List<Borrow_Record> borrow_recordList = query.list();
        session.getTransaction().commit();

        for(Borrow_Record borrow_record: borrow_recordList){
            if(borrow_record.getBorrow_Book_ID() == Borrow_Book_ID && borrow_record.getBorrow_Member_ID() == Book_Borrower_ID){
                exist = true;
                break;
            }
        }
        return exist;
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

    // 更改用户使用状态
    public static boolean UpdateMember_Function(String Member_fromUserName, String Member_Function){
        int Member_ID = getMember_Info(Member_fromUserName).getMember_ID();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Member_Info member_info = session.get(Member_Info.class, Member_ID);
        if (member_info.getMember_Function() != Member_Function) {
            member_info.setMember_Function(Member_Function);
        } else { member_info.setMember_Function(""); }
        session.getTransaction().commit();

        return true;
    }

    // 更新工作人员验证状态
    public static boolean UpdateWorker_Verification(String Worker_Name, String Worker_fromUserName){
        int Worker_ID = getWoker_Info(Worker_Name).getWorker_ID();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Worker_Info worker_info = session.get(Worker_Info.class, Worker_ID);
        worker_info.setWorker_fromUserName(Worker_fromUserName);
        worker_info.setWorker_Verification(true);
        session.getTransaction().commit();

        return true;
    }

    // 更新书本借阅情况
    public static boolean UpdateBook_State(int Book_ID, int Book_Borrower_ID) throws ParseException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Book_State book_state = session.get(Book_State.class, Book_ID);
        if (book_state.getBook_Statement().equals("归还")) { // 书未被借, 用户发出借书请求
            book_state.setBook_Borrow_Time(getDate(0));
            book_state.setBook_Return_Time(getDate(30));
            book_state.setBook_Statement("于" + getDate(30) + "归还");
            book_state.setBook_Statement_ID(1);
            book_state.setBook_Borrower_ID(Book_Borrower_ID);
        } else if (book_state.getBook_Borrower_ID() == Book_Borrower_ID) { // 用户已经借过这本书
            String Return_Time = book_state.getBook_Return_Time();
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
            Date Return_Date = SDF.parse(Return_Time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Return_Date);
            calendar.add(Calendar.DATE, 14);
            String New_Return_Time = SDF.format(calendar.getTime());
            book_state.setBook_Return_Time(New_Return_Time);
            book_state.setBook_Statement("于" + New_Return_Time + "归还");
            book_state.setBook_Statement_ID(book_state.getBook_Statement_ID() + 1);
        }
        return  true;
    }

    // 还书
    public static boolean ReturnBook(int Borrow_Book_ID){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Book_State book_state = session.get(Book_State.class, Borrow_Book_ID);
        book_state.setBook_Borrow_Time(null);
        book_state.setBook_Return_Time(null);
        book_state.setBook_Statement("归还");
        book_state.setBook_Statement_ID(0);
        book_state.setBook_Borrower_ID(0);
        session.getTransaction().commit();

        return true;
    }

    // 更新借阅记录/次数
    public static boolean UpdateBorrow_Record(int Borrow_Book_ID, int Borrow_Member_ID){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Borrow_Record");
        List<Borrow_Record> borrow_recordList = query.list();
        for (Borrow_Record borrow_record: borrow_recordList) {
            if (borrow_record.getBorrow_Book_ID() == Borrow_Book_ID && borrow_record.getBorrow_Member_ID() == Borrow_Member_ID) {
                borrow_record.setBorrow_Statement_ID(borrow_record.getBorrow_Statement_ID()+1);
                session.getTransaction().commit();
                break;
            }
        }

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

    public static Book_State getBook_StatebyTitle(String Title){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Book_State where Book_Title = '%s'", Title));
        Book_State book_state = null;
        if (query.list().size() > 0) {
            book_state = (Book_State) query.list().get(0);
        }
        session.getTransaction().commit();

        return book_state;
    }

    public static List<Book_State> getBook_StatebyBorrower(int Borrower_ID){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Book_State where Book_Borrower_ID = '%s'", Borrower_ID));
        List<Book_State> book_stateList = null;
        if (query.list().size() > 0) {
            book_stateList = query.list();
        }
        session.getTransaction().commit();

        return book_stateList;
    }

    public static List<Book_State> getBook_StateListbyTitle(String Title){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Book_State where Book_Title = '%s'", Title));
        List<Book_State> book_stateList = null;
        if (query.list().size() > 0) {
            book_stateList = query.list();
        }
        session.getTransaction().commit();

        return book_stateList;
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

    public static Member_Info getMember_InfobyMember_ID(int Member_ID){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Member_Info where Member_ID = '%s'", Member_ID));
        Member_Info member_info = null;
        if (query.list().size() > 0) {
            member_info = (Member_Info) query.list().get(0);
        }
        session.getTransaction().commit();

        return member_info;
    }

    public static List<Member_Info> getAllMember_Info(){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Member_Info"));
        List<Member_Info> member_infoList = null;
        if (query.list().size() > 0) {
            member_infoList = query.list();
        }
        session.getTransaction().commit();

        return member_infoList;
    }

    // 查工作人员
    public static Worker_Info getWoker_Info(String Worker_Name){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Worker_Info where Worker_Name = '%s'", Worker_Name));
        Worker_Info worker_info = null;
        if (query.list().size() > 0) {
            worker_info = (Worker_Info) query.list().get(0);
        }
        session.getTransaction().commit();

        return worker_info;
    }

    public static Worker_Info getWoker_InfobyfromUserName(String Worker_fromUserName){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Worker_Info where Worker_fromUserName = '%s'", Worker_fromUserName));
        Worker_Info worker_info = null;
        if (query.list().size() > 0) {
            worker_info = (Worker_Info) query.list().get(0);
        }
        session.getTransaction().commit();

        return worker_info;
    }


    // 查借书状态/记录
    public static List<Borrow_Record> getBorrow_Record(int Borrow_Member_ID){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Borrow_Record where Borrow_Member_ID", Borrow_Member_ID));
        List<Borrow_Record> borrow_recordList = null;
        if (query.list().size() > 0) {
            borrow_recordList = query.list();
        }
        session.getTransaction().commit();

        return borrow_recordList;
    }

    // 查图书馆名字
    public static String getLibraryName(int Library_ID){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Library_Info where Library_ID = '%s'", Library_ID));
        Library_Info library_info = null;
        if (query.list().size() > 0) {
            library_info = (Library_Info) query.list().get(0);
        }
        session.getTransaction().commit();

        String Library_Name = library_info.getLibrary_Name();
        return Library_Name;
    }
}