package ut.ac.ir.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ut.ac.ir.library.service.LibraryService;

@RestController
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;
}
