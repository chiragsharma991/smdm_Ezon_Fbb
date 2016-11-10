package apsupportapp.aperotechnologies.com.designapp.model;

import org.json.JSONObject;

/**
 * Created by hasai on 27/09/16.
 */
public class VisualAssortComment extends JSONObject {

    private String articleOption;
    private String likeDislikeFlg;
    private String feedback;
    private int sizeSet;
    private String userId;
    private String modifiedDate;

    public void setArticleOption(String articleOption)
    {
        this.articleOption = articleOption;
    }

    public String getArticleOption() {
        return articleOption;
    }

    public void setLikeDislikeFlg(String likeDislikeFlg) {
        this.likeDislikeFlg = likeDislikeFlg;
    }

    public String getLikeDislikeFlg() {
        return likeDislikeFlg;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setSizeSet(int sizeSet) {
        this.sizeSet = sizeSet;
    }

    public int getSizeSet() {
        return sizeSet;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }
}
