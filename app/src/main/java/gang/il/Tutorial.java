package gang.il;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import static gang.il.Valiable.direction;
import static gang.il.Valiable.stageSize_x;
import static gang.il.Valiable.stageSize_y;
import static gang.il.Valiable.tutorialNum;
import static gang.il.Valiable.width;
import static gang.il.Valiable.height;
import static gang.il.Valiable.spaceX;
import static gang.il.Valiable.spaceY;

public class Tutorial {
    Bitmap arrow, messageBoard, tutorialMaskResult, Translucent, tutorialMask;
    Canvas mCanvas;
    Paint mPaint;
    Context mContext;
    String tutorialMessage1, tutorialMessage2;

    public Tutorial(Context context, Canvas canvas) {
        mContext = context;
        mCanvas = canvas;
        mPaint = new Paint();
        arrow = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.arrow);
        arrow = Bitmap.createScaledBitmap(arrow, (int) spaceX, (int) spaceY * 3 / 2, true);
        messageBoard = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.messageboard);
        messageBoard = Bitmap.createScaledBitmap(messageBoard, (int) width - 10, (int) height / 7, true);
        Translucent = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mask_tutorial);
    }

    public Bitmap tutorialMask(int blankX, int blankY) {
        Translucent = Bitmap.createScaledBitmap(Translucent, (int) spaceX * stageSize_x, (int) spaceY * stageSize_y, true);
        tutorialMaskResult = Bitmap.createBitmap(Translucent.getWidth(), Translucent.getHeight(), Bitmap.Config.ARGB_8888);
        switch (tutorialNum) {
            case 1:
            case 3:
                tutorialMask = Bitmap.createBitmap((int)spaceX, (int)spaceY*4, Bitmap.Config.ARGB_8888);
                break;
            case 2:
                tutorialMask = Bitmap.createBitmap((int)spaceX*4, (int)spaceY, Bitmap.Config.ARGB_8888);
                break;
        }
        Canvas c = new Canvas(tutorialMaskResult);
        c.drawBitmap(Translucent, 0, 0, null); // 전경의 바탕 위에 이미지에 전경 이미지 그림
        Paint tutorialPaint = new Paint();
        tutorialPaint.setFilterBitmap(false);
        tutorialPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        if(tutorialNum<3)c.drawBitmap(tutorialMask, 0, 0, tutorialPaint);
        else c.drawBitmap(tutorialMask, spaceX*2, 0, tutorialPaint);
        tutorialPaint.setXfermode(null);
        return tutorialMaskResult;
    }

    public void drawMessage(int startX, int startY) {
        mCanvas.drawBitmap(messageBoard, startX, startY, null);
        mPaint.setTextSize(width/21);

        switch (tutorialNum) {
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
        mCanvas.drawText(tutorialMessage1, startX + spaceX, startY + spaceY + spaceY / 2, mPaint);
        mCanvas.drawText(tutorialMessage2, startX + spaceX, startY + spaceY * 2, mPaint);
    }

    public void drawArrow(int startX, int startY) {
        Matrix rotateMatrix = new Matrix();
        Bitmap matrixArrow;
        switch (tutorialNum) {
            case 1:
                mCanvas.drawBitmap(arrow, startX, startY, null);
                break;
            case 2:
                rotateMatrix.postRotate(90); //-360~360
                matrixArrow = Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), rotateMatrix, false);
                mCanvas.drawBitmap(matrixArrow, startX + spaceX * 3 / 2, startY - spaceY, null);
                break;
            case 3:
                rotateMatrix.postRotate(180); //-360~360
                matrixArrow = Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), rotateMatrix, false);
                mCanvas.drawBitmap(matrixArrow, startX + spaceX * 2, startY + spaceY / 2, null);
                break;
        }
    }


    public int getTutorialNum() {
        return tutorialNum;
    }

    public boolean nextStep(int stepNum) {
        boolean nextStep = false;
        switch (stepNum) {
            case 1:
                if (direction.equals("up"))
                    nextStep = true;
                break;
            case 2:
                if (direction.equals("right"))
                    nextStep = true;
                break;
            case 3:
                if (direction.equals("down"))
                    nextStep = true;
                break;
        }

        return nextStep;
    }
}
