package net.coderodde.lists.serial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * This class contains static methods for serializing the lists of elements to
 * a textual representation and deserializing it back to the list of elements.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.61
 */
public class LineStringSerializationFactory {

    /**
     * The alias for the new line string.
     */
    private static final String ENDL = "\n";
    
    // Do not instantiate this class.
    private LineStringSerializationFactory() {}

    /**
     * Serializes the elements in <code>list</code>. The <code>serializer</code>
     * is supposed to return a single line of text with no new line characters.
     * That string is supposed to encode the state of the serialized element. 
     * The order of encoding lines is dictated by the iteration order of the 
     * input list.
     * 
     * <b>Note:</b> this serialization procedure assumes that
     * <code>serializer</code> does not map any element to a string containing
     * a new line character as it is used for separating the element encodings.
     * 
     * @param <E>        the actual type of elements to serialize.
     * @param collection the collection of elements to serialize.
     * @param serializer the serializer returning a line of text encoding the 
     *                   state of the input element.
     * @return           a string each line of which encodes a single element.
     * 
     * @throws IllegalArgumentException if <code>serializer</code> returns a
     *                                  string containing a new line character.
     */
    public static <E> String serialize(Collection<E> collection, 
                                       LineStringSerializer<E> serializer) {
        StringBuilder sb = new StringBuilder();

        for (E element : collection) {
            String line = serializer.serialize(element);

            if (line.contains(ENDL)) {
                throw new IllegalArgumentException(
                        "The line serializer may not return the new line " +
                        "character in its output.");
            }

            sb.append(line).append(ENDL);
        }

        return sb.toString();
    }

    /**
     * Deserializes the list from the input text <code>text</code>. Each line 
     * is expected to produce exactly one element.
     * 
     * @param  <E>          the actual deserialized element type.
     * @param  text         the entire string holding the encoding of the entire
     *                      list.
     * @param  deserializer the deserializer converting each input line to an
     *                      element whose state is encoded by that line.
     * @return              the list of elements encoded by <code>text</code> in
     *                      the same order as their respective encoding lines.
     */
    public static <E> List<E> 
        deserialize(String text, LineStringDeserializer<E> deserializer) {
        List<E> ret = new ArrayList<>();
        Scanner scanner = new Scanner(text);

        while (scanner.hasNextLine()) {
            ret.add(deserializer.deserialize(scanner.nextLine()));
        }

        scanner.close();
        return ret;
    }
}
