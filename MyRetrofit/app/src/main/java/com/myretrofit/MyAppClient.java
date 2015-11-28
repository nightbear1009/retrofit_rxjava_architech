package com.myretrofit;

import android.util.Log;

import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by tedliang on 2015/11/28.
 */
public class MyAppClient {
    public interface ICallBack{
        void callBack(MyWrapper o);
    }


    public void getFilterAndOverTen(final ICallBack callBack){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SimpleService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        SimpleService.GitHub github = retrofit.create(SimpleService.GitHub.class);
//        SimpleService.GitHub github = new MainActivity.MyGithub();

        // Create a call instance for looking up Retrofit contributors.
        Observable<List<SimpleService.Contributor>> call = github.contributors("square", "retrofit");

        Observable<List<SimpleService.Contributor>> a = call.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<List<SimpleService.Contributor>, Observable<SimpleService.Contributor>>() {
                    @Override
                    public Observable<SimpleService.Contributor> call(List<SimpleService.Contributor> contributors) {
                        return Observable.from(contributors);
                    }
                })
                .filter(new Func1<SimpleService.Contributor, Boolean>() {
                    @Override
                    public Boolean call(SimpleService.Contributor contributor) {
                        return contributor.login.contains("JakeWharton");
                    }
                }).toList()
                .doOnNext(new Action1<List<SimpleService.Contributor>>() {
                    @Override
                    public void call(List<SimpleService.Contributor> contributors) {
                        Log.d("Ted", "size " + contributors.size());
                    }
                });

        Observable<List<SimpleService.Contributor>> b = call.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<List<SimpleService.Contributor>, Observable<SimpleService.Contributor>>() {
                    @Override
                    public Observable<SimpleService.Contributor> call(List<SimpleService.Contributor> contributors) {
                        return Observable.from(contributors);
                    }
                })
                .filter(new Func1<SimpleService.Contributor, Boolean>() {
                    @Override
                    public Boolean call(SimpleService.Contributor contributor) {
                        return contributor.contributions > 10;
                    }
                }).toList()
                .doOnNext(new Action1<List<SimpleService.Contributor>>() {
                    @Override
                    public void call(List<SimpleService.Contributor> contributors) {
                        Log.d("Ted", "contributations " + contributors.size());

                    }
                });

        Observable.zip(a, b, new Func2<List<SimpleService.Contributor>, List<SimpleService.Contributor>, MyWrapper>() {
            @Override
            public MyWrapper call(List<SimpleService.Contributor> contributors, List<SimpleService.Contributor> contributors2) {
                return new MyWrapper(contributors, contributors2);
            }
        }).subscribe(new Action1<MyWrapper>() {
            @Override
            public void call(MyWrapper o) {
                Log.d("TEd","go");
                callBack.callBack(o);
            }
        });
    }
}
