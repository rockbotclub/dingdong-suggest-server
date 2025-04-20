package cc.rockbot.dds.security;

import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String wxid) throws UsernameNotFoundException {
        List<UserDO> users = userRepository.findByWxid(wxid);
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("User not found with wxid: " + wxid);
        }

        // 使用第一个用户的信息，因为所有用户都有相同的wxid
        UserDO user = users.get(0);

        // 微信用户不需要密码，使用空字符串
        return new User(
                user.getWxid(),
                "",
                Collections.singletonList(new SimpleGrantedAuthority(user.getStatus() == 1 ? "ROLE_ADMIN" : "ROLE_USER"))
        );
    }
} 