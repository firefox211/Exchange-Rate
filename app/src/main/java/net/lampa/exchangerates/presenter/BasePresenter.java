package net.lampa.exchangerates.presenter;

public abstract class BasePresenter<T> {

    private T view;

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    protected abstract void cancel();

    protected abstract void destroy();

}