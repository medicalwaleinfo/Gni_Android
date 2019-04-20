package com.medicalwale.gniapp.WebAPI;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.LruCache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.medicalwale.gniapp.AppConfiguration;
import com.medicalwale.gniapp.Model.JsonResponseSignUp;
import com.medicalwale.gniapp.Model.StatusResponseModel;
import com.medicalwale.gniapp.Model.VerifyOTPResponse;
import com.medicalwale.gniapp.R;


import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiClientMain {

    private String baseUrl;
    private Context context;
    private NetworkAPI networkAPI;
    private OkHttpClient okHttpClient;
    private LruCache<Class<?>, Observable<?>> apiObservables;
    public static final String MEDIA_TYPE_STRING = "text/plain";
    private static InputStream rawRes;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static SSLContext getSSLConfig(Context context) throws CertificateException, IOException,
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        // Loading CAs from an InputStream
        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance("X.509");

        //InputStream source = context.getResources().openRawResource(R.raw.sandbox_ssl);

        rawRes = AppConfiguration.isLive ? context.getResources().openRawResource(R.raw.live_awsssl) : context.getResources().openRawResource(R.raw.sandbox_ssl);

        Certificate ca;
       /* // I'm using Java7. If you used Java6 close it manually with finally.
        try (InputStream cert = context.getResources().openRawResource(R.raw.sandbox_ssl)) {
            ca = cf.generateCertificate(cert);
        }*/


        try (InputStream cert = AppConfiguration.isLive ? context.getResources().openRawResource(R.raw.live_awsssl) : context.getResources().openRawResource(R.raw.sandbox_ssl)) {
            ca = cf.generateCertificate(cert);
        }

        // Creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext;
    }

    public ApiClientMain(String baseUrl, Context context, Boolean isCertificate) {
        this.baseUrl = baseUrl;
        this.context = context;
        okHttpClient = buildClient(isCertificate);

        // okHttpClient = buildClient(context);
        apiObservables = new LruCache<>(10);


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();


        networkAPI = retrofit.create(NetworkAPI.class);
    }

    /**
     * Method to return the API interface.
     *
     * @return
     */
    public NetworkAPI getApiClient() {
        return networkAPI;
    }

    private OkHttpClient buildClient(Boolean isCertificate) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && isCertificate) {
            try {
                builder = new OkHttpClient.Builder().sslSocketFactory(getSSLConfig(context).getSocketFactory());
//                 builder.sslSocketFactory(getSSLConfig(context).getSocketFactory());
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        } else {

        }

        builder.retryOnConnectionFailure(true);
        builder.connectTimeout(5 * 60, TimeUnit.SECONDS);
        builder.readTimeout(5 * 60, TimeUnit.SECONDS);
        builder.writeTimeout(5 * 60, TimeUnit.SECONDS);

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        });

        builder.addInterceptor(


                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //this is where we will add whatever we want to our request headers.
                        Request request = chain.request().newBuilder().addHeader("Accept", "application/json").build();
                        return chain.proceed(request);
                    }
                });
        /*HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);*/
        return builder.build();
    }

    public void clearCache() {
        apiObservables.evictAll();
    }

    public Observable<?> getPreparedObservable(Observable<?> unPreparedObservable, Class<?> clazz, boolean cacheObservable, boolean useCache) {

        Observable<?> preparedObservable = null;

        if (useCache)//this way we don't reset anything in the cache if this is the only instance of us not wanting to use it.
            preparedObservable = apiObservables.get(clazz);

        if (preparedObservable != null)
            return preparedObservable;

        //we are here because we have never created this observable before or we didn't want to use the cache...

        preparedObservable = unPreparedObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        if (cacheObservable) {
            preparedObservable = preparedObservable.cache();
            apiObservables.put(clazz, preparedObservable);
        }

        return preparedObservable;
    }


    /**
     * all the Service alls to use for the retrofit requests.
     */
    public interface NetworkAPI {


        @Headers({
                "Client-Service:frontend-client",
                "Auth-Key:medicalwalerestapi",
                "Content-Type:application/json",
                "User-ID:1"
        })

        @POST("userlogin")
        Call<VerifyOTPResponse> getResponseOTP(@Header("Authorizations") String token,
                                               @Body Map<String, String> body
        );

        @Headers({
                "Client-Service:frontend-client",
                "Auth-Key:medicalwalerestapi",
                "Content-Type:application/json",
                "User-ID:1"
        })

        @POST("language/update_language")
        Call<StatusResponseModel> updateLanguage(@Header("Authorizations") String token,
                                                 @Body Map<String, String> body
        );

        @POST
        Call<JsonResponseSignUp> getResponseSignUp(@Url String url, @Body RequestBody file);

        @Headers({
                "Client-Service:frontend-client",
                "Auth-Key:medicalwalerestapi",
                "Content-Type:application/json",
                "User-ID:1"
        })

        @POST("auth/userloginformail")
        Call<VerifyOTPResponse> getResponseEmailLogin(@Header("Authorizations") String token,
                                                        @Body Map<String, String> body);


    }

    public static RequestBody getStringRequestBody(String s) {
        return RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), s);
    }
}