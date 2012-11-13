package translatables

/**
 * A placeholder in a translation that is to be filled with a value during translation.
 */
case class Replaceable(override val name:String) extends Placeholder(name) {

}