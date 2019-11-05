package com.example.ammaryasser.mydocsbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final LinearLayout posts_layout = findViewById(R.id.posts_layout);
        final EditText post_et = findViewById(R.id.post_et);
        final ImageButton post_btn = findViewById(R.id.post_btn);
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View doc = getLayoutInflater().inflate(R.layout.item_post, null);
                final TextView post_content = doc.findViewById(R.id.post_content_tv);
                post_content.setText("Fuck My Life");//post_et.getText().toString());
                posts_layout.addView(doc);
                post_et.setText("");
            }
        });
    }
}
