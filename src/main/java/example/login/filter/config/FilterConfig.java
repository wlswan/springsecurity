package example.login.filter.config;

import example.login.filter.MyFirstFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<MyFirstFilter> firstFilter(){
        FilterRegistrationBean<MyFirstFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MyFirstFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
