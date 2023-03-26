package com.nhom1.asm_sof3011_group1.dao;

import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.JpaUtils;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class VideoDao extends DAO<Video,Long>{
    private  EntityManager em= JpaUtils.getEntityManger();
    @Override
    public void insert(Video var1) {

    }

    @Override
    public void update(Video var1) {

    }

    @Override
    public void delete(Long var1) {

    }

    @Override
    public Video findById(Long var1) {
        Video video=em.find(Video.class,var1);
        return video;
    }

    @Override
    public List<Video> findAll() {
        String jpql="SELECT v FROM Video v ";
        TypedQuery<Video> query=em.createQuery(jpql,Video.class);
        List<Video> list=query.getResultList();
        return list;
    }

    @Override
    protected List<Video> selectBySql(String var1, Object... var2) {
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        em.close();
    }
}
