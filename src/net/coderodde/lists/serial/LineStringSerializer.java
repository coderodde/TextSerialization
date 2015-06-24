    package net.coderodde.lists.serial;

    /**
     * This interface defines the API for serializing an object to a string.
     * 
     * @author Rodion "rodde" Efremov
     * @version 1.61
     * @param <E> the object type.
     */
    @FunctionalInterface
    public interface LineStringSerializer<E> {

        /**
         * Returns the textual representation of the input object.
         * 
         * @param  object the object to serialize.
         * @return the textual representation of the input object.
         */
        public String serialize(E object);
    }
