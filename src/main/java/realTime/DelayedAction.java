package realTime;

public abstract class DelayedAction extends PiAction {


    public DelayedAction(long delay) {
        super(delay);
    }

    public DelayedAction(long interval, TimeUnit tu) {
        super(interval, tu);
    }

    public abstract void action();

    @Override
    public void refresh() {
        if(isPowered()){
            action();
            setState(ActionState.KILL);
        }
    }
}
