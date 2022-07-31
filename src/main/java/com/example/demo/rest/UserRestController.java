package com.example.demo.rest;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder encoder;
    public UserRestController(UserService service, ObjectMapper objectMapper, PasswordEncoder encoder) {
        this.userService = service;
        this.objectMapper = objectMapper;
        this.encoder = encoder;
    }

    @PutMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam String nickname,
                                             @RequestParam String email,
                                             @RequestParam String password) throws Exception {
        try {
            userService.createNewUser(nickname, email, password);
        }catch (Exception e){
            return ResponseEntity.status(400).body("User already exist");
        }
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept();
        SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.serializeAllExcept();
        FilterProvider filters = new SimpleFilterProvider().addFilter("roomFilter", filter).addFilter("userFilter", userFilter);
        return ResponseEntity.ok("User created:\n" + objectMapper.writer(filters).writeValueAsString(userService.getUserDataByEmail(email)));
    }
    @PostMapping("/change/userdata")
    public ResponseEntity<String> changeUserData(@RequestParam String nickname,
                                                 @RequestParam String email,
                                                 @RequestParam(required = false) String password){
        User user;
        try {
            user = userService.getUserDataByEmail(email);
        }catch (NullPointerException e){
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        user.setEmail(email);
        if (Objects.nonNull(password)){
            user.setPassword(encoder.encode(password));
        }
        user.setNickName(nickname);
        userService.updateUser(user);
        return ResponseEntity.ok("User data is changed");
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email,
                                        @RequestParam String password) throws JsonProcessingException {
        User user;
        try {
            user = userService.getUserDataByEmail(email);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        if (!encoder.matches(password, user.getPassword())) {
            return new ResponseEntity<>("Wrong password", HttpStatus.BAD_REQUEST);
        }
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept();
        SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.serializeAllExcept();
        FilterProvider filters = new SimpleFilterProvider().addFilter("roomFilter", filter).addFilter("userFilter", filter);
        return ResponseEntity.ok(objectMapper.writer(filters).writeValueAsString(user));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam String email,
                                         @RequestParam String password){
        User user;
        try {
            user = userService.getUserDataByEmail(email);
        }catch (Exception e){
            return new ResponseEntity<>("Something wrong", HttpStatus.BAD_REQUEST);
        }
        if (!encoder.matches(password, user.getPassword())){
            return new ResponseEntity<>("Wrong password", HttpStatus.BAD_REQUEST);
        }
        userService.deleteUser(user.getId());
        return ResponseEntity.ok("User successfully deleted!");
    }

    private String userToJson(User user){
        StringBuilder string = new StringBuilder();
        string.append("{\n");
        string.append("\"id\": \"" + user.getId() +"\",\n");
        string.append("\"nickName\": \"" + user.getNickName() + "\",\n");
        string.append("\"email\": \"" + user.getEmail() + "\",\n");
        string.append("\"registrationDate\": \"" + user.getRegistrationDate() + "\",\n");
        string.append("\"rooms\": \n[");
        user.getRooms().forEach(e->{
            string.append("{\n");
            string.append("\"roomId\": \"" + e.getId() + "\"\n}");
        });
        string.append("\n]\n}");
        return string.toString();
    }
}
