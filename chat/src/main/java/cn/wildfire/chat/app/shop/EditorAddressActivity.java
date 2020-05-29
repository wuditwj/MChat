package cn.wildfire.chat.app.shop;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aries.ui.view.radius.RadiusTextView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.shop.entity.AddressInfo;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.entity.JsonBean;
import cn.wildfire.chat.app.shop.utils.GetJsonDataUtil;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.remote.ChatManager;

/**
 * 添加地址/编辑地址页面
 */
public class EditorAddressActivity extends BaseActivity {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.choose_area)
    TextView chooseArea;
    @BindView(R.id.address_detail)
    EditText addressDetail;
    @BindView(R.id.save)
    RadiusTextView save;

    private AddressInfo addressInfo;
    //省
    private List<JsonBean> options1Items = new ArrayList<>();
    //市
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    //区
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();


    @Override
    protected int contentLayout() {
        return R.layout.activity_editor_address;
    }


    @OnClick({R.id.choose_area, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_area:
                showPickerView();
                break;
            case R.id.save:
                if (addressInfo != null) {
                    //修改地址
                    modifyAddress();
                } else {
                    //新增地址
                    addAddress();
                }
                break;
        }
    }

    protected void init() {
        addressInfo = getIntent().getParcelableExtra("addressInfo");
        if (addressInfo != null) {
            toolbarTitle.setText("修改地址");
            name.setText(addressInfo.getName());
            mobile.setText(addressInfo.getMobile());
            addressDetail.setText(addressInfo.getAddr());
            chooseArea.setText(addressInfo.getAreaName());
        } else {
            toolbarTitle.setText("新增地址");
        }
        initCityJson();
    }

    /**
     * 修改地址
     */
    private void modifyAddress() {
        ApiMethodFactory.getInstance().modifyAddress(addressInfo.getAddrId(),
                name.getText().toString().trim(),
                mobile.getText().toString().trim(),
                chooseArea.getText().toString().trim(),
                addressDetail.getText().toString().trim(), new HttpHandler() {
                    @Override
                    public void requestSuccess(String response) {
                        BaseBean<String> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                        });
                        if (obj.getCode() == 200) {
                            finish();
                        }
                        ToastUtils.show(obj.getMessage());
                    }
                });
    }

    /**
     * 添加地址
     */
    private void addAddress() {
        ApiMethodFactory.getInstance().addAddress(ChatManager.Instance().getUserId(),
                name.getText().toString().trim(),
                mobile.getText().toString().trim(),
                chooseArea.getText().toString().trim(),
                addressDetail.getText().toString().trim(), new HttpHandler() {
                    @Override
                    public void requestSuccess(String response) {
                        BaseBean<String> obj = JSONObject.parseObject(response, new TypeReference<BaseBean<String>>() {
                        });
                        if (obj.getCode() == 200) {
                            finish();
                        }
                        ToastUtils.show(obj.getMessage());
                    }
                });
    }

    /**
     * 初始化地区数据
     */
    private void initCityJson() {
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    /**
     * 弹出选择器
     */
    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = options1Items.size() > 0 ?
                    options1Items.get(options1).getPickerViewText() : "";

            String opt2tx = options2Items.size() > 0
                    && options2Items.get(options1).size() > 0 ?
                    options2Items.get(options1).get(options2) : "";

            String opt3tx = options2Items.size() > 0
                    && options3Items.get(options1).size() > 0
                    && options3Items.get(options1).get(options2).size() > 0 ?
                    options3Items.get(options1).get(options2).get(options3) : "";

            String tx = opt1tx + opt2tx + opt3tx;
            chooseArea.setText(tx);
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        if (options1Items.size() > 0 && options2Items.size() > 0 && options3Items.size() > 0) {
            pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        }
        pvOptions.show();
    }

}
