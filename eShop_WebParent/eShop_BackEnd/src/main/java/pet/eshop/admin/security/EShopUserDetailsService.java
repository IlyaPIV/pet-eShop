package pet.eshop.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pet.eshop.admin.users.UserRepository;
import pet.eshop.common.entity.User;

public class EShopUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        System.out.println(email);
        System.out.println(user);
        if (user != null) {
            return new EShopUserDetails(user);
        }

        throw new UsernameNotFoundException("Could not find user with email " + email);
    }
}
