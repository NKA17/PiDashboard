package realTime;

public class RefreshRateHandler {
    private long interval = 0;
    private long start;
    private boolean play = false;

    public RefreshRateHandler(long interval) {
        this.interval = interval;
    }

    public boolean isTimeToRefresh(){
        long now = System.currentTimeMillis();
        long delta = now - start;
        boolean ok = delta > interval;
        if(ok) {
            start = now;
        }
        return play && ok;
    }

    public void play(){
        play = true;
    }
    public void pause(){
        play = false;
    }
}
