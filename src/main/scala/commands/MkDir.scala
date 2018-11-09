package commands
import java.nio.file.Path

import files.Directory
import filesys.State

class MkDir(name: String) extends Command {

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }


  def doMkDir(state: State, name: String): State = {
    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: Directory): Directory = {
      if(path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val wd = state.wd

    val allDirInPath = wd.getAllFoldersInPath

    val newDir = Directory.empty(wd.path, name)

    val newRoot = updateStructure(state.root, allDirInPath, newDir)

    val newWd = newRoot.findDescendant(allDirInPath)

    State(newRoot, newWd)
  }

  override def apply(state: State): State = {
    val wd = state.wd
    if(wd.hasEntry(name)){
      state.setMessage("Entry " + name + " already exist")
    } else if(name.contains(Directory.SEPARATOR)){
      state.setMessage(name + "shouldnt contain separators")
    } else if(checkIllegal(name)) {
      state.setMessage(name + ": illegal entry name")
    } else {
      doMkDir(state, name)
    }

  }
}
