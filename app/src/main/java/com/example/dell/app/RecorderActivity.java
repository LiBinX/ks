package com.example.dell.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bank.R;

import java.util.ArrayList;
import java.util.Calendar;

public class RecorderActivity extends Activity implements View.OnClickListener {

        private Button mbutton_sure, mbutton_cancel;
        private Spinner mspinner_type;
        private EditText  medittext_fee, medittext_remarks;
        private TextView  medittext_time;
        private RadioGroup mRadiogroup;

        private MySQLiteHelper mMysql;
        private SQLiteDatabase mDataBase;
        private ImageButton imageButtonBack;

        // 用来装日期的
        private Calendar calendar;
        //日历控件
        private DatePickerDialog dialog;
        //保存类型数据
        private String content_type, content_select_group;
        private ArrayList<String> Data = new ArrayList<String>();

        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.recorder);

                //获得控件
                mbutton_sure = (Button) findViewById(R.id.plan_sure);
                mbutton_cancel = (Button) findViewById(R.id.button_cancel);
                medittext_time = (TextView) findViewById(R.id.edit_text_time);
                medittext_fee = (EditText) findViewById(R.id.editText_fee);
                medittext_remarks = (EditText) findViewById(R.id.editText_remarks);
                mbutton_sure = (Button) findViewById(R.id.plan_sure);
                mspinner_type = (Spinner) findViewById(R.id.spinner_type);
                mRadiogroup = (RadioGroup) this.findViewById(R.id.radioGroup);
                imageButtonBack = (ImageButton) this.findViewById(R.id.imageButtonBack);

                //获取spinner的数据并操作
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.Select_item, android.R.layout.simple_spinner_item);

                //自定义下拉菜单样式定义在res/layout目录下
               adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //将ArrayAdapter 添加Spinner 对象中
                mspinner_type.setAdapter(adapter);

                //为各个控件添加事件监听
                medittext_time.setOnClickListener(this);
                imageButtonBack.setOnClickListener(this);
                mspinner_type.setOnItemSelectedListener(listener);
                mRadiogroup.setOnCheckedChangeListener(grouplistener);
                mbutton_sure.setOnClickListener(this);
                mbutton_cancel.setOnClickListener(this);
        }

        //单选按钮点击响应
        RadioGroup.OnCheckedChangeListener grouplistener = new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup arg0, int arg1) {
                        // TODO Auto-generated method stub
                        //获取变更后的选中项的ID
                        int radioButtonId = arg0.getCheckedRadioButtonId();
                        //根据ID获取RadioButton的实例
                        content_select_group = (((RadioButton) findViewById(radioButtonId)).getText()).toString();
                }
        };
        //spinner控件的响应事件
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        // An item was selected. You can retrieve the selected item using
                        content_type = mspinner_type.getItemAtPosition(pos).toString();
                        // Toast.makeText(getActivity(), "选中了"+content_type, Toast.LENGTH_LONG).show();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                        // Another interface callback
                }
        };
        //视图组件点击响应
        @Override
        public void onClick(View view) {
                switch (view.getId()) {
                        case R.id.plan_sure:
                                Data.clear();
                                Data.add(content_type);
                                Data.add(medittext_time.getText().toString());
                                Data.add(medittext_fee.getText().toString());
                                Data.add(medittext_remarks.getText().toString());
                                Data.add(content_select_group);
                                //    Toast.makeText(getActivity(), "click sure button" + Data, Toast.LENGTH_LONG).show();
                                WriteData(Data);
                                //   Toast.makeText(getActivity(), "click sure button" + Data, Toast.LENGTH_LONG).show();
                                break;
                        case R.id.button_cancel:
                                //     GetData()
                                this.finish();
                                break;
                        case R.id.imageButtonBack:
                                this.finish();
                                break;
                        case R.id.edit_text_time:
                                calendar = Calendar.getInstance();
                                //将获取到的年月日放进new出来的DatePickerDialog中
                                dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                                                @Override
                                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                calendar.set(year, monthOfYear, dayOfMonth);
                                                        //DateFormat日期格式化类
                                                medittext_time.setText(DateFormat.format("yyy-MM-dd", calendar));
                                                }
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                );
                                dialog.show();
                                break;
                        default:
                                break;
                }
        }

        public void WriteData(ArrayList<String> Data)
        {
                mMysql = new MySQLiteHelper(this, "finance.db", null, 1);
                //获取一个用于操作数据库的SQLiteDatabase实例
                mDataBase = mMysql.getReadableDatabase();
                ContentValues cv=new ContentValues();
                cv.put("Type",Data.get(0));
                cv.put("Time",Data.get(1));
                cv.put("Fee",Data.get(2));
                cv.put("Remarks",Data.get(3));
                cv.put("Budget",Data.get(4));

                mDataBase.insert("finance", "Type", cv);
                mDataBase.close();
                mMysql.close();
                //结束当前activity
                this.finish();
        }
}
