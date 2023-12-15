package expresstalk.dev.backend.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                description = "OpenApi documentation for ExpressTalk backend application",
                title = "OpenApi specification",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Dev ENV",
                        url = "http://localhost:8080"
                )
        }

)
public class OpenApiConfig {
}
