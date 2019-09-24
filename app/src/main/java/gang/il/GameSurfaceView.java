package gang.il;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import static gang.il.Valiable.drawInit;
import static gang.il.Valiable.finishObj;
import static gang.il.Valiable.gameMode;
import static gang.il.Valiable.minCount;
import static gang.il.Valiable.moveCount;
import static gang.il.Valiable.objCount;
import static gang.il.Valiable.stageCount;
import static gang.il.Valiable.stageSize_x;
import static gang.il.Valiable.stageSize_y;
import static gang.il.Valiable.totalObj;
import static gang.il.Valiable.direction;
import static gang.il.Valiable.curObjNum;
import static gang.il.Valiable.width;
import static gang.il.Valiable.height;
import static gang.il.Valiable.spaceX;
import static gang.il.Valiable.spaceY;
import static gang.il.Valiable.blankX;
import static gang.il.Valiable.blankY;


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Context mContext;
    SurfaceHolder mHolder;
    public static ImageThread mThread;
    Canvas mCanvas = null;
    boolean animal_clk = false;
    boolean running = true;
    Tutorial Tutorial;
    PointF firstPoint = new PointF(0, 0), lastPoint = new PointF(0, 0);

    public GameSurfaceView(Context context) {
        super(context);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public GameSurfaceView(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // Surface가 만들어질 때 호출됨
        drawInit = false;
        running = true;
        mThread = new GameSurfaceView.ImageThread();
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        // Surface가 변경될 때 호출됨
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Surface가 종료될 때 호출됨
        try {
            running = false;
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstPoint.x = event.getX();
                firstPoint.y = event.getY();
                for (int i = 0; i < objCount; i++)
                    if (!totalObj[i].getType().equals("none") && (int) (firstPoint.x - blankX) / spaceX == totalObj[i].getPosX() / 2 && (int) (firstPoint.y - blankY) / spaceY == totalObj[i].getPosY() / 2) { //빈칸이 아니고 동물이 있는 땅을 눌렀을 때
                        if (!totalObj[i].getType().endsWith("fin") && !totalObj[i].getType().equals("trap") && !totalObj[i].getType().equals("cave")) {
                            animal_clk = true;
                            curObjNum = i;
                            break;
                        }
                    }
                break;

            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                lastPoint.x = event.getX();
                lastPoint.y = event.getY();
                float firstX=spaceX * totalObj[curObjNum].getPosX() / 2 + blankX, firstY=spaceY * totalObj[curObjNum].getPosY() / 2 + blankY;
                double dx = lastPoint.x - firstX, dy = lastPoint.y - firstY;
                if (animal_clk && (Math.abs(dx) > spaceX / 2 || Math.abs(dy) > spaceY / 2)) {
                    setDirection(dx, dy);
                    if (stageCount.equals("1")) {
                        if (!Tutorial.nextStep(Tutorial.getTutorialNum()))
                            break;
                    }
                    Controller control = new Controller(mContext);
                    control.move();
                }
                animal_clk = false;
                if(height-spaceY*3<=lastPoint.y){// && lastPoint.y <= height&& firstPoint.y >= height-spaceY*3) { //하단 메뉴영역 터치시
                    if(spaceX*2<=lastPoint.x && lastPoint.x <= (spaceX*2+width/7)  &&spaceX*2<=firstPoint.x && firstPoint.x <= (spaceX*2+width/7)) //back버튼
                        ((GamePage) mContext).backStage();
                    else if(width-spaceX*4 <= lastPoint.x && (width-spaceX*4+width/7) >= lastPoint.x && width-spaceX*4 <= firstPoint.x && (width-spaceX*4+width/7) >= firstPoint.x) //reset버튼
                        ((GamePage) mContext).gameReset();
                }
                return false;
        }
        return true;
    }

    public void setDirection(double dx, double dy) {
        if (-135 < Math.toDegrees(Math.atan2(dy, dx)) && Math.toDegrees(Math.atan2(dy, dx)) <= -45) //상 -135보다 크고 -45보다 작다
            direction = "up";
        else if (45 < Math.toDegrees(Math.atan2(dy, dx)) && Math.toDegrees(Math.atan2(dy, dx)) <= 135) // 하 45보다 크고 135보단 작다.
            direction = "down";
        else if (135 < Math.toDegrees(Math.atan2(dy, dx)) && Math.toDegrees(Math.atan2(dy, dx)) <= 180 || -180 <= Math.toDegrees(Math.atan2(dy, dx)) && Math.toDegrees(Math.atan2(dy, dx)) < -135) //좌 135보단 크고 180보단 작으며 / -180보다 크고 -135보다 작다
            direction = "left";
        else if (-45 < Math.toDegrees(Math.atan2(dy, dx)) && Math.toDegrees(Math.atan2(dy, dx)) <= 45) //우 -45보다 크고 0보단 작으며 / 0보다 크며 45보단 작다
            direction = "right";
    }


    class ImageThread extends Thread {
        Bitmap ground, dog, squirrel, rabbit, panda, tiger, wall_down, wall_right, dog_fin, squirrel_fin, rabbit_fin, panda_fin, tiger_fin, acorn,
                bamboo, meat, trap, carrot, bone, cave, blackScreen, mask_result, mask, banner1, banner2, backBtn, resetBtn;//, backGround;

        private ImageThread() {
            WindowManager manager = (WindowManager)
                    mContext.getSystemService((Context.WINDOW_SERVICE));
            Display display = manager.getDefaultDisplay();
            Point sizePoint = new Point();
            display.getSize(sizePoint);
            width = sizePoint.x;
            height = sizePoint.y;
            spaceX = (int) width / 10;
            spaceY = (int) height / 20;
            ground = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ground);
            ground = Bitmap.createScaledBitmap(ground, (int) spaceX, (int) spaceY, true);
            dog = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dog);
            dog = Bitmap.createScaledBitmap(dog, (int) spaceX, (int) spaceY, true);
            squirrel = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.squirrel);
            squirrel = Bitmap.createScaledBitmap(squirrel, (int) spaceX, (int) spaceY, true);
            rabbit = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rabbit);
            rabbit = Bitmap.createScaledBitmap(rabbit, (int) spaceX, (int) spaceY, true);
            panda = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.panda);
            panda = Bitmap.createScaledBitmap(panda, (int) spaceX, (int) spaceY, true);
            tiger = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tiger);
            tiger = Bitmap.createScaledBitmap(tiger, (int) spaceX, (int) spaceY, true);
            wall_right = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wall);
            wall_right = Bitmap.createScaledBitmap(wall_right, (int) spaceX / 10, (int) spaceY, true);
            wall_down = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wall);
            wall_down = Bitmap.createScaledBitmap(wall_down, (int) spaceX, (int) spaceY / 10, true);
            dog_fin = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dog_fin);
            dog_fin = Bitmap.createScaledBitmap(dog_fin, (int) spaceX, (int) spaceY, true);
            squirrel_fin = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.squirrel_fin);
            squirrel_fin = Bitmap.createScaledBitmap(squirrel_fin, (int) spaceX, (int) spaceY, true);
            rabbit_fin = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rabbit_fin);
            rabbit_fin = Bitmap.createScaledBitmap(rabbit_fin, (int) spaceX, (int) spaceY, true);
            panda_fin = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.panda_fin);
            panda_fin = Bitmap.createScaledBitmap(panda_fin, (int) spaceX, (int) spaceY, true);
            tiger_fin = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tiger_fin);
            tiger_fin = Bitmap.createScaledBitmap(tiger_fin, (int) spaceX, (int) spaceY, true);
            trap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.trap);
            trap = Bitmap.createScaledBitmap(trap, (int) spaceX, (int) spaceY, true);
            carrot = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.carrot);
            carrot = Bitmap.createScaledBitmap(carrot, (int) spaceX, (int) spaceY, true);
            bone = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bone);
            bone = Bitmap.createScaledBitmap(bone, (int) spaceX, (int) spaceY, true);
            cave = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cave);
            cave = Bitmap.createScaledBitmap(cave, (int) spaceX, (int) spaceY, true);
            meat = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.meat);
            meat = Bitmap.createScaledBitmap(meat, (int) spaceX, (int) spaceY, true);
            acorn = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.acorn);
            acorn = Bitmap.createScaledBitmap(acorn, (int) spaceX, (int) spaceY, true);
            bamboo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bamboo);
            bamboo = Bitmap.createScaledBitmap(bamboo, (int) spaceX, (int) spaceY, true);
            banner1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.banner);
            banner1 = Bitmap.createScaledBitmap(banner1, (int) width, (int) spaceY*4, true);
            banner2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.banner2);
            banner2 = Bitmap.createScaledBitmap(banner2, (int) width, (int) spaceY*2, true);
            backBtn = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.back_btn);
            backBtn = Bitmap.createScaledBitmap(backBtn, (int) width/7, (int) width/7, true);
            resetBtn = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.reset_btn);
            resetBtn = Bitmap.createScaledBitmap(resetBtn, (int) width/7, (int) width/7, true);
            //backGround = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.background);
            //backGround = Bitmap.createScaledBitmap(backGround, (int) width, (int) height, true);
        }

        private void doDraw() {
            Paint textPaint = new Paint();
            //mCanvas.drawBitmap(backGround,0,0,null);
            mCanvas.drawBitmap(banner1,0,0,null);
            mCanvas.drawBitmap(banner2,0,spaceY*4,null);
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(width/15);
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            textPaint.setTextAlign(Paint.Align.CENTER);
            mCanvas.drawText("스테이지:"+stageCount,(int)width/2,spaceY,textPaint);
            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTextSize(width/25);
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            mCanvas.drawText("이동횟수:"+moveCount,1,spaceY*3-textPaint.getTextSize(),textPaint);
            mCanvas.drawText("남은동물",1,spaceY*4+textPaint.getTextSize(),textPaint);
            textPaint.setTextAlign(Paint.Align.RIGHT);
            mCanvas.drawText("최소횟수:"+minCount,(int)width-spaceX/5,spaceY*2,textPaint);
            mCanvas.drawBitmap(backBtn,spaceX*2,height-spaceY*3,null);
            mCanvas.drawBitmap(resetBtn,width-spaceX*4,height-spaceY*3,null);
            mCanvas.drawBitmap(ground, spaceX + blankX, spaceY + blankY, null);
            for (int i = 0; i < objCount; i++) {
                if (totalObj[i].getType().equals("wall")) {
                    if (totalObj[i].getPosX() % 2 == 0) //아래쪽 벽
                        mCanvas.drawBitmap(wall_down, spaceX * (totalObj[i].getPosX() / 2) + blankX, spaceY * ((int) totalObj[i].getPosY() / 2 + 0.95f) + blankY, null);
                    else //오른쪽 벽
                        mCanvas.drawBitmap(wall_right, spaceX * ((int) totalObj[i].getPosX() / 2 + 0.95f) + blankX, (spaceY * totalObj[i].getPosY() / 2) + blankY, null);
                }
                if (totalObj[i].getType().equals("dog"))
                    mCanvas.drawBitmap(dog, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("squirrel"))
                    mCanvas.drawBitmap(squirrel, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("rabbit"))
                    mCanvas.drawBitmap(rabbit, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("panda"))
                    mCanvas.drawBitmap(panda, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("tiger"))
                    mCanvas.drawBitmap(tiger, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("dog_fin"))
                    mCanvas.drawBitmap(dog_fin, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("squirrel_fin"))
                    mCanvas.drawBitmap(squirrel_fin, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("rabbit_fin"))
                    mCanvas.drawBitmap(rabbit_fin, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("panda_fin"))
                    mCanvas.drawBitmap(panda_fin, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("tiger_fin"))
                    mCanvas.drawBitmap(tiger_fin, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("trap"))
                    mCanvas.drawBitmap(trap, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("food_carrot"))
                    mCanvas.drawBitmap(carrot, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("food_bone"))
                    mCanvas.drawBitmap(bone, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("food_bamboo"))
                    mCanvas.drawBitmap(bamboo, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("food_acorn"))
                    mCanvas.drawBitmap(acorn, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("food_meat"))
                    mCanvas.drawBitmap(meat, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
                if (totalObj[i].getType().equals("cave"))
                    mCanvas.drawBitmap(cave, spaceX * totalObj[i].getPosX() / 2 + blankX, spaceY * totalObj[i].getPosY() / 2 + blankY, null);
            }
        }

        private void blindDraw() {
            blackScreen = Bitmap.createScaledBitmap(wall_down, (int) spaceX * stageSize_x, (int) spaceY * stageSize_y, true);
            mask_result = Bitmap.createBitmap(blackScreen.getWidth(), blackScreen.getHeight(), Bitmap.Config.ARGB_8888);
            // 전경 이미지와 동일 크기의 mutable 이미지 생성, 전경 이미지의 바탕으로 사용됨

            // 동적으로 마스킹 이미지를 생성하고 그리는 예
            // 전경 이미지와 동일한 크기의 알파 마스크 이미지를 생성하고 흰색을 채워 투명영역 설정함
            mask = Bitmap.createBitmap(blackScreen.getWidth(), blackScreen.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvasForMask = new Canvas(mask);
            Paint paint1 = new Paint();
            paint1.setAntiAlias(true);
            paint1.setARGB(255, 255, 255, 255);
            for (int i = 0; i < objCount; i++) {
                if (totalObj[i].getType().startsWith("food"))
                    continue;
                else if (totalObj[i].getType().endsWith("fin"))
                    continue;
                else if (totalObj[i].getType().equals("trap"))
                    continue;
                else if (totalObj[i].getType().equals("wall"))
                    continue;
                else if (totalObj[i].getType().equals("cave"))
                    continue;
                else if (totalObj[i].getType().equals("boundary"))
                    continue;
                canvasForMask.drawCircle(spaceX * (totalObj[i].getPosX() - 1) / 2, spaceY * (totalObj[i].getPosY() - 1) / 2, spaceX * 3 / 4.0f, paint1);//흰색으로 그려주는 곳은 투명하게 되어 배경이 보이게 될 영역이다
            }
            Canvas c = new Canvas(mask_result);
            c.drawBitmap(blackScreen, 0, 0, null); // 전경의 바탕 위에 이미지에 전경 이미지 그림
            Paint paint2 = new Paint();
            paint2.setFilterBitmap(false);
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            c.drawBitmap(mask, 0, 0, paint2); // 전경 이미지 위에 마스크 이미지 그림
            paint2.setXfermode(null);
            mCanvas.drawBitmap(mask_result, blankX + spaceX, blankY + spaceY, null); // 배경 위에 완성된 전경 이미지를 그림
        }

        private void init() {
            switch (stageSize_x) {
                case 4:
                    blankX = spaceX * 2;
                    break;
                case 5:
                    blankX = spaceX * 3 / 2;
                    break;
                case 6:
                    blankX = spaceX * 1;
                    break;
                case 7:
                    blankX = spaceX / 2;
                    break;
                case 8:
                    blankX = spaceX * 0;
                    break;
            }
            switch (stageSize_y) {
                case 4:
                    blankY = spaceY * 9;
                    break;
                case 5:
                    blankY = spaceY * 17 / 2;
                    break;
                case 6:
                    blankY = spaceY * 8;
                    break;
                case 7:
                    blankY = spaceY * 15 / 2;
                    break;
                case 8:
                    blankY = spaceY * 7;
                    break;
                case 9:
                    blankY = spaceY * 13 / 2;
                    break;
            }
            Bitmap result;
            result = Bitmap.createBitmap((int) spaceX * stageSize_x, (int) spaceY * stageSize_y, Bitmap.Config.ARGB_8888);
            Canvas groundCanvas = new Canvas(result);
            for (int i = 0; i < stageSize_y; i++)
                for (int j = 0; j < stageSize_x; j++)
                    groundCanvas.drawBitmap(ground, spaceX * j, spaceY * i, null);
            ground = Bitmap.createScaledBitmap(result, (int) spaceX * stageSize_x, (int) spaceY * stageSize_y, true);
            drawInit = true;
        }



        public void run() {
            while (running) {
                mCanvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
                        if (!drawInit) init();
                        doDraw();
                        if (gameMode.equals("night")) blindDraw();
                        if (stageCount.equals("1")) {
                            Tutorial = new Tutorial(mContext, mCanvas);
                            Tutorial.drawArrow(blankX + spaceX, blankY + (spaceY * 2));
                            Tutorial.drawMessage(0, blankY + (spaceY * 5));
                            mCanvas.drawBitmap(Tutorial.tutorialMask(blankX, blankY), blankX + spaceX, blankY + spaceY, null);
                        }
                        Widget Widget = new Widget(mContext, mCanvas);
                        Widget.drawAnimalWidget();
                    }
                } finally {
                    if (mCanvas == null) return;
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }
}
