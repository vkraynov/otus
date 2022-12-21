package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EntitySQLMetaDataImpl.
 *
 * @author Vadim_Kraynov
 * @since 21.12.2022
 */
public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + entityClassMetaData.getName().toLowerCase();
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT * FROM " + entityClassMetaData.getName().toLowerCase() + " WHERE " + entityClassMetaData.getIdField().getName().toLowerCase() + " = ?";
    }

    @Override
    public String getInsertSql() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String columns = fieldsWithoutId.stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
        String values = fieldsWithoutId.stream()
                .map(el -> "?")
                .collect(Collectors.joining(","));

        return "INSERT INTO " + entityClassMetaData.getName().toLowerCase() + " (" + columns + ") " + " values" + " (" + values + ")";
    }

    @Override
    public String getUpdateSql() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String updatedColumns = fieldsWithoutId.stream()
                .map(f -> f.getName() + " = ?")
                .collect(Collectors.joining(","));
        return "UPDATE " + entityClassMetaData.getName().toLowerCase() + " SET " + updatedColumns + " WHERE "
                + entityClassMetaData.getIdField().getName().toLowerCase() + " = ?";
    }
}
