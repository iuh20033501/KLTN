/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.destop;

import com.mycompany.destop.Screen.DangNhap;
//import com.mycompany.destop.Screen.DangNhap;

/**
 *
 * @author Windows 10
 */
public class Destop {

     public static void main(String[] args) {
        // Set the Nimbus look and feel (optional)
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Create and display the login form
        java.awt.EventQueue.invokeLater(() -> {
            new DangNhap().setVisible(true);
        });
//    System.out.println("Hello, World!");
}
}