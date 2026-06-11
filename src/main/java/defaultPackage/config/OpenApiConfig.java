package defaultPackage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI horoscopeApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Гороскопы на все случаи жизни!")
                        .version("1.0.0")
                        .description("API для генерации и хранения гороскопов"));
    }
}