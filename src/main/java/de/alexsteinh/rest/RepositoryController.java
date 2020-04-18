package de.alexsteinh.rest;

import de.alexsteinh.data.Grade;
import de.alexsteinh.data.Repository;
import de.alexsteinh.data.Table;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RepositoryController {
    private static Repository repo;

    @PostConstruct
    public void init() {
        repo = new Repository();
        repo.start();
    }

    @CrossOrigin
    @GetMapping("/tables/{grade}")
    public Table getTable(@PathVariable String grade) {
        return repo.getTable(repo.getGrade(grade));
    }

    @CrossOrigin
    @GetMapping("/grades")
    public List<String> getGrades() {
        return repo.getGrades().stream()
                .map(Grade::getName)
                .collect(Collectors.toList());
    }

    @GetMapping("/update")
    public void update() {
        repo.update();
    }
}
