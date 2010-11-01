package ru.ipo.dces.buildutils;

import ru.ipo.dces.clientservercommunication.BinInfo;
import ru.ipo.structurededitor.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;

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
     *
     * @param c bin to convert
     * @return name of created class name
     * @throws FileNotFoundException if failed to create file for generated class
     */
    public String convert(Class<?> c) throws FileNotFoundException {
        String newClassName = makeNewClassName(c);
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

        outputGetBin(out, publicFields, c);

        outputLoadBin(out, publicFields, c);

        outputGetLayoutMethod(out, publicFields.toArray(new Field[publicFields.size()]));

        out.println('}');
        out.close();

        return newClassName;
    }

    private void outputLoadBin(PrintStream out, LinkedList<Field> publicFields, Class<?> c) {
        out.println();
        out.printf(
                "%spublic void loadBin(%s %s) {\n",
                CodeGeneratorSettings.INDENT,
                c.getCanonicalName(),
                CodeGeneratorSettings.getUniqueVariable()
        );

        for (Field field : publicFields)
            outputToFieldTransformer(
                    out,
                    field.getType(),
                    field.getName(),
                    CodeGeneratorSettings.INDENT + CodeGeneratorSettings.INDENT,
                    1
            );

        out.println(CodeGeneratorSettings.INDENT + "}");
    }

    private void outputToFieldTransformer(PrintStream out, Class<?> fieldType, String fieldName, String indent, int extraVarInd) {
        if (fieldType.isArray()) {
            out.printf(
                "%s%s = new %s;\n",
                    indent,
                    fieldName,
                    createArray(
                            type2string(fieldType.getComponentType()),
                            CodeGeneratorSettings.getUniqueVariable() + "." + fieldName + ".length"
                    )
            );
            out.printf(
                    "%sfor (int %s = 0; %s < %s.%s.length; %s++) {\n",
                    indent,
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd),
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd),
                    CodeGeneratorSettings.getUniqueVariable(),
                    fieldName,
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd)
            );
            outputToFieldTransformer(
                    out,
                    fieldType.getComponentType(),
                    fieldName + "[" + CodeGeneratorSettings.getUniqueVariable(extraVarInd) + "]",
                    indent + CodeGeneratorSettings.INDENT,
                    extraVarInd + 1
            );
            out.println(indent + "}");
        } else {
            if (needsConversion(fieldType)) {
                out.printf(
                    "%s%s.loadBin(%s.%s);\n",
                    indent,
                    fieldName,
                    CodeGeneratorSettings.getUniqueVariable(),
                    fieldName
                );
            } else {
                out.printf(
                    "%s%s = %s.%s;\n",
                    indent,
                    fieldName,
                    CodeGeneratorSettings.getUniqueVariable(),
                    fieldName
                );
            }
        }
    }

    private void outputGetBin(PrintStream out, LinkedList<Field> publicFields, Class<?> c) {
        out.println();

        out.printf(
                "%spublic %s getBin() {\n",
                CodeGeneratorSettings.INDENT,
                c.getCanonicalName()
        );

        out.printf(
                "%s%s%s %s = new %s();\n",
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.INDENT,
                c.getCanonicalName(),
                CodeGeneratorSettings.getUniqueVariable(),
                c.getCanonicalName()
        );

        for (Field field : publicFields)
            outputFromFieldTransformer(
                    out,
                    field.getType(),
                    field.getName(),
                    CodeGeneratorSettings.INDENT + CodeGeneratorSettings.INDENT,
                    1
            );

        out.printf(
                "%s%sreturn %s;\n",
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.getUniqueVariable()
        );
        out.println(CodeGeneratorSettings.INDENT + "}");
    }

    private void outputFromFieldTransformer(PrintStream out, Class<?> fieldType, String fieldName, String indent, int extraVarInd) {
        if (fieldType.isArray()) {
            out.printf(
                "%s%s.%s = new %s;\n",
                    indent,                  
                    CodeGeneratorSettings.getUniqueVariable(),
                    fieldName,
                    createArray(fieldType.getComponentType().getCanonicalName(), fieldName + ".length")
            );
            out.printf(
                    "%sfor (int %s = 0; %s < %s.length; %s++) {\n",
                    indent,
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd),
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd),
                    fieldName,
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd)
            );
            outputFromFieldTransformer(
                    out,
                    fieldType.getComponentType(),
                    fieldName + "[" + CodeGeneratorSettings.getUniqueVariable(extraVarInd) + "]",
                    indent + CodeGeneratorSettings.INDENT,
                    extraVarInd + 1
            );
            out.println(indent + "}");
        } else
            out.printf(
                "%s%s.%s = %s%s;\n",
                indent,
                CodeGeneratorSettings.getUniqueVariable(),
                fieldName,
                fieldName,
                needsConversion(fieldType) ? ".getBin()" : ""
            );
    }

    private String createArray(String name, String ind) {
        //cut all postfix []-s, insert new before
        if (name.endsWith("[]"))
            return createArray(name.substring(0, name.length() - 2), ind) + "[]";
        else
            return name + "[" + ind + "]";         
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
                "%s%sreturn new %s(",
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.INDENT,
                Vert.class.getCanonicalName()
        );

        //output cells for each property
        boolean isFirst = true;
        for (Field field : publicFields) {
            //skip fields that are marked to be skipped
            BinInfo binInfo = field.getAnnotation(BinInfo.class);
            if (binInfo == null || binInfo.editable()) {
                outputFieldEditor(out, CodeGeneratorSettings.INDENT + CodeGeneratorSettings.INDENT + CodeGeneratorSettings.INDENT, field, isFirst);
                isFirst = false;
            }
        }
        out.println();

        //output vert ending and return
        out.println(CodeGeneratorSettings.INDENT + CodeGeneratorSettings.INDENT + ");");

        out.println(CodeGeneratorSettings.INDENT + "}");
    }

    private void outputFieldEditor(PrintStream out, String indent, Field field, boolean isFirst) {
        if (!isFirst)
            out.println(",");
        else
            out.println();

        //get title
        BinInfo binInfo = field.getAnnotation(BinInfo.class);
        String title = binInfo == null ? "" : binInfo.title();
        if (title.isEmpty())
            title = field.getName();

        //new Horiz(
        out.printf("%snew %s(\n", indent, Horiz.class.getCanonicalName());
        //    new ConstCell("fieldName"),
        out.printf(
                "%s%snew %s(\"%s:\"),\n",
                indent,
                CodeGeneratorSettings.INDENT,
                ConstantCell.class.getCanonicalName(),
                title
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
        out.print(indent + ")");
    }

    private String makeNewClassName(Class<?> className) {
        if (className.isArray())
            return makeNewClassName(className.getComponentType()) + "[]";
        else
            return className.getSimpleName() + postfix;
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
        BinInfo binInfo = field.getAnnotation(BinInfo.class);
        String defaultValue = binInfo == null ? "" : binInfo.defaultValue();

        out.printf(
                "%sprivate %s %s%s;\n",
                CodeGeneratorSettings.INDENT,
                type2string(field.getType()),
                field.getName(),
                defaultValue.isEmpty() ? "" : " = " + defaultValue
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
        if (needsConversion(type))
            name = CodeGeneratorSettings.EDITOR_BEANS_PACKAGE + '.' +
                    makeNewClassName(type);        
        return name;
    }

    private boolean needsConversion(Class<?> type) {
        return
                type.getCanonicalName().startsWith(CodeGeneratorSettings.BINS_PACKAGE)
                        &&                        
                        !type.isEnum();
    }
}