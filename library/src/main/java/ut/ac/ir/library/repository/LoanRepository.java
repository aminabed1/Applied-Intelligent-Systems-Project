package ut.ac.ir.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ut.ac.ir.library.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Long, Loan> {
}
