package grad.database;

import grad.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Created by Jamie on 4/15/16.
 */
public class Database {


    /*
    *
    * 增
     */
    public static boolean AddMember(Member member) {


        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(member);
        session.getTransaction().commit();

        return true;
    }



    /*
     *
     * 查
     */
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

    public static Member getMember(String toUserName){

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Member where toUserName = '%s'", toUserName));
        Member member = null;
        if (query.list().size() > 0) {
            member = (Member) query.list().get(0);
        }
        session.getTransaction().commit();

        return member;
    }
}
