package grad.database;

import grad.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

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

    // 增加会员
    public static boolean AddMember(Member member){


        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(member);
        session.getTransaction().commit();

        return true;
    }

    // 增加新书
    public static boolean AddBook(Book book){


        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(book);
        session.getTransaction().commit();

        return true;
    }

    /*
     *
     * 改
     */
    public static boolean UpdateMobile(String fromUserName, String Mobile){
        int Member_id = getMember_id(fromUserName);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Member member = (Member)session.get(Member.class, Member_id);
        member.setMobile(Mobile);
        session.getTransaction().commit();

        return true;
    }

    public static boolean UpdateMember_Verification(String fromUserName, boolean verify){
        int Member_id = getMember_id(fromUserName);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Member member = (Member)session.get(Member.class, Member_id);
        member.setMember_Verification(verify);
        session.getTransaction().commit();

        return true;
    }



    /*
     *
     * 查
     */

    // 查书本
    public static Book getBook(String title){

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
    public static int getMember_id(String  fromUserName){
        int Member_id = 0;

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Member");
        List<Member> member_list = query.list();
        session.getTransaction().commit();

        for(Member m: member_list) {
            if(m.getFromUserName().equals(fromUserName)){
                Member_id = m.getMember_id();
                break;
            }
        }
        return  Member_id;
    }

}
