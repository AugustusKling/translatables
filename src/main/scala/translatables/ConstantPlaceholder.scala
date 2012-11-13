package translatables

/**
 * Holds a constant string that is part of a translation
 */
case class ConstantPlaceholder(private val content:String) extends Placeholder(content) {
	override val name = content
}