package com.nhom1.asm_sof3011_group1.dao;

import com.nhom1.asm_sof3011_group1.model.Role;
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


    @Override
    public Long insert(User var1) {
        try {
        	em.getTransaction().begin();
        	em.persist(var1);
            em.getTransaction().commit();
            return var1.getId();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new RuntimeException(e);
		}
    }

    @Override
    public Long update(User var1) {
        try {
            em.getTransaction().begin();
            em.merge(var1);
            em.getTransaction().commit();
            return var1.getId();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long delete(Long var1) {
        try {
            em.getTransaction().begin();
            em.remove(findById(var1));
            em.getTransaction().commit();
            return var1;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        UserDao userDao=new UserDao();
        userDao.delete((long)18);

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
    public User checkLogin(String username, String password, Role role) {
    	String jpql="SELECT v FROM User v where v.username= :username and v.password =:password and v.role=:role";
        TypedQuery<User> query=em.createQuery(jpql,User.class);
        query.setParameter("username",username);
        query.setParameter("password",password);
        query.setParameter("role",role);
        return query.getResultList().isEmpty()?null:query.getResultList().get(0);
    }
    public User findUserByUsernameOrEmail(User user){
        String jpql="SELECT v FROM User v where v.username= :username or v.email =:email";
        TypedQuery<User> query=em.createQuery(jpql,User.class);
        query.setParameter("username",user.getUsername());
        query.setParameter("email",user.getEmail());
        List<User> users= query.getResultList();
        return users.isEmpty()?null:users.get(0);
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
