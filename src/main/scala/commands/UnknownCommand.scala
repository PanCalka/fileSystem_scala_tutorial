package commands

import filesys.State

class UnknownCommand extends Command {
  override def apply(state: State): State = state.setMessage("command not found")
}
