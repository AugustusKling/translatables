package translatables

/**
 * Element of a parsed translation
 */
abstract class Placeholder(val name:String) {
	override def toString() = name
}