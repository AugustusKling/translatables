package translatables;

/**
 * Object that is able to provide translations.
 */
public interface Adapter {
	/**
	 * @param translationKey Key in list of translations.
	 * @return Translated text.
	 * @throws NotTranslated If requested translation cannot be provided.
	 */
	public String get(String translationKey) throws NotTranslated;
}
