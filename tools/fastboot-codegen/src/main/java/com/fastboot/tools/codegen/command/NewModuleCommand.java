package com.fastboot.tools.codegen.command;

import com.fastboot.tools.codegen.Naming;
import com.fastboot.tools.codegen.TemplateRenderer;
import java.nio.file.Path;
import java.util.Map;
import picocli.CommandLine;

/**
 * 生成新业务模块骨架 {@code fastboot-modules-<name>}。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@CommandLine.Command(name = "new-module", description = "Scaffold a new fastboot-modules-* Maven module")
public class NewModuleCommand implements Runnable {

    @CommandLine.Option(names = "--name", required = true, description = "Module short name, e.g. order")
    String name;

    @CommandLine.Option(
            names = "--root",
            defaultValue = ".",
            description = "Repository root (default: current directory)")
    Path root;

    @Override
    public void run() {
        try {
            String module = name.toLowerCase().replaceAll("[^a-z0-9-]", "");
            String packageName = module.replace('-', '_');
            String pascal = Naming.pascalCase(module);
            Path moduleDir = root.resolve("fastboot-modules/fastboot-modules-" + module);
            TemplateRenderer renderer = new TemplateRenderer();
            Map<String, Object> model =
                    Map.of("module", module, "packageName", packageName, "pascal", pascal, "aggregate", pascal);

            renderer.renderToFile(
                    "module/pom.xml.ftl",
                    model,
                    moduleDir.resolve("pom.xml"));
            renderer.renderToFile(
                    "module/ModuleAutoConfiguration.java.ftl",
                    model,
                    moduleDir.resolve("src/main/java/com/fastboot/modules/" + packageName + "/config/"
                            + pascal + "ModuleAutoConfiguration.java"));
            renderer.renderToFile(
                    "module/AutoConfiguration.imports.ftl",
                    model,
                    moduleDir.resolve(
                            "src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports"));
            renderer.renderToFile(
                    "module/package-info.java.ftl",
                    model,
                    moduleDir.resolve("src/main/java/com/fastboot/modules/" + packageName + "/package-info.java"));

            System.out.println("Created module skeleton at " + moduleDir.toAbsolutePath());
            System.out.println("Next: add module to fastboot-modules/pom.xml and fastboot-dependencies BOM.");
        } catch (Exception ex) {
            throw new CommandLine.ExecutionException(new CommandLine(this), ex.getMessage(), ex);
        }
    }
}
