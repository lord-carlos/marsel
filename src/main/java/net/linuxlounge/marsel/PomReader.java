package net.linuxlounge.marsel;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PomReader {

    private static Model readModel() throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model;
        if ((new File("pom.xml")).exists())
            model = reader.read(new FileReader("pom.xml"));
        else
            model = reader.read(
                    new InputStreamReader(
                            PomReader.class.getResourceAsStream(
                                    "/META-INF/maven/de.scrum-master.stackoverflow/aspectj-introduce-method/pom.xml"
                            )
                    )
            );
        return model;
    }

    static String getVersion() {
        try {
            return PomReader.readModel().getVersion();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}
