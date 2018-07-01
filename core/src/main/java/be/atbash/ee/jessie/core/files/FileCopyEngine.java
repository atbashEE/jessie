package be.atbash.ee.jessie.core.files;

import be.atbash.ee.jessie.core.exception.TechnicalException;
import com.google.common.io.ByteStreams;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 *
 */
@ApplicationScoped
public class FileCopyEngine {

    @Inject
    private FilesLocator filesLocator;

    public byte[] processFile(String file, Set<String> alternatives) {

        String fileIndication = filesLocator.findFile(file, alternatives);

        if ("-1".equals(fileIndication)) {
            throw new FileResolutionException(file, alternatives);
        }

        String sourceFile = "/" + filesLocator.getTemplateFile(fileIndication);

        InputStream resource = FileCopyEngine.class.getResourceAsStream(sourceFile);
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        try {
            ByteStreams.copy(resource, result);
        } catch (IOException e) {
            throw new TechnicalException(e);
        }
        return result.toByteArray();
    }

}
