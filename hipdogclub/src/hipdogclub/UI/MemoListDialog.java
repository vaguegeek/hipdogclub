package hipdogclub.UI;

import javax.swing.*;
import java.awt.*;
import hipdogclub.Model.User;
import hipdogclub.Model.Book;
import hipdogclub.Model.Memo;

public class MemoListDialog extends JDialog {
    private JFrame mainFrame;

    public MemoListDialog(JDialog parent, User user, Book book, Runnable onMemoSaved) {
        super(parent, "감상평 목록", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 감상평 리스트 패널
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        java.util.List<Memo> memos = user.getMemos().stream()
            .filter(m -> m.getBook().equals(book))
            .toList();

        JLabel title = new JLabel("이 책의 감상평");
        title.setFont(new Font("SansSerif", Font.BOLD, 17));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(title);
        listPanel.add(Box.createVerticalStrut(10));

        if (memos.isEmpty()) {
            JLabel none = new JLabel("아직 작성된 감상평이 없습니다.");
            none.setFont(new Font("SansSerif", Font.PLAIN, 14));
            none.setForeground(Color.GRAY);
            none.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(none);
        } else {
            for (Memo memo : memos) {
                JPanel memoPanel = new JPanel(new BorderLayout());
                memoPanel.setBackground(Color.WHITE);
                JLabel memoTitle = new JLabel("• " + memo.getTitle());
                memoTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
                JTextArea memoContent = new JTextArea(memo.getContent());
                memoContent.setLineWrap(true);
                memoContent.setWrapStyleWord(true);
                memoContent.setEditable(false);
                memoContent.setFont(new Font("SansSerif", Font.PLAIN, 13));
                memoContent.setBackground(new Color(250,250,250));
                memoContent.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                memoPanel.add(memoTitle, BorderLayout.NORTH);
                memoPanel.add(memoContent, BorderLayout.CENTER);
                memoPanel.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));
                listPanel.add(memoPanel);
            }
        }

        // 작성하기 버튼
        JButton writeBtn = new JButton("작성하기");
        writeBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        writeBtn.setBackground(new Color(255,255,255));
        writeBtn.setBorder(BorderFactory.createLineBorder(new Color(186, 85, 13)));
        writeBtn.setFocusPainted(false);
        writeBtn.setPreferredSize(new Dimension(100, 34));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(writeBtn);

        add(listPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        // 작성하기 버튼 클릭 시 입력창 다이얼로그 띄움
        // MemoListDialog.java
        writeBtn.addActionListener(e -> {
            MemoDialog memoDialog = new MemoDialog(this, user, book, () -> {
                if (onMemoSaved != null) onMemoSaved.run();
                this.dispose();
                if (mainFrame != null) {
                    mainFrame.setContentPane(new DashboardView(user, mainFrame).getPanel());
                    mainFrame.revalidate();
                    mainFrame.repaint();
                }
            });
            memoDialog.setVisible(true);
        });
    }
}
