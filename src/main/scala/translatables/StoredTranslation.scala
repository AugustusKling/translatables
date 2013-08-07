package translatables

/**
 * Stores translation that has been read from a persistent storage. Holds supplemental data dependent on format (such
 * as fuzzy-flags and comments by translators).
 * @param translationKey Translation identifier.
 * @param translation Translation as entered by translator.
 * @param extra Information related to a single translation that was stored, too.
 */
case class StoredTranslation[FormatDependent](translationKey: String, translation: String, extra: FormatDependent)