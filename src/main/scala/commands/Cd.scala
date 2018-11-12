package commands
import files.{DirEntry, Directory}
import filesys.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {



  def findEntry(root: Directory, path: String): DirEntry = {
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

    @tailrec
    def collapsRelativeTokens(path: List[String], result: List[String]): List[String] = {
      if(path.isEmpty) result
      else if(".".equals(path.head)) collapsRelativeTokens(path.tail, result)
      else if("..".equals(path.head)){
        if(result.isEmpty) null
        else collapsRelativeTokens(path.tail, result.tail)
      }
      else collapsRelativeTokens(path.tail, result :+ path.head)
    }

    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList

    val newTokens = collapsRelativeTokens(tokens, List())
    if (newTokens == null) null

    else findEntryHelper(root, newTokens)
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
