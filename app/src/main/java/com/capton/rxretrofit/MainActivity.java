package com.capton.rxretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.capton.rxretrofit.demo.NewsBean;
import com.capton.rxretrofit.listener.HttpOnNextListener;
import com.capton.rxretrofit.demo.NewsApi;
import com.capton.rxretrofit.util.JsonUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.getData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });


    }

    private void getData(){
        NewsApi newsApi =new NewsApi(this, new HttpOnNextListener() {
            @Override
            public void onNext(String data, String method) {

                ((TextView)findViewById(R.id.showNews)).setText(data);

                // 自定义解析的方案
                if(method.equals(NewsApi.GET_NEWS)){
                    NewsBean newsBean = (NewsBean) JsonUtil.strToObject(data,NewsBean.class);
                    if("0".equals(newsBean.getStatus())){

                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
        newsApi.getNews("科技",0,10);
    }

}
