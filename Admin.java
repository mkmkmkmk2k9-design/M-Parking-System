import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter; // Thư viện dùng để xử lý sự kiện chuột mà không cần cài đặt toàn bộ MouseListener.
import java.awt.event.MouseEvent; // Thư viện chứa thông tin của sự kiện chuột như vị trí và hành động click.
import javax.swing.event.DocumentListener; // Thư viện dùng để bắt sự kiện thay đổi nội dung của JTextField.
import javax.swing.event.DocumentEvent; // Thư viện mô tả sự thay đổi của nội dung văn bản.
import java.io.BufferedReader;// Thư viện dùng để đọc file text theo từng dòng.
import java.io.FileReader; // Thư viện dùng để đọc dữ liệu ký tự từ file.
import java.io.IOException; // Thư viện xử lý lỗi vào/ra khi thao tác với file.
import java.io.File; // Thư viện dùng để làm việc với file và thư mục trong hệ thống.

public class Admin extends JFrame {
    private static final long serialVersionUID = 1L;
    private boolean isSystemAction = false;
    public Admin() {
    	new Thread(() -> {
            try {
                DBConnection.getConnection(); 
                System.out.println("--- Database đã sẵn sàng phục vụ ---");
            } catch (Exception e) {
                System.err.println("Lỗi kết nối sớm: " + e.getMessage());
            }
        }).start();
    	
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Hệ thống quản lý bãi giữ xe");

        JLabel background = new JLabel(new ImageIcon("img/login_bg.jpg"));
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        JPanel p = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        p.setPreferredSize(new Dimension(400, 320)); 
        p.setBackground(new Color(255, 255, 255, 210)); 
        p.setOpaque(false); 
        background.add(p);

        GridBagConstraints b = new GridBagConstraints();
        b.insets = new Insets(8, 8, 8, 8); 
        b.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel title = new JLabel("ĐĂNG NHẬP HỆ THỐNG BÃI GIỮ XE", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        b.gridx = 0; b.gridy = 0; b.gridwidth = 2;
        p.add(title, b);
   
        ImageIcon uicon = new ImageIcon(new ImageIcon("img/user-interface.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JLabel juser = new JLabel(uicon);
        JTextField user = new JTextField(15);
        user.setFont(new Font("Arial", Font.PLAIN, 15));
        user.setPreferredSize(new Dimension(200, 35));
        b.gridx = 0; b.gridy = 1; b.gridwidth = 1; p.add(juser, b);
        b.gridx = 1; p.add(user, b);
 
        ImageIcon picon = new ImageIcon(new ImageIcon("img/pass.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JLabel jpass = new JLabel(picon);
        JPasswordField pass = new JPasswordField(15);
        pass.setFont(new Font("Arial", Font.PLAIN, 15));
        pass.setPreferredSize(new Dimension(200, 35));
        b.gridx = 0; b.gridy = 2; p.add(jpass, b);
        b.gridx = 1; p.add(pass, b);

        JLabel lblError = new JLabel(" ", JLabel.CENTER); 
        lblError.setForeground(new Color(220, 20, 60));
        lblError.setFont(new Font("Arial", Font.ITALIC, 13));
        lblError.setPreferredSize(new Dimension(300, 25)); 
        b.gridx = 0; b.gridy = 3; b.gridwidth = 2;
        b.insets = new Insets(0, 0, 0, 0);
        p.add(lblError, b);

        JLabel forgotPass = new JLabel("Quên mật khẩu?");
        forgotPass.setFont(new Font("Arial", Font.BOLD, 13));
        forgotPass.setForeground(new Color(26, 115, 232));
        forgotPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        b.gridx = 1; b.gridy = 4; b.gridwidth = 1;
        b.fill = GridBagConstraints.NONE;
        b.anchor = GridBagConstraints.EAST;
        b.insets = new Insets(0, 5, 10, 0);
        p.add(forgotPass, b);

        forgotPass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Fpass fp = new Fpass(Admin.this);
                fp.setVisible(true);
            }
        });
        
        JButton login = new JButton("Đăng nhập");
        login.setPreferredSize(new Dimension(160, 40));
        login.setBackground(new Color(66, 133, 244));
        login.setForeground(Color.WHITE);
        login.setFont(new Font("Arial", Font.BOLD, 15));
        login.setFocusPainted(false);
        login.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ImageIcon eicon = new ImageIcon(new ImageIcon("img/exit.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        JButton ex = new JButton(eicon);
        ex.setFocusPainted(false);
        ex.setBorderPainted(false);
        ex.setContentAreaFilled(false);
        ex.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel subp = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        subp.setOpaque(false);
        subp.add(login);
        subp.add(ex);
        
        b.gridx = 0; b.gridy = 5; b.gridwidth = 2;
        b.fill = GridBagConstraints.HORIZONTAL;
        b.insets = new Insets(10, 0, 10, 0);
        p.add(subp, b);

        login.addActionListener(e -> {
            String us = user.getText().trim();
            String pa = new String(pass.getPassword()).trim();
            String currentStoredPass = "123";
            File filePass = new File("img/admin_pass.txt");        
            
            if (filePass.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(filePass))) {
                    String line = br.readLine();
                    if (line != null) currentStoredPass = line.trim(); 
                } catch (IOException ioex) { ioex.printStackTrace(); }
            }

            if (us.isEmpty() || pa.isEmpty()) {
                lblError.setText("Vui lòng nhập đầy đủ thông tin");
            } else if (!us.equals("admin") || !pa.equals(currentStoredPass)) { 
                lblError.setText("Sai tên đăng nhập hoặc mật khẩu");
                pass.setText("");
                pass.requestFocusInWindow();
            } else {
                lblError.setText("Đang kết nối cơ sở dữ liệu, vui lòng đợi...");
                login.setEnabled(false);
                new Thread(() -> {
                    ParkForm pf = new ParkForm();                            
                    SwingUtilities.invokeLater(() -> {
                        dispose(); 
                        pf.setVisible(true);
                    });
                }).start();
            }
        });

        ex.addActionListener(e -> System.exit(0));
        pass.addActionListener(e -> login.doClick());
        user.addActionListener(e -> pass.requestFocusInWindow());

        DocumentListener hideErrorListener = new DocumentListener() {
            private void hide() {
                if (!isSystemAction && !lblError.getText().equals(" ")) {
                    lblError.setText(" ");
                    p.repaint();
                }
            }
            @Override public void insertUpdate(DocumentEvent e) { hide(); }
            @Override public void removeUpdate(DocumentEvent e) { hide(); }
            @Override public void changedUpdate(DocumentEvent e) { hide(); }
        };
        user.getDocument().addDocumentListener(hideErrorListener);
        pass.getDocument().addDocumentListener(hideErrorListener);
        
        getRootPane().setDefaultButton(login);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Admin().setVisible(true));
    }
}
