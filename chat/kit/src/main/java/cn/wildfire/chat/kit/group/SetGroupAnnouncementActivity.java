package cn.wildfire.chat.kit.group;

import android.content.Context;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import cn.wildfire.chat.kit.AppServiceProvider;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.GroupInfo;

public class SetGroupAnnouncementActivity extends WfcBaseActivity {
    @BindView(R.id.announcementEditText)
    EditText announcementEditText;
    @BindView(R.id.title)
    TextView title;

    private MenuItem confirmMenuItem;
    private GroupInfo groupInfo;
    private MenuItem editMenuItem;
    @Override
    protected int contentLayout() {
        return R.layout.group_set_announcement_activity;
    }

    @Override
    protected void afterViews() {
        title.setText("群公告");
        groupInfo = getIntent().getParcelableExtra("groupInfo");
        if (groupInfo == null) {
            finish();
            return;
        }

        WfcUIKit.getWfcUIKit().getAppServiceProvider().getGroupAnnouncement(groupInfo.target, new AppServiceProvider.GetGroupAnnouncementCallback() {
            @Override
            public void onUiSuccess(GroupAnnouncement announcement) {
                if (isFinishing()) {
                    return;
                }
                if (TextUtils.isEmpty(announcementEditText.getText())) {
                    announcementEditText.setText(announcement.text);
                }
            }

            @Override
            public void onUiFailure(int code, String msg) {
                if (isFinishing()) {
                    return;
                }
            }
        });
    }

    @Override
    protected int menu() {
        return R.menu.group_set_group_name;
    }

    @Override
    protected void afterMenus(Menu menu) {
        editMenuItem = menu.findItem(R.id.edit);
        editMenuItem.setVisible(true);
        confirmMenuItem = menu.findItem(R.id.confirm);
        confirmMenuItem.setVisible(false);
        announcementEditText.setEnabled(false);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.confirm) {
            setGroupName();
            return true;
        }
        if (item.getItemId() == R.id.edit) {
            editMenuItem.setVisible(false);
            confirmMenuItem.setVisible(true);
            announcementEditText.setEnabled(true);
            announcementEditText.setFocusable(true);
            announcementEditText.setFocusableInTouchMode(true);
            announcementEditText.setSelection(announcementEditText.length());
            announcementEditText.requestFocus();
//            announcementEditText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                               public void run() {
                                   InputMethodManager inputManager =
                                           (InputMethodManager) announcementEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                   inputManager.showSoftInput(announcementEditText, 0);
                               }

                           },
                    600);
            announcementEditText.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
            if (announcementEditText.getText().toString().trim().length() > 0) {
                confirmMenuItem.setEnabled(true);
            } else {
                confirmMenuItem.setEnabled(false);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnTextChanged(R.id.announcementEditText)
    void onTextChanged() {
        if (confirmMenuItem != null) {
            confirmMenuItem.setEnabled(announcementEditText.getText().toString().trim().length() > 0);
        }
    }

    private void setGroupName() {
        String announcement = announcementEditText.getText().toString().trim();
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content("请稍后...")
                .progress(true, 100)
                .cancelable(false)
                .build();
        dialog.show();

        WfcUIKit.getWfcUIKit().getAppServiceProvider().updateGroupAnnouncement(groupInfo.target, announcement, new AppServiceProvider.UpdateGroupAnnouncementCallback() {
            @Override
            public void onUiSuccess(GroupAnnouncement announcement) {
                if (isFinishing()) {
                    return;
                }
                dialog.dismiss();
                Toast.makeText(SetGroupAnnouncementActivity.this, "设置群公告成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onUiFailure(int code, String msg) {
                if (isFinishing()) {
                    return;
                }
                dialog.dismiss();
                Toast.makeText(SetGroupAnnouncementActivity.this, "设置群公告失败: " + code + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
