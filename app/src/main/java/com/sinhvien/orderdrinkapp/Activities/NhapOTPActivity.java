package com.sinhvien.orderdrinkapp.Activities;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.sinhvien.orderdrinkapp.R;
import com.sinhvien.orderdrinkapp.DAO.NhanVienDAO;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NhapOTPActivity extends AppCompatActivity {

    private EditText edtOTP, edtMatKhauMoi;
    private Button btnXacNhan;
    private String email;
    private ImageView IMG_OTP_backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_otp);

        edtOTP = findViewById(R.id.edtOTP);
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        IMG_OTP_backbtn = findViewById(R.id.img_OTP_back);


        email = getIntent().getStringExtra("email");

        IMG_OTP_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });


        btnXacNhan.setOnClickListener(v -> {
            String otp = edtOTP.getText().toString();
            String newPassword = edtMatKhauMoi.getText().toString();

            if (otp.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã OTP và mật khẩu mới", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mã OTP
            if (isValidOTP(otp)) {
                NhanVienDAO nhanVienDAO = new NhanVienDAO(this);
                boolean success = nhanVienDAO.CapNhatMatKhau(email, newPassword);
                if (success) {
                    Toast.makeText(this, "Mật khẩu đã được cập nhật", Toast.LENGTH_SHORT).show();
                    // Quay lại màn hình đăng nhập
                    Intent intent = new Intent(NhapOTPActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Cập nhật mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Mã OTP không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Kiểm tra mã OTP từ SharedPreferences
    private boolean isValidOTP(String otp) {
        SharedPreferences preferences = getSharedPreferences("OTP", MODE_PRIVATE);
        String savedOTP = preferences.getString("otp", "");
        return otp.equals(savedOTP);
    }
}
