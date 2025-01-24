package paf.workshop.paf_27w.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class Comment {
    
    @Schema(description="username", example="fred")
    private String user;

    @Schema(description="rating of game", example="5")
    private int rating;

    @Schema(description = "comment review body (optional)", requiredMode=RequiredMode.NOT_REQUIRED)
    private String comment;

    @Schema(description="game id", example="10")
    private int gid;

    @Schema(description="date of comment", example="2025-01-01")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date posted;

    public Comment() {
    }

    public Comment(String user, int rating, String comment, int gameId, Date posted) {
        this.user = user;
        this.rating = rating;
        this.comment = comment;
        this.gid = gameId;
        this.posted = posted;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gameId) {
        this.gid = gameId;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    @Override
    public String toString() {
        return "Comment [user=" + user + ", rating=" + rating + ", comment=" + comment + ", gid=" + gid + ", posted="
                + posted + "]";
    }

    
}
