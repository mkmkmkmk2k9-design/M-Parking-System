import javax.swing.*;
import javax.swing.border.EmptyBorder; // Thư viện dùng để tạo khoảng trống (padding) xung quanh component mà không vẽ viền.
import java.awt.*;
import java.io.File;

public class ParkForm extends JFrame {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout = new CardLayout();
    private JPanel contentPanel = new JPanel(cardLayout);
    private JButton lastBtn;
    private Color sidebarColor = new Color(214, 120, 29);   
    private Color menuGroupBg = new Color(29, 123, 214);    
    private Color activeColor = new Color(0, 200, 100);   

    public ParkForm() {
        setTitle("K-PARKING SYSTEM");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(sidebarColor);
        sidebar.setPreferredSize(new Dimension(140, 0));

        JPanel logoArea = new JPanel(new GridBagLayout());
        logoArea.setBackground(sidebarColor);
        logoArea.setPreferredSize(new Dimension(140, 140));

        JLabel lblLogo = new JLabel("K");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 45));
        lblLogo.setForeground(Color.BLACK);
        lblLogo.setOpaque(true);
        lblLogo.setBackground(Color.WHITE);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setPreferredSize(new Dimension(75, 75));
        logoArea.add(lblLogo);
        sidebar.add(logoArea, BorderLayout.NORTH);

        JPanel menuWrapper = new JPanel();
        menuWrapper.setLayout(new BoxLayout(menuWrapper, BoxLayout.Y_AXIS));
        menuWrapper.setBackground(menuGroupBg);
        menuWrapper.setBorder(new EmptyBorder(20, 10, 20, 10));

        JButton btnHome = navBtn("TRANG CHỦ", "home.png", "trangchu");
        JButton btnNhan = navBtn("NHẬN XE", "in.png", "nhanxe");
        JButton btnTra = navBtn("TRẢ XE", "out.png", "traxe");
        JButton btnTraCuu = navBtn("TRA CỨU", "find.png", "tracuu");
        JButton btnThongKe = navBtn("THỐNG KÊ", "dia.png", "thongke");
        JButton btnLeave = navBtn("LEAVE", "leave.png", "leave");

        menuWrapper.add(btnHome);
        menuWrapper.add(separator());
        menuWrapper.add(btnNhan);
        menuWrapper.add(separator());
        menuWrapper.add(btnTra);
        menuWrapper.add(separator());
        menuWrapper.add(btnTraCuu);
        menuWrapper.add(separator());
        menuWrapper.add(btnThongKe);
        menuWrapper.add(separator()); 
        menuWrapper.add(Box.createVerticalGlue());
        menuWrapper.add(btnLeave);
        sidebar.add(menuWrapper, BorderLayout.CENTER);
        contentPanel.setBackground(Color.WHITE);
        
        contentPanel.add(new TrangChu(), "trangchu");
        contentPanel.add(new NhanXe(), "nhanxe"); 
        contentPanel.add(new TraXe(), "traxe");
        contentPanel.add(new TraCuu(), "tracuu");
        contentPanel.add(new ThongKe(), "thongke");
        contentPanel.add(new JPanel(), "leave");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            cardLayout.show(contentPanel, "trangchu");
        });
    }

    private JButton navBtn(String name, String iconName, String card) {
        String html = "<html><center><span style='font-size:8px;'>" + name + "</span></center></html>";
        JButton b = new JButton(html);

        String path = "C:/Dự án/" + iconName;
        if (new File(path).exists()) {
            Image img = new ImageIcon(path).getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            b.setIcon(new ImageIcon(img));
        }

        b.setVerticalTextPosition(SwingConstants.BOTTOM);
        b.setHorizontalTextPosition(SwingConstants.CENTER);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(110, 85));
        b.setPreferredSize(new Dimension(110, 85));

        b.setBorder(BorderFactory.createEmptyBorder());
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setContentAreaFilled(true);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.setBackground(menuGroupBg);
        b.setForeground(Color.WHITE);

        b.addActionListener(e -> {
            if ("leave".equals(card)) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn thoát?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) System.exit(0);
                return;
            }

            cardLayout.show(contentPanel, card);

            if (lastBtn != null) {
                lastBtn.setBackground(menuGroupBg);
            }
            b.setBackground(activeColor);
            lastBtn = b;
        });
        return b;
    }

    private JSeparator separator() {
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setMaximumSize(new Dimension(120, 1));
        sep.setForeground(new Color(255, 255, 255, 50)); 
        return sep;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new ParkForm().setVisible(true));
    }
}