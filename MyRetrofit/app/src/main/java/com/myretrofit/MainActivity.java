package com.myretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Path;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static class MyGithub implements SimpleService.GitHub{

        @Override
        public Observable<List<SimpleService.Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo) {
            Log.d("Ted","get");
            SimpleService.Contributor c = new SimpleService.Contributor("ted",1);
            List<SimpleService.Contributor> list = new ArrayList<>();
            list.add(c);
            return Observable.just(list);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SimpleService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
//        SimpleService.GitHub github = retrofit.create(SimpleService.GitHub.class);
        SimpleService.GitHub github = new MyGithub();

        // Create a call instance for looking up Retrofit contributors.
        Observable<List<SimpleService.Contributor>> call = github.contributors("square", "retrofit");

        Observable<List<SimpleService.Contributor>> a = call.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .doOnNext(new Action1<List<SimpleService.Contributor>>() {
                    @Override
                    public void call(List<SimpleService.Contributor> contributors) {
                        Log.d("Ted", "login " + contributors.get(0).login);

                    }
                });
        Observable<List<SimpleService.Contributor>> b = call.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .doOnNext(new Action1<List<SimpleService.Contributor>>() {
                    @Override
                    public void call(List<SimpleService.Contributor> contributors) {
                        Log.d("Ted", "contributations " + contributors.get(0).contributions);

                    }
                });

        Observable.zip(a, b, new Func2<List<SimpleService.Contributor>, List<SimpleService.Contributor>, Boolean>() {
            @Override
            public Boolean call(List<SimpleService.Contributor> contributors, List<SimpleService.Contributor> contributors2) {
                return true;
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean o) {
                Log.d("TEd","go");
            }
        });
    }

}
