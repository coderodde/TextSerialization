package net.coderodde.lists.serial;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class LineStringSerializationFactoryTest {

    private static Random random;

    @BeforeClass
    public static void init() {
        long seed = System.currentTimeMillis();
        System.out.println("Seed: " + seed);
        random = new Random(seed);
    }

    @Test
    public void testSimpleIntegers() {
        List<Integer> input = getRandomIntegerList(1000, random);
        String text = LineStringSerializationFactory
                      .serialize(input, Object::toString);
        List<Integer> output = LineStringSerializationFactory
                               .deserialize(text, Integer::parseInt);
        assertTrue(input.equals(output));
    }

    @Test
    public void testListOfLists() {
        // Create data to serialize.
        List<List<Integer>> input = getRandomListOfLists(1000, random);
        // Serialize it.
        String text = LineStringSerializationFactory
                      .serialize(input, new IntListSerializer());
        // Deserialize it.
        List<List<Integer>> output = 
                LineStringSerializationFactory
                .deserialize(text, new IntListDeserializer());
        // Compare.
        assertTrue(input.equals(output));
    }

    /**
     * Constructs a random integer array.
     * 
     * @param  size   the length of the integer array.
     * @param  random the random number generator.
     * @return        the integer array.
     */
    private static List<Integer> 
        getRandomIntegerList(int size, Random random) {
        return random.ints(size).boxed().collect(Collectors.toList());
    }

    /**
     * Constructs a random list of integer lists.
     * 
     * @param  size   the length of the outer list.
     * @param  random the random number generator.
     * @return        the random list of integer lists.
     */
    private static List<List<Integer>> 
        getRandomListOfLists(int size, Random random) {
        List<List<Integer>> ret = new ArrayList<>(size);

        for (int i = 0; i < size; ++i) {
            ret.add(getRandomIntegerList(random.nextInt(50), random));
        }

        return ret;
    }

    private static class IntListSerializer
    implements LineStringSerializer<List<Integer>> {

        @Override
        public String serialize(List<Integer> list) {
            StringBuilder sb = new StringBuilder();
            int index = 0;

            for (Integer i : list) {
                sb.append(i.toString());

                if (index < list.size() - 1) {
                    sb.append(",");
                }
            }

            return sb.toString();
        }
    }

    private static class IntListDeserializer 
    implements LineStringDeserializer<List<Integer>> {

        @Override
        public List<Integer> deserialize(String text) {
            return Stream.of(text.split(","))
                         .map(String::trim)
                         .filter(s -> !s.isEmpty())
                         .map(LineStringSerializationFactoryTest::asInteger)
                         .filter(Objects::nonNull)
                         .collect(Collectors.toList());
        }
    }

    private static Integer asInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
