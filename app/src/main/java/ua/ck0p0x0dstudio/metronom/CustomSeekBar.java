package ua.ck0p0x0dstudio.metronom;

/**
 * Created by CK0P0X0D on 21.03.2016.
 */
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.SeekBar;

public class CustomSeekBar extends SeekBar {
    @SuppressWarnings("unused")
    private static String TAG = CustomSeekBar.class.getSimpleName();
    private Context mContext;

    private String mSigns = "-+";
    private int mHeight = 22; // DP

    private Path mBGPath = new Path();
    private Paint mBGPaint = new Paint();

    private Paint mTextPaint = new Paint();
    private int mLeftPadding, mRightPadding;

    Paint paint;
    Bitmap bitmap;
    Rect rectSrc;
    Rect rectDst;
    Matrix matrix;

    public CustomSeekBar(Context context) {
        this(context, null);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mHeight = (int)DpToPx(context, mHeight);
        // Init Paint
        initBGPaint();
        initTextPaint();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.thumb50);
        String info =
                String.format("Info: size = %s x %s, bytes = %s (%s), config = %s",
                        bitmap.getWidth(),
                        bitmap.getHeight(),
                        bitmap.getByteCount(),
                        bitmap.getRowBytes(),
                        bitmap.getConfig());
        Log.d("log", info);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Log.d(TAG, "Size: Width:" + (right-left) + ", Height: " + (top-bottom));
    }

    private void initBGPaint(){
        mBGPaint.setColor(Color.argb(255, 3, 169, 245));
        mBGPaint.setAntiAlias(true);
        mBGPaint.setStyle(Paint.Style.FILL);
    }

    private void initTextPaint(){
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mHeight);

    }

    private void initBGPath(){
        mBGPath.reset();
        mBGPath.moveTo(mLeftPadding, getHeight()/2 + getPaddingTop());
        mBGPath.lineTo(getWidth() - mRightPadding, getHeight() - getPaddingBottom());
        mBGPath.lineTo(getWidth() - mRightPadding, getPaddingTop());
        mBGPath.lineTo(mLeftPadding, getHeight()/2 + getPaddingTop());
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 100;
        int desiredHeight = mHeight;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) { // match_parent
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) { // wrap_content
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) { // match_parent
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //height = Math.min(desiredHeight, heightSize); // wrap_content
            height = desiredHeight;
        } else {
            height = desiredHeight;
        }
        //height=height/2;

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        int saveCount = canvas.save();

        // Draw Left and Right Sign
        drawSigns(canvas);

        // Draw Background
        initBGPath();
        canvas.drawPath(mBGPath, mBGPaint);

        drawThumb(canvas);



        canvas.restoreToCount(saveCount);
    }

    private void drawSigns(Canvas canvas){
        int xLeft = getPaddingLeft();
        int xRight = getPaddingRight();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            xLeft = getPaddingStart();
            xRight = getPaddingEnd();
        }
        int y = getPaddingTop() + getHeight()/2 + 20;

        canvas.drawText(mSigns, 0, 1, xLeft, y, mTextPaint);
        canvas.drawText(mSigns, 1, 2, getWidth() - xRight - mTextPaint.measureText(mSigns, 1, 2), y, mTextPaint);

        mLeftPadding = xLeft + (int) mTextPaint.measureText(mSigns, 0, 1) + 10;
        mRightPadding = xRight + (int) mTextPaint.measureText(mSigns, 1, 2) + 10;

    }

    private void drawThumb(Canvas canvas){
        int width = getWidth() - mLeftPadding - mRightPadding-100;
        int thumbX = mLeftPadding + getProgress()*width/getMax();


        // Draw thumb - кнопка перемещения
        //canvas.drawCircle(thumbX, getHeight()/2, 20, mTextPaint);



        canvas.drawBitmap(bitmap, thumbX, getHeight()/2-50, paint);


    }

    public static float DpToPx(Context context, float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @SuppressWarnings("unused")
    public static float PxToDp(Context context, float px){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}