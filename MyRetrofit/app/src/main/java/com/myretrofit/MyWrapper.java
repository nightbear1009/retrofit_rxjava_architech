package com.myretrofit;

import java.util.List;

/**
 * Created by tedliang on 2015/11/28.
 */
public class MyWrapper {
    private List<SimpleService.Contributor> a;
    private List<SimpleService.Contributor> b;
    public MyWrapper(List<SimpleService.Contributor> contributors, List<SimpleService.Contributor> contributors2) {
        a = contributors;
        b = contributors2;
    }
}
