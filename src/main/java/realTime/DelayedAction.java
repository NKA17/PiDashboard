package realTime;

public abstract class DelayedAction extends PiAction {


    public DelayedAction(int delay) {
        super(delay);
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
