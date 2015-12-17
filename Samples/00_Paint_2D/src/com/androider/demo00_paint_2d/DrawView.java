package com.androider.demo00_paint_2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener {
    private static final String TAG = "DrawView";

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
    	
    	//draw the watermark
    	if (points.size()>0)
    	{
	        Point pointSingle = points.get(points.size()-1);
	   	 	Paint paint = new Paint();
		    paint.setColor(Color.RED);
		    //paint.setTextSize(35);
		    paint.setAntiAlias(true);
		    paint.setUnderlineText(true);
		    //canvas.drawText("Sample watermark", pointSingle.x, pointSingle.y, paint);
    	}
	    //draw the trailing points
	    paint.setColor(Color.RED);
        for (Point point : points) {
            //canvas.drawCircle(point.x, point.y, point.marime, paint);
            canvas.drawCircle(point.x, point.y, point.marime, paint);
            //canvas.drawText("BLA BLA", point.x, point.y, paint);
            
        }
        
//    	if (points!= null && points.size()>0)
//    	{
//    		paint.setColor(Color.BLACK);
//	        Point point = points.get(points.size()-1);
//	        canvas.drawCircle(point.x, point.y, getRandomSize(), paint);
//    	}
    }

    public boolean onTouch(View view, MotionEvent event) {
    	// append the point position to a list
        Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        point.marime= getRandomSize();
        points.add(point);
        
        // add text on the screen
        invalidate();
        Log.d(TAG, "point: " + point);
        return true;
    }
    
    int getRandomSize()
    {
    	Random r = new Random();
    	int valRandom = r.nextInt(20-3) + 3;
    	return (valRandom);
    }
}

class Point {
    float x, y;
    int marime;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}