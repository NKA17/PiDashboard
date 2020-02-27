package realTime;

public class Ticker implements Runnable {

    private int interval = 200;
    private Refreshable refreshable;

    public Ticker(int interval, Refreshable refreshable) {
        this.interval = interval;
        this.refreshable = refreshable;
    }

    @Override
    public void run() {
        while(refreshable.isPowered()){
            long start = System.currentTimeMillis();
            while(System.currentTimeMillis() - start < interval);

            refreshable.refresh();
        }
    }
}
