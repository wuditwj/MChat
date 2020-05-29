package cn.wildfire.chat.app.envelope;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.allen.library.CircleImageView;
import com.bumptech.glide.Glide;
import com.zrq.spanbuilder.Spans;

import java.text.DecimalFormat;

import butterknife.BindView;
import cn.wildfire.chat.app.entity.RedPackageDetailEntity;
import cn.wildfire.chat.app.http.ApiMethodFactory;
import cn.wildfire.chat.app.http.HttpHandler;
import cn.wildfire.chat.app.entity.BaseBean;
import cn.wildfire.chat.app.shop.utils.ItemDecorationDivider;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfirechat.chat.R;

import static cn.wildfire.chat.app.ConfigPath.RedPackageDetail;

@Route(path = RedPackageDetail)
public class RedPackageDetailActivity extends WfcBaseActivity {
    @BindView(R.id.bg_header)
    RelativeLayout bgHeader;
    @BindView(R.id.header)
    CircleImageView header;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.red_title)
    TextView redTitle;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.money_layout)
    LinearLayout moneyLayout;
    @BindView(R.id.total_red_amount)
    TextView totalRedAmount;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.value)
    TextView value;
    @Autowired
    public String sendLogId;
    @Autowired
    public String robUser;
    @Autowired
    public int red_Type;
    @Autowired
    public int code;

    @Override
    protected int contentLayout() {
        return R.layout.activity_red_package_detail;
    }

    @Override
    protected void afterViews() {
        super.afterViews();

        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new ItemDecorationDivider(this, ItemDecorationDivider.VERTICAL_LIST,1,
                ContextCompat.getColor(this,R.color.line)));
        getData();

    }

    private void getData(){
        ApiMethodFactory.getInstance().getRedPackageDetail(sendLogId, new HttpHandler() {
            @Override
            public void requestSuccess(String response) {
                BaseBean<RedPackageDetailEntity> baseBean = JSONObject.parseObject(response, new TypeReference<BaseBean<RedPackageDetailEntity>>() {
                });
                if (baseBean.getCode() == 200) {
                    if (baseBean.getData().getTitle()!=null){
                        Glide.with(RedPackageDetailActivity.this).load(baseBean.getData().getTitle().get_portrait())
                                .into(header);
                        name.setText(baseBean.getData().getTitle().getDisplayname());
                        redTitle.setText(baseBean.getData().getTitle().getRed_title());
                    }

                    DecimalFormat df = new DecimalFormat("#######0.00");

                    if (baseBean.getData().getMoney() > 0){//表示未抢到
                        money.setText( Spans.builder().text(df.format(baseBean.getData().getMoney())+"",30, Color.parseColor("#DF5050"))
                                .text("元",13,Color.parseColor("#464646"))
                                .build());
                    }else {
                        moneyLayout.setVisibility(View.GONE);
                    }
                    totalRedAmount.setVisibility(View.VISIBLE);
                    if (baseBean.getData().getList() != null){
                        totalRedAmount.setText("已领取"+baseBean.getData().getList().size()+"/"+baseBean.getData().getTitle().getRed_number()+"个红包，共"+
                                df.format(baseBean.getData().getTitle().getRed_money())+"元");
                        ReceiveRedListAdapter recevieRedListAdapter = new ReceiveRedListAdapter(R.layout.item_receive,baseBean.getData().getList());
                        list.setAdapter(recevieRedListAdapter);
                    }else {
                        if (baseBean.getData().getTitle()!=null){
                            totalRedAmount.setText("已领取0/"+baseBean.getData().getTitle().getRed_number()+"个红包，共"+
                                    df.format(baseBean.getData().getTitle().getRed_money()) +"元");
                        }

                    }
                    if (red_Type == 1) {//查看自己发的红包
                        if (baseBean.getData().getTitle()!=null){
                            money.setText(Spans.builder().text(baseBean.getData().getTitle().getRed_money() +"",30, Color.parseColor("#DF5050"))
                                    .text("元",13,Color.parseColor("#464646"))
                                    .build());
                        }

                        if (baseBean.getData().getList() != null) {
                            moneyLayout.setVisibility(View.GONE);
                            //表示已经领取  显示列表
                            value.setText("红包已被领取");
                        } else {
                            //等待对方领取
                            value.setText("未领取的红包,将于24小时后发起退款");
                            money.setVisibility(View.GONE);
                        }
                    } else {
                        if (code==600&&baseBean.getData().getMoney()==0){
                            money.setVisibility(View.GONE);
                            value.setText("已存入零钱，可用来发红包或提现");
                        }else if (code==700&&baseBean.getData().getMoney()==0){
                            money.setVisibility(View.GONE);
                            value.setText("已存入零钱，可用来发红包或提现");


                        }else {
                            //未领取的红包,将于24小时后发起退款
                            if (TextUtils.equals(robUser,ChatManagerHolder.gChatManager.getUserId())){
                                value.setText("未领取的红包,将于24小时后发起退款");
                            }else {

                                value.setText("已存入零钱，可用来发红包或提现");
                            }
                        }
                    }
                }
            }
        });
    }

}
