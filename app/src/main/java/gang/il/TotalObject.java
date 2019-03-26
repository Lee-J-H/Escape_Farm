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
}

class  AnimalObject extends TotalObject{
    boolean moveAvailable = true;

    public AnimalObject(float posX, float posY, String type, boolean moveAvailable){
        super(posX,posY,type);
        this.moveAvailable = moveAvailable;
    }

    public boolean isMoveAvailable() {
        return moveAvailable;
    }

    public void setMoveAvailable(boolean moveAvailable) {
        this.moveAvailable = moveAvailable;
    }
}

