package com.htxtdshopping.htxtd.frame.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.htxtdshopping.htxtd.frame.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * 无任何控制ui的播放器
 *
 * @author 陈志鹏
 * @date 2021/8/6
 */
public class EmptyControlVideoPlayer extends StandardGSYVideoPlayer {
    public EmptyControlVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public EmptyControlVideoPlayer(Context context) {
        super(context);
    }

    public EmptyControlVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_empty_control_video_player;
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }

    @Override
    protected void touchDoubleUp(MotionEvent e) {
//        super.touchDoubleUp(e);
        //不需要双击暂停
    }
}
