package net.lampa.exchangerates.view.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import net.lampa.exchangerates.R;
import net.lampa.exchangerates.di.components.DaggerScreenComponent;
import net.lampa.exchangerates.interfaces.ConversionItemClickListener;
import net.lampa.exchangerates.interfaces.views.DBView;
import net.lampa.exchangerates.interfaces.views.LatestView;
import net.lampa.exchangerates.managers.SharedPreferencesManager;
import net.lampa.exchangerates.model.entities.SimpleRateEntity;
import net.lampa.exchangerates.model.entities.room.ConversionEntity;
import net.lampa.exchangerates.presenter.DBPresenter;
import net.lampa.exchangerates.presenter.LatestPresenter;
import net.lampa.exchangerates.utils.ExchangeRatesUtils;
import net.lampa.exchangerates.utils.Layout;
import net.lampa.exchangerates.view.adapters.ConversionsAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static net.lampa.exchangerates.utils.ExchangeRatesUtils.getDateFromServerFormat;
import static net.lampa.exchangerates.utils.ExchangeRatesUtils.getRateNames;
import static net.lampa.exchangerates.utils.ExchangeRatesUtils.getUserFormatFromDate;

@Layout(id = R.layout.activity_conversion)
public class ConversionActivity extends BaseActivity
        implements LatestView, DBView, ConversionItemClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.conversion_swipe_refresh)
    SwipeRefreshLayout conversionSwipeRefresh;
    @BindView(R.id.update_date_txt)
    TextView updateDateTxt;
    @BindView(R.id.base_currency_txt)
    TextView baseCurrencyTxt;
    @BindView(R.id.another_currency_txt)
    TextView anotherCurrencyTxt;
    @BindView(R.id.conversion_value_edt)
    EditText conversionValueEdt;
    @BindView(R.id.conversion_btn)
    Button conversionBtn;
    @BindView(R.id.result_txt)
    TextView resultTxt;
    @BindView(R.id.conversions_recycler)
    RecyclerView conversionsRecycler;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    @Inject
    LatestPresenter latestPresenter;
    @Inject
    DBPresenter dbPresenter;

    private ConversionsAdapter conversionsAdapter;
    private ArrayList<SimpleRateEntity> rates = new ArrayList<>();
    private List<ConversionEntity> conversions = new ArrayList<>();
    private String baseCurrency;
    private String anotherCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDefaultValues();
        latestPresenter.getLatestRates();
        dbPresenter.getConversionHistory();
    }

    private void setupDefaultValues() {
        latestPresenter.setView(this);
        dbPresenter.setView(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        conversionsAdapter = new ConversionsAdapter(conversions, this);
        conversionsRecycler.setAdapter(conversionsAdapter);
        conversionsRecycler.setLayoutManager(new LinearLayoutManager(this));
        conversionsRecycler.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        baseCurrencyTxt.setOnClickListener(v -> {
            ArrayList<String> rateNames = getRateNames(rates);
            if (rateNames == null || rateNames.isEmpty())
                return;

            int indexOfSelectedItem = rateNames.indexOf(baseCurrency);

            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.title(R.string.base_currency_chose_title)
                    .items(rateNames)
                    .itemsCallbackSingleChoice(indexOfSelectedItem, (dialog, itemView, which, text) -> {
                        latestPresenter.getLatestRates(rateNames.get(which));
                        dialog.dismiss();
                        return true;
                    });

            builder.build().show();
        });
        anotherCurrencyTxt.setOnClickListener(v -> {
            ArrayList<String> rateNames = getRateNames(rates);
            if (rateNames == null || rateNames.isEmpty())
                return;

            int indexOfSelectedItem = rateNames.indexOf(anotherCurrency);

            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.title(R.string.second_currency_chose_title)
                    .items(rateNames)
                    .itemsCallbackSingleChoice(indexOfSelectedItem, (dialog, itemView, which, text) -> {
                        anotherCurrency = rateNames.get(which);
                        anotherCurrencyTxt.setText(anotherCurrency);
                        dialog.dismiss();
                        return true;
                    });

            builder.build().show();
        });

        conversionBtn.setOnClickListener(v -> {
            ArrayList<String> rateNames = getRateNames(rates);
            if (baseCurrency == null) {
                baseCurrencyTxt.setText("");
                Toast.makeText(getApplicationContext(), getString(R.string.select_base_currency), Toast.LENGTH_SHORT).show();
                return;
            }
            if (anotherCurrency == null || !rateNames.contains(anotherCurrency)) {
                anotherCurrency = null;
                anotherCurrencyTxt.setText("");
                Toast.makeText(getApplicationContext(), getString(R.string.select_second_currency), Toast.LENGTH_SHORT).show();
                return;
            }
            if (conversionValueEdt.getText().length() == 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.enter_amount), Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleRateEntity baseRate = ExchangeRatesUtils.findSimpleRateEntity(anotherCurrency, rates);
            if (baseRate == null) {
                return;
            }
            float result = baseRate.getRate() * Float.parseFloat(conversionValueEdt.getText().toString());
            resultTxt.setVisibility(View.VISIBLE);
            resultTxt.setText(
                    getString(R.string.conversion_result_tmp,
                            conversionValueEdt.getText().toString(),
                            baseCurrency,
                            result,
                            anotherCurrency));

            dbPresenter.addConversion(
                    new ConversionEntity(
                            new Date().getTime(),
                            baseRate.getRate(),
                            result,
                            baseCurrency,
                            anotherCurrency)
            );
        });

        conversionSwipeRefresh.setOnRefreshListener(() -> {
            if (baseCurrency != null) {
                latestPresenter.getLatestRates(baseCurrency);
            }
            dbPresenter.getConversionHistory();
        });
    }

    @Override
    public void showLatestRates(ArrayList<SimpleRateEntity> rates, String date, String base) {
        baseCurrency = base;
        baseCurrencyTxt.setText(baseCurrency);
        conversionValueEdt.setText("");
        conversionValueEdt.setHint(getString(R.string.conversion_value_edt_hint, baseCurrency));
        updateDateTxt.setText(getString(R.string.update_tmp, getUserFormatFromDate(getDateFromServerFormat(date))));
        this.rates.clear();
        if (rates != null && !rates.isEmpty()) {
            this.rates.addAll(rates);
        }
        if (anotherCurrency == null && !this.rates.isEmpty()) {
            anotherCurrency = this.rates.get(0).getName();
            anotherCurrencyTxt.setText(anotherCurrency);
        }
        resultTxt.setText("");
        resultTxt.setVisibility(View.GONE);
    }

    @Override
    public void showConversionHistory(List<ConversionEntity> conversions) {
        this.conversions.clear();
        if (conversions != null && !conversions.isEmpty()) {
            this.conversions.addAll(conversions);
        }
        conversionsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHistoryCleared() {
        this.conversions.clear();
        conversionsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConversionAdded() {
        dbPresenter.getConversionHistory();
    }

    @Override
    public void onConversionRemoved() {
        dbPresenter.getConversionHistory();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        conversionSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onItemClick(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.conversion_popup_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_remove:
                    if (conversions.size() > position) {
                        dbPresenter.removeConversion(conversions.get(position));
                    }
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conversion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_clear:
                dbPresenter.clearConversionHistory();
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
        dbPresenter.destroy();
        super.onDestroy();
    }
}
