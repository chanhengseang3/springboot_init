package com.chs.persistence.utils;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SFWhere {

    public static <T> SFClass<T> and(T entity) {
        return new SFClass<>(entity, "and");
    }

    public static <T> SFClass<T> or(T entity) {
        return new SFClass<>(entity, "or");
    }

    public static class SFClass<T> {
        private final String andOr;
        private final Map<String, Specification<T>> sMap = new HashMap<>();

        public SFClass(T entity, String andOr) {
            this.andOr = andOr;
            //反射循环出该实体对象的所有字段
            for (Field declaredField : entity.getClass().getDeclaredFields()) {
                try {
                    //设置是否允许获取字段的值
                    declaredField.setAccessible(true);
                    Object value = declaredField.get(entity);
                    //获取字段的名称
                    String fieldName = declaredField.getName();
                    boolean tmp = false;
                    //过滤字段上的注解
                    for (Annotation annotation : declaredField.getAnnotations()) {
                        if (judgeAnnotation(annotation)) {
                            tmp = true;
                            break;
                        }
                    }
                    //字段注解及值,都允许的情况下才包装条件
                    if (!tmp && value != null) {
                        sMap.put(fieldName, (Specification<T>) (root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(root.get(fieldName), value));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean judgeAnnotation(Annotation annotation) {
            return annotation.annotationType().equals(OneToOne.class) ||
                    annotation.annotationType().equals(OneToMany.class) ||
                    annotation.annotationType().equals(ManyToOne.class) ||
                    annotation.annotationType().equals(ManyToOne.class) ||
                    annotation.annotationType().equals(Transient.class);
        }

        public Specification<T> build() {
            //构建一个条件实例
            return (Specification<T>) (root, query, criteriaBuilder) -> {
                //建立一个条件容器
                List<Predicate> predicateList = new ArrayList<>();
                //取出条件
                sMap.forEach((String fieldName, Specification<T> sp) -> predicateList.add(sp.toPredicate(root, query, criteriaBuilder)));
                //建立一个数组容器
                Predicate[] predicates = new Predicate[predicateList.size()];
                //链接条件
                if (this.andOr.equals("and")) {
                    return criteriaBuilder.and(predicateList.toArray(predicates));
                } else {
                    return criteriaBuilder.or(predicateList.toArray(predicates));
                }
            };
        }

        public SFClass<T> equal(boolean condition, String property, Object value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.equal(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        public SFClass<T> like(boolean condition, String property, String value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.like(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        public SFClass<T> gt(boolean condition, String property, Number value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.gt(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        public SFClass<T> gt(String property) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> criteriaBuilder.and());
            return this;
        }

        public SFClass<T> ge(boolean condition, String property, Number value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.ge(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        public SFClass<T> lt(boolean condition, String property, Number value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.lt(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        public SFClass<T> le(boolean condition, String property, Number value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.le(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        public <Y> SFClass<T> in(boolean condition, String property, List<Y> value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    Path<Object> objectPath = root.get(property);
                    return objectPath.in(value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        public <Y extends Comparable<Y>> SFClass<T> between(boolean condition, String property, Y start, Y end) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.between(root.get(property), start, end);
                }
                return criteriaBuilder.and();
            });
            return this;
        }
    }
}