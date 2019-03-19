package gov.idaho.isp.forensic.time.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements Serializable, UserDetails {
  public enum Role {
    ADMIN, USER;
  }

  @Id @GeneratedValue
  private Long id;

  @NotBlank(message = "{user.firstName.blank}")
  private String firstName;

  private String middleName;

  @NotBlank(message = "{user.lastName.blank}")
  private String lastName;

  private String email;

  @NotBlank(message = "{user.username.blank}")
  private String username;

  @NotBlank(message = "{user.password.blank}")
  private String password;

  @Transient
  private String plaintextPassword;

  @NotNull(message = "{user.userRole.null}")
  private Role userRole;

  public User() {
  }

  public User(String first, String middle, String last, String username) {
    this.firstName = first;
    this.middleName = middle;
    this.lastName = last;
    this.username = username;
  }

  public String getDisplayName() {
    return lastName + ", " + firstName;
  }

  public boolean isAdmin() {
    return userRole == Role.ADMIN;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPlaintextPassword() {
    return plaintextPassword;
  }

  public void setPlaintextPassword(String plaintextPassword) {
    this.plaintextPassword = plaintextPassword;
  }

  public Role getUserRole() {
    return userRole;
  }

  public void setUserRole(Role userRole) {
    this.userRole = userRole;
  }

  @Override
  public Set<GrantedAuthority> getAuthorities() {
    return new HashSet<>(Arrays.asList(new SimpleGrantedAuthority(userRole.name())));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", email=" + email + ", username=" + username + ", password=" + password + ", userRole=" + userRole + '}';
  }
}
