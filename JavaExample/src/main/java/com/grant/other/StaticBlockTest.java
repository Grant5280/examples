package com.grant.other;

public class StaticBlockTest{
    public static void main(String args[]) throws IllegalAccessException, InstantiationException {
        try{
            Class c = Class.forName("com.grant.other.Test",false,ClassLoader.getSystemClassLoader());
            Test t = (Test) c.newInstance();
            Test.display();
            t.display_1();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }
}
