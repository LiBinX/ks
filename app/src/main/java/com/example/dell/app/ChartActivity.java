package com.example.dell.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bank.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2018/11/28.
 */

public class ChartActivity extends Activity{

    private LineChartView lineChart;

    //String[] data = new String[1023];
    //int[] score = new int[1023];

    String[] data = {"0:00","04:00","08:00","12:00","16:00","20:00","24:00"};
    int[] score = {0,-3,-2,5,7,-6,-1};
    //String[] data = getIntent().getStringArrayExtra("time");
    //int[] score = getIntent().getIntArrayExtra("fee");

    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        lineChart = findViewById(R.id.chart);
        //InputStream inputStream = getResources().openRawResource(R.raw.a);
        getAxisXLable();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化

    }
    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#7EC0EE"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);////是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //x轴的属性设置
        Axis axisx = new Axis();
        axisx.setHasTiltedLabels(true);
        axisx.setTextColor(Color.GRAY);
        axisx.setTextSize(15);
        axisx.setMaxLabelChars(6);
        axisx.setValues(mAxisXValues);
        data.setAxisXBottom(axisx);
        axisx.setHasLines(true);

        //y轴
        Axis axisy = new Axis();
        axisy.setName("温度（°）");
        axisy.setTextSize(15);
        data.setAxisYLeft(axisy);

        //数据添加
        //data.setBaseValue(Float.NEGATIVE_INFINITY);
        //lineChart.setLineChartData(data);
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float)2);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        /**注：下面的7，10只是代表一个数字去类比而已
          * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
          */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);

    }

    //设置X 轴的显示
    private void getAxisPoints() {
        for (int i = 0; i < score.length; i++) {
             mPointValues.add(new PointValue(i, score[i]));
            }
    }

    //图表的每个点的显示
    private void getAxisXLable() {
        for (int i = 0; i < data.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(data[i]));
        }
    }

}
