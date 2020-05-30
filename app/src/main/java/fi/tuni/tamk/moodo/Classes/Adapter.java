package fi.tuni.tamk.moodo.Classes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import fi.tuni.tamk.moodo.R;

public class Adapter extends BaseAdapter {
    ArrayList<Routine> allItems;
    Context mContext;

    public Adapter(Context context, ArrayList<Routine> allItems) {
        this.allItems = allItems;
        mContext = context;
    }

    @Override
    public int getCount() {
        return allItems.size();
    }

    @Override
    public Routine getItem(int position) {
        return allItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = View.inflate(mContext, R.layout.list_item, null);
        TextView mText = mView.findViewById(R.id.routine_list_textview);
        ImageView mImage = mView.findViewById(R.id.routine_list_imageview);

        Routine mRoutine = getItem(position);
        mText.setText(mRoutine.getName());
        if(mRoutine.getIconId() != 0) {
            mImage.setImageDrawable(ContextCompat.getDrawable(mContext, mRoutine.getIconId()));
        }

        return mView;
    }
}