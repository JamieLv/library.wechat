/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.main;

import grad.pojo.*;
import grad.util.WeixinUtil;


/**
 * 菜单管理器类
 *
 * Created by Jamie on 4/12/16.
 */
public class MenuManager {

    public static void main(String[] args) {

        boolean STAGE = true;
        String appId;
        String appSecret;

        if (STAGE) {
            appId = "wxe13392d6482304c4";
            appSecret = "41b6c04d9ac9819c779a186e0d6908ab";
        } else {
            appId = "wx7a473eabfb2a4495";
            appSecret = "24895b9d3db3d73f85cfd3eb9e5c59f9";
        }

        // 调用接口获取access_token
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

        if (null != at) {
            // 调用接口创建菜单
            int result = WeixinUtil.createMenu(getMenu(), at.getToken());

            // 判断菜单创建结果
            if (0 == result) {
                System.out.println("菜单创建成功！");
            } else {
                System.out.println("菜单创建失败，错误码：" + result);
            }
        }
    }

    /**
     * 组装菜单数据
     *
     * @return
     */
    private static Menu getMenu() {
        CommonButton btn11 = new CommonButton();
        btn11.setName("11");
        btn11.setType("click");
        btn11.setKey("11");

        CommonButton btn12 = new CommonButton();
        btn12.setName("12");
        btn12.setType("click");
        btn12.setKey("12");

        CommonButton btn13 = new CommonButton();
        btn13.setName("13");
        btn13.setType("click");
        btn13.setKey("13");

        CommonButton btn14 = new CommonButton();
        btn14.setName("扫码");
        btn14.setType("scancode_waitmsg");
        btn14.setKey("14");

        CommonButton btn21 = new CommonButton();
        btn21.setName("21");
        btn21.setType("click");
        btn21.setKey("21");

        CommonButton btn22 = new CommonButton();
        btn22.setName("22");
        btn22.setType("click");
        btn22.setKey("22");

        CommonButton btn23 = new CommonButton();
        btn23.setName("百度");
        btn23.setType("view");
        btn23.setUrl("http://m.baidu.com");

        CommonButton btn31 = new CommonButton();
        btn31.setName("31");
        btn31.setType("click");
        btn31.setKey("31");

        CommonButton btn32 = new CommonButton();
        btn32.setName("32");
        btn32.setType("click");
        btn32.setKey("32");
        
        CommonButton btn33 = new CommonButton();
        btn33.setName("官网");
        btn33.setType("view");
        btn33.setUrl("http://sse.tongji.edu.cn/zh");

        CommonButton btn34 = new CommonButton();
        btn34.setName("加入我们");
        btn34.setType("click");
        btn34.setKey("34");

        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("我最在意");
        mainBtn1.setSub_button(new Button[]{btn11, btn12, btn13, btn14});

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("我想知道");
        mainBtn2.setSub_button(new CommonButton[]{btn21, btn22, btn23});

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("我有烦恼");
        mainBtn3.setSub_button(new Button[]{btn31, btn32, btn33, btn34});

        Menu menu = new Menu();
        menu.setButton(new Button[]{mainBtn1, mainBtn2, mainBtn3});

        return menu;
    }
}
