package grad.database;

/**
 * Created by Jamie on 4/19/16.
 */
public class Borrow_Display {

    int Display_id;
    int Book_id;
    String Book_Title;
    String Borrow_Catalog;
    String Borrow_Time;
    String Return_Time;
    int Borrow_Statement;

    public Borrow_Display(){}

    public Borrow_Display(int Display_id, int Book_id, String Book_Title, String Borrow_Catalog, String Borrow_Time, String Return_Time, int Borrow_Statement){
        this.Display_id = Display_id;
        this.Book_id = Book_id;
        this.Book_Title = Book_Title;
        this.Borrow_Catalog = Borrow_Catalog;
        this.Borrow_Time = Borrow_Time;
        this.Return_Time = Return_Time;
        this.Borrow_Statement =Borrow_Statement;

    }

    public int getDisplay_id() {
        return Display_id;
    }

    public void setDisplay_id(int display_id) {
        Display_id = display_id;
    }

    public int getBook_id() {
        return Book_id;
    }

    public void setBook_id(int book_id) {
        Book_id = book_id;
    }

    public String getBook_Title() {
        return Book_Title;
    }

    public void setBook_Title(String book_title) {
        Book_Title = book_title;
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
}
