package com.example.retrofitfin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Adapter.onAdapterListener {

    private RecyclerView infoList;
    private List<Post> listPost;
    String[] data = {"Все", "Открытые", "Закрытые", "Занятые"};
    SharedPreferences sPref;
    Call<ResponseGen<List<Post>>> call;
    int savePos = 1;
    int savedText = 1;
    Spinner spinner;
    Button btnTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        btnTry = findViewById(R.id.btnTry);
        btnTry.setVisibility(View.INVISIBLE);

        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
            }
        });

        infoList = findViewById(R.id.rv_info);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        infoList.setLayoutManager(layoutManager);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        sPref = getPreferences(MODE_PRIVATE);
        savedText = sPref.getInt("Save", 1);
        spinner.setSelection(savedText);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://glabstore.blob.core.windows.net/test/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HolderApi jsonPlaceHolderApi = retrofit.create(HolderApi.class);
        call = jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<ResponseGen<List<Post>>>() {
            @Override
            public void onResponse(Call<ResponseGen<List<Post>>> call, Response<ResponseGen<List<Post>>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "GG", Toast.LENGTH_SHORT).show();
                    return;
                }
                infoList.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                btnTry.setVisibility(View.INVISIBLE);

                assert response.body() != null;

                listPost = response.body().getData();

                Collections.sort(listPost, new Comparator<Post>() {
                    @Override
                    public int compare(Post op1, Post op2) {
                        return (int) (op1.getActualTime() - op2.getActualTime());
                    }
                });
                Adapter adapter = new Adapter(listPost, MainActivity.this, savedText);
                infoList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseGen<List<Post>>> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.toast_title_fail)
                        .setMessage(R.string.toast_message_fail)
                        .setCancelable(true);
                AlertDialog alert = builder.create();
                alert.show();

                infoList.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                btnTry.setVisibility(View.VISIBLE);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(listPost==null)
                    return;
                infoList.getRecycledViewPool().clear();

                Adapter adapter = new Adapter(listPost, MainActivity.this, i);
                infoList.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                savePos = i;
                sPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("Save", i);
                ed.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onItemClick(int pos, List<Post> posts) {
        Post post = posts.get(pos);
        Intent intent = new Intent(this, Activity2.class);
        intent.putExtra("id", post.getId());
        startActivity(intent);
    }

}



