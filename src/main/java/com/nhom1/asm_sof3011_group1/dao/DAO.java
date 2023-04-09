package com.nhom1.asm_sof3011_group1.dao;




import com.nhom1.asm_sof3011_group1.utils.JpaUtils;

import javax.persistence.EntityManager;
import java.util.List;

public abstract class DAO <EntityType,KeyType> {
    protected EntityManager entityManager= JpaUtils.getEntityManger();;
    public abstract KeyType insert(EntityType var1);

    public abstract KeyType update(EntityType var1);

    public abstract KeyType delete(KeyType var1);

    public abstract EntityType findById(KeyType var1);

    public abstract List<EntityType> findAll();

    protected abstract List<EntityType> selectBySql(String var1, Object... var2);
}
