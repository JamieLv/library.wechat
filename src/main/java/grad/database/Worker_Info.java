package grad.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jamie on 4/28/16.
 */

@Entity
@Table(name = "WORKER_INFO")

public class Worker_Info {
    int Worker_ID;
    String Worker_Name;
    String Worker_Gender;
    int Worker_Age;
    String Worker_Mobile;
    String Worker_fromUserName;
    String Worker_Duty;
    int Worker_Coefficient;

    public Worker_Info() {
    }

    public Worker_Info(String Worker_Name, String Worker_Gender, int Worker_Age, String Worker_Mobile, String Worker_Duty) {
        this.Worker_Name = Worker_Name;
        this.Worker_Gender = Worker_Gender;
        this.Worker_Age = Worker_Age;
        this.Worker_Mobile = Worker_Mobile;
        this.Worker_Duty = Worker_Duty;
    }

    @Id
    public int getWorker_ID() {
        return Worker_ID;
    }

    public void setWorker_ID(int worker_ID) {
        Worker_ID = worker_ID;
    }

    public String getWorker_Name() {
        return Worker_Name;
    }

    public void setWorker_Name(String worker_Name) {
        Worker_Name = worker_Name;
    }

    public String getWorker_Gender() {
        return Worker_Gender;
    }

    public void setWorker_Gender(String worker_Gender) {
        Worker_Gender = worker_Gender;
    }

    public int getWorker_Age() {
        return Worker_Age;
    }

    public void setWorker_Age(int worker_Age) {
        Worker_Age = worker_Age;
    }

    public String getWorker_Mobile() {
        return Worker_Mobile;
    }

    public void setWorker_Mobile(String worker_Mobile) {
        Worker_Mobile = worker_Mobile;
    }

    public String getWorker_fromUserName() {
        return Worker_fromUserName;
    }

    public void setWorker_fromUserName(String worker_fromUserName) {
        Worker_fromUserName = worker_fromUserName;
    }

    public String getWorker_Duty() {
        return Worker_Duty;
    }

    public void setWorker_Duty(String worker_Duty) {
        Worker_Duty = worker_Duty;
    }

    public int getWorker_Coefficient() {
        return Worker_Coefficient;
    }

    public void setWorker_Coefficient(int worker_Coefficient) {
        Worker_Coefficient = worker_Coefficient;
    }
}
