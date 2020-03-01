package realTime;

public abstract class ImmediateAction extends PiAction {

    public ImmediateAction(){
        super(0);
    }

    public abstract void action();

    @Override
    public void refresh() {
        action();
        setState(ActionState.KILL);
    }
}
