package com.example.dell.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bank.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTab02 extends Fragment {

        List<Map<String, String>> gruops = new ArrayList<Map<String, String>>();

        List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();
        ImageView loggroup, logchild;
        TextView textgroup, textchild;
        private ExpandableListView ep;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View messageLayout = inflater.inflate(R.layout.stream, container, false);


                final ExpandableListAdapter adapter = new BaseExpandableListAdapter() {


                        //设置组视图的图片
                        int[] logos = new int[]{R.drawable.cloth, R.drawable.zhu, R.drawable.getmoney};
                        //设置组视图的显示文字
                        private String[] category = new String[]{"衣", "食", "住"};
                        //子视图显示文字
                        private String[][] subcategory = new String[][]{
                                {"一月", "一月", "一月", "一月", "一月", "一月"},
                                {"一月", "一月", "一月", "一月", "一月", "一月"},
                                {"一月", "一月", "一月", "一月", "一月", "一月"}

                        };

                        //子视图图片
                        public int[][] sublogos = new int[][]{
                                {R.drawable.cloth, R.drawable.cloth,
                                        R.drawable.cloth, R.drawable.cloth,
                                        R.drawable.cloth, R.drawable.cloth},
                                {R.drawable.cloth, R.drawable.cloth,
                                        R.drawable.cloth, R.drawable.cloth,
                                        R.drawable.cloth, R.drawable.cloth},
                                {R.drawable.cloth, R.drawable.cloth,
                                        R.drawable.cloth, R.drawable.cloth,
                                        R.drawable.cloth, R.drawable.cloth}};

                        @Override
                        public int getGroupCount() {
                                return category.length;
                        }

                        @Override
                        public int getChildrenCount(int i) {
                                return subcategory[i].length;
                        }

                        @Override
                        public Object getGroup(int i) {
                                return category[i];
                        }

                        @Override
                        public Object getChild(int i, int i1) {
                                return subcategory[i][i1];
                        }

                        @Override
                        public long getGroupId(int i) {
                                return i;
                        }

                        @Override
                        public long getChildId(int i, int i1) {
                                return i1;
                        }

                        @Override
                        public boolean hasStableIds() {
                                return true;
                        }

                        @Override
                        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
                                if (view == null) {
                                        view = getActivity().getLayoutInflater().inflate(R.layout.groups, viewGroup, false);
                                }

                                //创建一级菜单视图
//                    LinearLayout glayout = (LinearLayout) LayoutInflater.from(getActivity().getBaseContext()).inflate(R.layout.groups, null);
                                //获得ImageView视图
                                loggroup = (ImageView) view.findViewById(R.id.ImageGroup);
                                loggroup.setImageResource(logos[i]);
                                // 获得TextView视图
                                textgroup = (TextView) view.findViewById(R.id.textGroup);
                                textgroup.setText(category[i]);
                                return view;
                        }

                        @Override
                        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
                                //需要获得布局文件的对象
                                if (view == null) {
                                        view = getActivity().getLayoutInflater().inflate(R.layout.childs, viewGroup, false);
                                }

                                //创建一级菜单视图
                                //   LinearLayout glayout = (LinearLayout) LayoutInflater.from(getActivity().getBaseContext()).inflate(R.layout.childs, null);
                                //获得ImageView视图
                                logchild = (ImageView) view.findViewById(R.id.imageChild);
                                logchild.setImageResource(sublogos[i][i1]);
                                // 获得TextView视图
                                textchild = (TextView) view.findViewById(R.id.textChild);
                                textchild.setText(subcategory[i][i1]);


                                return view;
                        }

                        @Override
                        public boolean isChildSelectable(int i, int i1) {
                                return true;
                        }
                };

                ep = (ExpandableListView) messageLayout.findViewById(R.id.expandable);

                ep.setAdapter(adapter);

                //为ExpandableListView的子列表单击事件设置监听器
                ep.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v,
                                                    int groupPosition, int childPosition, long id) {
                                // TODO Auto-generated method stub
                                Toast.makeText(getActivity(), "你单击了："
                                        + adapter.getChild(groupPosition, childPosition), Toast.LENGTH_LONG).show();
                                return true;
                        }
                });

                //    setListData();
                return messageLayout;
        }

        /**
         * 设置列表内容
         */
        public void setListData() {
                // 创建一级条目标题
                String[] names = {"1月", "2月"};
                //创建二级标题
                String[][] child_names = {{"费用", "费用", "费用"}, {"费用", "费用", "费用"}};

                for (int i = 0; i < names.length; i++) {
                        Map<String, String> namedata = new HashMap<String, String>();
                        namedata.put("names", names[i]);
                        gruops.add(namedata);

                        List<Map<String, String>> child_map = new ArrayList<Map<String, String>>();
                        for (int j = 0; j < child_names[0].length; j++) {
                                Map<String, String> mapcs = new HashMap<String, String>();
                                mapcs.put("child_names", child_names[i][j]);
                                child_map.add(mapcs);
//                System.out.println(child_map);
                        }

                        childs.add(child_map);
//            System.out.println(childs);
                }
                /**
                 * 创建ExpandableList的Adapter容器 参数: 1.上下文 2.一级集合 3.一级样式文件 4. 一级条目键值
                 * 5.一级显示控件名 6. 二级集合 7. 二级样式 8.二级条目键值 9.二级显示控件名
                 */
                SimpleExpandableListAdapter sela = new SimpleExpandableListAdapter(
                        getActivity(), gruops, R.layout.groups, new String[]{"names"},
                        new int[]{R.id.textGroup}, childs, R.layout.childs,
                        new String[]{"child_names"}, new int[]{R.id.textChild, R.id.imageChild});

                // 加入列表
                ep.setAdapter(sela);
        }

}
