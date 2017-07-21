package com.ntucap.itcm.views;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ntucap.itcm.R;

import java.util.List;

/**
 * Created by ProgrammerYuan on 30/05/17.
 */

public class IrisSwitchButton extends RelativeLayout implements OnTouchListener {

    private static final String LOG_TAG = "IRIS_MY_LOVE";

    private ImageView mIvChosenMask;
    private LinearLayout mItemContainer;
    private int mSingleItemWidth = -1;
    private int mItemCount;
    private int mChosenIndex = 0;
    private int mChosenTextColor, mNotChosenTextColor;
    private OnIrisSwitchListener mSwitchListener = null;
    private TextView[] mItems;

    private static final int DEFAULT_ITEM_COUNT = 2;
    private static final int DEFAULT_ITEM_INDEX = 0;
    private static final int DEFAULT_ANIM_DURATION = 400;
    private static final int DEFAULT_CHOSEN_COLOR = R.color.white;
    private static final int DEFAULT_NOT_CHOSEN_COLOR = R.color.mainTheme;

    public IrisSwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IrisSwitchButton);

        try {
            mItemCount = a.getInteger(R.styleable.IrisSwitchButton_itemCount, DEFAULT_ITEM_COUNT);

            mChosenIndex = Math.max(Math.min(a.getInteger(R.styleable.IrisSwitchButton_initChosenItemIndex,
                    DEFAULT_ITEM_INDEX), mItemCount - 1), 0);

            mChosenTextColor = a.getColor(R.styleable.IrisSwitchButton_chosenTextColor,
                    getResources().getColor(DEFAULT_CHOSEN_COLOR));
            mNotChosenTextColor = a.getColor(R.styleable.IrisSwitchButton_notChosenTextColor,
                    getResources().getColor(DEFAULT_NOT_CHOSEN_COLOR));

        } finally {
            a.recycle();
        }

        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_iris_switch, this);

        mIvChosenMask = (ImageView) this.findViewById(R.id.iv_mask_view_swich);
        mItemContainer = (LinearLayout) this.findViewById(R.id.ll_container_view_switch);

        mItems = new TextView[mItemCount];
        TextView tempTextView;
        for (int i = 0; i < mItemCount; i++) {
            tempTextView = new TextView(context);
            tempTextView.setText("test" + i);
            tempTextView.setGravity(Gravity.CENTER);
            if (i != mChosenIndex) tempTextView.setTextColor(mNotChosenTextColor);
            else tempTextView.setTextColor(mChosenTextColor);
            tempTextView.setBackgroundColor(getResources().getColor(R.color.trans));
            mItems[i] = tempTextView;
            mItemContainer.addView(tempTextView);
        }

        this.setOnTouchListener(this);
    }

    public void setItemAt(int index, String item) {

        if (index >= mItemCount) return;
        mItems[index].setText(item);

    }

    public void setItemsWithArray(String[] array) {

        if (array == null) return;
        int len = Math.min(array.length, mItemCount);
        for (int i = 0; i < len; i ++)
            mItems[i].setText(array[i]);

    }

    public void setItemsWithList(List<String> list) {

        if (list == null) return;
        int len = Math.min(list.size(), mItemCount);
        for (int i = 0; i < len; i ++)
            mItems[i].setText(list.get(i));

    }

    public void setOnSwitchListener(OnIrisSwitchListener listener) {
        this.mSwitchListener = listener;
    }

    private void switchTextColor(TextView textView, int colorFrom, int colorTo) {
        ObjectAnimator animator = ObjectAnimator.ofInt(textView, "textColor", colorFrom, colorTo);
        animator.setDuration(DEFAULT_ANIM_DURATION);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
    }

    private void moveTo(int to) {
        Log.e(LOG_TAG, "FROM: " + mChosenIndex + " TO: " + to);
        switchTextColor(mItems[mChosenIndex], mChosenTextColor, mNotChosenTextColor);
        switchTextColor(mItems[to], mNotChosenTextColor, mChosenTextColor);
        move(mChosenIndex, to);
        if (mSwitchListener != null) mSwitchListener.onSwitch(mChosenIndex);
    }

    private void move(int from, int to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mIvChosenMask, "x",
                from * mSingleItemWidth, to * mSingleItemWidth + getOffset(to));
        animator.setDuration(DEFAULT_ANIM_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
        mChosenIndex = to;
    }

    private int getOffset(int index) {
        if(index == 0) return 2;
        if(index == mItemCount - 1) return -2;
        return 0;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e(LOG_TAG, "onLayout() Called" + " when Changed = " + changed);
        if (changed) resizeToCorrectSize();
    }

    private void resizeToCorrectSize() {

        mSingleItemWidth = getWidth() / mItemCount;

        LinearLayout.LayoutParams llp;
        for (TextView textView : mItems) {
            llp = (LinearLayout.LayoutParams) textView.getLayoutParams();
            llp.width = mSingleItemWidth;
            llp.height = getHeight();
            textView.setLayoutParams(llp);
        }

        RelativeLayout.LayoutParams rlp;
        rlp = (RelativeLayout.LayoutParams) mIvChosenMask.getLayoutParams();
        rlp.width = mSingleItemWidth;
        mIvChosenMask.setX(mChosenIndex * mSingleItemWidth);
        mIvChosenMask.setLayoutParams(rlp);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.e(LOG_TAG, "X:" + x + "   Width:" + getWidth());
                int toIndex = (int) ((x * mItemCount) / getWidth());
                moveTo(toIndex);
                break;
        }

        return true;
    }

    public interface OnIrisSwitchListener {

        void onSwitch(int switchIndex);

    }
}
