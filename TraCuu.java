import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.TitledBorder; // Thư viện tạo viền có tiêu đề cho component
import javax.swing.table.DefaultTableCellRenderer; // Thư viện tùy chỉnh cách hiển thị ô trong JTable.
import javax.swing.table.DefaultTableModel; // Thư viện là model dữ liệu cho JTable

public class TraCuu extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField txtViTriID, txtKhuVuc, txtBienSo, txtHoTen, txtSDT;
    private JComboBox<String> cbLoaiXe, cbTrangThaiForm;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;

    public TraCuu() {
        setLayout(new GridLayout(2, 1, 0, 10));
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.WHITE);
        
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204), 2), " QUẢN LÝ XE TRONG BÃI ");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        topPanel.setBorder(border);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 15);

        txtViTriID = new JTextField(10); txtViTriID.setFont(inputFont);
        txtKhuVuc = new JTextField(10); txtKhuVuc.setFont(inputFont);
        txtBienSo = new JTextField(15); txtBienSo.setFont(inputFont);
        txtHoTen = new JTextField(15); txtHoTen.setFont(inputFont);
        txtSDT = new JTextField(15); txtSDT.setFont(inputFont);
        
        cbTrangThaiForm = new JComboBox<>(new String[]{"Có xe", "Trống"}); cbTrangThaiForm.setFont(inputFont);
        cbLoaiXe = new JComboBox<>(new String[]{"Xe máy", "Ô tô", "Xe đạp"}); cbLoaiXe.setFont(inputFont);

        gbc.gridx = 0; gbc.gridy = 0; topPanel.add(createLabel("Vị trí ID:", labelFont), gbc);
        gbc.gridx = 1; topPanel.add(txtViTriID, gbc);
        gbc.gridx = 2; topPanel.add(createLabel("Khu vực:", labelFont), gbc);
        gbc.gridx = 3; topPanel.add(txtKhuVuc, gbc);

        gbc.gridx = 0; gbc.gridy = 1; topPanel.add(createLabel("Biển số:", labelFont), gbc);
        gbc.gridx = 1; topPanel.add(txtBienSo, gbc);
        gbc.gridx = 2; topPanel.add(createLabel("Tên khách hàng:", labelFont), gbc);
        gbc.gridx = 3; topPanel.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2; topPanel.add(createLabel("Số điện thoại:", labelFont), gbc);
        gbc.gridx = 1; topPanel.add(txtSDT, gbc);
        gbc.gridx = 2; topPanel.add(createLabel("Loại xe:", labelFont), gbc);
        gbc.gridx = 3; topPanel.add(cbLoaiXe, gbc);

        gbc.gridx = 0; gbc.gridy = 3; topPanel.add(createLabel("Trạng thái:", labelFont), gbc);
        gbc.gridx = 1; topPanel.add(cbTrangThaiForm, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(Color.WHITE);
       
        JButton btnSearch = createStyledButton("TÌM KIẾM", new Color(0, 102, 204));
        JButton btnUpdate = createStyledButton("CẬP NHẬT", new Color(40, 167, 69));
        JButton btnReset = createStyledButton("XÓA THÔNG TIN XE", new Color(220, 53, 69));
        JButton btnClear = createStyledButton("LÀM MỚI FORM", new Color(108, 117, 125));
        
        btnPanel.add(btnSearch); btnPanel.add(btnUpdate); 
        btnPanel.add(btnReset); btnPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        topPanel.add(btnPanel, gbc);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        String[] columns = {"Vị Trí", "Khu Vực", "Trạng Thái", "Biển Số", "Khách Hàng", "SĐT", "Loại Xe", "Giờ Vào"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        
        table.getTableHeader().setDefaultRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            JLabel lbl = new JLabel(value.toString(), JLabel.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lbl.setOpaque(true);
            lbl.setBackground(new Color(230, 235, 245));
            lbl.setForeground(Color.BLACK);
            lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));
            return lbl;
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        table.setRowHeight(32);
        table.setSelectionBackground(new Color(184, 218, 255));
        JScrollPane scrollPane = new JScrollPane(table);

        lblTotal = new JLabel("Đang có: 0 xe");
        lblTotal.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        bottomPanel.add(lblTotal, BorderLayout.SOUTH);

        add(topPanel);
        add(bottomPanel);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtViTriID.setText(getValue(row, 0));
                    txtKhuVuc.setText(getValue(row, 1));
                    cbTrangThaiForm.setSelectedItem(getValue(row, 2));
                    txtBienSo.setText(getValue(row, 3));
                    txtHoTen.setText(getValue(row, 4));
                    txtSDT.setText(getValue(row, 5));
                    if (model.getValueAt(row, 6) != null) cbLoaiXe.setSelectedItem(getValue(row, 6));
                }
            }
        });

        btnSearch.addActionListener(e -> loadDataByFields());
        btnUpdate.addActionListener(e -> updateBaiXe());
        btnReset.addActionListener(e -> resetViTri());
        btnClear.addActionListener(e -> clearForm());
       
        loadDataByFields();
    }
     
    private void loadDataByFields() {
        model.setRowCount(0);
        String vID = "%" + txtViTriID.getText().trim() + "%";
        String bSo = "%" + txtBienSo.getText().trim() + "%";
        String hTen = "%" + txtHoTen.getText().trim() + "%";
        String sdt = "%" + txtSDT.getText().trim() + "%";
        String kVuc = "%" + txtKhuVuc.getText().trim() + "%";

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM BaiXe WHERE TrangThai NOT LIKE N'Trống' "
                       + "AND ViTriID LIKE ? "
                       + "AND (BienSo LIKE ? OR BienSo IS NULL) "
                       + "AND (HoTenKH LIKE ? OR HoTenKH IS NULL) "
                       + "AND (SoDienThoai LIKE ? OR SoDienThoai IS NULL) "
                       + "AND KhuVuc LIKE ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, vID);
            pstmt.setString(2, bSo);
            pstmt.setString(3, hTen);
            pstmt.setString(4, sdt);
            pstmt.setString(5, kVuc);
            
            ResultSet rs = pstmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                model.addRow(new Object[]{
                    rs.getString("ViTriID"), rs.getString("KhuVuc"), rs.getString("TrangThai"),
                    rs.getString("BienSo"), rs.getString("HoTenKH"), rs.getString("SoDienThoai"),
                    rs.getString("LoaiXe"), rs.getTimestamp("ThoiGianVao")
                });
            }
            lblTotal.setText("Đang hiển thị: " + count + " vị trí có xe");
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void updateBaiXe() {
        if (txtViTriID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập dữ liệu để cập nhật!");
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE BaiXe SET BienSo = ?, HoTenKH = ?, SoDienThoai = ?, TrangThai = ?, LoaiXe = ? WHERE ViTriID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtBienSo.getText().trim());
            pstmt.setString(2, txtHoTen.getText().trim());
            pstmt.setString(3, txtSDT.getText().trim());
            pstmt.setString(4, cbTrangThaiForm.getSelectedItem().toString());
            pstmt.setString(5, cbLoaiXe.getSelectedItem().toString());
            pstmt.setString(6, txtViTriID.getText().trim());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataByFields();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void resetViTri() {
        if (txtViTriID.getText().isEmpty()) return;
        int resp = JOptionPane.showConfirmDialog(this, "Xác nhận xóa xe và giải phóng vị trí này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE BaiXe SET TrangThai = N'Trống', BienSo = NULL, HoTenKH = NULL, SoDienThoai = NULL, LoaiXe = NULL, ThoiGianVao = NULL WHERE ViTriID = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, txtViTriID.getText().trim());
                pstmt.executeUpdate();
                clearForm();
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    private void clearForm() {
        txtViTriID.setText(""); txtKhuVuc.setText(""); txtBienSo.setText("");
        txtHoTen.setText(""); txtSDT.setText("");
        cbTrangThaiForm.setSelectedIndex(0);
        loadDataByFields(); 
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(170, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private String getValue(int row, int col) {
        Object val = model.getValueAt(row, col);
        return val != null ? val.toString() : "";
    }
}