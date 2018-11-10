package commands
import files.{DirEntry, Directory}
import filesys.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {



  def findEntry(root: Directory, absolutePath: String): DirEntry = {
    @tailrec
    def findEntryHelper(currentDirectry: Directory, path: List[String]): DirEntry = {
      if(path.isEmpty || path.head.isEmpty) currentDirectry
      else if(path.tail.isEmpty) currentDirectry.findEntry(path.head)
      else{
        val nextDir = currentDirectry.findEntry(path.head)
        if(nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    val tokens: List[String] = absolutePath.substring(1).split(Directory.SEPARATOR).toList

    findEntryHelper(root, tokens)
  }

  override def apply(state: State): State = {
    val root = state.root

    val wd = state.wd

    val absolutePath =
      if(dir.startsWith(Directory.SEPARATOR)) dir
      else if(wd.isRoot) wd.path + dir
    else wd.path + Directory.SEPARATOR + dir

    val destinationDirectory = findEntry(root, absolutePath)

    if(destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage(dir + ": no such directory")
    else
      State(root, destinationDirectory.asDirectory)



  }
}
