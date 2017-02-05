package hr.math.android.signme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View {

    private final static String TAG = "DrawingView diff";

    private Paint mPaint;
    //public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;
    ArrayList<Float> xCoordinates;
    ArrayList<Float> yCoordinates;
    ArrayList<Float> penStart;
    private final int backgroundColor = 0xFFCCFFFF;
    final float touchDownCode = 1;
    final float touchMoveCode = 2;
    final float touchUpCode = 3;


    public DrawingView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context=c;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        xCoordinates = new ArrayList<>(500);
        yCoordinates = new ArrayList<>(500);
        penStart = new ArrayList<>(500);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(backgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath( mPath,  mPaint);
        canvas.drawPath( circlePath,  circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 2;

    private void touchStart(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        xCoordinates.add(x);
        yCoordinates.add(y);
        penStart.add(touchDownCode);

    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            xCoordinates.add(x);
            yCoordinates.add(y);
            penStart.add(touchMoveCode);

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);

        penStart.set(penStart.size() - 1, touchUpCode);

        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath,  mPaint);
        Log.d("DRAW PATH", "Crta path");
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        //Log.d("POZICIJA", "x = " + Float.toString(x));
        //Log.d("POZICIJA", "y = " + Float.toString(y));
        //Log.d("BROJ DODIRA", "broj dodira = " + Integer.toString(++broj_dodira));

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    public void discardSignature()
    {
        Log.d(TAG, "Discarding signature");
        //mCanvas.drawColor(Color.WHITE);
        //mCanvas.drawColor(Color.GRAY);
        mCanvas.drawColor(backgroundColor);
        xCoordinates.clear();
        yCoordinates.clear();
        penStart.clear();
    }

}