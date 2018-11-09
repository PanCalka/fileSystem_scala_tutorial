package commands
import files.Directory
import filesys.State

class MkDir(name: String) extends Command {

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }

  def doMkDir(state: State, name: String): State = ???

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
