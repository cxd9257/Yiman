package com.demo.yiman.book;

import com.demo.yiman.base.baseMVP.BaseObserver;
import com.demo.yiman.base.baseMVP.BasePresenter;

public class BookPresenter extends BasePresenter<BookView> {
    public BookPresenter(BookView baseView) {
        super(baseView);
    }

    public void book(int id){
        addDisposable(apiServer.bookByRx(id), new BaseObserver<BookModle>(baseView) {
            @Override
            protected void onSuccess(BookModle bookModle) {
                baseView.onBookSucc();
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }
}
