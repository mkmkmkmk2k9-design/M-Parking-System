import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class NhanXe extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane;
    private JLabel lblSoLuongXe;
    private boolean isLoading = false;
    private final Color COLOR_TRONG = new Color(235, 51, 36);
    private final Color COLOR_CO_XE = new Color(0, 200, 100);

    class LimitDocumentFilter extends DocumentFilter {
        private int limit;
        public LimitDocumentFilter(int limit) { this.limit = limit; }
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if (string.matches("\\d+") && fb.getDocument().getLength() + string.length() <= limit) {
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (text.matches("\\d+") && fb.getDocument().getLength() + text.length() - length <= limit) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    public NhanXe() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ VỊ TRÍ ĐỖ XE", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));

        lblSoLuongXe = new JLabel("Số xe trong bãi: 0", JLabel.RIGHT);
        lblSoLuongXe.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSoLuongXe.setForeground(new Color(33, 150, 243));
        lblSoLuongXe.setBorder(new EmptyBorder(0, 0, 0, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(lblSoLuongXe, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(tabbedPane, BorderLayout.CENTER);

        JPanel legend = new JPanel();
        legend.setBackground(Color.WHITE);
        legend.setBorder(new EmptyBorder(10, 0, 10, 0));
        legend.add(createLegendItem("TRỐNG", COLOR_TRONG));
        legend.add(Box.createHorizontalStrut(30));
        legend.add(createLegendItem("ĐÃ CÓ XE", COLOR_CO_XE));
        add(legend, BorderLayout.SOUTH);

        loadDataToTabs();
    }

    private void loadDataToTabs() {
    	if (isLoading) return;
    	isLoading = true;
        int currentTab = tabbedPane.getSelectedIndex();
        new Thread(() -> {
            try {
                int soLuong = getSoLuongXeDangGui();
                JPanel panelA = createGridPanel("A");
                JPanel panelB = createGridPanel("B");
                JPanel panelC = createGridPanel("C");

                SwingUtilities.invokeLater(() -> {
                    tabbedPane.removeAll();
                    tabbedPane.addTab("KHU VỰC A", panelA);
                    tabbedPane.addTab("KHU VỰC B", panelB);
                    tabbedPane.addTab("KHU VỰC C", panelC);
                    if (currentTab != -1 && currentTab < tabbedPane.getTabCount()) {
                        tabbedPane.setSelectedIndex(currentTab);
                    }
                    lblSoLuongXe.setText("Số xe trong bãi: " + soLuong + " / 60");
                });
            } catch (Exception e) { e.printStackTrace(); isLoading = false; }
        }).start();
    }

    private int getSoLuongXeDangGui() {
        int count = 0;
        synchronized (DBConnection.class) {
            try {
                Connection conn = DBConnection.getConnection(); 
                String sql = "SELECT COUNT(*) FROM BaiXe WHERE TrangThai = 'Có xe'";
                try (PreparedStatement ps = conn.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) count = rs.getInt(1);
                }
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return count;
    }

    private JPanel createGridPanel(String khuVuc) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel grid = new JPanel(new GridLayout(0, 5, 12, 12));
        grid.setBorder(new EmptyBorder(20, 20, 20, 20));
        grid.setBackground(new Color(240, 240, 240));

        java.util.List<String[]> dataList = new java.util.ArrayList<>();
        synchronized (DBConnection.class) {
            try {
                Connection conn = DBConnection.getConnection();
                String sql = "SELECT ViTriID, TrangThai, BienSo FROM BaiXe WHERE KhuVuc = ? " +
                             "ORDER BY CAST(SUBSTRING(ViTriID, 2) AS SIGNED) ASC";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, khuVuc);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            dataList.add(new String[]{ rs.getString("ViTriID"), rs.getString("TrangThai"), rs.getString("BienSo") });
                        }
                    }
                }
            } catch (SQLException e) { System.err.println("Lỗi tại khu vực " + khuVuc + ": " + e.getMessage()); }
        }

        for (String[] row : dataList) {
            grid.add(buildSlotUI(row[0], row[1], row[2]));
        }
        panel.add(new JScrollPane(grid), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildSlotUI(String id, String status, String plate) {
        boolean isOccupied = "Có xe".equals(status);
        Color themeColor = isOccupied ? COLOR_CO_XE : COLOR_TRONG;
        JPanel slot = new JPanel(new BorderLayout());
        slot.setPreferredSize(new Dimension(120, 100));
        slot.setBackground(Color.WHITE);
        slot.setBorder(new LineBorder(themeColor, 3));
        slot.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblID = new JLabel(id, JLabel.CENTER);
        lblID.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblID.setForeground(themeColor);

        JLabel lblPlate = new JLabel(isOccupied ? (plate == null || plate.isEmpty() ? "XE ĐẠP" : plate) : "TRỐNG", JLabel.CENTER);
        lblPlate.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        slot.add(lblID, BorderLayout.CENTER);
        slot.add(lblPlate, BorderLayout.SOUTH);
        slot.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (isOccupied) JOptionPane.showMessageDialog(null, "Vị trí " + id + " đã có xe!");
                else nhanXeAction(id);
            }
        });
        return slot;
    }

    private void nhanXeAction(String id) {
        JTextField txtHoTen = new JTextField();
        JTextField txtSDT = new JTextField();
        JTextField txtBienSo = new JTextField();
        ((AbstractDocument) txtSDT.getDocument()).setDocumentFilter(new LimitDocumentFilter(10));
        JComboBox<String> cbLoaiXe = new JComboBox<>(new String[]{"Xe máy", "Ô tô", "Xe đạp"});
        cbLoaiXe.addActionListener(e -> {
            boolean bike = cbLoaiXe.getSelectedItem().equals("Xe đạp");
            txtBienSo.setEnabled(!bike);
            if (bike) txtBienSo.setText("");
        });

        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.add(new JLabel("Họ tên:")); p.add(txtHoTen);
        p.add(new JLabel("SĐT:")); p.add(txtSDT);
        p.add(new JLabel("Loại xe:")); p.add(cbLoaiXe);
        p.add(new JLabel("Biển số:")); p.add(txtBienSo);

        if (JOptionPane.showConfirmDialog(this, p, "NHẬN XE - " + id, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            synchronized (DBConnection.class) {
                try {
                    Connection conn = DBConnection.getConnection();
                    String sql = "UPDATE BaiXe SET TrangThai = 'Có xe', BienSo=?, HoTenKH=?, SoDienThoai=?, LoaiXe=?, ThoiGianVao=NOW() WHERE ViTriID=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, txtBienSo.getText().trim().toUpperCase());
                        ps.setString(2, txtHoTen.getText().trim());
                        ps.setString(3, txtSDT.getText().trim());
                        ps.setString(4, cbLoaiXe.getSelectedItem().toString());
                        ps.setString(5, id);
                        if (ps.executeUpdate() > 0) {
                            JOptionPane.showMessageDialog(this, "Nhận xe thành công!");
                            loadDataToTabs();
                        }
                    }
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        JLabel box = new JLabel("■");
        box.setForeground(color);
        box.setFont(new Font("Serif", Font.BOLD, 20));
        p.add(box);
        p.add(new JLabel(text));
        return p;
    }
}
