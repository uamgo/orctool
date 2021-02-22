package org.trafodion.orctool;

public interface Writer {

    void addBatch();

    void executeBatch();
}
