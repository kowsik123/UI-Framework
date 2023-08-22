package callback;

public interface UICallback<ParaType, ReturnType> {
    ReturnType callback(ParaType data);
}
