package grad.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jamie on 4/18/16.
 */

@Entity
@Table(name = "LIBRARY")

public class Library {
    int Library_id;
    String Library_Name;

    @Id
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
}
