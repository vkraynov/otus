package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        List<Field> allFields = entityClassMetaData.getAllFields();
        Constructor<T> constructor = entityClassMetaData.getConstructor();

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    List<Object> args = new ArrayList<>();
                    for (Field field : allFields) {
                        args.add(rs.getObject(rs.findColumn(field.getName())));
                    }
                    return Optional.of(constructor.newInstance(args.toArray()));
                }

                return Optional.<T>empty();

            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public List<T> findAll(Connection connection) {
        List<Field> allFields = entityClassMetaData.getAllFields();
        Constructor<T> constructor = entityClassMetaData.getConstructor();

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var dataList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    List<Object> args = new ArrayList<>();
                    for (Field field : allFields) {
                        args.add(rs.getObject(rs.findColumn(field.getName())));
                    }

                    dataList.add(constructor.newInstance(args.toArray()));
                }
                return dataList;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        List<Object> values = new ArrayList<>();
        for (Field field : fieldsWithoutId) {
            field.setAccessible(true);
            try {
                values.add(field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        List<Object> values = new ArrayList<>();
        for (Field field : fieldsWithoutId) {
            field.setAccessible(true);
            try {
                values.add(field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        Field idField = entityClassMetaData.getIdField();
        idField.setAccessible(true);
        try {
            values.add(idField.get(object));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
