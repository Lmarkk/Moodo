package fi.tuni.tamk.moodo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class CircleTimerView extends View
{
    private Context context = getContext();
    private static final String TAG = "CircleTimerView";

    // Status
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_RADIAN = "status_radian";

    // Default dimension in dp/pt
    private static final float DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE = 5;
    private static final float DEFAULT_GAP_BETWEEN_NUMBER_AND_LINE = 5;
    private static final float DEFAULT_NUMBER_SIZE = 10;
    private static final float DEFAULT_LINE_LENGTH = 14;
    private static final float DEFAULT_LONGER_LINE_LENGTH = 23;
    private static final float DEFAULT_LINE_WIDTH = 0.8f;
    private static final float DEFAULT_CIRCLE_BUTTON_RADIUS = 15;
    private static final float DEFAULT_CIRCLE_STROKE_WIDTH = 1;
    private static final float DEFAULT_TIMER_NUMBER_SIZE = 35;
    private static final float DEFAULT_TIMER_TEXT_SIZE = 14;
    private static final float DEFAULT_GAP_BETWEEN_TIMER_NUMBER_AND_TEXT = 30;

    // if you want to get color from resources
    // ContextCompat.getColor(context, R.color.red);
    // Default color
    private final int DEFAULT_CIRCLE_COLOR = Color.BLACK;
    private final int DEFAULT_CIRCLE_BUTTON_COLOR = Color.RED;
    private final int DEFAULT_LINE_COLOR = Color.BLACK;
    private final int DEFAULT_HIGHLIGHT_LINE_COLOR = Color.RED;
    private final int DEFAULT_NUMBER_COLOR = Color.BLACK;
    private final int DEFAULT_TIMER_NUMBER_COLOR = Color.RED;
    private final int DEFAULT_TIMER_COLON_COLOR = Color.RED;
    private final int DEFAULT_TIMER_TEXT_COLOR = Color.BLACK;

    // Paint
    private Paint mCirclePaint;
    private Paint mHighlightLinePaint;
    private Paint mLinePaint;
    private Paint mCircleButtonPaint;
    private Paint mNumberPaint;
    private Paint mTimerNumberPaint;
    private Paint mTimerTextPaint;
    private Paint mTimerColonPaint;

    // Dimension
    private float mGapBetweenCircleAndLine;
    private float mGapBetweenNumberAndLine;
    private float mNumberSize;
    private float mLineLength;
    private float mLongerLineLength;
    private float mLineWidth;
    private float mCircleButtonRadius;
    private float mCircleStrokeWidth;
    private float mTimerNumberSize;
    private float mTimerTextSize;
    private float mGapBetweenTimerNumberAndText;

    // Color
    private int mCircleColor;
    private int mCircleButtonColor;
    private int mLineColor;
    private int mHighlightLineColor;
    private int mNumberColor;
    private int mTimerNumberColor;
    private int mTimerTextColor;

    // Parameters
    private float mCx;
    private float mCy;
    private float mRadius;
    private float mCurrentRadian;
    private float mPreRadian;
    private boolean mInCircleButton;
    private int mCurrentTime; // seconds
    private boolean mStarted;
    private String mHintText;

    // TimerTask
    private Timer timer = new Timer();

    private TimerTask timerTask;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Log.d(TAG, "handleMessage");
            super.handleMessage(msg);
            if (mCurrentRadian > 0 && mCurrentTime > 0)
            {
                mCurrentRadian -= (2 * Math.PI) / 3600;
                mCurrentTime--;
                if (mCircleTimerListener != null)
                {
                    mCircleTimerListener.onTimerTimingValueChanged(mCurrentTime);
                }
            }
            else
            {
                mCurrentRadian = 0;
                mCurrentTime = 0;
                timer.cancel();
                mStarted = false;
                if (mCircleTimerListener != null)
                {
                    mCircleTimerListener.onTimerStop();
                }
            }
            invalidate();
        }
    };

    // Runt
    private CircleTimerListener mCircleTimerListener;

    public CircleTimerView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public CircleTimerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CircleTimerView(Context context)
    {
        this(context, null);
    }

    private void initialize()
    {
        Log.d(TAG, "initialize");
        // Set default dimension or read xml attributes
        mGapBetweenCircleAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE,
                getContext().getResources().getDisplayMetrics());
        mGapBetweenNumberAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_NUMBER_AND_LINE,
                getContext().getResources().getDisplayMetrics());
        mNumberSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_NUMBER_SIZE, getContext().getResources()
                .getDisplayMetrics());
        mLineLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_LENGTH, getContext().getResources()
                .getDisplayMetrics());
        mLongerLineLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LONGER_LINE_LENGTH, getContext()
                .getResources().getDisplayMetrics());
        mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_WIDTH, getContext().getResources()
                .getDisplayMetrics());
        mCircleButtonRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_BUTTON_RADIUS, getContext()
                .getResources().getDisplayMetrics());
        mCircleStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_STROKE_WIDTH, getContext()
                .getResources().getDisplayMetrics());
        mTimerNumberSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIMER_NUMBER_SIZE, getContext()
                .getResources().getDisplayMetrics());
        mTimerTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIMER_TEXT_SIZE, getContext()
                .getResources().getDisplayMetrics());
        mGapBetweenTimerNumberAndText = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_GAP_BETWEEN_TIMER_NUMBER_AND_TEXT, getContext().getResources().getDisplayMetrics());

        // Set default color or read xml attributes
        mCircleColor = DEFAULT_CIRCLE_COLOR;
        mCircleButtonColor = DEFAULT_CIRCLE_BUTTON_COLOR;
        mLineColor = DEFAULT_LINE_COLOR;
        mHighlightLineColor = DEFAULT_HIGHLIGHT_LINE_COLOR;
        mNumberColor = DEFAULT_NUMBER_COLOR;
        mTimerNumberColor = DEFAULT_TIMER_NUMBER_COLOR;
        mTimerTextColor = DEFAULT_TIMER_TEXT_COLOR;

        // Init all paints
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerColonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // CirclePaint
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);

        // CircleButtonPaint
        mCircleButtonPaint.setColor(mCircleButtonColor);
        mCircleButtonPaint.setAntiAlias(true);
        mCircleButtonPaint.setStyle(Paint.Style.FILL);

        // LinePaint
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineWidth);

        // HighlightLinePaint
        mHighlightLinePaint.setColor(mHighlightLineColor);
        mHighlightLinePaint.setStrokeWidth(mLineWidth);

        // NumberPaint
        mNumberPaint.setColor(mNumberColor);
        mNumberPaint.setTextSize(mNumberSize);
        mNumberPaint.setTextAlign(Paint.Align.CENTER);

        // TimerNumberPaint
        mTimerNumberPaint.setColor(mTimerNumberColor);
        mTimerNumberPaint.setTextSize(mTimerNumberSize);
        mTimerNumberPaint.setTextAlign(Paint.Align.CENTER);

        // TimerTextPaint
        mTimerTextPaint.setColor(mTimerTextColor);
        mTimerTextPaint.setTextSize(mTimerTextSize);
        mTimerTextPaint.setTextAlign(Paint.Align.CENTER);

        // TimerColonPaint
        mTimerColonPaint.setColor(DEFAULT_TIMER_COLON_COLOR);
        mTimerColonPaint.setTextAlign(Paint.Align.CENTER);
        mTimerColonPaint.setTextSize(mTimerNumberSize);

        mHintText = "Hello world";
        // Solve the target version related to shadow
        setLayerType(View.LAYER_TYPE_SOFTWARE, null); // use this, when targetSdkVersion is greater than or equal to api 14
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // Assist lines
//        canvas.drawColor(Color.RED);
        // canvas.drawLine(mCx, 0, mCx, getHeight(), new Paint());
        // canvas.drawLine(0, mCy, getWidth(), mCy, new Paint());
        Log.d(TAG, "onDraw");
        // Content
        canvas.drawCircle(mCx, mCy, mRadius, mCirclePaint);
        canvas.save();
        for (int i = 0; i < 120; i++)
        {
            canvas.save();
            canvas.rotate(360f / 120f * i, mCx, mCy);
            if (i % 30 == 0)
            {
                if (360f / 120f * i <= Math.toDegrees(mCurrentRadian))
                {
                    canvas.drawLine(mCx, getMeasuredHeight() / 2f - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine,
                            mCx,
                            getMeasuredHeight() / 2f - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                                    mLongerLineLength, mHighlightLinePaint);
                }
                else
                {
                    canvas.drawLine(mCx, getMeasuredHeight() / 2f - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine,
                            mCx,
                            getMeasuredHeight() / 2f - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                                    mLongerLineLength, mLinePaint);
                }
            }
            else
            {
                if (360f / 120f * i <= Math.toDegrees(mCurrentRadian))
                {
                    canvas.drawLine(mCx, getMeasuredHeight() / 2f - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine,
                            mCx,
                            getMeasuredHeight() / 2f - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine + mLineLength,
                            mHighlightLinePaint);
                }
                else
                {
                    canvas.drawLine(mCx, getMeasuredHeight() / 2f - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine,
                            mCx,
                            getMeasuredHeight() / 2f - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine + mLineLength,
                            mLinePaint);
                }
            }
            canvas.restore();
        }
        canvas.restore();
        // Number it is rubbish code
        float textLength = mNumberPaint.measureText("15");
        canvas.drawText("60", mCx, getMeasuredHeight() / 2f - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                mLongerLineLength + mGapBetweenNumberAndLine + getFontHeight(mNumberPaint), mNumberPaint);
        canvas.drawText("15", mCx + mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine - mLongerLineLength -
                textLength / 2
                - mGapBetweenNumberAndLine, mCy + getFontHeight(mNumberPaint) / 2, mNumberPaint);
        canvas.drawText("30", mCx, getMeasuredHeight() / 2f + mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine -
                mLongerLineLength - mGapBetweenNumberAndLine, mNumberPaint);
        canvas.drawText("45", getMeasuredHeight() / 2f - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                        mLongerLineLength + mGapBetweenNumberAndLine + textLength / 2, mCy + getFontHeight(mNumberPaint) / 2,
                mNumberPaint);
        // Circle button
        canvas.save();
        canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2f - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                mLineLength /
                        2, mCircleButtonRadius, mCircleButtonPaint);
        canvas.restore();
        // TimerNumber
        canvas.save();
        canvas.drawText((mCurrentTime / 60 < 10 ? "0" + mCurrentTime / 60 : mCurrentTime / 60) + " " + (mCurrentTime % 60 < 10 ?
                "0" + mCurrentTime % 60 : mCurrentTime % 60), mCx, mCy + getFontHeight(mTimerNumberPaint) / 2, mTimerNumberPaint);
        canvas.drawText(":", mCx, mCy + getFontHeight(mTimerNumberPaint) / 2, mTimerColonPaint);
        canvas.restore();
        // Timer Text
        canvas.save();
        canvas.drawText(mHintText, mCx, mCy + getFontHeight(mTimerNumberPaint) / 2 + mGapBetweenTimerNumberAndText + getFontHeight
                (mTimerTextPaint) / 2, mTimerTextPaint);
        canvas.restore();
        super.onDraw(canvas);
    }

    private float getFontHeight(Paint paint)
    {
        // FontMetrics sF = paint.getFontMetrics();
        // return sF.descent - sF.ascent;
        Rect rect = new Rect();
        paint.getTextBounds("1", 0, 1, rect);
        return rect.height();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction() & event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                // If the point in the circle button
                if (mInCircleButton(event.getX(), event.getY()) && isEnabled())
                {
                    mInCircleButton = true;
                    mPreRadian = getRadian(event.getX(), event.getY());
                    Log.d(TAG, "In circle button");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mInCircleButton && isEnabled())
                {
                    float temp = getRadian(event.getX(), event.getY());
                    if (mPreRadian > Math.toRadians(270) && temp < Math.toRadians(90))
                    {
                        mPreRadian -= 2 * Math.PI;
                    }
                    else if (mPreRadian < Math.toRadians(90) && temp > Math.toRadians(270))
                    {
                        mPreRadian = (float) (temp + (temp - 2 * Math.PI) - mPreRadian);
                    }
                    mCurrentRadian += (temp - mPreRadian);
                    mPreRadian = temp;
                    if (mCurrentRadian > 2 * Math.PI)
                    {
                        mCurrentRadian = (float) (2 * Math.PI);
                    }
                    else if (mCurrentRadian < 0)
                    {
                        mCurrentRadian = 0;
                    }
                    if (mCircleTimerListener != null)
                        mCircleTimerListener.onTimerSetValueChange(getCurrentTime());
                    mCurrentTime = (int) (60 / (2 * Math.PI) * mCurrentRadian * 60);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mInCircleButton && isEnabled())
                {
                    mInCircleButton = false;
                    if (mCircleTimerListener != null)
                        mCircleTimerListener.onTimerSetValueChanged(getCurrentTime());
                }
                break;
        }
        return true;
    }

    // Whether the down event inside circle button
    private boolean mInCircleButton(float x, float y)
    {
        float r = mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine - mLineLength / 2;
        float x2 = (float) (mCx + r * Math.sin(mCurrentRadian));
        float y2 = (float) (mCy - r * Math.cos(mCurrentRadian));
        if (Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2)) < mCircleButtonRadius)
        {
            return true;
        }
        return false;
    }

    // Use tri to cal radian
    private float getRadian(float x, float y)
    {
        float alpha = (float) Math.atan((x - mCx) / (mCy - y));
        // Quadrant
        if (x > mCx && y > mCy)
        {
            // 2
            alpha += Math.PI;
        }
        else if (x < mCx && y > mCy)
        {
            // 3
            alpha += Math.PI;
        }
        else if (x < mCx && y < mCy)
        {
            // 4
            alpha = (float) (2 * Math.PI + alpha);
        }
        return alpha;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Ensure width = height
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        this.mCx = width / 2f;
        this.mCy = height / 2f;
        // Radius
        if (mLineLength / 2 + mGapBetweenCircleAndLine + mCircleStrokeWidth >= mCircleButtonRadius)
        {
            this.mRadius = width / 2f - mCircleStrokeWidth / 2;
            Log.d(TAG, "No exceed");
        }
        else
        {
            this.mRadius = width / 2f - (mCircleButtonRadius - mGapBetweenCircleAndLine - mLineLength / 2 -
                    mCircleStrokeWidth / 2);
            Log.d(TAG, "Exceed");
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Log.d(TAG, "onSaveInstanceState");
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_RADIAN, mCurrentRadian);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        Log.d(TAG, "onRestoreInstanceState");
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            mCurrentRadian = bundle.getFloat(STATUS_RADIAN);
            mCurrentTime = (int) (60 / (2 * Math.PI) * mCurrentRadian * 60);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * start timer
     */
    public void startTimer()
    {
        Log.d(TAG, "startTimer");
        if (mCurrentRadian > 0 && !mStarted)
        {
            timer = new Timer();
            timerTask = new TimerTask()
            {
                @Override
                public void run()
                {
                    Log.d(TAG, "TimerTask");
                    handler.obtainMessage().sendToTarget();
                }
            };
            timer.schedule(timerTask, 1000, 1000);
            mStarted = true;
            if (this.mCircleTimerListener != null)
            {
                this.mCircleTimerListener.onTimerStart(mCurrentTime);
            }
        }
    }

    /**
     * pause timer
     */
    public void pauseTimer()
    {
        if (mStarted)
        {
            timerTask.cancel();
            mStarted = false;
            if (this.mCircleTimerListener != null)
            {
                this.mCircleTimerListener.onTimerPause(mCurrentTime);
            }
        }
    }

    /**
     * set current time in seconds
     *
     * @param time
     */
    public void setCurrentTime(int time)
    {
        if (time >= 0 && time <= 3600)
        {
            mCurrentTime = time;
            if (mCircleTimerListener != null)
            {
                mCircleTimerListener.onTimerSetValueChanged(time);
            }
            this.mCurrentRadian = (float) (time / 60.0f * 2 * Math.PI / 60);
            invalidate();
        }
    }

    /**
     * set the hint text, default is 时间设置
     *
     * @param id int id value
     */
    public void setHintText(int id)
    {
        if (id > 0)
        {
            setHintText(getResources().getString(id));
        }
    }


    /**
     * set the hint text, default is 时间设置
     *
     * @param value String value
     */
    public void setHintText(String value)
    {
        if (value != null)
        {
            mHintText = value;
        }
        invalidate();
    }

    /**
     * set timer listener
     *
     * @param mCircleTimerListener
     */
    public void setCircleTimerListener(CircleTimerListener mCircleTimerListener)
    {
        this.mCircleTimerListener = mCircleTimerListener;
    }

    /**
     * get current time in seconds
     *
     * @return
     */
    public int getCurrentTime()
    {
        return mCurrentTime;
    }


    public interface CircleTimerListener
    {
        /**
         *  launch timer stop event
         */
        void onTimerStop();

        /**
         * launch timer start event
         *
         * @param time
         */
        void onTimerStart(int time);

        /**
         * launch timer pause event
         *
         * @param time
         */
        void onTimerPause(int time);


        /**
         * launch timer timing value changed event
         *
         * @param time
         */
        void onTimerTimingValueChanged(int time);

        /**
         * launch timer set value changed event
         *
         * @param time
         */
        void onTimerSetValueChanged(int time);


        /**
         * launch timer set value chang event
         *
         * @param time
         */
        void onTimerSetValueChange(int time);
    }

}
