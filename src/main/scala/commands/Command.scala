package commands

import filesys.State

trait Command {

  def apply(state: State):State


}

object Command {

  def from(input: String): Command = new UnknownCommand
}