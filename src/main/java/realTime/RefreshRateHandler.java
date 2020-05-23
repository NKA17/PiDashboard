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

    public static long calculateInterval(long interval, String tu){
        switch (tu.toLowerCase()){
            case "hours":
                return calculateInterval(interval, TimeUnit.HOURS);
            case "minutes":
                return calculateInterval(interval, TimeUnit.MINUTES);
            case "seconds":
                return calculateInterval(interval, TimeUnit.SECONDS);
            default:
                return calculateInterval(interval, TimeUnit.MILLISECONDS);
        }
    }
    public static long calculateInterval(long interval, TimeUnit tu){
        switch (tu){
            case HOURS:
                interval *= 60;
            case MINUTES:
                interval *= 60;
            case SECONDS:
                interval *= 1000;
        }

        return interval;
    }
}
