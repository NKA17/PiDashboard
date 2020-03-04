package ui.widget;

import graphics.ImageCache;
import raspberryPi.RPiInterface;
import realTime.PiAction;
import config.Configuration;
import ui.view.PiPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InternetStatusPanel extends PiPanel {

    //invert this so we know our status on start up
    private boolean internetStatus = !RPiInterface.checkInternetConnection();
    private PiAction statusTurnedGoodAction = null;
    private PiAction statusTurnedBadAction = null;
    private PiAction extendedAction = null;

    public InternetStatusPanel(){
        super(50,50);
    }

    @Override
    public void draw(Graphics g) {
        int L = Configuration.GRIDBAG_INSETS.left;
        int R = Configuration.GRIDBAG_INSETS.right;
        int T = Configuration.GRIDBAG_INSETS.top;
        int B = Configuration.GRIDBAG_INSETS.bottom;

        int s = Math.min(getW()-L-R,getH()-T-B);
        BufferedImage image = internetStatus
                ? ImageCache.get("internet good.png",s,s)
                : ImageCache.get("internet bad.png",s,s);

        g.drawImage(image,getX()+getW()/2-image.getWidth()/2,getY()+getH()/2-image.getHeight()/2,null);
    }

    @Override
    public void update() {
        boolean newStatus = RPiInterface.checkInternetConnection();

        if(newStatus != internetStatus){
            PiPanel.updatedPanels.add(this);
        }

        if(newStatus != internetStatus && newStatus && statusTurnedGoodAction != null){
            statusTurnedGoodAction.deploy();
        }
        if(newStatus != internetStatus && !newStatus && statusTurnedBadAction != null){
            statusTurnedBadAction.deploy();
        }
        internetStatus = newStatus;

        if(extendedAction != null){
            extendedAction.deploy();
        }
    }

    public boolean getInternetStatus() {
        return internetStatus;
    }

    public void setInternetStatus(boolean internetStatus) {
        this.internetStatus = internetStatus;
    }

    public PiAction getStatusTurnedGoodAction() {
        return statusTurnedGoodAction;
    }

    public void setStatusTurnedGoodAction(PiAction statusTurnedGoodAction) {
        this.statusTurnedGoodAction = statusTurnedGoodAction;
    }

    public PiAction getStatusTurnedBadAction() {
        return statusTurnedBadAction;
    }

    public void setStatusTurnedBadAction(PiAction statusTurnedBadAction) {
        this.statusTurnedBadAction = statusTurnedBadAction;
    }

    public PiAction getExtendedAction() {
        return extendedAction;
    }

    public void setExtendedAction(PiAction extendedAction) {
        this.extendedAction = extendedAction;
    }

    @Override
    public boolean isPowered() {
        return false;
    }


}
