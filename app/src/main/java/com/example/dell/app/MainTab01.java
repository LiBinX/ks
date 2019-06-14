package com.example.dell.app;

import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bank.R;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainTab01 extends Fragment implements View.OnClickListener {
        private Button mbutton_sure, mbutton_cancel;
        private Spinner mspinner_type;
        private EditText medittext_time, medittext_fee, medittext_remarks;
        //保存类型数据
        private String content_type;
        private ArrayList<String> Data = new ArrayList<String>();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                View view = inflater.inflate(R.layout.recorder, container, false);

                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

                //获得控件
                mbutton_sure = (Button) view.findViewById(R.id.plan_sure);
                mbutton_cancel = (Button) view.findViewById(R.id.button_cancel);
                medittext_time = (EditText) view.findViewById(R.id.edit_text_time);
                medittext_fee = (EditText) view.findViewById(R.id.editText_fee);
                medittext_remarks = (EditText) view.findViewById(R.id.editText_remarks);
                mbutton_sure = (Button) view.findViewById(R.id.plan_sure);
                mspinner_type = (Spinner) view.findViewById(R.id.spinner_type);


                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.Select_item, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                mspinner_type.setAdapter(adapter);

                mspinner_type.setOnItemSelectedListener(listener);
                mbutton_sure.setOnClickListener(this);
                mbutton_cancel.setOnClickListener(this);
                return view;
        }

        OnItemSelectedListener listener = new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        // An item was selected. You can retrieve the selected item using
                        content_type = mspinner_type.getItemAtPosition(pos).toString();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                        // Another interface callback
                }
        };

        public void onClick(View view) {

                switch (view.getId()) {
                        case R.id.plan_sure:
                                Data.clear();
                                Data.add(content_type);
                                Data.add(medittext_time.getText().toString());
                                Data.add(medittext_fee.getText().toString());
                                Data.add(medittext_remarks.getText().toString());
                                //    Toast.makeText(getActivity(), "click sure button" + Data, Toast.LENGTH_LONG).show();
                                WriteData(Data);
                                //   Toast.makeText(getActivity(), "click sure button" + Data, Toast.LENGTH_LONG).show();
                                break;
                        case R.id.button_cancel:
                                GetData();
                                break;
                        default:
                                break;
                }
        }

        public void GetData() {
                HttpURLConnection connection = null;

                try {
                        //设置url
                        URL url = new URL("http://10.135.188.23:8080/test.php");
                        //获得url连接
                        connection = (HttpURLConnection) url.openConnection();
                        //设置可写，默认为false
                        connection.setDoOutput(true);
                        //设置可读，默认为true，一般不写
                        connection.setDoInput(true);
                        //设置POST传参，默认为GET
                        connection.setRequestMethod("POST");

                        connection.setUseCaches(false);
                        connection.setInstanceFollowRedirects(true);
                        connection.setRequestProperty("contentType", "text/html");
                        connection.connect();

                        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                        String content = "username=" + URLEncoder.encode("zcl", "utf-8");
                        out.writeBytes(content);
                        //刷新
                        out.flush();

                        out.close();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                        //设置编码,否则中文乱码
                        String line = "";
                        System.out.println(reader);
                        while ((line = reader.readLine()) != null) {
                                Toast.makeText(getActivity(), line.toString(), Toast.LENGTH_LONG).show();
                        }
                        reader.close();
                        connection.disconnect();
                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                        connection.disconnect();
                }

        }

        public void WriteData(ArrayList<String> Data) {
                String outputFile = "//a.xls";
                try {

                        // 判断是否存在SD卡
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        } else {
                                Toast.makeText(getActivity(), "sd卡不存在", Toast.LENGTH_LONG).show();
                                return;
                        }

                        String sdCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
                        File file = new File(sdCardRoot + outputFile);
                        if (!file.exists()) {

                                // 创建新的Excel 工作簿
                                HSSFWorkbook workbook = new HSSFWorkbook();

                                // 在Excel工作簿中建一工作表，其名为缺省值
                                // 如要新建一名为"效益指标"的工作表，其语句为：
                                // HSSFSheet sheet = workbook.createSheet("效益指标");
                                HSSFSheet sheet = workbook.createSheet("消费记录");
                                // 在索引0的位置创建行（最顶端的行）
                                HSSFRow row = sheet.createRow((short) 0);

                                //创建表头
                                HSSFCell empCodeCell = row.createCell(0);
                                empCodeCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                empCodeCell.setCellValue("类型");

                                HSSFCell empNameCell = row.createCell(1);
                                empNameCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                empNameCell.setCellValue("时间");

                                HSSFCell sexCell = row.createCell(2);
                                sexCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                sexCell.setCellValue("费用");

                                HSSFCell birthdayCell = row.createCell(3);
                                birthdayCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                birthdayCell.setCellValue("备注");

                                //写入数据
                                int num = sheet.getLastRowNum();
                                //   Toast.makeText(getActivity(), num, Toast.LENGTH_LONG).show();
                                row = sheet.createRow(num + 1);
                                for (int i = 0; i < 4; i++) {

                                        empCodeCell = row.createCell(i);
                                        empCodeCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                        empCodeCell.setCellValue(Data.get(i));
                                }

                                // 新建一输出文件流
                                FileOutputStream fOut = new FileOutputStream(sdCardRoot + outputFile);
                                // 把相应的Excel 工作簿存盘
                                workbook.write(fOut);
                                fOut.flush();
                                // 操作结束，关闭文件
                                fOut.close();
                                workbook.close();
                        } else {
                                //更改数据
                                try {
                                        FileInputStream fs = new FileInputStream(sdCardRoot + outputFile);
                                        POIFSFileSystem ps = new POIFSFileSystem(fs);
                                        // 使用POI提供的方法得到excel的信息
                                        HSSFWorkbook wb = new HSSFWorkbook(ps);
                                        HSSFSheet sheet = wb.getSheetAt(0);
                                        // 获取到工作表，因为一个excel可能有多个工作表

                                        FileOutputStream out = new FileOutputStream(sdCardRoot + outputFile);
                                        HSSFCell empCodeCell = null;
                                        //写入数据
                                        int num = sheet.getLastRowNum();
                                        //   Toast.makeText(getActivity(), num, Toast.LENGTH_LONG).show();
                                        HSSFRow row = sheet.createRow(num + 1);
                                        for (int i = 0; i < 4; i++) {
                                                empCodeCell = row.createCell(i);
                                                empCodeCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                                empCodeCell.setCellValue(Data.get(i));
                                        }
                                        out.flush();
                                        wb.write(out);
                                        out.close();

                                } catch (Exception e) {
                                        Toast.makeText(getActivity(), "写入失败" + e, Toast.LENGTH_LONG).show();
                                }

                        }
                } catch (Exception e) {
                        Toast.makeText(getActivity(), "写入失败" + e, Toast.LENGTH_LONG).show();
                } finally {
                        Toast.makeText(getActivity(), "关闭", Toast.LENGTH_LONG).show();
                }
        }
}

