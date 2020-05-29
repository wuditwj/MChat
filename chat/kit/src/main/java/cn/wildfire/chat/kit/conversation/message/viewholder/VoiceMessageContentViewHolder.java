package cn.wildfire.chat.kit.conversation.message.viewholder;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.kit.annotation.EnableContextMenu;
import cn.wildfire.chat.kit.annotation.MessageContentType;
import cn.wildfire.chat.kit.annotation.ReceiveLayoutRes;
import cn.wildfire.chat.kit.annotation.SendLayoutRes;
import cn.wildfire.chat.kit.conversation.ConversationFragment;
import cn.wildfire.chat.kit.conversation.message.model.UiMessage;
import cn.wildfire.chat.kit.utils.GlobalHandler;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.message.SoundMessageContent;
import cn.wildfirechat.message.core.MessageDirection;
import cn.wildfirechat.message.core.MessageStatus;

@MessageContentType(SoundMessageContent.class)
@SendLayoutRes(resId = R.layout.conversation_item_voice_send)
@ReceiveLayoutRes(resId = R.layout.conversation_item_voice_receive)
@EnableContextMenu
public class VoiceMessageContentViewHolder extends MediaMessageContentViewHolder implements GlobalHandler.HandleMsgListener {
    private String TAG = "--==>>";

    @BindView(R.id.voice_play)
    ImageView voicePlay;
    @BindView(R.id.voice_seek_bar)
    SeekBar seekBar;
    @BindView(R.id.time_left)
    TextView timeLeft;
    @BindView(R.id.time_right)
    TextView timeRight;
    @Nullable
    @BindView(R.id.playStatusIndicator)
    View playStatusIndicator;

    private GlobalHandler mHandler;

    private Runnable seekAndTimeRunnable = new Runnable() {

        @Override
        public void run() {
            int position;
            Log.i(TAG, "run: " + messageViewModel.getProgress());
            if ((position = messageViewModel.getProgress()) != 0) {
                seekBar.setProgress(position);
                String time = formatTime(position);
                timeLeft.setText(time);
            }
            mHandler.postDelayed(seekAndTimeRunnable, 10);
        }
    };

    public VoiceMessageContentViewHolder(ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
        mHandler = GlobalHandler.getInstance();
        mHandler.setHandleMsgListener(this);
    }

    @Override
    public void onBind(UiMessage message) {
        super.onBind(message);
        SoundMessageContent voiceMessage = (SoundMessageContent) message.message.content;

        timeRight.setText(formatTime(voiceMessage.getDuration() * 1000));
        if (message.message.direction == MessageDirection.Receive) {
            if (message.message.status != MessageStatus.Played) {
                playStatusIndicator.setVisibility(View.VISIBLE);
            } else {
                playStatusIndicator.setVisibility(View.GONE);
            }
        }

        Log.i(TAG, "onBind: " + message.isPlaying);
        if (message.isPlaying) {
            mHandler.post(seekAndTimeRunnable);
        } else {
            voicePlay.setImageResource(R.mipmap.voice_play_blue);
        }

        // 下载完成，开始播放
        if (message.progress == 100) {
            message.progress = 0;
            itemView.post(() -> {
                File file = messageViewModel.mediaMessageContentFile(message);
                if (!file.exists()) {
                    messageViewModel.downloadMedia(message, file);
                }
                messageViewModel.playVoiceMessage(message);
            });
        }

        View.OnTouchListener seekBarTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
                        break;
                    //抬起
                    case MotionEvent.ACTION_UP:
                        seekBar.setOnSeekBarChangeListener(null);
                        break;
                    //移动
                    case MotionEvent.ACTION_MOVE:
                        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
                        break;
                }
                return false;
            }
        };
        seekBar.setOnTouchListener(seekBarTouchListener);
    }

    //时间格式
    private String formatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(time);
    }


    @Override
    public void onViewRecycled() {
        // TODO 可实现语音是否持续播放、中断登录逻辑
//        stopPlay();
    }

    @OnClick(R.id.voice_play)
    public void onClick(View view) {

        File file = messageViewModel.mediaMessageContentFile(message);
        if (file == null) {
            return;
        }
        if (file.exists()) {
            voicePlay.setImageResource(R.mipmap.voice_stop_blue);
            mHandler.post(seekAndTimeRunnable);
            messageViewModel.playVoiceMessage(message);
        } else {
            if (message.isDownloading) {
                return;
            }
            messageViewModel.downloadMedia(message, file);
        }
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            messageViewModel.seekTo(seekBar.getProgress());
            timeLeft.setText(formatTime(seekBar.getProgress()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            timeLeft.setText(formatTime(seekBar.getProgress()));
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            timeLeft.setText(formatTime(seekBar.getProgress()));
        }
    };


    @Override
    public void handleMsg(Message msg) {
        switch (msg.what) {
            case 0:
                int time = msg.getData().getInt("time");
                seekBar.setProgress(time);
                String left = formatTime(position);
                timeLeft.setText(left);
                break;
        }
    }
}
