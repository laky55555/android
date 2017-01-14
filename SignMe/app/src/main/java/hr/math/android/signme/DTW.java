package hr.math.android.signme;

import java.util.ArrayList;

/**
 * Created by mira on 13.01.17..
 */

public class DTW {

    static public double calculateDistance(ArrayList<Double> x1, ArrayList<Double> y1,
                                           ArrayList<Double> x2, ArrayList<Double> y2){
        int m = x1.size();
        int n = x2.size();
        double cost;
        double[][] dtwArray = new double[m + 1][n + 1];
        for(int i = 1; i < m + 1; ++i)
            dtwArray[i][0] = Double.POSITIVE_INFINITY;
        for(int i = 1; i < n + 1; ++i)
            dtwArray[0][i] = Double.POSITIVE_INFINITY;
        dtwArray[0][0] = 0;
        for(int i = 1; i < m + 1; ++i)
            for(int j = 1; j < n + 1; ++j){
                cost = euclideanDistance(x1.get(i-1), y1.get(i-1), x2.get(j-1), y2.get(j-1));
                dtwArray[i][j] = cost + minElement(dtwArray[i-1][j],
                        dtwArray[i][j-1],
                        dtwArray[i-1][j-1]);
            }
        return dtwArray[n][m];
    }

    public static ArrayList<Double> normaliseXData(ArrayList<Double> x){
        return normaliseAndTranslateData(x, 300);
    }

    public static ArrayList<Double> normaliseYData(ArrayList<Double> y){
        return normaliseAndTranslateData(y, 150);
    }

    private static ArrayList<Double> normaliseAndTranslateData(ArrayList<Double> array, double length){
        double min = 0.0, max = 0.0, element;
        for(int i = 0; i < array.size(); ++i){
            element = array.get(i);
            if(min > element)
                min = element;
            if(max < element)
                max = element;
        }
        double divisor = (max - min) / length;
        for(int i = 0; i < array.size(); ++i)
            array.set(i, (array.get(i) - min) / divisor);
        return  array;
    }

    private static double euclideanDistance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2-y1, 2));
    }

    private static double minElement(double a, double b, double c){
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