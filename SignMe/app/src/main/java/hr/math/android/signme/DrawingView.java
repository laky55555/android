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

//    public int saveSignature(int number, int student_id, int lecture_id)
//    {
//        if(xCoordinates.size() > DBAdapter.number_of_coords) {
//            discardSignature();
//            return -1;
//        }
//
//        if(xCoordinates.size() < minimalCoordNumber) {
//            discardSignature();
//            return -2;
//        }
//
//        Log.d(TAG, "x coord = " + xCoordinates.toString());
//        Log.d(TAG, "y coord = " + yCoordinates.toString());
//        Log.d(TAG, "pen start = " + penStart.toString());
//        Log.d(TAG, "Saving signature number " + number + " of student "
//                + student_id + " on lecture " + lecture_id);
//        Log.d(TAG, "duljina prije spremanja je: " + xCoordinates.size() + " " + yCoordinates.size() +" " + penStart.size() );
//
//        dbDistances.open();
//        dbSignatures.open();
//
//        Cursor c = dbSignatures.getStudentSignature(student_id, lecture_id);
//        Log.d(TAG, "Got " + c.getCount() + " rows.");
//
//        if(c.getCount() % 3 != 0) {
//            discardSignature();
//            return -3;
//        }
//
//        ArrayList<Float> x_normalised = DTW.normaliseXData(xCoordinates);
//        ArrayList<Float> y_normalised = DTW.normaliseYData(yCoordinates);
//
//        dbSignatures.saveSignature(number, student_id, lecture_id, x_normalised, y_normalised, penStart);
//
//        float max1 = 0, max_temp1;
//        float max2 = 0, max_temp2;
//
//        // In database we save signatures by number from last.
//        for (int i = 1; i <= c.getCount()/3; i++) {
//            ArrayList<Float> x = getArray(c, "x", i);
//            ArrayList<Float> y = getArray(c, "y", i);
//            ArrayList<Float> p = getArray(c, "p", i);
//            Log.d(TAG, "x koordinate iz baze" + x.toString());
//            Log.d(TAG, "y koordinate iz baze" + y.toString());
//            Log.d(TAG, "pen koordinate iz baze" + p.toString());
//            Log.d(TAG, "x koordinate trenutni potpis" + x_normalised.toString());
//            Log.d(TAG, "y koordinate trenutni potpis" + y_normalised.toString());
//            Log.d(TAG, "pen koordinate trenutni potpis" + penStart.toString());
//
//            Log.d(TAG, "duljina iz baze je: " + x.size() + " " + y.size() +" " + p.size() );
//            Log.d(TAG, "duljina live je: " + x_normalised.size() + " " + y_normalised.size() +" " + penStart.size() );
//
//
//            max_temp1 = DTW.calculateDistance1(x_normalised, y_normalised, penStart, x, y, p);
//            max_temp2 = DTW.calculateDistance2(x_normalised, y_normalised, penStart, x, y, p);
//            Log.d(TAG, "Maximal distance between signatures is: " + max_temp1 +
//                    "maximal distance2 is " + max_temp2);
//            if (max_temp1 > max1)
//                max1 = max_temp1;
//            if (max_temp2 > max2)
//                max2 = max_temp2;
//        }
//
//        if(c.getCount() != 0)
//            dbDistances.updateMaxDistance(student_id, lecture_id, max1, max2);
//        dbDistances.close();
//        dbSignatures.close();
//        discardSignature();
//        return 1;
//    }

    /**
     *
     * @param student_id
     * @param lecture_id
     * @return -1 if signature was too long, -2 if signature was too short and student needs to repeat signature,
     *          otherwise number of minimal distance of current signature and signatures in database.
     */
//    public float checkSignature(int student_id, int lecture_id)
//    {
//        if(xCoordinates.size() > DBAdapter.number_of_coords) {
//            discardSignature();
//            return -1;
//        }
//        if(xCoordinates.size() < minimalCoordNumber) {
//            discardSignature();
//            return -2;
//        }
//
//        float min1, min2;
//        ArrayList<Float> min_sum = new ArrayList<>();
//        min_sum.add(0F);
//        min_sum.add(0F);
//
//        dbDistances.open();
//        dbSignatures.open();
//        Cursor c = dbSignatures.getStudentSignature(student_id, lecture_id);
//        Log.d(TAG, "Got " + c.getCount() + " rows. from student id " + student_id + " and lecture id = " + lecture_id);
//
//        if(c.getCount() % 3 != 0) {
//            discardSignature();
//            return -1;
//        }
//
//        ArrayList<Float> xCoordinates_norm = DTW.normaliseXData(xCoordinates);
//        ArrayList<Float> yCoordinates_norm = DTW.normaliseYData(yCoordinates);
//
//        for (int i = 1; i <= c.getCount()/3; i++) {
//            ArrayList<Float> x = getArray(c, "x", i);
//            ArrayList<Float> y = getArray(c, "y", i);
//            ArrayList<Float> p = getArray(c, "p", i);
//            Log.d(TAG, "x koordinate iz baze" + x.toString());
//            Log.d(TAG, "y koordinate iz baze" + y.toString());
//            Log.d(TAG, "pen koordinate iz baze" + penStart.toString());
//            Log.d(TAG, "x koordinate trenutni potpis" + xCoordinates_norm.toString());
//            Log.d(TAG, "y koordinate trenutni potpis" + yCoordinates_norm.toString());
//            Log.d(TAG, "pen koordinate trenutni potpis" + p.toString());
//
//            Log.d(TAG, "duljina iz baze je: " + x.size() + " " + y.size() +" " + p.size() );
//            Log.d(TAG, "duljina live je: " + xCoordinates_norm.size() + " " + yCoordinates_norm.size() +" " + penStart.size() );
//
//
//            //Log.d(TAG, "min1 = " + DTW.calculateDistance1(xCoordinates_norm, yCoordinates_norm, penStart, x, y, p));
//            //Log.d(TAG, "min2 = " + DTW.calculateDistance2(xCoordinates_norm, yCoordinates_norm, penStart, x, y, p));
//            min1 = DTW.calculateDistance1(xCoordinates_norm, yCoordinates_norm, penStart, x, y, p);
//            min2 = DTW.calculateDistance2(xCoordinates_norm, yCoordinates_norm, penStart, x, y, p);
//            Log.d(TAG, "Minmal distance between signatures is: " + min1 +
//                        " minimal distance2 is " + min2 + " min_sum before " + min_sum.toString());
//            min_sum.set(0, min_sum.get(0) + min1);
//            min_sum.set(1, min_sum.get(1) + min2);
//            Log.d(TAG, " min_sum after " + min_sum.toString());
//        }
//
//        float min = Float.POSITIVE_INFINITY;
//        ArrayList<Float> max_distances = dbDistances.getMaxDistance(student_id, lecture_id);
//        for (int i = 0; i < max_distances.size(); i++) {
//            min_sum.set(i, min_sum.get(i)/max_distances.get(i)/c.getCount()*3);
//            if (min > min_sum.get(i))
//                min = min_sum.get(i);
//        }
//
//        dbSignatures.close();
//        dbDistances.close();
//        Toast.makeText(getContext(), "Minmal distance between signatures is: " + min_sum.toString(), Toast.LENGTH_LONG).show();
//        Log.d(TAG, "Minmal distance between signatures is: " + min_sum.toString());
//        return min;
//    }

//    private ArrayList<Float> getArray(Cursor c, String axis, int signature_number)
//    {
//        ArrayList<Float> array = new ArrayList<>();
//        int i=2;
//        c.moveToFirst();
//        do {
//            //Log.d(TAG, "signature number = " + signature_number + " iz baze = " + c.getInt(0));
//            //Log.d(TAG, "axis = " + axis + " iz baze = " + c.getString(1));
//            if(c.getInt(0) == signature_number && c.getString(1).equals(axis))
//                while (true) {
//                    try {
//                        float a = c.getFloat(i++);
//                        if(a == 0)
//                            return array;
//                        array.add(a);
//                    }
//                    catch (Exception e) {
//                        Log.d(TAG, "DOBIVENI ARRAY IZ BAZE " + array.toString());
//                        return array;
//                    }
//                }
//        } while (c.moveToNext());
//
//        return array;
//    }
}