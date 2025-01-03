package com.sinhvien.orderdrinkapp.Activities;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.Dialog;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.sinhvien.orderdrinkapp.R;

public class FeedbackActivity extends AppCompatActivity {

    private ProgressBar progressBar; // Khai báo ProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        showReminderDialog();

        ImageView IMG_lienhe2_backbtn = findViewById(R.id.img_lienhe2_back);
        EditText edtFeedback = findViewById(R.id.edtFeedback);
        progressBar = findViewById(R.id.progressBar);// Khởi tạo ProgressBar
        showReminderDialog();

        IMG_lienhe2_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        findViewById(R.id.btnSendFeedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = edtFeedback.getText().toString().trim();
                if (feedback.isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, "Vui lòng nhập nội dung phản hồi", Toast.LENGTH_SHORT).show();
                } else {
                    sendFeedback(feedback);
                }
            }
        });
    }
    private void showReminderDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_reminder);
        dialog.setCancelable(false); // Không cho phép tắt bằng cách nhấn ngoài
        Button btnClose = dialog.findViewById(R.id.btnCloseDialog);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Đóng thông báo
            }
        });
        dialog.show();
    }

    private void sendFeedback(String feedback) {
        String email = "nghuyhieu3010@gmail.com"; // Cập nhật với email của bạn
        String subject = "Phản hồi từ ứng dụng";

        // Hiển thị ProgressBar khi gửi phản hồi
        progressBar.setVisibility(View.VISIBLE);

        // Gửi email không đồng bộ.
        JavaMailAPI javaMailAPI = new JavaMailAPI(email, subject, feedback) {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                // Ẩn ProgressBar sau khi gửi email xong
                progressBar.setVisibility(View.GONE);

                // Hiển thị thông báo kết quả
                Toast.makeText(FeedbackActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        };
        javaMailAPI.execute();
    }
}
