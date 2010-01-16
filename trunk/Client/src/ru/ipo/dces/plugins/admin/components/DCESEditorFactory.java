/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 04.08.2009
 * Time: 17:16:23
 */
package ru.ipo.dces.plugins.admin.components;

import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.clientservercommunication.AvailablePluginsRequest;
import ru.ipo.dces.clientservercommunication.AvailablePluginsResponse;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.ResultsAccessPolicy;
import ru.ipo.dces.server.ServerFacade;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

public class DCESEditorFactory extends PropertyEditorRegistry {
  private static DCESEditorFactory ourInstance = new DCESEditorFactory();

  public static DCESEditorFactory getInstance() {
    return ourInstance;
  }

  private DCESEditorFactory() {
    this.registerEditor(ContestDescription.RegistrationType.class, RegistrationTypeEditor.class);
    this.registerEditor(ResultsAccessPolicy.AccessPermission.class, ResultsAccessPolicyEditor.class);
  }

  public static class RegistrationTypeEditor extends ComboBoxPropertyEditor {
    public RegistrationTypeEditor() {
      setAvailableValues(new Object[]{
              new Value(ContestDescription.RegistrationType.Self, "самостоятельно"),
              new Value(ContestDescription.RegistrationType.ByAdmins, "администратором")
      });
    }
  }

  public static class ResultsAccessPolicyEditor extends ComboBoxPropertyEditor {
    public ResultsAccessPolicyEditor() {
      setAvailableValues(new Object[]{
              new Value(ResultsAccessPolicy.AccessPermission.FullAccess, "полный доступ"),
              new Value(ResultsAccessPolicy.AccessPermission.OnlySelfResults, "только свои"),
              new Value(ResultsAccessPolicy.AccessPermission.NoAccess, "нет доступа")
      });
    }
  }

  public static class PluginAliasEditor extends ComboBoxPropertyEditor {
    private final AvailablePluginsRequest.PluginSide side;
    private final HashMap<Object, PluginValue> val2plval = new HashMap<Object, PluginValue>();

    public static class PluginValue {
      private String alias;
      private String description;

      public PluginValue(String alias, String description) {
        this.alias = alias;
        this.description = description;
      }

      public String getAlias() {
        return alias;
      }

      public String getDescription() {
        return description;
      }

      @Override
      public String toString() {
        return alias;
      }
    }

    public PluginAliasEditor(AvailablePluginsRequest.PluginSide side) {
      this.side = side;

      //TODO popup menu may not work with nonstandard L&F (!!! this is a probable bug)
      final JComboBox combo = (JComboBox) editor;

      combo.addPopupMenuListener(new PopupMenuListener() {
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
          refreshValuesList();
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }

        public void popupMenuCanceled(PopupMenuEvent e) {
        }
      });
      final ListCellRenderer renderer = combo.getRenderer();

      combo.setRenderer(new ListCellRenderer() {        
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
          //TODO the cast may not work sometimes
          final JComponent r = (JComponent) renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
          final PluginValue pluginValue = val2plval.get(value);
          //TODO write description in some window instead of sout
          //System.out.println(pluginValue.getDescription());

          return r;
        }
      });
    }

    private void refreshValuesList() {
      try {
        //get values from server
        ServerFacade server = Controller.getContestConnection().getServer();
        AvailablePluginsRequest req = new AvailablePluginsRequest();
        req.sessionID = Controller.getContestConnection().getSessionID();
        req.pluginSide = this.side;
        AvailablePluginsResponse resp = server.doRequest(req);

        Value[] values = new Value[resp.aliases.length];
        val2plval.clear();

        for (int i = 0; i < resp.aliases.length; i++) {
          final PluginValue pluginValue = new PluginValue(resp.aliases[i], resp.descriptions[i]);
          values[i] = new Value(resp.aliases[i], pluginValue);
          val2plval.put(values[i], pluginValue);
        }

        setAvailableValues(values);
      } catch (Exception e) {
        setFailedValues(e.getMessage());
      }
    }

    private void setFailedValues(String msg) {
      setAvailableValues(new Object[]{
              new Value(null, new PluginValue("... не удалось получить список плагинов", msg)),
      });
    }
  }
}