package gang.il;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static gang.il.Valiable.StagePage;
import static gang.il.Valiable.btnSound;
import static gang.il.Valiable.soundPlay;
import static gang.il.Valiable.soundPool;
import static gang.il.Valiable.stageCount;

public class StagePagerAdapter extends RecyclerView.Adapter<StagePagerAdapter.ViewHolder> {
private long mLastClickTime = 0;
        StageDBHelper StageDB;
        Context context;
private ArrayList<RecyclerItem> mData = null;
        int succeedStage;

// 아이템 뷰를 저장하는 뷰홀더 클래스.
public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView[] btnImg = new ImageView[4];
    TextView[] stage = new TextView[4];
    TextView[] move = new TextView[4];

    ViewHolder(View itemView) {
        super(itemView);
        // 뷰 객체에 대한 참조. (hold strong reference)
        for(int i=0; i<4; i++){
            int btnImgId = context.getResources().getIdentifier("btn_" + (i+1), "id", "gang.il");
            int stageId = context.getResources().getIdentifier("num_" + (i+1), "id", "gang.il");
            int moveId = context.getResources().getIdentifier("min_" + (i+1), "id", "gang.il");
            btnImg[i] = itemView.findViewById(btnImgId);
            stage[i] =  itemView.findViewById(stageId);
            move[i] = itemView.findViewById(moveId);
        }
        btnImg[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    RecyclerItem item = mData.get(position);
                    stageCount = String.valueOf(item.getStageNum(0));
                    onBtnClick();
                }
            }
        });
        btnImg[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    RecyclerItem item = mData.get(position);
                    stageCount = String.valueOf(item.getStageNum(1));
                    onBtnClick();
                }
            }
        });
        btnImg[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    RecyclerItem item = mData.get(position);
                    stageCount = String.valueOf(item.getStageNum(2));
                    onBtnClick();
                }
            }
        });
        btnImg[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    RecyclerItem item = mData.get(position);
                    stageCount = String.valueOf(item.getStageNum(3));
                    onBtnClick();
                }
            }
        });
    }
}

    private void onBtnClick(){
        if (Integer.parseInt(stageCount) > succeedStage)
            return;
        StageDB.getStageObj();
        if (soundPlay)
            soundPool.play(btnSound, 1f, 1f, 0, 0, 1f); //버튼 사운드 재생
        Intent intent = new Intent(StagePage, GamePage.class);
        StagePage.startActivity(intent);
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    StagePagerAdapter(ArrayList<RecyclerItem> list, Context pContext) {
        mData = list;
        context=pContext;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public StagePagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.stage_list, parent, false);
        StagePagerAdapter.ViewHolder vh = new StagePagerAdapter.ViewHolder(view);
        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(StagePagerAdapter.ViewHolder holder, int position) {
        RecyclerItem item = mData.get(position);
        StageDB = new StageDBHelper(context);
        succeedStage = StageDB.clearStageNum()+1;

        for (int i = 0; i < 4; i++) {
            holder.btnImg[i].setImageDrawable(item.getBtnImg(i));
            holder.stage[i].setText(String.valueOf(item.getStageNum(i)));
            holder.move[i].setText(item.getMoveNum(i));
            if(Integer.parseInt(String.valueOf(holder.stage[i].getText())) <= succeedStage){
                holder.stage[i].setVisibility(View.VISIBLE);
                holder.move[i].setVisibility(View.VISIBLE);
            }
            else{
                holder.stage[i].setVisibility(View.INVISIBLE);
                holder.move[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }
}