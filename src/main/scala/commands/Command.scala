package commands

import filesys.State

trait Command {


  def apply(state: State):State


}

object Command {
  val MKDIR = "mkdir"

  def emptyCommand : Command = (state: State) => state

  def incompleteCommand(name: String) : Command = (state: State) => state.setMessage(name + " incomplete command")

  def from(input: String): Command = {
    val tokens: Array[String] = input.split(" ")

    if(tokens.isEmpty) emptyCommand
    else if (MKDIR.equals(tokens(0))) {
      if (tokens.length < 2) incompleteCommand(MKDIR)
      else new MkDir(tokens(1))
    }
    else new UnknownCommand
  }
}