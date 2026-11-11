/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.oneapi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Locates Intel oneAPI tools on disk and builds the command lines the plugin suggests.
 * <p>
 * Pure logic with no IDE dependencies, so it is fully unit-tested; the actions only
 * decide which roots to search and how to present the result.
 */
public final class OneApiTools {

    /** oneAPI command-line tools the plugin knows about, with their path relative to a oneAPI root. */
    public enum Tool {
        ICPX("compiler/latest/bin/icpx"),
        VTUNE("vtune/latest/bin64/vtune"),
        ADVISOR("advisor/latest/bin64/advisor"),
        GDB_ONEAPI("debugger/latest/bin/gdb-oneapi");

        private final String relativePath;

        Tool(String relativePath) {
            this.relativePath = relativePath;
        }

        /** Relative path of the executable inside a oneAPI root, with {@code .exe} on Windows. */
        public String relativePath(boolean windows) {
            return windows ? relativePath + ".exe" : relativePath;
        }
    }

    private OneApiTools() {
    }

    /**
     * Candidate oneAPI install roots in search order: {@code $ONEAPI_ROOT} first (set by
     * {@code setvars.sh}), then the default install locations for the current OS family.
     */
    @NotNull
    public static List<Path> candidateRoots(@Nullable String oneApiRootEnv, @NotNull String userHome, boolean windows) {
        Set<Path> roots = new LinkedHashSet<>();
        if (oneApiRootEnv != null && !oneApiRootEnv.isBlank()) {
            roots.add(Paths.get(oneApiRootEnv.trim()));
        }
        if (windows) {
            roots.add(Paths.get("C:\\Program Files (x86)\\Intel\\oneAPI"));
        } else {
            roots.add(Paths.get("/opt/intel/oneapi"));
            roots.add(Paths.get(userHome, "intel", "oneapi"));
            roots.add(Paths.get("/usr/local/intel/oneapi"));
        }
        return new ArrayList<>(roots);
    }

    /** Returns the first existing executable of {@code tool} under the given roots. */
    @NotNull
    public static Optional<Path> locate(@NotNull Tool tool, @NotNull List<Path> roots, boolean windows) {
        for (Path root : roots) {
            Path candidate = root.resolve(tool.relativePath(windows));
            if (Files.isRegularFile(candidate) && (windows || Files.isExecutable(candidate))) {
                return Optional.of(candidate);
            }
        }
        return Optional.empty();
    }

    /** Roots that contain a usable DPC++ compiler ({@code icpx}). */
    @NotNull
    public static List<Path> installations(@NotNull List<Path> roots, boolean windows) {
        List<Path> found = new ArrayList<>();
        for (Path root : roots) {
            if (locate(Tool.ICPX, List.of(root), windows).isPresent()) {
                found.add(root);
            }
        }
        return found;
    }

    /** {@code vtune -collect <analysis> -result-dir <dir> -- <executable>}. */
    @NotNull
    public static List<String> vtuneCommand(@NotNull String vtune, @NotNull String analysis,
                                            @NotNull Path resultDir, @NotNull String executable) {
        return List.of(vtune, "-collect", analysis, "-result-dir", resultDir.toString(), "--", executable);
    }

    /** {@code advisor --collect=<analysis> --project-dir=<dir> -- <executable>}. */
    @NotNull
    public static List<String> advisorCommand(@NotNull String advisor, @NotNull String analysis,
                                              @NotNull Path projectDir, @NotNull String executable) {
        return List.of(advisor, "--collect=" + analysis, "--project-dir=" + projectDir, "--", executable);
    }

    /** {@code gdb-oneapi <executable>}. */
    @NotNull
    public static List<String> gdbCommand(@NotNull String gdb, @NotNull String executable) {
        return List.of(gdb, executable);
    }

    /**
     * Renders a command for display / copy-paste into a POSIX shell. Arguments containing
     * whitespace or shell metacharacters are single-quoted.
     */
    @NotNull
    public static String toShellLine(@NotNull List<String> command) {
        StringBuilder sb = new StringBuilder();
        for (String arg : command) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(quote(arg));
        }
        return sb.toString();
    }

    private static String quote(String arg) {
        if (!arg.isEmpty() && arg.matches("[A-Za-z0-9_./=:@%+,-]+")) {
            return arg;
        }
        return "'" + arg.replace("'", "'\\''") + "'";
    }
}
