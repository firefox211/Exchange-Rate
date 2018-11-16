package net.lampa.exchangerates.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import net.lampa.exchangerates.R;
import net.lampa.exchangerates.di.components.DaggerScreenComponent;
import net.lampa.exchangerates.interfaces.ListItemClickListener;
import net.lampa.exchangerates.interfaces.views.LatestView;
import net.lampa.exchangerates.managers.SharedPreferencesManager;
import net.lampa.exchangerates.model.entities.SimpleRateEntity;
import net.lampa.exchangerates.presenter.LatestPresenter;
import net.lampa.exchangerates.utils.Layout;
import net.lampa.exchangerates.view.adapters.LatestRatesAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

import static net.lampa.exchangerates.utils.ExchangeRatesUtils.getDateFromServerFormat;
import static net.lampa.exchangerates.utils.ExchangeRatesUtils.getRateNames;
import static net.lampa.exchangerates.utils.ExchangeRatesUtils.getUserFormatFromDate;

@Layout(id = R.layout.activity_main)
public class MainActivity extends BaseActivity implements LatestView, ListItemClickListener {


    @BindView(R.id.base_currency_txt)
    TextView baseCurrencyTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.update_date_txt)
    TextView updateDateTxt;
    @BindView(R.id.latest_rates_swipe_refresh)
    SwipeRefreshLayout latestRatesSwipeRefresh;
    @BindView(R.id.latest_rates_recycler)
    RecyclerView latestRatesRecycler;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    @Inject
    LatestPresenter latestPresenter;

    private LatestRatesAdapter latestRatesAdapter;
    private ArrayList<SimpleRateEntity> rates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDefaultValues();
        latestPresenter.getLatestRates();
    }

    private void setupDefaultValues() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        latestPresenter.setView(this);
        baseCurrencyTxt.setText(getString(R.string.base_currency_tmp,
                sharedPreferencesManager.getBaseCurrency()));
        baseCurrencyTxt.setOnClickListener(v -> {
            ArrayList<String> rateNames = getRateNames(rates);
            int indexOfSelectedItem = rateNames.indexOf(sharedPreferencesManager.getBaseCurrency());

            MaterialDialog.Builder builder = new MaterialDialog.Builder(MainActivity.this);
            builder.title(R.string.base_currency_chose_title)
                    .items(rateNames)
                    .itemsCallbackSingleChoice(indexOfSelectedItem, (dialog, itemView, which, text) -> {
                        String currency = rates.get(which).getName();
                        sharedPreferencesManager.setBaseCurrency(currency);
                        baseCurrencyTxt.setText(getString(R.string.base_currency_tmp, currency));
                        latestPresenter.getLatestRates();
                        dialog.dismiss();
                        return true;
                    });

            builder.build().show();
        });


        latestRatesAdapter = new LatestRatesAdapter(rates, this);
        latestRatesRecycler.setAdapter(latestRatesAdapter);
        latestRatesRecycler.setLayoutManager(new LinearLayoutManager(this));
        latestRatesRecycler.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        latestRatesSwipeRefresh.setOnRefreshListener(() -> latestPresenter.getLatestRates());
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, RateFinalActivity.class);
        if (rates.size() > position && rates.get(position) != null) {
            intent.putExtra(RateFinalActivity.RATE_NAME_INTENT_KEY, rates.get(position).getName());
        }
        startActivity(intent);
    }

    @Override
    public void showLatestRates(ArrayList<SimpleRateEntity> rates, String date, String base) {
        baseCurrencyTxt.setText(getString(R.string.base_currency_tmp, base));
        updateDateTxt.setText(getString(R.string.update_tmp, getUserFormatFromDate(getDateFromServerFormat(date))));
        this.rates.clear();
        if (rates != null && !rates.isEmpty()) {
            this.rates.addAll(rates);
        }
        if (latestRatesAdapter != null) {
            latestRatesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        latestRatesSwipeRefresh.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_conversion:
                Intent intent = new Intent(this, ConversionActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void inject() {
        DaggerScreenComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        latestPresenter.destroy();
        super.onDestroy();
    }


}
