package ut.ac.ir.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ut.ac.ir.library.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final BookRepository bookRepository;
}
