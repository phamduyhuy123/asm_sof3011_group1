package com.nhom1.asm_sof3011_group1.dao;

import com.nhom1.asm_sof3011_group1.model.Role;
import com.nhom1.asm_sof3011_group1.model.User;
import com.nhom1.asm_sof3011_group1.model.ViewHistory;
import com.nhom1.asm_sof3011_group1.utils.JpaUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

public class UserDao extends DAO<User, Long>  {

    public UserDao(){

    }


    @Override
    public Long insert(User var1) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(var1);
            entityManager.getTransaction().commit();
            return var1.getId();
		} catch (Exception e) {
            entityManager.getTransaction().rollback();
			throw new RuntimeException(e);
		}
    }

    @Override
    public Long update(User var1) {
        try {
            entityManager.getTransaction().begin();
            var1= entityManager.merge(var1);
            entityManager.refresh(var1);
            entityManager.getTransaction().commit();
            return var1.getId();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long delete(Long var1) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(findById(var1));
            entityManager.getTransaction().commit();
            return var1;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        UserDao userDao=new UserDao();
        userDao.delete((long)18);

    }
    @Override
    public User findById(Long var1) {
        entityManager.clear();
        String jpql="SELECT v FROM User v where v.id= :userId";
        TypedQuery<User> query=entityManager.createQuery(jpql,User.class);
        query.setParameter("userId",var1);
        return query.getResultList().isEmpty()?null:query.getSingleResult();
    }


    @Override
    public List<User> findAll() {
        String jpql="SELECT v FROM User v";
        TypedQuery<User> query=entityManager.createQuery(jpql,User.class);
        System.out.print(query.getResultList().toString());
        return query.getResultList();
    }
    public User checkLogin(String username, String password, Role role) {
    	String jpql="SELECT v FROM User v where v.username= :username and v.password =:password and v.role=:role";
        TypedQuery<User> query=entityManager.createQuery(jpql,User.class);
        query.setParameter("username",username);
        query.setParameter("password",password);
        query.setParameter("role",role);
        return query.getResultList().isEmpty()?null:query.getResultList().get(0);
    }
    public User findUserByEmail(User user){
        String jpql="SELECT v FROM User v where  v.email =:email";
        TypedQuery<User> query=entityManager.createQuery(jpql,User.class);

        query.setParameter("email",user.getEmail());
        List<User> users= query.getResultList();
        return users.isEmpty()?null:users.get(0);
    }public User findUserByUsername(User user){
        String jpql="SELECT v FROM User v where v.username= :username ";
        TypedQuery<User> query=entityManager.createQuery(jpql,User.class);
        query.setParameter("username",user.getUsername());
        List<User> users= query.getResultList();
        return users.isEmpty()?null:users.get(0);
    }
    public List<ViewHistory> findAllViewHistoryByUserId(Long userId){
        entityManager.clear();
        String jpql="SELECT v FROM ViewHistory v where v.user.id=:userId";
        TypedQuery<ViewHistory> query=entityManager.createQuery(jpql,ViewHistory.class);
        query.setParameter("userId",userId);
        return query.getResultList().isEmpty()?null:query.getResultList();
    }
    @Override
    protected List<User> selectBySql(String var1, Object... var2) {
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        entityManager.close();
    }
}
