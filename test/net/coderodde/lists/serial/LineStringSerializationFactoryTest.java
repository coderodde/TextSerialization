    package net.coderodde.lists.serial;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;
    import java.util.stream.Collectors;
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

        /**
         * janos, yes we can serialize a list of lists.
         */
        @Test
        public void testListOfLists() {
            List<List<Integer>> input = getRandomListOfLists(1000, random);
            String text = LineStringSerializationFactory
                          .serialize(input, Object::toString);
            List<List<Integer>> output = 
                    LineStringSerializationFactory
                    .deserialize(text, new IntListDeserializer());

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

        private static class IntListDeserializer 
        implements LineStringDeserializer<List<Integer>> {

            @Override
            public List<Integer> deserialize(String text) {
                List<Integer> ret = new ArrayList<>();
                String[] intStrings = text.substring(1, text.length() - 1)
                                          .split(",");

                for (String s : intStrings) {
                    // 'trim' in order to get rid of any leading/trailing 
                    // white space.
                    s = s.trim();

                    if (!s.equals("")) {
                        ret.add(Integer.parseInt(s.trim()));
                    }
                }

                return ret;    
            }
        }
    }
