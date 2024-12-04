package banquemisr.challenge05.service;

import banquemisr.challenge05.entities.User;
import banquemisr.challenge05.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user from the database
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Convert the roles string into granted authorities
        String roles = user.getRole();
        GrantedAuthority authority = new SimpleGrantedAuthority(roles);

        // Return a UserDetails object with the user's roles
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                List.of(authority)
        );
    }
}