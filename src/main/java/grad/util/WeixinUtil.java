/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.util;

import grad.pojo.AccessToken;
import grad.pojo.Menu;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

/**
 *
 * Created by Jamie on 4/11/16.
 */
public class WeixinUtil {

    private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化，解决HTTPS请求问题
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（兼容GET/POST两种方式）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时  
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码  
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源  
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonObject;
    }

    // 获取access_token的接口地址（GET） 限200（次/天）  
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 获取access_token
     *
     * @param appid 凭证
     * @param appsecret 密钥
     * @return
     */
    public static AccessToken getAccessToken(String appid, String appsecret) {
        AccessToken accessToken = null;

        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功  
        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
            } catch (JSONException e) {
                accessToken = null;
                // 获取token失败  
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return accessToken;
    }

    /**
     * 创建菜单
     *
     * @param menu 菜单实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(Menu menu, String accessToken) {
        int result = 0;

        // 拼装创建菜单的url  
        String url =  "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken;
        // 将菜单对象转换成json字符串  
        String jsonMenu = JSONObject.fromObject(menu).toString();
        // 调用接口创建菜单  
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            int errCode  = jsonObject.getInt("errcode");
            if (0 != errCode) {
                result = errCode;
                String errMsg = jsonObject.getString("errmsg");
                System.out.println("创建菜单失败 errCode:" + errCode + " errMsg:" + errMsg);
            }
        }
        return result;
    }

    public static int AddConditionalMenu(Menu menu, String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url =  "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=" + accessToken;
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.fromObject(menu).toString();
        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

        System.out.println("WeixinManager.conditional()"+jsonObject.toString());
        return result;
    }


    /**
     * 创建群组
     */
    public static int createTag(String accessToken){
        int result = 0;
        String post = "\n" +
                "{\n" +
                "\"tag\":{\n" +
                "\"name\":\"职工\"}\n" +
                "}";
        String url = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=" + accessToken;
        String jsonGroup = JSONObject.fromObject(post).toString();
        JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);

        System.out.println("WeixinManager.createGroup()"+jsonObject.toString());

        return result;
    }

    public static int getAllTag(String accessToken){
        int result = 0;

        String url = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=" + accessToken;
        JSONObject jsonObject = httpRequest(url, "GET", null);

        System.out.println("WeixinManager.getGroup()"+jsonObject.toString());

        return result;
    }

    public static int deleteTag(String accessToken){
        int result = 0;
        String post = "\n" +
                "{\n" +
                "\"tagid\":101,\n" +
                "}";
        String url = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=" + accessToken;
        String jsonGroup = JSONObject.fromObject(post).toString();
        JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);

        System.out.println("WeixinManager.deleteGroup()"+jsonObject.toString());

        return result;
    }

    public static int updateTag(String accessToken){
        int result = 0;
        String post = "\n" +
                "{\n" +
                "\"tag\":{\n" +
                "\"id\":101,\n" +
                "\"name\":\"职工\"\n" +
                "}}";
        String url = "https://api.weixin.qq.com/cgi-bin/tags/update?access_token=" + accessToken;
        String jsonGroup = JSONObject.fromObject(post).toString();
        JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);

        System.out.println("WeixinManager.updateGroup()"+jsonObject.toString());

        return result;
    }

    public static int batchReaderTag(String accessToken, String fromUserName){
        int result = 0;
        String post = "\n" +
                "{\n" +
                "\"openid_list\":[\n" +
                "\"" + fromUserName + "\"\n" +
                "],\n" +
                "\"tagid\":100\n" +
                "}";
        String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=" + accessToken;
        String jsonGroup = JSONObject.fromObject(post).toString();
        JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);

        System.out.println("WeixinManager.batchreadertag()"+jsonObject.toString());

        return result;
    }

    public static int batchWorkerTag(String accessToken, String fromUserName){
        int result = 0;
        String post = "\n" +
                "{\n" +
                "\"openid_list\":[\n" +
                "\"" + fromUserName + "\"\n" +
                "],\n" +
                "\"tagid\":101\n" +
                "}";
        String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=" + accessToken;
        String jsonGroup = JSONObject.fromObject(post).toString();
        JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);

        System.out.println("WeixinManager.batchworkertag()"+jsonObject.toString());

        return result;
    }

    public static int getUserTagID(String accessToken, String fromUserName){
        String post = "\n" +
                "{\n" +
                "\"openid\":\"" + fromUserName + "\"\n" +
                "}";
        String url = "https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=" + accessToken;
        String jsonTagID = JSONObject.fromObject(post).toString();
        JSONObject jsonObject = httpRequest(url, "POST", jsonTagID);

        JSONArray jsonArray = jsonObject.getJSONArray("tagid_list");
        int tagID = (int) jsonArray.get(0);

        System.out.println("WeixinManager.getUserTagID()"+jsonObject.toString());

        return tagID;
    }

}
