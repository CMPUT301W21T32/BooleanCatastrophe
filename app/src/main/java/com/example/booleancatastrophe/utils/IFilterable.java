package com.example.booleancatastrophe.utils;

/**
 * Interface to represent that the contents of an object can be filtered.
 * It is up to the implementor to decide what this means.
 */
public interface IFilterable {
    public void filter(String query);
}
