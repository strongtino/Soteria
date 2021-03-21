package dev.strongtino.soteria;

import dev.strongtino.soteria.command.LicenseCommand;
import dev.strongtino.soteria.service.database.DatabaseService;
import dev.strongtino.soteria.service.license.LicenseService;
import dev.strongtino.soteria.util.Base;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.stream.Stream;

@Getter
public class Soteria {

    @Getter
    private static Soteria instance;

    private JDA jda;

    private DatabaseService databaseService;
    private LicenseService licenseService;

    void start() {
        instance = this;

        connect();
        loadServices();
        loadCommands();
    }

    private void connect() {
        try {
            jda = JDABuilder.create(Base.BOT_TOKEN, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)).build();
            jda.getPresence().setPresence(Activity.playing(Base.BOT_PRESENCE), false);
        } catch (LoginException e) {
            System.exit(1);
        }
    }

    private void loadServices() {
        databaseService = new DatabaseService();
        licenseService = new LicenseService();
    }

    private void loadCommands() {
        Stream.of( // more commands will be added therefore -> stream
                new LicenseCommand()
        ).forEach(command -> jda.addEventListener(command));
    }
}
