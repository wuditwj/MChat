package cn.wildfire.chat.app.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;

public class EditTextUtils {
    public static void afterDotTwo(final EditText editText, TextView textView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss = s.toString();
                // 限制最多能输入9位整数
                if (s.toString().contains(".")) {
                    if (s.toString().indexOf(".") > 9) {
                        s = s.toString().subSequence(0,9) + s.toString().substring(s.toString().indexOf("."));
                        editText.setText(s);
                        editText.setSelection(9);
                    }
                }else {
                    if (s.toString().length() > 9){
                        s = s.toString().subSequence(0,9);
                        editText.setText(s);
                        editText.setSelection(9);
                    }
                }
                // 判断小数点后只能输入两位
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                //如果第一个数字为0，第二个不为点，就不允许输入
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
                if (s.toString().length() >0){
                    if (Double.parseDouble(s.toString()) >2000){
                        s="2000";
                        ToastUtils.show("单个红包不可超过2000元");
                        editText.setText(s.toString());
                        //textView.setText("￥" +s.toString());
                        textView.setText("￥" + String.format("%.2f", Double.parseDouble(s.toString())));
                        editText.setSelection(s.length());
                    }else {
//                        textView.setText("￥" + s.toString());
                        textView.setText("￥" + String.format("%.2f", Double.parseDouble(s.toString())));
                    }
                }else {
//                    textView.setText("￥"+0);
                    textView.setText("￥" + String.format("%.2f", 0.00));
                }


            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().trim() != null && !editText.getText().toString().trim().equals("")) {
                    if (editText.getText().toString().trim().substring(0, 1).equals(".")) {
                        editText.setText("0" + editText.getText().toString().trim());
                        editText.setSelection(1);
                    }
                }
            }
        });
    }
}
