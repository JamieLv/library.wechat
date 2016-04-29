package grad.servlet;

import grad.database.Borrow_Record;
import grad.database.Database;
import grad.database.Member_Info;
import grad.service.BorrowReminder;
import grad.util.WeixinUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jamie on 4/29/16.
 */
public class ReminderServlet implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<Member_Info> member_infoList = Database.getAllMember_Info();
                for (Member_Info member_info: member_infoList) {
                    try {
                        BorrowReminder.BorrowReminderTemplate(member_info.getMember_fromUserName());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 1, TimeUnit.DAYS);
    }
}
