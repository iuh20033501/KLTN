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
import com.mycompany.destop.Modul.ThanhToan;
import com.mycompany.destop.Modul.User;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.destop.DTO.OTPRequestDTO;
import com.mycompany.destop.DTO.OTPResponseDTO;
import com.mycompany.destop.DTO.PhoneNumberDTO;
import com.mycompany.destop.DTO.SignupDto;
import com.mycompany.destop.Enum.ChucVuEnum;

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
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
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
import javax.swing.table.DefaultTableCellRenderer;
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
    int y = 700;    //chieu cao

    String accessTokenLogin;
    SigninDTO signinDTO;
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateAdapter()).create();
    private ApiClient apiClient = new ApiClient();
    private AWSService awsService = new AWSService();
    private TaiKhoanService taiKhoanService = new TaiKhoanService();
    private LopHocService lopHocService = new LopHocService();
    private KhoaHocService khoaHocService = new KhoaHocService();
    private HoaDonService hoaDonService = new HoaDonService();
    private Long idLopOutClass = 1l;
//    private Long idHoaDonOutClass = 1l;

    /**
     * Creates new form Menu
     */
    public Menu(String accessToken) throws Exception {
        initComponents();
        accessTokenLogin = accessToken;
        jplSlideMenu.setSize(210, 600);
        cardTrangChu.setVisible(true);
        cardTaiKhoan.setVisible(false);
        cardHoaDon.setVisible(false);
        cardLopHoc.setVisible(false);
        cardKhoaHoc.setVisible(false);
        cardThongKe.setVisible(false);
        LoadInfo();
    }

    private void LoadInfo() throws Exception {
        signinDTO = apiClient.callProfileApi(accessTokenLogin);
        if (signinDTO.getCvEnum().equals(ChucVuEnum.QUANLY)) {
            lblTaiKhoan.setVisible(false);
            lblThongKe.setVisible(false);
        }
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

    private void LoadTableDSLop() {
        try {
            // Gọi API để lấy danh sách lớp học
            ArrayList<LopHoc> danhSachLop = (ArrayList<LopHoc>) lopHocService.getAllLopHocApi(accessTokenLogin);

            // Tạo model cho JTable
            DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                    new String[]{
                        "ID", "Tên Lớp", "Trạng thái", "Tên giảng viên", "Số học viên", "Ngày bắt đầu",
                        "Ngày kết thúc", "Khóa Học", "Tùy Chỉnh", "Delete"
                    }) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Chỉ cho phép chỉnh sửa cột "Tùy Chỉnh" và "Delete"
                    return column == 8 || column == 9;
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
                    lop.getIdLopHoc(),
                    lop.getTenLopHoc(),
                    lop.getTrangThai(),
                    lop.getGiangVien().getHoTen(),
                    lop.getSoHocVien(),
                    ngayBDFormatted, // Ngày bắt đầu đã được định dạng lại
                    ngayKTFormatted,
                    lop.getKhoaHoc().getTenKhoaHoc(),
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

            // Thêm ListSelectionListener để lắng nghe sự kiện chọn dòng
            jTabDSLop.getSelectionModel().addListSelectionListener(e -> {
                // Kiểm tra nếu sự kiện không phải là do việc chọn dòng mới
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = jTabDSLop.getSelectedRow();
                    if (selectedRow != -1) {
                        // Lấy dữ liệu từ dòng được chọn
                        Long idLopHoc = (Long) jTabDSLop.getValueAt(selectedRow, 0);
                        idLopOutClass = idLopHoc;
                        // Gọi hàm loadTableHocVien() với ID lớp học được chọn
                        LoadTableHocVien(idLopHoc);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void LoadTableHocVien(Long idLop) {
        try {
            // Gọi API để lấy danh sách học viên của lớp
            ArrayList<ThanhToan> danhSachThanhToan = (ArrayList<ThanhToan>) hoaDonService.FindThanhToanByIdLop(accessTokenLogin, idLop);

            // Tạo model cho JTable
            DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                    new String[]{
                        "IDThanhToan", "Tên học viên", "Trạng thái", "Delete"
                    }) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Chỉ cho phép chỉnh sửa cột "Delete"
                    return column == 3;
                }
            };

            // Thêm dữ liệu vào model
            for (ThanhToan thanhToan : danhSachThanhToan) {
                model.addRow(new Object[]{
                    thanhToan.getIdTT(),
                    thanhToan.getNguoiThanhToan().getHoTen(),
                    thanhToan.getTrangThai(), // Trạng thái
                    "Delete" // Nút "Delete"
                });
            }

            // Gắn model vào JTable
            jTabHocVien.setModel(model);

            // Đặt chiều cao dòng
            jTabHocVien.setRowHeight(30);

            // Thêm renderer và editor cho cột nút "Delete"
            TableColumn deleteColumn = jTabHocVien.getColumn("Delete");
            deleteColumn.setCellRenderer(new ButtonRenderer());
            deleteColumn.setCellEditor(new ButtonEditorHocVien(new JButton("Delete"), "delete", jTabHocVien, idLop));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ButtonEditorHocVien extends DefaultCellEditor {

        private JButton button;
        private String label;
        private boolean clicked;
        private String action;
        private JTable table;
        private Long idLop;

        public ButtonEditorHocVien(JButton button, String action, JTable table, Long idLop) {
            super(new JTextField());
            this.button = button;
            this.action = action;
            this.table = table;
            this.idLop = idLop;
            button.addActionListener(e -> performAction());
        }

        private void performAction() {
            int row = table.getSelectedRow(); // Lấy dòng hiện tại
            Object id = table.getValueAt(row, 0); // Lấy giá trị cột ID
            Object tenHV = table.getValueAt(row, 1);
            Object trangThai = table.getValueAt(row, 2);
            Long idTT = Long.parseLong(id.toString());
            if ("delete".equals(action)) {
                // Xử lý nút "Delete"
                int confirm = JOptionPane.showConfirmDialog(button, "Bạn có chắc muốn xóa Thành Viên : " + tenHV + " khỏi lớp học?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (trangThai.toString().equals("CANCEL")) {
//                          if (trangThai.toString().equals("DONE")) {
                        try {
                            if (hoaDonService.loadApiDeleteThanhToan(accessTokenLogin, idTT) != null) {
                                JOptionPane.showMessageDialog(button, "Đã xóa học viên : " + tenHV);
                                LoadTableHocVien(idLop);
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }
//                          }else {
//                        JOptionPane.showMessageDialog(null, "Học viên dã thanh toán tiền không thể xóa", "Error", JOptionPane.ERROR_MESSAGE);
//                    }
                    } else {
                        JOptionPane.showMessageDialog(null, "Học viên dã bị bị xóa khỏi lớp trước đó", "Error", JOptionPane.ERROR_MESSAGE);
                    }
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
            Object tenLop = table.getValueAt(row, 1); // Lấy giá trị tên lớp
            Object id = table.getValueAt(row, 0); // (Nếu cần thêm ID, bạn phải truyền qua API)
            long idLop = Long.parseLong(id.toString());
            System.out.println("idd" + idLop);
            if ("info".equals(action)) {
                LopHoc lopHoc;

                try {
                    lopHoc = lopHocService.loadLopHocById(accessTokenLogin, idLop);
                    showDialogLop(lopHoc);
                } catch (Exception ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if ("delete".equals(action)) {
                // Xử lý nút "Delete"
                int confirm = JOptionPane.showConfirmDialog(button, "Bạn có chắc muốn xóa lớp: " + tenLop + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Gọi API để xóa lớp (thêm hàm deleteLopHoc nếu cần)
                        LopHoc lopHocXoa = lopHocService.deleteLopHoc(accessTokenLogin, idLop);
                        if (lopHocXoa != null) {
                            JOptionPane.showMessageDialog(button, "Đã xóa lớp: " + tenLop);
                            LoadTableDSLop();
                        } else {

                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }

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

    private void loadTableTaiKhoan() {
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

    private void LoadTableKhoa() {
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
                    LoadTableKhoa();

                } catch (Exception ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if ("delete".equals(action)) {
                // Xử lý nút "Delete"
                int confirm = JOptionPane.showConfirmDialog(button, "Bạn có chắc muốn xóa khóa học ID: " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        KhoaHoc khoaHocXoa = khoaHocService.deleteKhoaHocApi(accessTokenLogin, idKhoa);
                        JOptionPane.showMessageDialog(button, "Đã xóa khóa học ID: " + id);
                        LoadTableKhoa();
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

    private void LoadTableHoaDon() {
        try {
            // Gọi API để lấy danh sách hóa đơn
            ArrayList<HoaDon> danhSachHoaDon = (ArrayList<HoaDon>) hoaDonService.getAllHoaDonApi(accessTokenLogin);

            // Tạo model cho JTable
            DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                    new String[]{
                        "ID", "Ngày Lập", "Người Lập", "Thành Tiền", "Xem Thông tin"
                    }) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Chỉ cho phép chỉnh sửa cột "Delete" và "Xem"
                    return column == 4;
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
                    "Xem" // Nút "Xem"
                });
            }

            // Gắn model vào JTable
            jTableHoaDon.setModel(model);

            // Đặt chiều cao dòng
            jTableHoaDon.setRowHeight(30);

            // Thêm renderer cho các cột nút
            jTableHoaDon.getColumn("Xem Thông tin").setCellRenderer(new ButtonRenderer());
//            jTableHoaDon.getColumn("Delete").setCellRenderer(new ButtonRenderer());
// jTableTK.getColumn("Xem Thông tin").setCellRenderer(new ButtonRenderer());

            // Thêm editor cho các cột nút
            jTableHoaDon.getColumn("Xem Thông tin").setCellEditor(new ButtonEditorHoaDon(new JButton("Xem"), "info", jTableHoaDon));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ButtonEditorHoaDon extends DefaultCellEditor {

        private JButton button;
        private String action;
        private JTable table;

        public ButtonEditorHoaDon(JButton button, String action, JTable table) {
            super(new JTextField());
            this.button = button;
            this.action = action;
            this.table = table;

            // Lắng nghe sự kiện nhấn nút
            button.addActionListener(e -> performAction());
        }

        private void performAction() {
            try {
                int row = table.getSelectedRow(); // Lấy dòng được chọn
                if (row < 0) {
                    JOptionPane.showMessageDialog(button, "Không có dòng nào được chọn!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Object idObj = table.getValueAt(row, 0); // Lấy giá trị ID từ cột đầu tiên
                if (idObj == null) {
                    JOptionPane.showMessageDialog(button, "Không tìm thấy ID hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Long idHD = Long.parseLong(idObj.toString());
                if ("info".equals(action)) {
                    // Gọi hàm xử lý khi nhấn nút "Xem"
                    showInfoHoaDon(idHD);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(button, "Đã xảy ra lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText(value != null ? value.toString() : "Xem");
            return button;
        }
    }

//    class ButtonRenderer extends JButton implements TableCellRenderer {
//
//        public ButtonRenderer() {
//            setOpaque(true);
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            setText(value != null ? value.toString() : "Xem");
//            return this;
//        }
//    }
    /**
     *
     */
    private void openMenu() {
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
                    LoadInfo();
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

    private void showCatalogTaiKhoan(TaiKhoanLogin taiKhoan) {
        // Tạo JDialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Thông tin tài khoản");
        dialog.setSize(500, 600);
        dialog.setLayout(new BorderLayout(10, 10)); // Sử dụng BorderLayout
        dialog.setLocationRelativeTo(null);

        // Panel chính cho các trường
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Tạo viền bên trong
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        JTextField txtUsername = new JTextField();
        if (taiKhoan != null && taiKhoan.getTenDangNhap() != null) {
            txtUsername.setText(taiKhoan.getTenDangNhap());
        }
        gbc.gridx = 1;
        gbc.weightx = 0.8;  // 80% chiều rộng dòng
        txtUsername.setColumns(20);  // Chiều rộng 80%
        mainPanel.add(txtUsername, gbc);

        // Họ tên
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Họ tên:"), gbc);
        JTextField txtName = new JTextField();
        if (taiKhoan != null && taiKhoan.getUser() != null && taiKhoan.getUser().getHoTen() != null) {
            txtName.setText(taiKhoan.getUser().getHoTen());
        }
        gbc.gridx = 1;
        txtName.setColumns(20);
        mainPanel.add(txtName, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Email:"), gbc);
        JTextField txtEmail = new JTextField();
        if (taiKhoan != null && taiKhoan.getUser() != null && taiKhoan.getUser().getEmail() != null) {
            txtEmail.setText(taiKhoan.getUser().getEmail());
        }
        gbc.gridx = 1;
        mainPanel.add(txtEmail, gbc);

        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Địa chỉ:"), gbc);
        JTextField txtAddress = new JTextField();
        if (taiKhoan != null && taiKhoan.getUser() != null && taiKhoan.getUser().getDiaChi() != null) {
            txtAddress.setText(taiKhoan.getUser().getDiaChi());
        }
        gbc.gridx = 1;
        mainPanel.add(txtAddress, gbc);

        // Giới tính
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Giới tính:"), gbc);
        JCheckBox chkMale = new JCheckBox("Nam");
        if (taiKhoan != null && taiKhoan.getUser() != null) {
            chkMale.setSelected(taiKhoan.getUser().isGioiTinh());
        }
        gbc.gridx = 1;
        mainPanel.add(chkMale, gbc);

        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("SĐT:"), gbc);
        JTextField txtPhone = new JTextField();
        if (taiKhoan != null && taiKhoan.getUser() != null && taiKhoan.getUser().getSdt() != null) {
            txtPhone.setText(taiKhoan.getUser().getSdt());
        }
        gbc.gridx = 1;
        mainPanel.add(txtPhone, gbc);

        // Ngày sinh
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Ngày sinh:"), gbc);
        com.toedter.calendar.JDateChooser dateChooser = new com.toedter.calendar.JDateChooser();
        if (taiKhoan != null && taiKhoan.getUser() != null && taiKhoan.getUser().getNgaySinh() != null) {
            dateChooser.setDate(java.sql.Date.valueOf(taiKhoan.getUser().getNgaySinh()));
        }
        gbc.gridx = 1;
        mainPanel.add(dateChooser, gbc);

        // Lương
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Lương:"), gbc);
        JTextField txtLuong = new JTextField();
//        if (taiKhoan != null) {
//            txtLuong.setText(String.valueOf(taiKhoan.getLuong()));
//        }
        gbc.gridx = 1;
        mainPanel.add(txtLuong, gbc);

        // Vai trò
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Vai trò:"), gbc);
        JComboBox<String> cbVaiTro = new JComboBox<>(new String[]{"GiangVien", "NhanVien", "Admin"});
        if (taiKhoan != null) {
            String vaiTro = taiKhoan.getRole().toString(); // Trả về chuỗi, ví dụ: "TEACHER", "QUANLY", "ADMIN"
            switch (vaiTro) {
                case "TEACHER" ->
                    cbVaiTro.setSelectedItem("GiangVien");
                case "QUANLY" ->
                    cbVaiTro.setSelectedItem("NhanVien");
                case "ADMIN" ->
                    cbVaiTro.setSelectedItem("Admin");
            }
        }
        gbc.gridx = 1;
        mainPanel.add(cbVaiTro, gbc);

        // Hiển thị ảnh
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel lblDisplayImage = new JLabel("", JLabel.CENTER); // Căn giữa nội dung
        lblDisplayImage.setPreferredSize(new Dimension(100, 100));
        lblDisplayImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        if (taiKhoan != null && taiKhoan.getUser() != null && taiKhoan.getUser().getImage() != null) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(taiKhoan.getUser().getImage()).getImage()
                    .getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            lblDisplayImage.setIcon(imageIcon);
        }
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

        // Xử lý sự kiện lưu và hủy
        btnSave.addActionListener(e -> {
            try {
                // Kiểm tra các điều kiện
                String username = txtUsername.getText();
                String email = txtEmail.getText();
                String phone = txtPhone.getText();
                String salary = txtLuong.getText();
                Date selectedDate = dateChooser.getDate();
                Date currentDate = new java.util.Date();
                if (!username.matches(".*[a-zA-Z].*")) { // Tên đăng nhập phải chứa ít nhất một chữ cái
                    JOptionPane.showMessageDialog(dialog, "Tên đăng nhập phải chứa ít nhất một chữ cái.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) { // Email phải có dạng @gmail.com
                    JOptionPane.showMessageDialog(dialog, "Email phải có dạng @gmail.com.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!phone.matches("^0\\d{9}$")) { // SDT phải bắt đầu bằng 0 và có 10 chữ số
                    JOptionPane.showMessageDialog(dialog, "Số điện thoại phải bắt đầu bằng 0 và có đúng 10 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedDate != null && !selectedDate.before(currentDate)) { // Kiểm tra ngày sinh phải là quá khứ
                    JOptionPane.showMessageDialog(dialog, "Ngày sinh phải là một ngày trong quá khứ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String selectedRole = (String) cbVaiTro.getSelectedItem();
                Long vaiTroValue = switch (selectedRole) {
                    case "GiangVien" ->
                        1L;
                    case "NhanVien" ->
                        2L;
                    case "Admin" ->
                        3L;
                    default ->
                        null; // Giá trị mặc định nếu không khớp
                };

                if (vaiTroValue == null) {
                    JOptionPane.showMessageDialog(dialog, "Vai trò không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Double.parseDouble(salary); // Kiểm tra lương là số
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Lương phải là một số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
//                Long phoneNumber = Long.parseLong(phone);
                // Gọi phương thức cập nhật thông tin tài khoản
                TaiKhoanLogin taiKhoanFind = apiClient.getTaiKhoanBySDT(phone);
                if (taiKhoan != null) {
                    PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(phone);
                    String OTP = apiClient.sendOTP(phoneNumberDTO);
                    System.out.println(OTP);
                    String otpInput = JOptionPane.showInputDialog(null, "Nhập mã OTP vừa được gửi tới " + phone + ":");
                    if (OTP != null) {
                        if (otpInput != null && !otpInput.isEmpty()) {

                            if (otpInput.equals(OTP)) {
                                System.out.println("thanhcong");
                                OTPResponseDTO reponseOTP = apiClient.verifyOTPFromClient(new OTPRequestDTO(phone, otpInput));
                                JOptionPane.showMessageDialog(null, "Xác thực thành công!");
                                //

                                //
                            } else {
                                JOptionPane.showMessageDialog(null, "Mã OTP không đúng. Vui lòng kiểm tra lại mã và thử lại.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Mã OTP không thể bỏ trống. Vui lòng nhập mã OTP.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Không thể gửi mã OTP. Vui lòng thử lại sau.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Số điện thoại đã được đăng ký tài khoản. Đảm bảo số điện thoại có định dạng đúng.");
                }
            } catch (Exception ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
    }

    private boolean updateTaiKhoanInfo(SignupDto signupDTO) {
        // Thực hiện cập nhật thông tin tài khoản từ các trường

        // Cập nhật hình ảnh nếu có
//        String image = lblDisplayImage.getIcon() != null ? ((ImageIcon) lblDisplayImage.getIcon()).getDescription() : null;
        // Gọi dịch vụ để lưu thông tin tài khoản
        // Nếu bạn có một phương thức để cập nhật thông tin tài khoản, gọi nó ở đây
        // Ví dụ: TaiKhoanService.updateTaiKhoan(new TaiKhoan(username, name, email, password, address, image, gender, phone, birthday, luong));
        return true; // Trả về true nếu cập nhật thành công
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
                    LoadTableKhoa();
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

    private ArrayList<User> getDanhSachGiangVien() {
        try {
            return (ArrayList<User>) apiClient.getAllGiangVienLamViec(accessTokenLogin);

        } catch (Exception ex) {

            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

    private ArrayList<KhoaHoc> getDanhSachKhoaHoc() {
        try {
            return (ArrayList<KhoaHoc>) khoaHocService.getAllKhoaHocApi(accessTokenLogin);
        } catch (Exception ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

    private void ShowDialogThanhToan(ArrayList<ThanhToan> list) {
        // Khởi tạo danh sách thanh toán
        ArrayList<ThanhToan> thanhToanDaChon = new ArrayList<ThanhToan>();

        // Tạo dialog
        JDialog dialogThanhToan = new JDialog(this, "Danh sách thanh toán", true);
        dialogThanhToan.setSize(600, 400);
        dialogThanhToan.setLocationRelativeTo(this);

        // Tạo bảng hiển thị thông tin thanh toán
        Object[][] data = new Object[list.size()][5];
        String[] columnNames = {"Chọn", "ID", "Học viên", "Lớp học", "Trạng thái"};

        for (int i = 0; i < list.size(); i++) {
            ThanhToan thanhToan = list.get(i);
            data[i][0] = false; // Dùng cho checkbox, mặc định là không chọn
            data[i][1] = thanhToan.getIdTT();
            data[i][2] = thanhToan.getNguoiThanhToan().getHoTen(); // Giả sử `getHoTen()` trả về tên học viên
            data[i][3] = thanhToan.getLopHoc().getTenLopHoc(); // Giả sử `getTenLopHoc()` trả về tên lớp
            data[i][4] = thanhToan.getTrangThai().toString(); // Trạng thái thanh toán
        }

        // Tạo DefaultTableModel
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Chỉ cho phép chỉnh sửa cột "Chọn" (checkbox)
            }
        };

        JTable table = new JTable(model);

        // Sử dụng DefaultCellEditor để tạo checkbox trong cột "Chọn"
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected(value != null && (Boolean) value); // Thiết lập trạng thái checkbox
                return checkBox;
            }
        });

        // ScrollPane cho JTable
        JScrollPane scrollPane = new JScrollPane(table);
        dialogThanhToan.add(scrollPane, BorderLayout.CENTER);

        // Tạo Panel chứa nút Hủy và Thêm
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton btnHuy = new JButton("Hủy");
        btnHuy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogThanhToan.dispose(); // Đóng dialog khi nhấn Hủy
            }
        });

        JButton btnThem = new JButton("Thêm");
        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Long> listIdClick = new ArrayList<>();
                try {
                    // Lấy danh sách thanh toán đã chọn
                    for (int i = 0; i < list.size(); i++) {
                        Boolean isSelected = (Boolean) table.getValueAt(i, 0);
                        if (isSelected != null && isSelected) {
                            ThanhToan thanhToanClick = list.get(i);
                            listIdClick.add(thanhToanClick.getIdTT());
                        }
                    }

                    HoaDon hoaDonThem = hoaDonService.createHoaDonApi(accessTokenLogin, signinDTO.getU().getIdUser(), listIdClick);
                    JOptionPane.showMessageDialog(null, "Thêm hóa đơn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    dialogThanhToan.dispose(); // Đóng dialog khi nhấn Thêm
                    LoadTableHoaDon();
                } catch (Exception ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        panel.add(btnHuy);
        panel.add(btnThem);

        dialogThanhToan.add(panel, BorderLayout.SOUTH);

        // Hiển thị dialog
        dialogThanhToan.setVisible(true); // Gọi setVisible(true) để hiển thị dialog
    }

    public void showInfoHoaDon(Long idHoaDon) {
        // Giả sử lấy thông tin hóa đơn từ dịch vụ
        try {
            HoaDon hoaDon = hoaDonService.getHoaDonByIdApi(accessTokenLogin, idHoaDon);
            ArrayList<ThanhToan> listTT = (ArrayList<ThanhToan>) hoaDonService.FindThanhToanByIdHoaDon(accessTokenLogin, idHoaDon);

            if (hoaDon == null) {
                JOptionPane.showMessageDialog(null, "Hóa đơn không tồn tại.");
                return;
            }

            // Tạo JDialog
            JDialog dialogListThanhToan = new JDialog();
            dialogListThanhToan.setTitle("Thông tin hóa đơn");
            dialogListThanhToan.setSize(600, 400);
            dialogListThanhToan.setLocationRelativeTo(null); // Đặt dialog vào giữa màn hình

            // Tạo bảng thông tin hóa đơn (dữ liệu giả định)
            String[] columnNames = {"ID", "Học viên", "Lớp học"};
            Object[][] data = new Object[listTT.size()][3]; // Khởi tạo mảng dữ liệu

            // Thêm dữ liệu vào bảng
            for (int i = 0; i < listTT.size(); i++) {
                ThanhToan thanhToan = listTT.get(i);
                data[i][0] = thanhToan.getIdTT();
                data[i][1] = thanhToan.getNguoiThanhToan().getHoTen();
                data[i][2] = thanhToan.getLopHoc().getTenLopHoc();
            }

            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            // Tạo Panel chứa các thông tin thêm
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(4, 2));

            // Dòng thông tin người lập và ngày lập
            infoPanel.add(new JLabel("Người lập:"));
            JTextField txtNguoiLap = new JTextField(hoaDon.getNguoiLap().getHoTen()); // Hiển thị tên người lập
            infoPanel.add(txtNguoiLap);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            infoPanel.add(new JLabel("Ngày lập:"));
            JTextField txtNgayLap = new JTextField(sdf.format(hoaDon.getNgayLap())); // Hiển thị ngày lập
            infoPanel.add(txtNgayLap);

            // Dòng thành tiền
            infoPanel.add(new JLabel("Thành Tiền:"));
            JTextField txtThanhTien = new JTextField(hoaDon.getThanhTien().toString());
            infoPanel.add(txtThanhTien);

            // Tạo các nút Cancel và Export PDF
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());

            JButton btnCancel = new JButton("Hủy");
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialogListThanhToan.dispose(); // Đóng dialog khi nhấn "Hủy"
                }
            });

            JButton btnExportPDF = new JButton("Export PDF");
            btnExportPDF.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        exportToPDF(hoaDon, listTT);
                        JOptionPane.showMessageDialog(dialogListThanhToan, "Đã xuất PDF thành công!");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialogListThanhToan, "Có lỗi khi xuất PDF.");
                        ex.printStackTrace();
                    }
                }
            });

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnExportPDF);

            // Tạo Layout cho Dialog
            dialogListThanhToan.setLayout(new BorderLayout());
            dialogListThanhToan.add(scrollPane, BorderLayout.CENTER);
            dialogListThanhToan.add(infoPanel, BorderLayout.SOUTH);
            dialogListThanhToan.add(buttonPanel, BorderLayout.SOUTH);

            dialogListThanhToan.setVisible(true); // Hiển thị dialog
        } catch (Exception ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// Phương thức xuất ra file PDF
    public void exportToPDF(HoaDon hoaDon, ArrayList<ThanhToan> listTT) throws DocumentException, IOException {
        // Tạo tài liệu PDF
        Document document = new Document();
        String fileName = "hoa_don_" + hoaDon.getIdHoaDon() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        // Thêm thông tin vào PDF
        document.add(new Paragraph("Ma Hoa Don: " + hoaDon.getIdHoaDon()));
        document.add(new Paragraph("Ten Nguoi Lap: " + hoaDon.getNguoiLap().getHoTen()));
        document.add(new Paragraph("Ngay Lap: " + hoaDon.getNgayLap()));
        document.add(new Paragraph("Thanh Tien: " + hoaDon.getThanhTien()));
        document.add(new Paragraph(" "));
        // Thêm bảng danh sách thanh toán
        PdfPTable table = new PdfPTable(3); // Bảng có 3 cột: ID, Học viên, Lớp học
        table.addCell("ID");
        table.addCell("Học viên");
        table.addCell("Lớp học");

        for (ThanhToan thanhToan : listTT) {
            table.addCell(String.valueOf(thanhToan.getIdTT()));
            table.addCell(thanhToan.getNguoiThanhToan().getHoTen());
            table.addCell(thanhToan.getLopHoc().getTenLopHoc());
        }

        // Thêm bảng vào tài liệu PDF
        document.add(table);

        // Đóng tài liệu PDF
        document.close();

        // Tự động mở file PDF sau khi lưu
        File file = new File(fileName);
        if (file.exists()) {
            Desktop.getDesktop().open(file);
        } else {
            System.out.println("Không thể tìm thấy file: " + fileName);
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
        JComboBox<TrangThaiLop> cbTrangThai = new JComboBox<>(TrangThaiLop.values());
        cbTrangThai.setSelectedItem(lop == null ? TrangThaiLop.READY : lop.getTrangThai());
        gbc.gridx = 1;
        mainPanel.add(cbTrangThai, gbc);

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
        ArrayList<User> danhSachGiangVien = getDanhSachGiangVien();
        JComboBox<User> cbGiangVien = taoComboBox(danhSachGiangVien, User::getHoTen);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Giảng viên:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(cbGiangVien, gbc);

        // Khóa học
        ArrayList<KhoaHoc> danhSachKhoaHoc = getDanhSachKhoaHoc();
        JComboBox<KhoaHoc> cbKhoaHoc = taoComboBox(danhSachKhoaHoc, KhoaHoc::getTenKhoaHoc);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Khóa học:"), gbc);
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

        if (lop != null) {
            txtSoHocVien.setText(lop.getSoHocVien().toString());
            txtMoTa.setText(lop.getMoTa());
            txtTenLopHoc.setText(lop.getTenLopHoc());
            dateChooserNgayBD.setDate(lop.getNgayBD());
            dateChooserNgayKT.setDate(lop.getNgayKT());
            // Gán giảng viên vào ComboBox
            if (lop.getGiangVien() != null) {
                cbGiangVien.setSelectedItem(lop.getGiangVien());
            }
            // Gán khóa học vào ComboBox
            if (lop.getKhoaHoc() != null) {
                cbKhoaHoc.setSelectedItem(lop.getKhoaHoc());
            }
        } else {
            cbTrangThai.enable(false);
        }

// Nút Lưu hoặc Cập nhật
        JButton btnSaveLop = new JButton(lop == null ? "Lưu" : "Cập nhật");
        btnSaveLop.setPreferredSize(new Dimension(120, 40)); // Kích thước nút
        btnSaveLop.setBackground(new Color(0, 153, 0)); // Màu nền xanh lá
        btnSaveLop.setForeground(Color.WHITE); // Màu chữ trắng
        btnSaveLop.setFont(new Font("Arial", Font.BOLD, 14)); // Kiểu chữ

// Nút Hủy
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(120, 40)); // Kích thước nút
        btnCancel.setBackground(new Color(204, 0, 0)); // Màu nền đỏ
        btnCancel.setForeground(Color.WHITE); // Màu chữ trắng
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14)); // Kiểu chữ

        buttonPanel.add(btnSaveLop);
        buttonPanel.add(btnCancel);

        dialogLop.add(buttonPanel, BorderLayout.SOUTH);

        // Xử lý nút lưu
        btnSaveLop.addActionListener(e -> {
            boolean success = themLopHoc(
                    accessTokenLogin, lop, txtTenLopHoc, txtSoHocVien, dateChooserNgayBD, dateChooserNgayKT, cbGiangVien, cbKhoaHoc, txtMoTa, cbTrangThai
            );
            if (success) {
                try {
                    LoadTableDSLop();
                    dialogLop.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Xử lý nút hủy
        btnCancel.addActionListener(e -> dialogLop.dispose());

        // Hiển thị dialog
        dialogLop.setVisible(true);
    }

// Tạo ComboBox chung
    private <T> JComboBox<T> taoComboBox(ArrayList<T> danhSach, Function<T, String> hienThi) {
        JComboBox<T> comboBox = new JComboBox<>();
        for (T item : danhSach) {
            comboBox.addItem(item);
        }
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    setText(hienThi.apply((T) value));
                }
                return this;
            }
        });
        return comboBox;
    }

    private boolean themLopHoc(
            String token, LopHoc lop, JTextField txtTenLopHoc, JTextField txtSoHocVien,
            JDateChooser dateChooserNgayBD, JDateChooser dateChooserNgayKT,
            JComboBox<User> cbGiangVien, JComboBox<KhoaHoc> cbKhoaHoc,
            JTextArea txtMoTa, JComboBox<TrangThaiLop> cbTrangThai
    ) {
        String tenLopHoc = txtTenLopHoc.getText().trim();
        String soHocVienStr = txtSoHocVien.getText().trim();

        Date ngayBD = dateChooserNgayBD.getDate();
        Date ngayKT = dateChooserNgayKT.getDate(); // Sửa: đúng tên biến của DateChooser

        // Kiểm tra đầu vào
        if (lop == null) {
            lop = new LopHoc();
        }

        String moTa = txtMoTa.getText().trim();
        TrangThaiLop trangThai = (TrangThaiLop) cbTrangThai.getSelectedItem();

        if (tenLopHoc.isEmpty() || soHocVienStr.isEmpty() || ngayBD == null || ngayKT == null || moTa.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!soHocVienStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Số học viên phải là số nguyên hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (ngayKT.before(ngayBD)) {
            JOptionPane.showMessageDialog(null, "Ngày kết thúc không được trước ngày bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra giảng viên và khóa học
        User selectedGiangVien = (User) cbGiangVien.getSelectedItem();
        KhoaHoc selectedKhoaHoc = (KhoaHoc) cbKhoaHoc.getSelectedItem();
        if (selectedGiangVien == null || selectedKhoaHoc == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn Giảng viên và Khóa học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Xử lý định dạng ngày (nếu cần)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String ngayBDFix = dateFormat.format(ngayBD);
            String ngayKTFix = dateFormat.format(ngayKT);
            ngayBD = dateFormat.parse(ngayBDFix);
            ngayKT = dateFormat.parse(ngayKTFix);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Gán các giá trị cho đối tượng lop
        lop.setTenLopHoc(tenLopHoc);
        lop.setSoHocVien(Long.parseLong(soHocVienStr));
        lop.setNgayBD(ngayBD);
        lop.setNgayKT(ngayKT);
        lop.setMoTa(moTa);
        lop.setTrangThai(trangThai);

        // Gửi dữ liệu đến service để lưu
        try {
            LopHoc result = lopHocService.createLopHoc(token, lop, selectedKhoaHoc.getIdKhoaHoc(), selectedGiangVien.getIdUser());
            if (result != null) {
                JOptionPane.showMessageDialog(null, "Lưu thông tin lớp học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Lưu thông tin lớp học thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi trong quá trình lưu", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
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
        System.out.println("selectedSkills:  " + selectedSkills);
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

    public static void showDialogFeedback() {
        // Tạo một JDialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Phản hồi ý kiến");

        // Tạo JLabel chú thích
        JLabel lblInstructions = new JLabel("<html>Ý kiến góp ý của bạn sẽ được gửi đến Admin và xem xét một cách nhanh nhất.</html>");
        lblInstructions.setFont(new Font("Arial", Font.PLAIN, 14));
        lblInstructions.setHorizontalAlignment(SwingConstants.CENTER);

        // Tạo JTextArea để nhập phản hồi
        JTextArea textAreaFeedback = new JTextArea();
        textAreaFeedback.setWrapStyleWord(true);
        textAreaFeedback.setLineWrap(true);
        textAreaFeedback.setFont(new Font("Arial", Font.PLAIN, 14));
        textAreaFeedback.setCaretPosition(0);

        // Tạo JScrollPane để cuộn văn bản
        JScrollPane scrollPane = new JScrollPane(textAreaFeedback);
        scrollPane.setPreferredSize(new Dimension(500, 200)); // Kích thước JScrollPane

        // Tạo nút "Gửi" và "Đóng"
        JButton btnSend = new JButton("Gửi");
        JButton btnClose = new JButton("Đóng");

        // Thêm sự kiện cho nút "Gửi"
        btnSend.addActionListener(e -> {
            String feedback = textAreaFeedback.getText().trim();
            if (feedback.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập phản hồi trước khi gửi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Phản hồi của bạn đã được gửi. Cảm ơn bạn!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose(); // Đóng dialog sau khi gửi
            }
        });

        // Thêm sự kiện cho nút "Đóng"
        btnClose.addActionListener(e -> dialog.dispose());

        // Tạo một JPanel chứa hai nút
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtons.add(btnSend);
        panelButtons.add(btnClose);

        // Tạo một JPanel chính để chứa các thành phần
        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());
        panelMain.add(lblInstructions, BorderLayout.NORTH);
        panelMain.add(scrollPane, BorderLayout.CENTER);
        panelMain.add(panelButtons, BorderLayout.SOUTH);

        // Thêm JPanel chính vào dialog
        dialog.add(panelMain);

        // Thiết lập dialog
        dialog.setSize(600, 400);
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
        lblThongKe = new javax.swing.JLabel();
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
        jBtKTraDangKy = new javax.swing.JButton();
        jBtThemHocVien = new javax.swing.JButton();
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
        setPreferredSize(new java.awt.Dimension(1115, 730));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jplFilnal.setBackground(new java.awt.Color(255, 255, 255));
        jplFilnal.setMinimumSize(new java.awt.Dimension(1100, 700));
        jplFilnal.setPreferredSize(new java.awt.Dimension(1010, 700));
        jplFilnal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jplSlideMenu.setBackground(new java.awt.Color(255, 255, 255));
        jplSlideMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jplSlideMenu.setMinimumSize(new java.awt.Dimension(210, 800));
        jplSlideMenu.setPreferredSize(new java.awt.Dimension(190, 900));
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
                .addGap(58, 58, 58)
                .addComponent(jLabel4)
                .addContainerGap(121, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCloseMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCloseMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel4)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jplSlideMenu.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 150));

        lblThongKe.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblThongKe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThongKe.setText("Thống Kê");
        lblThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThongKeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblThongKeMouseEntered(evt);
            }
        });
        jplSlideMenu.add(lblThongKe, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 210, 30));

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

        jplFilnal.add(jplSlideMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, 900));
        jplSlideMenu.getAccessibleContext().setAccessibleName("");

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
                .addContainerGap(1060, Short.MAX_VALUE))
        );
        jpllMenuBarLayout.setVerticalGroup(
            jpllMenuBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpllMenuBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblOpenMenu)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jplFilnal.add(jpllMenuBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 1110, 60));

        jplTitle.setBackground(new java.awt.Color(0, 204, 204));

        javax.swing.GroupLayout jplTitleLayout = new javax.swing.GroupLayout(jplTitle);
        jplTitle.setLayout(jplTitleLayout);
        jplTitleLayout.setHorizontalGroup(
            jplTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1110, Short.MAX_VALUE)
        );
        jplTitleLayout.setVerticalGroup(
            jplTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jplFilnal.add(jplTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1110, 30));

        jplMain.setBackground(new java.awt.Color(255, 255, 255));
        jplMain.setPreferredSize(new java.awt.Dimension(990, 600));
        jplMain.setLayout(new java.awt.CardLayout());

        cardTaiKhoan.setBackground(new java.awt.Color(255, 255, 255));
        cardTaiKhoan.setPreferredSize(new java.awt.Dimension(990, 600));

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
        jBtThemTK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBtThemTKMouseClicked(evt);
            }
        });

        jComSearchTK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Tên", "Chức vụ" }));

        javax.swing.GroupLayout cardTaiKhoanLayout = new javax.swing.GroupLayout(cardTaiKhoan);
        cardTaiKhoan.setLayout(cardTaiKhoanLayout);
        cardTaiKhoanLayout.setHorizontalGroup(
            cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                        .addComponent(jLabelMenuTK, javax.swing.GroupLayout.PREFERRED_SIZE, 929, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 81, Short.MAX_VALUE))
                    .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                        .addComponent(jTextSearchTK, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComSearchTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBtThemTK, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jScrollPane1)
        );
        cardTaiKhoanLayout.setVerticalGroup(
            cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelMenuTK, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComSearchTK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBtThemTK, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextSearchTK, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton2.getAccessibleContext().setAccessibleName("jButtonSearch");
        jBtThemTK.getAccessibleContext().setAccessibleName("jButtonAdd");

        jplMain.add(cardTaiKhoan, "card3");

        cardTrangChu.setBackground(new java.awt.Color(255, 255, 255));
        cardTrangChu.setPreferredSize(new java.awt.Dimension(990, 600));

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
                .addContainerGap(55, Short.MAX_VALUE))
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
                                .addComponent(jPanelBirthday, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
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
                .addGap(52, 52, 52)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jplMain.add(cardTrangChu, "card2");

        cardLopHoc.setBackground(new java.awt.Color(0, 153, 102));
        cardLopHoc.setPreferredSize(new java.awt.Dimension(990, 600));

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
                {null, null, null, null}
            },
            new String [] {
                "ID", "Tên học viên", "Trạng thái", "Delete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTabHocVien);

        jBtKTraDangKy.setBackground(new java.awt.Color(102, 0, 204));
        jBtKTraDangKy.setForeground(new java.awt.Color(255, 255, 255));
        jBtKTraDangKy.setText("Kiểm tra đăng ký");
        jBtKTraDangKy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBtKTraDangKyMouseClicked(evt);
            }
        });

        jBtThemHocVien.setBackground(new java.awt.Color(153, 153, 0));
        jBtThemHocVien.setForeground(new java.awt.Color(255, 255, 255));
        jBtThemHocVien.setText("Thêm Học Viên");
        jBtThemHocVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBtThemHocVienMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout cardLopHocLayout = new javax.swing.GroupLayout(cardLopHoc);
        cardLopHoc.setLayout(cardLopHocLayout);
        cardLopHocLayout.setHorizontalGroup(
            cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabTextLop, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(cardLopHocLayout.createSequentialGroup()
                .addGroup(cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardLopHocLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBtThemLop, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cardLopHocLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cardLopHocLayout.createSequentialGroup()
                                .addComponent(jBtThemHocVien, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBtKTraDangKy, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)))
                .addContainerGap())
        );
        cardLopHocLayout.setVerticalGroup(
            cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardLopHocLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabTextLop, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBtThemLop, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBtTimLop, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardLopHocLayout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cardLopHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jBtThemHocVien, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBtKTraDangKy, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jplMain.add(cardLopHoc, "card3");

        cardKhoaHoc.setBackground(new java.awt.Color(255, 255, 255));
        cardKhoaHoc.setPreferredSize(new java.awt.Dimension(990, 600));

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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE))
        );

        jplMain.add(cardKhoaHoc, "card3");

        cardHoaDon.setBackground(new java.awt.Color(255, 255, 255));
        cardHoaDon.setPreferredSize(new java.awt.Dimension(990, 600));

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
        jBntThemHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBntThemHoaDonMouseClicked(evt);
            }
        });

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
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE))
        );

        jplMain.add(cardHoaDon, "card3");

        cardThongKe.setBackground(new java.awt.Color(255, 255, 255));
        cardThongKe.setPreferredSize(new java.awt.Dimension(990, 600));

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

        jplFilnal.add(jplMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1100, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jplFilnal, javax.swing.GroupLayout.DEFAULT_SIZE, 1115, Short.MAX_VALUE)
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
        closeMenu();
        try {
            LoadInfo();
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
        closeMenu();
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
//        jplFilnal.setComponentZOrder(jplSlideMenu, 0);
        showDialogFeedback();
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
        closeMenu();
        LoadTableDSLop();
    }//GEN-LAST:event_jLabelLopHocMouseClicked

    private void jLabelKhoaHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelKhoaHocMouseClicked
        // TODO add your handling code here:
        cardTrangChu.setVisible(false);
        cardTaiKhoan.setVisible(false);
        cardHoaDon.setVisible(false);
        cardKhoaHoc.setVisible(true);
        cardLopHoc.setVisible(false);
        cardThongKe.setVisible(false);
        closeMenu();
        LoadTableKhoa();
    }//GEN-LAST:event_jLabelKhoaHocMouseClicked

    private void jLabelHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelHoaDonMouseClicked
        // TODO add your handling code here:
        cardTrangChu.setVisible(false);
        cardTaiKhoan.setVisible(false);
        cardHoaDon.setVisible(true);
        cardKhoaHoc.setVisible(false);
        cardLopHoc.setVisible(false);
        cardThongKe.setVisible(false);
        closeMenu();
        LoadTableHoaDon();
    }//GEN-LAST:event_jLabelHoaDonMouseClicked

    private void lblThongKeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongKeMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblThongKeMouseEntered

    private void lblThongKeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongKeMouseClicked
        // TODO add your handling code here:
        cardTrangChu.setVisible(false);
        cardTaiKhoan.setVisible(false);
        cardHoaDon.setVisible(false);
        cardKhoaHoc.setVisible(false);
        cardLopHoc.setVisible(false);
        cardThongKe.setVisible(true);
        closeMenu();
    }//GEN-LAST:event_lblThongKeMouseClicked

    private void jBntKhoaHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBntKhoaHocMouseClicked
        // TODO add your handling code here:
        showDialogKhoa(null);
    }//GEN-LAST:event_jBntKhoaHocMouseClicked

    private void jBtThemLopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBtThemLopMouseClicked
        // TODO add your handling code here:
        showDialogLop(null);
    }//GEN-LAST:event_jBtThemLopMouseClicked

    private void jBtKTraDangKyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBtKTraDangKyMouseClicked
        try {
            // TODO add your handling code here:
            ArrayList<ThanhToan> list = (ArrayList<ThanhToan>) hoaDonService.loadApiUploadThanhToanByLop(accessTokenLogin, 1l);
            JOptionPane.showMessageDialog(null, "Kiểm tra đã hoàn thành!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtKTraDangKyMouseClicked

    private void jBtThemHocVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBtThemHocVienMouseClicked
        // TODO add your handling code here:
        String idHocVien = JOptionPane.showInputDialog(this, "Vui lòng nhập mã học viên:");

        // Kiểm tra xem mã học viên có rỗng không
        if (idHocVien == null || idHocVien.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã học viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra xem mã học viên có phải là số hay không bằng regex
        String regex = "^[0-9]+$"; // Biểu thức chính quy kiểm tra chuỗi chỉ gồm các ký tự số
        if (!idHocVien.matches(regex)) {
            JOptionPane.showMessageDialog(this, "Mã học viên phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            ThanhToan thanhToan = hoaDonService.loadApiCreateThanhToan(accessTokenLogin, idLopOutClass, Long.parseLong(idHocVien));
            JOptionPane.showMessageDialog(null, "Bạn đã đăng ký cho học viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            LoadTableHocVien(idLopOutClass);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lớp đầy hoặc đã có học viên trong lớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtThemHocVienMouseClicked

    private void jBntThemHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBntThemHoaDonMouseClicked
        // TODO add your handling code here:
        String idHocVien = JOptionPane.showInputDialog(this, "Vui lòng nhập mã học viên:");

        // Kiểm tra xem mã học viên có rỗng không
        if (idHocVien == null || idHocVien.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã học viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra xem mã học viên có phải là số hay không bằng regex
        String regex = "^[0-9]+$"; // Biểu thức chính quy kiểm tra chuỗi chỉ gồm các ký tự số
        if (!idHocVien.matches(regex)) {
            JOptionPane.showMessageDialog(this, "Mã học viên phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            ArrayList<ThanhToan> listTT = (ArrayList<ThanhToan>) hoaDonService.FindThanhToanWaitByIdHocVien(accessTokenLogin, Long.parseLong(idHocVien));
            if (listTT.size() > 0) {
                ShowDialogThanhToan(listTT);
                LoadTableHoaDon();
            } else {
                JOptionPane.showMessageDialog(null, "Học viên chưa có thanh toán nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không tồn tại học viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBntThemHoaDonMouseClicked

    private void jBtThemTKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBtThemTKMouseClicked
        // TODO add your handling code here:
        showCatalogTaiKhoan(null);
    }//GEN-LAST:event_jBtThemTKMouseClicked

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
    private javax.swing.JButton jBtKTraDangKy;
    private javax.swing.JButton jBtSoHocVien;
    private javax.swing.JButton jBtThemHocVien;
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
    private javax.swing.JLabel lblThongKe;
    private javax.swing.JLabel lblTrangChu;
    // End of variables declaration//GEN-END:variables
}