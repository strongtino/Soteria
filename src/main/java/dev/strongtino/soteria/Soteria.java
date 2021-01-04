package dev.strongtino.soteria;

import dev.strongtino.soteria.command.LicenseCommand;
import dev.strongtino.soteria.service.database.DatabaseService;
import dev.strongtino.soteria.service.license.LicenseService;
import dev.strongtino.soteria.util.Base;
import lombok.Getter;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.util.stream.Stream;

/**
 * Created by StrongTino on 28.12.2020.
 */

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
        registerCommands();
        loadServices();
        loadOther();
    }

    private void registerCommands() {
        Stream.of(
                new LicenseCommand()
        ).forEach(command -> jda.addEventListener(command));
    }

    private void loadServices() {
        databaseService = new DatabaseService();
        licenseService = new LicenseService();
    }

    private void loadOther() {
        databaseService.loadLicenses();
    }

    private void connect() {
        try {
            jda = new JDABuilder(Base.BOT_TOKEN).build();
            jda.getPresence().setPresence(Game.watching(Base.BOT_PRESENCE), false);
        } catch (LoginException e) {
            System.exit(1);
        }
    }
}
