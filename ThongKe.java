import org.jfree.chart.ChartFactory; // Thư viện giúp tạo biểu đồ
import org.jfree.chart.ChartPanel; // Thư viện giúp nhúng biểu đồ vào Swing
import org.jfree.chart.JFreeChart; // Thư viện giúp chỉnh sửa thiết kế biểu đồ (chỉnh màu, thêm chú thích,...)
import org.jfree.chart.plot.CategoryPlot; // Thư viện giúp quản lý vùng vẽ đồ thị (chỉnh tọa độ Oxy)
import org.jfree.chart.plot.PlotOrientation; // Thư viện giúp hướng biểu đồ (hướng của cột đướng và cột ngang)
import org.jfree.chart.renderer.category.BarRenderer; // Thư viện giúp tùy chỉnh cột
import org.jfree.chart.title.LegendTitle; // Thư viện giúp thêm chú thích
import org.jfree.chart.ui.RectangleEdge; // Thư viện giúp xác định vị trí của các thành phần trong biểu đồ
import org.jfree.data.category.DefaultCategoryDataset; // Thư viện giúp căn lề trang
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat; // Thư viện giúp định dạng số
import java.util.Calendar; // Thư viện giúp xác định thời gian thực
import org.jfree.chart.ui.HorizontalAlignment; // Thư viện giúp

public class ThongKe extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel lblTongNam, lblThangCaoNhat;
    private DecimalFormat df = new DecimalFormat("#,### VNĐ");
    private DefaultCategoryDataset dataset; 

    public ThongKe() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        hienThiBieuDo();
        
        JPanel bottomWrapper = new JPanel();
        bottomWrapper.setLayout(new BoxLayout(bottomWrapper, BoxLayout.X_AXIS));
        bottomWrapper.setBackground(Color.WHITE);
        bottomWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 30));

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(Color.WHITE);

        lblTongNam = new JLabel("Tổng doanh thu năm: 0 VNĐ");
        lblTongNam.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongNam.setForeground(new Color(0, 102, 204));
        lblTongNam.setAlignmentX(Component.RIGHT_ALIGNMENT); 
        
        lblThangCaoNhat = new JLabel("Tháng cao nhất: Chưa có dữ liệu");
        lblThangCaoNhat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblThangCaoNhat.setForeground(new Color(220, 53, 69));
        lblThangCaoNhat.setAlignmentX(Component.RIGHT_ALIGNMENT); 

        statsPanel.add(lblTongNam);
        statsPanel.add(Box.createVerticalStrut(10)); 
        statsPanel.add(lblThangCaoNhat);

        bottomWrapper.add(Box.createHorizontalGlue());
        bottomWrapper.add(statsPanel);

        add(bottomWrapper, BorderLayout.SOUTH);
        loadDataFromSQL(); 
    }

    public void hienThiBieuDo() {
        dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "BIỂU ĐỒ DOANH THU NĂM " + Calendar.getInstance().get(Calendar.YEAR),
                "Tháng", "Doanh thu", dataset,
                PlotOrientation.VERTICAL, true, true, false);
        LegendTitle legend = chart.getLegend();
        if (legend != null) {
            legend.setPosition(RectangleEdge.BOTTOM);
            legend.setHorizontalAlignment(HorizontalAlignment.LEFT); 
            legend.setPadding(5, 10, 5, 5); 
        }
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 153, 76)); 
        renderer.setShadowVisible(false);
        renderer.setItemMargin(0.1); 
        plot.getDomainAxis().setCategoryMargin(0.2); 
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        add(chartPanel, BorderLayout.CENTER);
    }

    private void loadDataFromSQL() {
        int namHienTai = Calendar.getInstance().get(Calendar.YEAR);
        double[] doanhThuThang = new double[13];
        double tongNam = 0;
        double maxDoanhThu = 0;
        int thangMax = 0;
        String sql = "SELECT MONTH(ThoiGianRa) as Thang, SUM(TongTien) as DoanhThu " +
                     "FROM LichSu WHERE YEAR(ThoiGianRa) = ? " +
                     "GROUP BY MONTH(ThoiGianRa)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {           
            pstmt.setInt(1, namHienTai);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int thang = rs.getInt("Thang");
                double doanhThu = rs.getDouble("DoanhThu");
                doanhThuThang[thang] = doanhThu;
                tongNam += doanhThu;

                if (doanhThu > maxDoanhThu) {
                    maxDoanhThu = doanhThu;
                    thangMax = thang;
                }
            }
            dataset.clear();
            for (int i = 1; i <= 12; i++) {
                dataset.addValue(doanhThuThang[i], "Doanh thu", "T" + i);
            }
            lblTongNam.setText("Tổng doanh thu năm " + namHienTai + ": " + df.format(tongNam));
            if (thangMax > 0) {
                lblThangCaoNhat.setText("Tháng doanh thu cao nhất: Tháng " + thangMax + " (" + df.format(maxDoanhThu) + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}