package com.github.thiagosqr.viewmodel;

/**
 * Created by thiago on 24/07/17.
 */

import javaslang.control.Try;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

public interface ViewModel<V,E> {

    V from(final E entity);

    E toTarget();

    default List<Object> asListofValues(final String... fields) {
        return asListofValues(null, 0, null, fields);
    }

    default List<Object> asListofValues(final Function<Object, String> f, final Integer additionalValOrder, final String fieldForAdditionalVal, final String... fields) {
        final List<Object> values = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            try {
                if(additionalValOrder == i && f != null){
                    values.add(asObject(f.apply(getMethodValue(fieldForAdditionalVal))));
                }
                values.add(asObject(getMethodValue(fields[i])));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    default Map<String, Object> asMapofValues(final String... fields) {
        return asMapofValues(null, null, null, fields);
    }

    default Map<String,Object> asMapofValues(final Function<Object, String> f, final String additionalField, final String fieldForAdditionalVal, final String... fields) {
        final Map<String,Object> map = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {

            if (fields[i].split("-").length > 1) {
                map.put(fields[i],getNestedAttributtesAsString(fields[i], this, 0));
            } else {
                try {
                    if(f != null && !map.containsKey(additionalField)) {
                        map.put(additionalField, asObject(f.apply(getMethodValue(fieldForAdditionalVal))));
                    }
                    map.put(fields[i], asObject(getMethodValue(fields[i])));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }


        }
        return map;
    }

    default Object getNestedAttributtesAsString(final String field,final Object nestedObject, int i) {
        while (i < field.split("-").length ) {
            try {
                return asObject(getNestedAttributtesAsString(field,((ViewModel)nestedObject).getMethodValue(field.split("-")[i]), ++i));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                return "";
            }
        }
        return asObject(nestedObject);
    }

    default Object asObject(final Object fieldValue) {

        if(fieldValue == null){
            return "";
        }else if (Date.class.isInstance(fieldValue)) {

            final LocalDate ld = Try.of(() -> ((java.sql.Date) fieldValue).toLocalDate())
                    .recover(e -> ((java.util.Date) fieldValue).toInstant().atZone( ZoneId.systemDefault()).toLocalDate())
                    .get();

            return ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            return fieldValue;
        }
    }

    default Object getMethodValue(final String field) throws InvocationTargetException, IllegalAccessException {

        final Boolean isBoolean = Try.of(() -> this.getClass().getDeclaredField(field))
                .map(v -> Boolean.class.getSimpleName().equalsIgnoreCase(v.getType().getSimpleName())).recover(e -> false).get();

        final String format = getMethodFormat(isBoolean, field);
        final Method m = getMethod(format) == null? getMethod(getMethodFormat(false, field)) : getMethod(format);
        return m == null? null : m.invoke(this,null);
    }

    default String getMethodFormat(final boolean isBoolean,final String field){
        return (isBoolean?"is":"get").concat(String.valueOf(field.charAt(0)).toUpperCase()).concat(field.substring(1));
    }

    default Method getMethod(String format){
        return Arrays.asList(this.getClass().getDeclaredMethods()).stream().filter(m -> m.getName().equals(format)).findFirst().orElse(null);
    }

}