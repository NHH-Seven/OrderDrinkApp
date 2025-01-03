package com.sinhvien.orderdrinkapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputLayout;
import com.sinhvien.orderdrinkapp.DAO.NhanVienDAO;

import java.util.regex.Pattern;

import static com.sinhvien.orderdrinkapp.R.*;

public class RegisterActivity extends AppCompatActivity {

    ImageView IMG_signup_back;
    Button BTN_signup_next;
    TextView TXT_signup_title;
    CheckBox termsCheckBox,newsletterCheckBox,priCheckBox;
    TextInputLayout TXTL_signup_HoVaTen, TXTL_signup_TenDN, TXTL_signup_Email, TXTL_signup_SDT, TXTL_signup_MatKhau;
    public static final String BUNDLE = "BUNDLE";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,}" +                // at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.register_layout);

        //region lấy đối tượng view
        IMG_signup_back = (ImageView)findViewById(id.img_signup_back);
        BTN_signup_next = (Button)findViewById(id.btn_signup_next);
        TXT_signup_title = (TextView)findViewById(id.txt_signup_title);
        TXTL_signup_HoVaTen = (TextInputLayout)findViewById(id.txtl_signup_HoVaTen);
        TXTL_signup_TenDN = (TextInputLayout)findViewById(id.txtl_signup_TenDN);
        TXTL_signup_Email = (TextInputLayout)findViewById(id.txtl_signup_Email);
        TXTL_signup_SDT = (TextInputLayout)findViewById(id.txtl_signup_SDT);
        TXTL_signup_MatKhau = (TextInputLayout)findViewById(id.txtl_signup_MatKhau);
        termsCheckBox = findViewById(id.checkbox_terms);
        newsletterCheckBox = findViewById(id.checkbox_privacy_policy);
        priCheckBox = findViewById(id.checkbox_private);
        //endregion

        BTN_signup_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiểm tra validate false => phải thỏa đk validate
                if(!validateFullName() | !validateUserName() | !validateEmail() | !validatePhone() | !validatePassWord()){
                    return;
                }
                String hoTen = TXTL_signup_HoVaTen.getEditText().getText().toString();
                String tenDN = TXTL_signup_TenDN.getEditText().getText().toString();
                String eMail = TXTL_signup_Email.getEditText().getText().toString();
                String sDT = TXTL_signup_SDT.getEditText().getText().toString();
                String matKhau = TXTL_signup_MatKhau.getEditText().getText().toString();

                byBundleNextSignupScreen(hoTen,tenDN,eMail,sDT,matKhau);
            }
        });
        priCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priCheckBox.isChecked()) {
                    // Hiển thị dialog khi checkbox được tích
                    showPrivatePolicy();
                }
            }
        });

// Hiển thị chính sách khi nhấn vào checkbox "Điều khoản sử dụng"
        termsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termsCheckBox.isChecked()) {
                    // Hiển thị dialog khi checkbox được tích
                    showTermsPolicy();
                }
            }
        });

// Hiển thị chính sách khi nhấn vào checkbox "Nhận bản tin"
        newsletterCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newsletterCheckBox.isChecked()) {
                    // Hiển thị dialog khi checkbox được tích
                    showPrivacyPolicy();
                }
            }
        });

    }

    //Hàm quay về màn hình trước
    public void backFromRegister(View view){

        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(id.layoutRegister),"transition_signup");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);
            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }


    //truyền dữ liệu qua trang đk thứ 2 bằng bundle
    public void byBundleNextSignupScreen(String hoTen, String tenDN, String eMail, String sDT, String matKhau){

        Intent intent = new Intent(getApplicationContext(),Register2ndActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("hoten",hoTen);
        bundle.putString("tendn",tenDN);
        bundle.putString("email",eMail);
        bundle.putString("sdt",sDT);
        bundle.putString("matkhau",matKhau);
        intent.putExtra(BUNDLE,bundle);

        if (!termsCheckBox.isChecked() || !newsletterCheckBox.isChecked() || !priCheckBox.isChecked()) {
            Toast.makeText(this, "Bạn phải đồng ý với tất cả các điều khoản", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Xác Nhận Thông Tin Đăng Ký")
                .setMessage("Bạn có chắc chắn muốn đăng ký với thông tin đã cung cấp không?")
                .setPositiveButton("Đồng Ý", (dialog, which) -> startActivity(intent))
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();




        overridePendingTransition(anim.slide_in_right, anim.slide_out_left);
    }

    //region Validate field
    private boolean validateFullName(){
        String val = TXTL_signup_HoVaTen.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_signup_HoVaTen.setError(getResources().getString(string.not_empty));
            return false;
        }else {
            TXTL_signup_HoVaTen.setError(null);
            TXTL_signup_HoVaTen.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName(){
        NhanVienDAO nhanVienDAO = new NhanVienDAO(this); // this là context
        String val = TXTL_signup_TenDN.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,50}\\z";

        if(val.isEmpty()){
            TXTL_signup_TenDN.setError(getResources().getString(string.not_empty));
            return false;
        }else if(val.length()>50){
            TXTL_signup_TenDN.setError("Phải nhỏ hơn 50 ký tự");
            return false;
        }else if(!val.matches(checkspaces)){
            TXTL_signup_TenDN.setError("Không được cách chữ!");
            return false;
        }
        else if (nhanVienDAO.KiemTratk(val)) {
            TXTL_signup_TenDN.setError("Tên tài khoản đã tồn tại!");
            return false;
        }
        else {
            TXTL_signup_TenDN.setError(null);
            TXTL_signup_TenDN.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(){
        NhanVienDAO nhanVienDAO = new NhanVienDAO(this); // this là context
        String val = TXTL_signup_Email.getEditText().getText().toString().trim();
        String checkspaces = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if(val.isEmpty()){
            TXTL_signup_Email.setError(getResources().getString(string.not_empty));
            return false;
        }else if(!val.matches(checkspaces)){
            TXTL_signup_Email.setError("Email không hợp lệ!");
            return false;
        }
        else if (nhanVienDAO.KiemTraEmail(val)) {
            TXTL_signup_Email.setError("Email đã tồn tại!");
            return false;
        }
        else {
            TXTL_signup_Email.setError(null);
            TXTL_signup_Email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhone(){
        NhanVienDAO nhanVienDAO = new NhanVienDAO(this); // this là context
        String val = TXTL_signup_SDT.getEditText().getText().toString().trim();


        if(val.isEmpty()){
            TXTL_signup_SDT.setError(getResources().getString(string.not_empty));
            return false;
        }else if(val.length() != 10){
            TXTL_signup_SDT.setError("Số điện thoại không hợp lệ!");
            return false;
        }
        else if (nhanVienDAO.KiemTraPhone(val)) {
            TXTL_signup_SDT.setError("Phone đã tồn tại!");
            return false;
        }
        else {
            TXTL_signup_SDT.setError(null);
            TXTL_signup_SDT.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassWord(){
        String val = TXTL_signup_MatKhau.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_signup_MatKhau.setError(getResources().getString(string.not_empty));
            return false;
        }else if(!PASSWORD_PATTERN.matcher(val).matches()){
            TXTL_signup_MatKhau.setError("Mật khẩu ít nhất 6 ký tự!");
            return false;
        }
        else {
            TXTL_signup_MatKhau.setError(null);
            TXTL_signup_MatKhau.setErrorEnabled(false);
            return true;
        }
    }
    private void showTermsPolicy() {
        new AlertDialog.Builder(this)
                .setTitle("Điều khoản sử dụng")
                .setMessage("Điều khoản và điều kiện\n\n" +
                        "Cập nhật lần cuối: 18 tháng 11 năm 2024\n" +
                        "Vui lòng đọc kỹ các điều khoản và điều kiện này trước khi sử dụng Dịch vụ của chúng tôi.\n\n" +
                        "Giải thích và định nghĩa\n" +
                        "Diễn giải\n" +
                        "Các từ có chữ cái đầu viết hoa có nghĩa được định nghĩa theo các điều kiện sau. Các định nghĩa sau đây sẽ có cùng nghĩa bất kể chúng xuất hiện ở dạng số ít hay số nhiều.\n\n" +
                        "Định nghĩa\n" +
                        "Cho mục đích của các Điều khoản và Điều kiện này:\n" +
                        "Ứng dụng có nghĩa là chương trình phần mềm do Công ty cung cấp được Bạn tải xuống trên bất kỳ thiết bị điện tử nào có tên là Ứng Dụng Quản Lý Coffee\n" +
                        "Cửa hàng ứng dụng có nghĩa là dịch vụ phân phối kỹ thuật số do Apple Inc. (Apple App Store) hoặc Google Inc. (Google Play Store) vận hành và phát triển, trong đó Ứng dụng đã được tải xuống.\n" +
                        "Công ty liên kết có nghĩa là một thực thể kiểm soát, được kiểm soát bởi hoặc chịu sự kiểm soát chung với một bên, trong đó \"kiểm soát\" có nghĩa là sở hữu 50% hoặc nhiều hơn số cổ phiếu, quyền sở hữu vốn chủ sở hữu hoặc chứng khoán khác có quyền biểu quyết bầu giám đốc hoặc cơ quan quản lý khác.\n" +
                        "Quốc gia đề cập đến: Việt Nam\n" +
                        "Công ty (được gọi là \"Công ty\", \"Chúng tôi\", \"Chúng tôi\" hoặc \"Của chúng tôi\" trong Thỏa thuận này) đề cập đến Ứng Dụng Quản Lý Quán Coffee.\n" +
                        "Thiết bị có nghĩa là bất kỳ thiết bị nào có thể truy cập Dịch vụ như máy tính, điện thoại di động hoặc máy tính bảng kỹ thuật số.\n" +
                        "Dịch vụ đề cập đến Ứng dụng.\n" +
                        "Điều khoản và Điều kiện (còn được gọi là \"Điều khoản\") có nghĩa là các Điều khoản và Điều kiện này tạo thành toàn bộ thỏa thuận giữa Bạn và Công ty liên quan đến việc sử dụng Dịch vụ. Thỏa thuận Điều khoản và Điều kiện này được tạo ra với sự trợ giúp của Trình tạo Điều khoản và Điều kiện.\n" +
                        "Dịch vụ truyền thông xã hội của bên thứ ba có nghĩa là bất kỳ dịch vụ hoặc nội dung nào (bao gồm dữ liệu, thông tin, sản phẩm hoặc dịch vụ) do bên thứ ba cung cấp có thể được hiển thị, bao gồm hoặc cung cấp thông qua Dịch vụ.\n" +
                        "Bạn có nghĩa là cá nhân truy cập hoặc sử dụng Dịch vụ, hoặc công ty hoặc pháp nhân khác mà cá nhân đó thay mặt truy cập hoặc sử dụng Dịch vụ, tùy theo trường hợp.\n\n" +
                        "Sự thừa nhận\n" +
                        "Đây là các Điều khoản và Điều kiện chi phối việc sử dụng Dịch vụ này và thỏa thuận có hiệu lực giữa Bạn và Công ty. Các Điều khoản và Điều kiện này nêu rõ các quyền và nghĩa vụ của tất cả người dùng liên quan đến việc sử dụng Dịch vụ.\n" +
                        "Việc bạn truy cập và sử dụng Dịch vụ có điều kiện là bạn chấp nhận và tuân thủ các Điều khoản và Điều kiện này. Các Điều khoản và Điều kiện này áp dụng cho tất cả khách truy cập, người dùng và những người khác truy cập hoặc sử dụng Dịch vụ.\n" +
                        "Bằng cách truy cập hoặc sử dụng Dịch vụ, Bạn đồng ý bị ràng buộc bởi các Điều khoản và Điều kiện này. Nếu Bạn không đồng ý với bất kỳ phần nào của các Điều khoản và Điều kiện này, Bạn không được phép truy cập Dịch vụ.\n" +
                        "Bạn cam kết rằng bạn đã trên 18 tuổi. Công ty không cho phép những người dưới 18 tuổi sử dụng Dịch vụ.\n" +
                        "Việc bạn truy cập và sử dụng Dịch vụ cũng phụ thuộc vào việc bạn chấp nhận và tuân thủ Chính sách bảo mật của Công ty. Chính sách bảo mật của chúng tôi mô tả các chính sách và thủ tục của Chúng tôi về việc thu thập, sử dụng và tiết lộ thông tin cá nhân của Bạn khi Bạn sử dụng Ứng dụng hoặc Trang web và cho Bạn biết về quyền riêng tư của Bạn và luật pháp bảo vệ Bạn như thế nào. Vui lòng đọc kỹ Chính sách bảo mật của Chúng tôi trước khi sử dụng Dịch vụ của Chúng tôi.\n\n" +
                        "Chính sách Tạo Tài Khoản\n" +
                        "Tạo tài khoản\n" +
                        "Để sử dụng đầy đủ các tính năng của Dịch vụ, bạn cần tạo tài khoản. Bạn có thể đăng ký trực tiếp bằng email.\n" +
                        "Thông tin tài khoản\n" +
                        "Bạn cam kết rằng thông tin cung cấp trong quá trình tạo tài khoản là chính xác, đầy đủ và cập nhật. Bạn có trách nhiệm duy trì bảo mật tài khoản của mình, bao gồm tên đăng nhập và mật khẩu.\n" +
                        "Trách nhiệm của người dùng\n" +
                        "Bạn phải chịu trách nhiệm hoàn toàn về tất cả các hoạt động dưới tài khoản của mình, bao gồm cả việc sử dụng trái phép nếu xảy ra do lỗ hổng bảo mật hoặc bất kỳ hành vi gian lận nào.\n" +
                        "Khôi phục tài khoản\n" +
                        "Nếu bạn quên mật khẩu hoặc gặp vấn đề về tài khoản, bạn có thể yêu cầu khôi phục mật khẩu thông qua tính năng quên mật khẩu trong ứng dụng. Chúng tôi sẽ gửi hướng dẫn khôi phục qua email hoặc SMS, tùy thuộc vào thông tin bạn cung cấp khi đăng ký.\n" +
                        "Chấm dứt tài khoản\n" +
                        "Bạn có quyền yêu cầu xóa tài khoản của mình bất kỳ lúc nào. Để thực hiện việc này, vui lòng liên hệ với chúng tôi qua email hoặc qua cài đặt trong ứng dụng. Chúng tôi sẽ xử lý yêu cầu của bạn trong thời gian sớm nhất.\n" +
                        "Hạn chế quyền truy cập\n" +
                        "Công ty có quyền đình chỉ hoặc hủy bỏ tài khoản của bạn nếu chúng tôi phát hiện việc sử dụng trái phép hoặc vi phạm các Điều khoản và Điều kiện này.\n" +
                        "Bảo mật tài khoản\n" +
                        "Chúng tôi cam kết bảo mật thông tin cá nhân của bạn theo Chính sách bảo mật của chúng tôi. Tuy nhiên, bạn cần thực hiện các biện pháp cần thiết để bảo vệ mật khẩu và không chia sẻ thông tin tài khoản của mình với bất kỳ ai.\n\n" +
                        "Liên kết đến các trang web khác\n" +
                        "Dịch vụ của chúng tôi có thể chứa các liên kết đến các trang web hoặc dịch vụ của bên thứ ba không thuộc quyền sở hữu hoặc kiểm soát của Công ty.\n" +
                        "Công ty không kiểm soát và không chịu trách nhiệm về nội dung, chính sách bảo mật hoặc hoạt động của bất kỳ trang web hoặc dịch vụ của bên thứ ba nào. Bạn cũng thừa nhận và đồng ý rằng Công ty sẽ không chịu trách nhiệm hoặc nghĩa vụ, trực tiếp hoặc gián tiếp, đối với bất kỳ thiệt hại hoặc mất mát nào do hoặc được cho là do hoặc liên quan đến việc sử dụng hoặc dựa vào bất kỳ nội dung, hàng hóa hoặc dịch vụ nào có sẵn trên hoặc thông qua bất kỳ trang web hoặc dịch vụ nào như vậy.\n" +
                        "Chúng tôi đặc biệt khuyên bạn nên đọc kỹ các điều khoản và điều kiện cũng như chính sách bảo mật của bất kỳ trang web hoặc dịch vụ của bên thứ ba nào mà bạn truy cập.\n\n" +
                        "Chấm dứt\n" +
                        "Chúng tôi có thể chấm dứt hoặc đình chỉ quyền truy cập của bạn ngay lập tức, mà không cần thông báo trước hoặc chịu trách nhiệm pháp lý, vì bất kỳ lý do gì, bao gồm nhưng không giới hạn trong trường hợp bạn vi phạm các Điều khoản và Điều kiện này.\n" +
                        "Sau khi chấm dứt, quyền sử dụng Dịch vụ của bạn sẽ chấm dứt ngay lập tức."+"Giới hạn trách nhiệm\n" +
                        "Bất chấp mọi thiệt hại mà Bạn có thể phải chịu, toàn bộ trách nhiệm pháp lý của Công ty và bất kỳ nhà cung cấp nào của Công ty theo bất kỳ điều khoản nào của Điều khoản này và biện pháp khắc phục độc quyền của Bạn đối với tất cả những điều nêu trên sẽ bị giới hạn ở số tiền Bạn thực sự đã thanh toán thông qua Dịch vụ hoặc 100 USD nếu Bạn chưa mua bất kỳ thứ gì thông qua Dịch vụ.\n" +
                        "Ở phạm vi tối đa được luật hiện hành cho phép, trong mọi trường hợp, Công ty hoặc các nhà cung cấp của Công ty sẽ không chịu trách nhiệm đối với bất kỳ thiệt hại đặc biệt, ngẫu nhiên, gián tiếp hoặc thiệt hại do hậu quả nào (bao gồm nhưng không giới hạn ở thiệt hại do mất lợi nhuận, mất dữ liệu hoặc thông tin khác, do gián đoạn kinh doanh, do thương tích cá nhân, mất quyền riêng tư phát sinh từ hoặc theo bất kỳ cách nào liên quan đến việc sử dụng hoặc không thể sử dụng Dịch vụ, phần mềm của bên thứ ba và/hoặc phần cứng của bên thứ ba được sử dụng với Dịch vụ hoặc liên quan đến bất kỳ điều khoản nào của Điều khoản này), ngay cả khi Công ty hoặc bất kỳ nhà cung cấp nào đã được thông báo về khả năng xảy ra những thiệt hại đó và ngay cả khi biện pháp khắc phục không đạt được mục đích cơ bản của nó.\n" +
                        "Một số tiểu bang không cho phép loại trừ các bảo hành ngụ ý hoặc giới hạn trách nhiệm đối với thiệt hại ngẫu nhiên hoặc do hậu quả, điều này có nghĩa là một số giới hạn trên có thể không áp dụng. Ở các tiểu bang này, trách nhiệm của mỗi bên sẽ bị giới hạn ở mức độ lớn nhất mà luật pháp cho phép.\n\n" +
                        "Tuyên bố miễn trừ trách nhiệm \"NGUYÊN TRẠNG\" và \"CÓ SẴN\"\n" +
                        "Dịch vụ được cung cấp cho Bạn \"NGUYÊN TRẠNG\" và \"THEO KHẢ NĂNG CÓ SẴN\" và với mọi lỗi và khuyết điểm mà không có bất kỳ bảo hành nào. Ở mức độ tối đa được pháp luật hiện hành cho phép, Công ty, thay mặt cho chính mình và thay mặt cho các Chi nhánh của mình và các bên cấp phép và nhà cung cấp dịch vụ tương ứng của mình và của họ, từ chối rõ ràng mọi bảo hành, dù là rõ ràng, ngụ ý, theo luật định hay cách khác, liên quan đến Dịch vụ, bao gồm mọi bảo hành ngụ ý về khả năng bán được, tính phù hợp cho một mục đích cụ thể, quyền sở hữu và không vi phạm, và các bảo hành có thể phát sinh trong quá trình giao dịch, quá trình thực hiện, sử dụng hoặc hoạt động thương mại. Không giới hạn ở những điều nêu trên, Công ty không cung cấp bất kỳ bảo hành hoặc cam kết nào và không đưa ra bất kỳ tuyên bố nào rằng Dịch vụ sẽ đáp ứng các yêu cầu của Bạn, đạt được bất kỳ kết quả mong muốn nào, tương thích hoặc hoạt động với bất kỳ phần mềm, ứng dụng, hệ thống hoặc dịch vụ nào khác, hoạt động mà không bị gián đoạn, đáp ứng bất kỳ tiêu chuẩn hiệu suất hoặc độ tin cậy nào hoặc không có lỗi hoặc rằng bất kỳ lỗi hoặc khuyết điểm nào có thể hoặc sẽ được sửa chữa.\n" +
                        "Không giới hạn những điều nêu trên, Công ty hoặc bất kỳ nhà cung cấp nào của công ty đều không đưa ra bất kỳ tuyên bố hoặc bảo đảm nào, dù là rõ ràng hay ngụ ý: (i) về hoạt động hoặc tính khả dụng của Dịch vụ, hoặc thông tin, nội dung và tài liệu hoặc sản phẩm có trong đó; (ii) rằng Dịch vụ sẽ không bị gián đoạn hoặc không có lỗi; (iii) về tính chính xác, độ tin cậy hoặc tính cập nhật của bất kỳ thông tin hoặc nội dung nào được cung cấp thông qua Dịch vụ; hoặc (iv) rằng Dịch vụ, máy chủ của Dịch vụ, nội dung hoặc email được gửi từ hoặc thay mặt cho Công ty không có vi-rút, tập lệnh, ngựa thành Troy, sâu, phần mềm độc hại, bom hẹn giờ hoặc các thành phần có hại khác.\n" +
                        "Một số khu vực pháp lý không cho phép loại trừ một số loại bảo hành hoặc hạn chế nhất định đối với các quyền theo luật định hiện hành của người tiêu dùng, do đó một số hoặc tất cả các loại trừ và hạn chế nêu trên có thể không áp dụng cho Bạn. Nhưng trong trường hợp như vậy, các loại trừ và hạn chế nêu trong phần này sẽ được áp dụng ở mức độ lớn nhất có thể thực thi theo luật hiện hành.\n\n" +
                        "Luật điều chỉnh\n" +
                        "Luật pháp của Quốc gia, trừ các quy tắc xung đột luật pháp, sẽ chi phối các Điều khoản này và việc Bạn sử dụng Dịch vụ. Việc Bạn sử dụng Ứng dụng cũng có thể phải tuân theo các luật pháp địa phương, tiểu bang, quốc gia hoặc quốc tế khác.\n\n" +
                        "Giải quyết tranh chấp\n" +
                        "Nếu Bạn có bất kỳ thắc mắc hoặc tranh chấp nào về Dịch vụ, trước tiên Bạn đồng ý cố gắng giải quyết tranh chấp một cách không chính thức bằng cách liên hệ với Công ty.\n\n" +
                        "Dành cho người dùng Liên minh Châu Âu (EU)\n" +
                        "Nếu bạn là người tiêu dùng Liên minh Châu Âu, bạn sẽ được hưởng lợi từ mọi điều khoản bắt buộc của luật pháp tại quốc gia nơi bạn cư trú.\n\n" +
                        "Tuân thủ pháp luật Hoa Kỳ\n" +
                        "Bạn tuyên bố và bảo đảm rằng (i) Bạn không ở tại quốc gia chịu lệnh cấm vận của chính phủ Hoa Kỳ hoặc bị chính phủ Hoa Kỳ chỉ định là quốc gia \"hỗ trợ khủng bố\" và (ii) Bạn không có tên trong bất kỳ danh sách các bên bị cấm hoặc hạn chế nào của chính phủ Hoa Kỳ.\n\n" +
                        "Khả năng tách rời và từ bỏ\n" +
                        "Khả năng tách rời\n" +
                        "Nếu bất kỳ điều khoản nào trong các Điều khoản này bị coi là không thể thi hành hoặc không hợp lệ, điều khoản đó sẽ được thay đổi và diễn giải để đạt được mục tiêu của điều khoản đó ở mức độ lớn nhất có thể theo luật hiện hành và các điều khoản còn lại sẽ tiếp tục có hiệu lực đầy đủ.\n\n" +
                        "Miễn trừ\n" +
                        "Ngoại trừ những quy định có tại đây, việc không thực hiện quyền hoặc không yêu cầu thực hiện nghĩa vụ theo các Điều khoản này sẽ không ảnh hưởng đến khả năng thực hiện quyền đó hoặc yêu cầu thực hiện quyền đó của một bên bất kỳ lúc nào sau đó và việc từ bỏ vi phạm cũng không cấu thành việc từ bỏ bất kỳ vi phạm nào sau đó.\n\n" +
                        "Biên dịch Phiên dịch\n" +
                        "Các Điều khoản và Điều kiện này có thể đã được dịch nếu Chúng tôi cung cấp cho Bạn trên Dịch vụ của chúng tôi. Bạn đồng ý rằng văn bản tiếng Anh gốc sẽ được ưu tiên trong trường hợp có tranh chấp.\n\n" +
                        "Những thay đổi đối với các Điều khoản và Điều kiện này\n" +
                        "Chúng tôi có quyền, theo quyết định riêng của Chúng tôi, sửa đổi hoặc thay thế các Điều khoản này bất kỳ lúc nào. Nếu bản sửa đổi là quan trọng, Chúng tôi sẽ nỗ lực hợp lý để thông báo trước ít nhất 30 ngày trước khi bất kỳ điều khoản mới nào có hiệu lực. Những gì cấu thành nên sự thay đổi quan trọng sẽ được xác định theo quyết định riêng của Chúng tôi.\n\n" +
                        "Bằng cách tiếp tục truy cập hoặc sử dụng Dịch vụ của chúng tôi sau khi những sửa đổi đó có hiệu lực, Bạn đồng ý bị ràng buộc bởi các điều khoản đã sửa đổi. Nếu Bạn không đồng ý với các điều khoản mới, toàn bộ hoặc một phần, vui lòng ngừng sử dụng trang web và Dịch vụ.\n\n" +
                        "Liên hệ với chúng tôi\n" +
                        "Nếu bạn có bất kỳ câu hỏi nào về các Điều khoản và Điều kiện này, bạn có thể liên hệ với chúng tôi:\n" +
                        "Qua email: nghuyhieu3010@gmail.com")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        termsCheckBox.setChecked(true);  // Tích checkbox khi đồng ý
                    }
                })
                .setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        termsCheckBox.setChecked(false);  // Bỏ tích checkbox nếu không đồng ý
                    }
                })
                .show();
    }


    private void showPrivacyPolicy() {
        new AlertDialog.Builder(this)
                .setTitle("Chính sách bảo mật")
                .setMessage("Chính sách bảo mật cho Ứng Dụng Quản lý Coffee\n\n" +
                        "CHÍNH SÁCH BẢO MẬT\n\n" +
                        "Cập nhật lần cuối: 18 tháng 11 năm 2024\n" +
                        "Chính sách bảo mật này mô tả các chính sách và thủ tục của Chúng tôi về việc thu thập, sử dụng và tiết lộ thông tin của Bạn khi Bạn sử dụng Dịch vụ và cho Bạn biết về quyền riêng tư của Bạn và luật pháp bảo vệ Bạn như thế nào.\n\n" +
                        "Chúng tôi sử dụng Dữ liệu cá nhân của bạn để cung cấp và cải thiện Dịch vụ. Bằng cách sử dụng Dịch vụ, bạn đồng ý với việc thu thập và sử dụng thông tin theo Chính sách bảo mật này. Chính sách bảo mật này được tạo ra với sự trợ giúp của Trình tạo chính sách bảo mật.\n\n" +
                        "GIẢI THÍCH VÀ ĐỊNH NGHĨA\n\n" +
                        "Diễn giải\n" +
                        "Các từ có chữ cái đầu viết hoa có nghĩa được định nghĩa theo các điều kiện sau. Các định nghĩa sau đây sẽ có cùng nghĩa bất kể chúng xuất hiện ở dạng số ít hay số nhiều.\n\n" +
                        "ĐỊNH NGHĨA\n" +
                        "Vì mục đích của Chính sách bảo mật này:\n" +
                        "Tài khoản có nghĩa là tài khoản duy nhất được tạo cho Bạn để truy cập Dịch vụ của chúng tôi hoặc một phần Dịch vụ của chúng tôi.\n" +
                        "Công ty liên kết có nghĩa là một thực thể kiểm soát, được kiểm soát bởi hoặc chịu sự kiểm soát chung với một bên, trong đó 'kiểm soát' có nghĩa là sở hữu 50% hoặc nhiều hơn số cổ phiếu, quyền sở hữu vốn chủ sở hữu hoặc chứng khoán khác có quyền biểu quyết bầu giám đốc hoặc cơ quan quản lý khác.\n" +
                        "Ứng dụng này đề cập đến Ứng Dụng Quản Lý Coffee, chương trình phần mềm do Công ty cung cấp.\n" +
                        "Công ty (được gọi là 'Công ty', 'Chúng tôi', 'Chúng tôi' hoặc 'Của chúng tôi' trong Thỏa thuận này) đề cập đến Ứng Dụng Quản Lý Coffee.\n" +
                        "Quốc gia đề cập đến: Việt Nam\n" +
                        "Thiết bị có nghĩa là bất kỳ thiết bị nào có thể truy cập Dịch vụ như máy tính, điện thoại di động hoặc máy tính bảng kỹ thuật số.\n" +
                        "Dữ liệu cá nhân là bất kỳ thông tin nào liên quan đến một cá nhân đã được xác định hoặc có thể xác định được.\n" +
                        "Dịch vụ đề cập đến Ứng dụng.\n" +
                        "Nhà cung cấp dịch vụ có nghĩa là bất kỳ cá nhân hoặc pháp nhân nào xử lý dữ liệu thay mặt cho Công ty. Nó đề cập đến các công ty hoặc cá nhân bên thứ ba được Công ty thuê để tạo điều kiện cho Dịch vụ, cung cấp Dịch vụ thay mặt cho Công ty, thực hiện các dịch vụ liên quan đến Dịch vụ hoặc hỗ trợ Công ty phân tích cách sử dụng Dịch vụ.\n" +
                        "Dữ liệu sử dụng là dữ liệu được thu thập tự động, được tạo ra do sử dụng Dịch vụ hoặc từ chính cơ sở hạ tầng của Dịch vụ (ví dụ: thời lượng truy cập trang).\n" +
                        "Bạn có nghĩa là cá nhân truy cập hoặc sử dụng Dịch vụ, hoặc công ty hoặc pháp nhân khác mà cá nhân đó thay mặt truy cập hoặc sử dụng Dịch vụ, tùy theo trường hợp.\n\n" +
                        "THU THẬP VÀ SỬ DỤNG DỮ LIỆU CÁ NHÂN CỦA BẠN\n\n" +
                        "CÁC LOẠI DỮ LIỆU ĐƯỢC THU THẬP\n" +
                        "Dữ liệu cá nhân\n" +
                        "Trong khi sử dụng Dịch vụ của Chúng tôi, Chúng tôi có thể yêu cầu Bạn cung cấp cho Chúng tôi một số thông tin nhận dạng cá nhân nhất định có thể được sử dụng để liên hệ hoặc xác định Bạn. Thông tin nhận dạng cá nhân có thể bao gồm, nhưng không giới hạn ở:\n" +
                        "Địa chỉ email\n" +
                        "Dữ liệu sử dụng\n\n" +
                        "DỮ LIỆU SỬ DỤNG\n" +
                        "Dữ liệu sử dụng được thu thập tự động khi sử dụng Dịch vụ.\n" +
                        "Dữ liệu sử dụng có thể bao gồm thông tin như địa chỉ Giao thức Internet của Thiết bị của Bạn (ví dụ: địa chỉ IP), loại trình duyệt, phiên bản trình duyệt, các trang trong Dịch vụ của chúng tôi mà Bạn truy cập, thời gian và ngày truy cập của Bạn, thời gian dành cho các trang đó, mã định danh thiết bị duy nhất và dữ liệu chẩn đoán khác.\n\n" +
                        "SỬ DỤNG DỮ LIỆU CÁ NHÂN CỦA BẠN\n" +
                        "Công ty có thể sử dụng Dữ liệu cá nhân cho các mục đích sau:\n" +
                        "Cung cấp và duy trì Dịch vụ của chúng tôi, bao gồm cả việc theo dõi việc sử dụng Dịch vụ của chúng tôi.\n" +
                        "Để quản lý Tài khoản của Bạn: để quản lý việc đăng ký của Bạn với tư cách là người dùng Dịch vụ. Dữ liệu cá nhân Bạn cung cấp có thể cho Bạn quyền truy cập vào các chức năng khác nhau của Dịch vụ mà Bạn có thể sử dụng với tư cách là người dùng đã đăng ký.\n\n" +
                        "Để liên hệ với Bạn: Để liên hệ với Bạn qua email, cuộc gọi điện thoại, tin nhắn SMS hoặc các hình thức liên lạc điện tử tương đương khác, chẳng hạn như thông báo đẩy của ứng dụng di động về các bản cập nhật hoặc thông tin liên lạc liên quan đến chức năng, sản phẩm hoặc dịch vụ theo hợp đồng, bao gồm các bản cập nhật bảo mật, khi cần thiết hoặc hợp lý để triển khai chúng.\n" +
                        "Cung cấp cho Bạn tin tức, ưu đãi đặc biệt và thông tin chung về các hàng hóa, dịch vụ và sự kiện khác mà chúng tôi cung cấp tương tự như những sản phẩm, dịch vụ và sự kiện mà Bạn đã mua hoặc tìm hiểu, trừ khi Bạn đã chọn không nhận những thông tin đó.\n\n" +
                        "Để quản lý các yêu cầu của bạn: Để giải quyết và quản lý các yêu cầu của bạn gửi đến Chúng tôi.\n" +
                        "Cho các mục đích khác: Chúng tôi có thể sử dụng thông tin của bạn cho các mục đích khác, chẳng hạn như phân tích dữ liệu, xác định xu hướng sử dụng, xác định hiệu quả của các chiến dịch quảng cáo của chúng tôi và để đánh giá và cải thiện Dịch vụ, sản phẩm, dịch vụ, tiếp thị và trải nghiệm của bạn.\n\n" +
                        "CHÚNG TÔI CÓ THỂ CHIA SẺ THÔNG TIN CÁ NHÂN CỦA BẠN TRONG CÁC TRƯỜNG HỢP SAU:\n" +
                        "Với Nhà cung cấp dịch vụ: Chúng tôi có thể chia sẻ thông tin cá nhân của Bạn với Nhà cung cấp dịch vụ để theo dõi và phân tích việc sử dụng Dịch vụ của chúng tôi, để liên hệ với Bạn.\n" +
                        "Đối với việc chuyển nhượng doanh nghiệp: Chúng tôi có thể chia sẻ hoặc chuyển thông tin cá nhân của Bạn liên quan đến hoặc trong quá trình đàm phán về bất kỳ hoạt động sáp nhập, bán tài sản của Công ty, tài trợ hoặc mua lại toàn bộ hoặc một phần doanh nghiệp của Chúng tôi cho một công ty khác.\n" +
                        "Với các Chi nhánh: Chúng tôi có thể chia sẻ thông tin của Bạn với các chi nhánh của Chúng tôi, trong trường hợp đó, chúng tôi sẽ yêu cầu các chi nhánh đó tôn trọng Chính sách bảo mật này.\n" +
                        "Với các đối tác kinh doanh: Chúng tôi có thể chia sẻ thông tin của bạn với các đối tác kinh doanh của chúng tôi để cung cấp cho bạn một số sản phẩm, dịch vụ hoặc chương trình khuyến mãi nhất định.\n" +
                        "Với sự đồng ý của bạn: Chúng tôi có thể tiết lộ thông tin cá nhân của bạn cho bất kỳ mục đích nào khác khi có sự đồng ý của bạn.\n\n" +
                        "LƯU GIỮ DỮ LIỆU CÁ NHÂN CỦA BẠN\n" +
                        "Công ty sẽ chỉ lưu giữ Dữ liệu cá nhân của bạn trong thời gian cần thiết cho các mục đích được nêu trong Chính sách bảo mật này. Chúng tôi sẽ lưu giữ và sử dụng Dữ liệu cá nhân của bạn trong phạm vi cần thiết để tuân thủ các nghĩa vụ pháp lý của chúng tôi (ví dụ: nếu chúng tôi phải giữ lại dữ liệu của bạn để tuân thủ các yêu cầu pháp lý), giải quyết tranh chấp và thực thi các thỏa thuận và chính sách pháp lý của chúng tôi.\n" +
                        "Công ty sẽ lưu trữ Dữ liệu cá nhân của bạn trong các khoảng thời gian sau:\n" +
                        "Để tuân thủ các nghĩa vụ pháp lý của chúng tôi.\n" +
                        "Để giải quyết các tranh chấp.\n" +
                        "Để thực thi các thỏa thuận và chính sách của chúng tôi.\n\n" +
                        "BẢO MẬT DỮ LIỆU CÁ NHÂN\n" +
                        "Chúng tôi coi trọng quyền riêng tư của bạn và cam kết bảo vệ dữ liệu cá nhân của bạn. Tuy nhiên, xin lưu ý rằng không có phương thức truyền tải dữ liệu qua Internet hoặc phương thức lưu trữ điện tử nào là 100% an toàn. Mặc dù chúng tôi cố gắng sử dụng các phương thức bảo mật hợp lý để bảo vệ Dữ liệu cá nhân của bạn, chúng tôi không thể đảm bảo sự an toàn tuyệt đối của nó.\n\n" +
                        "CHUYỂN GIAO DỮ LIỆU\n" +
                        "Thông tin của bạn, bao gồm Dữ liệu cá nhân, có thể được chuyển giao đến — và duy trì trên — máy chủ của bạn ngoài tiểu bang, tỉnh, quốc gia hoặc khu vực pháp lý của bạn nơi các luật bảo vệ dữ liệu có thể khác với khu vực của bạn.\n" + "Thông tin của bạn, bao gồm Dữ liệu cá nhân, được xử lý tại các văn phòng điều hành của Công ty và bất kỳ nơi nào khác mà các bên liên quan đến quá trình xử lý được đặt tại đó. Điều đó có nghĩa là thông tin này có thể được chuyển đến — và được lưu giữ trên — các máy tính nằm ngoài tiểu bang, tỉnh, quốc gia hoặc khu vực pháp lý chính phủ khác của Bạn, nơi luật bảo vệ dữ liệu có thể khác với luật pháp của khu vực pháp lý của Bạn.\n\n" +
                        "Sự đồng ý của bạn với Chính sách bảo mật này kèm theo việc bạn gửi thông tin đó thể hiện sự đồng ý của bạn đối với việc chuyển giao đó.\n\n" +
                        "Công ty sẽ thực hiện mọi bước cần thiết hợp lý để đảm bảo dữ liệu của Bạn được xử lý an toàn và phù hợp với Chính sách bảo mật này và sẽ không chuyển Giao dữ liệu cá nhân của Bạn cho bất kỳ tổ chức hoặc quốc gia nào trừ khi có các biện pháp kiểm soát đầy đủ bao gồm cả tính bảo mật dữ liệu của Bạn và thông tin cá nhân khác.\n\n" +
                        "Xóa dữ liệu cá nhân của bạn:\n" +
                        "Bạn có quyền xóa hoặc yêu cầu Chúng tôi hỗ trợ xóa Dữ liệu cá nhân mà Chúng tôi đã thu thập về Bạn.\n\n" +
                        "Dịch vụ của chúng tôi có thể cung cấp cho Bạn khả năng xóa một số thông tin nhất định về Bạn từ trong Dịch vụ.\n\n" +
                        "Bạn có thể cập nhật, sửa đổi hoặc xóa thông tin của mình bất kỳ lúc nào bằng cách đăng nhập vào Tài khoản của bạn, nếu bạn có, và truy cập phần cài đặt tài khoản cho phép bạn quản lý thông tin cá nhân của mình. Bạn cũng có thể liên hệ với Chúng tôi để yêu cầu truy cập, sửa hoặc xóa bất kỳ thông tin cá nhân nào mà Bạn đã cung cấp cho Chúng tôi.\n\n" +
                        "Tiết lộ dữ liệu cá nhân của bạn:\n" +
                        "Giao dịch kinh doanh: Nếu Công ty tham gia vào việc sáp nhập, mua lại hoặc bán tài sản, Dữ liệu cá nhân của bạn có thể được chuyển giao. Chúng tôi sẽ thông báo trước khi Dữ liệu cá nhân của bạn được chuyển giao và trở thành đối tượng của Chính sách bảo mật khác.\n\n" +
                        "Thực thi pháp luật: Trong một số trường hợp nhất định, Công ty có thể được yêu cầu tiết lộ Dữ liệu cá nhân của bạn nếu luật pháp yêu cầu hoặc để đáp ứng các yêu cầu hợp lệ của cơ quan công quyền (ví dụ: tòa án hoặc cơ quan chính phủ).\n\n" +
                        "Bảo mật dữ liệu cá nhân của bạn:\n" +
                        "Tính bảo mật của Dữ liệu cá nhân của bạn rất quan trọng đối với Chúng tôi, nhưng hãy nhớ rằng không có phương pháp truyền dữ liệu qua Internet hoặc phương pháp lưu trữ điện tử nào an toàn 100%. Mặc dù Chúng tôi cố gắng sử dụng các phương tiện được chấp nhận về mặt thương mại để bảo vệ Dữ liệu cá nhân của bạn, Chúng tôi không thể đảm bảo tính bảo mật tuyệt đối của dữ liệu đó.\n\n" +
                        "Quyền riêng tư của trẻ em: Dịch vụ của chúng tôi không hướng đến bất kỳ ai dưới 13 tuổi. Chúng tôi không cố ý thu thập thông tin nhận dạng cá nhân từ bất kỳ ai dưới 13 tuổi.\n\n" +
                        "Liên kết đến các trang web khác: Dịch vụ của chúng tôi có thể chứa các liên kết đến các trang web khác không do Chúng tôi điều hành. Nếu Bạn nhấp vào liên kết của bên thứ ba, Bạn sẽ được chuyển hướng đến trang web của bên thứ ba đó. Chúng tôi đặc biệt khuyên Bạn nên xem lại Chính sách quyền riêng tư của mọi trang web Bạn truy cập.\n\n" +
                        "Những thay đổi đối với Chính sách bảo mật này: Chúng tôi có thể cập nhật Chính sách bảo mật của mình theo thời gian. Chúng tôi sẽ thông báo cho Bạn về bất kỳ thay đổi nào bằng cách đăng Chính sách bảo mật mới trên trang này.\n\n" +
                        "Liên hệ với chúng tôi: Nếu bạn có bất kỳ câu hỏi nào về Chính sách bảo mật này, bạn có thể liên hệ với chúng tôi qua email: nghuyhieu3010@gmail.com")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newsletterCheckBox.setChecked(true);  // Tích checkbox khi đồng ý
                    }
                })
                .setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newsletterCheckBox.setChecked(false);  // Bỏ tích checkbox nếu không đồng ý
                    }
                })
                .show();
    }

    private void showPrivatePolicy() {
        new AlertDialog.Builder(this)
                .setTitle("Chính sách quyền riêng tư")
                .setMessage("Chính sách quyền riêng tư cho Ứng Dụng Bán Coffee\n\n" +
                        "Cập nhật lần cuối: 18 tháng 11 năm 2024\n\n" +
                        "Chào mừng bạn đến với ứng dụng Ứng Dụng Quản Lý Coffee! Chúng tôi hiểu rằng việc bảo vệ thông tin cá nhân của bạn là rất quan trọng. " +
                        "Vì vậy, Chính sách quyền riêng tư này được thiết kế để giúp bạn hiểu rõ cách chúng tôi thu thập, sử dụng, bảo vệ và quản lý thông tin của bạn khi sử dụng ứng dụng của chúng tôi.\n\n" +
                        "Bằng cách tải xuống và sử dụng ứng dụng Ứng Dụng Quản Lý Coffee, bạn đồng ý với các điều khoản của chính sách này. " +
                        "Nếu bạn không đồng ý với bất kỳ phần nào, vui lòng ngừng sử dụng ứng dụng.\n\n" +
                        "1. THÔNG TIN ĐƯỢC THU THẬP\n" +
                        "Chúng tôi thu thập thông tin từ bạn để cung cấp trải nghiệm tốt nhất khi sử dụng ứng dụng. Các loại thông tin bao gồm:\n" +
                        "1.1. Thông tin cá nhân\n" +
                        "Họ và tên: Để cá nhân hóa trải nghiệm người dùng.\n" +
                        "Email: Dùng để hỗ trợ bạn hoặc gửi thông báo quan trọng.\n\n" +
                        "1.2. Thông tin không nhận diện cá nhân\n" +
                        "Thông tin thiết bị: Hệ điều hành, loại thiết bị (Android/iOS), phiên bản phần mềm.\n" +
                        "Dữ liệu sử dụng: Các thao tác sử dụng ứng dụng, thời gian đăng nhập, trang hoặc tính năng đã truy cập.\n" +
                        "Địa chỉ IP: Để phân tích và phát hiện các vấn đề bảo mật.\n\n" +
                        "2. MỤC ĐÍCH SỬ DỤNG THÔNG TIN\n" +
                        "Thông tin thu thập được sẽ được sử dụng với các mục đích sau:\n" +
                        "- Cung cấp dịch vụ: Cải thiện tính năng và nội dung của ứng dụng để phục vụ bạn tốt hơn.\n" +
                        "- Cá nhân hóa trải nghiệm: Hiển thị nội dung phù hợp với sở thích và nhu cầu của bạn.\n" +
                        "- Gửi thông báo: Cập nhật các thay đổi về ứng dụng, chính sách hoặc tính năng mới.\n" +
                        "- Hỗ trợ kỹ thuật: Giải quyết các lỗi và vấn đề bạn gặp phải trong quá trình sử dụng ứng dụng.\n\n" +
                        "3. CHIA SẺ DỮ LIỆU VỚI BÊN THỨ BA\n" +
                        "Chúng tôi chỉ chia sẻ thông tin của bạn trong các trường hợp cụ thể sau:\n" +
                        "- Với sự đồng ý của bạn: Chúng tôi sẽ yêu cầu sự đồng ý của bạn trước khi chia sẻ bất kỳ thông tin nào.\n" +
                        "- Tuân thủ pháp luật: Chúng tôi có thể chia sẻ thông tin nếu được yêu cầu bởi pháp luật.\n" +
                        "- Nhà cung cấp dịch vụ: Hợp tác với các bên thứ ba để xử lý dữ liệu hoặc cung cấp dịch vụ bổ sung.\n\n" +
                        "4. BẢO MẬT DỮ LIỆU\n" +
                        "Chúng tôi cam kết bảo mật thông tin cá nhân của bạn bằng cách áp dụng các biện pháp bảo mật hiện đại, bao gồm mã hóa dữ liệu, kiểm soát truy cập và lưu trữ an toàn.\n\n" +
                        "5. QUYỀN CỦA NGƯỜI DÙNG\n" +
                        "Bạn có quyền xem, chỉnh sửa hoặc xóa thông tin cá nhân của mình bất kỳ lúc nào bằng cách liên hệ với chúng tôi.\n\n" +
                        "6. QUYỀN RIÊNG TƯ CỦA TRẺ EM\n" +
                        "Ứng dụng không dành cho người dưới 13 tuổi. Nếu phát hiện đã thu thập thông tin từ trẻ em, chúng tôi sẽ xóa ngay lập tức.\n\n" +
                        "7. LIÊN KẾT ĐẾN BÊN THỨ BA\n" +
                        "Ứng dụng có thể chứa liên kết đến các trang web hoặc dịch vụ bên thứ ba. Chúng tôi không chịu trách nhiệm về nội dung hoặc bảo mật của họ.\n\n" +
                        "8. THAY ĐỔI CHÍNH SÁCH\n" +
                        "Chúng tôi có thể cập nhật chính sách quyền riêng tư này để phản ánh các thay đổi trong hoạt động hoặc quy định pháp luật. Bạn sẽ được thông báo trước khi thay đổi có hiệu lực.\n\n"+
                        "Liên hệ với chúng tôi: Nếu bạn có bất kỳ câu hỏi nào về Chính sách quyền riêng tư này, bạn có thể liên hệ với chúng tôi qua email: nghuyhieu3010@gmail.com")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        priCheckBox.setChecked(true);  // Tích checkbox khi đồng ý
                    }
                })
                .setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        priCheckBox .setChecked(false);  // Bỏ tích checkbox nếu không đồng ý
                    }
                })
                .show();
    }
}

//endregion
