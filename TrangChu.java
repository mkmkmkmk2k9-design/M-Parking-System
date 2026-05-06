import javax.swing.*;
import javax.swing.border.EmptyBorder; // Thư viện dùng để tạo khoảng trống (padding) xung quanh component mà không vẽ viền.
import javax.swing.border.LineBorder; // Thư viện dùng để vẽ đường viền hình chữ nhật xung quanh component trong Swing.
import java.awt.*;
import java.text.SimpleDateFormat; // Thư viện dùng để định dạng hoặc phân tích ngày giờ theo mẫu bạn tự đặt.
import java.util.Date; // Thư viện đại diện cho thời điểm hiện tại (ngày + giờ).
import java.util.Locale; // Thư viện xác định ngôn ngữ + vùng khi hiển thị (đặc biệt quan trọng với ngày, tiền tệ).

public class TrangChu extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblTime;

    public TrangChu() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblWelcome = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG K-PARKING", JLabel.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblWelcome.setForeground(new Color(214, 120, 29)); 
        
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0); 
        add(lblWelcome, gbc);

        lblTime = new JLabel("", JLabel.CENTER);
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lblTime.setForeground(new Color(100, 100, 100)); 
        
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
        updateTime(); 

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 50, 0); 
        add(lblTime, gbc);

        JPanel cardAdmin = new JPanel();
        cardAdmin.setLayout(new BoxLayout(cardAdmin, BoxLayout.Y_AXIS));
        cardAdmin.setBackground(new Color(252, 252, 252));
        cardAdmin.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(29, 123, 214), 2),
            new EmptyBorder(30, 60, 30, 60)
        ));

        JLabel titleAdmin = new JLabel("THÔNG TIN QUẢN TRỊ VIÊN");
        titleAdmin.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleAdmin.setForeground(new Color(29, 123, 214)); 
        titleAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardAdmin.add(titleAdmin);
        cardAdmin.add(Box.createVerticalStrut(15));
        cardAdmin.add(new JSeparator());
        cardAdmin.add(Box.createVerticalStrut(20));
        
        cardAdmin.add(createInfoLabel("Họ và tên:", "Trần Đăng Anh Khoa"));
        cardAdmin.add(Box.createVerticalStrut(12));
        cardAdmin.add(createInfoLabel("Số điện thoại:", "0942 568 436"));
        cardAdmin.add(Box.createVerticalStrut(12));
        cardAdmin.add(createInfoLabel("Địa chỉ:", "Khố phố Ngân Câu, Điện Bàn Đông, TP.Đà Nẵng"));
        cardAdmin.add(Box.createVerticalStrut(12));
        cardAdmin.add(createInfoLabel("Chức vụ:", "Quản trị viên hệ thống"));

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE; 
        add(cardAdmin, gbc);
    }

    private JLabel createInfoLabel(String label, String value) {
        JLabel lbl = new JLabel("<html><div style='width: 350px;'>" +
                                "<b style='color:#444444; font-size:14px;'>" + label + "</b> &nbsp; " + 
                                "<span style='color:#000000; font-size:14px;'>" + value + "</span></div></html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private void updateTime() {
    	Locale localeVN = Locale.of("vi", "VN");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd/MM/yyyy - HH:mm:ss", localeVN);
        String timeStr = sdf.format(new Date());
        lblTime.setText(timeStr.toUpperCase());
    }
}