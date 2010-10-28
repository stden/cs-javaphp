package ru.ipo.dces.buildutils;

import ru.ipo.structurededitor.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * Gets all public fields of class and creates class that has the same properties
 */
public class BinToBeanConverter {

    private final String postfix;

    public BinToBeanConverter(String postfix) {
        this.postfix = postfix;
    }

    /**
     * returns name of created class names
     * @param c bin to convert
     * @return name of created class name
     * @throws FileNotFoundException if failed to create file for generated class
     */
    public String convert(Class<?> c) throws FileNotFoundException {
        String newClassName = makeNewClassName(c.getSimpleName());
        File beanFile = new File(CodeGeneratorSettings.EDITOR_BEANS_FOLDER + newClassName + ".java");

        PrintStream out = new PrintStream(beanFile);

        out.println("package " + CodeGeneratorSettings.EDITOR_BEANS_PACKAGE + ";");
        out.println();
        out.println("/*" + CodeGeneratorSettings.HEADER + "*/");
        out.printf(
                "public class %s implements %s {\n",
                newClassName,
                DSLBean.class.getCanonicalName()
        );

        //DSLBeansRegistry.getInstance().registerBean(ThisBean.class);
        /*out.println();
        out.println(INDENT + "static {");
        out.printf(
                "%s%s%s.getInstance().registerBean(%s.class);\n",
                INDENT,
                INDENT,
                DSLBeansRegistry.class.getCanonicalName(),
                newClassName
        );
        out.println(INDENT + "}");*/

        //get all public fields
        LinkedList<Field> publicFields = new LinkedList<Field>();

        //iterate over all public fields
        for (Field field : c.getFields())
            if (Modifier.isPublic(field.getModifiers()))
                publicFields.add(field);

        if (!publicFields.isEmpty())
            out.println();

        for (Field field : publicFields)
            outputDeclaration(out, field);

        for (Field field : publicFields) {
            outputGetter(out, field);
            outputSetter(out, field);
        }

        outputGetLayoutMethod(out, publicFields.toArray(new Field[publicFields.size()]));

        out.println('}');
        out.close();

        return newClassName;
    }

    private void outputGetLayoutMethod(PrintStream out, Field[] publicFields) {
        out.println();
        out.printf(
                "%spublic %s getLayout() {\n",
                CodeGeneratorSettings.INDENT,
                Cell.class.getCanonicalName()
        );

        //output method body

        //output "cell = ..."
        out.printf(
                "%s%sreturn new %s(\n",
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.INDENT,
                Vert.class.getCanonicalName()
        );

        //output cells for each property
        for (int i = 0; i < publicFields.length; i++) {
            Field field = publicFields[i];
            //TODO skip fields that are marked to be skipped
            outputFieldEditor(out, CodeGeneratorSettings.INDENT + CodeGeneratorSettings.INDENT + CodeGeneratorSettings.INDENT, field, i == publicFields.length - 1);
        }

        //output vert ending and return
        out.println(CodeGeneratorSettings.INDENT + CodeGeneratorSettings.INDENT + ");");

        out.println(CodeGeneratorSettings.INDENT + "}");
    }

    private void outputFieldEditor(PrintStream out, String indent, Field field, boolean isLast) {
        //new Horiz(
        out.printf("%snew %s(\n", indent, Horiz.class.getCanonicalName());
        //    new ConstCell("fieldName"),
        out.printf(
                "%s%snew %s(\"%s:\"),\n",
                indent,
                CodeGeneratorSettings.INDENT,
                ConstantCell.class.getCanonicalName(),
                field.getName()
        );
        //    new FieldCell("fieldName")
        out.printf(
                "%s%snew %s(\"%s\")\n",
                indent,
                CodeGeneratorSettings.INDENT,
                field.getType().isArray() ?
                        VertArray.class.getCanonicalName() :
                        FieldCell.class.getCanonicalName(),
                field.getName()
        );
        //close Horiz
        if (isLast)
            out.println(indent + ")");
        else
            out.println(indent + "),");
    }

    private String makeNewClassName(String className) {
        //TODO implement all cases, not only 'linear array of'
        if (className.endsWith("[]"))
            return className.substring(0, className.length() - 2) + postfix + "[]";
        return className + postfix;
    }

    private void outputGetter(PrintStream out, Field field) {
        out.println();
        out.printf(
                "%spublic %s %s() {\n%s%sreturn %s;\n%s}\n",
                CodeGeneratorSettings.INDENT,
                type2string(field.getType()),
                generateGetterName(field),
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.INDENT,
                field.getName(),
                CodeGeneratorSettings.INDENT
        );
    }

    private String generateSetterName(Field field) {
        return "set" + capitalize(field.getName());
    }

    private String generateGetterName(Field field) {
        if (field.getType().equals(boolean.class))
            return "is" + capitalize(field.getName());
        else
            return "get" + capitalize(field.getName());
    }

    private String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private void outputDeclaration(PrintStream out, Field field) {
        out.printf(
                "%sprivate %s %s;\n",
                CodeGeneratorSettings.INDENT,
                type2string(field.getType()),
                field.getName()
        );
    }

    private void outputSetter(PrintStream out, Field field) {
        out.println();
        out.printf(
                "%spublic void %s(%s %s) {\n%s%sthis.%s = %s;\n%s}\n",
                CodeGeneratorSettings.INDENT,
                generateSetterName(field),
                type2string(field.getType()),
                field.getName(),
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.INDENT,
                field.getName(),
                field.getName(),
                CodeGeneratorSettings.INDENT
        );
    }

    /**
     * Converts type to its name to use in code.
     * This is just getCanonicalName(), but if the type corresponds
     * to the bin package, then the name converts to the new package
     *
     * @param type type to convert
     * @return name of the type
     */
    private String type2string(Class<?> type) {
        String name = type.getCanonicalName();
        //TODO rewrite it in the following manner: if type is connected anyhow with the old package
        if (name.startsWith(CodeGeneratorSettings.BINS_PACKAGE)) {
            if (type.getEnclosingClass() != null)
                return name;
            name = CodeGeneratorSettings.EDITOR_BEANS_PACKAGE + '.' +
                    makeNewClassName(type.getSimpleName());
        }
        return name;
    }
}