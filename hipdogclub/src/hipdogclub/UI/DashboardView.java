package hipdogclub.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import hipdogclub.Model.User;
import hipdogclub.Model.Book;
import hipdogclub.Util.CalendarView;

public class DashboardView {
    private JPanel panel;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JButton[] tabButtons;
    private JFrame mainFrame;
    private User user;
    private JPanel booksPanel;
    private CalendarView calendarViewInstance;

    public DashboardView(User user, JFrame mainFrame) {
        this.user = user;
        this.mainFrame = mainFrame;

        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // 1. 상단 탭 메뉴 (가운데 정렬)
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tabPanel.setBackground(Color.WHITE);

        String[] tabNames = {"활동현황", "나의서재", "출석체크"};
        tabButtons = new JButton[tabNames.length];
        Font tabFont = new Font("SansSerif", Font.BOLD, 17);

        for (int i = 0; i < tabNames.length; i++) {
            JButton btn = new JButton(tabNames[i]);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
            btn.setContentAreaFilled(false);
            btn.setFont(tabFont);
            btn.setForeground(i == 0 ? Color.BLACK : new Color(180, 180, 180));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            int idx = i;
            btn.addActionListener(e -> switchTab(idx));
            tabPanel.add(btn);
            tabButtons[i] = btn;
        }
        panel.add(tabPanel, BorderLayout.NORTH);

        // 2. 컨텐츠 영역 (카드레이아웃)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // 2-1. 활동현황 패널 (사용자별 데이터 반영)
        JPanel activityPanel = createActivityPanel(user, mainFrame);
        contentPanel.add(activityPanel, "활동현황");

        // 2-2. 나의서재 패널 (도서 등록/리스트)
        JPanel libraryPanel = createLibraryPanel();
        contentPanel.add(libraryPanel, "나의서재");


        // 2-3. 출석체크 패널
        LocalDate now = LocalDate.now();
        calendarViewInstance = new CalendarView(user, now.getYear(), now.getMonthValue());
        contentPanel.add(calendarViewInstance, "출석체크");

        panel.add(contentPanel, BorderLayout.CENTER);
    }

    // 탭 전환 메서드
    private void switchTab(int idx) {
        for (int i = 0; i < tabButtons.length; i++) {
            tabButtons[i].setForeground(i == idx ? Color.BLACK : new Color(180, 180, 180));
            tabButtons[i].setFont(new Font("SansSerif", i == idx ? Font.BOLD : Font.PLAIN, 17));
        }
        cardLayout.show(contentPanel, tabButtons[idx].getText());
    }

    // 활동현황 패널 생성 (사용자별 데이터 반영)
    private JPanel createActivityPanel(User user, JFrame mainFrame) {
        JPanel activityPanel = new JPanel();
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));
        activityPanel.setBorder(BorderFactory.createEmptyBorder(32, 60, 32, 60));

        // 1. 환영 메시지
        JLabel welcome = new JLabel("주예나님 환영합니다!");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 22));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        activityPanel.add(welcome);

        // 2. 가입 정보
        JLabel info = new JLabel("2025년 힙독클럽 1기 | 가입기간 2025.04.01 ~ 2025.12.31");
        info.setFont(new Font("SansSerif", Font.PLAIN, 13));
        info.setForeground(new Color(120, 120, 120));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        activityPanel.add(Box.createVerticalStrut(10));
        activityPanel.add(info);

        activityPanel.add(Box.createVerticalStrut(32));

        // 3. 오늘의 출석체크
        JPanel checkPanel = new JPanel();
        checkPanel.setBackground(new Color(248, 248, 248));
        checkPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230,230,230)),
                BorderFactory.createEmptyBorder(18, 24, 18, 24)
        ));
        checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.X_AXIS));
        checkPanel.setMaximumSize(new Dimension(480, 60));
        checkPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel checkTitle = new JLabel("오늘의 출석체크");
        checkTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        checkPanel.add(checkTitle);

        checkPanel.add(Box.createHorizontalStrut(28));

        // 이번 달 출석일 계산
        int thisMonthAttendance = (int) user.getAttendanceLog().stream()
                .filter(date -> date.getMonthValue() == LocalDate.now().getMonthValue()
                             && date.getYear() == LocalDate.now().getYear())
                .count();
        JLabel monthLabel = new JLabel("이번달 출석 " + thisMonthAttendance + "일");
        monthLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        monthLabel.setForeground(new Color(90, 90, 90));
        checkPanel.add(monthLabel);

        checkPanel.add(Box.createHorizontalStrut(28));

        // 오늘 출석 여부
        boolean todayChecked = user.getAttendanceLog().contains(LocalDate.now());
        JButton checkBtn = new JButton(todayChecked ? "출석완료" : "출석하기");
        checkBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkBtn.setBackground(new Color(230,230,230));
        checkBtn.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
        checkBtn.setFocusPainted(false);
        checkBtn.setEnabled(!todayChecked);

        // 출석 버튼 클릭 시 처리
        checkBtn.addActionListener(e -> {
            user.checkAttendance();
            // 대시보드 새로고침
            mainFrame.setContentPane(new DashboardView(user, mainFrame).getPanel());
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        checkPanel.add(checkBtn);

        activityPanel.add(checkPanel);

        activityPanel.add(Box.createVerticalStrut(32));

        // 4. 마일리지바
        JPanel mileagePanel = new JPanel();
        mileagePanel.setBackground(Color.WHITE);
        mileagePanel.setLayout(new BoxLayout(mileagePanel, BoxLayout.X_AXIS));
        mileagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 등급 계산
        int mileage = user.getMileage();
        int[] section = {0, 300, 800, 1500, 3000, 5000};
        String[] grades = {"힙독이", "현무", "청룡", "백호", "주작", "해치"};
        int gradeIndex = 0;
        for (int i = 0; i < section.length - 1; i++) {
            if (mileage >= section[i]) gradeIndex = i;
        }
        String gradeName = grades[gradeIndex];
        int nextLevel = (gradeIndex + 1 < section.length) ? section[gradeIndex + 1] : section[gradeIndex];
        int needed = Math.max(0, nextLevel - mileage);

        // 캐릭터(원형)
        JPanel charPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 225, 180));
                g.fillOval(0, 0, 80, 80);
            }
        };
        charPanel.setPreferredSize(new Dimension(80, 80));
        charPanel.setMaximumSize(new Dimension(80, 80));
        charPanel.setMinimumSize(new Dimension(80, 80));
        charPanel.setOpaque(false);
        charPanel.setLayout(new BorderLayout());

        JLabel charLabel = new JLabel(gradeName, SwingConstants.CENTER);
        charLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        charPanel.add(charLabel, BorderLayout.CENTER);

        JLabel gradeLabel = new JLabel("(" + (gradeIndex + 1) + "단계)", SwingConstants.CENTER);
        gradeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        charPanel.add(gradeLabel, BorderLayout.SOUTH);

        mileagePanel.add(charPanel);
        mileagePanel.add(Box.createHorizontalStrut(32));

        // 마일리지 점수 및 바
        JPanel barPanel = new JPanel();
        barPanel.setBackground(Color.WHITE);
        barPanel.setLayout(new BoxLayout(barPanel, BoxLayout.Y_AXIS));
        barPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel mileageScore = new JLabel("마일리지 " + mileage + "점");
        mileageScore.setFont(new Font("SansSerif", Font.BOLD, 16));
        barPanel.add(mileageScore);
        barPanel.add(Box.createVerticalStrut(10));

        // 바
        JPanel barBg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Color[] colors = {
                    new Color(255, 180, 140),
                    new Color(255, 140, 100),
                    new Color(255, 100, 100),
                    new Color(180, 180, 180),
                    new Color(220, 220, 220)
                };
                int width = getWidth();
                int height = getHeight();
                int total = section[section.length-1];

                int x = 0;
                for (int i = 0; i < section.length-1; i++) {
                    int segWidth = (int)((section[i+1] - section[i]) * width / (double)total);
                    g.setColor(colors[i < colors.length ? i : colors.length-1]);
                    g.fillRect(x, 0, segWidth, height);
                    x += segWidth;
                }
                // 마일리지 표시
                int markX = (int)(mileage * width / (double)total);
                g.setColor(Color.RED);
                g.fillRect(markX-2, 0, 4, height);
            }
        };
        barBg.setPreferredSize(new Dimension(300, 16));
        barBg.setMaximumSize(new Dimension(300, 16));
        barBg.setMinimumSize(new Dimension(300, 16));
        barBg.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
        barPanel.add(barBg);

        // 등급 구간 라벨
        JPanel gradeBarLabel = new JPanel();
        gradeBarLabel.setBackground(Color.WHITE);
        gradeBarLabel.setLayout(new BoxLayout(gradeBarLabel, BoxLayout.X_AXIS));
        for (int i = 0; i < grades.length; i++) {
            JLabel gLabel = new JLabel(grades[i] + " " + section[i] + "점");
            gLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            gradeBarLabel.add(gLabel);
            if (i < grades.length-1) gradeBarLabel.add(Box.createHorizontalStrut(10));
        }
        barPanel.add(gradeBarLabel);

        // 다음 등급 안내
        String nextGradeMsg = (needed > 0) ? "다음 등급으로 마일리지 " + needed + "점 더 필요합니다." : "최고 등급입니다!";
        JLabel nextGrade = new JLabel(nextGradeMsg);
        nextGrade.setFont(new Font("SansSerif", Font.PLAIN, 13));
        nextGrade.setForeground(Color.DARK_GRAY);
        barPanel.add(Box.createVerticalStrut(10));
        barPanel.add(nextGrade);

        mileagePanel.add(barPanel);

        activityPanel.add(mileagePanel);

        return activityPanel;
    }

    // 나의서재 패널 생성 (도서 등록/리스트)
    private JPanel createLibraryPanel() {
        JPanel libraryPanel = new JPanel();
        libraryPanel.setBackground(Color.WHITE);
        libraryPanel.setLayout(new BorderLayout());

        // 상단: 도서 등록 버튼
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        topPanel.setBackground(Color.WHITE);
        JButton addBookBtn = new JButton("도서 등록");
        addBookBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        addBookBtn.setBackground(new Color(240,240,240));
        addBookBtn.setFocusPainted(false);
        topPanel.add(addBookBtn);
        libraryPanel.add(topPanel, BorderLayout.NORTH);

        // 도서 카드 리스트 (필드로 보관)
        booksPanel = new JPanel();
        booksPanel.setBackground(Color.WHITE);
        booksPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 24, 24));
        refreshBooksPanel();

        JScrollPane scrollPane = new JScrollPane(booksPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        libraryPanel.add(scrollPane, BorderLayout.CENTER);

        // 도서 등록 버튼 클릭 시 팝업
        addBookBtn.addActionListener(e -> {
            BookRegisterDialog dialog = new BookRegisterDialog(
                mainFrame, user, booksPanel, b -> createBookCard(b)
            );
            dialog.setVisible(true);
            // 등록 후 새로고침
            refreshBooksPanel();
            // 출석체크 리스트도 갱신
            if (calendarViewInstance != null) calendarViewInstance.refreshBooksList();
        });

        return libraryPanel;
    }

    // 도서 카드 리스트 새로고침 (진행률/상태 반영)
    private void refreshBooksPanel() {
        booksPanel.removeAll();
        for (Book book : user.getBookShelf()) {
            booksPanel.add(createBookCard(book));
        }
        booksPanel.revalidate();
        booksPanel.repaint();
    }

    // 도서 카드 생성 (클릭 시 상세 팝업, 기록 추가 후 새로고침)
    private JPanel createBookCard(Book book) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(160, 240));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // 책 표지(이미지 대신 색상/라벨)
        JPanel cover = new JPanel();
        cover.setPreferredSize(new Dimension(140, 180));
        cover.setMaximumSize(new Dimension(140, 180));
        cover.setBackground(new Color(255, 244, 200));
        JLabel titleLabel = new JLabel("<html><center>" + book.getTitle() + "</center></html>");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cover.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(cover);

        // 독서 상태
        String status = book.isFinished() ? "완독" : (book.getCurrentPage() > 0 ? "읽는중" : "미시작");
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(book.isFinished() ? new Color(255,140,0) : new Color(0, 150, 136));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(8));
        card.add(statusLabel);

        // 진행률
        int percent = (int)(book.getCurrentPage() * 100.0 / Math.max(1, book.getTotalPages()));
        JLabel percentLabel = new JLabel(percent + "%");
        percentLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        percentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(percentLabel);

        // 저자/출판사
        JLabel authorLabel = new JLabel(book.getAuthor());
        authorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(authorLabel);

        JLabel publisherLabel = new JLabel(book.getPublisher());
        publisherLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        publisherLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(publisherLabel);

        // 카드 클릭 시 상세 팝업
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                BookDetailDialog dialog = new BookDetailDialog(mainFrame, user, book, () -> {
                    refreshBooksPanel();
                    if (calendarViewInstance != null) calendarViewInstance.refreshBooksList();
                    // 대시보드 전체 새로고침
                    if (mainFrame != null) {
                        mainFrame.setContentPane(new DashboardView(user, mainFrame).getPanel());
                        mainFrame.revalidate();
                        mainFrame.repaint();
                    }
                });
                dialog.setVisible(true);
            }
        });

        return card;
    }

    public JPanel getPanel() {
        return panel;
    }
}