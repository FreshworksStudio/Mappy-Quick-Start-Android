package io.mappy.mappysdkimplementationexample.java.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import io.bemappy.sdk.models.Venue;
import io.bemappy.sdk.models.callbacks.CompletionCallback;
import io.bemappy.sdk.services.auth.Mappy;
import io.bemappy.sdk.services.venue.VenueService;
import io.mappy.mappysdkimplementationexample.R;
import io.mappy.mappysdkimplementationexample.databinding.MainJavaActivityBinding;
import io.mappy.mappysdkimplementationexample.java.adapter.VenuesAdapter;

public class VenuesActivity extends AppCompatActivity {

    private MainJavaActivityBinding binding;

    private final VenuesAdapter venuesAdapter = new VenuesAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainJavaActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Mappy mappy = Mappy.createInstance(this);
        VenueService venueService = VenueService.createInstance(this);

        initViews();
        initializeMappy(mappy, venueService);
    }

    private void initViews() {
        binding.recyclerViewVenue.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewVenue.setAdapter(venuesAdapter);
    }

    private void initializeMappy(Mappy mappy, VenueService venueService) {
        binding.progressBar.setVisibility(View.VISIBLE);
        mappy.initialize(getString(R.string.client_id), getString(R.string.client_secret), null, false,
                null, new CompletionCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        getVenues(venueService);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        launchError(throwable);
                    }
                });
    }

    private void getVenues(VenueService venueService) {
        venueService.getVenues(new CompletionCallback<List<Venue>>() {
            @Override
            public void onSuccess(List<Venue> result) {
                new Handler(Looper.getMainLooper()).post(() -> {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.recyclerViewVenue.setVisibility(View.VISIBLE);
                            venuesAdapter.load(result);
                        }
                );
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                launchError(throwable);
            }
        });
    }

    private void launchError(Throwable throwable) {
        new Handler(Looper.getMainLooper()).post(() -> launchMessage(throwable.getMessage()));
    }

    private void launchMessage(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("ok", (dialogInterface, i) -> {
                })
                .create()
                .show();
    }

}
