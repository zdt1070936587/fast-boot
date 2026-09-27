package com.fastboot.tools.codegen.command;

import com.fastboot.tools.codegen.Naming;
import com.fastboot.tools.codegen.TemplateRenderer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import picocli.CommandLine;

/**
 * 按 YAML 规格生成聚合 CRUD 骨架。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@CommandLine.Command(name = "new-aggregate", description = "Generate DDD layers for an aggregate from YAML spec")
public class NewAggregateCommand implements Runnable {

    @CommandLine.Option(names = "--module", required = true, description = "Module short name, e.g. order")
    String module;

    @CommandLine.Option(names = "--spec", required = true, description = "Path to aggregate YAML spec")
    Path spec;

    @CommandLine.Option(names = "--root", defaultValue = ".", description = "Repository root")
    Path root;

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> specMap = yaml.load(java.nio.file.Files.readString(spec));
            String aggregate = String.valueOf(specMap.getOrDefault("aggregate", "Sample"));
            String table = String.valueOf(specMap.getOrDefault("table", Naming.snakeTable(module, aggregate)));
            String moduleKey = module.toLowerCase().replaceAll("[^a-z0-9-]", "");
            String packageName = moduleKey.replace('-', '_');
            String aggregatePascal = Naming.pascalCase(aggregate);
            String aggregateCamel = Naming.camelCase(aggregate);

            Path base = root.resolve("fastboot-modules/fastboot-modules-" + moduleKey);
            Path javaBase = base.resolve("src/main/java/com/fastboot/modules/" + packageName);
            Path testBase = base.resolve("src/test/java/com/fastboot/modules/" + packageName);
            Path resources = base.resolve("src/main/resources/db/migration");
            Path httpDir = base.resolve("http");

            Map<String, Object> model = new HashMap<>();
            model.put("module", moduleKey);
            model.put("packageName", packageName);
            model.put("aggregate", aggregatePascal);
            model.put("aggregateCamel", aggregateCamel);
            model.put("table", table);
            model.put("description", specMap.getOrDefault("description", aggregatePascal + " aggregate"));

            TemplateRenderer renderer = new TemplateRenderer();
            renderer.renderToFile("aggregate/Aggregate.java.ftl", model, javaBase.resolve("domain/" + aggregatePascal + ".java"));
            renderer.renderToFile("aggregate/Repository.java.ftl", model, javaBase.resolve("domain/" + aggregatePascal + "Repository.java"));
            renderer.renderToFile("aggregate/AppService.java.ftl", model, javaBase.resolve("application/" + aggregatePascal + "AppService.java"));
            renderer.renderToFile("aggregate/Entity.java.ftl", model, javaBase.resolve("infrastructure/persistence/entity/" + aggregatePascal + "Entity.java"));
            renderer.renderToFile("aggregate/Mapper.java.ftl", model, javaBase.resolve("infrastructure/mapper/" + aggregatePascal + "Mapper.java"));
            renderer.renderToFile("aggregate/RepoImpl.java.ftl", model, javaBase.resolve("infrastructure/persistence/" + aggregatePascal + "RepositoryImpl.java"));
            renderer.renderToFile("aggregate/Controller.java.ftl", model, javaBase.resolve("interfaces/" + aggregatePascal + "Controller.java"));
            renderer.renderToFile("aggregate/Flyway.sql.ftl", model, resources.resolve("V1__" + table + ".sql"));
            renderer.renderToFile("aggregate/AppServiceTest.java.ftl", model, testBase.resolve("application/" + aggregatePascal + "AppServiceTest.java"));
            renderer.renderToFile("aggregate/api.http.ftl", model, httpDir.resolve(aggregateCamel + ".http"));

            System.out.println("Generated aggregate " + aggregatePascal + " under " + base.toAbsolutePath());
        } catch (Exception ex) {
            throw new CommandLine.ExecutionException(new CommandLine(this), ex.getMessage(), ex);
        }
    }
}
