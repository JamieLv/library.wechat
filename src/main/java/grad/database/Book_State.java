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
    String Book_ISBN;
    String Book_Title;
    String Book_Category;
    String Book_Author;
    String Book_Publisher;
    String Book_PubTime;
    String Book_Price;
    int Book_inLibrary_id;
    String Book_Borrow_Time;
    String Book_Return_Time;
    String Book_Statement;
    int Book_Statement_ID;
    String Book_Borrower_ID;

    public Book_State() {
    }

    public Book_State(String Book_ISBN, String Book_Title, String Book_Category, String Book_Author, String Book_Publisher, String Book_PubTime, String Book_Price, int Book_inLibrary_id, String Book_Statement) {
        this.Book_ISBN = Book_ISBN;
        this.Book_Title = Book_Title;
        this.Book_Category = Book_Category;
        this.Book_Author = Book_Author;
        this.Book_Publisher = Book_Publisher;
        this.Book_PubTime = Book_PubTime;
        this.Book_Price = Book_Price;
        this.Book_inLibrary_id = Book_inLibrary_id;
        this.Book_Statement = Book_Statement;
    }

    @Id
    public int getBook_id() {
        return Book_id;
    }

    public void setBook_id(int book_id) {
        Book_id = book_id;
    }

    public String getBook_ISBN() {
        return Book_ISBN;
    }

    public void setBook_ISBN(String book_ISBN) {
        Book_ISBN = book_ISBN;
    }

    public String getBook_Title() {
        return Book_Title;
    }

    public void setBook_Title(String book_Title) {
        Book_Title = book_Title;
    }

    public String getBook_Category() {
        return Book_Category;
    }

    public void setBook_Category(String book_Category) {
        Book_Category = book_Category;
    }

    public String getBook_Author() {
        return Book_Author;
    }

    public void setBook_Author(String book_Author) {
        Book_Author = book_Author;
    }

    public String getBook_Publisher() {
        return Book_Publisher;
    }

    public void setBook_Publisher(String book_Publisher) {
        Book_Publisher = book_Publisher;
    }

    public String getBook_PubTime() {
        return Book_PubTime;
    }

    public void setBook_PubTime(String book_PubTime) {
        Book_PubTime = book_PubTime;
    }

    public String getBook_Price() {
        return Book_Price;
    }

    public void setBook_Price(String book_Price) {
        Book_Price = book_Price;
    }

    public int getBook_inLibrary_id() {
        return Book_inLibrary_id;
    }

    public void setBook_inLibrary_id(int book_inLibrary_id) {
        Book_inLibrary_id = book_inLibrary_id;
    }

    public String getBook_Borrow_Time() {
        return Book_Borrow_Time;
    }

    public void setBook_Borrow_Time(String book_Borrow_Time) {
        Book_Borrow_Time = book_Borrow_Time;
    }

    public String getBook_Return_Time() {
        return Book_Return_Time;
    }

    public void setBook_Return_Time(String book_Return_Time) {
        Book_Return_Time = book_Return_Time;
    }

    public String getBook_Statement() {
        return Book_Statement;
    }

    public void setBook_Statement(String book_Statement) {
        Book_Statement = book_Statement;
    }

    public int getBook_Statement_ID() {
        return Book_Statement_ID;
    }

    public void setBook_Statement_ID(int book_Statement_ID) {
        Book_Statement_ID = book_Statement_ID;
    }

    public String getBook_Borrower_ID() {
        return Book_Borrower_ID;
    }

    public void setBook_Borrower_ID(String book_Borrower_ID) {
        Book_Borrower_ID = book_Borrower_ID;
    }

}
