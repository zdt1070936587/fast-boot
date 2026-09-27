package com.fastboot.tools.codegen;

import com.fastboot.tools.codegen.command.NewAggregateCommand;
import com.fastboot.tools.codegen.command.NewModuleCommand;
import picocli.CommandLine;

/**
 * FastBoot 代码生成 CLI 入口。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@CommandLine.Command(
        name = "fastboot-codegen",
        mixinStandardHelpOptions = true,
        subcommands = {NewModuleCommand.class, NewAggregateCommand.class})
public class FastbootCodegenMain {

    public static void main(String[] args) {
        int code = new CommandLine(new FastbootCodegenMain()).execute(args);
        System.exit(code);
    }
}
