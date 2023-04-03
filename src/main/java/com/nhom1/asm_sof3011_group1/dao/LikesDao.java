package com.nhom1.asm_sof3011_group1.dao;


import com.nhom1.asm_sof3011_group1.model.Like;
import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.JpaUtils;
import org.bytedeco.javacpp.freenect;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class LikesDao extends DAO<Like,Long>{
    private EntityManager em= JpaUtils.getEntityManger();
    @Override
    public Long insert(Like var1) {
        return null;
    }

    @Override
    public Long update(Like var1) {
        return null;
    }

    @Override
    public Long delete(Long var1) {
        return null;
    }

    @Override
    public Like findById(Long var1) {
        return null;
    }
    public List<Like> findByVideoId(Long videoId){
        String jpql="SELECT l FROM Like l where l.video.id=:videoId";
        TypedQuery<Like> query=em.createQuery(jpql,Like.class);
        query.setParameter("videoId",videoId);
        List<Like> list=query.getResultList();
        return list;
    }
    public VideoLikes countLikeAndDisLike(Long videoId){
        String jpql = "SELECT " +
                "SUM(CASE WHEN l.isLike = true THEN 1 ELSE 0 END), SUM(CASE WHEN l.isLike = false THEN 1 ELSE 0 END) " +
                "FROM Like l where l.video.id=:videoId " +
                "GROUP BY l.video.id";
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        query.setParameter("videoId",videoId);
        List<Object[]> likesAndDislikes = query.getResultList();
        List<VideoLikes> result = new ArrayList<>();
        for (Object[] row : likesAndDislikes) {
            Long likes = (Long) row[0];
            Long dislikes = (Long) row[1];
            result.add(new VideoLikes(likes, dislikes));
        }
        return result.isEmpty()?null:result.get(0);
    }
    public Like findByUserIdAndVideoId(Long userId, Long videoId){
        String jpsql="select l from Like l where l.video.id=:videoId and l.user.id=:userId";
        TypedQuery<Like> query=em.createQuery(jpsql,Like.class);
        query.setParameter("videoId",videoId);
        query.setParameter("userId",userId);

        return query.getResultList().isEmpty()?null:query.getSingleResult();
    }

    @Override
    public List<Like> findAll() {
        return null;
    }

    @Override
    protected List<Like> selectBySql(String var1, Object... var2) {
        return null;
    }
    public static class VideoLikes {


        private final Long likes;
        private final Long dislikes;

        public VideoLikes( Long likes, Long dislikes) {

            this.likes = likes;
            this.dislikes = dislikes;
        }

        public Long getLikes() {
            return likes;
        }

        public Long getDislikes() {
            return dislikes;
        }

    }
}
