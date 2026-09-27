package com.fastboot.tools.codegen;

/**
 * 命名转换工具。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public final class Naming {

    private Naming() {}

    public static String pascalCase(String raw) {
        if (raw == null || raw.isBlank()) {
            return raw;
        }
        String[] parts = raw.replace('-', '_').split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            sb.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                sb.append(part.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    public static String camelCase(String raw) {
        String pascal = pascalCase(raw);
        if (pascal.isEmpty()) {
            return pascal;
        }
        return Character.toLowerCase(pascal.charAt(0)) + pascal.substring(1);
    }

    public static String snakeTable(String module, String aggregate) {
        return module + "_" + aggregate.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
