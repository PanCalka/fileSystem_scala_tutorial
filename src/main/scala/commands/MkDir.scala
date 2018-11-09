package commands
import files.{DirEntry, Directory}
import filesys.State

class MkDir(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry = {
    Directory.empty(state.wd.path, name)
  }
}
