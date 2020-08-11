package com.guozongkui.testimsdk.net;

import android.content.Context;
import android.content.SharedPreferences;

import com.guozongkui.testimsdk.common.NetConstant;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class RetrofitHelper {


    private static RetrofitHelper instance;
    private Context mContext;
    private Retrofit retrofit;

    private RetrofitHelper() {

    }

    public static RetrofitHelper getInstance() {
        if (instance == null) {
            instance = new RetrofitHelper();
        }

        return instance;
    }

    public Retrofit getRetrofit(Context context){

        mContext = context;

        retrofit = new Retrofit.Builder()
                .client(getUnsafeOkHttpClient())
                .baseUrl("http://api.sealtalk.im")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    private OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                    .addInterceptor(new RetrofitHelper.AddHeaderInterceptor(mContext))
                    .addInterceptor(new RetrofitHelper.ReceivedCookiesInterceptor(mContext))
                    .connectTimeout(NetConstant.API_CONNECT_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(NetConstant.API_READ_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(NetConstant.API_WRITE_TIME_OUT, TimeUnit.SECONDS);
            okHttpBuilder.sslSocketFactory(sslSocketFactory);
            okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * 接受cookie拦截器
     */
    public class ReceivedCookiesInterceptor implements Interceptor {
        private Context mContext;

        public ReceivedCookiesInterceptor(Context context) {
            mContext = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookiesSet = new HashSet<>(originalResponse.headers("Set-Cookie"));

                SharedPreferences.Editor config = mContext.getSharedPreferences(NetConstant.API_SP_NAME_NET, MODE_PRIVATE)
                        .edit();
                config.putStringSet(NetConstant.API_SP_KEY_NET_COOKIE_SET, cookiesSet);
                config.apply();
            }

            return originalResponse;
        }
    }

    /**
     * 添加header包含cookie拦截器
     */
    public class AddHeaderInterceptor implements Interceptor {
        private Context mContext;

        public AddHeaderInterceptor(Context context) {
            mContext = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            SharedPreferences preferences = mContext.getSharedPreferences(NetConstant.API_SP_NAME_NET,
                    Context.MODE_PRIVATE);

            //添加cookie
            HashSet<String> cookieSet = (HashSet<String>) preferences.getStringSet(NetConstant.API_SP_KEY_NET_COOKIE_SET, null);
            if (cookieSet != null) {
                for (String cookie : cookieSet) {
                    builder.addHeader("Cookie", cookie);
                }
            }

            //添加用户登录认证
            String auth = preferences.getString(NetConstant.API_SP_KEY_NET_HEADER_AUTH, null);
            if (auth != null) {
                builder.addHeader("Authorization", auth);
            }

            return chain.proceed(builder.build());
        }
    }




}
