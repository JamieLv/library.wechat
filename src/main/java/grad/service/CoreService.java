package grad.service;

import grad.message.resp.Article;
import grad.message.resp.NewsMessage;
import grad.message.resp.TextMessage;
import grad.util.MessageUtil;
import org.hibernate.boot.model.relational.Database;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jamie on 4/11/16.
 */
public class CoreService {

    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    public static String processRequest(HttpServletRequest request) {
        String respMessage = null;
        Database db = new Database();
        String currentTime;
        //获得当前时间
        //格式如下： 20160411-11:12:43
        Calendar calendar = Calendar.getInstance(); //获取当天日期
        SimpleDateFormat sdf = new SimpleDateFormat("20yyMMdd-HH:mm:ss");
        currentTime = sdf.format(calendar.getTime());

        try {
            // 默认返回的文本消息内容
            String respContent = "请求处理异常，请稍候尝试！";

            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 时间
            String createTime = requestMap.get("CreateTime");

            // 默认回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);

            //创建图文信息
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setFuncFlag(0);


            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                //拿到用户发来的信息 去除用户回复信息的前后空格
                String content = requestMap.get("Content").trim();

                List<Article> articleList = new ArrayList<Article>();

                //确认领取公益伞 => 改变公益伞名单中对应的人为再借转态, pure=false
                if ("借用".equals(content)) {
                    //v = \, p =\ (数据库中没有记录)
                    if (db.getGYS_Id(fromUserName) == 0) {
                        respContent = "是要借公益伞么~\n"
                                + "先点击左下角菜单中的公益伞哦！";
                    } else {
                        //v = 0, p= 1  未通过验证，还未借用(这里v=0，p肯定=1, 因为没通过验证肯定不可能借伞)
                        if (!db.GongYiSanIsVerified(fromUserName)) {
                            respContent = "你好，你的手机号码还没有通过验证哦！\n"
                                    + "请严格按照这个格式进行回复：gys 手机号码 姓名\n"
                                    + "60秒内将会收到有验证码的短信。\n"
                                    + "到时请将验证码回复给微信平台。";
                        } else {
                            // v = 1 , p = 1 通过验证，还未借用
                            if (db.GongYiSanIsPure(fromUserName)) {
                                try {
                                    db.updatePure(fromUserName, false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                GongYiSanEventHelper helper = GongYiSanEventHelper.getHelper();
                                helper.insertRecord(fromUserName, GongYiSanEvent.EVENT_TYPE_BORROW);
                                String date = new Date().toString();
                                respContent = "【恭喜您于" + date + "成功借用公益伞。】\n\n"
                                        + "请将此回复出示给阿姨作为借伞凭证就行啦 ^-^\n"
                                        + "---------\n"
                                        + "请及时归还至借伞处并扫描二维码确认还伞哦~";
                            } else {
                                // v = 1 , p = 0 已经借用 (不会出现 p = 0 ,v = 0的情况)
                                respContent = "同学您好，请及时归还至借伞处并扫描二维码确认还伞哦~";
                            }
                        }
                    }

                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("时刻表".equals(content)
                        || "班车".equals(content)
                        || "短驳车".equals(content)
                        || "同济班车".equals(content)
                        || "校车".equals(content)
                        || "北安跨".equals(content)
                        || "时间表".equals(content)
                        || "车".equals(content)) {
                    Article article = new Article();
                    article.setTitle("嘉定校区班车出行表");
                    article.setDescription("这里是最新的时刻表，包括：短驳车，北安跨及其增加班次，大润发，曹杨路高速车。");
                    article.setPicUrl("http://nickli-jdquanyi.daoapp.io/img/logo.png");
                    article.setUrl("http://nickli-jdquanyi.daoapp.io/Bus.jsp");
                    articleList.add(article);

                    // 设置图文消息个数
                    newsMessage.setArticleCount(articleList.size());
                    // 设置图文消息包含的图文集合  f
                    newsMessage.setArticles(articleList);
                    // 将图文消息对象转换成xml字符串
                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
                }

                // available during winter holiday (2016)
//                else if ("时刻表".equals(content)
//                        || "班车".equals(content)
//                        || "短驳车".equals(content)
//                        || "同济班车".equals(content)
//                        || "校车".equals(content)
//                        || "北安跨".equals(content)
//                        || "时间表".equals(content)
//                        || "车".equals(content)) {
//                    Article article = new Article();
//                    article.setTitle("嘉定校区班车寒假时刻表");
//                    article.setPicUrl(CDN + "nickli-jdquanyi.daoapp.io/img/logo.png");
//                    article.setUrl("http://nickli-jdquanyi.daoapp.io/WinterHolidayBusSchedule.html");
//                    articleList.add(article);
//                    // 设置图文消息个数
//                    newsMessage.setArticleCount(articleList.size());
//                    // 设置图文消息包含的图文集合  f
//                    newsMessage.setArticles(articleList);
//                    // 将图文消息对象转换成xml字符串
//                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
//                }

                else if ("校历".equals(content)) {
                    Article schoolCalResp = new Article();
                    schoolCalResp.setTitle("同济大学2015-2016学年第二学期校历");
                    schoolCalResp.setDescription("");
                    schoolCalResp.setPicUrl("http://7xs5vg.com1.z0.glb.clouddn.com/IMG_School_Canlendar_without_logo.png");
                    schoolCalResp.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5MTY1NjYyOA==&mid=402636731&idx=1&sn=c365d2ccbd4fa2f66bec6ff8d3c1fed3#rd");
                    List<Article> articles = new ArrayList<>();
                    articles.add(schoolCalResp);
                    newsMessage.setArticleCount(articles.size());
                    newsMessage.setArticles(articles);
                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
                }

                else if ("校医院".equals(content) || KEY_HOSPITAL.equals(content)) {
                    respContent = "开放科室：内科、口腔科、输液室、换药室、药房收费、化验、外科\n"
                            + "（注：外科门诊时间：\n"
                            + "上午 9：00 - 11：30 \n"
                            + "下午 12：30 - 17：00\n\n"
                            + "地点：篮球场旁"
                            + "每周三开设皮肤科,每周五开设内科专家门诊,"
                            + "其余时间段为急诊时间\n"
                            + "-----\n"
                            + "报销说明：每月第一、二周周三本部报销；第三周周三嘉定报销\n"
                            + "TIPS:不要忘记带一卡通哦~ ";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("浴室".equals(content) || KEY_BATHROOM.equals(content)) {
                    respContent = "开放时间：中午12:00至晚上10:00\n\n" +
                            "地点：春禾苑食堂西北侧";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("图书馆".equals(content) || KEY_LIBRARY.equals(content)) {
                    respContent = "开放时间：早上8:00至晚上10:00";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("食堂".equals(content) || KEY_DININGROOM.equals(content)) {
                    respContent = "正常营业中";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("工作时间".equals(content)) {
                    respContent = getSchoolDepartmentWorkday();
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("高尔夫球场".equals(content) || KEY_GOLF.equals(content)) {
                    respContent = "地点：图书馆后方；\n"
                            + "价格：对水池打：1盒球（30个）/15元\n"
                            + "     场地打：30元/小时\n"
                            + "     提供球杆；\n"
                            + "开放时间：8：:00-17:00（在不影响体育教学及协会活动的情况下）\n"
                            + "注：周末可能会因接待来宾不开放";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("健身房".equals(content) || KEY_GYM.equals(content)) {
                    respContent = "地点：济人楼（国会）一楼（进门右转）\n"
                            + "价格: 单次卡 20 元/ 次\n"
                            + "     月卡250 元/10 次\n"
                            + "     季卡400 元/20 次\n"
                            + "     学期卡 600元 不限次数\n"
                            + "     年卡 850 元 不限次数\n"
                            + "开放时间：11:30-22:00";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("乒乓球场".equals(content)
                        || KEY_PINGPONG.equals(content)
                        || "乒乓球".equals(content)
                        || "乒乓馆".equals(content )) {
                    respContent = "地点: 济人楼（国会）进门右转（健身房内）；仰望星空（中央天桥）；德楼（D楼）一楼、二楼\n"
                            + "开放时间：\n"
                            + "济人楼  周四17：30-21：30   周二、三、五15：30--21：30  \n"
                            + "双休日 13： 00-21：30；\n"
                            + "仰望星空 见通知，一般为8:00-20:00\n"
                            + "德楼 8：00-17：00（工作日）不影响体育教学前提下\n"
                            + "周二，周四中午为教师活动时间\n"
                            + "场地数量:济人楼 8-10 桌左右；仰望星空 4桌；德楼 7桌，二楼2桌\n";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("桌球房".equals(content) || KEY_TABLETENNIS.equals(content)) {
                    respContent = "地点: 同德楼（汽展中心）三楼\n"
                            + "价格：花式桌球 15元/小时，斯诺克18元/小时，有会员卡可优惠\n"
                            + "场地数量：花式桌球3桌，斯诺克1桌\n"
                            + "开放时间：17:00-22:00 （工作日）13:00-22:00 （周末）\n"
                            + "Tips:可以打电话预定哦，预定电话13764647792";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("网球场".equals(content)
                        || KEY_TENNIS.equals(content)
                        || "网球馆".equals(content)) {
                    respContent = "地点: 足球场旁，校医院北方\n"
                            + "开放时间：8：00-20：00 在不影响体育教学的前提下\n"
                            + "场地数量:4片"
                            + "Tips:买卡消费，需提前订场；价格与本部同";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("羽毛球场".equals(content)
                        || KEY_BADMINTON.equals(content)
                        || "羽毛球".equals(content)) {
                    respContent = "地点: 仰望星空（中央天桥）；德楼（D楼）一楼；同德楼（汽展中心）一楼\n"
                            + "------\n"
                            + "开放时间: \n"
                            + "仰望星空 见通知，一般为8:00-20:00\n"
                            + "周二，周四中午为教师活动时间\n"
                            + "同德楼 见通知，一般为8:00-20:00\n(需要提前在健身馆预约)"
                            + "------\n"
                            + "场地数量:仰望星空 4片；德楼 3片；同德楼 4片";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else if ("虹桥班车".equals(content)) {
                    List<Article> articles = new ArrayList<>();
                    Article interviewReplies = new Article();
                    interviewReplies.setTitle("嘉定校区-虹桥枢纽专线时刻表");
                    interviewReplies.setPicUrl("http://7xs5vg.com1.z0.glb.clouddn.com/IMG_jiading-hongqiao-logo.jpg");
                    interviewReplies.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5MTY1NjYyOA==&mid=402764963&idx=1&sn=29fb66dc36d116fb1d5ac35b691ab6d9#rd");
                    articles.add(interviewReplies);
                    newsMessage.setArticleCount(articles.size());
                    newsMessage.setArticles(articles);
                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
                }

                else if ("走访".equals(content)) {
                    List<Article> articles = new ArrayList<>();
                    Article interviewReplies = new Article();
                    interviewReplies.setTitle("我们的走访回复都在这里啦~");
                    interviewReplies.setPicUrl("http://7xs5vg.com1.z0.glb.clouddn.com/IMG_quanyi.jpg");
                    interviewReplies.setUrl("http://mp.weixin.qq.com/mp/homepage?__biz=MjM5MTY1NjYyOA==&hid=2&sn=ac06ad652f1b5f2d094050be91192c10#wechat_redirect");
                    articles.add(interviewReplies);
                    newsMessage.setArticleCount(articles.size());
                    newsMessage.setArticles(articles);
                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
                }

                //fxfx 1252855 胡星 软件学院
                else if (content.startsWith("fxfx")) {

                    String[] keywords = content.trim().split("\\s+");
                    if (keywords.length == 4) {
                        String studentNumber = keywords[1];
                        String name = keywords[2];
                        String academy = keywords[3];
                        FX_FuXiu fx_user = new FX_FuXiu();
                        if (!db.FuXiuExists(fromUserName)) {
                            fx_user.setFx_fromUserName(fromUserName);
                            fx_user.setFx_description("New Add");
                            fx_user.setFx_studentNumber(studentNumber);
                            fx_user.setFx_academy(academy);
                            fx_user.setFx_name(name);
                            db.AddFuxiu(fx_user);
                            respContent = "恭喜你，辅修车票登记成功！" + "\n" + content;
                            textMessage.setContent(respContent);
                            respMessage = MessageUtil.textMessageToXml(textMessage);

                        } else {
                            respContent = "你已经登记过了哦！";
                            textMessage.setContent(respContent);
                            respMessage = MessageUtil.textMessageToXml(textMessage);

                        }
                    } else {
                        respContent = "你要登记辅修吧？格式不对哦！~" + "\n" + content;
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);

                    }

                }

                //gys 13122363681 胡星
                else if (content.startsWith("gys")) {

                    String[] keywords = content.trim().split("\\s+");
                    if (keywords.length == 3) {
                        String telephone = keywords[1];
                        String name = keywords[2];

                        int tag = db.GongYiSanExists(fromUserName);
                        //没有登记过公益伞
                        if (tag == 0) {
                            GYS_GongYiSan gys_user = new GYS_GongYiSan();
                            gys_user.setGYS_name(name);
                            gys_user.setGYS_telephone(telephone);
                            gys_user.setGYS_fromUserName(fromUserName);
                            gys_user.setGYS_pure(true);
                            gys_user.setGYS_verification(false);
                            db.AddGongYiSan(gys_user);
                            int id = db.getGYS_Id(fromUserName);
                            SendMsg_webchinese sendMsg = new SendMsg_webchinese();
                            sendMsg.send(telephone, id);
                            respContent = "请输入您手机短信收到的验证码，如：yzm 12";
                            textMessage.setContent(respContent);
                            respMessage = MessageUtil.textMessageToXml(textMessage);
                            //已登记，手机号码验证未通过
                        } else if (tag == 1) {
                            db.updateTelephone(fromUserName, telephone);
                            int id = db.getGYS_Id(fromUserName);
                            SendMsg_webchinese sendMsg = new SendMsg_webchinese();
                            sendMsg.send(telephone, id);
                            respContent = "请输入您手机短信收到的验证码，如：yzm 12";
                            textMessage.setContent(respContent);
                            respMessage = MessageUtil.textMessageToXml(textMessage);

                        } //已登记，手机号码验证已通过
                        else {
                            respContent = "你已经登记过了哦！";
                            textMessage.setContent(respContent);
                            respMessage = MessageUtil.textMessageToXml(textMessage);
                        }
                        //gys开头 但是格式不对
                    } else {
                        respContent = "你要登记注册公益伞信息吧？格式不对哦！~\n"
                                + "请严格按照：gys 手机号码 姓名 \n"
                                + "的格式进行回复哦！";
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);

                    }
                }

                //yzm 23
                else if (content.startsWith("yzm")) {

                    String[] keywords = content.trim().split("\\s+");
                    if (keywords.length == 2) {
                        String str_id = keywords[1];
                        int i_id = Integer.parseInt(str_id);

                        int tag = db.GongYiSanExists(fromUserName);
                        //没有登记过公益伞
                        if (tag == 0) {
                            respContent = "第一次使用公益伞？赶快来验证您的手机号码吧！\n"
                                    + "请严格按照这个格式进行回复：gys 手机号码 姓名\n"
                                    + "60秒内将会收到有验证码的短信。\n"
                                    + "到时请将验证码回复给微信平台。";
                            textMessage.setContent(respContent);
                            respMessage = MessageUtil.textMessageToXml(textMessage);
                            //已登记，手机号码验证未通过
                        } else if (tag == 1) {
                            //一致
                            if (i_id == db.getGYS_Id(fromUserName)) {
                                db.updateVerification(fromUserName, true);
                                respContent = "恭喜你验证成功！可以点击菜单中的【公益伞】领取公益伞!";
                                textMessage.setContent(respContent);
                                respMessage = MessageUtil.textMessageToXml(textMessage);
                            } else {
                                respContent = "对不起，验证码不一致！\n"
                                        + "请再次严格按照这个格式进行回复：gys 手机号码 姓名\n"
                                        + "60秒内将会收到有验证码的短信。\n"
                                        + "到时请将验证码回复给微信平台。";;
                                textMessage.setContent(respContent);
                                respMessage = MessageUtil.textMessageToXml(textMessage);
                            }
                        } //已登记，手机号码验证已通过
                        else {
                            respContent = "你的手机号码之前已经通过验证了哦！";
                            textMessage.setContent(respContent);
                            respMessage = MessageUtil.textMessageToXml(textMessage);
                        }

                    } else {
                        respContent = "验证码格式错误";
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                    }
                }

                //ts 18号楼外的小青蛙好吵吖!能不能解决下~
                else if (content.startsWith("ts")) {
                    //存问题
                    MQ_MyQuestions mq = new MQ_MyQuestions();
                    mq.setFromUserName(fromUserName);
                    mq.setPutTime(currentTime);
                    mq.setDescription(content.substring(3));
                    mq.setReply("权小益走访中...");
                    mq.setTag(false);
                    db.AddQuestion(mq);
                    respContent = "问题已经受理，静待处理。\n你可以点开【投诉追踪】，查看问题进度。";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }

                else {
                    respContent = getGreeting() + "\n你的权益，我最在意！\n"
                            + "你的问题我们已经收到，会尽快回复。" +
                            "查看班车时刻表，请直接回复“时刻表”\n" +
                            "查看校内机构工作时间，请直接回复“工作时间”\n" +
                            "谢谢。";

                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);

                    ReplyGapHelper helper = ReplyGapHelper.getHelper();
                    ReplyGap r = helper.getSingleRecord(fromUserName);
                    if (r != null) {
                        int lastReplyUnixTime = r.getLastReplyTime();
                        if (3600 >= Integer.parseInt(createTime) - lastReplyUnixTime) {
                            respMessage = "";
                        }
                        helper.updateSingleRecord(r);
                    }
                }
            } // 图片消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                respContent = "我喜欢你发的图片！";
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            } // 地理位置消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                respContent = "同学你发的是地理位置哦！";
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            } // 链接消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "同学你发送的是链接消息哦";
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            } // 音频消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "同学你发送的是音频消息哦！";
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            } // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    respContent
                            = getGreeting() + "，朋友:)\n"
                            + "感谢关注嘉定权益\n"
                            + "我们的微信一直在不断完善，同时您的支持给了我们更多的动力！"
                            + "并希望您能对我们的微信平台和日常工作提出宝贵意见与建议。\n";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);

                    SubscriberHelper helper = SubscriberHelper.getHelper();
                    Subscriber subscriber = helper.getSingleRecord(fromUserName);
                    if (subscriber == null) {
                        helper.insertSingleRecord(fromUserName);
                    } else {
                        subscriber.setValidate(Subscriber.VALID);
                        helper.updateSingleRecord(subscriber);
                    }

                } // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    SubscriberHelper helper = SubscriberHelper.getHelper();
                    Subscriber subscriber = helper.getSingleRecord(fromUserName);
                    if (subscriber != null) {
                        subscriber.setValidate(Subscriber.INVALID);
                        helper.updateSingleRecord(subscriber);
                    }
                } // 自定义菜单点击事件
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    // 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    String eventKey = requestMap.get("EventKey");
                    if (eventKey.equals(CommonButton.KEY_BUS)) {
                        db.updateMenuClickCount(11);
                        List<Article> articleList1 = new ArrayList<Article>();
                        List<String> nextBusTimes = new BusTime().getNextBuses();

                        Article articleSKB = new Article();
                        articleSKB.setTitle("嘉定校区班车出行表");
                        articleSKB.setDescription("这里是最新的时刻表，包括：短驳车，北安跨及其增加班次，大润发，曹杨路高速车。");
                        articleSKB.setPicUrl("http://7xs5vg.com1.z0.glb.clouddn.com/IMG_logo.png");
                        articleSKB.setUrl("http://nickli-jdquanyi.daoapp.io/Bus.jsp");

                        Article articleT = new Article();
                        articleT.setTitle(emoji(0x1F551) + "离你最近的一班车");

                        Article article = new Article();
                        article.setTitle(emoji(0x1F68C) + "【短驳车】");

                        Article article0 = new Article();
                        article0.setTitle(emoji(0x1F51C) + "汽车城站      " + nextBusTimes.get(0));

                        Article article1 = new Article();
                        article1.setTitle(emoji(0x1F51C) + "嘉定校区      " + nextBusTimes.get(1));

                        Article articleDA = new Article();
                        articleDA.setTitle(emoji(0x1F68C) + "【大润发】");

                        Article article2 = new Article();
                        article2.setTitle(emoji(0x1F51C) + "安亭大润发    " + nextBusTimes.get(2));

                        Article article3 = new Article();
                        article3.setTitle(emoji(0x1F51C) + "学校正门口    " + nextBusTimes.get(3));

                        Article articleGS = new Article();
                        articleGS.setTitle(emoji(0x1F68C) + "【高速车】(7号楼前上车)\n" + nextBusTimes.get(4));

//                                Article article4 = new Article();
//                                article4.setTitle(emoji(0x1F51C) + "曹杨路地铁站    " + nextBusTimes.get(4));
//                                article4.setDescription("");

//                                Article busBook = new Article();
//                                busBook.setTitle(emoji(0x1F68C) + "\n点我预定校区班车\n点最上方图片查看完整寒假时刻表");
//                                busBook.setUrl("http://jiading.tongji.edu.cn:8080/TJbus");


                        articleList1.add(articleSKB);
                        articleList1.add(articleT);
                        articleList1.add(article);
                        articleList1.add(article0);
                        articleList1.add(article1);
                        articleList1.add(articleDA);
                        articleList1.add(article2);
                        articleList1.add(article3);
                        articleList1.add(articleGS);
//                                articleList1.add(busBook);

                        // 设置图文消息个数
                        newsMessage.setArticleCount(articleList1.size());
                        // 设置图文消息包含的图文集合  f
                        newsMessage.setArticles(articleList1);
                        // 将图文消息对象转换成xml字符串
                        respMessage = MessageUtil.newsMessageToXml(newsMessage);

                    } else if (eventKey.equals(CommonButton.KEY_BORROW_UMBRELLA)) {//公益伞
                        db.updateMenuClickCount(12);
                        int tag = db.GongYiSanExists(fromUserName);
                        //v=?，没有登记
                        if (tag == 0) {
                            respContent = "公益伞功能现已全面更新!\n"
                                    + "想要开通公益伞功能？赶快来验证您的手机号码吧！\n"
                                    + "请严格按照这个格式进行回复：gys 手机号码 姓名\n"
                                    + "60秒内将会收到有验证码的短信。\n"
                                    + "到时请将验证码回复给微信平台。";
                        } //v=0，已登记，手机号码没有通过验证
                        else if (tag == 1) {
                            respContent = "你手机号码还没有通过验证哦！"
                                    + "请尽快并且严格按照这个格式进行回复：gys 手机号码 姓名\n"
                                    + "60秒内将会收到有验证码的短信。\n"
                                    + "到时请将验证码回复给微信平台。";
                        } //v=1,p=1手机号码通过验证了，没有借伞
                        else if (db.GongYiSanIsPure(fromUserName)) {
                            respContent = "同学您好，欢迎您在B楼、F楼、济事楼借用公益伞。请问您是否确认要"
                                    + "借取公益伞？确认请回复：借用。";
                        } else {
                            respContent = "同学您好，请及时归还至借伞处并扫描二维码确认还伞哦~"; //v=1, p=0已经借出了
                        }
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);

                    } else if (eventKey.equals(CommonButton.KEY_SIDE_BUS)) {
                        db.updateMenuClickCount(13);
                        if (db.FuXiuExists(fromUserName)) {
                            respContent = "本周六辅修班车正常发车！\n" +
                                    "恭喜你拿到第" + db.getWC_count() + "周的辅修车票。 ^-^"
                                    + "\n 这是你辅修车票的电子凭证。\n";
                            db.updateFX_count(fromUserName);
                            textMessage.setContent(respContent);
                            respMessage = MessageUtil.textMessageToXml(textMessage);
                        } else {
                            respContent = "对不起，同学你不在辅修车票名单中 ~~.\n 辅修登记请按如下格式回复\n fxfx 学号 姓名 学院\n字段之间加请空格";
                            textMessage.setContent(respContent);
                            respMessage = MessageUtil.textMessageToXml(textMessage);
                        }
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);

                    } else if (eventKey.equals(CommonButton.KEY_WORKTIME)) {
                        db.updateMenuClickCount(21);
                        respContent = getSchoolDepartmentWorkday();
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                    } else if (eventKey.equals(CommonButton.KEY_CONTACTS)) {
                        db.updateMenuClickCount(22);
                        respContent = getSchoolDepartmentNumber();
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                    }  else if (eventKey.equals(CommonButton.KEY_SUE)) {//我要投诉
                        db.updateMenuClickCount(31);
                        respContent = "同学，你好哇。\n"
                                + "如果想要投诉，请按照下面例子格式：\n"
                                + "ts 18号楼外的小青蛙好吵吖！能不能解决下?"
                                + "\n---------------"
                                + "\n点击【投诉追踪】即可及时查看自己问题的走访情况。";

                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                    } else if (eventKey.equals(CommonButton.KEY_SUE_TRACK)) {
                        db.updateMenuClickCount(32);
                        List<MQ_MyQuestions> mq_list = db.GetMyQuestions(fromUserName);
                        if (mq_list.isEmpty()) {
                            respContent = "你过得很舒服，没有投诉问题哦。";
                        }else {

                            respContent = "你投诉的问题有：\n";
                            int i = 1;
                            for (MQ_MyQuestions mq : mq_list) {
                                respContent += i + ". " + mq.getDescription() + "\n回复：" + mq.getReply() + "\n";
                                i ++;
                            }
                        }
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                    } else if (eventKey.equals(CommonButton.KEY_JOIN_US)) {
                        db.updateMenuClickCount(34);
                        respContent
                                = "-你是否也想和学校部门进行更多的沟通？\n"
                                + "-你是否也想为同学们的生活提供更多福利？\n"
                                + "-你是否也想举办饮食文化节女生节等等活动？\n"
                                + "~~快来加入我们吧！\n"
                                + "请在微信下留言：【报名权益】姓名 学号 手机号\n"
                                + "我们会尽快与你联系。！";
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                    }
                } else if (eventType.equals(MessageUtil.EVENT_TYPE_SCANCODE_WAITMSG)) {
                    String scanResult = requestMap.get("ScanResult");
                    if (scanResult.equals(MessageUtil.RETURN_UMBRELLA)) {
                        if (db.GongYiSanExists(fromUserName) == 2 && !db.GongYiSanIsPure(fromUserName)) {
                            if (db.updatePure(fromUserName, true)) {
                                GongYiSanEventHelper helper = GongYiSanEventHelper.getHelper();
                                helper.insertRecord(fromUserName, GongYiSanEvent.EVENT_TYPE_RETURN);
                                textMessage.setContent("还伞成功！");
                            }
                        } else {
                            textMessage.setContent("同学你还没通过验证或者没借伞哦~");
                        }
                    } else {
                        textMessage.setContent("还伞失败，二维码不正确哦~");
                    }
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }

    /**
     * emoji表情转换(hex -> utf-16)
     *
     * @param hexEmoji
     * @return
     */
    public static String emoji(int hexEmoji) {
        return String.valueOf(Character.toChars(hexEmoji));
    }

}
