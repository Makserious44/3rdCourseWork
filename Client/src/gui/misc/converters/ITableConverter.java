package gui.misc.converters;

@FunctionalInterface
public interface ITableConverter<T, M> {
    T convert(M adapt);
}
