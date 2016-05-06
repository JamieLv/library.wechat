package grad.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jamie on 5/5/16.
 */

@Entity
@Table(name = "SUBSCRIBER_INFO")

public class Subscriber_Info {
    int Subscriber_ID;
    int Subscribe;
    String OpenID;
    String Nickname;
    String Subscriber_Sex;
    String Subscriber_Language;
    String Subscriber_City;
    String Subscriber_Province;
    String Subscriber_Country;
    String Subscriber_HeadingImgURL;
    String Tagid_List;
    String Subscriber_Function;

    public Subscriber_Info(){}

    public Subscriber_Info(String openID, String nickname, String subscriber_Sex, String subscriber_Language, String subscriber_City, String subscriber_Province, String subscriber_Country, String subscriber_HeadingImgURL, String tagid_List) {
        OpenID = openID;
        Nickname = nickname;
        Subscriber_Sex = subscriber_Sex;
        Subscriber_Language = subscriber_Language;
        Subscriber_City = subscriber_City;
        Subscriber_Province = subscriber_Province;
        Subscriber_Country = subscriber_Country;
        Subscriber_HeadingImgURL = subscriber_HeadingImgURL;
        Tagid_List = tagid_List;
    }

    @Id
    public int getSubscriber_ID() {
        return Subscriber_ID;
    }

    public void setSubscriber_ID(int subscriber_ID) {
        Subscriber_ID = subscriber_ID;
    }

    public int getSubscribe() {
        return Subscribe;
    }

    public void setSubscribe(int subscribe) {
        Subscribe = subscribe;
    }

    public String getOpenID() {
        return OpenID;
    }

    public void setOpenID(String openID) {
        OpenID = openID;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getSubscriber_Sex() {
        return Subscriber_Sex;
    }

    public void setSubscriber_Sex(String subscriber_Sex) {
        Subscriber_Sex = subscriber_Sex;
    }

    public String getSubscriber_Language() {
        return Subscriber_Language;
    }

    public void setSubscriber_Language(String subscriber_Language) {
        Subscriber_Language = subscriber_Language;
    }

    public String getSubscriber_City() {
        return Subscriber_City;
    }

    public void setSubscriber_City(String subscriber_City) {
        Subscriber_City = subscriber_City;
    }

    public String getSubscriber_Province() {
        return Subscriber_Province;
    }

    public void setSubscriber_Province(String subscriber_Province) {
        Subscriber_Province = subscriber_Province;
    }

    public String getSubscriber_Country() {
        return Subscriber_Country;
    }

    public void setSubscriber_Country(String subscriber_Country) {
        Subscriber_Country = subscriber_Country;
    }

    public String getSubscriber_HeadingImgURL() {
        return Subscriber_HeadingImgURL;
    }

    public void setSubscriber_HeadingImgURL(String subscriber_HeadingImgURL) {
        Subscriber_HeadingImgURL = subscriber_HeadingImgURL;
    }

    public String getTagid_List() {
        return Tagid_List;
    }

    public void setTagid_List(String tagid_List) {
        Tagid_List = tagid_List;
    }

    public String getSubscriber_Function() {
        return Subscriber_Function;
    }

    public void setSubscriber_Function(String subscriber_Function) {
        Subscriber_Function = subscriber_Function;
    }
}
