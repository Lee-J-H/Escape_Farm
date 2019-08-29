package gang.il;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import static gang.il.Valiable.direction;
import static gang.il.Valiable.tutorialNum;

public class Tutorial {
    Bitmap arrow, messageBoard, back;
    Canvas mCanvas;
    Paint mPaint;
    float width, height;
    Context mContext;
    String tutorialMessage1,tutorialMessage2;
    float spaceX;
    float spaceY;

    public Tutorial(Context context, float width, float height, Canvas canvas) {
        mContext = context;
        mCanvas = canvas;
        mPaint = new Paint();
        this.width = width;
        this.height = height;
        spaceX = width/10;
        spaceY = height/20;
        arrow = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.arrow);
        arrow = Bitmap.createScaledBitmap(arrow, (int) spaceX, (int) spaceY*3/2, true);
        messageBoard = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.messageboard);
        messageBoard = Bitmap.createScaledBitmap(messageBoard, (int)width-10, (int) height/7, true);
    }

    public void drawMessage(int startX, int startY) {
        mCanvas.drawBitmap(messageBoard, startX, startY, null);
        mPaint.setTextSize(35);
        switch (tutorialNum){
            case 1:
                tutorialMessage1 = mContext.getResources().getString(R.string.Tutorial_text_1);
                tutorialMessage2 = mContext.getResources().getString(R.string.Tutorial_text_2);
                break;
            case 2:
                tutorialMessage1 = mContext.getResources().getString(R.string.Tutorial_text_3);
                tutorialMessage2 = mContext.getResources().getString(R.string.Tutorial_text_4);
                break;
            case 3:
                tutorialMessage1 = mContext.getResources().getString(R.string.Tutorial_text_5);
                tutorialMessage2 = mContext.getResources().getString(R.string.Tutorial_text_6);
                break;
        }
        mCanvas.drawText(tutorialMessage1, startX+spaceX, startY+spaceY+spaceY/2, mPaint);
        mCanvas.drawText(tutorialMessage2, startX+spaceX, startY+spaceY*2, mPaint);
    }

    public void drawArrow(int startX, int startY) {
        Matrix rotateMatrix = new Matrix();
        Bitmap matrixArrow;
        switch (tutorialNum){
            case 1:
                mCanvas.drawBitmap(arrow, startX, startY, null);
                break;
            case 2:
                rotateMatrix.postRotate(90); //-360~360
                matrixArrow = Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), rotateMatrix, false);
                mCanvas.drawBitmap(matrixArrow, startX + spaceX * 3/2, startY-spaceY, null);
                break;
            case 3:
                rotateMatrix.postRotate(180); //-360~360
                matrixArrow = Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), rotateMatrix, false);
                mCanvas.drawBitmap(matrixArrow, startX + spaceX * 2, startY+spaceY/2, null);
                break;
        }
    }


    public int getTutorialNum() {
        return tutorialNum;
    }

    public boolean nextStep(int stepNum){
        boolean nextStep = false;
        switch (stepNum){
            case 1:
                if(direction.equals("up"))
                    nextStep=true;
                break;
            case 2:
                if(direction.equals("right"))
                    nextStep=true;
                break;
            case 3:
                if(direction.equals("down"))
                    nextStep=true;
                break;
        }

        return nextStep;
    }
}
