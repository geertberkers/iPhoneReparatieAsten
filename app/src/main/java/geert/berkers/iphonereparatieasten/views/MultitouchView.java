package geert.berkers.iphonereparatieasten.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.activitytest.MultiTouchTestActivity;

/**
 * Created by Geert.
 */
public class MultitouchView extends View {

    private static final int SIZE = 150;

    private SparseArray<PointF> mActivePointers;
    private Paint mPaint;
    private int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.BLACK,
            Color.GRAY, Color.DKGRAY, Color.LTGRAY, Color.YELLOW,
            Color.MAGENTA, Color.CYAN,};


    public MultitouchView(Context context) {
        super(context);
        initView();
    }

    public MultitouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mActivePointers = new SparseArray<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {
            // New Pointer
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {

                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);

                handleMultiTouches(mActivePointers.size());
                break;
            }
            // Moved Pointer
            case MotionEvent.ACTION_MOVE: {
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = mActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers.remove(pointerId);
                break;
            }
        }
        invalidate();

        return true;
    }

    private void handleMultiTouches(int activePointerSize) {
        if (getContext() instanceof MultiTouchTestActivity) {
            FloatingActionButton fabWorking = (FloatingActionButton) ((MultiTouchTestActivity) getContext()).findViewById(R.id.fabWorking);
            FloatingActionButton fabNotWorking = (FloatingActionButton) ((MultiTouchTestActivity) getContext()).findViewById(R.id.fabNotWorking);

            TextView txtInfo = (TextView) ((MultiTouchTestActivity) getContext()).findViewById(R.id.txtInfo);
            TextView txtQuestion = (TextView) ((MultiTouchTestActivity) getContext()).findViewById(R.id.txtQuestion);

            if(activePointerSize == 3){
                txtInfo.setText(R.string.multitouch_4_fingers);
            } else if (activePointerSize == 4){
                txtInfo.setVisibility(INVISIBLE);
                fabWorking.setVisibility(VISIBLE);
                fabNotWorking.setVisibility(GONE);
                txtQuestion.setText(R.string.result_multitouch);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw all pointers
        for (int size = mActivePointers.size(), i = 0; i < size; i++) {
            PointF point = mActivePointers.valueAt(i);
            mPaint.setColor(colors[i % 10]);

            if (point != null) {
                canvas.drawCircle(point.x, point.y, SIZE, mPaint);
            }
        }
    }

}