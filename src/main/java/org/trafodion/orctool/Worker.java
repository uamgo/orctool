package org.trafodion.orctool;

import java.io.File;

public class Worker implements Runnable {

    private OrcConfig config;
    private Writer writer;
    private Reader reader;

    public Worker(OrcConfig config, File srcFile, File tgrFile) {
        this.config = config;
        switch (config.getType()) {
            case 1:
                this.reader = new OrcReader(srcFile);
                this.writer = new ConsoleWriter();
                break;
            case 2:
                this.reader = new CsvReader(srcFile);
                this.writer = new OrcWriter(tgrFile);
                break;
            case 3:
                this.reader = new OrcReader(srcFile);
                this.writer = new CsvWriter(tgrFile);
                break;
            default:
                this.reader = new OrcReader(srcFile);
                this.writer = new CsvWriter(tgrFile);
        }
    }

    public void run() {
        int n = 0;
        while (this.reader.next()) {

            this.writer.addBatch();
            if (++n >= this.config.getBatchSize()) {
                this.writer.executeBatch();
                n = 0;
            }
        }
    }
}
