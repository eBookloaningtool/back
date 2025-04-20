package one.wcy.ebookloaningtool.llf.scheduler;

import one.wcy.ebookloaningtool.llf.service.impl.BorrowServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BorrowRecordScheduler {

    @Autowired
    private BorrowServiceimpl borrowServiceimpl;

    @Scheduled(cron = "0 30 8 * * ?")//每天早上8点半执行
    public void scheduleOverdueReminder(){
        borrowServiceimpl.overdueReiminder(10);//提醒时间为逾期的十天前
    }
}
