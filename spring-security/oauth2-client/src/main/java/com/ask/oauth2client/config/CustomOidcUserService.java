package com.ask.oauth2client.config;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

  private final OidcUserService delegate = new OidcUserService();

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = delegate.loadUser(userRequest);

    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

    return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
  }

}
