package translatables

/**
 * Placeholder for a value of certain type.
 * @param name Identifier for binding a value
 * @param domain Specifies which values are acceptable to be bound.
 */
case class TypedPlaceholder(override val name:String, val domain:Domain) extends Placeholder(name) {
	override def toString() = name+":"+domain.name
}