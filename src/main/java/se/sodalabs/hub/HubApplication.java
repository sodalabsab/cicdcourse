package se.sodalabs.hub;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Theme(value = "hub", variant = Lumo.DARK)
@Push
@OpenAPIDefinition(
		servers = { @Server(url = "/", description = "Default Server URL") }
)
public class HubApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(HubApplication.class, args);
	}

}
