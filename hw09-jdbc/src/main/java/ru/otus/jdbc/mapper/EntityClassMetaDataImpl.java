package ru.otus.jdbc.mapper;

import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import ru.otus.Id;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;
    private final Field[] fields;
    private final Field idField;
    private final Constructor<T> constructor;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> inputClass) {
        clazz = inputClass;
        fields = clazz.getDeclaredFields();
        idField = searchIdField(fields);
        constructor = searchConstructor(clazz);
        fieldsWithoutId = searchFieldsWithoutId(fields);
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() throws IncompleteAnnotationException {
        if (idField != null) {
            return idField;
        }
        throw new IncompleteAnnotationException(Id.class, "Аннотация не присутствует ни у одного поля класса");
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(fields);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    private Field searchIdField(Field[] fields) {
        Field tempField = null;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                tempField = field;
            }
        }
        return tempField;
    }

    private Constructor<T> searchConstructor(Class<T> tempClass) {
        try {
            return tempClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Field> searchFieldsWithoutId(Field[] fields) {
        List<Field> fieldsWithoutId = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }
            fieldsWithoutId.add(field);
        }
        return fieldsWithoutId;
    }
}
