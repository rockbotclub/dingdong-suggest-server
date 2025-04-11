package cc.rockbot.dds.service;

import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDO createUser(UserDO user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(user);
    }

    public Optional<UserDO> getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(id);
    }

    public UserDO getUserByWxid(String wxid) {
        if (!StringUtils.hasText(wxid)) {
            throw new IllegalArgumentException("User WxID cannot be null or empty");
        }
        return userRepository.findByWxid(wxid);
    }

    public List<UserDO> getUsersByOrgId(String orgId) {
        if (!StringUtils.hasText(orgId)) {
            throw new IllegalArgumentException("Organization ID cannot be null or empty");
        }
        return userRepository.findByOrgId(orgId);
    }

    public List<UserDO> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserDO updateUser(UserDO user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (!userRepository.existsById(user.getId())) {
            throw new RuntimeException("User not found");
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public boolean existsByWxid(String wxid) {
        if (!StringUtils.hasText(wxid)) {
            throw new IllegalArgumentException("User WxID cannot be null or empty");
        }
        return userRepository.existsByWxid(wxid);
    }
} 