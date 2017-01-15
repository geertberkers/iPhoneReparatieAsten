package geert.berkers.iphonereparatieasten.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Geert.
 */
public class CompassDrawView extends View {

    private Paint paint;
    private RectF rectF;
    private ArrayList<Float> degrees;

    public CompassDrawView(Context context) {
        super(context);
        init();
    }

    public CompassDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CompassDrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        degrees = new ArrayList<>();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        rectF = new RectF();
    }

    public void setOvalSize(int width, int height) {
        int top = (height - width) / 2;
        rectF.set(40, top + 40, width - 40, top + width - 40);
    }

    public void setDegrees(ArrayList<Float> degrees) {
        this.degrees = degrees;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);

        for (Float degree : degrees) {
            canvas.drawArc(rectF, -degree - 90, 1, false, paint);
        }
    }
}