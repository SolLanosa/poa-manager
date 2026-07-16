package com.poa_manager;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
  private final TestRepository repository;
  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  public GreetingController(TestRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/greeting")
  public Greeting greeting(@RequestParam(defaultValue = "World") String name) {
    return new Greeting(counter.incrementAndGet(), template.formatted(name));
  }

  @GetMapping("/test")
  public List<Test> list() {
    return repository.findAll();
  }

  @GetMapping("/test/name")
  public Test test(@RequestParam(defaultValue = "") String name) {
    return repository.findByName(name);
  }

  @PostMapping("/test")
  @ResponseStatus(HttpStatus.CREATED)
  public Test create(@RequestBody Test test) {
    return repository.save(test);
  }
}