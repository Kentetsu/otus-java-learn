package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;
    private final List<String> listOfNames;
    private final String tableName;
    private final List<String> listOfNamesWithoutId;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaDataClient) {
        this.entityClassMetaData = entityClassMetaDataClient;
        this.listOfNames =
                entityClassMetaData.getAllFields().stream().map(Field::getName).toList();
        this.tableName =
                Arrays.asList(entityClassMetaData.getName().split("\\.")).getLast();
        this.listOfNamesWithoutId = entityClassMetaDataClient.getFieldsWithoutId().stream()
                .map(Field::getName)
                .toList();
        ;
    }

    @Override
    public String getSelectAllSql() {
        String result = String.format("SELECT %s FROM %s;", String.join(", ", listOfNames), tableName);
        return result;
    }

    @Override
    public String getSelectByIdSql() {
        String idColumnName = entityClassMetaData.getIdField().getName();

        String result = String.format(
                "SELECT %s FROM %s WHERE %s = ?;", String.join(", ", listOfNames), tableName, idColumnName);
        return result;
    }

    @Override
    public String getInsertSql() {
        String columns = String.join(", ", listOfNamesWithoutId);
        String placeholders =
                String.join(", ", listOfNamesWithoutId.stream().map(s -> "?").toList());
        return String.format("INSERT INTO %s (%s) VALUES (%s);", tableName, columns, placeholders);
    }

    @Override
    public String getUpdateSql() {
        String idColumnName = entityClassMetaData.getIdField().getName();
        StringBuilder accumulator = new StringBuilder(String.format("UPDATE %s SET ", tableName));

        for (String fieldName : listOfNamesWithoutId) {
            accumulator.append(String.format("%s = ?, ", fieldName));
        }

        if (!listOfNamesWithoutId.isEmpty()) {
            accumulator.delete(accumulator.length() - 2, accumulator.length());
        }

        accumulator.append(String.format(" WHERE %s = ?;", idColumnName));
        return accumulator.toString();
    }
}
