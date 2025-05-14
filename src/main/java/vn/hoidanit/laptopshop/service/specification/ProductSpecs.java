package vn.hoidanit.laptopshop.service.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.Product_;

public class ProductSpecs {
    public static Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Product_.NAME), "%" + name + "%");
    }

    public static Specification<Product> matchMultiPrice(String range1, String range2, String range3) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.disjunction();
            int count = 0;
            if (range1.equals("tu-500-toi-1000")) {
                double min = 500;
                double max = 1000;
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.between(root.get(Product_.PRICE), min, max));
                count++;
            }
            if (range2.equals("tu-1000-toi-1500")) {
                double min = 1000;
                double max = 1500;
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.between(root.get(Product_.PRICE), min, max));
                count++;
            }
            if (range3.equals("tu-1500-toi-2000")) {
                double min = 1500;
                double max = 2000;
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.between(root.get(Product_.PRICE), min, max));
                count++;
            }
            if (count == 0) {
                return null;
            }
            return predicate;
        };
    }
}
