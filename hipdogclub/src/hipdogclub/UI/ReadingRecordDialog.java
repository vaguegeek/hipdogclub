package hipdogclub.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import hipdogclub.Model.*;

public class ReadingRecordDialog extends JDialog {
    private DefaultTableModel tableModel;
    private User user;
    private Book book;

    // 콜백 파라미터 추가
    public ReadingRecordDialog(JDialog parent, User user, Book book, Runnable onRecordChanged) {
        super(parent, "오늘의 독서기록", true);
        this.user = user;
        this.book = book;

        setSize(720, 430);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 제목
        JLabel title = new JLabel("오늘의 독서기록");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(16, 24, 10, 24));

        // 입력 패널
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 0));

        JLabel pagesLabel = new JLabel("읽은 페이지수");
        pagesLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        JTextField pagesField = new JTextField(5);
        pagesField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        JLabel totalLabel = new JLabel("/ " + book.getTotalPages() + "p");
        totalLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JLabel dateLabel = new JLabel("읽은 날짜");
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        JFormattedTextField dateField = new JFormattedTextField(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        dateField.setValue(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        dateField.setColumns(10);
        dateField.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JFormattedTextField timeField = new JFormattedTextField(DateTimeFormatter.ofPattern("HH:mm"));
        timeField.setValue(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeField.setColumns(6);
        timeField.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JButton addBtn = new JButton("등록확인");
        addBtn.setBackground(Color.BLACK);
        addBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        addBtn.setPreferredSize(new Dimension(100, 34));

        addBtn.addActionListener(e -> {
            // 독서기록 추가 후
            if (onRecordChanged != null) onRecordChanged.run();
        });

        inputPanel.add(pagesLabel);
        inputPanel.add(pagesField);
        inputPanel.add(totalLabel);
        inputPanel.add(Box.createHorizontalStrut(16));
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);
        inputPanel.add(timeField);
        inputPanel.add(addBtn);

        // 구분선
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(Color.BLACK);

        // 테이블
        String[] colNames = {"번호", "읽은 페이지/총 페이지", "읽은 날짜", "등록일", "관리"};
        tableModel = new DefaultTableModel(colNames, 0);
        JTable table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table.setRowHeight(32);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(Color.BLACK);

        // 삭제 버튼 렌더러
        table.getColumn("관리").setCellRenderer((tbl, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("삭제");
            btn.setForeground(Color.BLACK);
            btn.setBackground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setFocusPainted(false);
            return btn;
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);

        // 테이블 데이터 채우기
        updateTable();

        // 등록확인 버튼 이벤트 (입력값으로 갱신)
        addBtn.addActionListener(e -> {
            try {
                String input = pagesField.getText().trim();
                input = input.replaceAll("[^0-9]", ""); // 숫자 이외 문자 모두 제거
                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "숫자를 입력하세요.");
                    return;
                }
                int pages = Integer.parseInt(input);
                if (pages <= 0 || pages > book.getTotalPages()) {
                    JOptionPane.showMessageDialog(this, "올바른 페이지수를 입력하세요.");
                    return;
                }
                LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                LocalTime time = LocalTime.parse(timeField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                LocalDateTime dateTime = LocalDateTime.of(date, time);
                // 기록 추가 (입력값으로 갱신, 마일리지는 누적)
                user.addReadingRecord(book, pages);

                updateTable();
                pagesField.setText("");
                // 콜백: BookDetailDialog, DashboardView 등 동기화
                if (onRecordChanged != null) onRecordChanged.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력값을 확인하세요.");
            }
        });

        // 삭제 버튼 이벤트
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 4) { // 관리-삭제
                    int confirm = JOptionPane.showConfirmDialog(
                        ReadingRecordDialog.this, "정말 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        List<ReadingRecord> records = user.getReadingRecords().stream()
                            .filter(r -> r.getBook().equals(book))
                            .toList();
                        if (row >= 0 && row < records.size()) {
                            ReadingRecord recordToRemove = records.get(row);
                            user.removeReadingRecord(recordToRemove);

                            // Book의 currentPage 최신 독서기록으로 갱신
                            List<ReadingRecord> bookRecords = user.getReadingRecords().stream()
                                .filter(r -> r.getBook().equals(book))
                                .toList();
                            if (!bookRecords.isEmpty()) {
                                int lastPage = bookRecords.get(bookRecords.size() - 1).getPagesRead();
                                book.setCurrentPage(lastPage);
                            } else {
                                book.setCurrentPage(0);
                            }

                            updateTable();
                            if (onRecordChanged != null) onRecordChanged.run();
                        }
                    }
                }
            }
        });


        // 전체 배치
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(sep1, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    // 테이블 갱신: 항상 Book의 최신 currentPage 기준으로 표시
    private void updateTable() {
        tableModel.setRowCount(0);
        List<ReadingRecord> records = user.getReadingRecords().stream()
            .filter(r -> r.getBook().equals(book))
            .toList();
        int idx = 1;
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        DateTimeFormatter regFmt = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        for (ReadingRecord r : records) {
            tableModel.addRow(new Object[]{
                idx++,
                r.getPagesRead() + "/" + book.getTotalPages() + "p",
                r.getDateTime().format(dateFmt),
                r.getDate().format(regFmt),
                "삭제"
            });
        }
    }
}