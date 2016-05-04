package grad.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jamie on 4/20/16.
 */

@Entity
@Table(name = "MEMBER_INFO")

public class Member_Info {
    int Member_ID;
    String Member_Name;
    String Member_Gender;
    int Member_Age;
    String Member_Mobile;
    String Member_RegisterTime;
    String Member_fromUserName;
    Boolean Member_Verification;
    String Member_Function;

    public Member_Info(){}

    public Member_Info(String Member_Name, String Member_Gender, int Member_Age, String Member_Mobile,
                       String Member_RegisterTime, String Member_fromUserName, Boolean Member_Verification){
        this.Member_Name = Member_Name;
        this.Member_Gender = Member_Gender;
        this.Member_Age = Member_Age;
        this.Member_Mobile = Member_Mobile;
        this.Member_RegisterTime = Member_RegisterTime;
        this.Member_fromUserName = Member_fromUserName;
        this.Member_Verification = Member_Verification;
    }

    @Id
    public int getMember_ID() {
        return Member_ID;
    }

    public void setMember_ID(int member_ID) {
        Member_ID = member_ID;
    }

    public String getMember_Name() {
        return Member_Name;
    }

    public void setMember_Name(String member_Name) {
        Member_Name = member_Name;
    }

    public String getMember_Gender() {
        return Member_Gender;
    }

    public void setMember_Gender(String member_Gender) {
        Member_Gender = member_Gender;
    }

    public int getMember_Age() {
        return Member_Age;
    }

    public void setMember_Age(int member_Age) {
        Member_Age = member_Age;
    }

    public String getMember_Mobile() {
        return Member_Mobile;
    }

    public void setMember_Mobile(String member_Mobile) {
        Member_Mobile = member_Mobile;
    }

    public String getMember_RegisterTime() {
        return Member_RegisterTime;
    }

    public void setMember_RegisterTime(String member_RegisterTime) {
        Member_RegisterTime = member_RegisterTime;
    }

    public String getMember_fromUserName() {
        return Member_fromUserName;
    }

    public void setMember_fromUserName(String member_fromUserName) {
        Member_fromUserName = member_fromUserName;
    }

    public Boolean getMember_Verification() {
        return Member_Verification;
    }

    public void setMember_Verification(Boolean member_Verification) {
        Member_Verification = member_Verification;
    }

    public String getMember_Function() {
        return Member_Function;
    }

    public void setMember_Function(String member_Function) {
        Member_Function = member_Function;
    }
}
