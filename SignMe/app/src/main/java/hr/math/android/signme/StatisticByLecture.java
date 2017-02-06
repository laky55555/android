package hr.math.android.signme;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;

import hr.math.android.signme.Database.DBAttendance;
import hr.math.android.signme.Database.DBLectures;
import hr.math.android.signme.Database.DBStudents;

/**
 * Created by ivan on 05.02.17..
 */


public class StatisticByLecture extends Fragment {

    private static final String FILTER = "filter";
    public static final String FILTER_STUDENT = "student";
    public static final String FILTER_DATE = "date";
    private String fragmentFilter;

    private Spinner spinnerLecture;
    private Spinner spinnerFilter;
    private TextView textFilter;
    private RecyclerView recyclerView;

    private int lectureId;
    private int filterId;
    private int filterPosition;

    private DBLectures lectures;
    private DBStudents students;
    private DBAttendance attendance;

    private ArrayList<String> filterFill;
    private ArrayList<Integer> filterIds;

    public static StatisticByLecture newInstance(String filter) {
        StatisticByLecture fragmentDemo = new StatisticByLecture();
        Bundle args = new Bundle();
        args.putString(FILTER, filter);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentFilter = getArguments().getString(FILTER, FILTER_STUDENT);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistic_by_lecture, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        getViews(view);
        initializeLectureSpinner();
        addListenerOnSpinnerItemSelection();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        attendance.close();
        students.close();
        lectures.close();
    }

    private void getViews(View view) {
        attendance = new DBAttendance(getContext());
        lectures = new DBLectures(getContext());
        students = new DBStudents(getContext());

        attendance.open();
        lectures.open();
        students.open();

        spinnerLecture = (Spinner) view.findViewById(R.id.spinner_lecture);
        spinnerFilter = (Spinner) view.findViewById(R.id.spinner_filter);
        textFilter = (TextView) view.findViewById(R.id.text_filter);
        if(fragmentFilter.equals(FILTER_DATE))
            textFilter.setText(getString(R.string.select_date));
        else if(fragmentFilter.equals(FILTER_STUDENT))
            textFilter.setText(getString(R.string.select_student));

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void initializeLectureSpinner() {
        lectures.open();
        Cursor cursor = lectures.getAllLectures();

        SimpleCursorAdapter adapter =new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, cursor,
                new String[]{DBLectures.NAME}, new int[]{android.R.id.text1}, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLecture.setAdapter(adapter);
        lectures.close();
    }

    private void initializeFilterSpinner() {
        //int selectedLectureId = (int)spinnerLecture.getSelectedItemId();
        if(fragmentFilter.equals(FILTER_STUDENT)) {
            //TODO: decide between cursoradapter and arrayadapter
//            Cursor cursor = students.getAllStudentsOfLecture(lectureId);
//            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, cursor,
//                    new String[]{DBLectures.NAME}, new int[]{android.R.id.text1}, 0);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//            spinnerFilter.setAdapter(adapter);

            Pair<ArrayList<String>, ArrayList<Integer>> novi = students.getAllStudentsOfLecture(lectureId, true);
            if(novi == null) {
                filterFill = new ArrayList<String>();
                filterFill = new ArrayList<String>();
            }
            else {
                filterFill = novi.first;
                filterIds = novi.second;
            }
            Log.v("DATES", "broj datuma = " + filterFill.size());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, filterFill);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFilter.setAdapter(adapter);


        }
        else if(fragmentFilter.equals(FILTER_DATE)) {
            filterFill = attendance.getAllDatesOfLecture(lectureId);
            if (filterFill == null)
                filterFill = new ArrayList<String>();
            Log.v("DATES", "broj datuma = " + filterFill.size());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, filterFill);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFilter.setAdapter(adapter);
        }
    }

    private void addListenerOnSpinnerItemSelection() {
        spinnerLecture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerFilter.setVisibility(View.VISIBLE);
                textFilter.setVisibility(View.VISIBLE);
                lectureId = (int)id;
                clearRecyclerView();
                initializeFilterSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterId = (int) id;
                filterPosition = position;
                Log.v("FILTER item selected", "id " + id);
                Log.v("FILTER item selected", "position " + position);
                fillRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                clearRecyclerView();
            }
        });
    }

    private void clearRecyclerView() {
        StudentsAdapter adapter = new StudentsAdapter(getActivity(), null);
        recyclerView.setAdapter(adapter);
    }

    private void fillRecyclerView() {

        if(fragmentFilter.equals(FILTER_STUDENT)) {
            ArrayList<String> dates = attendance.getAllDatesOfLectureOfStudent(filterIds.get(filterPosition), lectureId);
            StudentsAdapter adapter = new StudentsAdapter(getActivity(), dates);
            recyclerView.setAdapter(adapter);
        }
        else if (fragmentFilter.equals(FILTER_DATE)) {
            ArrayList<String> dates = attendance.getAllStudentsOfLectureOfDate(filterFill.get(filterPosition), lectureId);
            StudentsAdapter adapter = new StudentsAdapter(getActivity(), dates);
            recyclerView.setAdapter(adapter);
        }
    }

}
