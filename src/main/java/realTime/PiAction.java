package realTime;

import raspberryPi.Printer;

public abstract class PiAction implements Refreshable {

    private ActionState state;
    private long interval;
    private String description = null;

    public PiAction(long interval) {
        this.interval = interval;
    }

    public PiAction(long interval, TimeUnit tu){
        this.interval = calculateInterval(interval, tu);
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

    public ActionState getState() {
        return state;
    }

    public void setState(ActionState state) {
        this.state = state;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void deploy(){
        setState(ActionState.OPEN);
        Ticker ticker = new Ticker(interval,this);
        Thread th = new Thread(ticker);
        th.start();
    }

    @Override
    public boolean isPowered() {
        return state != ActionState.KILL;
    }

    public String toString(){
        return description != null
                ? getClass().getName()+" - "+description
                : getClass().getName();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
