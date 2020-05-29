package cn.wildfire.chat.app.personalcenter;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aries.ui.view.radius.RadiusTextView;
import com.hjq.toast.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import cn.wildfire.chat.app.entity.BasicEntity;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.personalcenter.utils.ImageUtil;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfirechat.chat.R;
import fj.edittextcount.lib.FJEditTextCount;
import pub.devrel.easypermissions.EasyPermissions;

public class ReportActivity extends WfcBaseActivity {
    @BindView(R.id.fjEdit)
    FJEditTextCount fjEdit;
    @BindView(R.id.submit)
    RadiusTextView submit;
    @BindView(R.id.title)
    TextView title;
    private String titleContent;
    private BGASortableNinePhotoLayout photo;

    private static final int PRC_PHOTO_LOGO_PICKER = 1;

    private static final int RC_PHOTO_PREVIEW = 2;

    @Override
    protected int contentLayout() {
        return R.layout.activity_report;
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        titleContent = getIntent().getStringExtra("title");
        title.setText("举报与投诉");
        fjEdit.setText(titleContent + "###");
        photo = findViewById(R.id.photots);
        photo.setDelegate(new BGASortableNinePhotoLayout.Delegate() {
            @Override
            public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
                choicePhotoWrapper();
            }

            @Override
            public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                photo.removeItem(position);
            }

            @Override
            public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(ReportActivity.this)
                        .previewPhotos(models) // 当前预览的图片路径集合
                        .selectedPhotos(models) // 当前已选中的图片路径集合
                        .maxChooseCount(sortableNinePhotoLayout.getMaxItemCount()) // 图片选择张数的最大值
                        .currentPosition(position) // 当前预览图片的索引
                        .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                        .build();
                startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
            }

            @Override
            public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PRC_PHOTO_LOGO_PICKER) {
                photo.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));

            }

        } else if (requestCode == RC_PHOTO_PREVIEW) {
            photo.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
        }
    }

    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(4) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, PRC_PHOTO_LOGO_PICKER);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", PRC_PHOTO_LOGO_PICKER, perms);
        }
    }


    @OnClick(R.id.submit)
    public void onViewClicked() {

        if (photo.getData().size() == 0) {
            ToastUtils.show("请上传举报图片");
            return;
        }
        if (TextUtils.isEmpty(fjEdit.getText().trim())) {
            ToastUtils.show("请填写举报详细信息");
            return;
        }
        ld.setLoadingText("正在提交信息");
        ld.show();
        List<File> files = new ArrayList<>();
        for (int i = 0; i < photo.getData().size(); i++) {

            files.add(ImageUtil.scal(new File(photo.getData().get(i))));
        }
        ApiMethodFactory.getInstance().onSubmit(fjEdit.getText().trim(), files, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BasicEntity<String> baseBean = JSONObject.parseObject(response, new TypeReference<BasicEntity<String>>() {
                });
                if (baseBean.getCode() == 200) {
                    finish();
                }
                ToastUtils.show(baseBean.getMessage());
                dismissContentLoading();
            }
        });

    }

}
