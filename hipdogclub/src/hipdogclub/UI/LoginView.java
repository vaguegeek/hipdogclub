package hipdogclub.UI;

import javax.swing.*;
import java.awt.*;
import hipdogclub.Model.User;
import hipdogclub.Model.UserManager;

public class LoginView {
    private JPanel panel;
    private JTextField idField;
    private JPasswordField pwField;
    private JLabel messageLabel;

    public LoginView(JFrame mainFrame) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(Color.WHITE);

        // 타이틀
        JLabel title = new JLabel("로그인");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("서비스 이용을 위해 로그인해 주세요.");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 14));
        desc.setForeground(Color.GRAY);
        desc.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 아이디
        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        idField = new JTextField(20);
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        idField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 비밀번호
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        pwLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        pwField = new JPasswordField(20);
        pwField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        pwField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 로그인 버튼
        JButton loginBtn = new JButton("로그인");
        loginBtn.setBackground(Color.BLACK);
        loginBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 메시지 라벨
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 하단 바텀 패널에 회원가입 라벨만 배치
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomPanel.setOpaque(false);

        JLabel signupLabel = new JLabel("회원가입");
        signupLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        signupLabel.setForeground(new Color(169, 169, 169)); // 진회색
        signupLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bottomPanel.add(signupLabel);
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 회원가입 라벨 클릭 이벤트
        signupLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String userId = idField.getText().trim();
                String password = new String(pwField.getPassword()).trim();
                if (userId.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("아이디와 비밀번호를 입력하세요.");
                    return;
                }
                if (UserManager.isDuplicated(userId)) {
                    messageLabel.setText("이미 존재하는 아이디입니다.");
                    return;
                }
                if (UserManager.signUp(userId, password)) {
                    messageLabel.setText("회원가입 성공!");
                } else {
                    messageLabel.setText("회원가입 실패");
                }
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signupLabel.setForeground(new Color(90, 90, 90));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                signupLabel.setForeground(new Color(169, 169, 169));
            }
        });

        // 컴포넌트 배치
        panel.add(title);
        panel.add(desc);
        panel.add(idLabel);
        panel.add(idField);
        panel.add(pwLabel);
        panel.add(pwField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loginBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(messageLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(bottomPanel);

        // 로그인 버튼 이벤트
        loginBtn.addActionListener(e -> {
            String userId = idField.getText().trim();
            String password = new String(pwField.getPassword()).trim();
            if (userId.isEmpty() || password.isEmpty()) {
                messageLabel.setText("아이디와 비밀번호를 입력하세요.");
                return;
            }
            User user = UserManager.login(userId, password);
            if (user != null) {
                mainFrame.setContentPane(new DashboardView(user, mainFrame).getPanel());
                mainFrame.revalidate();
                mainFrame.repaint();
            } else {
                messageLabel.setText("아이디 또는 비밀번호가 일치하지 않습니다.");
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }
}