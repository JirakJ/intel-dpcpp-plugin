/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.oneapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OneApiToolsTest {

    @TempDir
    Path tmp;

    private Path install(Path root, OneApiTools.Tool tool, boolean windows) throws IOException {
        Path exe = root.resolve(tool.relativePath(windows));
        Files.createDirectories(exe.getParent());
        Files.writeString(exe, "#!/bin/sh\n");
        exe.toFile().setExecutable(true);
        return exe;
    }

    @Test
    void candidateRootsPutOneApiRootFirstAndDeduplicate() {
        List<Path> roots = OneApiTools.candidateRoots("/opt/intel/oneapi", "/home/u", false);
        assertEquals(Paths.get("/opt/intel/oneapi"), roots.get(0));
        assertEquals(3, roots.size());
        assertTrue(roots.contains(Paths.get("/home/u", "intel", "oneapi")));
    }

    @Test
    void candidateRootsIgnoreBlankEnvAndUseWindowsDefault() {
        List<Path> roots = OneApiTools.candidateRoots("  ", "C:\\Users\\u", true);
        assertEquals(1, roots.size());
        assertTrue(roots.get(0).toString().contains("oneAPI"));
        assertEquals(3, OneApiTools.candidateRoots(null, "/home/u", false).size());
    }

    @Test
    void relativePathAddsExeOnWindows() {
        assertEquals("vtune/latest/bin64/vtune", OneApiTools.Tool.VTUNE.relativePath(false));
        assertEquals("compiler/latest/bin/icpx.exe", OneApiTools.Tool.ICPX.relativePath(true));
    }

    @Test
    void locateFindsFirstRootWithTheTool() throws IOException {
        Path empty = tmp.resolve("empty");
        Path a = tmp.resolve("a");
        Path b = tmp.resolve("b");
        Files.createDirectories(empty);
        Path expected = install(a, OneApiTools.Tool.VTUNE, false);
        install(b, OneApiTools.Tool.VTUNE, false);
        Optional<Path> found = OneApiTools.locate(OneApiTools.Tool.VTUNE, List.of(empty, a, b), false);
        assertEquals(Optional.of(expected), found);
        assertFalse(OneApiTools.locate(OneApiTools.Tool.ADVISOR, List.of(empty, a, b), false).isPresent());
    }

    @Test
    void locateOnWindowsDoesNotRequireExecutableBit() throws IOException {
        Path exe = tmp.resolve("w").resolve(OneApiTools.Tool.GDB_ONEAPI.relativePath(true));
        Files.createDirectories(exe.getParent());
        Files.writeString(exe, "");
        assertEquals(Optional.of(exe), OneApiTools.locate(OneApiTools.Tool.GDB_ONEAPI, List.of(tmp.resolve("w")), true));
    }

    @Test
    void installationsAreRootsWithIcpx() throws IOException {
        Path withCompiler = tmp.resolve("full");
        Path withoutCompiler = tmp.resolve("partial");
        install(withCompiler, OneApiTools.Tool.ICPX, false);
        install(withoutCompiler, OneApiTools.Tool.VTUNE, false);
        assertEquals(List.of(withCompiler), OneApiTools.installations(List.of(withoutCompiler, withCompiler), false));
        assertTrue(OneApiTools.installations(List.of(), false).isEmpty());
    }

    @Test
    void commandBuilders() {
        Path dir = Paths.get("/p/.vtune");
        assertEquals(List.of("vtune", "-collect", "gpu-hotspots", "-result-dir", dir.toString(), "--", "./app"),
                OneApiTools.vtuneCommand("vtune", "gpu-hotspots", dir, "./app"));
        assertEquals(List.of("advisor", "--collect=roofline", "--project-dir=" + Paths.get("/p/.advisor"), "--", "./app"),
                OneApiTools.advisorCommand("advisor", "roofline", Paths.get("/p/.advisor"), "./app"));
        assertEquals(List.of("gdb-oneapi", "./app"), OneApiTools.gdbCommand("gdb-oneapi", "./app"));
    }

    @Test
    void shellLineQuotesOnlyWhenNeeded() {
        assertEquals("vtune -collect gpu-hotspots -- ./app",
                OneApiTools.toShellLine(List.of("vtune", "-collect", "gpu-hotspots", "--", "./app")));
        assertEquals("'/opt/my tools/vtune' '<your-executable>' ''",
                OneApiTools.toShellLine(List.of("/opt/my tools/vtune", "<your-executable>", "")));
        assertEquals("'it'\\''s'", OneApiTools.toShellLine(List.of("it's")));
        assertEquals("", OneApiTools.toShellLine(List.of()));
    }
}
