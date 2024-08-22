package com.neil.project.security;

import com.neil.project.user.api.UserFacade;
import com.neil.project.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author nihao
 * @date 2024/8/21
 */
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserFacade userFacade;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDTO userDTO = userFacade.getUserById(Long.valueOf(userId));
        if (Objects.isNull(userDTO)) {
            throw new UsernameNotFoundException("无此用户！");
        }

        return JwtUserDetails.builder()
                .userId(userDTO.getId())
                .mobile(userDTO.getMobile())
                .username(userDTO.getUsername())
                .build();
    }
}
