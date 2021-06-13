package com.carlgira.soda.model;

import java.util.Objects;

public class BookId {

    private Integer value;

    public BookId(){

    }

    public BookId(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookId bookId = (BookId) o;
        return getValue().equals(bookId.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
