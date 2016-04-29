/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.main;

import grad.pojo.*;
import grad.util.WeixinUtil;

import java.text.ParseException;


/**
 * 菜单管理器类
 *
 * Created by Jamie on 4/12/16.
 */
public class MenuManager {

    public static void main(String[] args) throws ParseException {

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

        if (at != null) {
            // 调用接口创建菜单
            int result = WeixinUtil.createMenu(getMenu(), at.getToken());

            // 判断菜单创建结果
            if (result == 0) {
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
        btn11.setName("会员卡");
        btn11.setType("click");
        btn11.setKey(CommonButton.KEY_MEMBERSHIP);

        CommonButton btn12 = new CommonButton();
        btn12.setName("我要借书");
        btn12.setType("scancode_waitmsg");
        btn12.setKey(CommonButton.KEY_BORROW_BOOK);

        CommonButton btn13 = new CommonButton();
        btn13.setName("我要还书");
        btn13.setType("click");
        btn13.setKey(CommonButton.KEY_RETURN_BOOK);

        CommonButton btn14 = new CommonButton();
        btn14.setName("使用帮助");
        btn14.setType("click");
        btn14.setKey(CommonButton.KEY_HELP);

        CommonButton btn21 = new CommonButton();
        btn21.setName("图书检索");
        btn21.setType("click");
        btn21.setKey(CommonButton.KEY_BOOK);

        CommonButton btn22 = new CommonButton();
        btn22.setName("阅览室预约");
        btn22.setType("click");
        btn22.setKey(CommonButton.KEY_RESERVE_ROOM);

        CommonButton btn23 = new CommonButton();
        btn23.setName("图书推荐");
        btn23.setType("click");
        btn23.setKey(CommonButton.KEY_BOOK_RECOMMEND);

        CommonButton btn24 = new CommonButton();
        btn24.setName("门店查询");
        btn24.setType("location_select");
        btn24.setKey(CommonButton.KEY_NEARBY);

        CommonButton btn31 = new CommonButton();
        btn31.setName("我有意见");
        btn31.setType("click");
        btn31.setKey(CommonButton.KEY_ADVICE);

        CommonButton btn32 = new CommonButton();
        btn32.setName("意见追踪");
        btn32.setType("click");
        btn32.setKey(CommonButton.KEY_ADVICE_TRACK);
        
        CommonButton btn33 = new CommonButton();
        btn33.setName("官网");
        btn33.setType("view");
        btn33.setUrl("http://sse.tongji.edu.cn/zh");

        CommonButton btn34 = new CommonButton();
        btn34.setName("加入我们");
        btn34.setType("click");
        btn34.setKey(CommonButton.KEY_JOIN_US);

        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("个人服务");
        mainBtn1.setSub_button(new Button[]{btn11, btn12, btn13, btn14});

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("图书馆");
        mainBtn2.setSub_button(new Button[]{btn21, btn22, btn23, btn24});

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("关于我们");
        mainBtn3.setSub_button(new Button[]{btn31, btn32, btn33, btn34});

        Menu menu = new Menu();
        menu.setButton(new Button[]{mainBtn1, mainBtn2, mainBtn3});

        return menu;
    }
}
