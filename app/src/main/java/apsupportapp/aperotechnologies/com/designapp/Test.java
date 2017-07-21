package apsupportapp.aperotechnologies.com.designapp;

/**
 * Created by csuthar on 21/07/17.
 */

class Test implements Cloneable{
    int rollno;
    String name;

    Test(int rollno,String name){
        this.rollno=rollno;
        this.name=name;
    }


    public static void main(String args[]){
        try{
            Test s1=new Test(101,"amit");

            Test s2=(Test)s1.clone();

            System.out.println(s1.rollno+" "+s1.name);
            System.out.println(s2.rollno+" "+s2.name);

        }catch(CloneNotSupportedException c){}

    }
}