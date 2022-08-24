package dev.junpring.scribble.interfaces;

public interface IResult <T extends Enum<?>> {
    T getResult();

    void setResult(T t);
}