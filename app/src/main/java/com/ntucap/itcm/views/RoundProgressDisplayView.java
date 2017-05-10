package com.ntucap.itcm.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gigamole.library.ArcProgressStackView;
import com.ntucap.itcm.R;

import java.util.ArrayList;

/**
 * Created by ProgrammerYuan on 19/04/17.
 */

public class RoundProgressDisplayView extends RelativeLayout {

    private Resources res;
    private TextView mTextView;
    private ImageView mImageView;
    private ArcProgressStackView mArcProgressBar;
    private int mContentDisplayState = STATE_SHOW_NOTHING; // 0 nothing, 1 textview, 2 imageview
    private int mProgressBarColor;
    private int mProgressBarBgColor;
    private float mContentSizeRatio;
    private float mProgressBarSizeRatio;
    private float mProgressPercentage;
    private float mProgressBarStartAngle;
    private float mProgressBarSweepAngle;
    private ArrayList<ArcProgressStackView.Model> models;

    //DEFAULT VALUE CONSTANTS
    private static final int DEFAULT_TEXT_COLOR = R.color.mainTheme;
    private static final int DEFAULT_PROGRESSBAR_COLOR = R.color.mainTheme;
    private static final int DEFAULT_PROGRESSBAR_BG_COLOR = R.color.textLightGray;
    private static final float DEFAULT_PROGRESSBAR_START_ANGLE = 270;
    private static final float DEFAULT_PROGRESSBAR_SWEEP_ANGLE = 360;
    private static final float DEFAULT_PROGRESSBAR_PROGRESS = 0.0f;
    private static final float DEFAULT_TEXT_SIZE = 44.0f;
    private static final float DEFAULT_CONTENT_SIZE_RATIO = 0.5f;
    private static final float DEFAULT_PROGRESSBAR_SIZE_RATIO = 0.15f;
    //STATE CONSTANTS
    private static final int STATE_SHOW_NOTHING = 0;
    private static final int STATE_SHOW_TEXTVIEW = 1;
    private static final int STATE_SHOW_IMAGEVIEW = 2;

    public RoundProgressDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        res = context.getResources();
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_round_progress_display, this);
        models = new ArrayList<>();
        mTextView = (TextView) findViewById(R.id.tv_indicator_progressview);
        mImageView = (ImageView) findViewById(R.id.iv_icon_progressview);
        mArcProgressBar = (ArcProgressStackView) findViewById(R.id.apsv);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressDisplayView);

        try {

            //ArcProgressBar Initialization
            mContentSizeRatio = a.getFraction(R.styleable.RoundProgressDisplayView_contentSizeRatio,
                    1, 1, DEFAULT_CONTENT_SIZE_RATIO);
            mProgressBarSizeRatio = a.getFraction(R.styleable.RoundProgressDisplayView_progressBarSizeRatio,
                    1, 1, DEFAULT_PROGRESSBAR_SIZE_RATIO);
            mProgressBarStartAngle = a.getFloat(R.styleable.RoundProgressDisplayView_progressBarStartAngle,
                    DEFAULT_PROGRESSBAR_START_ANGLE);
            mProgressBarSweepAngle = a.getFloat(R.styleable.RoundProgressDisplayView_progressBarSweepAngle,
                    DEFAULT_PROGRESSBAR_SWEEP_ANGLE);
            mProgressPercentage = a.getFloat(R.styleable.RoundProgressDisplayView_progressBarProgress,
                    DEFAULT_PROGRESSBAR_PROGRESS);
            mProgressBarColor = a.getColor(R.styleable.RoundProgressDisplayView_progressBarColor,
                    res.getColor(DEFAULT_PROGRESSBAR_COLOR));
            mProgressBarBgColor = a.getColor(R.styleable.RoundProgressDisplayView_progressBarBgColor,
                    res.getColor(DEFAULT_PROGRESSBAR_BG_COLOR));
            mArcProgressBar.setStartAngle(mProgressBarStartAngle);
            mArcProgressBar.setSweepAngle(mProgressBarSweepAngle);
            mArcProgressBar.setDrawWidthFraction(mProgressBarSizeRatio);
            mArcProgressBar.setIsShadowed(false);
            setProgress(mProgressPercentage);

            //Content Part Initialization
            if (a.hasValue(R.styleable.RoundProgressDisplayView_text)) {
                mTextView.setText(a.getString(R.styleable.RoundProgressDisplayView_text));
                if (a.hasValue(R.styleable.RoundProgressDisplayView_textSize)) {
                    mTextView.setTextSize(a.getDimension(R.styleable.RoundProgressDisplayView_textSize,
                            22.0f));
                } else {
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
                }
                mTextView.setTextColor(a.getColor(R.styleable.RoundProgressDisplayView_textColor,
                        getResources().getColor(DEFAULT_TEXT_COLOR)));
                mImageView.setVisibility(GONE);
                mContentDisplayState = STATE_SHOW_TEXTVIEW;
            } else if (a.hasValue(R.styleable.RoundProgressDisplayView_imageSource)) {
                mImageView.setImageResource(
                        a.getResourceId(R.styleable.RoundProgressDisplayView_imageSource,
                                R.drawable.ic_logo_theme_48));
                mTextView.setVisibility(GONE);
                mContentDisplayState = STATE_SHOW_IMAGEVIEW;
            }
        } finally {
            a.recycle();
        }
    }

    public void setProgress(@FloatRange(from = 0.0, to = 100.0) float progress) {
        setProgress(progress, mProgressBarBgColor, mProgressBarColor);
    }

    public void setProgress(@FloatRange(from = 0.0, to = 100.0) float progress,
                            int barBgColor, int barColor) {
        mProgressBarColor = barColor;
        mProgressPercentage = progress;
        mProgressBarBgColor = barBgColor;
        models.clear();
        models.add(new ArcProgressStackView.Model("", mProgressPercentage,
                mProgressBarBgColor,
                mProgressBarColor));
        mTextView.setText(String.valueOf(progress));
        mArcProgressBar.setModels(models);
        mArcProgressBar.animateProgress();
    }

    public void setSweepAngle(@FloatRange(from = 0.0, to = 360.0) float sweepAngle) {
        mArcProgressBar.setSweepAngle(mProgressBarSweepAngle = sweepAngle);
    }

    public void setStartAngle(@FloatRange(from = 0.0, to = 360.0) float startAngle) {
        mArcProgressBar.setStartAngle(mProgressBarStartAngle = startAngle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);

        int consistentSize = (widthMeasureSize == 0 || heightMeasureSize == 0) ?
                Math.max(widthMeasureSize, heightMeasureSize):
                Math.min(widthMeasureSize, heightMeasureSize);

        RelativeLayout.LayoutParams rlp;
        if(mContentDisplayState == STATE_SHOW_TEXTVIEW) {
            rlp = (LayoutParams) mTextView.getLayoutParams();
            rlp.width = rlp.height = (int)(consistentSize * mContentSizeRatio);
            mTextView.setLayoutParams(rlp);
        } else if(mContentDisplayState == STATE_SHOW_IMAGEVIEW) {
            rlp = (LayoutParams) mImageView.getLayoutParams();
            rlp.width = rlp.height = (int)(consistentSize * mContentSizeRatio);
            mImageView.setLayoutParams(rlp);
        }

    }
}
