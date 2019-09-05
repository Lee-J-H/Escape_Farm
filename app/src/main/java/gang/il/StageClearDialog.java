package gang.il;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static gang.il.Valiable.minCount;
import static gang.il.Valiable.moveCount;
import static gang.il.Valiable.stageCount;

public class StageClearDialog extends Dialog {
    private String mContent;
    private LinearLayout.LayoutParams params;
    private ImageView menuIcon,nextIcon,retryIcon;
    private TextView clrStage,clrMin,clrMove,clrText;

    private View.OnClickListener menuClickListener;
    private View.OnClickListener retryClickListener;
    private View.OnClickListener nextClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.stage_clear_dialog);
        menuIcon = (ImageView) findViewById(R.id.menu_icon);
        nextIcon = (ImageView) findViewById(R.id.next_icon);
        retryIcon = (ImageView) findViewById(R.id.retry_icon);
        clrStage = (TextView) findViewById(R.id.clr_stage);
        clrMin = (TextView) findViewById(R.id.clr_min);
        clrMove = (TextView) findViewById(R.id.clr_move);
        clrText = (TextView) findViewById(R.id.clr_text);
        clrText.setText(mContent);

        if (menuClickListener != null && nextClickListener != null && retryClickListener != null) { //버튼 3개짜리
            menuIcon.setOnClickListener(menuClickListener);
            nextIcon.setOnClickListener(nextClickListener);
            retryIcon.setOnClickListener(retryClickListener);
            retryIcon.setImageResource(R.drawable.retry_icon);
            clrStage.setText("stage:" + stageCount);
            clrMove.setText("이동횟수:" + moveCount);
            clrMin.setText("최소횟수:" + minCount);
            menuIcon.setVisibility(View.VISIBLE);
            nextIcon.setVisibility(View.VISIBLE);
            retryIcon.setVisibility(View.VISIBLE);
            clrStage.setVisibility(View.VISIBLE);
            clrMove.setVisibility(View.VISIBLE);
            clrMin.setVisibility(View.VISIBLE);
            clrText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 60f);
            params = (LinearLayout.LayoutParams) clrText.getLayoutParams();
            params.weight = 0.3f;
            clrText.setLayoutParams(params);
        } else if (menuClickListener == null && nextClickListener == null && retryClickListener != null) {
            retryIcon.setOnClickListener(retryClickListener);
            clrStage.setVisibility(View.GONE);
            clrMove.setVisibility(View.GONE);
            clrMin.setVisibility(View.GONE);
            menuIcon.setVisibility(View.INVISIBLE);
            nextIcon.setVisibility(View.INVISIBLE);
            retryIcon.setImageResource(R.drawable.dialog_yes);
            params = (LinearLayout.LayoutParams) retryIcon.getLayoutParams();
            retryIcon.setLayoutParams(params);
            clrText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40f);
            params = (LinearLayout.LayoutParams) clrText.getLayoutParams();
            params.weight = 0.7f;
            clrText.setLayoutParams(params);
        }
    }
//[출처] [안드로이드] 뷰의 가중치(layout_weight) 코드에서 동적으로 변경|작성자 자바킹


    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public StageClearDialog(Context context, String content,
                            View.OnClickListener retryListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContent = content;
        this.retryClickListener = retryListener;
    }

    public StageClearDialog(Context context,String content, View.OnClickListener menuListener,
                            View.OnClickListener nextListener,View.OnClickListener retryListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContent = content;
        this.menuClickListener = menuListener;
        this.nextClickListener = nextListener;
        this.retryClickListener = retryListener;
    }
}