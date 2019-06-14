package com.example.dell.app;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bank.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FragmentFind extends Fragment implements View.OnClickListener {
    private FrameLayout frameLayout_recorder;
    private FrameLayout frameLayout_bills;
    private FrameLayout frameLayout_plan;
    private FrameLayout frameLayout_chart;
    private List<CostBean> mCostBeanList;
    private MySQLiteHelper mmysqlitehelper;
    private SQLiteDatabase mDataBase;
    //private DatabaseHelper mDatabaseHelper;
    //private CostListAdapter mAdapter;\
    List<CostBean> mlistbills = new ArrayList<CostBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.deal_data, container, false);
        frameLayout_recorder = (FrameLayout) view.findViewById(R.id.frameLayoutRecorder);
        frameLayout_bills = (FrameLayout) view.findViewById(R.id.frameLayoutStream);
        frameLayout_plan = (FrameLayout) view.findViewById(R.id.frameLayoutPlan);
        frameLayout_chart = (FrameLayout) view.findViewById(R.id.frameLayoutChart);

        frameLayout_bills.setOnClickListener(this);
        frameLayout_recorder.setOnClickListener(this);
        frameLayout_plan.setOnClickListener(this);
        frameLayout_chart.setOnClickListener(this);
        //mmysqlitehelper = new MySQLiteHelper(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frameLayoutRecorder:
                Intent intent = new Intent(getActivity(), RecorderActivity.class);
                startActivity(intent);
                break;
            case R.id.frameLayoutStream:
                startActivity(new Intent(getActivity(), BillsActivity.class));
                break;

            case R.id.frameLayoutPlan:
                startActivity(new Intent(getActivity(), PlanActivity.class));
                break;
            case R.id.frameLayoutChart:
                //startActivity(new Intent(getActivity(),ChartActivity.class));
                //break;
                Intent intent1 = new Intent(getActivity(), ChartActivity.class);
                intent1.putExtra("mlistbills", (Serializable) mlistbills);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;

            default:
                break;
        }
    }



}
