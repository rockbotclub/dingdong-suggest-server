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

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String wxid) throws UsernameNotFoundException {
        UserDO user = userRepository.findByWxid(wxid);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with wxid: " + wxid);
        }

        // 微信用户不需要密码，使用空字符串
        return new User(
                user.getWxid(),
                "",
                Collections.singletonList(new SimpleGrantedAuthority(user.getStatus() == 1 ? "ROLE_ADMIN" : "ROLE_USER"))
        );
    }
} 