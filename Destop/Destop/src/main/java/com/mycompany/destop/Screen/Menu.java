/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.destop.Screen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.destop.DTO.ChangePassDTO;
import com.mycompany.destop.DTO.SigninDTO;
import com.mycompany.destop.DTO.UpdateUserDTO;
import com.mycompany.destop.Enum.Skill;
import com.mycompany.destop.Enum.TrangThaiLop;
import com.mycompany.destop.Modul.GiangVien;
import com.mycompany.destop.Modul.HoaDon;
import com.mycompany.destop.Modul.KhoaHoc;
import com.mycompany.destop.Modul.LopHoc;
import com.mycompany.destop.Modul.NhanVien;
import com.mycompany.destop.Modul.TaiKhoanLogin;
import com.mycompany.destop.Modul.User;

import com.mycompany.destop.Service.AWSService;
import com.mycompany.destop.Service.ApiClient;
import com.mycompany.destop.Service.DateAdapter;
import com.mycompany.destop.Service.HoaDonService;
import com.mycompany.destop.Service.KhoaHocService;
import com.mycompany.destop.Service.LopHocService;
import com.mycompany.destop.Service.TaiKhoanService;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.hibernate.boot.model.source.internal.hbm.Helper;

/**
 *
 * @author User
 */
public class Menu extends javax.swing.JFrame {

    int x = 210;    //chieu rong
    int y = 600;    //chieu cao

    String accessTokenLogin;
    SigninDTO signinDTO;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateAdapter()).create();
    private ApiClient apiClient = new ApiClient();
    private AWSService awsService = new AWSService();
    private TaiKhoanService taiKhoanService = new TaiKhoanService();
    private LopHocService lopHocService = new LopHocService();
    private KhoaHocService khoaHocService = new KhoaHocService();
    private HoaDonService hoaDonService = new HoaDonService();
    private boolean isEditModeKhoa = false;
    private boolean isEditModeLop = false;
    private boolean isEditModeHoaDon = false;
    private boolean isEditModeTaiKhoan = false;

    /**
     * Creates new form Menu
     */
    public Menu(String accessToken) throws Exception {
        initComponents();
        accessTokenLogin = accessToken;
        jplSlideMenu.setSize(210, 600);
        cardTrangChu.setVisible(true);
        cardTaiKhoan.setVisible(false);
        loadInfo();
    }

    public void loadInfo() throws Exception {
        signinDTO = apiClient.callProfileApi(accessTokenLogin);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = signinDTO.getU().getNgaySinh().format(formatter);
        jLabelId.setText(signinDTO.getU().getIdUser().toString());
        if (signinDTO.getU().isGioiTinh()) {
            jLabelGioiTinhMALE.setText("Nam");
        } else {
            jLabelGioiTinhMALE.setText("Nữ");
        }
        jLabelHoTen.setText(signinDTO.getU().getHoTen());
        jLabelEmail.setText(signinDTO.getU().getEmail());
        jLabelSoDienThoai.setText(signinDTO.getU().getSdt());
        jLabelBirthday.setText(signinDTO.getU().getNgaySinh().toString());
        jLabelAddress.setText(signinDTO.getU().getDiaChi());
        String imageUrl = signinDTO.getU().getImage();
        try {
            // Tải ảnh từ URL
            java.net.URL url = new java.net.URL(imageUrl);
            ImageIcon originalIcon = new ImageIcon(url);

            // Lấy kích thước của JLabel
            int width = jLabelImg.getWidth();
            int height = jLabelImg.getHeight();

            // Thay đổi kích thước ảnh
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            // Gắn ảnh đã thay đổi kích thước vào JLabel
            jLabelImg.setIcon(scaledIcon);
        } catch (Exception e) {
            e.printStackTrace();
            jLabelImg.setText("Không thể tải ảnh");
        }
    }

    public void loadTableDSLop() {
        try {
            // Gọi API để lấy danh sách lớp học
            ArrayList<LopHoc> danhSachLop = (ArrayList<LopHoc>) lopHocService.getAllLopHocApi(accessTokenLogin);

            // Tạo model cho JTable
            DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                    new String[]{
                        "Tên Lớp", "Trạng thái", "Tên giảng viên", "Số học viên", "Ngày bắt đầu",
                        "Ngày kết thúc", "Khóa Học", "Tùy Chỉnh", "Delete"
                    }) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Chỉ cho phép chỉnh sửa cột "Tùy Chỉnh" và "Delete"
                    return column == 7 || column == 8;
                }
            };

            // Thêm dữ liệu vào model
            for (LopHoc lop : danhSachLop) {
                SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"); // Định dạng ngày giờ với microsecond
                SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày xuất ra

                String ngayBDFormatted = ""; // Khởi tạo biến để chứa ngày đã định dạng
                String ngayKTFormatted = ""; // Khởi tạo biến để chứa ngày đã định dạng

                try {
                    // Kiểm tra nếu ngày bắt đầu không phải là null
                    if (lop.getNgayBD() != null) {
                        ngayBDFormatted = sdfOutput.format(lop.getNgayBD()); // Định dạng ngày bắt đầu
                    }

                    // Kiểm tra nếu ngày kết thúc không phải là null
                    if (lop.getNgayKT() != null) {
                        ngayKTFormatted = sdfOutput.format(lop.getNgayKT()); // Định dạng ngày kết thúc
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Xử lý ngoại lệ nếu có
                }

                model.addRow(new Object[]{
                    lop.getTenLopHoc(),
                    lop.getTrangThai(),
                    lop.getGiangVien().getHoTen(),
                    lop.getSoHocVien(),
                    lop.getKhoaHoc().getTenKhoaHoc(),
                    ngayBDFormatted, // Ngày bắt đầu đã được định dạng lại
                    ngayKTFormatted,
                    "Tùy Chỉnh", // Nút "Tùy Chỉnh"
                    "Delete" // Nút "Delete"
                }
                );
            }

            // Gắn model vào JTable
            jTabDSLop.setModel(model);

            // Đặt chiều cao dòng
            jTabDSLop.setRowHeight(30);

            // Thêm renderer cho các cột nút
            jTabDSLop.getColumn("Tùy Chỉnh").setCellRenderer(new ButtonRenderer());
            jTabDSLop.getColumn("Delete").setCellRenderer(new ButtonRenderer());

            // Thêm editor cho các cột nút
            jTabDSLop.getColumn("Tùy Chỉnh").setCellEditor(new ButtonEditorLop(new JButton("Tùy Chỉnh"), "info", jTabDSLop));
            jTabDSLop.getColumn("Delete").setCellEditor(new ButtonEditorLop(new JButton("Delete"), "delete", jTabDSLop));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ButtonEditorLop extends DefaultCellEditor {

        private JButton button;
        private String label;
        private boolean clicked;
        private String action;
        private JTable table;

        public ButtonEditorLop(JButton button, String action, JTable table) {
            super(new JTextField());
            this.button = button;
            this.action = action;
            this.table = table;
            button.addActionListener(e -> performAction());
        }

        private void performAction() {
            int row = table.getSelectedRow(); // Lấy dòng hiện tại
            Object tenLop = table.getValueAt(row, 0); // Lấy giá trị tên lớp
            Object id = table.getValueAt(row, 0); // (Nếu cần thêm ID, bạn phải truyền qua API)

            if ("info".equals(action)) {
                // Xử lý nút "Tùy Chỉnh"
                JOptionPane.showMessageDialog(button, "Xem thông tin lớp học: " + tenLop);
            } else if ("delete".equals(action)) {
                // Xử lý nút "Delete"
                int confirm = JOptionPane.showConfirmDialog(button, "Bạn có chắc muốn xóa lớp: " + tenLop + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Gọi API để xóa lớp (thêm hàm deleteLopHoc nếu cần)
                    JOptionPane.showMessageDialog(button, "Đã xóa lớp: " + tenLop);
                }
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = value != null ? value.toString() : "";
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }

    public void loadTableTaiKhoan() {
        try {
            // Gọi API để lấy danh sách tài khoản
            ArrayList<TaiKhoanLogin> danhSachTaiKhoan = (ArrayList<TaiKhoanLogin>) taiKhoanService.getAllTKhoanApi(accessTokenLogin);

            // Tạo model cho JTable
            DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                    new String[]{
                        "Id", "Tên đăng nhập", "Họ tên", "Số điện thoại", "Email", "Địa chỉ",
                        "Giới tính", "Ngày sinh", "Role", "Active", "Xem Thông tin", "Delete"
                    }) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Chỉ cho phép chỉnh sửa cột "Xem Thông tin" và "Delete"
                    return column == 10 || column == 11;
                }
            };

            // Thêm dữ liệu vào model
            for (TaiKhoanLogin tk : danhSachTaiKhoan) {
                model.addRow(new Object[]{
                    tk.getId(),
                    tk.getTenDangNhap(),
                    tk.getUser().getHoTen(),
                    tk.getUser().getSdt(),
                    tk.getUser().getEmail(),
                    tk.getUser().getDiaChi(),
                    tk.getUser().isGioiTinh(),
                    tk.getUser().getNgaySinh().toString(),
                    tk.getRole(),
                    tk.getEnable(),
                    "Xem Thông tin", // Nút "Xem Thông tin"
                    "Delete" // Nút "Delete"
                });
            }

            // Gắn model vào JTable
            jTableTK.setModel(model);

            // Đặt chiều cao dòng
            jTableTK.setRowHeight(30);

            // Thêm renderer cho các cột nút
            jTableTK.getColumn("Xem Thông tin").setCellRenderer(new ButtonRenderer());
            jTableTK.getColumn("Delete").setCellRenderer(new ButtonRenderer());

            // Thêm editor cho các cột nút
            jTableTK.getColumn("Xem Thông tin").setCellEditor(new ButtonEditor(new JButton("Xem "), "info", jTableTK));
            jTableTK.getColumn("Delete").setCellEditor(new ButtonEditor(new JButton("Delete"), "delete", jTableTK));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

// Class ButtonRenderer
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }
    }

// Class ButtonEditor
    class ButtonEditor extends DefaultCellEditor {

        private JButton button;
        private String label;
        private boolean clicked;
        private String action;
        private JTable table;

        public ButtonEditor(JButton button, String action, JTable table) {
            super(new JTextField());
            this.button = button;
            this.action = action;
            this.table = table;
            button.addActionListener(e -> performAction());
        }

        private void performAction() {
            int row = table.getSelectedRow(); // Lấy dòng hiện tại
            Object id = table.getValueAt(row, 0); // Lấy giá trị cột ID

            if ("info".equals(action)) {
                // Xử lý nút "Xem Thông tin"
                JOptionPane.showMessageDialog(button, "Xem thông tin tài khoản ID: " + id);
            } else if ("delete".equals(action)) {
                // Xử lý nút "Delete"
                int confirm = JOptionPane.showConfirmDialog(button, "Bạn có chắc muốn xóa tài khoản ID: " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Gọi API để xóa tài khoản (thêm hàm deleteAccount nếu cần)
                    JOptionPane.showMessageDialog(button, "Đã xóa tài khoản ID: " + id);
                }
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = value != null ? value.toString() : "";
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }

    public void loadTableKhoa() {
        try {
            // Gọi API để lấy danh sách khóa học
            ArrayList<KhoaHoc> danhSachKhoaHoc = (ArrayList<KhoaHoc>) khoaHocService.getAllKhoaHocApi(accessTokenLogin);

            // Tạo model cho JTable
            DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                    new String[]{
                        "ID", "Tên Khóa", "Giá", "Thời gian diễn ra", "Số buổi", "Mô tả", "Trạng thái",
                        "Xem thông tin", "Delete"
                    }) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Chỉ cho phép chỉnh sửa cột "Xem Thông tin" và "Delete"
                    return column == 7 || column == 8;
                }
            };

            // Thêm dữ liệu vào model
            for (KhoaHoc khoaHoc : danhSachKhoaHoc) {
                model.addRow(new Object[]{
                    khoaHoc.getIdKhoaHoc(),
                    khoaHoc.getTenKhoaHoc(),
                    khoaHoc.getGiaTien(),
                    khoaHoc.getThoiGianDienRa(),
                    khoaHoc.getSoBuoi(),
                    khoaHoc.getMoTa(),
                    khoaHoc.getTrangThai() ? "Hoạt động" : "Không hoạt động", // Trạng thái
                    "Xem Thông tin", // Nút "Xem Thông tin"
                    "Delete" // Nút "Delete"
                });
            }

            // Gắn model vào JTable
            jTableKhoa.setModel(model);

            // Đặt chiều cao dòng
            jTableKhoa.setRowHeight(30);

            // Thêm renderer và editor cho các cột nút
            TableColumn xemColumn = jTableKhoa.getColumn("Xem thông tin");
            TableColumn deleteColumn = jTableKhoa.getColumn("Delete");

            xemColumn.setCellRenderer(new ButtonRenderer());
            deleteColumn.setCellRenderer(new ButtonRenderer());

            xemColumn.setCellEditor(new ButtonEditorKhoa(new JButton("Xem"), "info", jTableKhoa));
            deleteColumn.setCellEditor(new ButtonEditorKhoa(new JButton("Delete"), "delete", jTableKhoa));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ButtonEditorKhoa extends DefaultCellEditor {

        private JButton button;
        private String action;
        private JTable table;

        public ButtonEditorKhoa(JButton button, String action, JTable table) {
            super(new JTextField());
            this.button = button;
            this.action = action;
            this.table = table;

            // Lắng nghe sự kiện khi nhấn nút
            button.addActionListener(e -> performAction());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText(value == null ? "" : value.toString()); // Cập nhật text của nút
            return button;
        }

        private void performAction() {
            int row = table.getSelectedRow(); // Lấy dòng hiện tại
            if (row == -1) {
                return; // Nếu không có dòng nào được chọn thì thoát
            }
            Object id = table.getValueAt(row, 0); // Lấy giá trị cột ID
            Long idKhoa = Long.parseLong(id.toString());
            if ("info".equals(action)) {
                // Xử lý nút "Xem Thông tin"
//                JOptionPane.showMessageDialog(button, "Xem thông tin tài khoản ID: " + id);

                try {
                    KhoaHoc khoaHoc = khoaHocService.loadKhoaHocById(accessTokenLogin, idKhoa);
                    showDialogKhoa(khoaHoc);
                    loadTableKhoa();

                } catch (Exception ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if ("delete".equals(action)) {
                // Xử lý nút "Delete"
                int confirm = JOptionPane.showConfirmDialog(button, "Bạn có chắc muốn xóa tài khoản ID: " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        KhoaHoc khoaHocXoa = khoaHocService.deleteKhoaHocApi(accessTokenLogin, idKhoa);
                        JOptionPane.showMessageDialog(button, "Đã xóa tài khoản ID: " + id);
                        loadTableKhoa();
                    } catch (Exception ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            fireEditingStopped(); // Kết thúc chế độ chỉnh sửa
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }

    public void loadTableHoaDon() {
        try {
            // Gọi API để lấy danh sách hóa đơn
            ArrayList<HoaDon> danhSachHoaDon = (ArrayList<HoaDon>) hoaDonService.getAllHoaDonApi(accessTokenLogin);

            // Tạo model cho JTable
            DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                    new String[]{
                        "ID", "Ngày Lập", "Người Lập", "Thành Tiền", "Delete", "Xem"
                    }) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Chỉ cho phép chỉnh sửa cột "Delete" và "Xem"
                    return column == 5 || column == 6;
                }
            };

            // Thêm dữ liệu vào model
            for (HoaDon hoaDon : danhSachHoaDon) {
                SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"); // Định dạng ngày giờ với microsecond
                SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày xuất ra

                String ngayLapFormatted = ""; // Khởi tạo biến để chứa ngày đã định dạng
                try {
                    // Kiểm tra nếu ngày bắt đầu không phải là null
                    if (hoaDon.getNgayLap() != null) {
                        ngayLapFormatted = sdfOutput.format(hoaDon.getNgayLap()); // Định dạng ngày bắt đầu
                    }

                } catch (Exception e) {
                    e.printStackTrace(); // Xử lý ngoại lệ nếu có
                }

                model.addRow(new Object[]{
                    hoaDon.getIdHoaDon(),
                    ngayLapFormatted,
                    hoaDon.getNguoiLap().getHoTen(), // Người lập hóa đơn
                    hoaDon.getThanhTien(), // Thành tiền
                    //                    hoaDon.getTrangThai()? "Đã thanh toán" : "Chưa thanh toán", // Trạng thái
                    "Delete", // Nút "Delete"
                    "Xem" // Nút "Xem"
                });
            }

            // Gắn model vào JTable
            jTableHoaDon.setModel(model);

            // Đặt chiều cao dòng
            jTableHoaDon.setRowHeight(30);

            // Thêm renderer cho các cột nút
            jTableHoaDon.getColumn("Xem").setCellRenderer(new ButtonRenderer());
            jTableHoaDon.getColumn("Delete").setCellRenderer(new ButtonRenderer());

            // Thêm editor cho các cột nút
            jTableHoaDon.getColumn("Xem").setCellEditor(new ButtonEditor(new JButton("Xem"), "info", jTableHoaDon));
            jTableHoaDon.getColumn("Delete").setCellEditor(new ButtonEditor(new JButton("Delete"), "delete", jTableHoaDon));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     */
    public void openMenu() {
        jplSlideMenu.setSize(x, y);
        if (x == 0) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i <= 210; i++) {
                            jplSlideMenu.setSize(i, y);
                            Thread.sleep(1);
                        }
                        //
                    } catch (Exception e) {
                    }
                }
            }).start();
            x = 210;
//          
        }
    }

    public void closeMenu() {
        jplSlideMenu.setSize(x, y);
        if (x == 210) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 210; i >= 0; i--) {
                            jplSlideMenu.setSize(i, y);
                            Thread.sleep(2);
                        }
                    } catch (Exception e) {
                    }
                }
            }).start();
            x = 0;
        }
    }

    private void createInfoDialog() {
//        var isImageLoaded = false;
        // Tạo JDialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Thay đổi thông tin");
        dialog.setSize(450, 600);
        dialog.setLayout(new BorderLayout(10, 10)); // Sử dụng BorderLayout
        dialog.setLocationRelativeTo(null);

        // Panel chính cho các trường
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Tạo viền bên trong
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Họ tên:"), gbc);
        JTextField txtName = new JTextField(signinDTO.getU().getHoTen());
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(txtName, gbc);

        // Giới tính
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Giới tính:"), gbc);
        JCheckBox chkMale = new JCheckBox("Nam");
        chkMale.setSelected(signinDTO.getU().isGioiTinh());
        gbc.gridx = 1;
        mainPanel.add(chkMale, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Email:"), gbc);
        JTextField txtEmail = new JTextField(signinDTO.getU().getEmail());
        gbc.gridx = 1;
        mainPanel.add(txtEmail, gbc);

        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("SĐT:"), gbc);
        JTextField txtPhone = new JTextField(signinDTO.getU().getSdt());
        gbc.gridx = 1;
        mainPanel.add(txtPhone, gbc);

        // Ngày sinh
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Ngày sinh:"), gbc);
        com.toedter.calendar.JDateChooser dateChooser = new com.toedter.calendar.JDateChooser();
        dateChooser.setDate(java.sql.Date.valueOf(signinDTO.getU().getNgaySinh()));
        gbc.gridx = 1;
        mainPanel.add(dateChooser, gbc);

        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Địa chỉ:"), gbc);
        JTextField txtAddress = new JTextField(signinDTO.getU().getDiaChi());
        gbc.gridx = 1;
        mainPanel.add(txtAddress, gbc);

        // Tải ảnh
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Hình ảnh:"), gbc);
        JButton btnUpload = new JButton("Tải ảnh");
        gbc.gridx = 1;
        mainPanel.add(btnUpload, gbc);

        // Hiển thị ảnh
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel lblDisplayImage = new JLabel("", JLabel.CENTER); // Căn giữa nội dung
        lblDisplayImage.setPreferredSize(new Dimension(100, 100));
        lblDisplayImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(lblDisplayImage, gbc);

        dialog.add(mainPanel, BorderLayout.CENTER);

        // Panel cho các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnSave = new JButton("Lưu");
        btnSave.setPreferredSize(new Dimension(100, 35));
        btnSave.setBackground(new Color(0, 204, 0));
        btnSave.setForeground(Color.WHITE);

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(100, 35));
        btnCancel.setBackground(new Color(204, 0, 0));
        btnCancel.setForeground(Color.WHITE);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        // Hiển thị dialog
        dialog.setVisible(true);

        // Xử lý sự kiện tải ảnh
        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(file.getAbsolutePath()).getImage()
                        .getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                imageIcon.setDescription(file.getAbsolutePath());
                lblDisplayImage.setIcon(imageIcon);

            }
        });

        // Xử lý sự kiện lưu và hủy
        btnSave.addActionListener(e -> {
            // Chuyển đổi LocalDate sang java.util.Date
//            java.util.Date dob = java.sql.Date.valueOf(((com.toedter.calendar.JDateChooser) dateChooser).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//            System.out.println('1'+dateChooser.getDate().toString());
            // Gọi phương thức cập nhật thông tin người dùng
            boolean isSuccess = UpdateInfo(txtName, chkMale, txtEmail, txtPhone, dateChooser, txtAddress, lblDisplayImage, awsService, accessTokenLogin, signinDTO.getU().getIdUser(), signinDTO.getU().getImage());
            if (isSuccess) {
//                JOptionPane.showMessageDialog(dialog, "Cập nhật thông tin thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                try {
                    loadInfo();
                } catch (Exception ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.dispose(); // Đóng dialog nếu cập nhật thành công
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
    }

    private boolean UpdateInfo(JTextField txtName, JCheckBox chkMale, JTextField txtEmail, JTextField txtPhone,
            com.toedter.calendar.JDateChooser dateChooser, JTextField txtAddress, JLabel lblDisplayImage,
            AWSService awsService, String token, Long nhanVienId, String oldImageUrl) {
        // Kiểm tra trường rỗng
        if (txtName.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()
                || txtPhone.getText().trim().isEmpty() || txtAddress.getText().trim().isEmpty()
                || dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
//        System.out.println(dateChooser.getDate());
        // Kiểm tra định dạng email (chỉ chấp nhận email kết thúc là .gmail.com)
        String emailPattern = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        if (!txtEmail.getText().matches(emailPattern)) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ! (Chỉ chấp nhận .gmail.com)", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra định dạng số điện thoại (bắt đầu bằng số 0 và có 9 chữ số)
        String phonePattern = "^0\\d{9}$"; // Số điện thoại bắt đầu bằng 0 và có 9 chữ số
        if (!txtPhone.getText().matches(phonePattern)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ! (Chỉ chấp nhận 0 và 9 chữ số)", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra ngày sinh hợp lệ
        if (dateChooser.getDate().after(new java.util.Date())) {
            JOptionPane.showMessageDialog(null, "Ngày sinh không được lớn hơn ngày hiện tại!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String newImageUrl = signinDTO.getU().getImage(); // Mặc định là ảnh cũ
//        System.out.println(lblDisplayImage.getIcon());
        if (lblDisplayImage.getIcon() != null) {
            try {
//                  System.out.println(lblDisplayImage.getIcon());
                // Kiểm tra và lấy đường dẫn từ ImageIcon
                ImageIcon icon = (ImageIcon) lblDisplayImage.getIcon();
                String localFilePath = icon.getDescription();
                System.out.println(localFilePath);// Phải được đặt trước đó
                if (localFilePath == null || localFilePath.trim().isEmpty()) {
                    throw new IOException("Đường dẫn ảnh không hợp lệ!");
                }

                // Thêm ngày tháng vào tên file để tránh trùng lặp
                String timeStamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
                String fileName = "images/" + timeStamp + ".jpg";

                // Upload ảnh lên AWS S3
                newImageUrl = awsService.uploadImage(localFilePath, fileName);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Không thể tải ảnh lên AWS: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Nếu có ảnh cũ, xóa ảnh cũ trên AWS (chỉ khi ảnh mới được upload thành công)
        if (oldImageUrl != null && !oldImageUrl.isEmpty() && !oldImageUrl.equals(newImageUrl)) {
            try {
                awsService.deleteImage(oldImageUrl);
            } catch (Exception e) {
                // In ra log lỗi, không dừng tiến trình
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Không thể xóa ảnh cũ trên AWS: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Gọi API cập nhật thông tin Nhân viên
        try {
            NhanVien nhanVien = new NhanVien();
            nhanVien.setHoTen(txtName.getText().trim());
            nhanVien.setGioiTinh(chkMale.isSelected());
            nhanVien.setEmail(txtEmail.getText().trim());
            nhanVien.setSdt(txtPhone.getText().trim());
            Date date = dateChooser.getDate();
//            System.out.println("date: " + date);
            try {
                LocalDate localDate;
                localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                nhanVien.setNgaySinh(localDate); // Gán vào đối tượng
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi chuyển đổi ngày: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            nhanVien.setDiaChi(txtAddress.getText().trim());
            System.out.println(newImageUrl);
            nhanVien.setImage(newImageUrl);
            System.out.println(nhanVien.getNgaySinh().toString());
            // Gọi API update
            NhanVien result = apiClient.UpdateNhanVien(accessTokenLogin, signinDTO.getU().getIdUser(), nhanVien); // Gọi hàm cập nhật API
            if (result != null) {
                JOptionPane.showMessageDialog(null, "Cập nhật thông tin thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật thông tin thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void changePass(String token) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Đổi mật khẩu");
        dialog.setSize(450, 300);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

// Tạo panel chính
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL; // Đảm bảo các thành phần chiếm hết chiều rộng

// Mật khẩu cũ
        JLabel lblOldPass = new JLabel("Mật khẩu cũ:");
        JPasswordField txtOldPass = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lblOldPass, gbc);

        gbc.gridx = 1;
        mainPanel.add(txtOldPass, gbc);

// Mật khẩu mới
        JLabel lblNewPass = new JLabel("Mật khẩu mới:");
        JPasswordField txtNewPass = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(lblNewPass, gbc);

        gbc.gridx = 1;
        mainPanel.add(txtNewPass, gbc);

// Nhập lại mật khẩu mới
        JLabel lblConfirmNewPass = new JLabel("Nhập lại mật khẩu:");
        JPasswordField txtConfirmNewPass = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(lblConfirmNewPass, gbc);

        gbc.gridx = 1;
        mainPanel.add(txtConfirmNewPass, gbc);

// Thêm panel chính vào dialog
        dialog.add(mainPanel, BorderLayout.CENTER);

// Nút xác nhận và hủy
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnConfirm = new JButton("Xác nhận");
        JButton btnCancel = new JButton("Hủy");

// Chỉnh giao diện cho nút
        btnConfirm.setPreferredSize(new Dimension(150, 40));
        btnConfirm.setBackground(new Color(0, 204, 0)); // Màu nền nút Xác nhận
        btnConfirm.setForeground(Color.WHITE); // Màu chữ nút Xác nhận
        btnConfirm.setFont(new Font("Arial", Font.BOLD, 14)); // Font chữ
        btnConfirm.setFocusPainted(false); // Không hiển thị viền khi nhấn
        btnConfirm.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2)); // Viền màu xanh

        btnCancel.setPreferredSize(new Dimension(150, 40));
        btnCancel.setBackground(new Color(204, 0, 0)); // Màu nền nút Hủy
        btnCancel.setForeground(Color.WHITE); // Màu chữ nút Hủy
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14)); // Font chữ
        btnCancel.setFocusPainted(false); // Không hiển thị viền khi nhấn
        btnCancel.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // Viền màu đỏ

        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Sự kiện nút Hủy
        btnCancel.addActionListener(e -> dialog.dispose());

        // Sự kiện nút Xác nhận
        btnConfirm.addActionListener(e -> {
            String oldPass = new String(txtOldPass.getPassword());
            String newPass = new String(txtNewPass.getPassword());
            String confirmNewPass = new String(txtConfirmNewPass.getPassword());

            // Kiểm tra rỗng
            if (oldPass.isEmpty() || newPass.isEmpty() || confirmNewPass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng không để trống bất kỳ trường nào!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra mật khẩu mới hợp lệ
            if (!newPass.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$")) {
                JOptionPane.showMessageDialog(dialog, "Mật khẩu mới phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra nhập lại mật khẩu
            if (!newPass.equals(confirmNewPass)) {
                JOptionPane.showMessageDialog(dialog, "Mật khẩu nhập lại không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo đối tượng ChangePassDTO
            ChangePassDTO passDTO = new ChangePassDTO();
            passDTO.setOldPass(oldPass);
            passDTO.setNewPass(newPass);

            // Gọi hàm changePasswordFromClient
            boolean success = apiClient.changePasswordFromClient(token, passDTO);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thất bại. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Hiển thị dialog
        dialog.setVisible(true);
    }

    public void showDialogKhoa(KhoaHoc khoaHoc) {
        JDialog dialogKhoa = new JDialog();
        dialogKhoa.setTitle("Nhập thông tin Khóa Học");
        dialogKhoa.setSize(600, 700);
        dialogKhoa.setLayout(new BorderLayout(10, 10));
        dialogKhoa.setLocationRelativeTo(null);

        // Panel chính
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tên khóa học
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Tên khóa học:"), gbc);
        JTextField txtTenKhoaHoc = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(txtTenKhoaHoc, gbc);

        // Giá tiền
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Giá tiền (VNĐ):"), gbc);
        JTextField txtGiaTien = new JTextField();
        gbc.gridx = 1;
        mainPanel.add(txtGiaTien, gbc);

        // Thời gian diễn ra
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Thời gian diễn ra:"), gbc);
        JDateChooser dateChooser = new JDateChooser();
        gbc.gridx = 1;
        mainPanel.add(dateChooser, gbc);

        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Trạng thái:"), gbc);
        JCheckBox chkTrangThai = new JCheckBox("Hoạt động");
        if (khoaHoc == null) {
            chkTrangThai.setSelected(true);
            chkTrangThai.setEnabled(false);
        }
        gbc.gridx = 1;
        mainPanel.add(chkTrangThai, gbc);

        // Số buổi
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Số buổi:"), gbc);
        JTextField txtSoBuoi = new JTextField();
        gbc.gridx = 1;
        mainPanel.add(txtSoBuoi, gbc);

        // Mô tả
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Mô tả:"), gbc);
        JTextArea txtMoTa = new JTextArea(5, 20);
//        JScrollPane scrollPaneMoTa = new JScrollPane(txtMoTa);
        gbc.gridx = 1;
        mainPanel.add(txtMoTa, gbc);

        // Chọn kỹ năng
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Kỹ năng:"), gbc);

// Tạo danh sách các kỹ năng
        String[] skills = {"LISTEN", "REAL", "WRITE", "SPEAK"};

// Tạo JComboBox cho phép chọn nhiều kỹ năng
        JCheckBox[] skillCheckBoxes = new JCheckBox[skills.length];
//        JPanel comboPanel = new JPanel(new GridLayout(0, 1)); // Panel chứa các CheckBox
        JPanel jpSkill = new JPanel();
        for (int i = 0; i < skills.length; i++) {
            skillCheckBoxes[i] = new JCheckBox(skills[i]);
            jpSkill.add(skillCheckBoxes[i]);
        }

//// Thêm vào giao diện chính
        gbc.gridx = 1;
        mainPanel.add(jpSkill, gbc);

        // Hình ảnh
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Hình ảnh:"), gbc);
        JButton btnUploadImage = new JButton("Tải ảnh");
        gbc.gridx = 1;
        mainPanel.add(btnUploadImage, gbc);

        // Hiển thị ảnh
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel lblDisplayImageKhoa = new JLabel("", JLabel.CENTER);
        lblDisplayImageKhoa.setPreferredSize(new Dimension(100, 100));
        lblDisplayImageKhoa.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(lblDisplayImageKhoa, gbc);

        dialogKhoa.add(mainPanel, BorderLayout.CENTER);

        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnSaveKhoa = new JButton("Lưu");
        if (khoaHoc != null) {
            txtTenKhoaHoc.setText(khoaHoc.getTenKhoaHoc());
            txtMoTa.setText(khoaHoc.getMoTa());
            txtGiaTien.setText(khoaHoc.getGiaTien().toString());
            txtSoBuoi.setText(khoaHoc.getSoBuoi().toString());
            if (khoaHoc.getThoiGianDienRa() != null) {
                dateChooser.setDate(java.sql.Date.valueOf(khoaHoc.getThoiGianDienRa()));
            }
            chkTrangThai.setSelected(khoaHoc.getTrangThai());
            // Xử lý set ảnh (nếu có ảnh)
            if (khoaHoc.getImage() != null) {
                try {
                    // Tạo URL từ link lưu trữ trên AWS
                    URL imageUrl = new URL(khoaHoc.getImage());
                    // Mở InputStream để đọc dữ liệu ảnh
                    InputStream inputStream = imageUrl.openStream();
                    // Tạo ImageIcon từ URL và chỉnh kích thước ảnh
                    ImageIcon imageIcon = new ImageIcon(new ImageIcon(imageUrl)
                            .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    lblDisplayImageKhoa.setIcon(imageIcon);
                } catch (Exception e) {
                    e.printStackTrace();
                    lblDisplayImageKhoa.setIcon(null); // Nếu có lỗi (ví dụ: URL không hợp lệ)
                }
            } else {
                lblDisplayImageKhoa.setIcon(null); // Nếu ảnh là null hoặc rỗng
            }

            // Xử lý so sánh và set checkbox (cho skillEnum)
            ArrayList<Skill> listSkills = khoaHoc.getSkillEnum();
            for (int i = 0; i < skillCheckBoxes.length; i++) {
                JCheckBox checkBox = skillCheckBoxes[i];
                checkBox.setSelected(false); // Mặc định bỏ chọn trước

                if (listSkills != null) {
                    for (Skill skill : listSkills) {
                        if (checkBox.getText().equals(skill.toString())) { // So sánh tên skill
                            checkBox.setSelected(true); // Chọn checkbox nếu khớp
                            break;
                        }
                    }
                }
            }

            btnSaveKhoa.setText("Cập nhật");
        }
        btnSaveKhoa.setPreferredSize(new Dimension(100, 35));
        btnSaveKhoa.setBackground(new Color(0, 204, 0));
        btnSaveKhoa.setForeground(Color.WHITE);

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(100, 35));
        btnCancel.setBackground(new Color(204, 0, 0));
        btnCancel.setForeground(Color.WHITE);

        buttonPanel.add(btnSaveKhoa);
        buttonPanel.add(btnCancel);

        dialogKhoa.add(buttonPanel, BorderLayout.SOUTH);

        // Xử lý nút tải ảnh
        btnUploadImage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(dialogKhoa);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(file.getAbsolutePath()).getImage()
                        .getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                imageIcon.setDescription(file.getAbsolutePath());
                lblDisplayImageKhoa.setIcon(imageIcon);

            }
        });
        // Xử lý nút lưu
        btnSaveKhoa.addActionListener(e -> {
            Long idKhoa;
            String img;
            if (khoaHoc != null) {
                idKhoa = khoaHoc.getIdKhoaHoc();
                img = khoaHoc.getImage();
            } else {
                idKhoa = null;
                img = null;
            }
            boolean update = themKhoa(accessTokenLogin, lblDisplayImageKhoa, txtTenKhoaHoc, txtGiaTien, dateChooser, txtSoBuoi, txtMoTa, skillCheckBoxes, chkTrangThai, idKhoa, img);
            if (update) {
                try {
                    loadTableKhoa();
                } catch (Exception ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialogKhoa.dispose();
            }
        });

        // Xử lý nút hủy
        btnCancel.addActionListener(e -> dialogKhoa.dispose());

        // Hiển thị dialog
        dialogKhoa.setVisible(true);
    }
    private String[] getDanhSachGiangVien (){
         try {
            ArrayList<User> list = (ArrayList<User>) apiClient.getAllGiangVienLamViec(accessTokenLogin);
             return list.stream()
            .map(User::getHoTen) // Lấy tên giảng viên
            .toArray(String[]::new);
        } catch (Exception ex) {
           
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
             return null;
        }
    }
    private String[] getDanhSachKhoaHoc (){
        try {
            ArrayList<KhoaHoc>list = (ArrayList<KhoaHoc>)khoaHocService.getAllKhoaHocApi(accessTokenLogin);
             return list.stream()
            .map(KhoaHoc::getTenKhoaHoc) // Lấy tên giảng viên
            .toArray(String[]::new);
        } catch (Exception ex) {
           
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
             return null;
        }
    }
    public void showDialogLop(LopHoc lop) {
        JDialog dialogLop = new JDialog();
        dialogLop.setTitle("Nhập thông tin Lớp Học");
        dialogLop.setSize(600, 700);
        dialogLop.setLayout(new BorderLayout(10, 10));
        dialogLop.setLocationRelativeTo(null);

        // Panel chính
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tên lớp học
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Tên lớp học:"), gbc);
        JTextField txtTenLopHoc = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(txtTenLopHoc, gbc);

        // Số học viên
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Số học viên:"), gbc);
        JTextField txtSoHocVien = new JTextField();
        gbc.gridx = 1;
        mainPanel.add(txtSoHocVien, gbc);

        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Trạng thái:"), gbc);
        JCheckBox chkTrangThai = new JCheckBox("Hoạt động");
        if (lop == null) {
            chkTrangThai.setSelected(true);
        }
        gbc.gridx = 1;
        mainPanel.add(chkTrangThai, gbc);

        // Ngày bắt đầu
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Ngày bắt đầu:"), gbc);
        JDateChooser dateChooserNgayBD = new JDateChooser();
        gbc.gridx = 1;
        mainPanel.add(dateChooserNgayBD, gbc);

        // Ngày kết thúc
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Ngày kết thúc:"), gbc);
        JDateChooser dateChooserNgayKT = new JDateChooser();
        gbc.gridx = 1;
        mainPanel.add(dateChooserNgayKT, gbc);

        // Giảng viên
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Giảng viên:"), gbc);
        JComboBox<String> cbGiangVien = new JComboBox<>(getDanhSachGiangVien()); // Lấy danh sách giảng viên
        gbc.gridx = 1;
        mainPanel.add(cbGiangVien, gbc);

        // Khóa học
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Khóa học:"), gbc);
        JComboBox<String> cbKhoaHoc = new JComboBox<>(getDanhSachKhoaHoc()); // Lấy danh sách khóa học
        gbc.gridx = 1;
        mainPanel.add(cbKhoaHoc, gbc);

        // Mô tả
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Mô tả:"), gbc);
        JTextArea txtMoTa = new JTextArea(5, 20);
        gbc.gridx = 1;
        mainPanel.add(txtMoTa, gbc);

        dialogLop.add(mainPanel, BorderLayout.CENTER);

        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnSaveLop = new JButton("Lưu");
        if (lop != null) {
            txtTenLopHoc.setText(lop.getTenLopHoc());
            txtSoHocVien.setText(lop.getSoHocVien() != null ? lop.getSoHocVien().toString() : "");
//            chkTrangThai.setSelected(TrangThaiLop.READY == lop.getTrangThai());
            if (lop.getNgayBD() != null) {
                dateChooserNgayBD.setDate(lop.getNgayBD());
            }
            if (lop.getNgayKT() != null) {
                dateChooserNgayKT.setDate(lop.getNgayKT());
            }
            cbGiangVien.setSelectedItem(lop.getGiangVien() != null ? lop.getGiangVien().getHoTen(): null);
            cbKhoaHoc.setSelectedItem(lop.getKhoaHoc() != null ? lop.getKhoaHoc().getTenKhoaHoc() : null);
            txtMoTa.setText(lop.getMoTa());
            btnSaveLop.setText("Cập nhật");
        }
        btnSaveLop.setPreferredSize(new Dimension(100, 35));
        btnSaveLop.setBackground(new Color(0, 204, 0));
        btnSaveLop.setForeground(Color.WHITE);

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(100, 35));
        btnCancel.setBackground(new Color(204, 0, 0));
        btnCancel.setForeground(Color.WHITE);

        buttonPanel.add(btnSaveLop);
        buttonPanel.add(btnCancel);

        dialogLop.add(buttonPanel, BorderLayout.SOUTH);

        // Xử lý nút lưu
        btnSaveLop.addActionListener(e -> {
            boolean success =true;
            if (success) {
                try {
                    loadTableDSLop();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                dialogLop.dispose();
            }
        });

        // Xử lý nút hủy
        btnCancel.addActionListener(e -> dialogLop.dispose());

        // Hiển thị dialog
        dialogLop.setVisible(true);
    }

    public ArrayList<Skill> getSelectedSkills(JCheckBox[] checkBoxes) {
        ArrayList<Skill> selectedSkills = new ArrayList<>();

        // Duyệt qua các checkbox, nếu được chọn thì thêm vào danh sách
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {
                selectedSkills.add(Skill.values()[i]); // Thêm Skill tương ứng từ enum
            }
        }
        return selectedSkills;
    }

    private boolean themKhoa(String token, JLabel lblDisplayImageKhoa, JTextField txtTenKhoaHoc, JTextField txtGiaTien, JDateChooser dateChooser, JTextField txtSoBuoi, JTextArea txtMoTa, JCheckBox[] skillCheckBoxes, JCheckBox chkTrangThai, Long id, String img) {
        String tenKhoaHoc = txtTenKhoaHoc.getText().trim();
        String giaTien = txtGiaTien.getText().trim();
        Date thoiGianDienRa = dateChooser.getDate();
        String soBuoi = txtSoBuoi.getText().trim();
        String moTa = txtMoTa.getText().trim();
        System.out.println("mota " + moTa);
        Boolean trangThai = chkTrangThai.isSelected();
        ArrayList<Skill> selectedSkills = getSelectedSkills(skillCheckBoxes);
        System.out.println("selectedSkills " + selectedSkills);
        // Kiểm tra rỗng và các điều kiện khác như trước
        if (tenKhoaHoc.isEmpty() || giaTien.toString().isEmpty()
                || soBuoi.toString().isEmpty() || moTa.toString().isEmpty()
                || thoiGianDienRa == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (thoiGianDienRa == null) {
            JOptionPane.showMessageDialog(null, "Thời gian diễn ra không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (selectedSkills.size() == 0) {
            JOptionPane.showMessageDialog(null, "Kĩ năng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Date currentDate = new Date();
        if (thoiGianDienRa.before(currentDate)) {
            JOptionPane.showMessageDialog(null, "Thời gian diễn ra phải là ngày trong tương lai!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String giaTienPattern = "\\d+";
        if (!giaTien.matches(giaTienPattern)) {
            JOptionPane.showMessageDialog(null, "Giá tiền phải là số nguyên hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String soBuoiPattern = "\\d+";
        if (!soBuoi.matches(soBuoiPattern)) {
            JOptionPane.showMessageDialog(null, "Số buổi phải là số nguyên hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        KhoaHoc khoaHoc = new KhoaHoc();
        if (id != null) {
            khoaHoc.setIdKhoaHoc(id);
        }
        khoaHoc.setTenKhoaHoc(tenKhoaHoc);
        try {
            khoaHoc.setGiaTien(Long.parseLong(giaTien));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá tiền phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            khoaHoc.setSoBuoi(Long.parseLong(soBuoi));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Số buổi phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

// Kiểm tra nếu Icon là kiểu ImageIcon\
        String oldImageUrl = img;
        String newImageUrl = oldImageUrl;

        if (lblDisplayImageKhoa.getIcon() != null) {
            try {
                // Lấy thông tin từ icon
                Icon icon = lblDisplayImageKhoa.getIcon();
                if (icon instanceof ImageIcon) {
                    ImageIcon imageIcon = (ImageIcon) icon;
                    String localFilePath = imageIcon.getDescription(); // Đường dẫn file ảnh gốc

                    // Kiểm tra nếu không chọn ảnh mới
                    if (localFilePath == null || localFilePath.isEmpty()) {
                        khoaHoc.setImage(oldImageUrl);
                    } else {
                        // Nếu chọn ảnh mới, thêm ngày tháng vào tên file để tránh trùng lặp
                        String timeStamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
                        String fileName = "images/" + timeStamp + ".jpg";

                        // Tải ảnh lên AWS S3
                        newImageUrl = awsService.uploadImage(localFilePath, fileName);
                        if (newImageUrl == null) {
                            JOptionPane.showMessageDialog(null, "Không thể tải ảnh lên AWS", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return false;
                        } else {
                            khoaHoc.setImage(newImageUrl);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy đường dẫn ảnh", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Không thể tải ảnh lên AWS: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Cần chọn ít nhất một ảnh", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

// Xóa ảnh cũ trên AWS nếu có ảnh mới được tải lên thành công
        if (oldImageUrl != null && oldImageUrl != newImageUrl) {
            try {
                awsService.deleteImage(oldImageUrl);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Không thể xóa ảnh cũ trên AWS: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

//        return true;
        SimpleDateFormat sdfKhoa = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdfKhoa.format(thoiGianDienRa);
        khoaHoc.setThoiGianDienRa(formattedDate);
        khoaHoc.setSkillEnum(selectedSkills);
        khoaHoc.setTrangThai(trangThai);
        khoaHoc.setMoTa(moTa);
//        System.out.println("anh"+khoaHoc.getImage());
        try {
            boolean result = khoaHocService.createKhoaHocFromClient(token, khoaHoc);
            if (result) {
                JOptionPane.showMessageDialog(null, "Cập nhật khóa học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật khóa học thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi trong quá trình cập nhật khóa học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void showDialogGioiThieu() {
        // Tạo một JDialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Thông tin về EYL");

        // Tạo JTextArea chứa văn bản
        JTextArea textArea = new JTextArea();
        textArea.setText("EYL - Ứng dụng quản lý đa nền tảng dành cho Trung tâm dạy tiếng Anh\n\n"
                + "Thời đại 4.0 đã mang đến nhiều bước ngoặt mới trong xã hội, dễ nhận thấy nhất là sự xuất hiện của nhiều công ty nước ngoài mở chi nhánh và \"cắm rễ\" tại Việt Nam. Điều này tạo ra nhiều cơ hội việc làm cho người dân nói chung và các bạn trẻ nói riêng.\n\n"
                + "Tuy nhiên, thực tế cho thấy, nhiều bạn trẻ vừa học vừa làm hoặc những nhân viên văn phòng thường gặp khó khăn về thời gian. Đôi khi, việc tăng ca ngoài giờ (OT) khiến họ không thể tham gia đầy đủ các buổi học tại trung tâm, dẫn đến việc mất kiến thức trong các buổi đó. Bên cạnh đó, một số trường hợp như không thể tiếp thu toàn bộ kiến thức do giáo viên truyền đạt hoặc không ghi chép đầy đủ nội dung bài học cũng thường xuyên xảy ra. Đặc biệt, với những bạn ngại giao tiếp, gặp vấn đề nhưng không dám hỏi trực tiếp thầy cô, việc học càng trở nên khó khăn hơn.\n\n"
                + "Những vấn đề nhỏ nhặt này có thể khiến người học không đạt được kết quả như mong đợi sau khi kết thúc khóa học tại trung tâm. Về phía trung tâm tiếng Anh, luôn có mong muốn mang đến trải nghiệm học tập và tối ưu hóa kiến thức tốt nhất cho các học viên đã tin tưởng lựa chọn trung tâm.\n\n"
                + "Vì vậy, EYL - Ứng dụng quản lý đa nền tảng dành cho trung tâm dạy tiếng Anh đã ra đời để giải quyết những khó khăn \"khó nói thành lời\" ở trên.\n\n"
                + "EYL không chỉ giúp giảng viên quản lý lớp học và nội dung bài giảng, mà còn hỗ trợ trung tâm quản lý hồ sơ cùng nhiều nhu cầu thiết yếu khác. Đặc biệt, ứng dụng còn tích hợp chatbot, cho phép học viên giải đáp thắc mắc ngay lập tức mà không cần phải đến trung tâm. Hơn thế nữa, EYL cung cấp nhiều chức năng giúp học viên tối ưu hóa kiến thức một cách hiệu quả nhất.");

        // Định dạng cho JTextArea
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setCaretPosition(0); // Đưa con trỏ về đầu
        textArea.setEditable(false); // Không cho phép chỉnh sửa
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(new Color(240, 240, 240));

        // Tạo JScrollPane để có thể cuộn văn bản
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400)); // Kích thước JScrollPane

        // Tạo nút OK để đóng dialog
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(e -> dialog.dispose()); // Đóng dialog khi nhấn OK

        // Tạo một JPanel để chứa JScrollPane và JButton
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnOK, BorderLayout.SOUTH);

        // Thêm JPanel vào dialog
        dialog.add(panel);

        // Thiết lập dialog
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(null); // Đặt vị trí ở giữa màn hình
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jplFilnal = new javax.swing.JPanel();
        jplSlideMenu = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblCloseMenu = new javax.swing.JLabel();
        jLabelThongKe = new javax.swing.JLabel();
        lblTrangChu = new javax.swing.JLabel();
        lblTaiKhoan = new javax.swing.JLabel();
        jLabelHoaDon = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelDangXuat = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabelLopHoc = new javax.swing.JLabel();
        jLabelKhoaHoc = new javax.swing.JLabel();
        jpllMenuBar = new javax.swing.JPanel();
        lblOpenMenu = new javax.swing.JLabel();
        jplTitle = new javax.swing.JPanel();
        jplMain = new javax.swing.JPanel();
        cardTaiKhoan = new javax.swing.JPanel();
        jLabelMenuTK = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTK = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jTextSearchTK = new javax.swing.JTextField();
        jBtThemTK = new javax.swing.JButton();
        jComSearchTK = new javax.swing.JComboBox<>();
        cardTrangChu = new javax.swing.JPanel();
        jLabelImg = new javax.swing.JLabel();
        jPanelName = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabelId = new javax.swing.JLabel();
        jPanelBirthday = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabelBirthday = new javax.swing.JLabel();
        jPanelGioiTinh = new javax.swing.JPanel();
        jLabelGioiTinhMALE = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanelAddress = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabelAddress = new javax.swing.JLabel();
        jPanelNgaySinh = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabelHoTen = new javax.swing.JLabel();
        jPanelSoDienThoai = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabelSoDienThoai = new javax.swing.JLabel();
        jPanelEmail = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelTrangChu = new javax.swing.JLabel();
        jLabelInfo = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButtonChangeInfo = new javax.swing.JButton();
        jButtonChangPass = new javax.swing.JButton();
        cardLopHoc = new javax.swing.JPanel();
        jLabTextLop = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabDSLop = new javax.swing.JTable();
        jBtTimLop = new javax.swing.JButton();
        jTextTimLop = new javax.swing.JTextField();
        jComTimLop = new javax.swing.JComboBox<>();
        jBtThemLop = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTabHocVien = new javax.swing.JTable();
        cardKhoaHoc = new javax.swing.JPanel();
        jLabText = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableKhoa = new javax.swing.JTable();
        jButTimKhoa = new javax.swing.JButton();
        jTextTimKhoa = new javax.swing.JTextField();
        jBntKhoaHoc = new javax.swing.JButton();
        cardHoaDon = new javax.swing.JPanel();
        jLabelTextHD = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableHoaDon = new javax.swing.JTable();
        jBntTimHoaDon = new javax.swing.JButton();
        jTextTimHoaDon = new javax.swing.JTextField();
        jBntThemHoaDon = new javax.swing.JButton();
        jComHoaDon = new javax.swing.JComboBox<>();
        cardThongKe = new javax.swing.JPanel();
        jLabelTextThongKe = new javax.swing.JLabel();
        jBtDoanhSo = new javax.swing.JButton();
        jComDoanhSo = new javax.swing.JComboBox<>();
        jBtDiemSo = new javax.swing.JButton();
        jComDiemSo = new javax.swing.JComboBox<>();
        jBtSoHocVien = new javax.swing.JButton();
        jComSoHocVien = new javax.swing.JComboBox<>();
        jButDiemTest = new javax.swing.JButton();
        jComDiemTest = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jplFilnal.setBackground(new java.awt.Color(255, 255, 255));
        jplFilnal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jplSlideMenu.setBackground(new java.awt.Color(255, 255, 255));
        jplSlideMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jplSlideMenu.setPreferredSize(new java.awt.Dimension(190, 590));
        jplSlideMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/avatar.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("EYL");

        lblCloseMenu.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lblCloseMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCloseMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/close.png"))); // NOI18N
        lblCloseMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCloseMenuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCloseMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jLabel4)))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCloseMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap())
        );

        jplSlideMenu.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 150));

        jLabelThongKe.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabelThongKe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelThongKe.setText("Thống Kê");
        jLabelThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelThongKeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelThongKeMouseEntered(evt);
            }
        });
        jplSlideMenu.add(jLabelThongKe, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 210, 30));

        lblTrangChu.setBackground(new java.awt.Color(255, 255, 255));
        lblTrangChu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblTrangChu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTrangChu.setText("Trang Chủ");
        lblTrangChu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTrangChuMouseClicked(evt);
            }
        });
        jplSlideMenu.add(lblTrangChu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 210, 30));

        lblTaiKhoan.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblTaiKhoan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTaiKhoan.setText("Tài Khoản");
        lblTaiKhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTaiKhoanMouseClicked(evt);
            }
        });
        jplSlideMenu.add(lblTaiKhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 210, 30));

        jLabelHoaDon.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabelHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHoaDon.setText("Hóa Đơn");
        jLabelHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelHoaDonMouseClicked(evt);
            }
        });
        jplSlideMenu.add(jLabelHoaDon, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 210, 30));
        jplSlideMenu.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 452, 210, 10));

        jLabelDangXuat.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabelDangXuat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDangXuat.setText("Đăng Xuất");
        jLabelDangXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelDangXuatMouseClicked(evt);
            }
        });
        jplSlideMenu.add(jLabelDangXuat, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 210, 30));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Giới Thiệu");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });
        jplSlideMenu.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 510, 210, 30));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Trợ Giúp");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });
        jplSlideMenu.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 210, 30));

        jLabelLopHoc.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabelLopHoc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLopHoc.setText("Lớp Học");
        jLabelLopHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelLopHocMouseClicked(evt);
            }
        });
        jplSlideMenu.add(jLabelLopHoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 210, 30));

        jLabelKhoaHoc.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabelKhoaHoc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelKhoaHoc.setText("Khóa Học");
        jLabelKhoaHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelKhoaHocMouseClicked(evt);
            }
        });
        jplSlideMenu.add(jLabelKhoaHoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 210, 30));

        jplFilnal.add(jplSlideMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 0, 600));

        jpllMenuBar.setBackground(new java.awt.Color(255, 255, 255));

        lblOpenMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/menu.png"))); // NOI18N
        lblOpenMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblOpenMenuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpllMenuBarLayout = new javax.swing.GroupLayout(jpllMenuBar);
        jpllMenuBar.setLayout(jpllMenuBarLayout);
        jpllMenuBarLayout.setHorizontalGroup(
            jpllMenuBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpllMenuBarLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblOpenMenu)
                .addContainerGap(890, Short.MAX_VALUE))
        );
        jpllMenuBarLayout.setVerticalGroup(
            jpllMenuBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpllMenuBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblOpenMenu)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jplFilnal.add(jpllMenuBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 940, 60));

        jplTitle.setBackground(new java.awt.Color(0, 204, 204));

        javax.swing.GroupLayout jplTitleLayout = new javax.swing.GroupLayout(jplTitle);
        jplTitle.setLayout(jplTitleLayout);
        jplTitleLayout.setHorizontalGroup(
            jplTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 990, Short.MAX_VALUE)
        );
        jplTitleLayout.setVerticalGroup(
            jplTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jplFilnal.add(jplTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 30));

        jplMain.setBackground(new java.awt.Color(255, 255, 255));
        jplMain.setLayout(new java.awt.CardLayout());

        cardTaiKhoan.setBackground(new java.awt.Color(255, 255, 255));

        jLabelMenuTK.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabelMenuTK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMenuTK.setText("Tài Khoản");

        jTableTK.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Tên đăng nhập", "Họ tên", "Số điện thoại", "Email", "Địa chỉ", "Giới tính", "Ngày sinh", "Role", "Active", "Xem Thông tin", "Delete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableTK);

        jButton2.setBackground(new java.awt.Color(51, 51, 255));
        jButton2.setForeground(new java.awt.Color(153, 255, 255));
        jButton2.setText("Tìm kiếm");
        jButton2.setName(""); // NOI18N

        jBtThemTK.setBackground(new java.awt.Color(0, 0, 255));
        jBtThemTK.setForeground(new java.awt.Color(255, 255, 255));
        jBtThemTK.setText("Thêm tài khoản");

        jComSearchTK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Tên", "Chức vụ" }));

        javax.swing.GroupLayout cardTaiKhoanLayout = new javax.swing.GroupLayout(cardTaiKhoan);
        cardTaiKhoan.setLayout(cardTaiKhoanLayout);
        cardTaiKhoanLayout.setHorizontalGroup(
            cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardTaiKhoanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                        .addComponent(jLabelMenuTK, javax.swing.GroupLayout.PREFERRED_SIZE, 929, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))
                    .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                        .addComponent(jTextSearchTK, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComSearchTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBtThemTK)
                        .addGap(47, 47, 47))))
            .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 937, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        cardTaiKhoanLayout.setVerticalGroup(
            cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelMenuTK, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComSearchTK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBtThemTK, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextSearchTK, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton2.getAccessibleContext().setAccessibleName("jButtonSearch");
        jBtThemTK.getAccessibleContext().setAccessibleName("jButtonAdd");

        jplMain.add(cardTaiKhoan, "card3");

        cardTrangChu.setBackground(new java.awt.Color(255, 255, 255));

        jLabelImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/avatar.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Id: ");

        jLabelId.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelId.setText("11");

        javax.swing.GroupLayout jPanelNameLayout = new javax.swing.GroupLayout(jPanelName);
        jPanelName.setLayout(jPanelNameLayout);
        jPanelNameLayout.setHorizontalGroup(
            jPanelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelId, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelNameLayout.setVerticalGroup(
            jPanelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelId, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabelId.getAccessibleContext().setAccessibleName("jLabelName");

        jPanelBirthday.setPreferredSize(new java.awt.Dimension(446, 60));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Ngày sinh: ");

        jLabelBirthday.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelBirthday.setText("jLabel15");

        javax.swing.GroupLayout jPanelBirthdayLayout = new javax.swing.GroupLayout(jPanelBirthday);
        jPanelBirthday.setLayout(jPanelBirthdayLayout);
        jPanelBirthdayLayout.setHorizontalGroup(
            jPanelBirthdayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBirthdayLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jLabelBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanelBirthdayLayout.setVerticalGroup(
            jPanelBirthdayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBirthdayLayout.createSequentialGroup()
                .addGroup(jPanelBirthdayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 2, Short.MAX_VALUE))
        );

        jLabelBirthday.getAccessibleContext().setAccessibleName("jLabelBirtday");

        jLabelGioiTinhMALE.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelGioiTinhMALE.setText("Nam");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("Giới tính: ");

        javax.swing.GroupLayout jPanelGioiTinhLayout = new javax.swing.GroupLayout(jPanelGioiTinh);
        jPanelGioiTinh.setLayout(jPanelGioiTinhLayout);
        jPanelGioiTinhLayout.setHorizontalGroup(
            jPanelGioiTinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelGioiTinhLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelGioiTinhMALE, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
        );
        jPanelGioiTinhLayout.setVerticalGroup(
            jPanelGioiTinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabelGioiTinhMALE, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setText("Địa chỉ: ");

        jLabelAddress.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelAddress.setText("jLabel24");

        javax.swing.GroupLayout jPanelAddressLayout = new javax.swing.GroupLayout(jPanelAddress);
        jPanelAddress.setLayout(jPanelAddressLayout);
        jPanelAddressLayout.setHorizontalGroup(
            jPanelAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelAddressLayout.setVerticalGroup(
            jPanelAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddressLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(jLabelAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabelAddress.getAccessibleContext().setAccessibleName("jLabelAddress");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("Họ tên: ");

        jLabelHoTen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelHoTen.setText("jLabel15");

        javax.swing.GroupLayout jPanelNgaySinhLayout = new javax.swing.GroupLayout(jPanelNgaySinh);
        jPanelNgaySinh.setLayout(jPanelNgaySinhLayout);
        jPanelNgaySinhLayout.setHorizontalGroup(
            jPanelNgaySinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNgaySinhLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel21)
                .addGap(18, 18, 18)
                .addComponent(jLabelHoTen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelNgaySinhLayout.setVerticalGroup(
            jPanelNgaySinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNgaySinhLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelNgaySinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(jLabelHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabelHoTen.getAccessibleContext().setAccessibleName("jLabelBirthday");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setText("Số điện thoại: ");

        jLabelSoDienThoai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelSoDienThoai.setText("jLabel26");

        javax.swing.GroupLayout jPanelSoDienThoaiLayout = new javax.swing.GroupLayout(jPanelSoDienThoai);
        jPanelSoDienThoai.setLayout(jPanelSoDienThoaiLayout);
        jPanelSoDienThoaiLayout.setHorizontalGroup(
            jPanelSoDienThoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSoDienThoaiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSoDienThoai, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelSoDienThoaiLayout.setVerticalGroup(
            jPanelSoDienThoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSoDienThoaiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSoDienThoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSoDienThoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelSoDienThoaiLayout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jLabelSoDienThoai.getAccessibleContext().setAccessibleName("jLabelSDT");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("Email: ");

        jLabelEmail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelEmail.setText("jLabel28");

        javax.swing.GroupLayout jPanelEmailLayout = new javax.swing.GroupLayout(jPanelEmail);
        jPanelEmail.setLayout(jPanelEmailLayout);
        jPanelEmailLayout.setHorizontalGroup(
            jPanelEmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEmailLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelEmailLayout.setVerticalGroup(
            jPanelEmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEmailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(jLabelEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabelEmail.getAccessibleContext().setAccessibleName("jLabelEmail");

        jLabelTrangChu.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabelTrangChu.setText("TRANG CHỦ");

        jLabelInfo.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabelInfo.setText("THÔNG TIN CÁ NHÂN");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jButtonChangeInfo.setBackground(new java.awt.Color(0, 204, 204));
        jButtonChangeInfo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButtonChangeInfo.setForeground(new java.awt.Color(0, 102, 102));
        jButtonChangeInfo.setText("Thay đổi thông tin cá nhân");
        jButtonChangeInfo.setDisplayedMnemonicIndex(2);
        jButtonChangeInfo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jButtonChangeInfoMouseMoved(evt);
            }
        });
        jButtonChangeInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonChangeInfoMouseClicked(evt);
            }
        });
        jButtonChangeInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangeInfoActionPerformed(evt);
            }
        });

        jButtonChangPass.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonChangPass.setForeground(new java.awt.Color(0, 153, 153));
        jButtonChangPass.setText("Đổi mật khẩu");
        jButtonChangPass.setDisplayedMnemonicIndex(1);
        jButtonChangPass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonChangPassMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jButtonChangeInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonChangPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonChangeInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonChangPass, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jButtonChangeInfo.getAccessibleContext().setAccessibleName("jButtonChangeInfo");

        javax.swing.GroupLayout cardTrangChuLayout = new javax.swing.GroupLayout(cardTrangChu);
        cardTrangChu.setLayout(cardTrangChuLayout);
        cardTrangChuLayout.setHorizontalGroup(
            cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardTrangChuLayout.createSequentialGroup()
                .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, cardTrangChuLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardTrangChuLayout.createSequentialGroup()
                                .addComponent(jPanelBirthday, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanelAddress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(cardTrangChuLayout.createSequentialGroup()
                                .addComponent(jPanelName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(cardTrangChuLayout.createSequentialGroup()
                                .addComponent(jLabelTrangChu)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelImg, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(113, 113, 113)
                                .addComponent(jLabelInfo))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, cardTrangChuLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(cardTrangChuLayout.createSequentialGroup()
                                .addComponent(jPanelSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(34, 34, 34))
        );
        cardTrangChuLayout.setVerticalGroup(
            cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTrangChuLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelImg, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelGioiTinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelBirthday, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelSoDienThoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jplMain.add(cardTrangChu, "card2");

        cardLopHoc.setBackground(new java.awt.Color(0, 153, 102));

        jLabTextLop.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabTextLop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabTextLop.setText("Lớp Học");

        jTabDSLop.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Tên Lớp", "Trạng thái", "Tên giảng viên", "Số học viên", "Ngày bắt đầu", "Ngày kết thúc", "Khóa Học", "Tùy Chỉnh", "Delete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTabDSLop);

        jBtTimLop.setText("Tìm kiếm");
        jBtTimLop.setName(""); // NOI18N

        jComTimLop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "IDKhoa", "Tên Lớp", "Tên Khóa", " " }));
        jComTimLop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComTimLopActionPerformed(evt);
            }
        });

        jBtThemLop.setText("Thêm Lớp Học");
        jBtThemLop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBtThemLopMouseClicked(evt);
            }
        });

        jTabHocVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "STT", "Tên học viên", "Delete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTabHocVien);

        javax.swing.GroupLayout cardLopHocLayout = new javax.swing.GroupLayout(cardLopHoc);
        cardLopHoc.setLayout(cardLopHocLayout);
        cardLopHocLayout.setHorizontalGroup(
            cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabTextLop, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(cardLopHocLayout.createSequentialGroup()
                .addComponent(jTextTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBtThemLop, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(cardLopHocLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        cardLopHocLayout.setVerticalGroup(
            cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardLopHocLayout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabTextLop, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBtThemLop, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBtTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                    .addComponent(jScrollPane6)))
        );

        jplMain.add(cardLopHoc, "card3");

        cardKhoaHoc.setBackground(new java.awt.Color(255, 255, 255));

        jLabText.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabText.setText("Khóa Học");

        jTableKhoa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Tên Khóa", "Giá", "Thời gian diễn ra", "Số buổi ", "Mô tả", "Trang thái", "Xem thông tin", "Delete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTableKhoa);

        jButTimKhoa.setText("Tìm kiếm");
        jButTimKhoa.setName(""); // NOI18N

        jBntKhoaHoc.setText("Thêm khóa học");
        jBntKhoaHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBntKhoaHocMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout cardKhoaHocLayout = new javax.swing.GroupLayout(cardKhoaHoc);
        cardKhoaHoc.setLayout(cardKhoaHocLayout);
        cardKhoaHocLayout.setHorizontalGroup(
            cardKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardKhoaHocLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardKhoaHocLayout.createSequentialGroup()
                        .addComponent(jTextTimKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButTimKhoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 320, Short.MAX_VALUE)
                        .addComponent(jBntKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jLabText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jScrollPane3)
        );
        cardKhoaHocLayout.setVerticalGroup(
            cardKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardKhoaHocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabText)
                .addGap(18, 18, 18)
                .addGroup(cardKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextTimKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBntKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButTimKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jplMain.add(cardKhoaHoc, "card3");

        cardHoaDon.setBackground(new java.awt.Color(255, 255, 255));

        jLabelTextHD.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabelTextHD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTextHD.setText("Hóa Đơn");

        jTableHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Ngày Lập", "Người Lập", "Thành Tiền", "Delete", "Xem"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTableHoaDon);

        jBntTimHoaDon.setText("Tìm kiếm");
        jBntTimHoaDon.setName(""); // NOI18N
        jBntTimHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBntTimHoaDonActionPerformed(evt);
            }
        });

        jBntThemHoaDon.setText("Thêm Hoá Đơn");

        jComHoaDon.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "IDNhân Viên" }));
        jComHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cardHoaDonLayout = new javax.swing.GroupLayout(cardHoaDon);
        cardHoaDon.setLayout(cardHoaDonLayout);
        cardHoaDonLayout.setHorizontalGroup(
            cardHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextTimHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBntTimHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(184, 184, 184)
                .addComponent(jBntThemHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane4)
            .addComponent(jLabelTextHD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardHoaDonLayout.setVerticalGroup(
            cardHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTextHD)
                .addGap(18, 18, 18)
                .addGroup(cardHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(cardHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextTimHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBntThemHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBntTimHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jplMain.add(cardHoaDon, "card3");

        cardThongKe.setBackground(new java.awt.Color(255, 255, 255));

        jLabelTextThongKe.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabelTextThongKe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTextThongKe.setText("Thống Kê");

        jBtDoanhSo.setText("Doanh Số");

        jComDoanhSo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Theo năm", "Theo tháng", " ", " " }));

        jBtDiemSo.setText("Điểm bài tập");

        jComDiemSo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Theo Lớp", "Theo Khóa", " " }));

        jBtSoHocVien.setText("Số học viên");

        jComSoHocVien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Theo lớp", "Theo khóa", " " }));

        jButDiemTest.setText("Điểm bài Test");
        jButDiemTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButDiemTestActionPerformed(evt);
            }
        });

        jComDiemTest.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Theo Lớp", "Theo Khóa", " " }));
        jComDiemTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComDiemTestActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cardThongKeLayout = new javax.swing.GroupLayout(cardThongKe);
        cardThongKe.setLayout(cardThongKeLayout);
        cardThongKeLayout.setHorizontalGroup(
            cardThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelTextThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(cardThongKeLayout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addGroup(cardThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jBtSoHocVien, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtDoanhSo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cardThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComDoanhSo, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComSoHocVien, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 218, Short.MAX_VALUE)
                .addGroup(cardThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(cardThongKeLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jBtDiemSo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComDiemSo, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cardThongKeLayout.createSequentialGroup()
                        .addComponent(jButDiemTest, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComDiemTest, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(130, 130, 130))
        );
        cardThongKeLayout.setVerticalGroup(
            cardThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardThongKeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelTextThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71)
                .addGroup(cardThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComDoanhSo, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBtDoanhSo, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                    .addGroup(cardThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jBtDiemSo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComDiemSo, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(96, 96, 96)
                .addGroup(cardThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButDiemTest, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(cardThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComDiemTest, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComSoHocVien, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBtSoHocVien, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(120, 120, 120))
        );

        jplMain.add(cardThongKe, "card3");

        jplFilnal.add(jplMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 990, 540));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jplFilnal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jplFilnal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblCloseMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMenuMouseClicked
        closeMenu();
        jButtonChangeInfo.setEnabled(true);
        jBtDoanhSo.setEnabled(true);
        jBtSoHocVien.setEnabled(true);
    }//GEN-LAST:event_lblCloseMenuMouseClicked

    private void lblOpenMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOpenMenuMouseClicked
        openMenu();
        jButtonChangeInfo.setEnabled(false);
        jBtDoanhSo.setEnabled(false);
        jBtSoHocVien.setEnabled(false);
    }//GEN-LAST:event_lblOpenMenuMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        jplSlideMenu.setSize(0, y);
        x = 0;
    }//GEN-LAST:event_formWindowOpened

    private void lblTrangChuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTrangChuMouseClicked
        cardTrangChu.setVisible(true);
        cardTaiKhoan.setVisible(false);
        cardHoaDon.setVisible(false);
        cardKhoaHoc.setVisible(false);
        cardLopHoc.setVisible(false);
        cardThongKe.setVisible(false);
        jButtonChangPass.setEnabled(true);
        try {
            loadInfo();
        } catch (Exception ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_lblTrangChuMouseClicked

    private void lblTaiKhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTaiKhoanMouseClicked
        cardTrangChu.setVisible(false);
        cardTaiKhoan.setVisible(true);
        cardHoaDon.setVisible(false);
        cardKhoaHoc.setVisible(false);
        cardLopHoc.setVisible(false);
        cardThongKe.setVisible(false);
        loadTableTaiKhoan();
    }//GEN-LAST:event_lblTaiKhoanMouseClicked

    private void jButtonChangeInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChangeInfoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButtonChangeInfoActionPerformed

    private void jButtonChangPassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonChangPassMouseClicked
        // TODO add your handling code here:
        changePass(accessTokenLogin);
    }//GEN-LAST:event_jButtonChangPassMouseClicked

    private void jLabelDangXuatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelDangXuatMouseClicked
        // TODO add your handling code here:
        DangNhap loginFrame = new DangNhap();
        loginFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabelDangXuatMouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        // TODO add your handling code here:
        showDialogGioiThieu();
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        // TODO add your handling code here:
        jplFilnal.setComponentZOrder(jplSlideMenu, 0);
        JOptionPane.showMessageDialog(null, "Chức năng chưa được phát triển mong người dùng thông cảm!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jButtonChangeInfoMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonChangeInfoMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonChangeInfoMouseMoved

    private void jButtonChangeInfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonChangeInfoMouseClicked
        // TODO add your handling code here:
        createInfoDialog();
    }//GEN-LAST:event_jButtonChangeInfoMouseClicked

    private void jComTimLopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComTimLopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComTimLopActionPerformed

    private void jButDiemTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButDiemTestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButDiemTestActionPerformed

    private void jComDiemTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComDiemTestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComDiemTestActionPerformed

    private void jBntTimHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBntTimHoaDonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBntTimHoaDonActionPerformed

    private void jComHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComHoaDonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComHoaDonActionPerformed

    private void jLabelLopHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelLopHocMouseClicked
        // TODO add your handling code here:
        cardTrangChu.setVisible(false);
        cardTaiKhoan.setVisible(false);
        cardHoaDon.setVisible(false);
        cardKhoaHoc.setVisible(false);
        cardLopHoc.setVisible(true);
        cardThongKe.setVisible(false);
        loadTableDSLop();
    }//GEN-LAST:event_jLabelLopHocMouseClicked

    private void jLabelKhoaHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelKhoaHocMouseClicked
        // TODO add your handling code here:
        cardTrangChu.setVisible(false);
        cardTaiKhoan.setVisible(false);
        cardHoaDon.setVisible(false);
        cardKhoaHoc.setVisible(true);
        cardLopHoc.setVisible(false);
        cardThongKe.setVisible(false);
        loadTableKhoa();
    }//GEN-LAST:event_jLabelKhoaHocMouseClicked

    private void jLabelHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelHoaDonMouseClicked
        // TODO add your handling code here:
        cardTrangChu.setVisible(false);
        cardTaiKhoan.setVisible(false);
        cardHoaDon.setVisible(true);
        cardKhoaHoc.setVisible(false);
        cardLopHoc.setVisible(false);
        cardThongKe.setVisible(false);
        loadTableHoaDon();
    }//GEN-LAST:event_jLabelHoaDonMouseClicked

    private void jLabelThongKeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelThongKeMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelThongKeMouseEntered

    private void jLabelThongKeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelThongKeMouseClicked
        // TODO add your handling code here:
        cardTrangChu.setVisible(false);
        cardTaiKhoan.setVisible(false);
        cardHoaDon.setVisible(false);
        cardKhoaHoc.setVisible(false);
        cardLopHoc.setVisible(false);
        cardThongKe.setVisible(true);
    }//GEN-LAST:event_jLabelThongKeMouseClicked

    private void jBntKhoaHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBntKhoaHocMouseClicked
        // TODO add your handling code here:
        showDialogKhoa(null);
    }//GEN-LAST:event_jBntKhoaHocMouseClicked

    private void jBtThemLopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBtThemLopMouseClicked
        // TODO add your handling code here:
        showDialogLop(null);
    }//GEN-LAST:event_jBtThemLopMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Menu(null).setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cardHoaDon;
    private javax.swing.JPanel cardKhoaHoc;
    private javax.swing.JPanel cardLopHoc;
    private javax.swing.JPanel cardTaiKhoan;
    private javax.swing.JPanel cardThongKe;
    private javax.swing.JPanel cardTrangChu;
    private javax.swing.JButton jBntKhoaHoc;
    private javax.swing.JButton jBntThemHoaDon;
    private javax.swing.JButton jBntTimHoaDon;
    private javax.swing.JButton jBtDiemSo;
    private javax.swing.JButton jBtDoanhSo;
    private javax.swing.JButton jBtSoHocVien;
    private javax.swing.JButton jBtThemLop;
    private javax.swing.JButton jBtThemTK;
    private javax.swing.JButton jBtTimLop;
    private javax.swing.JButton jButDiemTest;
    private javax.swing.JButton jButTimKhoa;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonChangPass;
    private javax.swing.JButton jButtonChangeInfo;
    private javax.swing.JComboBox<String> jComDiemSo;
    private javax.swing.JComboBox<String> jComDiemTest;
    private javax.swing.JComboBox<String> jComDoanhSo;
    private javax.swing.JComboBox<String> jComHoaDon;
    private javax.swing.JComboBox<String> jComSearchTK;
    private javax.swing.JComboBox<String> jComSoHocVien;
    private javax.swing.JComboBox<String> jComTimLop;
    private javax.swing.JLabel jLabText;
    private javax.swing.JLabel jLabTextLop;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelAddress;
    private javax.swing.JLabel jLabelBirthday;
    private javax.swing.JLabel jLabelDangXuat;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelGioiTinhMALE;
    private javax.swing.JLabel jLabelHoTen;
    private javax.swing.JLabel jLabelHoaDon;
    private javax.swing.JLabel jLabelId;
    private javax.swing.JLabel jLabelImg;
    private javax.swing.JLabel jLabelInfo;
    private javax.swing.JLabel jLabelKhoaHoc;
    private javax.swing.JLabel jLabelLopHoc;
    private javax.swing.JLabel jLabelMenuTK;
    private javax.swing.JLabel jLabelSoDienThoai;
    private javax.swing.JLabel jLabelTextHD;
    private javax.swing.JLabel jLabelTextThongKe;
    private javax.swing.JLabel jLabelThongKe;
    private javax.swing.JLabel jLabelTrangChu;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelAddress;
    private javax.swing.JPanel jPanelBirthday;
    private javax.swing.JPanel jPanelEmail;
    private javax.swing.JPanel jPanelGioiTinh;
    private javax.swing.JPanel jPanelName;
    private javax.swing.JPanel jPanelNgaySinh;
    private javax.swing.JPanel jPanelSoDienThoai;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTabDSLop;
    private javax.swing.JTable jTabHocVien;
    private javax.swing.JTable jTableHoaDon;
    private javax.swing.JTable jTableKhoa;
    private javax.swing.JTable jTableTK;
    private javax.swing.JTextField jTextSearchTK;
    private javax.swing.JTextField jTextTimHoaDon;
    private javax.swing.JTextField jTextTimKhoa;
    private javax.swing.JTextField jTextTimLop;
    private javax.swing.JPanel jplFilnal;
    private javax.swing.JPanel jplMain;
    private javax.swing.JPanel jplSlideMenu;
    private javax.swing.JPanel jplTitle;
    private javax.swing.JPanel jpllMenuBar;
    private javax.swing.JLabel lblCloseMenu;
    private javax.swing.JLabel lblOpenMenu;
    private javax.swing.JLabel lblTaiKhoan;
    private javax.swing.JLabel lblTrangChu;
    // End of variables declaration//GEN-END:variables
}
