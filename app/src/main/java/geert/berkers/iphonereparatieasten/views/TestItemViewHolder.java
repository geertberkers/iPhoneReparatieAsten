package geert.berkers.iphonereparatieasten.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */
public class TestItemViewHolder extends RecyclerView.ViewHolder {

    public final TextView testName;
    public final ImageView testImage;

    public TestItemViewHolder(View itemView) {
        super(itemView);
        testName = (TextView) itemView.findViewById(R.id.testName);
        testImage = (ImageView) itemView.findViewById(R.id.testImage);
    }
}