package hipdogclub.UI;

import javax.swing.*;
import java.awt.*;
import hipdogclub.Model.Book;
import hipdogclub.Model.User;

public class BookDetailDialog extends JDialog {
    private JLabel statusLabel;
    private JLabel percentLabel;
    private JProgressBar bar;
    private Book book;

    // 콜백 파라미터 추가
    public BookDetailDialog(JFrame parent, User user, Book book, Runnable onRecordChanged) {
        super(parent, "도서 상세", true);
        this.book = book;

        setSize(700, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 왼쪽: 표지(이미지 대신 색상)
        JPanel left = new JPanel();
        left.setPreferredSize(new Dimension(220, 300));
        left.setBackground(Color.WHITE);
        JLabel cover = new JLabel();
        cover.setPreferredSize(new Dimension(180, 250));
        cover.setOpaque(true);
        cover.setBackground(new Color(255, 244, 200));
        left.add(cover);

        // 오른쪽: 책 정보/진행률/버튼
        JPanel right = new JPanel();
        right.setBackground(Color.WHITE);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel(book.getTitle());
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel info = new JLabel(book.getAuthor() + " | " + book.getPublisher() + " | " + book.getTotalPages() + "p");
        info.setFont(new Font("SansSerif", Font.PLAIN, 14));
        info.setForeground(Color.DARK_GRAY);
        info.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 상태
        statusLabel = new JLabel(getStatusText());
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusLabel.setForeground(new Color(204, 102, 0));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 진행률 바
        int percent = book.getProgressPercent();
        bar = new JProgressBar(0, 100);
        bar.setValue(percent);
        bar.setPreferredSize(new Dimension(320, 18));
        bar.setForeground(new Color(186, 85, 13));
        bar.setBackground(new Color(240, 240, 240));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);

        percentLabel = new JLabel(percent + "%");
        percentLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        percentLabel.setForeground(new Color(186, 85, 13));
        percentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 독서기록 버튼
        JButton recordBtn = new JButton("독서기록");
        recordBtn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        recordBtn.setBackground(Color.WHITE);
        recordBtn.setBorder(BorderFactory.createLineBorder(new Color(186, 85, 13)));
        recordBtn.setFocusPainted(false);
        recordBtn.setPreferredSize(new Dimension(120, 34));
        recordBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 감상평 버튼
        JButton memoBtn = new JButton("감상평");
        memoBtn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        memoBtn.setBackground(Color.WHITE);
        memoBtn.setBorder(BorderFactory.createLineBorder(new Color(186, 85, 13)));
        memoBtn.setFocusPainted(false);
        memoBtn.setPreferredSize(new Dimension(120, 34));
        memoBtn.setAlignmentX(Component.LEFT_ALIGNMENT);


        // 컴포넌트 배치 (add 순서, 여백)
        right.add(title);
        right.add(Box.createVerticalStrut(10));
        right.add(info);
        right.add(Box.createVerticalStrut(20));
        right.add(statusLabel);
        right.add(Box.createVerticalStrut(10));
        right.add(bar);
        right.add(percentLabel);
        right.add(Box.createVerticalStrut(24));
        
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(recordBtn);
        btnPanel.add(memoBtn);

        right.add(Box.createVerticalStrut(24));
        right.add(btnPanel);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);

        // 닫기 버튼
        JButton closeBtn = new JButton("목록으로");
        closeBtn.setPreferredSize(new Dimension(120, 34));
        closeBtn.addActionListener(e -> dispose());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 16));
        bottom.setBackground(Color.WHITE);
        bottom.add(closeBtn);
        add(bottom, BorderLayout.SOUTH);

        // 독서기록 버튼 클릭 시 (콜백 구조)
        recordBtn.addActionListener(e -> {
            ReadingRecordDialog dialog = new ReadingRecordDialog(this, user, book, () -> {
                updateProgressUI();
                if (onRecordChanged != null) onRecordChanged.run();
            });
            dialog.setVisible(true);
            SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
        });

        // 감상평 버튼 클릭 이벤트
        memoBtn.addActionListener(e -> {
            MemoListDialog dialog = new MemoListDialog(this, user, book, () -> {
                // 저장 후 필요한 UI 갱신
                if (onRecordChanged != null) onRecordChanged.run(); // 콜백 실행
                updateProgressUI();
            });
            dialog.setVisible(true);
        });

    }

    // 상태 텍스트 계산
    private String getStatusText() {
        if (book.isFinished()) return "완독";
        if (book.getCurrentPage() > 0) return "읽는중";
        return "미시작";
    }

    // 진행률/상태 UI 갱신
    private void updateProgressUI() {
        int percent = book.getProgressPercent();
        bar.setValue(percent);
        percentLabel.setText(percent + "%");
        statusLabel.setText(getStatusText());
    }
}