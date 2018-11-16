package net.lampa.exchangerates.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lampa.exchangerates.R;
import net.lampa.exchangerates.interfaces.ListItemClickListener;
import net.lampa.exchangerates.model.entities.SimpleRateEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LatestRatesAdapter extends RecyclerView.Adapter<LatestRatesAdapter.LatestRatesViewHolder> {

    private ArrayList<SimpleRateEntity> rates;
    private ListItemClickListener listener;

    public LatestRatesAdapter(ArrayList<SimpleRateEntity> rates, ListItemClickListener listener) {
        this.rates = rates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LatestRatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_rate_item, parent, false);
        return new LatestRatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LatestRatesViewHolder holder, int position) {
        SimpleRateEntity rateEntity = rates.get(position);
        if (rateEntity == null) {
            return;
        }
        holder.nameTxt.setText(rateEntity.getName());
        holder.rateTxt.setText(String.valueOf(rateEntity.getRate()));
        holder.itemView.setOnClickListener(view -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return rates.size();
    }

    public class LatestRatesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_txt)
        TextView nameTxt;

        @BindView(R.id.rate_txt)
        TextView rateTxt;

        public LatestRatesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
