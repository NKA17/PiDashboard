package realTime;

public class Ticker implements Runnable {

    private long interval = 200;
    private Refreshable refreshable;
    static int i = 0;
    int m = 0;

    public Ticker(long interval, Refreshable refreshable) {
        this.interval = interval;
        this.refreshable = refreshable;
        m = i++;
    }

    public Ticker(long interval, TimeUnit tu, Refreshable refreshable) {
        this.interval = calculateInterval(interval,tu);
        this.refreshable = refreshable;
    }

    protected long calculateInterval(long interval, TimeUnit tu){
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

    @Override
    public void run() {
        while(refreshable.isPowered()){
            long start = System.currentTimeMillis();
            while(System.currentTimeMillis() - start < interval);

            //if(m>0)
            //System.out.println("Ticker "+m);
            refreshable.refresh();
        }


        //System.out.println("Ticker "+m+" died.");
    }
}
