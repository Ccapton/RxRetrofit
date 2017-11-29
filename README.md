# RxRetrofit
这是一个retrofit2的封装库，结合rextrofit2、rxjava2、rxandroid，实现获取String类型原始网络数据的一个简单封装库

## 引入
#### 1 通过gradle
build.gradle(Project)
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
build.gradle(Module:app)
```
dependencies {
         compile 'com.github.Ccapton:EasyNaviBar:1.1.0'
   }
```
#### 2 下载项目后，直接把library工程引入到你的项目中，方便修改和优化（强烈建议）

## 如何使用

### 1.下面这个NewsApi是一个继承自BaseApi的演示子类api，大家可以参考这个例子写自己想要的Api。

```code

import android.content.Context;

import com.capton.rxretrofit.R;
import com.capton.rxretrofit.api.BaseApi;
import com.capton.rxretrofit.listener.HttpOnNextListener;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 *  这个接口用的是我的极速数据新闻api，演示api。
 *  如果到时请求不到数据了，应该是被前面的人把剩余的请求数消耗完了，请自行找接口测试吧
 *
 * Created by capton on 2017/11/29.
 */

public class NewsApi extends BaseApi {

    private NewsServer server;
    private String appkey;
    public final static String GET_NEWS = "get_news";  // 请求方法 ，每个接口类自由定义，

    /**
     * 演示api，获取新闻数据
     * @param context 这里的context是为了获取系统资源，如果不需要context，则你新建BaseApi子类时可以不传context.
     * @param onNextListener 监听回调
     */
    public NewsApi(Context context, HttpOnNextListener onNextListener) {
        super(onNextListener);
        appkey = context.getResources().getString(R.string.jisu_news_appkey);
        setBaseUrl("http://api.jisuapi.com/news/");        // 设置baseUrl
        server = getRetrofit().create(NewsServer.class);   //  获取Server实例
    }


    public void getNews(String channel,int start,int num){
        setMethod(GET_NEWS);   // 设置方法
        doHttpDeal(server.getNews(channel,start,num,appkey));  // 开始请求
    }

    private interface NewsServer{
        /**
         *  以包裹 String 类型的Observable<String>类型返回，则可以自由地选择解析框架、
         *  很直接地可以看到请求到的数据本体
         * @param channel
         * @param start
         * @param num
         * @param appkey
         * @return
         */
        // channel=头条&start=0&num=10&appkey=yourappkey
        @GET("get")
        Observable<String> getNews(@Query("channel") String channel, @Query("start") int start,
                                   @Query("num") int num, @Query("appkey") String appkey);
    }
}

```

### 2.在activity或者fragment中调用
```code
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
```
