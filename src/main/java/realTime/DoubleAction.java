package realTime;

public abstract class DoubleAction extends PiAction {

    private boolean actOnDeploy = true;

    public DoubleAction(int interval) {
        super(interval);
        setState(ActionState.OPEN);
    }

    public DoubleAction(long interval, TimeUnit tu) {
        super(interval, tu);
    }

    public boolean isActOnDeploy() {
        return actOnDeploy;
    }

    public void setActOnDeploy(boolean actOnDeploy) {
        this.actOnDeploy = actOnDeploy;
    }

    public final void deploy(){
        if(actOnDeploy){
            refresh();
        }
        super.deploy();
    }

    public abstract void open();

    public abstract void close();

    @Override
    public void refresh() {
        switch (getState()){
            case OPEN:
                open();
                setState(ActionState.CLOSE);
                break;
            case CLOSE:
                close();
                setState(ActionState.KILL);
                break;
            case KILL:
                break;
        }
    }
}
