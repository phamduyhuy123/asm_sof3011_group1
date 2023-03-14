package com.nhom1.asm_sof3011_group1.dao;

import com.nhom1.asm_sof3011_group1.model.User;



import java.util.List;
import java.util.UUID;

public class UserDao extends DAO<User, UUID>  {

    public UserDao(){

    }
    @Override
    public void insert(User var1) {

    }

    @Override
    public void update(User var1) {

    }

    @Override
    public void delete(UUID var1) {

    }

    @Override
    public User findById(UUID var1) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    protected List<User> selectBySql(String var1, Object... var2) {
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
