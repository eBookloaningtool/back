/**
 * Scheduled task component for managing book borrowing records.
 * Handles automated tasks related to book borrowing, including overdue reminders
 * and automatic returns for overdue books.
 */
package one.wcy.ebookloaningtool.llf.scheduler;

import one.wcy.ebookloaningtool.llf.service.impl.BorrowServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BorrowRecordScheduler {

    /**
     * Service implementation for handling book borrowing operations
     */
    @Autowired
    private BorrowServiceimpl borrowServiceimpl;

    /**
     * Scheduled task that runs daily at 8:30 AM to handle overdue books.
     * Performs two main functions:
     * 1. Sends reminders for books that will be due in 10 days
     * 2. Automatically processes returns for overdue books
     * 
     * Note: For testing purposes, there is a commented-out schedule that runs every minute
     */
    @Scheduled(cron = "0 30 8 * * ?")//execute every day at 8:30 AM
//    @Scheduled(cron = "0 * * * * *")//scan every minute, for testing
    public void scheduleOverdue(){
        borrowServiceimpl.overdueReminder(10);//reminder time is 10 days before overdue
        borrowServiceimpl.autoReturn();//auto return overdue books
    }
}
