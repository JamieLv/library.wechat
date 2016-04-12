/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.message;

import grad.pojo.*;
import grad.util.WeixinUtil;


/**
 * 菜单管理器类
 *
 * @author smile
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
        btn11.setName("短驳车");
        btn11.setType("click");
        btn11.setKey(CommonButton.KEY_BUS);

        CommonButton btn12 = new CommonButton();
        btn12.setName("我要借伞");
        btn12.setType("click");
        btn12.setKey(CommonButton.KEY_BORROW_UMBRELLA);

        CommonButton returnUmbrella = new CommonButton();
        returnUmbrella.setName("我要还伞");
        returnUmbrella.setType("scancode_waitmsg");
        returnUmbrella.setKey(CommonButton.KEY_RETURN_UMBRELLA);

        CommonButton btn13 = new CommonButton();
        btn13.setName("辅修车票");
        btn13.setType("click");
        btn13.setKey(CommonButton.KEY_SIDE_BUS);

        CommonButton btn21 = new CommonButton();
        btn21.setName("工作时间");
        btn21.setType("click");
        btn21.setKey(CommonButton.KEY_WORKTIME);

        CommonButton btn22 = new CommonButton();
        btn22.setName("联系方式");
        btn22.setType("click");
        btn22.setKey(CommonButton.KEY_CONTACTS);

        CommonButton schoolCal = new CommonButton();
        schoolCal.setName("学校校历");
        schoolCal.setType("view");
        schoolCal.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5MTY1NjYyOA==&mid=402636731&idx=1&sn=c365d2ccbd4fa2f66bec6ff8d3c1fed3#rd");

        CommonButton btn31 = new CommonButton();
        btn31.setName("我要投诉");
        btn31.setType("click");
        btn31.setKey(CommonButton.KEY_SUE);

        CommonButton btn32 = new CommonButton();
        btn32.setName("投诉追踪");
        btn32.setType("click");
        btn32.setKey(CommonButton.KEY_SUE_TRACK);
        
        CommonButton btn33 = new CommonButton();
        btn33.setName("校园日常");
        btn33.setType("view");
        btn33.setUrl("http://mp.weixin.qq.com/mp/homepage?__biz=MjM5MTY1NjYyOA==&hid=1&sn=362814850d723b6c81cc3d6731c4809d#wechat_redirect");

        CommonButton btn34 = new CommonButton();
        btn34.setName("加入我们");
        btn34.setType("click");
        btn34.setKey(CommonButton.KEY_JOIN_US);

        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("我最在意");
        mainBtn1.setSub_button(new Button[]{btn11, btn12, returnUmbrella, btn13});

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("我想知道");
        mainBtn2.setSub_button(new CommonButton[]{btn21, btn22, schoolCal});

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("我有烦恼");
        mainBtn3.setSub_button(new Button[]{btn31, btn32, btn33, btn34});

        Menu menu = new Menu();
        menu.setButton(new Button[]{mainBtn1, mainBtn2, mainBtn3});

        return menu;
    }
}
