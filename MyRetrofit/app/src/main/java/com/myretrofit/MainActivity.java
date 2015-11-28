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
        MyAppClient apiclient = new MyAppClient();
        apiclient.getFilterAndOverTen(new MyAppClient.ICallBack() {
            @Override
            public void callBack(MyWrapper o) {
                //use MyWrapper to updateUI;
            }
        });
    }

}
