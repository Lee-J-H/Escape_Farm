package gang.il;

import java.util.ArrayList;

import static gang.il.Valiable.direction;

class TotalObject {
    ArrayList<String> foods = new ArrayList<String>(); //음식 테스트중
    float posX, posY;
    String type;
    boolean moveAble;
    int caveNum;

    public TotalObject(float posX, float posY, String type,boolean moveAble) {
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        this.moveAble = moveAble;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public String getType() {
        return type;
    }

    public boolean isMoveAble() {
        return moveAble;
    }

    public void setMoveAble(boolean moveAble) {
        this.moveAble = moveAble;
    }

    public int getLength(float posX, float posY) {
        switch (direction){
            case "left" :
            case "right":
                return (int) Math.abs(posX - this.posX);
            case "up" :
            case "down" :
                return (int) Math.abs(posY - this.posY);
        }
        return 0;
    }

    public int getCaveNum() {
        return caveNum;
    }
    public void setCaveNum(int caveNum) {
        this.caveNum = caveNum;
    }
}

