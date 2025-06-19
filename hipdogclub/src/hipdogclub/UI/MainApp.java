package hipdogclub.UI;

import javax.swing.*;
import hipdogclub.Util.FileManager;

public class MainApp {
    public static void main(String[] args) {
        // 데이터 불러오기
        FileManager.loadData();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("힙독클럽 로그인");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            LoginView loginView = new LoginView(frame);
            frame.setContentPane(loginView.getPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // 창 닫을 때 데이터 저장
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    FileManager.saveData();
                    System.exit(0);
                }
            });
        });
    }
}