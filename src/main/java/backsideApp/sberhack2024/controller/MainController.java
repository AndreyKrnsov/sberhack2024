package backsideApp.sberhack2024.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")

public class MainController {
    @GetMapping(path = "/test")
    public String getAllUsers() {
        return "string";
    }
}
