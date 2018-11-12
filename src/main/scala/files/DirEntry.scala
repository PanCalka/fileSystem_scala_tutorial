package files

abstract class DirEntry(val parentPath: String, val name: String) {
  def path: String = {
    val separatorIfNessesery =
      if(Directory.ROOT_PATH.equals(parentPath))""
      else Directory.SEPARATOR
    parentPath + separatorIfNessesery + name
  }

  def asDirectory: Directory

  def isDirectory: Boolean

  def isFile: Boolean

  def getType: String

  def asFile: File
}
