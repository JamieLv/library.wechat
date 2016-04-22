package grad.servlet;

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.apache.commons.lang.StringUtils;


/**
 * Created by Jamie on 4/22/16.
 */
public class BaiduMapAPI {
    private static String ak = "HAWvDthwbrBM8itvGawZ2E99EQTIg5qf";

    public static Map<String, String> testPost(String x, String y) throws IOException {
        URL url = new URL("http://api.map.baidu.com/geocoder?" + ak + "HAWvDthwbrBM8itvGawZ2E99EQTIg5qf" +
                "&callback=renderReverse&location=" + x
                + "," + y + "&output=json");
        URLConnection connection = url.openConnection();
        /**
         * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
         * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
         */
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection
                .getOutputStream(), "utf-8");
//        remember to clean up
        out.flush();
        out.close();
//        一旦发送成功，用以下方法就可以得到服务器的回应：
        String res;
        InputStream l_urlStream;
        l_urlStream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                l_urlStream,"UTF-8"));
        StringBuilder sb = new StringBuilder("");
        while ((res = in.readLine()) != null) {
            sb.append(res.trim());
        }
        String str = sb.toString();
        System.out.println(str);
        Map<String,String> map = null;
        if(StringUtils.isNotEmpty(str)) {
            int addStart = str.indexOf("formatted_address\":");
            int addEnd = str.indexOf("\",\"business");
            if(addStart > 0 && addEnd > 0) {
                String address = str.substring(addStart+20, addEnd);
                map = new HashMap<String,String>();
                map.put("address", address);
                return map;
            }
        }

        return null;

    }

    public static void main(String[] args) throws IOException {
        Map<String, String> json = BaiduMapAPI.testPost("29.542938", "114.064022");
        System.out.println("address :" + json.get("address"));
    }

}
