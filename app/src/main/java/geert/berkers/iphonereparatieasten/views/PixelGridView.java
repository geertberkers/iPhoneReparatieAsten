package geert.berkers.iphonereparatieasten.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.activitytest.TouchscreenTestActivity;

/**
 * Created by Geert.
 */
public class PixelGridView extends View {

    private final Context context;

    private int counter;
    private int numRows;
    private int numColumns;
    private int cellWidth;
    private int cellHeight;

    private final Paint blackPaint = new Paint();
    private final Paint greenPaint = new Paint();

    private boolean[][] cellChecked;

    public PixelGridView(Context context) {
        this(context, null);
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        greenPaint.setColor(Color.GREEN);
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        counter = 0;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellWidth = getWidth() / numColumns;
        cellHeight = getHeight() / numRows;

        cellChecked = new boolean[numColumns][numRows];

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        if (numColumns == 0 || numRows == 0) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (cellChecked[i][j]) {
                    canvas.drawRect(i * cellWidth, j * cellHeight, (i + 1) * cellWidth, (j + 1) * cellHeight, greenPaint);
                }
            }
        }

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hideHintAndButtons(true);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            hideHintAndButtons(false);
        }

        try {
            int column = (int) (event.getX() / cellWidth);
            int row = (int) (event.getY() / cellHeight);

            if (!cellChecked[column][row]) {
                counter++;

                if (counter == numColumns * numRows) {
                    if (context instanceof TouchscreenTestActivity) {
                        FloatingActionButton fabWorking = (FloatingActionButton) ((TouchscreenTestActivity) context).findViewById(R.id.fabWorking);
                        fabWorking.setVisibility(VISIBLE);

                        FloatingActionButton fabNotWorking = (FloatingActionButton) ((TouchscreenTestActivity) context).findViewById(R.id.fabNotWorking);
                        fabNotWorking.setVisibility(GONE);

                        TextView txtQuestion = (TextView) ((TouchscreenTestActivity) context).findViewById(R.id.txtQuestion);
                        txtQuestion.setText(R.string.result_test_touchscreen);
                    }
                }
            }

            cellChecked[column][row] = true;
            invalidate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

    private void hideHintAndButtons(boolean hide) {
        TextView info = (TextView) ((TouchscreenTestActivity) context).findViewById(R.id.txtInfo);
        TextView question = (TextView) ((TouchscreenTestActivity) context).findViewById(R.id.txtQuestion);
        LinearLayout bottom = (LinearLayout) ((TouchscreenTestActivity) context).findViewById(R.id.bottom);

        if (hide) {
            info.setVisibility(GONE);
            question.setVisibility(GONE);
            bottom.setVisibility(GONE);
        } else {
            info.setVisibility(VISIBLE);
            question.setVisibility(VISIBLE);
            bottom.setVisibility(VISIBLE);
        }
    }
}
