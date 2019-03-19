package gov.idaho.isp.forensic.time.controller;

import gov.idaho.isp.forensic.time.domain.User;
import gov.idaho.isp.forensic.time.domain.persistence.UserRepository;
import gov.idaho.isp.forensic.time.message.MessageService;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController extends BaseController {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, MessageService messageService) {
    super(messageService);
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @ModelAttribute
  public User prepareUser(@RequestParam Optional<Long> id) {
    return id.isPresent() ? userRepository.findById(id.get()).orElse(new User()) : new User();
  }

  @ModelAttribute
  public User encodePassword(@ModelAttribute User user) {
    if (StringUtils.isNotBlank(user.getPlaintextPassword())) {
      user.setPassword(passwordEncoder.encode(user.getPlaintextPassword()));
    }
    return user;
  }

  @GetMapping("/admin/users")
  public String users(Model m) {
    m.addAttribute("users", userRepository.findAll());
    return "users";
  }

  @PostMapping("/admin/user/save")
  public String saveUser(@Valid User user, BindingResult br, Model m, RedirectAttributes ra) {
    if (br.hasErrors()) {
      m.addAttribute("users", userRepository.findAll());
      m.addAttribute("errors", getErrors(br));
      return "users";
    }

    userRepository.save(user);
    ra.addFlashAttribute("messages", getText("var.saved", "User " + user.getDisplayName()));
    return "redirect:/admin/users";
  }

  @PostMapping("/admin/user/delete")
  public String deleteUser(User user, RedirectAttributes ra) {
    if (user != null && user.getId() != null) {
      userRepository.delete(user);
      ra.addFlashAttribute("messages", getText("var.deleted", "User " + user.getDisplayName()));
    }
    return "redirect:/admin/users";
  }
}
