package com.example.spacenova.data;

public class Article {

    private int resId;
    private int articleNo;
    private String articleTitle;
    private String articleContent;
    private String author;
    private String date;

    public Article(int resId, int articleNo, String itemTitle, String itemContent, String author, String date) {
        this.resId = resId;
        this.articleNo = articleNo;
        this.author = author;
        this.articleTitle = itemTitle;
        this.articleContent = itemContent;
        this.date = date;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public int getArticleNo() {
        return articleNo;
    }

    public void setArticleNo(int articleNo) {
        this.articleNo = articleNo;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
