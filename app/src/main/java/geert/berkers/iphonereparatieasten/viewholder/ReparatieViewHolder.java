package geert.berkers.iphonereparatieasten.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */
public class ReparatieViewHolder extends RecyclerView.ViewHolder {

    public final TextView reparatieInfo;
    public final TextView reparatiePrijs;

    public ReparatieViewHolder(View itemView) {
        super(itemView);
        reparatieInfo = (TextView) itemView.findViewById(R.id.reparatieInfo);
        reparatiePrijs = (TextView) itemView.findViewById(R.id.reparatiePrijs);
    }
}