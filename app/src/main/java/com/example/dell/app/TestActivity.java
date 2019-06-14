package com.example.dell.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bank.R;


public class TestActivity extends Activity implements View.OnClickListener {

        private TextView showTime,showType,showRemarks,showFee;
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.test);
                showTime =(TextView)this.findViewById(R.id.texttimeshow);
                //showType =(TextView)this.findViewById(R.id.texttypeshow);
                showRemarks =(TextView)this.findViewById(R.id.textremarksshow);
                showFee =(TextView)this.findViewById(R.id.textfeeshow);

                //接受数据,先获得
                Bundle bundle = this.getIntent().getExtras();
                String time = bundle.getString("Time");
                String Type = bundle.getString("Type");
                String Budget = bundle.getString("Budget");
                double Fee = bundle.getDouble("Fee");
                int ID = bundle.getInt("ID");
                String Remarks = bundle.getString("Remarks");
                showTime.setText(time);
                showFee.setText(String.valueOf(Fee));
                showRemarks.setText(Remarks);
        }

        @Override
        public void onClick(View view) {

        }
}



