package com.silica.blogapp6.Model;

public class PostModel {

    String title, blog, blog_two, blog_three, blog_four,
            category, picture, pic_two, pic_three, pic_four, id, uid, likes, views, date;

    public PostModel() {
    }

    public PostModel(String title, String blog, String category, String picture, String id, String uid, String likes, String views, String date) {
        this.title = title;
        this.blog = blog;
        this.category = category;
        this.picture = picture;
        this.id = id;
        this.uid = uid;
        this.likes = likes;
        this.views = views;
        this.date = date;
    }

    public PostModel(String title, String blog, String blog_two, String category, String picture, String pic_two, String id, String uid, String likes, String views, String date) {
        this.title = title;
        this.blog = blog;
        this.blog_two = blog_two;
        this.category = category;
        this.picture = picture;
        this.pic_two = pic_two;
        this.id = id;
        this.uid = uid;
        this.likes = likes;
        this.views = views;
        this.date = date;
    }

    public PostModel(String title, String blog, String blog_two, String blog_three, String category, String picture, String pic_two, String pic_three, String id, String uid, String likes, String views, String date) {
        this.title = title;
        this.blog = blog;
        this.blog_two = blog_two;
        this.blog_three = blog_three;
        this.category = category;
        this.picture = picture;
        this.pic_two = pic_two;
        this.pic_three = pic_three;
        this.id = id;
        this.uid = uid;
        this.likes = likes;
        this.views = views;
        this.date = date;
    }

    public PostModel(String title, String blog, String blog_two, String blog_three, String blog_four, String category, String picture, String pic_two, String pic_three, String pic_four, String id, String uid, String likes, String views, String date) {
        this.title = title;
        this.blog = blog;
        this.blog_two = blog_two;
        this.blog_three = blog_three;
        this.blog_four = blog_four;
        this.category = category;
        this.picture = picture;
        this.pic_two = pic_two;
        this.pic_three = pic_three;
        this.pic_four = pic_four;
        this.id = id;
        this.uid = uid;
        this.likes = likes;
        this.views = views;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getBlog() {
        return blog;
    }

    public String getBlog_two() {
        return blog_two;
    }

    public String getBlog_three() {
        return blog_three;
    }

    public String getBlog_four() {
        return blog_four;
    }

    public String getCategory() {
        return category;
    }

    public String getPicture() {
        return picture;
    }

    public String getPic_two() {
        return pic_two;
    }

    public String getPic_three() {
        return pic_three;
    }

    public String getPic_four() {
        return pic_four;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getLikes() {
        return likes;
    }

    public String getViews() {
        return views;
    }

    public String getDate() {
        return date;
    }
}
