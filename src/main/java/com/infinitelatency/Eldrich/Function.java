package com.infinitelatency.Eldrich;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

class Function {
    private final String name;
    private final List<Class<?>> argTypes;
    private Function(@NotNull String name, @NotNull Class<?>... argTypes){
        this.name = name;
        this.argTypes = Arrays.asList(argTypes);
    }

    public static Function createFunction(@NotNull String name, @NotNull Class<?>... argTypes){
        return new Function(name, argTypes);
    }

    @Override
    final public boolean equals(Object o) {
        if (o instanceof  Function){
            boolean name = this.name == null ? ((Function) o).name == null : this.name.equals(((Function) o).name);
            boolean argTypes = this.argTypes == null ? ((Function) o).argTypes == null : this.argTypes.equals(((Function) o).argTypes);
            return name && argTypes;
        }
        return false;
    }

    @Override
    final public int hashCode(){
        int nameHash = name == null ? 0 : name.hashCode();
        int argHash = argTypes == null ? 0 : argTypes.hashCode();

        return nameHash ^ argHash;
    }
}