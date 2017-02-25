package net.linuxlounge.marsel;


import org.pircbotx.Configuration;
import org.pircbotx.MultiBotManager;
import org.pircbotx.UtilSSLSocketFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class App {

    public static void main(String[] args) throws IOException {
        List<Configuration> configs = new ArrayList<Configuration>();
        for (String arg : args) {
            Properties properties = loadProperties(arg);
            Configuration config = properties2configuration(properties);
            configs.add(config);
        }

        MultiBotManager multiBotManager = new MultiBotManager();
        for (Configuration config : configs) {
            multiBotManager.addBot(config);
        }

        multiBotManager.start();
    }

    private static Configuration properties2configuration(Properties properties) {
        String nick = properties.getProperty("nick");
        String server = properties.getProperty("server");
        int port = Integer.parseInt(properties.getProperty("port"));
        String ssl = properties.getProperty("ssl");
        String channel = properties.getProperty("channel");

        return new Configuration.Builder()
                .setName(nick) //Set the nick of the bot. CHANGE IN YOUR CODE
                .setLogin("marsel") //login part of hostmask, eg name:login@host
                .setAutoNickChange(true) //Automatically change nick when the current one is in use
                .setCapEnabled(true) //Enable CAP features
//                .addCapHandler(new TLSCapHandler(new UtilSSLSocketFactory().trustAllCertificates(), true))
                .addListener(new Marsel()) //This class is a listener, so add it to the bots known listeners
                .addServer(server, port)
                .setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
                .addAutoJoinChannel(channel)
                .buildConfiguration();
    }

    private static Properties loadProperties(String arg) throws IOException {
        Properties properties = new Properties();
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(arg));
        properties.load(stream);
        stream.close();
        return properties;

    }
}
