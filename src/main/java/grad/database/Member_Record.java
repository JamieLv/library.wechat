package grad.database;

/**
 * Created by Jamie on 4/18/16.
 */

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MEMBER_RECORD")

public class Member_Record {
    int Member_id;
    int Book_id;
    String Borrow_Catalog;
    String Borrow_Time;
    String Return_Time;
    int Borrow_Statement;
    String Borrower;

    public Member_Record(){}

    public Member_Record(int Member_id, int Book_id, String Borrow_Catalog, String Borrow_Time, String Return_Time, int Borrow_Statement, String Borrower){
        this.Member_id = Member_id;
        this.Book_id = Book_id;
        this.Borrow_Catalog = Borrow_Catalog;
        this.Borrow_Time = Borrow_Time;
        this.Return_Time = Return_Time;
        this.Borrow_Statement = Borrow_Statement;
        this.Borrower = Borrower;

    }

    public int getMember_id() {
        return Member_id;
    }

    public void setMember_id(int member_id) {
        Member_id = member_id;
    }

    @Id
    public int getBook_id() {
        return Book_id;
    }

    public void setBook_id(int book_id) {
        Book_id = book_id;
    }

    public String getBorrow_Catalog() {
        return Borrow_Catalog;
    }

    public void setBorrow_Catalog(String borrow_Catalog) {
        Borrow_Catalog = borrow_Catalog;
    }

    public String getBorrow_Time() {
        return Borrow_Time;
    }

    public void setBorrow_Time(String borrow_Time) {
        Borrow_Time = borrow_Time;
    }

    public String getReturn_Time() {
        return Return_Time;
    }

    public void setReturn_Time(String return_Time) {
        Return_Time = return_Time;
    }

    public int getBorrow_Statement() {
        return Borrow_Statement;
    }

    public void setBorrow_Statement(int borrow_Statement) {
        Borrow_Statement = borrow_Statement;
    }

    public String getBorrower() {return Borrower;}

    public void setBorrower(String borrower) {Borrower = borrower;}
}
