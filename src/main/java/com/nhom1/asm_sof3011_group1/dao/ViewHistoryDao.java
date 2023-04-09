package com.nhom1.asm_sof3011_group1.dao;

import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.model.ViewHistory;
import com.nhom1.asm_sof3011_group1.utils.JpaUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ViewHistoryDao extends DAO<ViewHistory,Long>{
    private EntityManager em= JpaUtils.getEntityManger();
    @Override
    public Long insert(ViewHistory var1) {
        try {
            em.getTransaction().begin();
            em.persist(var1);
            em.getTransaction().commit();

            return var1.getHistoryId();
        }catch (Exception ex){
            em.getTransaction().rollback();
            throw new RuntimeException(ex);
        }

    }

    @Override
    public Long update(ViewHistory var1) {
        try {
            em.getTransaction().begin();
            var1= em.merge(var1);
            em.getTransaction().commit();
            return var1.getHistoryId();
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
    public ViewHistory findById(Long var1) {
        return null;
    }
    public List<ViewHistory> findAllByUserId(Long userId){
        em.clear();
        String jpql="SELECT v FROM ViewHistory v where v.user.id=:userId";
        TypedQuery<ViewHistory> query=em.createQuery(jpql,ViewHistory.class);
        query.setParameter("userId",userId);
        return query.getResultList().isEmpty()?null:query.getResultList();
    }
    public ViewHistory findByUserIdAndVideoId(Long userId, Long videoId){
        em.clear();
        String jpql="SELECT v FROM ViewHistory v where v.user.id=:userId and v.video.id=:videoId";
        TypedQuery<ViewHistory> query=em.createQuery(jpql,ViewHistory.class);
        query.setParameter("userId",userId);
        query.setParameter("videoId",videoId);
        return query.getResultList().isEmpty()?null:query.getResultList().get(0);
    }

    public static void main(String[] args) {
        ViewHistoryDao dao=new ViewHistoryDao();
        ViewHistory viewHistory1=dao.findByUserIdAndVideoId((long)16,(long)31);
        ViewHistory viewHistory2=dao.findByUserIdAndVideoId((long)16,(long)38);
        System.out.println(viewHistory1);
        System.out.println(viewHistory2);
    }
    @Override
    public List<ViewHistory> findAll() {
        return null;
    }

    @Override
    protected List<ViewHistory> selectBySql(String var1, Object... var2) {
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        em.close();
    }
}
