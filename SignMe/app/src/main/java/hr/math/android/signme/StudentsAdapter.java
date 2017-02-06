package hr.math.android.signme;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ivan on 05.02.17..
 */

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> {

    Context context;

    ArrayList<String> students;

    public StudentsAdapter(Context context, ArrayList<String> students) {
        this.context = context;
        this.students = students;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.students_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvStudents.setText(students.get(position));
    }

    @Override
    public int getItemCount() {
        if(students == null)
            return 0;
        return students.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStudents;

        public ViewHolder(View view) {
            super(view);

            tvStudents = (TextView) view.findViewById(R.id.tvStudentName);
        }
    }

}
