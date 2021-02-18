package org.trafodion.orctool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class OrcConfig {

    private final String[] names = new String[]{
        "fileName", "type", "platfile", "orcfile", "override", "separateSizeMB", "threads"};
    private String fileName;
    private int type;
    private String platfile;
    private String orcfile;
    private boolean override;
    private int separateSizeMB;
    private int threads;

    public OrcConfig(InputStream configFile) throws IOException {
        Properties conf = new Properties();
        conf.load(configFile);
        this.fileName = conf.getProperty(names[0], "%1$s_%2$s");
        this.type = Integer.valueOf(conf.getProperty(names[1], "1"));
        this.platfile = conf.getProperty(names[2], "platfile");
        this.orcfile = conf.getProperty(names[3], "orcfile");
        this.override = Boolean.valueOf(conf.getProperty(names[4], "true"));
        this.separateSizeMB = Integer.valueOf(conf.getProperty(names[5], "64"));
        this.threads = Integer.valueOf(conf.getProperty(names[5], "10"));
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPlatfile() {
        return platfile;
    }

    public void setPlatfile(String platfile) {
        this.platfile = platfile;
    }

    public String getOrcfile() {
        return orcfile;
    }

    public void setOrcfile(String orcfile) {
        this.orcfile = orcfile;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public int getSeparateSizeMB() {
        return separateSizeMB;
    }

    public void setSeparateSizeMB(int separateSizeMB) {
        this.separateSizeMB = separateSizeMB;
    }
}
