import javax.swing.*;
import javax.swing.border.EmptyBorder; // Thư viện dùng để tạo khoảng cách cho component mà không hiển thị viền.
import javax.swing.border.TitledBorder; // Thư viện dùng để tạo viền có tiêu đề cho JPanel hoặc component.
import javax.swing.border.LineBorder; // Thư viện dùng để tạo viền đơn với màu và độ dày xác định.
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat; // Thư viện dùng để định dạng số, đặc biệt là tiền tệ theo từng quốc gia.
import java.text.SimpleDateFormat; // Thư viện dùng để chuyển ngày giờ sang chuỗi theo định dạng chỉ định.
import java.util.Locale; // Thư viện xác định vùng quốc gia để định dạng số và tiền tệ cho đúng chuẩn.

public class TraXe extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField txtSearch;
    private JComboBox<String> cbLoaiXeSearch; 
    private JButton btnSearch, btnConfirm;
    private JLabel lblHoTen, lblSDT, lblBienSo, lblLoaiXe, lblViTri, lblGioVao, lblGioRa, lblTongTien;
    
    private String currentViTriID = "";
    private Timestamp timeVaoValue;
    private long lastPrice = 0;

    public TraXe() {
        UIManager.put("Button.disabledText", Color.YELLOW);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("HỆ THỐNG TRẢ XE & TÍNH TIỀN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(220, 20, 60));
        title.setBorder(new EmptyBorder(20, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(new EmptyBorder(0, 80, 20, 80));

        JPanel searchWrapper = new JPanel(new GridBagLayout());
        searchWrapper.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        inputRow.setBackground(Color.WHITE);

        cbLoaiXeSearch = new JComboBox<>(new String[]{"Xe máy", "Ô tô", "Xe đạp"});
        cbLoaiXeSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbLoaiXeSearch.setPreferredSize(new Dimension(100, 30));

        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtSearch.setPreferredSize(new Dimension(200, 30));

        btnSearch = new JButton("TÌM KIẾM");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSearch.setBackground(new Color(0, 80, 150)); 
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setOpaque(true);
        btnSearch.setBorderPainted(false);
        btnSearch.setFocusPainted(false); 
        btnSearch.setPreferredSize(new Dimension(120, 32));

        inputRow.add(new JLabel("Loại xe:"));
        inputRow.add(cbLoaiXeSearch);
        inputRow.add(new JLabel("Biển số xe hoặc SĐT:"));
        inputRow.add(txtSearch);
        inputRow.add(btnSearch);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        searchWrapper.add(inputRow, gbc);

        JLabel lblNote = new JLabel("* Lưu ý: Vui lòng nhập Số điện thoại khách hàng đối với trường hợp sử dụng Xe đạp");
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblNote.setForeground(Color.GRAY);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST; 
        gbc.insets = new Insets(0, 80, 0, 0);
        searchWrapper.add(lblNote, gbc);

        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 8));
        infoPanel.setBackground(Color.WHITE);
        TitledBorder infoBorder = BorderFactory.createTitledBorder(new LineBorder(new Color(0, 102, 204), 2), " THÔNG TIN CHI TIẾT ", TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 16), new Color(0, 102, 204));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(infoBorder, new EmptyBorder(10, 25, 10, 25)));

        lblHoTen = createStyledLabel("Khách hàng: ");
        lblSDT = createStyledLabel("Số điện thoại: ");
        lblLoaiXe = createStyledLabel("Loại xe: ");
        lblBienSo = createStyledLabel("Biển số xe: ");
        lblViTri = createStyledLabel("Vị trí đỗ: ");
        lblGioVao = createStyledLabel("Thời điểm vào: ");
        lblGioRa = createStyledLabel("Thời điểm ra: "); 
        
        lblTongTien = new JLabel("TỔNG TIỀN: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTongTien.setForeground(new Color(204, 0, 0));

        infoPanel.add(lblHoTen);
        infoPanel.add(lblSDT);
        infoPanel.add(lblLoaiXe);
        infoPanel.add(lblBienSo);
        infoPanel.add(lblViTri);
        infoPanel.add(lblGioVao);
        infoPanel.add(lblGioRa); 
        infoPanel.add(lblTongTien);

        btnConfirm = new JButton("XÁC NHẬN THANH TOÁN & TRẢ XE");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnConfirm.setForeground(Color.YELLOW);              
        btnConfirm.setBackground(new Color(46, 139, 87));
        btnConfirm.setEnabled(false);                       
        btnConfirm.setOpaque(true);
        btnConfirm.setBorderPainted(false);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirm.setMaximumSize(new Dimension(500, 50));    
        btnConfirm.setPreferredSize(new Dimension(500, 50));

        centerContainer.add(searchWrapper);
        centerContainer.add(Box.createVerticalStrut(20));
        centerContainer.add(infoPanel);
        centerContainer.add(Box.createVerticalStrut(20));
        centerContainer.add(btnConfirm);

        add(centerContainer, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> searchXe());
        btnConfirm.addActionListener(e -> processTraXe());
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        return label;
    }

    private void searchXe() {
        String key = txtSearch.getText().trim();
        String loaiXeChon = (String) cbLoaiXeSearch.getSelectedItem();
        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin tìm kiếm!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM BaiXe WHERE TrangThai = N'Có xe' AND LoaiXe = ? AND (BienSo = ? OR SoDienThoai = ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loaiXeChon);
            pstmt.setString(2, key);
            pstmt.setString(3, key);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                currentViTriID = rs.getString("ViTriID");
                String dbLoaiXe = rs.getString("LoaiXe");
                lblHoTen.setText("Khách hàng: " + rs.getString("HoTenKH"));
                lblSDT.setText("Số điện thoại: " + rs.getString("SoDienThoai"));
                lblLoaiXe.setText("Loại xe: " + dbLoaiXe);
                lblBienSo.setText("Biển số xe: " + (dbLoaiXe.equals("Xe đạp") ? "KHÔNG CÓ" : rs.getString("BienSo")));
                lblViTri.setText("Vị trí đỗ: " + currentViTriID);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                timeVaoValue = rs.getTimestamp("ThoiGianVao");
                Timestamp ra = new Timestamp(System.currentTimeMillis()); 
                
                lblGioVao.setText("Thời điểm vào: " + sdf.format(timeVaoValue));
                lblGioRa.setText("Thời điểm ra: " + sdf.format(ra)); 

                long diffMillis = ra.getTime() - timeVaoValue.getTime();
                long diffMinutes = diffMillis / (1000 * 60);
                lastPrice = calculatePrice(dbLoaiXe, diffMinutes);
                
                lblTongTien.setText("TỔNG TIỀN: " + NumberFormat.getCurrencyInstance(Locale.of("vi", "VN")).format(lastPrice));
                btnConfirm.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu phù hợp!");
                clearInfo();
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private long calculatePrice(String loaiXe, long totalMinutes) {
        if (totalMinutes < 15) return 0; 
        long hours = (long) Math.ceil(totalMinutes / 60.0);
        if (loaiXe.equals("Xe đạp")) return 3000; 
        if (loaiXe.equals("Xe máy")) return 5000 * hours;
        if (loaiXe.equals("Ô tô")) return 20000 * hours;
        return 0;
    }

    private void processTraXe() {
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán và trả xe?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                conn.setAutoCommit(false); 
                try {                  
                    String sqlLichSu = "INSERT INTO LichSu (ViTriID, KhuVuc, TrangThai, BienSo, HoTenKH, SoDienThoai, LoaiXe, ThoiGianVao, ThoiGianRa, TongTien) " +
                                       "SELECT ViTriID, KhuVuc, N'Đã thanh toán', BienSo, HoTenKH, SoDienThoai, LoaiXe, ThoiGianVao, GETDATE(), ? " +
                                       "FROM BaiXe WHERE ViTriID = ?";
                    
                    PreparedStatement psHist = conn.prepareStatement(sqlLichSu);
                    psHist.setLong(1, lastPrice); 
                    psHist.setString(2, currentViTriID); 
                    psHist.executeUpdate();
                    String sqlUpdate = "UPDATE BaiXe SET TrangThai = N'Trống', BienSo = NULL, HoTenKH = NULL, " +
                                       "SoDienThoai = NULL, LoaiXe = NULL, ThoiGianVao = NULL WHERE ViTriID = ?";
                    PreparedStatement psUp = conn.prepareStatement(sqlUpdate);
                    psUp.setString(1, currentViTriID);
                    psUp.executeUpdate();

                    conn.commit(); 
                    JOptionPane.showMessageDialog(this, "Thanh toán thành công! Đã lưu vào lịch sử.");
                    clearInfo();
                    txtSearch.setText("");
                } catch (SQLException ex) {
                    conn.rollback(); 
                    JOptionPane.showMessageDialog(this, "Lỗi SQL chi tiết: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    private void clearInfo() {
        lblHoTen.setText("Khách hàng: ");
        lblSDT.setText("Số điện thoại: ");
        lblBienSo.setText("Biển số xe: ");
        lblLoaiXe.setText("Loại xe: ");
        lblViTri.setText("Vị trí đỗ: ");
        lblGioVao.setText("Thời điểm vào: ");
        lblGioRa.setText("Thời điểm ra: ");
        lblTongTien.setText("TỔNG TIỀN: 0 VNĐ");
        btnConfirm.setEnabled(false);
    }
}