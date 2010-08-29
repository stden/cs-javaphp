package ru.ipo.dces.plugins.admin.components;

import com.l2fprod.common.propertysheet.PropertyRendererRegistry;
import com.l2fprod.common.swing.renderer.DefaultCellRenderer;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.ResultsAccessPolicy;
import ru.ipo.dces.plugins.admin.beans.ZipBean;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 15.08.2009
 * Time: 21:08:03
 */
public class DCESRendererFactory extends PropertyRendererRegistry {

  private static DCESRendererFactory ourInstance = new DCESRendererFactory();

  public static DCESRendererFactory getInstance() {
    return ourInstance;
  }

  private DCESRendererFactory() {
    this.registerRenderer(ContestDescription.RegistrationType.class, new RegistrationTypeRenderer());
    this.registerRenderer(ResultsAccessPolicy.AccessPermission.class, new ResultsAccessPolicyRenderer());
    this.registerRenderer(ZipBean.class, new ZipBeanRenderer());
  }

  public static class RegistrationTypeRenderer extends DefaultCellRenderer {
    @Override
    public void setValue(Object value) {
      if (value != null)
        switch ((ContestDescription.RegistrationType) value) {
          case ByAdmins:
            value = "Администратором";
            break;
          case Self:
            value = "Самостоятельно";
            break;
        }

      super.setValue(value);
    }
  }

  public static class ResultsAccessPolicyRenderer extends DefaultCellRenderer {
    @Override
    public void setValue(Object value) {
      if (value != null)
        switch ((ResultsAccessPolicy.AccessPermission) value) {
          case FullAccess:
            value = "Полный доступ";
            break;
          case NoAccess:
            value = "Без доступа";
            break;
          case OnlySelfResults:
            value = "Только собственные";
            break;
        }

      super.setValue(value);
    }
  }

  public static class ZipBeanRenderer extends DefaultCellRenderer {
    @Override
    public void setValue(Object value) {
      if (value == null) {
        super.setValue(null);
        return;
      }

      File f = ((ZipBean) value).getFile();

      if (f == null)
        super.setValue("Без изменений");
      else
        super.setValue(f.getAbsolutePath());
    }
  }

  public static class DirectoryOnlyRenderer extends DefaultCellRenderer {
    @Override
    public void setValue(Object value) {
      File f = (File) value;

      if (f == null)
        super.setValue(null);
      else if (f.isDirectory())
        super.setValue(f.getPath());
      else
        super.setValue("");
    }
  }

  public static class FileOnlyRenderer extends DefaultCellRenderer {
    @Override
    public void setValue(Object value) {
      File f = (File) value;

      if (f == null)
        super.setValue(null);
      else if (f.isFile())
        super.setValue(f.getPath());
      else
        super.setValue("");
    }
  }

}
