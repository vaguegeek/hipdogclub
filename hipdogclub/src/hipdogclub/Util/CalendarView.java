package hipdogclub.Util;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import hipdogclub.Model.User;

public class CalendarView extends JPanel {
    // 유저, 연도, 월을 필드로 보관 (패널 새로고침용)
    private User user;
    private int year;
    private int month;
    private JPanel booksPanel;

    public CalendarView(User user, int year, int month) {
        this.user = user;
        this.year = year;
        this.month = month;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단: 이번달 출석일수, 누적 마일리지
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 20));

        int thisMonthAttendance = (int) user.getAttendanceLog().stream()
            .filter(d -> d.getYear() == year && d.getMonthValue() == month)
            .count();
        int totalMileage = user.getMileage();

        JLabel thisMonthLabel = new JLabel("이번달 출석 " + thisMonthAttendance + "일");
        thisMonthLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        infoPanel.add(thisMonthLabel);

        JLabel mileageLabel = new JLabel("누적 적립 " + totalMileage + "점");
        mileageLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        infoPanel.add(mileageLabel);

        add(infoPanel, BorderLayout.NORTH);

        // 중앙: 달력
        AttendanceCalendarPanel calendarPanel = new AttendanceCalendarPanel(year, month, user.getAttendanceLog());
        add(calendarPanel, BorderLayout.CENTER);

        // 하단: 이번 달 읽은 책 리스트
        booksPanel = new JPanel();
        booksPanel.setBackground(Color.WHITE);
        booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));
        booksPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        refreshBooksList();
        add(booksPanel, BorderLayout.SOUTH);
    }

    // 하단 책 리스트 새로고침
    public void refreshBooksList() {
        booksPanel.removeAll();

        List<String> books = user.getReadingRecords().stream()
            .filter(r -> YearMonth.from(r.getDate()).equals(YearMonth.of(year, month)))
            .map(r -> r.getBook().getTitle())
            .distinct()
            .collect(Collectors.toList());

        JLabel booksTitle = new JLabel("이번 달에 읽은 책");
        booksTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        booksTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        booksPanel.add(booksTitle);
        booksPanel.add(Box.createVerticalStrut(8));

        if (books.isEmpty()) {
            JLabel noneLabel = new JLabel("아직 읽은 책이 없습니다.");
            noneLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            noneLabel.setForeground(Color.GRAY);
            noneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            booksPanel.add(noneLabel);
        } else {
            for (String title : books) {
                JLabel bookLabel = new JLabel("• " + title);
                bookLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                bookLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                booksPanel.add(bookLabel);
            }
        }

        booksPanel.revalidate();
        booksPanel.repaint();
    }

    // 내부 클래스: 달력 패널
    private static class AttendanceCalendarPanel extends JPanel {
        private int year;
        private int month;
        private Set<LocalDate> attendanceLog;

        public AttendanceCalendarPanel(int year, int month, Set<LocalDate> attendanceLog) {
            this.year = year;
            this.month = month;
            this.attendanceLog = attendanceLog;
            setPreferredSize(new Dimension(700, 420));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 달력 설정
            Font headerFont = new Font("SansSerif", Font.BOLD, 18);
            Font dateFont = new Font("SansSerif", Font.PLAIN, 16);

            int cellWidth = getWidth() / 7;
            int cellHeight = (getHeight() - 40) / 6;

            // 요일 헤더
            String[] days = {"일", "월", "화", "수", "목", "금", "토"};
            g.setFont(headerFont);
            g.setColor(Color.BLACK);
            for (int i = 0; i < 7; i++) {
                int x = i * cellWidth + cellWidth / 2 - 12;
                g.drawString(days[i], x, 30);
            }

            // 이번 달 1일 요일 구하기
            LocalDate firstDay = LocalDate.of(year, month, 1);
            int firstDayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // 일요일=0

            int daysInMonth = firstDay.lengthOfMonth();
            g.setFont(dateFont);

            int day = 1;
            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 7; col++) {
                    int x = col * cellWidth;
                    int y = row * cellHeight + 40;

                    // 셀 테두리
                    g.setColor(new Color(230,230,230));
                    g.drawRect(x, y, cellWidth, cellHeight);

                    // 날짜 표시
                    if ((row == 0 && col < firstDayOfWeek) || day > daysInMonth) {
                        // 빈칸
                    } else {
                        g.setColor(Color.BLACK);
                        g.drawString(String.valueOf(day), x + 10, y + 25);

                        // 출석한 날이면 발바닥 아이콘 표시
                        LocalDate thisDate = LocalDate.of(year, month, day);
                        if (attendanceLog.contains(thisDate)) {
                            drawPawIcon(g, x + cellWidth / 2, y + cellHeight / 2);
                        }
                        day++;
                    }
                }
            }
        }

        // 발바닥 아이콘 직접 그리기
        private void drawPawIcon(Graphics g, int cx, int cy) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(220, 210, 190));
            g2.fillOval(cx - 14, cy - 10, 28, 20); // 메인 발바닥
            g2.fillOval(cx - 18, cy - 20, 10, 10); // 왼쪽
            g2.fillOval(cx + 8, cy - 20, 10, 10);  // 오른쪽
            g2.fillOval(cx - 7, cy - 22, 10, 10);  // 가운데 왼
            g2.fillOval(cx + 1, cy - 22, 10, 10);  // 가운데 오
        }
    }
}