package com.republic.entities;

/** *
 * Created by Akwasi Owusu on 7/17/15.
 */
public class Corruption {

    private CorruptionType corruptionType;
    private String location;
    private String description;
    private String mediaFilePath;
    private String postId;
    private MediaType mediaType;
    private User user;

    public CorruptionType getCorruptionType() {
        return corruptionType;
    }

    public void setCorruptionType(CorruptionType corruptionType) {
        this.corruptionType = corruptionType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaFilePath() {
        return mediaFilePath;
    }

    public void setMediaFilePath(String mediaFilePath) {
        this.mediaFilePath = mediaFilePath;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
