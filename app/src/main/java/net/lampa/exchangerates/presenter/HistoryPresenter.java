package net.lampa.exchangerates.presenter;

import net.lampa.exchangerates.interfaces.views.HistoryView;
import net.lampa.exchangerates.model.entities.HistoryResponseEntity;
import net.lampa.exchangerates.model.network.ApiRequestService;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static net.lampa.exchangerates.utils.ExchangeRatesUtils.getServerFormatFromDate;

public class HistoryPresenter extends BasePresenter<HistoryView> {

    @Inject
    ApiRequestService apiRequestService;

    private CompositeDisposable compositeDisposable;

    @Inject
    public HistoryPresenter() {
        this.compositeDisposable = new CompositeDisposable();
    }

    public void getHistory(String rateFromName, String rateToName, Date dateFrom, Date dateTo) {
        if (getView() == null) {
            return;
        }
        getView().showLoading();

        apiRequestService.getHistory(rateFromName, rateToName,
                getServerFormatFromDate(dateFrom),
                getServerFormatFromDate(dateTo))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<HistoryResponseEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<HistoryResponseEntity> historyResponseEntityResponse) {
                        if (getView() == null) {
                            return;
                        }

                        getView().hideLoading();

                        if (historyResponseEntityResponse == null) {
                            return;
                        }

                        if (historyResponseEntityResponse.isSuccessful() &&
                                historyResponseEntityResponse.body() != null) {

                            getView().showHistory(
                                    historyResponseEntityResponse.body()
                                            .getItemHistoryArray(rateToName));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideLoading();
                        }
                    }
                });
    }

    @Override
    public void cancel() {
        if (compositeDisposable != null && compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }


    @Override
    public void destroy() {
        cancel();
        setView(null);
    }
}
