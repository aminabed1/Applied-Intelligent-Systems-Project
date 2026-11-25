package ut.ac.ir.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ut.ac.ir.library.entity.Loan;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByBookIdAndActualReturnDateIsNull(Long bookId);
}
