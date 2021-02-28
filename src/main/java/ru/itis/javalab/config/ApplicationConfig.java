package ru.itis.javalab.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;


@Configuration
@EnableWebMvc
@PropertySource("classpath:app.properties")
@ComponentScan(basePackages = "ru.itis.javalab")
public class ApplicationConfig {

  @Autowired
  private Environment environment;

  @Bean
  public ExecutorService executorService() {
    return Executors.newCachedThreadPool();
  }

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(environment.getProperty("spring.mail.host"));
    mailSender.setPort(Integer.parseInt(environment.getProperty("spring.mail.port")));
    mailSender.setDefaultEncoding("UTF-8");
    mailSender.setUsername(environment.getProperty("spring.mail.username"));
    mailSender.setPassword(environment.getProperty("spring.mail.password"));
    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", environment.getProperty("spring.mail.properties.transport.protocol"));
    props.put("mail.smtp.auth", environment.getProperty("spring.mail.properties.mail.smtp.auth"));
    props.put("mail.smtp.starttls.enable", environment.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
    props.put("mail.debug", environment.getProperty("spring.mail.properties.mail.debug"));
    return mailSender;
  }

  @Bean
  public BCryptPasswordEncoder bcryptService() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DataSource dataSource() {
    return new HikariDataSource(hikariConfig());
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public HikariConfig hikariConfig() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(environment.getProperty("db.jdbc.url"));
    hikariConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty("db.jdbc.hikari.max-pool-size")));
    hikariConfig.setUsername(environment.getProperty("db.jdbc.username"));
    hikariConfig.setPassword(environment.getProperty("db.jdbc.password"));
    hikariConfig.setDriverClassName(environment.getProperty("db.jdbc.driver.classname"));
    return hikariConfig;
  }

  @Bean
  public freemarker.template.Configuration configuration() {
    return freemarkerConfig().getConfiguration();
  }

  @Bean
  public FreeMarkerViewResolver freemarkerViewResolver() {
    FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
    resolver.setPrefix("");
    resolver.setSuffix(".ftl");
    resolver.setContentType("text/html;charset=UTF-8");
    return resolver;
  }

  @Bean
  public FreeMarkerConfigurer freemarkerConfig() {
    FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
    configurer.setTemplateLoaderPath("/WEB-INF/ftl/");
    return configurer;
  }


}
