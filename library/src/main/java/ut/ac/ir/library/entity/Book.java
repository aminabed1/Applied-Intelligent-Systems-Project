package ut.ac.ir.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "ISBN", nullable = false, unique = true)
    private String isbn;

    @Column(name = "publish_year", nullable = false)
    private Integer publishYear;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;
}
