package fi.tuni.tamk.moodo.Classes;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import fi.tuni.tamk.moodo.R;

public class RoutineAdapter extends ArrayAdapter<String> {

    private List<String> list;
    private ListStatus responder;
    private Context context;
    private TextView subRoutineDesc, deleteSubRoutine, editSubRoutine;

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
        editSubRoutine = (TextView) listItemView.findViewById(R.id.adapterImageBtn2);

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

        editSubRoutine.setOnClickListener((v) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle(context.getString(R.string.routine_adapter_edit));

            final EditText input = new EditText(context);
            alert.setView(input);
            input.setText(list.get(position));


            alert.setPositiveButton(context.getString(R.string.routine_adapter_ok), (dialog, which_button) -> {
                String srt = input.getEditableText().toString();
                if(srt.length() != 0) {
                    list.set(position, srt);
                    responder.modifySubRoutine(position, srt);
                }
            });
            alert.setNegativeButton(context.getString(R.string.routine_dialog_cancel), (dialog, whichButton) -> {
                dialog.cancel();
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        });
        return listItemView;
    }
}
