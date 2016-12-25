package geert.berkers.iphonereparatieasten.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import geert.berkers.iphonereparatieasten.views.BrandViewHolder;
import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.model.Brand;

/**
 * Created by Geert.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandViewHolder> {

    private final List<Brand> brandList;

    public BrandAdapter(List<Brand> brands) {
        brandList = brands;
    }

    @Override
    public BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_cardview, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BrandViewHolder holder, int position) {
        holder.modelName.setText(brandList.get(position).getBrand());
        holder.modelImage.setImageResource(brandList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

}