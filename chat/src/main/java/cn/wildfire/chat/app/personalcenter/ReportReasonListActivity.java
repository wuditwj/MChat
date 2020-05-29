package cn.wildfire.chat.app.personalcenter;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.app.main.BaseActivity;
import cn.wildfire.chat.app.personalcenter.adapter.ReportAdapter;
import cn.wildfire.chat.app.shop.utils.ItemDecorationDivider;
import cn.wildfirechat.chat.R;

/**
 * 举报与投诉
 */
public class ReportReasonListActivity extends BaseActivity {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @Override
    protected int contentLayout() {
        return R.layout.activity_report_reason_list;
    }

    @Override
    protected void init() {
        toolbarTitle.setText("举报与投诉");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemDecorationDivider(this,
                ItemDecorationDivider.VERTICAL_LIST, 1, ContextCompat.getColor(this, R.color.line)));
        List<String> list = new ArrayList<>();
        list.add("发布色情、广告对我造成骚扰");
        list.add("存在反动，政治相关内容");
        list.add("存在欺诈骗钱行为");
        list.add("存在赌博行为");
        ReportAdapter reportAdapter = new ReportAdapter(R.layout.item_report, list);
        recyclerView.setAdapter(reportAdapter);
        reportAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(ReportReasonListActivity.this, ReportActivity.class);
            intent.putExtra("title", list.get(position));
            startActivity(intent);
        });
    }

}
