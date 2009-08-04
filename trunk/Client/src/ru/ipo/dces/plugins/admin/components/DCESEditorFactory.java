/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 04.08.2009
 * Time: 17:16:23
 */
package ru.ipo.dces.plugins.admin.components;

import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import ru.ipo.dces.plugins.admin.beans.DateBean;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.ResultsAccessPolicy;

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

}
