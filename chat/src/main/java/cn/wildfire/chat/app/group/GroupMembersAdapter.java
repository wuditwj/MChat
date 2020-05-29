package cn.wildfire.chat.app.group;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import cn.wildfire.chat.kit.GlideApp;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.UserInfo;

public class GroupMembersAdapter  extends BaseQuickAdapter<UserInfo, BaseViewHolder> {
    public GroupMembersAdapter(int layoutResId, @Nullable List<UserInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfo item) {
        ImageView imageView = helper.getView(R.id.image);
        GlideApp
                .with(getContext())
                .load(item.portrait)
                .placeholder(R.mipmap.avatar_def)
                .transforms(new CenterCrop(), new RoundedCorners(10))
                .into(imageView);
        helper.setText(R.id.name,item.displayName);
    }
}
