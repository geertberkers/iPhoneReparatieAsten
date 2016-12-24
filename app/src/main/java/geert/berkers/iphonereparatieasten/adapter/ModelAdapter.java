package geert.berkers.iphonereparatieasten.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.model.Model;
import geert.berkers.iphonereparatieasten.viewholder.BrandViewHolder;

/**
 * Created by Geert.
 */

public class ModelAdapter extends RecyclerView.Adapter<BrandViewHolder> {

    private final List<Model> modelList;

    public ModelAdapter(List<Model> models) {
        modelList = models;
    }

    @Override
    public BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_cardview, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BrandViewHolder holder, int position) {
        holder.modelName.setText(modelList.get(position).getModelName());
        holder.modelImage.setImageResource(modelList.get(position).getModelImage());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}