package grad.main;

import grad.pojo.AccessToken;
import grad.util.WeixinUtil;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Jamie on 5/5/16.
 */
public class UserManager {

    public static void main(String[] args) throws ParseException {
        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

        List<JSONObject> User_InfoList = WeixinUtil.getUserInfo("oog9Zv85WcOoYRCuOPwYb6KIVgHI", at.getToken());

        for (JSONObject User_Info : User_InfoList) {
            System.out.println(User_Info.get("nickname"));
        }
    }

    public static JSONObject getUser_Info(String fromUserName){
        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

        List<JSONObject> User_InfoList = WeixinUtil.getUserInfo(fromUserName, at.getToken());

        for (JSONObject User_Info : User_InfoList) {
            System.out.println(User_Info.get("nickname"));
            return User_Info;
        }

        return null;
    }

}