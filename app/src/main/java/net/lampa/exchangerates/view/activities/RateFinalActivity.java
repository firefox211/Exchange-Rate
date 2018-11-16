package net.lampa.exchangerates.view.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import net.lampa.exchangerates.R;
import net.lampa.exchangerates.di.components.DaggerScreenComponent;
import net.lampa.exchangerates.interfaces.views.HistoryView;
import net.lampa.exchangerates.managers.SharedPreferencesManager;
import net.lampa.exchangerates.model.entities.SimpleRateEntity;
import net.lampa.exchangerates.presenter.HistoryPresenter;
import net.lampa.exchangerates.utils.Layout;
import net.lampa.exchangerates.view.adapters.RateFinalAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

import static net.lampa.exchangerates.utils.ExchangeRatesUtils.getUserFormatFromDate;

@Layout(id = R.layout.activity_rate_final)
public class RateFinalActivity extends BaseActivity implements HistoryView {

    public static final String RATE_NAME_INTENT_KEY = "RateFinalActivity.RATE_NAME_INTENT_KEY";


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.final_line_chart)
    LineChart lineChart;
    @BindView(R.id.start_at_btn)
    Button startAtBtn;
    @BindView(R.id.end_at_btn)
    Button endAtBtn;
    @BindView(R.id.final_rate_swipe_refresh)
    SwipeRefreshLayout finalRateSwipeRefresh;
    @BindView(R.id.final_rate_recycler_view)
    RecyclerView finalRateRecyclerView;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;
    @Inject
    HistoryPresenter historyPresenter;

    private String rateFromName;
    private String rateToName;

    private Date dateFrom, dateTo;
    private SimpleDateFormat dateFormatTo;
    private RateFinalAdapter rateFinalAdapter;

    List<Entry> entries = new ArrayList<>();
    private ArrayList<SimpleRateEntity> itemHistoryArray = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDefaultValues();
        historyPresenter.getHistory(rateFromName, rateToName, dateFrom, dateTo);
    }

    private void setupDefaultValues() {
        historyPresenter.setView(this);
        getIntentValues();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        rateFromName = sharedPreferencesManager.getBaseCurrency();
        dateFormatTo = new SimpleDateFormat("dd.MM", Locale.getDefault());

        setupDateValues();

        rateFinalAdapter = new RateFinalAdapter(itemHistoryArray);

        finalRateRecyclerView.setAdapter(rateFinalAdapter);
        finalRateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        finalRateRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        finalRateSwipeRefresh.setOnRefreshListener(() ->
                historyPresenter.getHistory(rateFromName, rateToName, dateFrom, dateTo));

        setupChart();

    }

    private void getIntentValues() {
        if (getIntent() == null) {
            return;
        }
        rateToName = getIntent().getStringExtra(RATE_NAME_INTENT_KEY);
        if (rateToName == null || rateToName.length() == 0) {
            rateToName = "EUR";
        }
    }


    private void setupDateValues() {
        dateTo = new Date();
        dateFrom = new Date();
        dateFrom.setTime((dateTo.getTime() - (30 * 24 * 60 * 60 * 1000L)));


        startAtBtn.setText(getUserFormatFromDate(dateFrom));
        endAtBtn.setText(getUserFormatFromDate(dateTo));

        startAtBtn.setOnClickListener(v -> showDatePickerDialog(dateFrom,
                dateTo.getTime(),
                (dateTo.getTime() - (365 * 24 * 60 * 60 * 1000L)),
                (view, year, month, dayOfMonth) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, dayOfMonth);
                    dateFrom = cal.getTime();
                    startAtBtn.setText(getUserFormatFromDate(dateFrom));
                    historyPresenter.getHistory(rateFromName, rateToName, dateFrom, dateTo);
                }));

        endAtBtn.setOnClickListener(v -> showDatePickerDialog(dateTo,
                new Date().getTime(),
                dateFrom.getTime(),
                (view, year, month, dayOfMonth) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, dayOfMonth);
                    dateTo = cal.getTime();
                    endAtBtn.setText(getUserFormatFromDate(dateTo));
                    historyPresenter.getHistory(rateFromName, rateToName, dateFrom, dateTo);
                }));
    }

    private void showDatePickerDialog(Date date, long maxDate, long minDate, DatePickerDialog.OnDateSetListener listener) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(maxDate);
        datePickerDialog.getDatePicker().setMinDate(minDate);
        datePickerDialog.show();
    }


    private void setupChart() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter((value, axis) -> {
            Date d = new Date(Float.valueOf(value).longValue());
            return dateFormatTo.format(d);
        });
        setChartData();
    }

    private void setChartData() {
        entries.clear();
        if (!itemHistoryArray.isEmpty()) {
            for (SimpleRateEntity rateEntity : itemHistoryArray) {
                entries.add(new Entry(rateEntity.getDateInMillis(), rateEntity.getRate()));
            }
        }
        Collections.sort(entries, new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(entries, rateToName);
        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);
        lineChart.getDescription().setText("");
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    @Override
    public void showHistory(ArrayList<SimpleRateEntity> itemHistoryArray) {
        this.itemHistoryArray.clear();
        if (itemHistoryArray != null && !itemHistoryArray.isEmpty()) {
            this.itemHistoryArray.addAll(itemHistoryArray);
        }
        setChartData();
        if (rateFinalAdapter != null) {
            rateFinalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        finalRateSwipeRefresh.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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
        historyPresenter.destroy();
        super.onDestroy();
    }
}
