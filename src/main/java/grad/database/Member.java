package grad.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jamie on 4/15/16.
 */

@Entity
@Table(name = "MEMBER")

public class Member {

    int Member_id;
    String Name;
    String Gender;
    int Age;
    String Mobile;
    String RegisterTime;
    String fromUserName;

    public Member(){}

    public Member(String Name, String Gender, int Age, String Mobile, String RegisterTime, String fromUserName){
        this.Name = Name;
        this.Gender = Gender;
        this.Age = Age;
        this.Mobile = Mobile;
        this.RegisterTime = RegisterTime;
        this.fromUserName = fromUserName;

    }

    @Id
    public int getMember_id() {
        return Member_id;
    }

    public void setMember_id(int member_id) {
        Member_id = member_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getRegisterTime() {
        return RegisterTime;
    }

    public void setRegisterTime(String registerTime) {
        RegisterTime = registerTime;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }
}
