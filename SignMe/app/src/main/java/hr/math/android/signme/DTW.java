package hr.math.android.signme;

import android.util.FloatProperty;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

/**
 * Created by mira on 13.01.17..
 */

public class DTW {

    static public float calculateDistance2(ArrayList<Float> x1, ArrayList<Float> y1, ArrayList<Float> pen1,
                                           ArrayList<Float> x2, ArrayList<Float> y2, ArrayList<Float> pen2){

        ArrayList<Float> newX1 = new ArrayList<Float>(), newY1 = new ArrayList<Float>();
        ArrayList<Float> newX2 = new ArrayList<Float>(), newY2 = new ArrayList<Float>();
        refineSeries(x1, y1, pen1, newX1, newY1);
        refineSeries(x2, y2, pen2, newX2, newY2);

        int m = newX1.size();
        int n = newX2.size();
        float cost;
        float[][] dtwArray = new float[m + 1][n + 1];
        float c = (float)0.2;

        for(float[] row : dtwArray)
            Arrays.fill(row, Float.POSITIVE_INFINITY);
        dtwArray[0][0] = 0;
        Log.d(TAG, "m i n su " + m +" " + n);
        for(int i = 1; i < m +1; ++i) {
//            Log.d(TAG, "za i=" + i + "j ide od " + Math.max(1, Math.round(((float)n / (float)m) * i - c * (float)n)) + " do "
//                        + Math.min(n,((float)n / (float)m) * (float)i + c * (float) n) + "razmak " + ((((float)n / (float)m) * (float)i) + (c * (float) n))
//                + " razmak " + (float)n/(float)m * i);
            for (int j = Math.max(1, Math.round(((float)n / (float)m) * i - c * (float)n));
                 j <= Math.min(n, ((float)n / (float)m) * (float)i + c * (float) n); ++j) {
                cost = euclideanDistance(newX1.get(i - 1), newY1.get(i - 1), newX2.get(j - 1), newY2.get(j - 1));
                dtwArray[i][j] = cost + minElement(dtwArray[i - 1][j],
                        dtwArray[i][j - 1],
                        dtwArray[i - 1][j - 1]);
            }
        }

        return dtwArray[m][n];
    }

    static public float calculateDistance1(ArrayList<Float> x1, ArrayList<Float> y1, ArrayList<Float> pen1,
                                           ArrayList<Float> x2, ArrayList<Float> y2, ArrayList<Float> pen2){

        int m = x1.size();
        int n = x2.size();
        float cost;
        float[][] dtwArray = new float[m + 1][n + 1];
        float c = (float)0.2;

        for(int i = 1; i < m + 1; ++i)
            dtwArray[i][0] = Float.POSITIVE_INFINITY;
        for(int i = 1; i < n + 1; ++i)
            dtwArray[0][i] = Float.POSITIVE_INFINITY;
        dtwArray[0][0] = 0;
        for(int i = 1; i < m + 1; ++i)
            for(int j = 1; j < n + 1; ++j){
                cost = euclideanDistance(x1.get(i-1), y1.get(i-1), x2.get(j-1), y2.get(j-1));
                dtwArray[i][j] = cost + minElement(dtwArray[i-1][j],
                        dtwArray[i][j-1],
                        dtwArray[i-1][j-1]);
            }
        return dtwArray[m][n];
    }


    private static void refineSeries(ArrayList<Float> x, ArrayList<Float> y, ArrayList<Float> pen,
                                                 ArrayList<Float> newX, ArrayList<Float> newY){
//        Log.d(TAG, "duljina nizova prije profinjenja je: " + x.size() + " " + y.size() + " " + pen.size());

        float rememberX = -1, rememberY = -1;
        for(int i = 0; i < x.size(); ++i){
            if(pen.get(i) == 3){
                newX.add(x.get(i));
                newY.add(y.get(i));
                rememberX = x.get(i);
                rememberY = y.get(i);
            }
            else if(pen.get(i) == 1 && i != 0){
                float xNow = x.get(i), yNow = y.get(i);
                float diffX = (xNow - rememberX)/11, diffY = (yNow - rememberY)/11;
                for(int k = 1; k < 11; ++k){
                    newX.add(rememberX + diffX * k);
                    newY.add(rememberY + diffY * k);
                }
                newX.add(xNow);
                newY.add(yNow);
            }
            else {
                newX.add(x.get(i));
                newY.add(y.get(i));
            }
        }
        Log.d(TAG, "profinjeni niz je " + newX.toString());
        Log.d(TAG, "profinjeni niz je " + newY.toString());
//        Log.d(TAG, "duljina nizova nakon profinjenja je: " + newX.size() + " " + newY.size() + " " + pen.size());
    }

    public static ArrayList<Float> normaliseXData(ArrayList<Float> x){
        return normaliseAndTranslateData(x, 300);
    }

    public static ArrayList<Float> normaliseYData(ArrayList<Float> y){
        return normaliseAndTranslateData(y, 150);
    }

    private static ArrayList<Float> normaliseAndTranslateData(ArrayList<Float> array, float length){
        float min = Float.POSITIVE_INFINITY, max = 0, element;
        for(int i = 0; i < array.size(); ++i){
            element = array.get(i);
            if(min > element)
                min = element;
            if(max < element)
                max = element;
        }
        min -= 0.1;
        float divisor = (max - min) / length;
        Log.d("DTW", "min = " + min + " max = " + max);
        for(int i = 0; i < array.size(); ++i)
            array.set(i, (array.get(i) - min) / divisor);

        Log.d("DTW", "array after normalization " + array.toString());
        return  array;
    }

    private static float euclideanDistance(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2-y1, 2));
    }

    private static float minElement(float a, float b, float c){
        if(a < b)
            if(a < c)
                return a;
            else return c;
        else
        if(b < c)
            return b;
        else
            return c;
    }
}