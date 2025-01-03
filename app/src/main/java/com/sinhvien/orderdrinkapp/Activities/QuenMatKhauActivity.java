package com.sinhvien.orderdrinkapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import java.util.Random;

import com.sinhvien.orderdrinkapp.DAO.NhanVienDAO;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.sinhvien.orderdrinkapp.R;
import com.sinhvien.orderdrinkapp.DAO.NhanVienDAO;
import androidx.appcompat.app.AppCompatActivity;

public class QuenMatKhauActivity extends AppCompatActivity {

    private EditText edtEmail;
    private Button btnGoiMail;
    private ProgressBar progressBar;
    private ImageView IMG_QMK_backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);

        edtEmail = findViewById(R.id.edtEmail);
        btnGoiMail = findViewById(R.id.btnGoiMail);
        IMG_QMK_backbtn = findViewById(R.id.img_QMK_back);

        progressBar = findViewById(R.id.progressBar);// Khởi tạo ProgressBar


        IMG_QMK_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        btnGoiMail.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            NhanVienDAO nhanVienDAO = new NhanVienDAO(this);
            if (nhanVienDAO.KiemTraEmail(email)) {
                // Sinh mã OTP
                String otp = generateOTP();

                // Gửi email với mã OTP
                String subject = "Mã OTP thay đổi mật khẩu";
                String messageBody = "Mã OTP của bạn là: " + otp;

                // Gửi email trong nền
                new JavaMailAPI(email, subject, messageBody).execute();

                // Lưu mã OTP vào SharedPreferences hoặc cơ sở dữ liệu tạm thời
                saveOTP(otp);
                progressBar.setVisibility(View.VISIBLE);

                // Chuyển đến màn hình nhập OTP và mật khẩu mới
                Intent intent = new Intent(QuenMatKhauActivity.this, NhapOTPActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Giả lập hàm sinh mã OTP
    private String generateOTP() {
        Random rand = new Random();
        int otp = rand.nextInt(999999); // Mã OTP ngẫu nhiên từ 0 đến 999999
        return String.format("%06d", otp);  // Đảm bảo OTP là 6 chữ số
    }

    // Lưu OTP vào SharedPreferences
    private void saveOTP(String otp) {
        getSharedPreferences("OTP", MODE_PRIVATE)
                .edit()
                .putString("otp", otp)
                .apply();
    }

}

