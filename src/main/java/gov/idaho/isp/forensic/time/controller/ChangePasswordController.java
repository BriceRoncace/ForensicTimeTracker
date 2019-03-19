package gov.idaho.isp.forensic.time.controller;

import gov.idaho.isp.forensic.time.domain.User;
import gov.idaho.isp.forensic.time.domain.persistence.UserRepository;
import gov.idaho.isp.forensic.time.message.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChangePasswordController extends BaseController {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public ChangePasswordController(UserRepository userRepository, PasswordEncoder passwordEncoder, MessageService messageService) {
    super(messageService);
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @ModelAttribute
  public User prepareUser(@RequestParam Long id) {
    return userRepository.findById(id).orElse(null);
  }

  @GetMapping("/changePassword")
  public String toChangePasswordForm(Model m) {
    return "change-password";
  }

  @PostMapping("/changePassword")
  public String changePassword(User user, @RequestParam String currentPassword, @RequestParam String password1, @RequestParam String password2, Model m, RedirectAttributes ra) {
    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      m.addAttribute("errors", "Current password is not correct.");
      return "change-password";
    }

    if (StringUtils.isBlank(password1) || !StringUtils.equals(password1, password2)) {
      m.addAttribute("errors", "New passwords do not match.");
      return "change-password";
    }

    user.setPassword(passwordEncoder.encode(password1));
    userRepository.save(user);
    ra.addFlashAttribute("messages", getText("var.saved", "Updated password"));
    return "redirect:/";
  }
}
