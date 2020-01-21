package com.inn.foresight.core.generic;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.inn.commons.lang.MapUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.RestException;

/**
 * This class assists in validating arguments.
 *
 * @author Nimit Agrawal
 */
public class Preconditions {

    private Preconditions() {
        super();
    }

    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new RestException("Invalid Parameters");
        }
    }

    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new RestException(errorMessage);
        }
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new RestException("Invalid Parameters");
        } else {
            return reference;
        }
    }

    public static <T> T checkNotNull(T reference, String errorMessage) {
        if (reference == null) {
            throw new RestException(errorMessage);
        } else {
            return reference;
        }
    }

    public static String checkNotEmpty(String value) {
        checkArgument(StringUtils.isNotEmpty(value));
        return value;
    }

    public static String checkNotEmpty(String value, String errorMessage) {
        checkArgument(StringUtils.isNotEmpty(value), errorMessage);
        return value;
    }

    public static Properties checkNotEmpty(Properties properties) {
        checkArgument(properties != null && !properties.isEmpty());
        return properties;
    }

    public static Properties checkNotEmpty(Properties properties, String errorMessage) {
        checkArgument(properties != null && !properties.isEmpty(), errorMessage);
        return properties;
    }

    public static <T, C extends Collection<T>> C checkNotEmpty(C collection) {
        checkArgument(CollectionUtils.isNotEmpty(collection));
        return collection;
    }

    public static <T, C extends Collection<T>> C checkNotEmpty(C collection, String errorMessage) {
        checkArgument(CollectionUtils.isNotEmpty(collection), errorMessage);
        return collection;
    }

    public static <U, V> Map<U, V> checkNotEmpty(Map<U, V> map) {
        checkArgument(MapUtils.isNotEmpty(map), "Null or empty not allowed");
        return map;
    }

    public static <U, V> Map<U, V> checkNotEmpty(Map<U, V> map, String errorMessage) {
        checkArgument(MapUtils.isNotEmpty(map), errorMessage);
        return map;
    }

    public static <T> T[] checkNotEmpty(T[] array) {
        checkArgument(ArrayUtils.isNotEmpty(array), "Null or empty not allowed");
        return array;
    }

    public static <T> T[] checkNotEmpty(T[] array, String errorMessage) {
        checkArgument(ArrayUtils.isNotEmpty(array), errorMessage);
        return array;
    }

    public static Duration checkNotEmpty(Duration duration) {
        checkArgument(duration != null && duration.isValid());
        return duration;
    }

    public static Duration checkNotEmpty(Duration duration, String errorMessage) {
        checkArgument(duration != null && duration.isValid(), errorMessage);
        return duration;
    }

    public static String checkNotBlank(String value) {
        checkArgument(StringUtils.isNotBlank(value));
        return value;
    }

    public static String checkNotBlank(String value, String errorMessage) {
        checkArgument(StringUtils.isNotBlank(value), errorMessage);
        return value;
    }

    public static int checkElementIndex(int index, int size, String desc) {
        return Preconditions.checkElementIndex(index, size, desc);
    }

    @SafeVarargs
    public static <T> void checkNoneNull(T... references) {
        checkNotNull(references);
        for (T reference : references) {
            checkNotNull(reference);
        }
    }

    public static void checkNoneEmpty(String... args) {
        checkArgument(StringUtils.isNoneEmpty(args));
    }

    public static void checkFileExists(File file) {
        checkNotNull(file, "File is null");
        checkArgument(file.exists(), file.getAbsolutePath() + " not exists");
    }

    public static void checkFileExists(String filePath) {
        checkNotEmpty(filePath, "File path is null or empty");
        checkFileExists(new File(filePath));
    }

}