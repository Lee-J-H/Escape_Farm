package gang.il;

import android.graphics.drawable.Drawable;

public class RecyclerItem {
    private Drawable[] btnImg = new Drawable[4];
    private int[] stageNum = new int[4];
    private String[] moveNum = new String[4];

    public void setBtnImg(Drawable Img[]) {
        btnImg = Img ;
    }
    public void setStageNum(int stage[]) {
        stageNum = stage;
    }
    public void setMoveNum(String move[]) {
        moveNum = move;
    }

    public Drawable getBtnImg(int i) {
        return this.btnImg[i];
    }
    public int getStageNum(int i) {
        return this.stageNum[i];
    }
    public String getMoveNum(int i) {
        return this.moveNum[i];
    }
}
