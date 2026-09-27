package com.fastboot.tools.codegen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Freemarker 模板渲染与写文件。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public final class TemplateRenderer {

    private final Configuration configuration;

    public TemplateRenderer() {
        configuration = new Configuration(Configuration.VERSION_2_3_34);
        configuration.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public void renderToFile(String templateName, Map<String, Object> model, Path target) throws IOException {
        Files.createDirectories(target.getParent());
        String content = render(templateName, model);
        Files.writeString(target, content, StandardCharsets.UTF_8);
    }

    public String render(String templateName, Map<String, Object> model) throws IOException {
        try {
            Template template = configuration.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (Exception ex) {
            if (ex instanceof IOException io) {
                throw io;
            }
            throw new IOException("Template render failed: " + templateName, ex);
        }
    }
}
