package cc.rockbot.dds.controller;

import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDO> createUser(@RequestBody UserDO user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/wxid/{userWxid}")
    public ResponseEntity<UserDO> getUserByWxid(@PathVariable String userWxid) {
        UserDO user = userService.getUserByWxid(userWxid);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<List<UserDO>> getUsersByOrgId(@PathVariable String orgId) {
        return ResponseEntity.ok(userService.getUsersByOrgId(orgId));
    }

    @GetMapping
    public ResponseEntity<List<UserDO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDO> updateUser(@PathVariable Long id, @RequestBody UserDO user) {
        user.setId(id);
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
} 