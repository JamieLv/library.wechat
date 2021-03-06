package grad.tools;

import java.io.IOException;
import java.io.InputStream;

import grad.database.DouBanBook;
import grad.servlet.BookXMLParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * Created by Jamie on 4/17/16.
 */
public class RetrieveDocumentByURL {

    public static String Return_BookPicURL(String Book_ISBN) throws ClientProtocolException, IOException{
        DefaultHttpClient client = new DefaultHttpClient();

//        Pattern p = Pattern.compile("[^0-9]");
//        Matcher m = p.matcher(Book_ISBN);
//        String Str_ISBN = m.toString();

        HttpGet get = new HttpGet("http://api.douban.com/book/subject/isbn/" + Book_ISBN);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        DouBanBook book = new BookXMLParser(is).getBook();
//        System.out.println("title:->" + book.getTitle());
//        System.out.println("summary:->"+ book.getSummary());
//        System.out.println("price:-->" + book.getPrice());
//        System.out.println("author:-->" + book.getAuthor());
//        System.out.println("ImagePath:-->" + book.getImagePath());
        return book.getImagePath();
    }

    public static DouBanBook Return_BookInfo(String Book_ISBN) throws ClientProtocolException, IOException{
        DefaultHttpClient client = new DefaultHttpClient();

        HttpGet get = new HttpGet("http://api.douban.com/book/subject/isbn/" + Book_ISBN);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        DouBanBook book = new BookXMLParser(is).getBook();
//        System.out.println("title:->" + book.getTitle());
//        System.out.println("summary:->"+ book.getSummary());
//        System.out.println("price:-->" + book.getPrice());
//        System.out.println("author:-->" + book.getAuthor());
//        System.out.println("ImagePath:-->" + book.getImagePath());
       return book;
    }

//    public static void main(String[] args) throws ClientProtocolException, IOException {
//        new RetrieveDocumentByURL("http://api.douban.com/book/subject/isbn/9787308083256");
//    }

}
