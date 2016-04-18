package grad.database;

/**
 * Created by Jamie on 4/15/16.
 */

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BOOK")

public class Book {

    int Book_id;
    String ISBN;
    String Title;
    String Catalog;
    String Author;
    String Translator;
    String Publisher;
    String IssueTime;
    String Price;

    public Book(){}

    public Book(String ISBN, String Title, String Catalog, String Author, String Publisher, String IssueTime, String Price){
        this.ISBN = ISBN;
        this.Title = Title;
        this.Catalog = Catalog;
        this.Author = Author;
//        this.Translator = Translator;
        this.Publisher = Publisher;
        this.IssueTime = IssueTime;
        this.Price = Price;

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

    public String getCatalog() {
        return Catalog;
    }

    public void setCatalog(String catalog) {
        Catalog = catalog;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getTranslator() {
        return Translator;
    }

    public void setTranslator(String translator) {
        Translator = translator;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getIssueTime() {
        return IssueTime;
    }

    public void setIssueTime(String issueTime) {
        IssueTime = issueTime;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
