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

        String appId = "wxe13392d6482304c4";
        String appSecret = "41b6c04d9ac9819c779a186e0d6908ab";


        // 调用接口获取access_token
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

        if (at != null) {
            // 调用接口创建菜单
            int create = WeixinUtil.createMenu(getMenu(), at.getToken());
            int addEn = WeixinUtil.AddConditionalMenu(getMenuEn(), at.getToken());
            int addMember = WeixinUtil.AddConditionalMenu(getMemberMenu(), at.getToken());
            int addMemberEn = WeixinUtil.AddConditionalMenu(getMembemrMenuEn(), at.getToken());
            int addWorker = WeixinUtil.AddConditionalMenu(getWorkerMenu(), at.getToken());
            int addWorkerEn = WeixinUtil.AddConditionalMenu(getWorkerMenuEn(), at.getToken());

            // 判断菜单创建结果
            if (create == 0) {
                System.out.println("菜单创建成功！\n" + at.getToken());
            } else {
                System.out.println("菜单创建失败，错误码：" + create);
            }
        }
    }

    /**
     * 组装菜单数据
     *
     * @return
     */
    // 中文 默认
    private static Menu getMenu() {
        int l = 0;

        CommonButton btn11 = new CommonButton();
        btn11.setName(l == 0 ? "用户绑定" : "Login");
        btn11.setType("click");
        btn11.setKey(CommonButton.KEY_LOGIN);

        CommonButton btn21 = new CommonButton();
        btn21.setName(l == 0 ? "图书检索" : "Search Book");
        btn21.setType("click");
        btn21.setKey(CommonButton.KEY_BOOK);

        CommonButton btn24 = new CommonButton();
        btn24.setName(l == 0 ? "门店查询" : "Nearby Library");
        btn24.setType("location_select");
        btn24.setKey(CommonButton.KEY_NEARBY);

        CommonButton btn33 = new CommonButton();
        btn33.setName(l == 0 ? "官网" : "Official Website");
        btn33.setType("view");
        btn33.setUrl("http://sse.tongji.edu.cn/zh");

        CommonButton btn34 = new CommonButton();
        btn34.setName(l == 0 ? "加入我们" : "Join Us");
        btn34.setType("click");
        btn34.setKey(CommonButton.KEY_JOIN_US);

        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName(l == 0 ? "个人服务" : "User");
        mainBtn1.setSub_button(new Button[]{btn11});

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName(l == 0 ? "图书馆" : "Library");
        mainBtn2.setSub_button(new Button[]{btn21, btn24});

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName(l == 0 ? "关于我们" : "About");
        mainBtn3.setSub_button(new Button[]{btn33, btn34});

        Menu menu = new Menu();
        menu.setButton(new Button[]{mainBtn1, mainBtn2, mainBtn3});

        return menu;
    }
    // 英文 默认
    private static Menu getMenuEn() {
        int l = 1;

        CommonButton btn11 = new CommonButton();
        btn11.setName(l == 0 ? "用户绑定" : "Login");
        btn11.setType("click");
        btn11.setKey(CommonButton.KEY_LOGIN);

        CommonButton btn21 = new CommonButton();
        btn21.setName(l == 0 ? "图书检索" : "Search Book");
        btn21.setType("click");
        btn21.setKey(CommonButton.KEY_BOOK);

        CommonButton btn24 = new CommonButton();
        btn24.setName(l == 0 ? "门店查询" : "Nearby Library");
        btn24.setType("location_select");
        btn24.setKey(CommonButton.KEY_NEARBY);

        CommonButton btn33 = new CommonButton();
        btn33.setName(l == 0 ? "官网" : "Official Website");
        btn33.setType("view");
        btn33.setUrl("http://sse.tongji.edu.cn/zh");

        CommonButton btn34 = new CommonButton();
        btn34.setName(l == 0 ? "加入我们" : "Join Us");
        btn34.setType("click");
        btn34.setKey(CommonButton.KEY_JOIN_US);

        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName(l == 0 ? "个人服务" : "User");
        mainBtn1.setSub_button(new Button[]{btn11});

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName(l == 0 ? "图书馆" : "Library");
        mainBtn2.setSub_button(new Button[]{btn21, btn24});

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName(l == 0 ? "关于我们" : "About");
        mainBtn3.setSub_button(new Button[]{btn33, btn34});

        Menu menu = new Menu();
        menu.setButton(new Button[]{mainBtn1, mainBtn2, mainBtn3});
        menu.setMatchrule(new MatchRule("en"));

        return menu;
    }
    // 中文 读者
    private static Menu getMemberMenu() {
        int l = 0;

        CommonButton btn11 = new CommonButton();
        btn11.setName(l == 0 ? "会员卡" : "Member");
        btn11.setType("click");
        btn11.setKey(CommonButton.KEY_MEMBERSHIP);

        CommonButton btn12 = new CommonButton();
        btn12.setName(l == 0 ? "我要借书" : "Borrow");
        btn12.setType("scancode_waitmsg");
        btn12.setKey(CommonButton.KEY_BORROW_BOOK);

        CommonButton btn14 = new CommonButton();
        btn14.setName(l == 0 ? "记录查询" : "Record");
        btn14.setType("click");
        btn14.setKey(CommonButton.KEY_RECORD);

        CommonButton btn15 = new CommonButton();
        btn15.setName(l == 0 ? "退出登录" : "Log off");
        btn15.setType("click");
        btn15.setKey(CommonButton.KEY_LOG_OFF);

        CommonButton btn21 = new CommonButton();
        btn21.setName(l == 0 ? "图书检索" : "Search Book");
        btn21.setType("click");
        btn21.setKey(CommonButton.KEY_BOOK);

        CommonButton btn22 = new CommonButton();
        btn22.setName(l == 0 ? "阅览室预约" : "Reading Room");
        btn22.setType("click");
        btn22.setKey(CommonButton.KEY_RESERVE_ROOM);

        CommonButton btn23 = new CommonButton();
        btn23.setName(l == 0 ? "图书推荐" : "Recommendation");
        btn23.setType("click");
        btn23.setKey(CommonButton.KEY_BOOK_RECOMMEND);

        CommonButton btn24 = new CommonButton();
        btn24.setName(l == 0 ? "门店查询" : "Nearby Library");
        btn24.setType("location_select");
        btn24.setKey(CommonButton.KEY_NEARBY);

        CommonButton btn31 = new CommonButton();
        btn31.setName(l == 0 ? "我的建议" : "Advice");
        btn31.setType("click");
        btn31.setKey(CommonButton.KEY_ADVICE);

        CommonButton btn32 = new CommonButton();
        btn32.setName("意见追踪");
        btn32.setType("click");
        btn32.setKey(CommonButton.KEY_ADVICE_TRACK);

        CommonButton btn33 = new CommonButton();
        btn33.setName(l == 0 ? "官网" : "Official Website");
        btn33.setType("view");
        btn33.setUrl("http://sse.tongji.edu.cn/zh");

        CommonButton btn34 = new CommonButton();
        btn34.setName(l == 0 ? "加入我们" : "Join Us");
        btn34.setType("click");
        btn34.setKey(CommonButton.KEY_JOIN_US);

        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName(l == 0 ? "个人服务" : "Personal");
        mainBtn1.setSub_button(new Button[]{btn11, btn12, btn14, btn15});

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName(l == 0 ? "图书馆" : "Library");
        mainBtn2.setSub_button(new Button[]{btn21, btn22, btn23, btn24});

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName(l == 0 ? "关于我们" : "About");
        mainBtn3.setSub_button(new Button[]{btn31, btn33, btn34});

        Menu menu = new Menu();
        menu.setButton(new Button[]{mainBtn1, mainBtn2, mainBtn3});
        menu.setMatchrule(new MatchRule(100, "zh_CN"));

        return menu;
    }
    // 英文 读者
    private static Menu getMembemrMenuEn() {
        int l = 1;

        CommonButton btn11 = new CommonButton();
        btn11.setName(l == 0 ? "会员卡" : "Member");
        btn11.setType("click");
        btn11.setKey(CommonButton.KEY_MEMBERSHIP);

        CommonButton btn12 = new CommonButton();
        btn12.setName(l == 0 ? "我要借书" : "Borrow");
        btn12.setType("scancode_waitmsg");
        btn12.setKey(CommonButton.KEY_BORROW_BOOK);

        CommonButton btn14 = new CommonButton();
        btn14.setName(l == 0 ? "记录查询" : "Record");
        btn14.setType("click");
        btn14.setKey(CommonButton.KEY_RECORD);

        CommonButton btn15 = new CommonButton();
        btn15.setName(l == 0 ? "退出登录" : "Log off");
        btn15.setType("click");
        btn15.setKey(CommonButton.KEY_LOG_OFF);

        CommonButton btn21 = new CommonButton();
        btn21.setName(l == 0 ? "图书检索" : "Search Book");
        btn21.setType("click");
        btn21.setKey(CommonButton.KEY_BOOK);

        CommonButton btn22 = new CommonButton();
        btn22.setName(l == 0 ? "阅览室预约" : "Reading Room");
        btn22.setType("click");
        btn22.setKey(CommonButton.KEY_RESERVE_ROOM);

        CommonButton btn23 = new CommonButton();
        btn23.setName(l == 0 ? "图书推荐" : "Recommendation");
        btn23.setType("click");
        btn23.setKey(CommonButton.KEY_BOOK_RECOMMEND);

        CommonButton btn24 = new CommonButton();
        btn24.setName(l == 0 ? "门店查询" : "Nearby Library");
        btn24.setType("location_select");
        btn24.setKey(CommonButton.KEY_NEARBY);

        CommonButton btn31 = new CommonButton();
        btn31.setName(l == 0 ? "我的建议" : "Advice");
        btn31.setType("click");
        btn31.setKey(CommonButton.KEY_ADVICE);

        CommonButton btn32 = new CommonButton();
        btn32.setName("意见追踪");
        btn32.setType("click");
        btn32.setKey(CommonButton.KEY_ADVICE_TRACK);

        CommonButton btn33 = new CommonButton();
        btn33.setName(l == 0 ? "官网" : "Official Website");
        btn33.setType("view");
        btn33.setUrl("http://sse.tongji.edu.cn/zh");

        CommonButton btn34 = new CommonButton();
        btn34.setName(l == 0 ? "加入我们" : "Join Us");
        btn34.setType("click");
        btn34.setKey(CommonButton.KEY_JOIN_US);

        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName(l == 0 ? "个人服务" : "Personal");
        mainBtn1.setSub_button(new Button[]{btn11, btn12, btn14, btn15});

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName(l == 0 ? "图书馆" : "Library");
        mainBtn2.setSub_button(new Button[]{btn21, btn22, btn23, btn24});

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName(l == 0 ? "关于我们" : "About");
        mainBtn3.setSub_button(new Button[]{btn31, btn33, btn34});

        Menu menu = new Menu();
        menu.setButton(new Button[]{mainBtn1, mainBtn2, mainBtn3});
        menu.setMatchrule(new MatchRule(100, "en"));

        return menu;
    }
    // 中文 员工
    private static Menu getWorkerMenu() {
        int l = 0;

        CommonButton btn13 = new CommonButton();
        btn13.setName(l == 0 ? "还书" : "Return");
        btn13.setType("scancode_waitmsg");
        btn13.setKey(CommonButton.KEY_RETURN_BOOK);

        Menu menu = new Menu();
        menu.setButton(new Button[]{btn13});
        menu.setMatchrule(new MatchRule(101, "zh_CN"));

        return menu;
    }
    // 英文 员工
    private static Menu getWorkerMenuEn() {
        int l = 1;

        CommonButton btn13 = new CommonButton();
        btn13.setName(l == 0 ? "还书" : "Return");
        btn13.setType("scancode_waitmsg");
        btn13.setKey(CommonButton.KEY_RETURN_BOOK);

        Menu menu = new Menu();
        menu.setButton(new Button[]{btn13});
        menu.setMatchrule(new MatchRule(101, "en"));

        return menu;
    }
}
