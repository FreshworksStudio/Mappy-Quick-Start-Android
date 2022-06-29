package io.mappy.mappysdkimplementationexample.java.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import io.bemappy.sdk.models.Map;
import io.bemappy.sdk.models.Venue;
import io.bemappy.sdk.models.Scene;
import io.mappy.mappysdkimplementationexample.databinding.MapActivityBinding;

public class MapActivity extends AppCompatActivity {

    private MapActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MapActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        Venue venue = getIntent().getParcelableExtra("venue");

        Map map = new Map(venue);
        map.load(this, new Map.LoadListener() {
            @Override
            public void onSuccess(@NonNull Map map) {
                binding.mapView.setMap(map);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                launchToast(throwable);
            }
        });

        Scene scene = new Scene(venue);
        scene.load(this, new Scene.LoadListener() {
            @Override
            public void onSuccess(@NonNull Scene scene) {
                binding.sceneView.setScene(scene);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                launchToast(throwable);
            }
        });

        binding.buttonChangeView.setOnClickListener((view) -> {
            if(binding.mapView.getVisibility() == View.VISIBLE) {
                binding.mapView.setVisibility(View.GONE);
                binding.sceneView.setVisibility(View.VISIBLE);
                binding.buttonChangeView.setText("3D");
            } else {
                binding.sceneView.setVisibility(View.GONE);
                binding.mapView.setVisibility(View.VISIBLE);
                binding.buttonChangeView.setText("2D");
            }
        });

        binding.buttonDefaultRotation.setOnClickListener((view) -> {
            if(binding.mapView.getVisibility() == View.VISIBLE) {
                binding.mapView.rotateToDefault();
            } else {
                binding.sceneView.rotateToDefault();
            }
        });

        binding.buttonDefaultZoom.setOnClickListener((view) -> {
            if(binding.mapView.getVisibility() == View.VISIBLE) {
                binding.mapView.zoomToDefault();
            } else {
                binding.sceneView.zoomToDefault();
            }
        });
    }

    private void launchToast(Throwable throwable) {
        new Handler(Looper.getMainLooper()).post(() -> launchMessage(throwable.getMessage()));
    }

    private void launchMessage(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("ok", (dialogInterface, i) -> {})
                .create()
                .show();
    }
}
