package ru.job4j.cars.dto;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicate {
    private final List<Predicate> predicates = new ArrayList<>();

    public static QPredicate builder() {
        return new QPredicate();
    }

    public <T> QPredicate addPredicate(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public <T> QPredicate addBiPredicate(T object, T biobject, BiFunction<T, T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object, biobject));
        }
        return this;
    }

    public Predicate and() {
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate or() {
        return ExpressionUtils.anyOf(predicates);
    }
}
