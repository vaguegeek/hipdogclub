package hipdogclub.UI;

import javax.swing.*;

import hipdogclub.Model.User;
import hipdogclub.Util.FileManager;

public class MainApp {
    public static void main(String[] args) {
        // 데이터 불러오기
        FileManager.loadUser();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("나만의 독서 기록장");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 단일 사용자 객체 생성 또는 불러오기
            User user = FileManager.loadUser(); 
            frame.setContentPane(new DashboardView(user, frame).getPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // 창 닫을 때 데이터 저장
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    FileManager.saveUser(user); 
                    System.exit(0);
                }
            });
        });
    }
}