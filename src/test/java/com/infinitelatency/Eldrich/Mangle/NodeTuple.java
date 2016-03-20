package com.infinitelatency.Eldrich.Mangle;


import org.jetbrains.annotations.NotNull;

class NodeTuple {
    final String contents;
    final Class<?> type;

    NodeTuple(@NotNull String contents, @NotNull Class<?> type){
        this.contents = contents;
        this.type = type;
    }
}