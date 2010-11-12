package ru.ipo.dces.buildutils;

import ru.ipo.dces.buildutils.raw.BinInfo;
import ru.ipo.structurededitor.model.*;

import java.io.*;
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
     * @throws IOException if failed to create file for generated class
     */
    public String convert(Class<?> c) throws IOException {
        String newClassName = makeNewClassName(c);
        File beanFile = new File(CodeGeneratorSettings.EDITOR_BEANS_FOLDER + newClassName + ".java");

        PrintStream out = new PrintStream(beanFile, "UTF-8");

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

        outputConstructor(out, newClassName);

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

        outputLoadBin(out, publicFields, c, newClassName);

        outputGetLayoutMethod(out, publicFields.toArray(new Field[publicFields.size()]));

        out.println('}');
        out.close();

        return newClassName;
    }

    private void outputConstructor(PrintStream out, String newClassName) {
        out.println(CodeGeneratorSettings.INDENT + "public " + newClassName + "() {");
        out.println(CodeGeneratorSettings.INDENT + "}");
    }

    /*private void outputConstructor(PrintStream out, LinkedList<Field> publicFields) {
        out.println();
    }*/

    private void outputLoadBin(PrintStream out, LinkedList<Field> publicFields, Class<?> c, String newClassName) {
        out.println();
        out.printf(
                "%spublic %s(%s %s) {\n",
                CodeGeneratorSettings.INDENT,
                newClassName,
                changeBinPackage(c.getCanonicalName()),
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
                    "%sif (%s == null) {\n%s%s%s = null;\n%s} else {\n",
                    indent,
                    CodeGeneratorSettings.getUniqueVariable() + "." + fieldName,
                    indent,
                    CodeGeneratorSettings.INDENT,
                    fieldName,
                    indent
            );
            String indent2 = indent + CodeGeneratorSettings.INDENT;
            out.printf(
                    "%s%s = new %s;\n",
                    indent2,
                    fieldName,
                    createArray(
                            type2string(fieldType.getComponentType()),
                            CodeGeneratorSettings.getUniqueVariable() + "." + fieldName + ".length"
                    )
            );
            out.printf(
                    "%sfor (int %s = 0; %s < %s.%s.length; %s++) {\n",
                    indent2,
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd),
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd),
                    CodeGeneratorSettings.getUniqueVariable(),
                    fieldName,
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd)
            );
            String newFieldName = fieldName + "[" + CodeGeneratorSettings.getUniqueVariable(extraVarInd) + "]";
            Class<?> newFieldType = fieldType.getComponentType();
            outputToFieldTransformer(
                    out,
                    newFieldType,
                    newFieldName,
                    indent2 + CodeGeneratorSettings.INDENT,
                    extraVarInd + 1
            );
            out.println(indent2 + "}");
            out.println(indent + "}");
        } else {
            if (needsConversion(fieldType)) {
                out.printf(
                        "%s%s = %s.%s == null ? null : new %s(%s.%s);\n",
                        indent,
                        fieldName,
                        CodeGeneratorSettings.getUniqueVariable(),
                        fieldName,
                        type2string(fieldType),
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
                changeBinPackage(c.getCanonicalName())
        );

        out.printf(
                "%s%s%s %s = new %s();\n",
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.INDENT,
                changeBinPackage(c.getCanonicalName()),
                CodeGeneratorSettings.getUniqueVariable(),
                changeBinPackage(c.getCanonicalName())
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
                    "%sif (%s == null) {\n%s%s%s.%s = null;\n%s} else {\n",
                    indent,
                    fieldName,
                    indent,
                    CodeGeneratorSettings.INDENT,
                    CodeGeneratorSettings.getUniqueVariable(),
                    fieldName,
                    indent
            );
            String indent2 = indent + CodeGeneratorSettings.INDENT;
            out.printf(
                    "%s%s.%s = new %s;\n",
                    indent2,
                    CodeGeneratorSettings.getUniqueVariable(),
                    fieldName,
                    createArray(fieldType.getComponentType().getCanonicalName(), fieldName + ".length")
            );
            out.printf(
                    "%sfor (int %s = 0; %s < %s.length; %s++) {\n",
                    indent2,
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd),
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd),
                    fieldName,
                    CodeGeneratorSettings.getUniqueVariable(extraVarInd)
            );
            outputFromFieldTransformer(
                    out,
                    fieldType.getComponentType(),
                    fieldName + "[" + CodeGeneratorSettings.getUniqueVariable(extraVarInd) + "]",
                    indent2 + CodeGeneratorSettings.INDENT,
                    extraVarInd + 1
            );
            out.println(indent2 + "}");
            out.println(indent + "}");
        } else {
            out.printf(
                    "%s%s.%s = ",
                    indent,
                    CodeGeneratorSettings.getUniqueVariable(),
                    fieldName
            );

            if (needsConversion(fieldType)) {
                out.printf(
                        "%s == null ? null : %s.getBin()",
                        fieldName,
                        fieldName
                );
            } else
                out.print(fieldName);

            out.println(";");
        }
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

        if (needsConversion(field.getType())) {
            //new Vert(
            out.printf("%snew %s(\n", indent, Vert.class.getCanonicalName());
            //    new ConstCell("fieldName"),
            out.printf(
                    "%s%snew %s(\"%s:\"),\n",
                    indent,
                    CodeGeneratorSettings.INDENT,
                    ConstantCell.class.getCanonicalName(),
                    title
            );
            //    new Horiz(
            out.printf("%s%snew %s(\n", indent, CodeGeneratorSettings.INDENT, Horiz.class.getCanonicalName());
            //        new ConstCell("    "),
            out.printf(
                    "%s%s%snew %s(\"%s\"),\n",
                    indent,
                    CodeGeneratorSettings.INDENT,
                    CodeGeneratorSettings.INDENT,
                    ConstantCell.class.getCanonicalName(),
                    CodeGeneratorSettings.INDENT
            );
            //        new FieldCell("fieldName")
            out.printf(
                    "%s%s%snew %s(\"%s\")\n",
                    indent,
                    CodeGeneratorSettings.INDENT,
                    CodeGeneratorSettings.INDENT,
                    field.getType().isArray() ?
                            VertArray.class.getCanonicalName() :
                            FieldCell.class.getCanonicalName(),
                    field.getName()
            );
            //close Horiz
            out.println(indent + CodeGeneratorSettings.INDENT + ")");
            //close Vert
            out.print(indent + ")");
        } else {
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
    }

    private String makeNewClassName(Class<?> className) {
        if (className.isArray())
            return makeNewClassName(className.getComponentType()) + "[]";
        else
            return className.getSimpleName() + postfix;
    }

    private void outputGetter(PrintStream out, Field field) {
        out.println();

        //get field annotations and add new annotations
        //BinInfo binInfo = field.getAnnotation(BinInfo.class);

        /*if (noModification.equals("null"))
            out.printf(
                    "%s@%s(allowNull=false)\n",
                    CodeGeneratorSettings.INDENT,
                    DSLBeanProperty.class.getCanonicalName()
            );*/

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
                "%sprivate %s %s",
                CodeGeneratorSettings.INDENT,
                type2string(field.getType()),
                field.getName()
        );

        BinInfo binInfo = field.getAnnotation(BinInfo.class);
        String defaultValue = binInfo == null ? BinInfo.NO_DEFAULT_VALUE : binInfo.defaultValue();

        if (!defaultValue.equals(BinInfo.NO_DEFAULT_VALUE)) {
            out.print(" = ");
            out.print(convertDefaultValue(defaultValue, field.getType()));
        }

        out.println(";");
    }

    //must not be called with BinInfo.NO_DEFAULT_VALUE

    private String convertDefaultValue(String binInfoDefaultValue, Class<?> fieldType) {
        boolean newInst = binInfoDefaultValue.equals(BinInfo.NEW_INSTANCE_DEFAULT_VALUE);

        //enum
        if (fieldType.isEnum())
            return type2string(fieldType) + "." + binInfoDefaultValue;

        //String
        if (fieldType == String.class) {
            if (newInst)
                return "\"\"";
            else
                return "\"" + binInfoDefaultValue + "\"";
        }

        //Object
        if (fieldType.isArray() && newInst)
            return "new " + type2string(fieldType) + "{}";

        //Any object
        if (!fieldType.isPrimitive() && newInst)
            return "new " + type2string(fieldType) + "()";

        //Any other
        return binInfoDefaultValue;
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
        return changeBinPackage(name);
    }

    private String changeBinPackage(String className) {
        if (className.startsWith(CodeGeneratorSettings.BINS_PACKAGE))
            return CodeGeneratorSettings.BINS_OUT_PACKAGE +
                    className.substring(1 + CodeGeneratorSettings.BINS_PACKAGE.length());
        return className;
    }

    private boolean needsConversion(Class<?> type) {
        return
                type.getCanonicalName().startsWith(CodeGeneratorSettings.BINS_PACKAGE)
                        &&
                        !type.isEnum();
    }
}