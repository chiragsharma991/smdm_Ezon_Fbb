package apsupportapp.aperotechnologies.com.designapp.model;

/**
 * Created by csuthar on 16/09/17.
 */

public class TestModel {

    int id;
    String attribute1;
    String attribute2;
    String attribute3;
    String attribute4;
    int attribute5;


    public TestModel(int id, String attribute1, String attribute2, String attribute3, String attribute4, int attribute5) {
        this.id = id;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public void setAttribute5(int attribute5) {
        this.attribute5 = attribute5;
    }

    public TestModel() {

    }

    public int getId() {
        return id;

    }

    public String getAttribute1() {
        return attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public int getAttribute5() {
        return attribute5;
    }

}

