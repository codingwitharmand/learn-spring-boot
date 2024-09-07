package com.cwa.springbootdocker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false) String name) {
        return nonNull(name) ? String.format("Hello %s, from Docker!", name) : "Hello from Docker!";
    }
}
