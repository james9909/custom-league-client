package com.hawolt.service;

public interface FileService<T> {
    void writeFile(T content);

    T readFile();

    void deleteFile();
}
