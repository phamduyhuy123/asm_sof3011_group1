package com.nhom1.asm_sof3011_group1.utils;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtils {
    private static EntityManagerFactory factory;

    public static EntityManager getEntityManger(){
        if(factory==null || !factory.isOpen()){
            factory= Persistence.createEntityManagerFactory("PolyOE");

        }
        return factory.createEntityManager();
    }
    public static void shutDown(){
        if(factory!=null &&factory.isOpen()){
            factory.close();
        }
        factory=null;
    }

    public static void main(String[] args) {
        JpaUtils.getEntityManger();
    }


}
