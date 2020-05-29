package cn.wildfire.chat.kit.conversation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.ConversationInfo;
import cn.wildfirechat.model.GroupMember;
import cn.wildfirechat.model.UserInfo;

public class ConversationMemberAdapter extends RecyclerView.Adapter<ConversationMemberAdapter.MemberViewHolder> {
    private List<UserInfo> members;
    private boolean enableAddMember;
    private boolean enableRemoveMember;
    private OnMemberClickListener onMemberClickListener;
    private   List<GroupMember> groupMembers;
    private boolean isShow =false;
    private ConversationInfo conversationInfo;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }


    public ConversationMemberAdapter(ConversationInfo conversationInfo, boolean enableAddMember, boolean enableRemoveMember) {
        this.conversationInfo = conversationInfo;
        this.enableAddMember = enableAddMember;
        this.enableRemoveMember = enableRemoveMember;
    }

    public void setMembers(List<UserInfo> members) {
        this.members = members;
    }


    public void setGroupMembers(List<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
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
        if (position < members.size()) {
            if(groupMembers!=null){
                holder.bindUserInfo(members.get(position),groupMembers.get(position));

            }else {
                holder.bindUserInfo(members.get(position),null);
            }
        } else {
            if (position == members.size()) {
                if (enableAddMember) {
                    holder.bindAddMember();
                } else if (enableRemoveMember) {
                    holder.bindRemoveMember();
                }
            } else if (position == members.size() + 1 && enableRemoveMember) {
                holder.bindRemoveMember();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (members == null) {
            return 0;
        }
        int count = members.size();
        if (enableAddMember) {
            count++;
        }
        if (enableRemoveMember) {
            count++;
        }
        return count;
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.portraitImageView)
        ImageView portraitImageView;
        @BindView(R.id.nameTextView)
        TextView nameTextView;
        @BindView(R.id.type)
        TextView typeView;
        private UserInfo userInfo;
        private int type = TYPE_USER;
        private static final int TYPE_USER = 0;
        private static final int TYPE_ADD = 1;
        private static final int TYPE_REMOVE = 2;

        @OnClick(R.id.portraitImageView)
        void onClick() {
            if (onMemberClickListener == null) {
                return;
            }
            switch (type) {
                case TYPE_USER:
                    if (userInfo != null) {
                        onMemberClickListener.onUserMemberClick(userInfo);
                    }
                    break;
                case TYPE_ADD:
                    onMemberClickListener.onAddMemberClick();
                    break;
                case TYPE_REMOVE:
                    onMemberClickListener.onRemoveMemberClick();
                    break;
                default:
                    break;
            }
        }

        public MemberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindUserInfo(UserInfo userInfo,Object object) {
            if (userInfo == null) {
                nameTextView.setText("");

                Glide.with(portraitImageView).load(R.mipmap.default_header)
                        .apply(new RequestOptions().centerCrop().placeholder(R.mipmap.default_header)
                                . transforms(new CenterCrop(), new RoundedCorners(10)))
                        .into(portraitImageView);
                return;
            }
            this.userInfo = userInfo;
            this.type = TYPE_USER;
            GroupMember groupMember = (GroupMember) object;

            nameTextView.setVisibility(View.VISIBLE);
            nameTextView.setText(userInfo.displayName);
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

            Glide.with(portraitImageView).load(userInfo.portrait)
                    .apply(new RequestOptions().centerCrop().placeholder(R.mipmap.default_header)
                            . transforms(new CenterCrop(), new RoundedCorners(10)))
                    .into(portraitImageView);
        }

        public void bindAddMember() {
            nameTextView.setVisibility(View.GONE);
            typeView.setVisibility(View.GONE);
            portraitImageView.setImageResource(R.mipmap.ic_add_team_member);
            this.type = TYPE_ADD;

        }

        public void bindRemoveMember() {
            nameTextView.setVisibility(View.GONE);
            typeView.setVisibility(View.GONE);
            portraitImageView.setImageResource(R.mipmap.ic_remove_team_member);
            this.type = TYPE_REMOVE;
        }
    }

    public interface OnMemberClickListener {
        void onUserMemberClick(UserInfo userInfo);

        void onAddMemberClick();

        void onRemoveMemberClick();
    }
}
