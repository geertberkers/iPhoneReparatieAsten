package geert.berkers.iphonereparatieasten.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.List;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.model.Reparatie;
import geert.berkers.iphonereparatieasten.views.ReparatieViewHolder;

/**
 * Created by Geert.
 */

public class ReparatieAdapter extends RecyclerView.Adapter<ReparatieViewHolder> {

    private final List<Reparatie> reparaties;

    public ReparatieAdapter(List<Reparatie> models) {
        reparaties = models;
    }

    @Override
    public ReparatieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reparatie_row, parent, false);
        return new ReparatieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReparatieViewHolder holder, int position) {
        String reparatie = reparaties.get(position).getReparatie() + ":";
        holder.reparatieInfo.setText(reparatie);
        double price = reparaties.get(position).getPrice();

        DecimalFormat formatter = new DecimalFormat("#0.00");
        String priceString = "€ " + formatter.format(price);
        holder.reparatiePrijs.setText("" + priceString);
    }

    @Override
    public int getItemCount() {
        return reparaties.size();
    }
}