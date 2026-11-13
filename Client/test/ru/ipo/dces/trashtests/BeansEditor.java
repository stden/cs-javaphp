package ru.ipo.dces.trashtests;

import ru.ipo.dces.serverbeans.BeansRegistrator;
import ru.ipo.dces.serverbeans.ContestDescriptionBean;
import ru.ipo.structurededitor.Defaults;
import ru.ipo.structurededitor.StructuredEditor;
import ru.ipo.structurededitor.model.DSLBeansRegistry;
import ru.ipo.structurededitor.view.EditorRenderer;
import ru.ipo.structurededitor.view.StructuredEditorModel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 28.10.2010
 * Time: 1:12:53
 */
public class BeansEditor {

    public static void main(String[] args) {
        Defaults.registerDefaultEditors();
        BeansRegistrator.register();

        StructuredEditorModel model = new StructuredEditorModel();
        //CreateContestRequestBean ccrb = new CreateContestRequestBean();
        ContestDescriptionBean ccrb = new ContestDescriptionBean();
        model.setRootElement(new EditorRenderer(model, ccrb).getRenderResult());
        DSLBeansRegistry.getInstance().registerBean(ContestDescriptionBean.class);

        JFrame f = new JFrame("Редактирование ContestDescription");
        BorderLayout br = new BorderLayout();
        f.setLayout(br);
        StructuredEditor structuredEditor = new StructuredEditor(model, ccrb);
        JScrollPane structuredEditorScrPane = new JScrollPane(structuredEditor);
        f.add(structuredEditorScrPane, BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(640, 480);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

}