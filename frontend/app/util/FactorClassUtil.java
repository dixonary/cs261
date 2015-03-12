package util;

import team16.cs261.common.meta.FactorArity;
import team16.cs261.common.meta.FactorClass;
import team16.cs261.common.meta.FactorGroup;

import java.util.*;

/**
 * Created by martin on 05/03/15.
 */
public class FactorClassUtil {

    public static final String INDENT = "&nbsp;&nbsp;&nbsp;";

    public static Map<String, EnumSet<FactorClass>> mapping = new HashMap<>();

    static {

        for (FactorArity e : FactorArity.values()) {
            mapping.put(e.name(), FactorClass.getClasses(e));
        }

        for (FactorGroup e : FactorGroup.values()) {
            mapping.put(e.name(), FactorClass.getClasses(e));
        }

        for (FactorClass a : FactorClass.values()) {
            mapping.put(a.name(), EnumSet.of(a));
        }
    }


    public static List<Item> getFactorTree() {
        List<Item> items = new ArrayList<>();

        items.add(new Item("null", "*"));

        for(FactorClass fc : FactorClass.getImplemented()) {
            items.add(new Item(fc.name(), fc.getLabel()));
        }

        return items;
    }

    //public static Map<String, String> getFactorTree() {
    public static List<Item> getFactorTree2() {
        //Map<String, String> selection = new HashMap<>();
        List<Item> items = new ArrayList<>();

        items.add(new Item("null", "*"));

        for (FactorArity arity : FactorArity.values()) {
            //selection.put(arity.getKey(), arity.getLabel());
            items.add(new Item(arity.name(), arity.getLabel(), true));

            for (FactorGroup group : FactorGroup.getGroups(arity)) {
                //selection.put(group.getKey(), group.getLabel());
                items.add(new Item(group.name(), INDENT + group.getLabel(), true));

                for (FactorClass cls : FactorClass.getClasses(group))
                    //selection.put(cls.getKey(), cls.getLabel());
                    items.add(new Item(cls.name(), INDENT + INDENT + cls.getLabel()));
            }
        }

        //return selection;
        return items;
    }

    public static EnumSet<FactorClass> getClasses(String name) {
        return mapping.get(name);
    }

    public static class Item {
        public String value;
        public String label;
        public boolean group = false;

        public Item(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public Item(String value, String label, boolean group) {
            this.value = value;
            this.label = label;
            this.group = group;
        }
    }


    //public static Map<String, String> getFactorTree() {
   /* public static List<Option> getFactorTree() {
        //Map<String, String> selection = new HashMap<>();
        List<Option> options = new ArrayList<>();

        options.add(new Option("null", "*"));

        for (FactorArity arity : FactorArity.values()) {
            OptGroup arityGroup = new OptGroup(arity.getLabel());
            options.add(arityGroup);

            //selection.put(arity.getKey(), arity.getLabel());
            arityGroup.add(new Option(arity.getKey(), "*"));

            for (FactorGroup group : FactorGroup.getGroups(arity)) {
                OptGroup fgGroup = new OptGroup(group.getLabel());
                arityGroup.add(fgGroup);

                //selection.put(group.getKey(), group.getLabel());
                fgGroup.add(new Option(group.getKey(), indent+group.getLabel()));

                for (FactorClass cls : FactorClass.getClasses(group))
                    //selection.put(cls.getKey(), cls.getLabel());
                    fgGroup.add(new Option(cls.getKey(), indent+indent+cls.getLabel()));
            }


        }

        //return selection;
        return options;
    }

    public static class OptGroup extends Option {
        public List<Option> options = new ArrayList<>();

        public OptGroup(String label) {
            super(null, label);
        }

        public void add(Option opt) {
            options.add(opt);
        }
    }

    public static class Option {
        public String value;
        public String label;

        public Option(String value, String label) {
            this.value = value;
            this.label = label;
        }
    }*/




}
