package cn.wildfire.chat.app.personalcenter.personalInformationActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.MaterialDialog;
import com.allen.library.SuperTextView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.gson.Gson;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.entity.JsonBean;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.shop.utils.GetJsonDataUtil;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfire.chat.kit.WfcScheme;
import cn.wildfire.chat.kit.common.OperateResult;
import cn.wildfire.chat.kit.qrcode.QRCodeActivity;
import cn.wildfire.chat.kit.third.utils.ImageUtils;
import cn.wildfire.chat.kit.user.ChangeMyNameActivity;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.ModifyMyInfoEntry;
import cn.wildfirechat.model.ModifyMyInfoType;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptDialog;

import static cn.wildfirechat.model.ModifyMyInfoType.Modify_Address;

/**
 * 个人信息
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.portraitImageView)
    ImageView portraitImageView;
    @BindView(R.id.modify_portrait)
    LinearLayout modifyPortrait;
    @BindView(R.id.nickyName)
    SuperTextView nickyName;
    @BindView(R.id.meng_account)
    SuperTextView mengAccount;
    @BindView(R.id.user_qr_code)
    SuperTextView userQrCode;
    @BindView(R.id.sex)
    SuperTextView sex;
    @BindView(R.id.area)
    SuperTextView area;

    private static final int REQUEST_CODE_PICK_IMAGE = 100;


    private UserViewModel userViewModel;

    private UserInfo userInfo;

    private PromptDialog promptDialog;


    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    private Observer<List<UserInfo>> userInfoLiveDataObserver = new Observer<List<UserInfo>>() {
        @Override
        public void onChanged(@Nullable List<UserInfo> userInfos) {
            if (userInfos == null) {
                return;
            }
            for (UserInfo info : userInfos) {
                if (info.uid.equals(userViewModel.getUserId())) {
                    userInfo = info;
                    updateUserInfo(userInfo);
                    break;
                }
            }
        }
    };

    @Override
    protected int contentLayout() {
        return R.layout.activity_user_info;
    }

    private void updateUserInfo(UserInfo userInfo) {
        GlideApp
                .with(this)
                .load(userInfo.portrait)
                .placeholder(R.mipmap.avatar_def)
                .transforms(new CenterCrop(), new RoundedCorners(10))
                .into(portraitImageView);
        nickyName.setRightString(userInfo.displayName);
        area.setRightString(userInfo.address);

        mengAccount.setRightString(TextUtils.isEmpty(userInfo.email) ? "未设置" : userInfo.email);
        switch (userInfo.gender) {
            case 0:
                sex.setRightString("未设置");
                break;
            case 1:
                sex.setRightString("男");
                break;
            case 2:
                sex.setRightString("女");
                break;
        }

    }

    @Override
    protected void init() {
        toolbarTitle.setText("个人信息");

        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        initCityJson();

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.userInfoLiveData().observeForever(userInfoLiveDataObserver);
        userViewModel.getUserInfoAsync(userViewModel.getUserId(), true)
                .observe(this, info -> {
                    userInfo = info;
                    if (userInfo != null) {
                        updateUserInfo(userInfo);
                    }
                });
        userViewModel.userInfoLiveData().observeForever(userInfoLiveDataObserver);
    }

    @OnClick({R.id.modify_portrait, R.id.nickyName, R.id.meng_account, R.id.user_qr_code, R.id.sex, R.id.area})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.modify_portrait:
                //头像布局
                ImagePicker.picker().pick(this, REQUEST_CODE_PICK_IMAGE);
                break;
            case R.id.nickyName:
                //昵称
                startActivity(new Intent(UserInfoActivity.this, ChangeMyNameActivity.class));
                break;
            case R.id.meng_account:
                //萌聊号
                if (TextUtils.equals(mengAccount.getRightString(), "未设置")) {
                    //设置萌聊账户
                    startActivityForResult(new Intent(UserInfoActivity.this, ChangeAccountActivity.class), 1000);
                }
                break;
            case R.id.user_qr_code:
                //我的二维码
                UserQrCode();
                break;
            case R.id.sex:
                //性别
                showMenu();
                break;
            case R.id.area:
                //地区
                showPickerView();
                break;
        }
    }

    /**
     * 我的二维码
     */
    private void UserQrCode() {
        String uid = ChatManager.Instance().getUserId();
        UserInfo userInfo = ChatManager.Instance().getUserInfo(uid, true);
        String qrCodeValue = WfcScheme.QR_CODE_PREFIX_USER + userInfo.uid;
        //startActivity(QRCodeActivity.buildQRCodeIntent(this, "我的二维码", userInfo.portrait, qrCodeValue));
        startActivity(QRCodeActivity.buildQRCodeIntent(this, "我的二维码", userInfo.portrait, userInfo.displayName, userInfo.social, qrCodeValue));
    }

    /**
     * 性别弹窗
     */
    private void showMenu() {
        PromptButton cancle = new PromptButton("取消", null);
        cancle.setTextColor(Color.parseColor("#0076ff"));
        //设置显示的文字大小及颜色
        promptDialog.getAlertDefaultBuilder().textSize(12).textColor(ContextCompat.getColor(this, R.color.blue0));
        //默认两个按钮为Alert对话框，大于三个按钮的为底部SHeet形式展现
        promptDialog.showAlertSheet("", true, cancle,
                new PromptButton("男", button -> onChangeUserInfo("1", ModifyMyInfoType.Modify_Gender)),
                new PromptButton("女", button -> onChangeUserInfo("2", ModifyMyInfoType.Modify_Gender))
        );
    }

    /**
     * 城市选择框
     */
    private void showPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = options1Items.size() > 0 ?
                    options1Items.get(options1).getPickerViewText() : "";

            String opt2tx = options2Items.size() > 0
                    && options2Items.get(options1).size() > 0 ?
                    options2Items.get(options1).get(options2) : "";


            String tx = opt1tx + opt2tx;
            onChangeUserInfo(tx, Modify_Address);
            area.setRightString(tx);
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        if (options1Items.size() > 0 && options2Items.size() > 0) {
            pvOptions.setPicker(options1Items, options2Items);//三级选择器
        }
        pvOptions.show();
    }

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

    private void onChangeUserInfo(String name, ModifyMyInfoType modifyMyInfoType) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content("修改中...")
                .progress(true, 100)
                .build();
        dialog.show();
        ModifyMyInfoEntry entry = new ModifyMyInfoEntry(modifyMyInfoType, name);
        userViewModel.modifyMyInfo(Collections.singletonList(entry)).observe(this, booleanOperateResult -> {
            if (booleanOperateResult.isSuccess()) {
                Toast.makeText(UserInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            String imagePath = ImageUtils.genThumbImgFile(images.get(0).path).getAbsolutePath();
            MutableLiveData<OperateResult<Boolean>> result = userViewModel.updateUserPortrait(imagePath);
            result.observe(this, booleanOperateResult -> {
                if (booleanOperateResult.isSuccess()) {
                    Toast.makeText(UserInfoActivity.this, "更新头像成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserInfoActivity.this, "更新头像失败: " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (requestCode == 1000 && resultCode == 100) {
            String account = data.getStringExtra("account");
            mengAccount.setRightString(account);

        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userViewModel.userInfoLiveData().removeObserver(userInfoLiveDataObserver);
    }
}
