package pw.react.cars_api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableSpringDataWebSupport(
        pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Specify the allowed origin
                .allowedOriginPatterns("*")
                .allowedMethods("HEAD","OPTIONS","GET", "POST", "PUT", "DELETE").allowCredentials(true)  // Allow cookies and credentials
                .allowedHeaders("*","Authorization");   // Allow all headers (or specify required ones)
    }
}
