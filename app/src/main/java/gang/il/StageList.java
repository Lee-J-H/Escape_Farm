package gang.il;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static gang.il.CheckedStage.onCheckStage;
import static gang.il.Valiable.clrDialogBtn;
import static gang.il.Valiable.stageCount;

public class StageList extends BaseAdapter {
    int succeedStage;


    public StageList() {
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        final Context context = parent.getContext();
        succeedStage = onCheckStage(context);
        convertView=null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.stage_list, parent, false);
            for(int i=0; i<3; i++){
                int textViewId = context.getResources().getIdentifier("num_" + (i + 1), "id", "gang.il");
                int layoutId = context.getResources().getIdentifier("stage_" + (i + 1), "id", "gang.il");
                int imageViewId = context.getResources().getIdentifier("lock_" + (i + 1), "id", "gang.il");
                viewHolder.stage_num_text[i] = (TextView) convertView.findViewById(textViewId);
                viewHolder.stage_area[i] = (LinearLayout) convertView.findViewById(layoutId);
                viewHolder.stage_lock_img[i] = (ImageView) convertView.findViewById(imageViewId);
            }
        }


        for (int i = 0; i < 3; i++) {
            viewHolder.stage_num_text[i].setText("" + (position * 3 + 1 + i));
            viewHolder.stage_area[i].setTag("" + (position * 3 + 1 + i));
            if (Integer.parseInt(viewHolder.stage_area[i].getTag().toString()) <= succeedStage) {
                viewHolder.stage_lock_img[i].setVisibility(View.GONE);
                viewHolder.stage_area[i].setBackgroundColor(Color.parseColor("#F4DFAC"));
            }
        }

        for (int i = 0; i < 3; i++)
            viewHolder.stage_area[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stageCount = v.getTag().toString();
                    if (Integer.parseInt(stageCount) > succeedStage)
                        return;
                    clrDialogBtn="stageClk";
                    LoadDB.GetDB Data = new LoadDB.GetDB();
                    Data.execute("http://106.10.57.117/EscapeFarm/getStage.php", stageCount);  //스테이지DB 로딩
                }
            });

        return convertView;
    }

    private class ViewHolder{
        private TextView[] stage_num_text = new TextView[3];
        private LinearLayout[] stage_area = new LinearLayout[3];
        private ImageView[] stage_lock_img = new ImageView[3];
    }

}