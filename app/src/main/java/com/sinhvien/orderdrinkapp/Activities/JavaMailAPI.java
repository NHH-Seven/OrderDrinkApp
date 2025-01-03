package com.sinhvien.orderdrinkapp.Activities;


import android.os.AsyncTask;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class JavaMailAPI extends AsyncTask<Void, Void, String> {

    private String email, subject, message;
    private Session session;

    // Khởi tạo các tham số truyền vào
    public JavaMailAPI(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Đặt ProgressBar thành Visible trước khi gửi email
    }

    @Override
    protected String doInBackground(Void... voids) {
        // Thiết lập các thuộc tính cho phiên làm việc (session)
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Bật STARTTLS
        props.put("mail.smtp.port", "587");

        // Đăng nhập với mật khẩu ứng dụng (dùng mật khẩu ứng dụng của Gmail)
        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("hucby2@gmail.com", "ryysuxxscndhbtjk");
            }
        });

        try {
            // Tạo và cấu hình MimeMessage để gửi email
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress("hucby2@gmail.com"));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            // Gửi email
            Transport.send(mimeMessage);
            return "Gửi email thành công";

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Lỗi khi gửi email";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Kết quả khi gửi email hoàn tất, có thể hiển thị kết quả
        // Ví dụ: hiển thị thông báo thành công hay lỗi, và ẩn ProgressBar
    }

}
