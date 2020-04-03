package com.davis.drawtrangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {

    private List<PointBean> pointLists = new ArrayList<PointBean>();

    private PointBean pointBean = new PointBean(-1, -1, -1, -1);

    private static int height = 30;
    private static int bottom = 10;

    private Paint paint = new Paint(){
        {
            setColor(Color.RED);
            setAntiAlias(true);
            setStrokeWidth(4.0f);
        }
    };

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void clear(){
        if(pointBean != null){
            pointBean.setStartX(-1);
            pointBean.setStartY(-1);
            pointBean.setEndX(-1);
            pointBean.setEndY(-1);
        }

        if(pointLists != null && pointLists.size() > 0){
            pointLists.clear();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if(pointLists != null && pointLists.size() > 0){
            for(int i=0;i<pointLists.size();i++){
                PointBean pb = pointLists.get(i);
                canvas.drawLine(pb.getStartX(), pb.getStartY(), pb.getEndX(), pb.getEndY(), paint);
                drawTrangle(canvas, paint, pb.getStartX(), pb.getStartY(), pb.getEndX(), pb.getEndY(), height, bottom);
            }
        }

        if(pointBean != null && pointBean.getStartX() != -1
                && pointBean.getStartY() != -1 && pointBean.getEndX() != -1
                && pointBean.getEndY() != -1){
            canvas.drawLine(pointBean.getStartX(), pointBean.getStartY(), pointBean.getEndX(), pointBean.getEndY(), paint);
            drawTrangle(canvas, paint, pointBean.getStartX(), pointBean.getStartY(), pointBean.getEndX(), pointBean.getEndY(), height, bottom);
        }
    }

    /**
     * 绘制三角
     * @param canvas
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @param height
     * @param bottom
     */
    private void drawTrangle(Canvas canvas, Paint paintLine, float fromX, float fromY, float toX, float toY, int height, int bottom){
        try{
            float juli = (float) Math.sqrt((toX - fromX) * (toX - fromX)
                    + (toY - fromY) * (toY - fromY));// 获取线段距离
            float juliX = toX - fromX;// 有正负，不要取绝对值
            float juliY = toY - fromY;// 有正负，不要取绝对值
            float dianX = toX - (height / juli * juliX);
            float dianY = toY - (height / juli * juliY);
            float dian2X = fromX + (height / juli * juliX);
            float dian2Y = fromY + (height / juli * juliY);
            //终点的箭头
            Path path = new Path();
            path.moveTo(toX, toY);// 此点为三边形的起点
            path.lineTo(dianX + (bottom / juli * juliY), dianY
                    - (bottom / juli * juliX));
            path.lineTo(dianX - (bottom / juli * juliY), dianY
                    + (bottom / juli * juliX));
            path.close(); // 使这些点构成封闭的三边形
            canvas.drawPath(path, paintLine);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                onActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onActionUp(event);
                break;
        }
        return true;
        //return super.onTouchEvent(event);
    }

    private void onActionDown(MotionEvent event){
        try {
            if(pointBean == null){
                pointBean = new PointBean(-1, -1, -1, -1);
            }
            pointBean.setStartX(event.getX());
            pointBean.setStartY(event.getY());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        invalidate();
    }

    private void onActionMove(MotionEvent event){
        try{
            if(pointBean != null){
                pointBean.setEndX(event.getX());
                pointBean.setEndY(event.getY());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        invalidate();
    }

    private void onActionUp(MotionEvent event){
        try {
            if(pointBean != null){
                pointBean.setEndX(event.getX());
                pointBean.setEndY(event.getY());
                PointBean pb = new PointBean();
                pb.setStartX(pointBean.getStartX());
                pb.setStartY(pointBean.getStartY());
                pb.setEndX(pointBean.getEndX());
                pb.setEndY(pointBean.getEndY());
                pointLists.add(pb);
                pointBean.setStartX(-1);
                pointBean.setStartY(-1);
                pointBean.setEndX(-1);
                pointBean.setEndY(-1);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        invalidate();
    }

}
