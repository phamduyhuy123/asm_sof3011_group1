package com.nhom1.asm_sof3011_group1.dao;

import com.nhom1.asm_sof3011_group1.model.User;
import com.nhom1.asm_sof3011_group1.utils.JpaUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

public class UserDao extends DAO<User, Long>  {
    private  EntityManager em=JpaUtils.getEntityManger();
    public UserDao(){

    }
    public static void main(String[] args) {
		new UserDao().findAll();
	}

    @Override
    public Long insert(User var1) {
        return null;
    }

    @Override
    public Long update(User var1) {
        return null;
    }

    @Override
    public Long delete(Long var1) {
        return null;
    }

    @Override
    public User findById(Long var1) {
        String jpql="SELECT v FROM User v where v.id= :userId";
        TypedQuery<User> query=em.createQuery(jpql,User.class);
        query.setParameter("userId",var1);
        return query.getSingleResult();
    }


    @Override
    public List<User> findAll() {
        String jpql="SELECT v FROM User v";
        TypedQuery<User> query=em.createQuery(jpql,User.class);
        System.out.print(query.getResultList().toString());
        return query.getResultList();
    }
    public User checkLogin(String username, String password) {
    	String jpql="SELECT v FROM User v where v.username= :username and v.password =:password";
        TypedQuery<User> query=em.createQuery(jpql,User.class);
        query.setParameter("username",username);
        query.setParameter("password",password);
        return query.getSingleResult();
    }

    @Override
    protected List<User> selectBySql(String var1, Object... var2) {
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        em.close();
    }
}
