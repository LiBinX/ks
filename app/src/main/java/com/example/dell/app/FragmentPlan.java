package com.example.dell.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bank.R;

import java.util.ArrayList;


public class FragmentPlan extends Fragment {

        private RecyclerView mRecyclerView;
        private ArrayList<PlanData> mplanData;
        private HomeAdapter mAdapter;
        //数据库
        private MySQLiteHelper mMysql;
        private SQLiteDatabase mDataBase;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                View view = inflater.inflate(R.layout.showplan, container, false);

                initData();
                mRecyclerView = (RecyclerView)view.findViewById(R.id.id_recyclerview);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setAdapter(mAdapter = new HomeAdapter(getActivity(),mplanData));
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

                mAdapter.setOnItemLongClickListener(new HomeAdapter.OnItemLongClickLitener() {
                        @Override
                        public void onLongItemClick(View view, final int position) {
                    new AlertDialog.Builder(getActivity())
                    /* 弹出窗口的最上头文字 */
                    .setTitle("删除当前数据")
                    /* 设置弹出窗口的图式 */
                    .setIcon(android.R.drawable.ic_dialog_info)
                    /* 设置弹出窗口的信息 */
                    .setMessage("确定删除当前记录")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialoginterface, int i) {

                                   PlanData planData = mplanData.get(position);
                                   String morningPlan = planData.getMoringPlan();
                                   Log.i("text",morningPlan+"!!");

                                   // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
                                   String[] whereArgs = new String[]{String.valueOf(morningPlan)};
                                   //获取当前数据库
                                   mMysql = new MySQLiteHelper(getActivity(), "plan.db", null, 1);
                                   mDataBase = mMysql.getReadableDatabase();
                                   try {
                                           mDataBase.delete("plan", "Morningplan=?", whereArgs);
                                           mplanData.remove(position);
                                           mAdapter.notifyDataSetChanged();
                                   } catch (Exception e) {
                                           Log.e("删除出错了", "error");
                                   } finally {
                                           mDataBase.close();
                                           mMysql.close();
                                   }
                           }
                    }
                    ).setNegativeButton("否", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialoginterface, int i) {

                                    }
                            }
                    ).show();

                        }
                });
                return view;
        }

        //获取数据，初始化数据
        public void initData()
        {
                mplanData = new ArrayList<PlanData>();
                mMysql = new MySQLiteHelper(getActivity(), "finance.db", null, 1);
                mDataBase = mMysql.getReadableDatabase();

                Cursor cursor = mDataBase.rawQuery("select * from plan", null);
                cursor.moveToFirst();
                int columnsSize = cursor.getColumnCount();
                int number = 0;
                while (number < cursor.getCount()) {

                        PlanData a =new PlanData();
                        a.setMoringPlan(cursor.getString(cursor.getColumnIndex("Morningplan")));
                        a.setAfterPlan(cursor.getString(cursor.getColumnIndex("Afternoonplan")));
                        a.setNightPlan(cursor.getString(cursor.getColumnIndex("Nightplan")));
                        a.setConclusion(cursor.getString(cursor.getColumnIndex("Conclusion")));
                        a.setRank(cursor.getString(cursor.getColumnIndex("Rank")));

                        cursor.moveToNext();
                        number++;

                        mplanData.add(a);
                }

                cursor.close();
                mDataBase.close();
                mMysql.close();
        }

        class PlanData{
                String MoringPlan;
                String AfterPlan;
                String NightPlan;
                String Conclusion;
                String Rank;

                void setMoringPlan(String plan)
                {
                        MoringPlan =plan;
                }

                void setAfterPlan(String plan)
                {
                        AfterPlan =plan;
                }
                void setNightPlan(String plan)
                {
                        NightPlan =plan;
                }
                void setRank(String plan)
                {
                        Rank =plan;
                }
                void setConclusion(String plan)
                {
                        Conclusion =plan;
                }

                String getMoringPlan(){
                        return  this.MoringPlan;
                }

                String getAfterPlan(){
                        return  this.AfterPlan;
                }
                String getNightPlan(){
                        return  this.NightPlan;
                }
                String getConclusion(){
                        return  this.Conclusion;
                }
                String getRank(){
                        return  this.Rank;
                }
        }

        //设置分割线
        public class DividerItemDecoration extends RecyclerView.ItemDecoration {

                private  final int[] ATTRS = new int[]{
                        android.R.attr.listDivider
                };

                public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

                public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

                private Drawable mDivider;

                private int mOrientation;

                public DividerItemDecoration(Context context, int orientation) {
                        final TypedArray a = context.obtainStyledAttributes(ATTRS);
                        mDivider = a.getDrawable(0);
                        a.recycle();
                        setOrientation(orientation);
                }

                public void setOrientation(int orientation) {
                        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                                throw new IllegalArgumentException("invalid orientation");
                        }
                        mOrientation = orientation;
                }

                @Override
                public void onDraw(Canvas c, RecyclerView parent) {

                        if (mOrientation == VERTICAL_LIST) {
                                drawVertical(c, parent);
                        } else {
                                drawHorizontal(c, parent);
                        }
                }

                public void drawVertical(Canvas c, RecyclerView parent) {
                        final int left = parent.getPaddingLeft();
                        final int right = parent.getWidth() - parent.getPaddingRight();

                        final int childCount = parent.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                                final View child = parent.getChildAt(i);
                                android.support.v7.widget.RecyclerView v = new android.support.v7.widget.RecyclerView(parent.getContext());
                                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                                        .getLayoutParams();
                                final int top = child.getBottom() + params.bottomMargin;
                                final int bottom = top + mDivider.getIntrinsicHeight();
                                mDivider.setBounds(left, top, right, bottom);
                                mDivider.draw(c);
                        }
                }

                public void drawHorizontal(Canvas c, RecyclerView parent) {
                        final int top = parent.getPaddingTop();
                        final int bottom = parent.getHeight() - parent.getPaddingBottom();

                        final int childCount = parent.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                                final View child = parent.getChildAt(i);
                                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                                        .getLayoutParams();
                                final int left = child.getRight() + params.rightMargin;
                                final int right = left + mDivider.getIntrinsicHeight();
                                mDivider.setBounds(left, top, right, bottom);
                                mDivider.draw(c);
                        }
                }

                @Override
                public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                        if (mOrientation == VERTICAL_LIST) {
                                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
                        } else {
                                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
                        }
                }
        }

}



