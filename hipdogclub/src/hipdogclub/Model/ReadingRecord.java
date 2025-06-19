package hipdogclub.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReadingRecord implements Serializable{
    private Book book;
    private int pagesRead;
    private LocalDate date;

    public ReadingRecord(Book book, int pagesRead, LocalDate date) {
        this.book = book;
        this.pagesRead = pagesRead;
        this.date = date;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getDateTime() {
        return date.atTime(java.time.LocalTime.now());
    }

    public int getPagesRead() {
        return pagesRead;
    }
}