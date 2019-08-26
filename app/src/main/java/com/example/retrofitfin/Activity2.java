package com.example.retrofitfin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity2 extends AppCompatActivity {

    TextView tvTitle;
    TextView tvTime;
    TextView tvLocation;
    TextView tvStatus;
    TextView tvDescription;
    TextView tvName;
    TextView tvLastName;
    Button btnStart;
    Button btnTry;
    AlertDialog alert;
    Call<ResponseGen<DataItem>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        tvTitle = findViewById(R.id.tvTitle);
        tvTime = findViewById(R.id.tvTime);
        tvLocation = findViewById(R.id.tvLocation);
        tvStatus = findViewById(R.id.tvStatus);
        tvDescription = findViewById(R.id.tvDescription);
        tvName = findViewById(R.id.tvName);
        tvLastName = findViewById(R.id.tvLastName);
        btnTry = findViewById(R.id.btnTry);

        btnTry.setVisibility(View.INVISIBLE);

        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callRetrofit();
            }
        });

        btnStart = findViewById(R.id.btnStart);

        btnStart.setVisibility(View.INVISIBLE);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity2.this);
                builder.setTitle(R.string.toast_title_fail)
                        .setMessage(R.string.toast_in_the_development)
                        .setCancelable(false)
                        .setNegativeButton(R.string.btn_fail_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                alert = builder.create();
                alert.show();

            }
        });

        init();
    }

    public void init(){
        int id = getIntent().getExtras().getInt("id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://glabstore.blob.core.windows.net/test/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HolderApi jsonPlaceHolderApi = retrofit.create(HolderApi.class);
        call = jsonPlaceHolderApi.getData(id);

        callRetrofit();
    }

    public void callRetrofit(){

        call.clone().enqueue(new Callback<ResponseGen<DataItem>>() {
            @Override
            public void onResponse(Call<ResponseGen<DataItem>> call, Response<ResponseGen<DataItem>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "GG", Toast.LENGTH_SHORT).show();
                    return;
                }

                btnTry.setVisibility(View.INVISIBLE);

                ResponseGen<DataItem> pst = response.body();
                if(pst.getStatus()) {
                    tvTitle.setText(pst.getData().getTitle());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
                    tvTime.setText(dateFormat.format(new Date(((pst.getData().getActualTime()*1000)))));

                    tvLocation.setText(pst.getData().getLocation());

                    switch (pst.getData().getStatus()){
                        case "open":
                            tvStatus.setText(R.string.text_status_open);
                            btnStart.setVisibility(View.VISIBLE);
                            break;
                        case "closed":
                            tvStatus.setText(R.string.text_status_closed);
                            tvName.setText(getString(R.string.text_name) + " " + pst.getData().getSpecialist().getFirstName());
                            tvLastName.setText(getString(R.string.text_second_name) + " " + pst.getData().getSpecialist().getLastName());
                            break;
                        case "in_progress":
                            tvStatus.setText(R.string.text_status_in_progress);
                            tvName.setText(getString(R.string.text_name) + " " + pst.getData().getSpecialist().getFirstName());
                            tvLastName.setText(getString(R.string.text_second_name) + " " + pst.getData().getSpecialist().getLastName());
                            break;
                    }
                    tvDescription.setText(pst.getData().getDescription());
                }
                else{
                    tvTitle.setText(pst.getError());
                }
            }

            @Override
            public void onFailure(Call<ResponseGen<DataItem>> call, Throwable t) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity2.this);
                builder.setTitle(R.string.toast_title_fail)
                        .setMessage(R.string.toast_message_fail)
                        .setCancelable(true);
                AlertDialog alert = builder.create();
                alert.show();

                btnTry.setVisibility(View.VISIBLE);
            }
        });
    }

}
