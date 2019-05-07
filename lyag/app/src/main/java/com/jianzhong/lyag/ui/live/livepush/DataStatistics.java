package com.jianzhong.lyag.ui.live.livepush;

/**
 * Created by liujianghao on 16-6-15.
 */
public class DataStatistics implements Runnable {
    private Thread mStatisticThread;
    private ReportListener mReportListener = null;
    private long mInterval;

    public DataStatistics(long interval) {
        mInterval = interval;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if(mReportListener != null) {
                    mReportListener.onInfoReport();
                }
                Thread.sleep(mInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void setReportListener(ReportListener listener) {
        this.mReportListener = listener;
    }

    public void start() {
        mStatisticThread = new Thread(this);
        mStatisticThread.start();
    }

    public void stop() {
        mReportListener = null;
        mStatisticThread.interrupt();

        try {
            mStatisticThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public interface ReportListener {
        void onInfoReport();
    }

}
