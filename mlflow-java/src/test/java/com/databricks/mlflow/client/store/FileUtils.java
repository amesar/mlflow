package com.databricks.mlflow.client.store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.*;

// Thanks to: https://stackoverflow.com/questions/15022219/does-files-createtempdirectory-remove-the-directory-after-jvm-exits-normally

public class FileUtils {

  public static void recursiveDeleteOnExit(Path path) throws IOException {
    Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(Path file,
          @SuppressWarnings("unused") BasicFileAttributes attrs) {
        file.toFile().deleteOnExit();
        return FileVisitResult.CONTINUE;
      }
      @Override
      public FileVisitResult preVisitDirectory(Path dir,
          @SuppressWarnings("unused") BasicFileAttributes attrs) {
        dir.toFile().deleteOnExit();
        return FileVisitResult.CONTINUE;
      }
    });
  }
  
  public static void recursiveDeleteOnShutdownHook(final Path path) {
    Runtime.getRuntime().addShutdownHook(new Thread(
      new Runnable() {
        @Override
        public void run() {
          try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
              @Override
              public FileVisitResult visitFile(Path file,
                  @SuppressWarnings("unused") BasicFileAttributes attrs)
                  throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
          }
          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException e)
              throws IOException {
            if (e == null) {
              Files.delete(dir);
              return FileVisitResult.CONTINUE;
            }
            // directory iteration failed
            throw e;
          }
          });
        } catch (IOException e) {
          throw new RuntimeException("Failed to delete "+path, e);
        }
      }}));
  }
}
