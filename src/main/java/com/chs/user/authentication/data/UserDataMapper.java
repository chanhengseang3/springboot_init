package com.chs.user.authentication.data;

import com.chs.persistence.mapper.DataMapper;
import com.chs.user.authentication.domain.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataMapper extends DataMapper<AppUser, UserData> {

    final PasswordEncoder encoder;

    @Override
    public AppUser map(UserData data) {
        final var user = super.map(data);
        if (data.getPassword() != null) {
            user.setPassword(encoder.encode(data.getPassword()));
        }
        return user;
    }
}
