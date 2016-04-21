package grad.database;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Jamie on 4/20/16.
 */

@Entity
@Table(name = "LIBRARY_INFO")

public class Library_Info {
    int Library_ID;
    String Library_Name;

    public int getLibrary_ID() {
        return Library_ID;
    }

    public void setLibrary_ID(int library_ID) {
        Library_ID = library_ID;
    }

    public String getLibrary_Name() {
        return Library_Name;
    }

    public void setLibrary_Name(String library_Name) {
        Library_Name = library_Name;
    }
}
