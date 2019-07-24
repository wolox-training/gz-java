package wolox.training.security;

import java.util.ArrayList;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sun.security.util.Password;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
  @Autowired
  private UserRepository userRepository;

  private PasswordEncoder encoder = new BCryptPasswordEncoder();

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {

    String password = authentication.getCredentials().toString();
    User user = userRepository.findByUsername(authentication.getName());
    if (user != null && encoder.matches(password, user.getPassword())) {
      return new UsernamePasswordAuthenticationToken(user.getUsername(), password, Collections.emptyList());
    } else {
      return null;
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
