package ru.otus.jdbc.mapper;

import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import ru.otus.Id;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> inputClass) {
        clazz = inputClass;
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() throws IncompleteAnnotationException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new IncompleteAnnotationException(Id.class, "Аннотация не присутствует ни у одного поля класса");
    }

    @Override
    public List<Field> getAllFields() {
        Field[] fields = clazz.getDeclaredFields();
        return List.of(fields);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        Field[] fields = clazz.getDeclaredFields();
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
