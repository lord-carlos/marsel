package net.linuxlounge.marsel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Marsel extends ListenerAdapter {
    // Pattern for recognizing a URL, based off RFC 3986
    @SuppressWarnings("Annotator")
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);


    @Override
    public void onGenericMessage(GenericMessageEvent event) throws Exception {
        if (event.getMessage().startsWith("!ping")) {
            event.respond("pong!");
            return;
        }
        else if (event.getMessage().startsWith("!pizza")) {
            if (event.getMessage().trim().length() == 6) {
                event.respond("Usage !pizza $minutes");
            } else {
                try {
                    int time = Integer.parseInt(event.getMessage().substring(7).trim());
                    if (time < 1) {
                        event.respond(": Spacken .. gib mir ne Ordentliche zahl.");
                    } else {
                        event.respond("You will be notified in " + time + " minutes.");
                        Thread.sleep(1000 * 60 * time);
                        event.respond("Time is up");
                    }

                } catch (NumberFormatException numberFormatException) {
                    event.respond(": Spacken .. gib mir ne Ordentliche zahl.");
                }
            }
            return;
        } else if (event.getMessage().startsWith("!version")) {
            event.respond("Current version is " + PomReader.getVersion());
        }

        Matcher matcher = urlPattern.matcher(event.getMessage());
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            String url = event.getMessage().substring(matchStart, matchEnd);
            event.respondWith("Title: " + url2title(url));
        }

    }

    private String url2title(String url) {
        InputStream response;
        try {
            response = new URL(url).openStream();
            Scanner scanner = new Scanner(response);
            String responseBody = scanner.useDelimiter("\\A").next();
            Document doc = Jsoup.parse(responseBody);
            return doc.title();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
