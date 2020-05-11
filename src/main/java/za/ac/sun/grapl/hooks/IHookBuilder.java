package za.ac.sun.grapl.hooks;

public interface IHookBuilder {

    IHookBuilder useExistingGraph(final String graphDir);

    IHook build();

}
