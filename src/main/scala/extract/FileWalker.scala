package extract

import java.io.File

/**
 * Recursively processes a directory structure by calling a callback
 */
class FileWalker {
  /**
   * @param root Top level file or directory
   * @param filter Limits the visited files by returning None for a candidate
   * @param concreteFileConsumer Processor that is invoked for each visited file
   */
  def walk(root: File, filter: (File) => Option[File], concreteFileConsumer: (File) => Unit): Unit = filter(root) match {
    case Some(root) => {
      if (root.isFile()) {
        concreteFileConsumer(root)
      } else if(root.canRead() && root.isDirectory()) {
        root.listFiles().foreach[Unit](walk(_, filter, concreteFileConsumer))
      }
    }
    case None => // Ignoring file the filter instructed to skip
  }
}