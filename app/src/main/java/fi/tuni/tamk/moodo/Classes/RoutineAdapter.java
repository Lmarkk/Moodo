package fi.tuni.tamk.moodo.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fi.tuni.tamk.moodo.R;

public class RoutineAdapter extends ArrayAdapter<String> {

    private List<String> list;
    private ListStatus responder;
    private Context context;
    private TextView subRoutineDesc,
            deleteSubRoutine;

    public RoutineAdapter(Context context, List<String> mySubRoutines, ListStatus responder) {
        super(context, 0, mySubRoutines);
        this.list = mySubRoutines;
        this.context = context;
        this.responder = responder;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_view_layout, parent, false
            );
        }
        final String currentSubRoutine = getItem(position);

        subRoutineDesc = (TextView) listItemView.findViewById(R.id.adapterText1);
        deleteSubRoutine = (TextView) listItemView.findViewById(R.id.adapterImageBtn1);

        if (currentSubRoutine != null) {
            subRoutineDesc.setText(currentSubRoutine);
        }

        deleteSubRoutine.setOnClickListener((v) -> {
            list.remove(position);
            notifyDataSetChanged();
            if(list.size() == 0) {
                responder.checkStatus();
            }
        });
        return listItemView;
    }
}
