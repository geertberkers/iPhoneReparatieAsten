package geert.berkers.iphonereparatieasten.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */
public class BrandViewHolder extends RecyclerView.ViewHolder {

    public final TextView modelName;
    public final ImageView modelImage;

    public BrandViewHolder(View itemView) {
        super(itemView);
        modelName = (TextView) itemView.findViewById(R.id.modelName);
        modelImage = (ImageView) itemView.findViewById(R.id.modelImage);
    }
}