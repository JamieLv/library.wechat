package grad.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jamie on 4/20/16.
 */

@Entity
@Table(name = "BORROW_RECORD")

public class Borrow_Record {
    int Record_ID;
    int Borrow_Book_ID;
    int Borrow_Member_ID;

    public Borrow_Record(){}

    public Borrow_Record(int Borrow_Book_ID, int Borrow_Member_ID){
        this.Borrow_Book_ID = Borrow_Book_ID;
        this.Borrow_Member_ID = Borrow_Member_ID;
    }

    @Id
    public int getRecord_ID() {
        return Record_ID;
    }

    public void setRecord_ID(int record_ID) {
        Record_ID = record_ID;
    }

    public int getBorrow_Member_ID() {
        return Borrow_Member_ID;
    }

    public void setBorrow_Member_ID(int borrow_Member_ID) {
        Borrow_Member_ID = borrow_Member_ID;
    }

    public int getBorrow_Book_ID() {
        return Borrow_Book_ID;
    }

    public void setBorrow_Book_ID(int borrow_Book_ID) {
        Borrow_Book_ID = borrow_Book_ID;
    }

}
