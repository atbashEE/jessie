/*
 * Copyright 2017-2018 Rudy De Busscher (https://www.atbash.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.atbash.ee.jessie.core.files;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@ApplicationScoped
public class ThymeleafEngine {

    @Inject
    private FilesLocator filesLocator;

    private TemplateEngine engine;

    @PostConstruct
    public void init() {
        AbstractConfigurableTemplateResolver resolver = new JessieFileTemplateResolver(filesLocator);
        resolver.setTemplateMode("TEXT");
        engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

    }

    public String processFile(String file, Set<String> alternatives, Map<String, String> variables) {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        for (Map.Entry<String, String> variable : variables.entrySet()) {
            context.setVariable(variable.getKey(), variable.getValue());
        }

        String fileIndication = filesLocator.findFile(file, alternatives);

        if ("-1".equals(fileIndication)) {
            throw new TemplateFileResolutionException(file, alternatives);
        }
        engine.process(fileIndication, context, writer);

        return writer.toString();

    }

}
