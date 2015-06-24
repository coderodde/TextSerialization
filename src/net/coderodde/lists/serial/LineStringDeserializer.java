    package net.coderodde.lists.serial;

    /**
     * This interface defines the API for deserializing the objects from their 
     * textual representation.
     * 
     * @author Rodion "rodde" Efremov
     * @version 1.61
     * @param <E> the object type.
     */
    @FunctionalInterface
    public interface LineStringDeserializer<E> {

        /**
         * Deserializes an object from its textual representation.
         * 
         * @param  text the string representing the state of the object.
         * @return the actual, deserialized object.
         */
        public E deserialize(String text);
    }
