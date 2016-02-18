package com.kz.jv.jollyvolly.tabs.news;

/**
 * Created by madiyarzhenis on 10.09.15.
 */
public class NewsSale {
    String objectId;
    String title;
    String description;
    String imageUrl;
    int likeCount;
    int liked;

    public NewsSale(String objectId, String title, String description, String imageUrl, int likeCount, int liked) {
        this.objectId = objectId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.liked = liked;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
