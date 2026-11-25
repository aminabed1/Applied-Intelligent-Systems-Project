package ut.ac.ir.library.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ut.ac.ir.library.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {
    public static Specification<Book> filterBooks(String title, String authorName, Integer publishYear, Boolean isAvailable) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isFalse(root.get("deleted")));

            if (title != null && !title.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (authorName != null && !authorName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("authorName")), "%" + authorName.toLowerCase() + "%"));
            }

            if (publishYear != null) {
                predicates.add(cb.equal(root.get("publishYear"), publishYear));
            }

            if (isAvailable != null) {
                predicates.add(cb.equal(root.get("available"), isAvailable));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
