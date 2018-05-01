package mrodkiewicz.pl.bakingapp.db.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Step {
    @SerializedName("id")
    @PrimaryKey
    private Integer id;
    @ColumnInfo(name = "shortDescription")
    @SerializedName("shortDescription")
    private String shortDescription;
    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;
    @ColumnInfo(name = "videoURL")
    @SerializedName("videoURL")
    private String videoURL;
    @ColumnInfo(name = "thumbnailURL")
    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    public Step(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
