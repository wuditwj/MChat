package cn.wildfire.chat.kit.settings.blacklist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.kit.user.UserInfoActivity;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.remote.ChatManager;
import cn.wildfirechat.remote.GeneralCallback;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptDialog;


public class BlacklistListFragment extends Fragment implements BlacklistListAdapter.OnBlacklistItemClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private BlacklistViewModel blacklistViewModel;
    private BlacklistListAdapter blacklistListAdapter;

    private PromptDialog promptDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blacklist_list_frament, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshBlacklist();
    }

    private void init() {
        blacklistViewModel = ViewModelProviders.of(getActivity()).get(BlacklistViewModel.class);

        blacklistListAdapter = new BlacklistListAdapter();
        blacklistListAdapter.setOnBlacklistItemClickListener(this);

        recyclerView.setAdapter(blacklistListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        promptDialog = new PromptDialog(getActivity());
        promptDialog.getAlertDefaultBuilder().textSize(12).textColor(ContextCompat.getColor(getActivity(), R.color.blue0));
    }

    private void refreshBlacklist() {
        List<String> blacklists = blacklistViewModel.getBlacklists();
        blacklistListAdapter.setBlackedUserIds(blacklists);
        blacklistListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(String userId, View v) {
        PromptButton cancle = new PromptButton("取消", null);
        cancle.setTextColor(Color.parseColor("#0076ff"));
        promptDialog.showAlertSheet("确认移除黑名单并添加好友,", true, cancle,
                new PromptButton("确定", button -> {
                    ChatManager.Instance().setBlackList(userId, false, new GeneralCallback() {
                        @Override
                        public void onSuccess() {
                            blacklistListAdapter.getBlackedUserIds().remove(userId);
                            blacklistListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFail(int errorCode) {

                        }
                    });

                })

        );

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

}
