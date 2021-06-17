package com.example.ammaryasser.mydocsbox.ui.main.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ammaryasser.mydocsbox.R;
import com.example.ammaryasser.mydocsbox.databinding.ActivityProfileBinding;
import com.example.ammaryasser.mydocsbox.databinding.PostBinding;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        binding.postBtn.setOnClickListener(v -> {
            final PostBinding itemBinding = PostBinding.inflate(getLayoutInflater());

            itemBinding.postOwner.setText(getString(R.string.ammar_yasser));
            itemBinding.postContentTv.setText(binding.postEt.getText().toString());

            binding.postsLayout.addView(itemBinding.getRoot(), 0);
            binding.postEt.setText("");
        });
    }

}
