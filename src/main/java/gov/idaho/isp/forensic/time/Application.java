package gov.idaho.isp.forensic.time;

import gov.idaho.isp.forensic.time.formatter.LocalDateFormatter;
import gov.idaho.isp.forensic.time.message.MessageService;
import gov.idaho.isp.forensic.time.message.MessageServiceImpl;
import gov.idaho.isp.forensic.time.message.transformer.CourtTestimonyErrorMessageTransformer;
import gov.idaho.isp.forensic.time.message.transformer.WorkEntryErrorMessageTransformer;
import java.time.LocalDate;
import java.util.Arrays;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.Formatter;
import org.springframework.jmx.support.RegistrationPolicy;

@EnableMBeanExport(registration=RegistrationPolicy.IGNORE_EXISTING)
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  public MessageService getMessageService(MessageSource messageSource) {
    MessageServiceImpl msgService = new MessageServiceImpl(messageSource);
    msgService.addErrorMessageTransformer(new WorkEntryErrorMessageTransformer());
    msgService.addErrorMessageTransformer(new CourtTestimonyErrorMessageTransformer());
    return msgService;
  }

  @Bean
  public Formatter<LocalDate> getLocalDateFormatter() {
    return new LocalDateFormatter("MM/dd/yyyy", Arrays.asList("yyyy-MM-dd", "MM/dd/yyyy"));
  }
}
