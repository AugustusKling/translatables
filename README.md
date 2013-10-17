translatables
=============

translatables is a tool for localizing Scala and Java programs. It features a library to translate text by looking up translations from your translations file according to the grammar of the target language. A command line tool is also provided to scan Scala and Java sources for translatable texts, and to create and maintain translation lists.

A JavaScript based implementation with [online demo](http://augustuskling.github.io/translatables-js/) of the same concept exists. The demo is not as feature-rich as this implementation.

Multilingual domain aware translations
--------------------------------------

Being domain aware when building translation lists or looking up translations means that the number of translations varies between languages. The grammar of the target language is taken into account for pluralization and the like which leads to using the minimal number of translations required to completely cover a language.

Essentially this removes the need to adapt the translated software when a new language is to be added. Programmer won't need knowledge of the user interface's language and code is less cluttered.

Usage for the impatient
-----------------------

A method call is used to identify translatable texts and to perform the translation. This is similar to other translation tools. For example a program translated with translatables might look like the following fragment.

```
import static your.application.tr;

public class Test {
	public static void main(String[] args){
		String translatedValue1 = tr("This is a sample");
		String translatedValue2 = tr("You've received {number(0)} messages.", 8);
	}
}

```

Translations can be extracted with the command line tool, for example:
```
java -jar translatables.jar extract --source /application/src/ --target /application/translations/de.json --language de --calls tr
```
The default implementation supports JSON, Java property files and gettext's PO files. Assess PO files for suitability because they are compatible with a range of translation systems and usually a good choice when working with external translators.

If you want to do some quick tests, be sure to try the `--pseudo-translations` flag. It generates accented versions of the input texts as translations.

Running the extraction leaves you with a file for manual translation:
```
{
  "This is a sample": "",
	"You've received {number(zero(0))} messages": "",
	"You've received {number(one(0))} messages": ""
}
```
Go on by filling out the blanks with the actual translations:
```
{
  "This is a sample": "Das ist ein Test",
	"You've received {number(zero(0))} messages": "Du hast {0} Nachrichten erhalten.",
	"You've received {number(one(0))} messages": "Du hast {0} Nachricht erhalten."
}
```
Finally, use the translatables library to select the suitable translations at runtime of your program. The library will take care of the grammar for most languages.

Building
--------
Run `sbt minimal/assembly` and `sbt extractor/assembly` from the checkout's root to build the library and command line tool, repectively.

Note that the library does not contain dependencies. It cannot be used to scan source code for translations and leaves it to your build process to make the Scala libary available on the classpath.

The command line tool is self-contained and can be used to as library, too.

Core Concepts
-------------

**Source Key** Text to be translated as it appears in the source code. A source key can refer to multiple translations of which one is chosen as soon as values for all placeholders are known. Source keys are language agnostic.

**Translation Key** Identifier of a translation. Those are automatically generated from the source keys in dependence of placeholder types. The amount of translation keys varies across languages where more complex grammar usually means more translation keys.

**Domain** Type of a placeholder. All languages support plain text, numbers, gender, date, time and currencies. Domains define the formatting of values and user-defined types can be added if required.

**Category** Values are examined and grouped according to the grammar of the target language in a way that the same grammar applies for all values in a category. For example if a target language has 3 plural forms, 3 categories would result for the number domain.

###  Identifying Translations

Most translation systems rely on invented keys to identify translations but translatables makes use of natural language sources in combination with domains. Even though one is not constrained to use natural language to identify translations, it simplifies the translation process.

In practice the programmer types in the source key (which could be an English sentence or an invented key) and marks the translation with a method call (or `TranslationKey` annotation) and leaves the rest to translatables’ automatisms.

### Placeholders

At times messages require translations that contain dynamic parts. Think about including a person’s name or a calculated number as examples.

Placeholders are typed right into the source keys by the programmer and can optionally be bound to a value domain if special treatment is desired. Values that don’t require special treatment are plain text. Treatment is required for numbers (plural forms), dates (formaing rules) and other domains and is handled by translatables according to the grammar of the target language and the conventions of the targeted region.

**{2}**  Third numeric placeholder. Note that the number only serves to identify the placeholder and does not restrict the values to be inserted nor the order in which the placeholders appear. The plain domain will be used and the placeholder value will be inserted unchanged because no domain was explicitly given.

**{testname}** Semantically equal to numeric placeholders but the names can make it easier for programmers and translators to grasp the meaning of the inserted value.

**{number(0)}** First numeric placeholder that is bound to the number domain. Translatables will automatically generate translation keys according to the pluralization rules of the target language. The correct translation will be chosen at runtime when the placeholder’s value is know.

**{number(testname)}** Semantically equal to numeric placeholder for number domain in previous example.

Any number of placeholders can be used in a message and domains are specified per placeholder. A mix of numeric and named placeholder can be used in the same message. In fact the numeric placeholders are just using digits as variable names – they don’t have special semantics.

Using translatables as library
------------------------------

### Scala

Define a method that determines the language of the user and provides the location where the translations are stored. The method can then be imported where needed to make convenient use of it.

```
/**
 * Translates a text, inserts formatted placeholder values.
 * @param key Source key. Might contain placeholders with numeric names.
 * @param placeholder Any number of values to insert.
 * @return Translated text.
 */
 def tr(key:String, placeholder:Object*):String = {
	// TODO Determine lang based on your application's language settings.
	val locale:Locale = Locale.getDefault();
	val lang:Language = Language.fromLocale(locale);

	// TODO Get translations from your infrastructure (for example property file, database).
	val translationFile:File = new File("translations/"
			+ lang.code().getLanguage() + ".json");
	val translations:Adapter = new JsonAdapter(translationFile);
	
	Translation.getVariadic(language, translations, key, placeholders: _*)
 }
```

Note that the `Translation` object holds more methods to work with placeholders from lists, maps and tuples which you might find useful, too.

### Java

Place a fragment similar to the following listing somewhere in your application where you can access it easily. It's usually most comfortable if you allow for a static import of the translation call.

```
/**
 * Translates a text, inserts formatted placeholder values.
 * @param key Source key. Might contain placeholders with numeric names.
 * @param placeholder Any number of values to insert.
 * @return Translated text.
 */
private static String tr(String key, Object... placeholder) {
	// TODO Determine lang based on your application's language settings.
	Locale locale = Locale.getDefault();
	Language lang = Language.fromLocale(locale);

	// TODO Get translations from your infrastructure (for example property file, database).
	File translationFile = new File("translations/"
			+ lang.code().getLanguage() + ".json");
	Adapter translations = new JsonAdapter(translationFile);

	return JavaApi.instance
			.getVariadic(lang, translations, key, placeholder);
}
```

The rest of the application simply uses the method as does not bother more about translations.

Feedback
--------

Please file any problems in this repository's issue tracker.
