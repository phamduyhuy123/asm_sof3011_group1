package com.nhom1.asm_sof3011_group1.dao;

import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.JpaUtils;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class VideoDao extends DAO<Video,Long>{
    private  EntityManager em= JpaUtils.getEntityManger();


    @Override
    public Long insert(Video var1) {
        return null;
    }

    @Override
    public Long update(Video var1) {
        try {
            em.getTransaction().begin();
            em.merge(var1);
            em.getTransaction().commit();
            return var1.getId();
        }catch (Exception ex){
            em.getTransaction().rollback();
            throw new RuntimeException(ex);
        }

    }

    @Override
    public Long delete(Long var1) {
        return null;
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
    public List<Video> findAllPagination(String limit,String offSet){
        String jpql = "SELECT t FROM Video t ORDER BY t.id ASC";
        TypedQuery<Video> query = em.createQuery(jpql, Video.class);
        int limitInt = Integer.parseInt(limit);
        int offsetInt = Integer.parseInt(offSet);
        query.setFirstResult(offsetInt);
        query.setMaxResults(limitInt);
        List<Video> list = query.getResultList();
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
