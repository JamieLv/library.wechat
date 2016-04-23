package grad.database;


/**
 * Created by Jamie on 4/23/16.
 */
public class BaiduMapLibrary{
    private String library_name;
    private String library_address;
    private String library_telephone;

    public BaiduMapLibrary(){}

    public BaiduMapLibrary(String library_name, String library_address, String library_telephone){
        this.library_name = library_name;
        this.library_address = library_address;
        this.library_telephone = library_telephone;
    }

    public String getLibrary_name() {
        return library_name;
    }

    public void setLibrary_name(String library_name) {
        this.library_name = library_name;
    }

    public String getLibrary_address() {
        return library_address;
    }

    public void setLibrary_address(String library_address) {
        this.library_address = library_address;
    }

    public String getLibrary_telephone() {
        return library_telephone;
    }

    public void setLibrary_telephone(String library_telephone) {
        this.library_telephone = library_telephone;
    }
}
