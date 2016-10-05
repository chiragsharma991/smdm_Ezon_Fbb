package apsupportapp.aperotechnologies.com.designapp.model;

/**
 * Created by hasai on 28/09/16.
 */
public class VisualAssortCombo {


    VisualAssort visualAssort;
    VisualAssortComment visualAssortComment;
    String method;

    public void setVisualAssort(VisualAssort visualAssort) {
        this.visualAssort = visualAssort;
    }

    public VisualAssort getVisualAssort() {
        return visualAssort;
    }

    public void setVisualAssortComment(VisualAssortComment visualAssortComment) {
        this.visualAssortComment = visualAssortComment;
    }

    public VisualAssortComment getVisualAssortComment() {
        return visualAssortComment;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}

