package hr.math.android.signme;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawingView extends View {

    private final static String TAG = "DrawingView diff";

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
    private ArrayList<Float> pen_start;
    private final int backgroundColor = 0xFFCCFFFF;
    private final float touchDownCode = 1;
    private final float touchMoveCode = 2;
    private final float touchUpCode = 3;

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
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        x_coord.add(x);
        y_coord.add(y);
        pen_start.add(touchDownCode);

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
            pen_start.add(touchMoveCode);

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);

        pen_start.set(pen_start.size() - 1, touchUpCode);

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
        //mCanvas.drawColor(Color.WHITE);
        //mCanvas.drawColor(Color.GRAY);
        mCanvas.drawColor(backgroundColor);
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
        db.saveSignature(number, student_id, lecture_id, DTW.normaliseXData(x_coord), DTW.normaliseYData(y_coord), pen_start);
        discardSignature();
        return true;
    }

    /**
     *
     * @param student_id
     * @param lecture_id
     * @return -1 if signature was too long, student needs to repeat signature,
     *          number of minimal distance of current signature and signatures in database.
     */
    public float checkSignature(int student_id, int lecture_id)
    {
        if(x_coord.size() > 999) {
            discardSignature();
            return -1;
        }

        float min = 999;
        float min_temp;

        db.open();
        Cursor c = db.getStudentSignature(student_id, lecture_id);
        Log.d(TAG, "Got " + c.getCount() + " rows.");

        if(c.getCount() % 3 != 0) {
            discardSignature();
            return -1;
        }

        ArrayList<Float> x_coord_norm = DTW.normaliseXData(x_coord);
        ArrayList<Float> y_coord_norm = DTW.normaliseYData(y_coord);

        for (int i = 1; i <= c.getCount()/3; i++) {
            ArrayList<Float> x = getArray(c, "x", i);
            ArrayList<Float> y = getArray(c, "y", i);
            ArrayList<Float> p = getArray(c, "p", i);
            Log.d(TAG, "x koordinate potpisa" + x.toString());
            Log.d(TAG, "y koordinate potpisa" + y.toString());
            Log.d(TAG, "pen koordinate iz potpisa" + pen_start.toString());
            Log.d(TAG, "x koordinate iz baze" + x_coord_norm.toString());
            Log.d(TAG, "y koordinate iz baze" + y_coord_norm.toString());
            Log.d(TAG, "pen koordinate iz baze" + p.toString());

            if(i == 1) {
                min = DTW.calculateDistance1(x_coord_norm, y_coord_norm, p, x, y, pen_start);
                Log.d(TAG, "Minmal distance between signatures is: " + min);
            }
            else {
                min_temp = DTW.calculateDistance1(x_coord_norm, y_coord_norm, p, x, y, pen_start);
                Log.d(TAG, "Minmal distance between signatures is: " + min_temp);
                if (min_temp < min)
                    min = min_temp;
            }
        }

        db.close();
        Log.d(TAG, "Minmal distance between signatures is: " + min);
        Toast.makeText(getContext(), "Minmal distance between signatures is: " + min, Toast.LENGTH_LONG).show();
        return min;
    }

    private ArrayList<Float> getArray(Cursor c, String axis, int signature_number)
    {
        ArrayList<Float> array = new ArrayList<>();
        int i=2;
        c.moveToFirst();
        do {
            //Log.d(TAG, "signature number = " + signature_number + " iz baze = " + c.getInt(0));
            //Log.d(TAG, "axis = " + axis + " iz baze = " + c.getString(1));
            if(c.getInt(0) == signature_number && c.getString(1).equals(axis))
                while (true) {
                    try {
                        float a = c.getFloat(i++);
                        if(a == 0)
                            return array;
                        array.add(a);
                    }
                    catch (Exception e) {
                        Log.d(TAG, "DOBIVENI ARRAY IZ BAZE " + array.toString());
                        return array;
                    }
                }
        } while (c.moveToNext());

        return array;
    }
}