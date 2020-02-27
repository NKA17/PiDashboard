package realTime;

public abstract class PiAction implements Refreshable {

    private ActionState state;
    private int interval;

    public PiAction(int interval) {
        this.interval = interval;
    }

    public ActionState getState() {
        return state;
    }

    public void setState(ActionState state) {
        this.state = state;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void deploy(){
        Ticker ticker = new Ticker(interval,this);
        Thread th = new Thread(ticker);
        th.start();
    }

    @Override
    public boolean isPowered() {
        return state != ActionState.KILL;
    }
}
