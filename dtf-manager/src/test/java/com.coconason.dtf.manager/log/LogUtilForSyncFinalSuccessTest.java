package com.dtf.manager.log;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogUtilForSyncFinalSuccessTest {

    @Test
    public void assertAppendAndGet() {
        LogUtilForSyncFinalSuccess logUtil = LogUtilForSyncFinalSuccess.getInstance();
        logUtil.append("first log for test message");
        logUtil.append("second log for test message");
        String result1 = logUtil.getMessage(0,"first log for test message".length());
        String result2 = logUtil.getMessage(2048,"second log for test message".length());
        assertThat(result1, is("first log for test message"));
        assertThat(result2, is("second log for test message"));
    }

    @Test
    public void assertGetInstanceTwice() {
        LogUtilForSyncFinalSuccess logUtil1 = LogUtilForSyncFinalSuccess.getInstance();
        LogUtilForSyncFinalSuccess logUtil2 = LogUtilForSyncFinalSuccess.getInstance();
        assertThat(logUtil1, is(logUtil2));
    }

    @Test
    public void assertAppendWithMultiThread() {
        LogUtilForSyncFinalSuccess logUtil = LogUtilForSyncFinalSuccess.getInstance();
        ExecutorService executorService =  Executors.newCachedThreadPool();
        for(int i = 0; i < 50; i++) {
            executorService.execute(new LogRunnable(logUtil));
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = logUtil.getMessage(2048*50*100,UUID.randomUUID().toString().length());
        assertThat(result.length(), is(UUID.randomUUID().toString().length()));
    }

    private class LogRunnable implements Runnable {

        private LogUtilForSyncFinalSuccess logUtil;

        LogRunnable(LogUtilForSyncFinalSuccess logUtil) {
            this.logUtil = logUtil;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                logUtil.append(UUID.randomUUID().toString());
            }
        }

    }
}
