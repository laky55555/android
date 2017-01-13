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

    private final static String TAG = "DrawingView";

    private Paint mPaint;
    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;
    private int broj_dodira = 0;
    private ArrayList<Float> x_coord;
    private ArrayList<Float> y_coord;
    private ArrayList<Integer> pen_start;

    private DBAdapter db;

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
        x_coord = new ArrayList<>(500);
        y_coord = new ArrayList<>(500);
        pen_start = new ArrayList<>(500);
        db = new DBAdapter(getContext());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath( mPath,  mPaint);
        canvas.drawPath( circlePath,  circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        x_coord.add(x);
        y_coord.add(y);
        pen_start.add(1);

    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            x_coord.add(x);
            y_coord.add(y);
            pen_start.add(0);

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);

        pen_start.set(pen_start.size() - 1, -1);

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
        Log.d("POZICIJA", "x = " + Float.toString(x));
        Log.d("POZICIJA", "y = " + Float.toString(y));
        Log.d("BROJ DODIRA", "broj dodira = " + Integer.toString(++broj_dodira));

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public void discardSignature()
    {
        Log.d(TAG, "Discarding signature");
        mCanvas.drawColor(Color.WHITE);
        x_coord.clear();
        y_coord.clear();
        pen_start.clear();
        broj_dodira = 0;
    }

    public boolean saveSignature(int number, int student_id, int lecture_id)
    {
        if(x_coord.size() > 999) {
            discardSignature();
            return false;
        }
        Log.d(TAG, "x coord = " + x_coord.toString());
        Log.d(TAG, "y coord = " + y_coord.toString());
        Log.d(TAG, "pen start = " + pen_start.toString());
        Log.d(TAG, "Saving signature number " + number + " of student "
                + student_id + " on lecture " + lecture_id);

        db.open();
//        db.saveSignature(number, student_id, lecture_id, x_coord, "x");
//        db.saveSignature(number, student_id, lecture_id, y_coord, "y");
//        db.saveSignature(number, student_id, lecture_id, pen_start, "p");
        db.saveSignature(number, student_id, lecture_id, x_coord, y_coord, pen_start);
        discardSignature();
        return true;
    }
}