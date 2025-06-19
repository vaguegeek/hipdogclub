package hipdogclub.Model;

import java.io.Serializable;

public class Memo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Book book;
    private String title;
    private String content;

    public Memo(Book book, String title, String content) {
        this.book = book;
        this.title = title;
        this.content = content;
    }

    public Book getBook() {
        return book;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}