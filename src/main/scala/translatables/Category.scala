package translatables

/**
 * Class of values within domain. For example all numbers divisible by 2 in a domain numbers.
 * @param name Category name in order to identify category in translation keys
 * @param covers Function to determine whether a value is covered by the category
 */
class Category(val name:String, val covers:Any => Boolean) {

}