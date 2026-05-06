import javax.swing.*;
import java.awt.*;
import java.util.Properties; // Thư viện giúp dùng để lưu các thông số cấu hình, thường dùng để cấu hình SMTP khi gửi email.
import javax.mail.*; // Thư viện cung cấp các lớp nền tảng để tạo phiên làm việc và gửi email.
import javax.mail.internet.*; // Thư viện dùng để tạo và xử lý email theo chuẩn Internet như địa chỉ email, nội dung MIME.
import java.io.FileWriter; // Thư viện giúp dùng để ghi dữ liệu dạng text vào file.
import java.io.PrintWriter; // Thư viện hỗ trợ ghi text ra file với cú pháp đơn giản.
import java.io.IOException; // Thư viện dùng để xử lý lỗi vào/ra khi làm việc với file.

public class Fpass extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTextField txtEmail, txtCode;
    private JPasswordField txtNewPass, txtConfirmPass;
    private JButton btnSend, btnVerify, btnReset;
    private JPanel panelStep1, panelStep2, panelStep3;
    private String sentCode = ""; 

    public Fpass(JFrame parent) {
        super(parent, true); 
        setSize(420, 360);
        setLayout(new CardLayout()); 
        setLocationRelativeTo(parent);

        initStep1(); 
        initStep2(); 
        initStep3(); 

        add(panelStep1, "step1");
        add(panelStep2, "step2");
        add(panelStep3, "step3");
    }

    private void initStep1() {
        panelStep1 = new JPanel(new GridBagLayout());
        panelStep1.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,25,15,25); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0;
        panelStep1.add(new JLabel("<html><b style='font-size:16px; color:#1a73e8'>NHẬP EMAIL</b></html>"), gbc);
        gbc.gridy = 1;
        JLabel lblHelp = new JLabel("<html><i style='color:gray'>Bạn hãy nhập tài khoản email đã được đăng ký </i></html>");
        panelStep1.add(lblHelp, gbc);
        
        txtEmail = new JTextField(20);
        txtEmail.setPreferredSize(new Dimension(0, 50));
        txtEmail.setBorder(BorderFactory.createTitledBorder("Email"));
        gbc.gridy = 2; panelStep1.add(txtEmail, gbc);

        btnSend = new JButton("Gửi mã xác minh");
        btnSend.setPreferredSize(new Dimension(0, 40));
        btnSend.setBackground(new Color(26, 115, 232)); 
        btnSend.setForeground(Color.WHITE);
        btnSend.setFont(new Font("Arial", Font.BOLD, 13));
        btnSend.setFocusPainted(false);
        gbc.gridy = 3; panelStep1.add(btnSend, gbc);

        txtEmail.addActionListener(e -> btnSend.doClick());
        btnSend.addActionListener(e -> handleSendMail());
    }

    private void initStep2() {
        panelStep2 = new JPanel(new GridBagLayout());
        panelStep2.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,25,15,25); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0;
        panelStep2.add(new JLabel("<html><b style='font-size:15px; color:#1a73e8'>XÁC THỰC MÃ</b></html>"), gbc);

        txtCode = new JTextField(20);
        txtCode.setBorder(BorderFactory.createTitledBorder("Mã xác nhận (6 số)"));
        gbc.gridy = 1; panelStep2.add(txtCode, gbc);

        btnVerify = new JButton("Xác nhận mã");
        txtCode.setPreferredSize(new Dimension(0, 40));
        btnVerify.setBackground(new Color(52, 168, 83)); 
        btnVerify.setForeground(Color.WHITE);
        btnVerify.setFont(new Font("Arial", Font.BOLD, 13));
        btnVerify.setFocusPainted(false);
        gbc.gridy = 2; panelStep2.add(btnVerify, gbc);

        txtCode.addActionListener(e -> btnVerify.doClick());
        btnVerify.addActionListener(e -> {
            if (txtCode.getText().trim().equals(sentCode)) {
                ((CardLayout)getContentPane().getLayout()).show(getContentPane(), "step3");
            } else {
                JOptionPane.showMessageDialog(this, "Mã không đúng! Vui lòng kiểm tra lại.");
            }
        });
    }

    private void initStep3() {
        panelStep3 = new JPanel(new GridBagLayout());
        panelStep3.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,25,15,25); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0;
        panelStep3.add(new JLabel("<html><b style='font-size:15px; color:#1a73e8'>ĐẶT MẬT KHẨU MỚI</b></html>"), gbc);

        txtNewPass = new JPasswordField(20);
        txtNewPass.setPreferredSize(new Dimension(0, 40));
        txtNewPass.setBorder(BorderFactory.createTitledBorder("Tạo mật khẩu mới"));
        gbc.gridy = 1; panelStep3.add(txtNewPass, gbc);

        txtConfirmPass = new JPasswordField(20);
        txtConfirmPass.setPreferredSize(new Dimension(0, 40));
        txtConfirmPass.setBorder(BorderFactory.createTitledBorder("Xác nhận mật khẩu mới"));
        gbc.gridy = 2; panelStep3.add(txtConfirmPass, gbc);

        btnReset = new JButton("Cập nhật mật khẩu");
        btnReset.setPreferredSize(new Dimension(0, 40));
        btnReset.setBackground(new Color(234, 67, 53)); btnReset.setForeground(Color.WHITE);
        btnReset.setFont(new Font("Arial", Font.BOLD, 13));
        btnReset.setFocusPainted(false);
        gbc.gridy = 3; panelStep3.add(btnReset, gbc);

        txtNewPass.addActionListener(e -> txtConfirmPass.requestFocusInWindow());
        txtConfirmPass.addActionListener(e -> btnReset.doClick());
        btnReset.addActionListener(e -> {
            String p1 = new String(txtNewPass.getPassword());
            String p2 = new String(txtConfirmPass.getPassword());
            if (p1.equals(p2) && !p1.isEmpty()) {
                try (PrintWriter out = new PrintWriter(new FileWriter("C:/Dự án/admin_pass.txt"))) {
                    out.print(p1); 
                    JOptionPane.showMessageDialog(this, "Mật khẩu đã được cập nhật thành công!");
                    dispose();
                } catch (IOException ioex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lưu mật khẩu: " + ioex.getMessage());
                }
            } else if (p1.isEmpty()){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!");
            } else {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không đúng!");
            }
        });
    }

    private void handleSendMail() {
        String targetEmail = txtEmail.getText().trim();
        if (!targetEmail.contains("@") || targetEmail.length() < 5) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email hợp lệ!");
            return;
        }
        if (!targetEmail.equalsIgnoreCase("khoatda.25ai@vku.udn.vn")) {
            JOptionPane.showMessageDialog(this, "Email này không tồn tại trong hệ thống K Parking!", 
                                                "Lỗi xác thực", 
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        btnSend.setEnabled(false);
        btnSend.setText("Đang gửi...");
        sentCode = String.valueOf((int)((Math.random() * 900000) + 100000));

        new Thread(() -> {
            boolean success = sendMailReal(targetEmail, sentCode);
            SwingUtilities.invokeLater(() -> {
                if (success) {
                    JOptionPane.showMessageDialog(this, "Mã đã gửi tới: " + targetEmail);
                    ((CardLayout)getContentPane().getLayout()).show(getContentPane(), "step2");
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi gửi email! Kiểm tra kết nối mạng.");
                    btnSend.setEnabled(true);
                    btnSend.setText("Gửi lại mã xác minh");
                }
            });
        }).start();
    }

    private boolean sendMailReal(String target, String code) {
        final String user = "khoatda.25ai@vku.udn.vn"; 
        final String pass = "kjqfaysthulqaftn"; 

        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", "587");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");

        Session s = Session.getInstance(p, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        try {
            Message msg = new MimeMessage(s);
            msg.setFrom(new InternetAddress(user, "K Parking")); 
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(target));
            msg.setSubject("[" + code + "] Mã khôi phục mật khẩu");
            msg.setText("Chào bạn,\n\nMã xác minh của bạn là: " + code);
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}