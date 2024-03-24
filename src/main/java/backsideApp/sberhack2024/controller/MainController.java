package backsideApp.sberhack2024.controller;

import backsideApp.sberhack2024.algo.JsonFiles;
import backsideApp.sberhack2024.dto.Components;
import backsideApp.sberhack2024.dto.ElementsList;
import backsideApp.sberhack2024.entity.User;
import backsideApp.sberhack2024.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.util.WebUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import jakarta.servlet.http.Cookie;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class MainController {

    private final UserRepository userRepository;

    @Autowired
    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/test")
    public String test() {
        return "[ {\n" +
                "    \"type\" : \"Notification\",\n" +
                "    \"text\" : \"Даня трогал пенис\",\n" +
                "    \"id\" : 0,\n" +
                "    \"info\" : \"комментарий об объекте\"\n" +
                "  }, {\n" +
                "    \"type\" : \"Message\",\n" +
                "    \"text\" : \"свободен вечером?\",\n" +
                "    \"id\" : 1,\n" +
                "    \"user_id\" : 124124,\n" +
                "    \"info\" : \"комментарий об объекте\"\n" +
                "  }, {\n" +
                "    \"type\" : \"Rate\",\n" +
                "    \"text\" : \"wqewqe\",\n" +
                "    \"info\" : \"пятибалльная шкала для оценки чего-либо\",\n" +
                "    \"id\" : 2\n" +
                "  }, {\n" +
                "    \"type\" : \"Checkbox\",\n" +
                "    \"text\" : \"wqewqe\",\n" +
                "    \"info\" : \"пятибалльная шкала для оценки чего-либо\",\n" +
                "    \"id\" : 3,\n" +
                "    \"box\": [\n" +
                "        \"Бабаха\",\n" +
                "        \"Кошка\",\n" +
                "        \"Колобака\"\n" +
                "    ]\n" +
                "  } ]";
    }
    @GetMapping(path = "/test2")
    public ResponseEntity<Resource> getJson() throws IOException {
        String filepath = "src/main/resources/static/default.json";
        byte[] data = Files.readAllBytes(Paths.get(filepath));
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(resource);
    }

    @PostMapping(path = "/test2")
    public String doNothing() {
        return null;
    }

    @GetMapping(path = "/main")
    public String mainPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Cookie cookie = WebUtils.getCookie(request, "userId");

        if (cookie != null) {
            System.out.println(cookie.getValue());
            String userId = cookie.getValue();
            User user = userRepository.findByUserId(Long.valueOf(userId));
            if (user != null) {
                return user.getUserJson();
            }
        }


        User newUser = new User();
        String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/static/default.json")));
        newUser.setUserJson(jsonContent);

        userRepository.save(newUser);
        User savedUser = userRepository.save(newUser);
        String savedUserId = savedUser.getUserId().toString();
        Cookie newCookie = new Cookie("userId", savedUserId);
        newCookie.setMaxAge(24 * 60 * 60 * 60);
        response.addCookie(newCookie);

        return jsonContent;
    }

    @PostMapping(path = "/addElementToAllUsers")
    public String addElement(@RequestBody String body) throws IOException {

        ArrayList<Components> newComponents = JsonFiles.readPojoFromJson(body);

        String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/static/default.json")));
        ArrayList<Components> components = JsonFiles.readPojoFromJson(jsonContent);
        components.addAll(newComponents);
        System.out.println(body);
        try (FileWriter file = new FileWriter("путь/к/файлу.json")) {
            file.write(JsonFiles.createJsonStringFromPojo(components));
            System.out.println("JSON успешно сохранен в файл");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<User> users = userRepository.findAll();

        for (User user : users) {
            ArrayList<Components> userComponents = JsonFiles.readPojoFromJson(user.getUserJson());
            userComponents.addAll(newComponents);
            user.setUserJson(JsonFiles.createJsonStringFromPojo(userComponents));
        }

        userRepository.saveAll(users);

        return null;
    }

    @PostMapping(path = "/deleteElement")
    public String deleteElement(HttpServletRequest request, @RequestBody String id) throws IOException {

        Cookie cookie = WebUtils.getCookie(request, "userId");
        System.out.println(id);

        if (cookie != null) {
            String userId = cookie.getValue();
            User user = userRepository.findByUserId(Long.valueOf(userId));
            if (user != null) {
                ArrayList<Components> newComponentsArray = deleteFromArray(JsonFiles.readPojoFromJson(user.getUserJson()) , id);
                String newComponents = JsonFiles.createJsonStringFromPojo(newComponentsArray);
                user.setUserJson(newComponents);
                userRepository.save(user);
                return newComponents;
            }
        }
        return new String(Files.readAllBytes(Paths.get("src/main/resources/static/default.json")));

    }

    public ArrayList<Components> deleteFromArray(ArrayList<Components> array, String id) {
        Iterator<Components> it = array.iterator();
        while(it.hasNext()){
            Components component = it.next();
            if (component instanceof ElementsList) {
                deleteFromArray(((ElementsList) component).getElements(), id);
            }
            if(component.getId().equals(id)){
                it.remove();
            }
        }
        return array;
    }
}

