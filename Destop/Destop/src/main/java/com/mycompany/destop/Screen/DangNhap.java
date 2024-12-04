/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.destop.Screen;

import com.google.gson.Gson;
import com.mycompany.destop.DTO.JwtResponse;
import com.mycompany.destop.DTO.OTPRequestDTO;
import com.mycompany.destop.DTO.OTPResponseDTO;
import com.mycompany.destop.DTO.PhoneNumberDTO;
import com.mycompany.destop.DTO.ProfileDto;

import com.mycompany.destop.DTO.SigninDTO;
import com.mycompany.destop.DTO.SignupDto;
import com.mycompany.destop.Enum.ChucVuEnum;
import com.mycompany.destop.Modul.TaiKhoanLogin;
import com.mycompany.destop.Reponse.ApiResponse;

import com.mycompany.destop.Service.ApiClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;

/**
 *
 * @author Windows 10
 */
public class DangNhap extends javax.swing.JFrame {

    /**
     * Creates new form DangNhap
     */
    private ApiClient apiClient = new ApiClient();

    public DangNhap() {
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        jButtonLogin = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtUserNawme = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        jLabelIMG = new javax.swing.JLabel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login_Destop");
        setResizable(false);

        jButtonLogin.setBackground(new java.awt.Color(0, 204, 204));
        jButtonLogin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonLogin.setForeground(new java.awt.Color(153, 153, 0));
        jButtonLogin.setText("Login");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });

        jButtonReset.setBackground(new java.awt.Color(0, 204, 204));
        jButtonReset.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonReset.setForeground(new java.awt.Color(153, 153, 0));
        jButtonReset.setText("Reset");
        jButtonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("LOGIN TO SYSTEM");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Username:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Password:");

        txtUserNawme.setText("admin1");
        txtUserNawme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserNawmeActionPerformed(evt);
            }
        });

        txtPassword.setText("123456");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButtonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(100, 100, 100)
                                .addComponent(jButtonReset, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUserNawme, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(58, 58, 58)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtUserNawme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonReset, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(177, 177, 177))
        );

        jLabelIMG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/login2.jpg"))); // NOI18N
        jLabelIMG.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabelIMG.setAlignmentY(0.0F);
        jLabelIMG.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/image/login2.jpg"))); // NOI18N
        jLabelIMG.setMaximumSize(new java.awt.Dimension(500, 500));
        jLabelIMG.setMinimumSize(new java.awt.Dimension(500, 500));
        jLabelIMG.setName("500"); // NOI18N
        jLabelIMG.setPreferredSize(new java.awt.Dimension(500, 500));
        jLabelIMG.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabelIMG, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelIMG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoginActionPerformed
        String username = txtUserNawme.getText();
        String password = new String(txtPassword.getPassword());
        StringBuilder builder = new StringBuilder();

        // Kiểm tra xem tên đăng nhập và mật khẩu có rỗng không
        if (username.isEmpty()) {
            builder.append("Username đang rỗng\n");
        }
        if (password.isEmpty()) {
            builder.append("Password đang rỗng\n");
        }

        // Nếu có lỗi, hiển thị thông báo
        if (builder.length() > 0) {
            JOptionPane.showMessageDialog(this, builder.toString(), "Invalidation", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
//            ApiClient apiClient = new ApiClient();
                JwtResponse response = apiClient.callLoginApi(username, password);  // Sửa lại tên biến từ 'reponse' thành 'response'
//            System.out.println(response.getAccessToken());
                if (response != null) {

                    SigninDTO signinDTO = apiClient.callProfileApi(response.getAccessToken());

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String formattedDate = signinDTO.getU().getNgaySinh().format(formatter);
//                System.out.println("Ngày sinh định dạng: " + formattedDate);
                    // Kiểm tra quyền của người dùng
                    if (signinDTO.getCvEnum().equals(ChucVuEnum.ADMIN) || signinDTO.getCvEnum().equals(ChucVuEnum.QUANLY)) {

//                        JOptionPane.showMessageDialog(this, "Đăng nhập thành công");
                        Menu menuFrame = new Menu(response.getAccessToken());
                        menuFrame.setVisible(true);  // Make the Menu frame visible
                        this.dispose();  // Close the current login frame
                    } else {
                        JOptionPane.showMessageDialog(this, "Tài khoản có chức vụ không phù hợp");
                    }
                } else {
                    txtUserNawme.setText(""); // Làm trống trường tên người dùng 
                    txtPassword.setText("");
//                    System.out.println(txtUserNawme.getText());
//                    JOptionPane.showMessageDialog(this, "Đăng nhập thất bại");
                }
            } catch (Exception e) {
                e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Đã có lỗi xảy ra. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonLoginActionPerformed
    private boolean updateInfo(JTextField txtName, JCheckBox chkMale, JTextField txtEmail, JTextField txtPhone,
            com.toedter.calendar.JDateChooser dateChooser, JTextField txtAddress) {
        // Kiểm tra trường rỗng
        if (txtName.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()
                || txtPhone.getText().trim().isEmpty() || txtAddress.getText().trim().isEmpty()
                || dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra định dạng email
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!txtEmail.getText().matches(emailPattern)) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra định dạng số điện thoại
        String phonePattern = "^\\d{10,11}$"; // Chấp nhận 10-11 chữ số
        if (!txtPhone.getText().matches(phonePattern)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ! (Chỉ chấp nhận 10-11 chữ số)", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra ngày sinh hợp lệ
        if (dateChooser.getDate().after(new java.util.Date())) {
            JOptionPane.showMessageDialog(null, "Ngày sinh không được lớn hơn ngày hiện tại!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }


    private void jButtonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetActionPerformed

        // Nhập số điện thoại từ người dùng
        String phoneNumber = JOptionPane.showInputDialog(null, "Nhập số điện thoại:");
// ApiClient apiClient = new ApiClient(); // Khởi tạo apiClient nếu cần

// Kiểm tra xem người dùng đã nhập số điện thoại chưa
        try {
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                // Regex để kiểm tra định dạng số điện thoại
                String regex = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";

                if (phoneNumber.matches(regex)) {
                    TaiKhoanLogin taiKhoan = apiClient.getTaiKhoanBySDT(phoneNumber);
                    if (taiKhoan != null) {
                        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(phoneNumber);
                        String OTP = apiClient.sendOTP(phoneNumberDTO);
                        System.out.println(OTP);
                        if (OTP != null) {
                            int maxRetryAttempts = 5;
                            int attemptCount = 0;

                            while (attemptCount < maxRetryAttempts) {
                                String otpInput = JOptionPane.showInputDialog(null, "Nhập mã OTP vừa được gửi tới " + phoneNumber + ":");
                                if (otpInput == null) {
                                    // Thoát khỏi vòng lặp
                                    break;
                                }
                                if (!otpInput.isEmpty()) {
                                    if (otpInput.equals(OTP)) {
                                        OTPResponseDTO reponseOTP = apiClient.verifyOTPFromClient(new OTPRequestDTO(phoneNumber, otpInput));
                                        JOptionPane.showMessageDialog(null, "Xác thực thành công!");
                                        String newPass = null;
                                        String regex2 = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

                                        while (true) {
                                            newPass = JOptionPane.showInputDialog(null, "Nhập mật khẩu mới (ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số):");

                                            // Kiểm tra người dùng nhấn "Hủy"
                                            if (newPass == null) {
                                                JOptionPane.showMessageDialog(null, "Bạn đã hủy thay đổi mật khẩu.");
                                                String token = apiClient.resetPasswordFromClient(reponseOTP.getAccessToken(), taiKhoan.getMatKhau());
                                                break;
                                            }

                                            // Kiểm tra định dạng mật khẩu
                                            if (newPass.matches(regex2)) {
                                                String token = apiClient.resetPasswordFromClient(reponseOTP.getAccessToken(), newPass);
                                                JOptionPane.showMessageDialog(null, "Mật khẩu đã được thay đổi thành công!");
                                                break;
                                            } else {
                                                JOptionPane.showMessageDialog(null, "Mật khẩu mới phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số! Vui lòng thử lại.");
                                            }
                                        }

                                        break;
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Mã OTP không đúng. Vui lòng kiểm tra lại mã và thử lại.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Mã OTP không thể bỏ trống. Vui lòng nhập mã OTP.");
                                }
                                attemptCount++;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Không thể gửi mã OTP. Vui lòng thử lại sau.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Số điện thoại đã được đăng ký tài khoản. Đảm bảo số điện thoại có định dạng đúng.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại để tiếp tục.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Bạn chưa nhập số điện thoại. Vui lòng nhập số điện thoại để tiếp tục.");
            }
        } catch (Exception ex) {
            Logger.getLogger(DangNhap.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi trong quá trình gửi mã OTP. Vui lòng thử lại sau.");
        }


    }//GEN-LAST:event_jButtonResetActionPerformed

    private void txtUserNawmeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserNawmeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserNawmeActionPerformed

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
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DangNhap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelIMG;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserNawme;
    // End of variables declaration//GEN-END:variables
}
