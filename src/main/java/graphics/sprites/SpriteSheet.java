package graphics.sprites;

import enums.Axis;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    private int width;
    private int height;
    private BufferedImage spriteSheet;
    private int totalFrames;
    private Axis direction = Axis.HORIZONTAL;
    private int frame = 0;

    public SpriteSheet(BufferedImage spriteSheet, int width, int height, int totalFrames, Axis direction) {
        this.width = width;
        this.height = height;
        this.spriteSheet = spriteSheet;
        this.totalFrames = totalFrames;
        this.direction = direction;
    }

    public SpriteSheet(BufferedImage spriteSheet, int width, int height, int totalFrames) {
        this.width = width;
        this.height = height;
        this.spriteSheet = spriteSheet;
        this.totalFrames = totalFrames;
    }

    public SpriteSheet(BufferedImage spriteSheet, int width, int height) {
        this.width = width;
        this.height = height;
        this.spriteSheet = spriteSheet;
        totalFrames = (spriteSheet.getWidth() / width) * (spriteSheet.getHeight() / height);
    }

    public BufferedImage next(){
        int x;
        int y;
        int xFrames = spriteSheet.getWidth() / width;
        int yFrames = spriteSheet.getHeight() / height;

        if(direction == Axis.HORIZONTAL){
            x = (frame % xFrames) * width;
            y = ((frame / xFrames) * height);
        }else {
            x = ((frame / yFrames) * width);
            y = (frame % yFrames) * height;
        }

        frame++;
        if(frame >= totalFrames){
            frame = 0;
        }

        return spriteSheet.getSubimage(x,y,width,height);
    }

    public void gotoFrame(int frame){
        this.frame = frame;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BufferedImage getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheet(BufferedImage spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public Axis getDirection() {
        return direction;
    }

    public void setDirection(Axis direction) {
        this.direction = direction;
    }

    public int getCurrentFrame() {
        return frame;
    }
}
