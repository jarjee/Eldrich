package com.infinitelatency.Eldrich;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

class Function {
    protected final String name;
    protected final List<Class<?>> argTypes;
    private Function(@NotNull String name, @NotNull Class<?>... argTypes){
        this.name = name;
        this.argTypes = Arrays.asList(argTypes);
    }

    public static Function createFunction(@NotNull String name, @NotNull Class<?>... argTypes){
        return new Function(name, argTypes);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Function && this.name.equals(((Function) o).name) && this.argTypes.equals(((Function) o).argTypes);
    }

    @Override
    public int hashCode(){
        return name.hashCode() ^ argTypes.hashCode();
    }
}