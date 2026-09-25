package gui.misc;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;

import java.util.List;

public class TableCellValueFactorySetter<T> {
    private final TableView<T> table;

    public TableCellValueFactorySetter(TableView<T> table) {
        this.table = table;
    }

    public void setValueFactories(List<FactoryParam> factoryParams) throws TableValueFactorySetException {
        for (int i = 0; i < table.getColumns().size(); i++) {
            Class<?> type = factoryParams.get(i).getType();
            table.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(factoryParams.get(i).getField()));
        }
    }
}
