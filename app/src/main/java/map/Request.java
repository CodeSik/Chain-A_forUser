package map;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Request {
    public static RetrofitInterface getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-3-84-62-118.compute-1.amazonaws.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface service = retrofit.create(RetrofitInterface.class);

        return service;
    }
}