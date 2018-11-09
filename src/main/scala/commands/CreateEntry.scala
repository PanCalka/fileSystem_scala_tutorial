package commands

import files.{DirEntry, Directory}
import filesys.State

abstract class CreateEntry(name: String) extends Command {

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }


  def doCreateEntry(state: State, name: String): State = {
    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if(path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val wd = state.wd

    val allDirInPath = wd.getAllFoldersInPath

    val newEntry: DirEntry = createSpecificEntry(state)

    val newRoot = updateStructure(state.root, allDirInPath, newEntry)

    val newWd = newRoot.findDescendant(allDirInPath)

    State(newRoot, newWd)
  }

  def createSpecificEntry(state: State): DirEntry

  override def apply(state: State): State = {
    val wd = state.wd
    if(wd.hasEntry(name)){
      state.setMessage("Entry " + name + " already exist")
    } else if(name.contains(Directory.SEPARATOR)){
      state.setMessage(name + "shouldnt contain separators")
    } else if(checkIllegal(name)) {
      state.setMessage(name + ": illegal entry name")
    } else {
      doCreateEntry(state, name)
    }

  }
}
