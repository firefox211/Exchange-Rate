package net.lampa.exchangerates.presenter;

import net.lampa.exchangerates.interfaces.views.DBView;
import net.lampa.exchangerates.model.entities.room.ConversionEntity;
import net.lampa.exchangerates.model.room.dao.ConversionDao;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DBPresenter extends BasePresenter<DBView> {


    @Inject
    ConversionDao conversionDao;

    private CompositeDisposable compositeDisposable;

    @Inject
    public DBPresenter() {
        this.compositeDisposable = new CompositeDisposable();
    }

    public void getConversionHistory() {
        if (getView() == null) {
            return;
        }
        getView().showLoading();

        conversionDao.getAllConversions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ConversionEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<ConversionEntity> conversions) {
                        if (getView() == null) {
                            return;
                        }
                        getView().hideLoading();

                        if (conversions != null) {
                            Collections.reverse(conversions);
                            getView().showConversionHistory(conversions);
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

    public void addConversion(ConversionEntity conversionEntity) {
        if (getView() == null) {
            return;
        }
        getView().showLoading();

        Completable.fromAction(() -> conversionDao.insertConversion(conversionEntity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        if (getView() == null) {
                            return;
                        }
                        getView().hideLoading();
                        getView().onConversionAdded();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideLoading();
                        }
                    }
                });
    }


    public void removeConversion(ConversionEntity conversionEntity) {
        if (getView() == null) {
            return;
        }
        getView().showLoading();

        Completable.fromAction(() -> conversionDao.deleteConversion(conversionEntity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        if (getView() == null) {
                            return;
                        }
                        getView().hideLoading();
                        getView().onConversionRemoved();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideLoading();
                        }
                    }
                });
    }


    public void clearConversionHistory() {
        if (getView() == null) {
            return;
        }
        getView().showLoading();

        Completable.fromAction(() -> conversionDao.clearConversionHistory())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        if (getView() == null) {
                            return;
                        }
                        getView().hideLoading();
                        getView().onHistoryCleared();
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
