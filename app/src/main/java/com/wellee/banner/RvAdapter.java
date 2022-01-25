package com.wellee.banner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wellee.libbanner.adapter.BannerAdapter;
import com.wellee.libbanner.view.BannerView;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {

    private List<List<BannerInfo>> banners;

    public RvAdapter(List<List<BannerInfo>> banners) {
        this.banners = banners;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        List<BannerInfo> items = banners.get(position);
        holder.bannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getItemView(int position, View convertView) {
                ImageView imageView;
                if (convertView == null) {
                    imageView = new ImageView(holder.bannerView.getContext());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                } else {
                    imageView = (ImageView) convertView;
                }
                Glide.with(holder.bannerView.getContext())
                        .load(items.get(position).getUrl())
                        .apply(new RequestOptions().placeholder(R.drawable.default_picture))
                        .into(imageView);
                return imageView;
            }

            @Override
            public int getItemCount() {
                return items.size();
            }

        });
    }

    @Override
    public int getItemCount() {
        return banners == null ? 0 : banners.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        BannerView bannerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerView = itemView.findViewById(R.id.bv);
        }
    }
}
