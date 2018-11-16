package net.lampa.exchangerates.presenter;

import net.lampa.exchangerates.interfaces.views.LatestView;
import net.lampa.exchangerates.managers.SharedPreferencesManager;
import net.lampa.exchangerates.model.entities.LatestRateResponseEntity;
import net.lampa.exchangerates.model.network.ApiRequestService;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class LatestPresenter extends BasePresenter<LatestView> {

    @Inject
    ApiRequestService apiRequestService;
    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    private CompositeDisposable compositeDisposable;

    @Inject
    public LatestPresenter() {
        this.compositeDisposable = new CompositeDisposable();
    }

    public void getLatestRates() {
        getLatestRates(sharedPreferencesManager.getBaseCurrency());
    }

    public void getLatestRates(String baseCurrency) {
        if (getView() == null) {
            return;
        }
        getView().showLoading();


        apiRequestService.getLatestRates(baseCurrency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<LatestRateResponseEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<LatestRateResponseEntity> latestRateResponseEntityResponse) {
                        if (getView() == null) {
                            return;
                        }

                        getView().hideLoading();

                        if (latestRateResponseEntityResponse == null) {
                            return;
                        }

                        if (latestRateResponseEntityResponse.isSuccessful() &&
                                latestRateResponseEntityResponse.body() != null) {
                            getView().showLatestRates(
                                    latestRateResponseEntityResponse.body().ratesToArrayList(),
                                    latestRateResponseEntityResponse.body().getDate(),
                                    latestRateResponseEntityResponse.body().getBase());
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
