package com.nhom1.asm_sof3011_group1.dao;




import javax.persistence.EntityManager;
import java.util.List;

public abstract class DAO <EntityType,KeyType> {
    protected EntityManager entityManager;
    public abstract void insert(EntityType var1);

    public abstract void update(EntityType var1);

    public abstract void delete(KeyType var1);

    public abstract EntityType findById(KeyType var1);

    public abstract List<EntityType> findAll();

    protected abstract List<EntityType> selectBySql(String var1, Object... var2);
}
