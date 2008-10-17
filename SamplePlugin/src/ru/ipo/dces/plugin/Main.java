package ru.ipo.dces.plugin;

import ru.ipo.dces.pluginapi.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;

public class Main extends Plugin {

  private static final long serialVersionUID = -8533952562620075503L;

  public Main(Client client) {
    super(client);

    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC,
        FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("49dlu:grow(2.0)"),
        FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC },
        new RowSpec[] { FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default") }));

    // TODO Auto-generated constructor stub
  }

}
