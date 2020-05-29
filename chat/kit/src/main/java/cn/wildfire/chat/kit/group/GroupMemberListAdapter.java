package cn.wildfire.chat.kit.group;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.ui.view.radius.RadiusTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.contact.model.UIUserInfo;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.GroupInfo;
import cn.wildfirechat.model.GroupMember;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

public class GroupMemberListAdapter extends RecyclerView.Adapter<GroupMemberListAdapter.MemberViewHolder> {
    private GroupInfo groupInfo;
    private List<UserInfo> members;
    private OnMemberClickListener onMemberClickListener;
    private   List<GroupMember> groupMembers;

    public List<GroupMember> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public GroupMemberListAdapter(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public void setMembers(List<UserInfo> members) {
        this.members = members;
    }


    public void addMembers(List<UserInfo> members) {
        int startIndex = this.members.size();
        this.members.addAll(members);
        notifyItemRangeInserted(startIndex, members.size());
    }

    public void updateMember(UserInfo userInfo) {
        if (this.members == null) {
            return;
        }
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).uid.equals(userInfo.uid)) {
                members.set(i, userInfo);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void removeMembers(List<String> memberIds) {
        Iterator<UserInfo> iterator = members.iterator();
        while (iterator.hasNext()) {
            UserInfo userInfo = iterator.next();
            if (memberIds.contains(userInfo.uid)) {
                iterator.remove();
                memberIds.remove(userInfo.uid);
            }

            if (memberIds.size() == 0) {
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void setOnMemberClickListener(OnMemberClickListener onMemberClickListener) {
        this.onMemberClickListener = onMemberClickListener;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.conversation_item_member_info, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        if(groupMembers!=null){
            holder.bindUserInfo(members.get(position),groupMembers.get(position));

        }else {
            holder.bindUserInfo(members.get(position),null);
        }

    }

    @Override
    public int getItemCount() {
        if (members == null) {
            return 0;
        }
        return members.size();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.portraitImageView)
        ImageView portraitImageView;
        @BindView(R.id.nameTextView)
        TextView nameTextView;
        private UserInfo userInfo;
        @BindView(R.id.type)
        TextView typeView;
        @OnClick(R.id.portraitImageView)
        void onClick() {
            if (onMemberClickListener == null) {
                return;
            }
            if (userInfo != null) {
                onMemberClickListener.onUserMemberClick(userInfo);
            }
        }

        public MemberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindUserInfo(UserInfo userInfo, Object object) {
            if (userInfo == null) {
                nameTextView.setText("");
                portraitImageView.setImageResource(R.mipmap.default_header);
                return;
            }
            this.userInfo = userInfo;
            nameTextView.setVisibility(View.VISIBLE);
            nameTextView.setText(userInfo.displayName);
            GroupMember groupMember = (GroupMember) object;
            Glide.with(portraitImageView).load(userInfo.portrait).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.default_header)).into(portraitImageView);
            if (groupMember!=null){
                if (groupMember.type== GroupMember.GroupMemberType.Normal){
                    typeView.setVisibility(View.GONE);
                }else if (groupMember.type== GroupMember.GroupMemberType.Manager){
                    typeView.setText("管理员");
                    typeView.setVisibility(View.VISIBLE);
                }else if (groupMember.type== GroupMember.GroupMemberType.Owner){
                    typeView.setText("群主");
                    typeView.setVisibility(View.VISIBLE);
                }
            }else {
                typeView.setVisibility(View.GONE);
            }

        }


    }

    public interface OnMemberClickListener {
        void onUserMemberClick(UserInfo userInfo);
    }
}
