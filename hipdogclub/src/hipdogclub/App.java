package hipdogclub;

import javax.swing.*;
import hipdogclub.Model.User;
import hipdogclub.Util.FileManager;
import hipdogclub.UI.DashboardView;

public class App {
    public static void main(String[] args) {
        // 프로그램 시작 시 데이터 불러오기
        User user = FileManager.loadUser();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("독서 관리 프로그램");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(640, 480);
            frame.setLocationRelativeTo(null);

            // 로그인 없이 바로 대시보드로 진입
            frame.setContentPane(new DashboardView(user, frame).getPanel());
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

    // 필요시 다른 곳에서 종료 호출
    public static void exit(User user) {
        FileManager.saveUser(user);
        System.exit(0);
    }
}