package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createEntityFromResultSet(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var entityList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            entityList.add(createEntityFromResultSet(rs));
                        }
                        return entityList;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> parameters = getParametersWithoutId(client);
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), parameters);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> parameters = getParametersForUpdate(client);
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), parameters);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T createEntityFromResultSet(ResultSet rs) throws SQLException {
        try {
            Constructor<T> constructor = entityClassMetaData.getConstructor();
            T entity = constructor.newInstance();

            List<Field> allFields = entityClassMetaData.getAllFields();
            for (Field field : allFields) {
                field.setAccessible(true);
                Object value = rs.getObject(field.getName());
                field.set(entity, value);
            }

            return entity;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getParametersForUpdate(T entity) {
        List<Object> parameters = new ArrayList<>();
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        for (Field field : fieldsWithoutId) {
            try {
                field.setAccessible(true);
                parameters.add(field.get(entity));
            } catch (IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }
        try {
            Field idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            parameters.add(idField.get(entity));
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }

        return parameters;
    }

    private List<Object> getParametersWithoutId(T entity) {
        List<Object> resultList = new ArrayList<>();
        try {
            List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
            for (Field field : fieldsWithoutId) {
                field.setAccessible(true);
                resultList.add(field.get(entity));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
}
