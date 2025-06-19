package hipdogclub.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String password; // 비밀번호 추가
    private LocalDate joinDate;
    private int mileage;

    private List<Book> bookShelf = new ArrayList<>();
    private List<ReadingRecord> readingRecords = new ArrayList<>();
    private List<Memo> memos = new ArrayList<>();
    private Set<LocalDate> attendanceLog = new HashSet<>();

    // 생성자 (회원가입 시 아이디, 비밀번호 필요)
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.joinDate = LocalDate.now();
        this.mileage = 0;
    }

    // 출석체크: 오늘 출석하면 마일리지 +10
    public void checkAttendance() {
        LocalDate today = LocalDate.now();
        if (attendanceLog.contains(today)) {
            return;
        } else {
            attendanceLog.add(today);
            mileage += 10;
        }
    }

    // 책 등록
    public void addBook(Book book) {
        bookShelf.add(book);
    }

    // 독서 기록 추가: 입력값으로 페이지 갱신, 마일리지 누적
    public void addReadingRecord(Book book, int pagesRead) {
        book.setCurrentPage(pagesRead); // 입력값으로 갱신(누적X)
        mileage += 10; // 기록 1회당 10점(원하는대로 조정)
        readingRecords.add(new ReadingRecord(book, pagesRead, LocalDate.now()));
    }

    // 감상평 등록: 마일리지 +20
    public void addMemo(Book book, String title, String content) {
        memos.add(new Memo(book, title, content));
        mileage += 20;
    }

    // Getter
    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public int getMileage() {
        return mileage;
    }

    public List<Book> getBookShelf() {
        return bookShelf;
    }

    public List<ReadingRecord> getReadingRecords() {
        return readingRecords;
    }

    public List<Memo> getMemos() {
        return memos;
    }

    public Set<LocalDate> getAttendanceLog() {
        return attendanceLog;
    }

    public void removeReadingRecord(ReadingRecord record) {
    readingRecords.remove(record);
    }

    // Setter (필요시)
    public void setPassword(String password) {
        this.password = password;
    }
}