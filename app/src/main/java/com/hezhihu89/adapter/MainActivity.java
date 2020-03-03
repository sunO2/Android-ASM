package com.hezhihu89.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.hezhihu89.demo.DemoActivity;

import java.util.ArrayList;
import java.util.List;

@DemoClassInject
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName() + ": HEZHIHU89";

    @DemoInject
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_lay);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        List<Bean> beans = new ArrayList<>();
        for(int i=0;i<100;i++){
            beans.add(new Bean());
        }
        recyclerView.setAdapter(new Adapter(beans));

        Intent intent = new Intent(this, DemoActivity.class);
        startActivity(intent);

    }

}
