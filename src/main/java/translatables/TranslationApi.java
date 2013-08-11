package translatables;

/**
 * API for Java programs that want to use translatables.
 */
public interface TranslationApi {
	/**
	 * @param replacements
	 *            Any number of values to replace. Translations is expected to
	 *            define placeholders called 0, 1, ….
	 * @return Translated text.
	 */
	public String getVariadic(Language lang, Adapter adapter, String key,
			Object... placeholder);

	/**
	 * @param replacements
	 *            Values to replace. Keys match placeholder names.
	 * @return Translated text.
	 */
	public String get(Language lang, Adapter adapter, String sourceKey,
			java.util.Map<String, ?> replacements);
}
