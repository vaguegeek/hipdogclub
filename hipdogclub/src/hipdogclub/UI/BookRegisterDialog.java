package hipdogclub.UI;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;
import hipdogclub.Model.Book;
import hipdogclub.Model.User;

public class BookRegisterDialog extends JDialog {
    public BookRegisterDialog(JFrame parent, User user, JPanel booksPanel, Function<Book, JPanel> cardFactory) {
        super(parent, "도서 등록", true);
        setSize(350, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField publisherField = new JTextField();
        JTextField totalPagesField = new JTextField();

        panel.add(new JLabel("제목:"));
        panel.add(titleField);
        panel.add(new JLabel("작가:"));
        panel.add(authorField);
        panel.add(new JLabel("출판사:"));
        panel.add(publisherField);
        panel.add(new JLabel("총 페이지수:"));
        panel.add(totalPagesField);

        JButton registerBtn = new JButton("등록");
        JButton cancelBtn = new JButton("취소");
        panel.add(registerBtn);
        panel.add(cancelBtn);

        add(panel);

        registerBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String publisher = publisherField.getText().trim();
            int totalPages = 0;
            try {
                totalPages = Integer.parseInt(totalPagesField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "총 페이지수는 숫자로 입력하세요.");
                return;
            }
            if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || totalPages <= 0) {
                JOptionPane.showMessageDialog(this, "모든 항목을 올바르게 입력하세요.");
                return;
            }
            Book book = new Book(title, author, publisher, totalPages);
            user.addBook(book);

            // 리스트 새로고침
            booksPanel.add(cardFactory.apply(book));
            booksPanel.revalidate();
            booksPanel.repaint();

            dispose();
        });

        cancelBtn.addActionListener(e -> dispose());
    }
}