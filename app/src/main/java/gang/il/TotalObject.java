package gang.il;

class TotalObject {
    float posX, posY;
    String type; // animal, -fin, trap, wall

    public TotalObject(float posX, float posY, String type) {
        this.posX = posX;
        this.posY = posY;
        this.type = type;
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

    public int getLength(float posX, float posY, String direction) {
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
}

class  AnimalObject extends TotalObject{
    boolean moveAble = true;

    public AnimalObject(float posX, float posY, String type, boolean moveAble){
        super(posX,posY,type);
        this.moveAble = moveAble;
    }

    public boolean isMoveAble() {
        return moveAble;
    }

    public void setMoveAble(boolean moveAble) {
        this.moveAble = moveAble;
    }
}

