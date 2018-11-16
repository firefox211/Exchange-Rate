package net.lampa.exchangerates.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lampa.exchangerates.R;
import net.lampa.exchangerates.model.entities.SimpleRateEntity;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.lampa.exchangerates.utils.ExchangeRatesUtils.getUserFormatFromDate;

public class RateFinalAdapter extends RecyclerView.Adapter<RateFinalAdapter.RateFinalViewHolder> {

    private ArrayList<SimpleRateEntity> rates;

    public RateFinalAdapter(ArrayList<SimpleRateEntity> rates) {
        this.rates = rates;
    }

    @NonNull
    @Override
    public RateFinalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rate_final_item, parent, false);
        return new RateFinalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateFinalViewHolder holder, int position) {
        SimpleRateEntity rateEntity = rates.get(getItemCount() - 1 - position);
        if (rateEntity == null) {
            return;
        }
        holder.nameTxt.setText(rateEntity.getName());
        holder.rateTxt.setText(String.valueOf(rateEntity.getRate()));
        holder.dateTxt.setText(getUserFormatFromDate(new Date(rateEntity.getDateInMillis())));
    }

    @Override
    public int getItemCount() {
        return rates.size();
    }

    public class RateFinalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_txt)
        TextView nameTxt;

        @BindView(R.id.date_txt)
        TextView dateTxt;

        @BindView(R.id.rate_txt)
        TextView rateTxt;

        public RateFinalViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
