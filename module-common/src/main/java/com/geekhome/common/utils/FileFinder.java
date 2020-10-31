package com.geekhome.common.utils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class FileFinder extends SimpleFileVisitor<Path> {

    private final PathMatcher _matcher;
    private final IFileFoundListener _fileFoundListener;

    public FileFinder(String pattern, IFileFoundListener fileFoundListener) {
        _fileFoundListener = fileFoundListener;
        _matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    }

    // Compares the glob pattern against
    // the file or directory name.
    private void find(Path file) {
        Path name = file.getFileName();
        if (name != null && _matcher.matches(name)) {
            _fileFoundListener.fileFound(file);
        }
    }

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attrs) {
        find(file);
        return CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir,
                                             BasicFileAttributes attrs) {
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file,
                                           IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }
}
