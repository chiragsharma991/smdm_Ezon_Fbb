package apsupportapp.aperotechnologies.com.designapp.model;

/**
 * Created by pamrutkar on 10/01/17.
 */

public class VisualReport {

    int totalOptions;
    int likedOptions;
    int dislikedOptions;
    int pendingOptions;

    public int getPendingOptions() {
        return pendingOptions;
    }

    public void setPendingOptions(int pendingOptions) {
        this.pendingOptions = pendingOptions;
    }

    public int getTotalOptions() {
        return totalOptions;
    }

    public void setTotalOptions(int totalOptions) {
        this.totalOptions = totalOptions;
    }

    public int getLikedOptions() {
        return likedOptions;
    }

    public void setLikedOptions(int likedOptions) {
        this.likedOptions = likedOptions;
    }

    public int getDislikedOptions() {
        return dislikedOptions;
    }

    public void setDislikedOptions(int dislikedOptions) {
        this.dislikedOptions = dislikedOptions;
    }




}
