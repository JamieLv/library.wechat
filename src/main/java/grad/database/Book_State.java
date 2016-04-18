package grad.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jamie on 4/18/16.
 */

@Entity
@Table(name = "BOOK_STATE")

public class Book_State {

    int Book_id;
    String ISBN;
    String Title;
    int Library_id;
    String Library_Name;
    String Statement;
    String Borrower;

    public Book_State(){}

    public Book_State(int Book_id, String ISBN, String Title, int Library_id, String Library_Name, String Statement, String Borrower){
        this.Book_id = Book_id;
        this.ISBN = ISBN;
        this.Title = Title;
        this.Library_id = Library_id;
        this.Library_Name = Library_Name;
        this.Statement = Statement;
        this.Borrower = Borrower;

    }

    @Id
    public int getBook_id() {
        return Book_id;
    }

    public void setBook_id(int book_id) {
        Book_id = book_id;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getLibrary_id() {
        return Library_id;
    }

    public void setLibrary_id(int library_id) {
        Library_id = library_id;
    }

    public String getLibrary_Name() {
        return Library_Name;
    }

    public void setLibrary_Name(String library_Name) {
        Library_Name = library_Name;
    }

    public String getStatement() {
        return Statement;
    }

    public void setStatement(String statement) {
        Statement = statement;
    }

    public String getBorrower() {
        return Borrower;
    }

    public void setBorrower(String borrower) {
        Borrower = borrower;
    }
}
