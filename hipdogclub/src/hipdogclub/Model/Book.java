package hipdogclub.Model;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String author;
    private String publisher;
    private int totalPages;
    private int currentPage;

    public Book(String title, String author, String publisher, int totalPages) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.totalPages = totalPages;
        this.currentPage = 0;
    }

    // 현재 페이지를 입력값으로 "갱신" (누적X, 덮어쓰기)
    public void setCurrentPage(int page) {
        if (page < 0) page = 0;
        if (page > totalPages) page = totalPages;
        this.currentPage = page;
    }

    // 진행률 (0~100)
    public int getProgressPercent() {
        if (totalPages == 0) return 0;
        return (int) Math.round((currentPage / (double) totalPages) * 100);
    }

    // 완독 여부
    public boolean isFinished() {
        return currentPage >= totalPages && totalPages > 0;
    }

    // Getter
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public int getTotalPages() { return totalPages; }
    public int getCurrentPage() { return currentPage; }

    // Setter (필요하다면)
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
}