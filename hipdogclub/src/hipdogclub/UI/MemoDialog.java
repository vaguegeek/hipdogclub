package hipdogclub.UI;

import javax.swing.*;
import java.awt.*;
import hipdogclub.Model.User;
import hipdogclub.Model.Book;

public class MemoDialog extends JDialog {
    public MemoDialog(JDialog parent, User user, Book book, Runnable onMemoSaved) {
        super(parent, "감상평 작성", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JTextField titleField = new JTextField();
        JTextArea contentArea = new JTextArea();
        JButton saveBtn = new JButton("저장");

        JPanel inputPanel = new JPanel(new BorderLayout(8,8));
        inputPanel.add(new JLabel("제목:"), BorderLayout.NORTH);
        inputPanel.add(titleField, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JLabel("내용:"), BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(saveBtn);

        add(inputPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "제목과 내용을 입력하세요.");
                return;
            }
            user.addMemo(book, title, content);
            if (onMemoSaved != null) onMemoSaved.run();
            dispose();
        });
    }
}