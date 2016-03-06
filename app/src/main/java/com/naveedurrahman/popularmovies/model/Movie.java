package com.naveedurrahman.popularmovies.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Movie implements Parcelable {


    @SerializedName("id")
    private int id;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("title")
    private String title;
    @SerializedName("popularity")
    private float popularity;
    @SerializedName("video")
    private boolean video;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("vote_average")
    private float voteAverage;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(title);
        dest.writeFloat(popularity);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeList(genreIds);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(backdropPath);
        dest.writeInt(voteCount);
        dest.writeFloat(voteAverage);
    }

    public Movie() {
    }

    /**
     * A private constructor that can only be used by the CREATOR field.
     * You have to read the data from the Parcel in the exact same order you wrote it.
     *
     * @param in used to retrieve the values that we originally wrote into the `Parcel`
     */
    private Movie(Parcel in) {
        id = in.readInt();
        adult = in.readByte() != 0;
        overview = in.readString();
        title = in.readString();
        popularity = in.readFloat();
        video = in.readByte() != 0;
        posterPath = in.readString();
        releaseDate = in.readString();
        in.readList(genreIds, null);
        originalTitle = in.readString();
        originalLanguage = in.readString();
        backdropPath = in.readString();
        voteCount = in.readInt();
        voteAverage = in.readFloat();

    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Creator<Movie> CREATOR
            = new Creator<Movie>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}