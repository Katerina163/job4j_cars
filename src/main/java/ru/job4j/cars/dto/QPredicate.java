package ru.job4j.cars.dto;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        return Optional.ofNullable(ExpressionUtils.allOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }

    public Predicate or() {
        return Optional.ofNullable(ExpressionUtils.anyOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }
}
