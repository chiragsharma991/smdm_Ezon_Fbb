package apsupportapp.aperotechnologies.com.designapp.ProductInformation;

/**
 * Created by pamrutkar on 16/10/17.
 */

public class StyleModel
{
    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionCode() {
        return collectionCode;
    }

    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    String collectionName;



    String collectionCode;

    public String getCollectionNames() {
        return collectionNames;
    }

    public void setCollectionNames(String collectionNames) {
        this.collectionNames = collectionNames;
    }

    public String getArticleOptions() {
        return articleOptions;
    }

    public void setArticleOptions(String articleOptions) {
        this.articleOptions = articleOptions;
    }

    public String getArticleOptionCode() {
        return articleOptionCode;
    }

    public void setArticleOptionCode(String articleOptionCode) {
        this.articleOptionCode = articleOptionCode;
    }

    String collectionNames;
    String articleOptions;
    String articleOptionCode;
}
