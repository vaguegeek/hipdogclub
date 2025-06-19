package hipdogclub;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("독서 관리 프로그램");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(640, 480);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new hipdogclub.UI.LoginView(frame).getPanel());
            frame.setVisible(true);
        });
    }

    // 프로그램 종료 시 데이터 저장
    public static void exit() {
        hipdogclub.Util.FileManager.saveData();
        System.exit(0);
    }
}