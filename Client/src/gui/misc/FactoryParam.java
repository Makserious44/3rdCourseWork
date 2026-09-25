package gui.misc;

import lombok.Getter;

public class FactoryParam {
    @Getter
    private Class<?> type;
    @Getter
    private String field;

    public FactoryParam(Class<?> type, String field) {
        this.type = type;
        this.field = field;
    }
}
