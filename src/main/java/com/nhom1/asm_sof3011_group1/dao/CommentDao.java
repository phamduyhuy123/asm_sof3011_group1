package com.nhom1.asm_sof3011_group1.dao;

import com.nhom1.asm_sof3011_group1.model.Comment;
import com.nhom1.asm_sof3011_group1.model.User;
import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.JpaUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CommentDao extends DAO<Comment,Long>{
    private EntityManager em= JpaUtils.getEntityManger();
    @Override
    public Long insert(Comment var1) {
        try {
            em.getTransaction().begin();
            em.persist(var1);
            em.getTransaction().commit();
            return var1.getCommentId();
        }catch (Exception ex){
            em.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Long update(Comment var1) {
        return null;
    }

    @Override
    public Long delete(Long var1) {
        return null;
    }

    @Override
    public Comment findById(Long var1) {
        String jpql="SELECT c FROM Comment c  WHERE c.commentId=:id";
        TypedQuery<Comment> query=em.createQuery(jpql,Comment.class);
        query.setParameter("id",var1);
        return query.getSingleResult();
    }
    public List<Comment> findParentCommentsByVideoId(Long videoId){
        String jpql="SELECT c FROM Comment c  WHERE c.video.id=:videoId and c.parentComment is null ";
        TypedQuery<Comment> query=em.createQuery(jpql,Comment.class);
        query.setParameter("videoId",videoId);
        return query.getResultList();
    }
    public List<Comment> findChildrenCommentByParentId(Long parentId){
        String jpql="SELECT c FROM Comment c  WHERE c.parentComment.commentId=:parentId";
        TypedQuery<Comment> query=em.createQuery(jpql,Comment.class);
        query.setParameter("parentId",parentId);
        return query.getResultList();
    }

    public static void main(String[] args) {
        CommentDao commentDao=new CommentDao();
        List<Comment> comments=commentDao.findChildrenCommentByParentId((long)1);
        System.out.println(comments.get(0).getCommentText());
    }
    @Override
    public List<Comment> findAll() {

        return null;
    }

    @Override
    protected List<Comment> selectBySql(String var1, Object... var2) {
        return null;
    }
}
