package net.lampa.exchangerates.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lampa.exchangerates.R;
import net.lampa.exchangerates.interfaces.ConversionItemClickListener;
import net.lampa.exchangerates.model.entities.room.ConversionEntity;
import net.lampa.exchangerates.utils.ExchangeRatesUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversionsAdapter extends RecyclerView.Adapter<ConversionsAdapter.ConversionsViewHolder> {

    private List<ConversionEntity> conversions;
    private ConversionItemClickListener listener;

    public ConversionsAdapter(List<ConversionEntity> conversions, ConversionItemClickListener listener) {
        this.conversions = conversions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConversionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conversion_item, parent, false);
        return new ConversionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionsViewHolder holder, int position) {
        ConversionEntity conversionEntity = conversions.get(position);
        if (conversionEntity == null) {
            return;
        }
        holder.resultTxt.setText(holder.resultTxt.getContext()
                .getString(R.string.result_tmp,
                        String.valueOf(conversionEntity.getResult())));

        holder.currencyTxt.setText(
                holder.currencyTxt.getContext()
                        .getString(R.string.currency_tmp,
                                conversionEntity.getBaseCurrency(),
                                conversionEntity.getSecondCurrency()));

        holder.timeTxt.setText(
                ExchangeRatesUtils.getUserFullFormatFromDate(
                        new Date(conversionEntity.getDateInMillis())
                ));
        holder.rateTxt.setText(holder.rateTxt.getContext()
                .getString(R.string.rate_tmp,
                        String.valueOf(conversionEntity.getRate())));

        holder.itemView.setOnClickListener(view -> listener.onItemClick(view, position));
    }


    @Override
    public int getItemCount() {
        return conversions.size();
    }

    public class ConversionsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.result_txt)
        TextView resultTxt;
        @BindView(R.id.currency_txt)
        TextView currencyTxt;
        @BindView(R.id.time_txt)
        TextView timeTxt;
        @BindView(R.id.rate_txt)
        TextView rateTxt;

        public ConversionsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
