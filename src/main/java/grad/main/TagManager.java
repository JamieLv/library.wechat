package grad.main;

import grad.pojo.AccessToken;
import grad.util.WeixinUtil;
import java.text.ParseException;

import static grad.util.WeixinUtil.getUserTagID;

/**
 * Created by Jamie on 4/28/16.
 */
public class TagManager {

    public static void main(String[] args) throws ParseException {
        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

        if (at != null) {
            String op = "get";
            if (op.equals("add")){WeixinUtil.createTag(at.getToken());}
            if (op.equals("get")){WeixinUtil.getAllTag(at.getToken());}
            if (op.equals("del")){WeixinUtil.deleteTag(at.getToken());}
            if (op.equals("up")){WeixinUtil.updateTag(at.getToken());}
            System.out.println(getUserTagID(at.getToken(), "oog9Zv85WcOoYRCuOPwYb6KIVgHI"));
        }
    }

    public static int UserTagID(String fromUserName){
        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

        if (at != null) {
            int UserTagID = getUserTagID(at.getToken(), fromUserName);
            return UserTagID;
        }

        return 0;
    }

    public static boolean batchtagging(String fromUserName, String request){
        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

        if (at != null) {
            if (request.equals("Member")){
                WeixinUtil.batchReaderTag(at.getToken(), fromUserName);
            }
            else if (request.equals("ReturnWorker")){
                WeixinUtil.batchReturnWorkerTag(at.getToken(), fromUserName);
            }
            else if (request.equals("AddWorker")){
                WeixinUtil.batchAddWorkerTag(at.getToken(), fromUserName);
            }
        }
        return true;
    }

    public static boolean batchuntagging(String fromUserName, String request){
        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

        if (at != null) {
            if (request.equals("Member")){
                WeixinUtil.removeReaderTag(at.getToken(), fromUserName);
            }
            else if (request.equals("ReturnWorker")){
                WeixinUtil.removeReturnWorkerTag(at.getToken(), fromUserName);
            }
            else if (request.equals("AddWorker")){
                WeixinUtil.removeAddrWorkerTag(at.getToken(), fromUserName);
            }
        }
        return true;
    }


}
