package com.demo.yiman.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.demo.yiman.R;

import java.util.List;

public class RadarView extends View {
    //布局的宽度
    private int mWidth;
    //布局的高度
    private int mHeight;
    //最大半径
    private float mRadius;
    //默认个数
    private int mCount = 0;
    //角度
    private double mAngle;
    //雷达网路径
    private Path mRadarPath = new Path();
    //连接线路径
    private Path mLinePath = new Path();
    //覆盖层路径
    private Path mLayerPath = new Path();
    //雷达画笔
    private Paint mRadarPaint;
    //文本画笔
    private Paint mTextPaint;
    //覆盖层画笔
    private Paint mLayerPaint;
    //连接线画笔
    private Paint mLinePaint;
    //圆点画笔
    private Paint mCirclePaint;
    //雷达网数
    private int mNum;
    //雷达网络线的颜色
    private  int mRadarGridColor;
    //雷达网络线线宽
    private  int mRadarGridWidth;
    //连接线的颜色
    private  int radarLineColor;
    //连接线线宽
    private  int radarLineStrokeWidth;
    //小圆点的颜色
    private  int circleColor;
    //小圆点的大小
    private  int circleWidth;
    //覆盖图层的颜色
    private  int layerColor;
    //文本颜色
    private  int textColor;
    //文本颜色
    private  int textSize;
    //数据源
    private List<RadarData> mData;
    //百分比
    private double mMaxValue = 100.0d;
    public RadarView(Context context) {
        this(context,null);
    }

    public RadarView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public RadarView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadarView,defStyleAttr,0);
        mRadarGridColor = a.getColor(R.styleable.RadarView_radarGridColor,context.getResources().getColor(R.color.radarGridColor));
        mNum = a.getInteger(R.styleable.RadarView_num, context.getResources().getInteger(R.integer.num));
        circleColor = a.getColor(R.styleable.RadarView_circleColor, context.getResources().getColor(R.color.circleColor));
        circleWidth = a.getDimensionPixelSize(R.styleable.RadarView_circleWidth, (int) context.getResources().getDimension(R.dimen.circleWidth));
        mRadarGridWidth = a.getDimensionPixelSize(R.styleable.RadarView_radarGridStrokeWidth, (int) context.getResources().getDimension(R.dimen.radarGridWidth));
        radarLineColor = a.getColor(R.styleable.RadarView_radarLineColor, context.getResources().getColor(R.color.radarLineColor));
        radarLineStrokeWidth = a.getDimensionPixelSize(R.styleable.RadarView_radarLineStrokeWidth, (int) context.getResources().getDimension(R.dimen.radarLineStrokeWidth));
        layerColor = a.getColor(R.styleable.RadarView_layerColor, context.getResources().getColor(R.color.layerColor));
        textColor = a.getColor(R.styleable.RadarView_android_textColor, context.getResources().getColor(R.color.radarTextColor));
        textSize = a.getDimensionPixelSize(R.styleable.RadarView_android_textSize, (int) context.getResources().getDimension(R.dimen.radarTextSize));
        a.recycle();
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        mRadarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRadarPaint.setStyle(Paint.Style.STROKE);
        mRadarPaint.setColor(mRadarGridColor);
        mRadarPaint.setStrokeWidth(mRadarGridWidth);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(radarLineColor);
        mLinePaint.setStrokeWidth(radarLineStrokeWidth);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(circleColor);
        mCirclePaint.setStrokeWidth(circleWidth);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);

        mLayerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLayerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLayerPaint.setColor(layerColor);
        mLayerPaint.setAlpha(128);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = (float) (Math.min(w, h) / 2 * 0.8);
    }

    /**
     * 绑定数据
     *
     * @param data
     * @param maxValue
     */
    public void setData(List<RadarData> data, double maxValue) {
        if (data == null || data.size() < 3) {
            throw new RuntimeException("The number of data can not be less than 3");
        } else {
            this.mData = data;
            this.mMaxValue = maxValue;
            this.mCount = mData.size();
            this.mAngle = Math.PI * 2 / mCount;
            invalidate();
        }
    }

    public void setNum(int num){
        this.mNum = num;
        invalidate();

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        drawRadarBg(canvas);
        drawText(canvas);
        drawLayer(canvas);
    }

    /**
     * 绘制覆盖图层
     *
     * @param canvas
     */
    private void drawLayer(Canvas canvas) {
        for (int i = 0; i < mCount; i++) {
            double percent = mData.get(i).getPercent() / mMaxValue;
            float x = (float) (mRadius * Math.sin(mAngle / 2 + mAngle * i) * percent);
            float y = (float) (mRadius * Math.cos(mAngle / 2 + mAngle * i) * percent);
            if (i == 0) {
                mLayerPath.moveTo(x, y);
            } else {
                mLayerPath.lineTo(x, y);
            }
            //画小圆点
            canvas.drawCircle(x, y, circleWidth, mCirclePaint);
        }
        mLayerPath.close();
        canvas.drawPath(mLayerPath, mLayerPaint);
    }

    /**
     * 绘制文本内容
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i = 0; i < mCount; i++) {
            float x = (float) ((mRadius + fontHeight / 2) * Math.sin(mAngle / 2 + mAngle * i));
            float y = (float) ((mRadius + fontHeight / 2) * Math.cos(mAngle / 2 + mAngle * i));
            String text = mData.get(i).getTitle();
            float fontWidth = mTextPaint.measureText(text);
            canvas.drawText(text, x - fontWidth / 2, y, mTextPaint);
        }
    }

    /**
     * 绘制雷达网格
     *
     * @param canvas
     */
    private void drawRadarBg(Canvas canvas) {
        //雷达网格间距
        float mSpc = mRadius / (mNum - 1);
        for (int i = 1; i < mNum; i++) {
            //计算当前半径
            float curRadius = mSpc * i;
            for (int j = 0; j < mCount; j++) {
                //根据半径，计算出每个点的坐标
                float x = (float) (curRadius * Math.sin(mAngle / 2 + mAngle * j));
                float y = (float) (curRadius * Math.cos(mAngle / 2 + mAngle * j));
                if (j == 0) {
                    mRadarPath.moveTo(x, y);
                } else {
                    mRadarPath.lineTo(x, y);
                }
                //绘制最后一环时绘制连线
                drawLine(canvas, i, x, y);
            }
            mRadarPath.close();
            canvas.drawPath(mRadarPath, mRadarPaint);
        }
    }

    /**
     * 绘制连接线
     *
     * @param canvas
     * @param x
     * @param y
     */
    private void drawLine(Canvas canvas, int i, float x, float y) {
        if (i == mNum - 1) {
            mLinePath.reset();
            mLinePath.moveTo(0, 0);
            mLinePath.lineTo(x, y);
            canvas.drawPath(mLinePath, mLinePaint);
        }
    }

}
