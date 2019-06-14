package com.example.dell.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bank.R;

import java.util.ArrayList;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>  {

    private Context context;
    private ArrayList<FragmentPlan.PlanData> arrayList=new ArrayList<>();
    private OnItemLongClickLitener onItemLongClickListener;
    private OnItemClickListener mOnItemClickListener;
    public HomeAdapter(Context context, ArrayList<FragmentPlan.PlanData> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = View.inflate(context, R.layout.planitem, null);
        final MyViewHolder holder = new MyViewHolder(view);
      //  MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.planitem, parent,false));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getPosition();
                onItemLongClickListener.onLongItemClick(holder.itemView,position);
                //返回true 表示消耗了事件 事件不会继续传递
                return true;
            }

    });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getPosition(); // 1
                mOnItemClickListener.onItemClick(holder.itemView,position); // 2
            }
        });

        return holder;
    }

    //onBindViewHolder负责将每个子项holder绑定数据。俩参数分别是RecyclerView.ViewHolder holder, int position；
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        FragmentPlan.PlanData data = arrayList.get(position);
        holder.showMorningplan.setText(data.getMoringPlan());
        holder.showAfternoonplan.setText(data.getAfterPlan());
        holder.showNightplan.setText(data.getNightPlan());
        holder.iv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.iv_click.setSelected(!holder.iv_click.isSelected());
                Toast.makeText(context,"已完成",Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setTag(position);
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickLitener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickLitener{
        void onLongItemClick(View view,int position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView showMorningplan,showAfternoonplan,showNightplan;
        ImageView iv_click;

        public MyViewHolder(View view)
        {
            super(view);
            showMorningplan = (TextView) view.findViewById(R.id.showmorningplan);
            showAfternoonplan = (TextView) view.findViewById(R.id.showafternoonplan);
            showNightplan = (TextView) view.findViewById(R.id.shownightplan);
            iv_click = view.findViewById(R.id.iv_click);
        }
    }
}