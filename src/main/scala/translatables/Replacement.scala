package translatables

class Replacement(val placeholder:TypedPlaceholder, value:Any) {
	def getCategory(value:Any) = placeholder.domain.getCategory(value)
	def getValueString() = value.toString
}