package generatebarcode.com.testappliction.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import generatebarcode.com.testappliction.network.api_request.ApiRequest;
import generatebarcode.com.testappliction.network.api_response.ApiResponse;
import generatebarcode.com.testappliction.utils.Cv;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static generatebarcode.com.testappliction.fragments.FirstFragment.mProgressDialog;

/**
 * Created by punamchand on 18-Oct-18.
 */

public class ApiCallServices extends IntentService{
    private Api api;

    public ApiCallServices() {
        super("ApiCallServices");
    }

    public static void action(Context context, String action) {
        Intent intent = new Intent(context, ApiCallServices.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        api = ApiClient.getClient();
        if (intent != null) {
            final String action = intent.getAction();
            if (Cv.ACTION_IMAGE_SEND.equals(action)) {
                handleImageSend();
            }
        }
    }

    private void handleImageSend() {
        api.sendImage(new ApiRequest()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> r) {
                if (r.isSuccessful()) {
                    ApiResponse body = r.body();
                    if (body.getStatus()==200){
                        Toast.makeText(ApiCallServices.this, "Image send", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ApiCallServices.this,body.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                   // EventBus.getDefault().post(body);
                } else {
                    Toast.makeText(ApiCallServices.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                   // EventBus.getDefault().post(Cv.TIMEOUT);
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                try {
                    Toast.makeText(ApiCallServices.this, "onFailure try", Toast.LENGTH_SHORT).show();
                  //  EventBus.getDefault().post(t.getMessage());
                } catch (Exception ex) {
                    Toast.makeText(ApiCallServices.this, "onFailure catch", Toast.LENGTH_SHORT).show();
                 //   EventBus.getDefault().post(Cv.TIMEOUT);
                }
            }
        });
    }
}
