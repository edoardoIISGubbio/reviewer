package it.edu.iisgubbio.reviewer.service;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

import java.util.Optional;

/****************************************************************************
 * Espone la versione dell'applicazione letta da META-INF/build-info.properties,
 * file generato automaticamente da Maven durante il build (goal build-info).
 * Se il file è assente (run diretto da IDE senza Maven) restituisce "dev".
 ***************************************************************************/
@Service
public class VersionService {

    private final String version;
    private final String name;

    /* Nota: il bean BuildProperties è disponibile solo dopo un build Maven completo.
    Con mvn spring-boot:run viene generato in automatico; in un run diretto dall'IDE
    potrebbe servire un mvn generate-resources prima. */
    public VersionService(Optional<BuildProperties> buildProperties) {
        this.version = buildProperties.map(BuildProperties::getVersion).orElse("dev");
        this.name    = buildProperties.map(BuildProperties::getName).orElse("reviewer");
    }

    /** Restituisce la versione dichiarata nel pom.xml (es. "2.1") */
    public String getVersion() {
        return version;
    }

    /** Restituisce nome artefatto e versione (es. "reviewer 2.1") */
    public String getFullVersion() {
        return name + " " + version;
    }
}
