package org.trafodion.orctool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

public class Orc {

    public static void main(String[] args) throws ParseException, IOException {
        Options orcOpts = createOptions();
        CommandLine orcCli = new DefaultParser()
            .parse(orcOpts, args, true);
        InputStream configFile;
        if (orcCli.hasOption('h')) {
            HelpFormatter help = new HelpFormatter();
            help.printHelp("Usages: ", orcOpts);
            return;
        } else if (orcCli.hasOption('f')) {
            configFile = new FileInputStream(orcCli.getOptionValue('f'));
        } else {
            configFile = Orc.class.getResourceAsStream("/traforc.properties");
        }
        OrcConfig orcConfig = new OrcConfig(configFile);
        configFile.close();

        ExecutorService pool = Executors.newFixedThreadPool(orcConfig.getThreads());
        List<File> srcfileList = null;
        File tgtfile = null;
        switch (orcConfig.getType()) {
            case 1:
                srcfileList = getFileList(orcConfig.getPlatfile());
                tgtfile = new File(orcConfig.getOrcfile());
                break;
            case 2:
                srcfileList = getFileList(orcConfig.getOrcfile());
                tgtfile = new File(orcConfig.getPlatfile());
                break;
        }
        submitWorker(orcConfig, pool, srcfileList, tgtfile);
        pool.shutdown();
    }

    private static void submitWorker(OrcConfig orcConfig,
        ExecutorService pool,
        List<File> srcList, File tgtFile) {
        boolean isFile = tgtFile.isFile();
        File tmpFile;
        for (int i = 0; i < srcList.size(); i++) {
            if (isFile) {
                tmpFile = tgtFile;
            } else {
                tmpFile = new File(
                    tgtFile.getAbsolutePath() + File.pathSeparator + String
                        .format(orcConfig.getFileName(), i, orcConfig.getPostfix()));
            }

            pool.submit(new Worker(orcConfig, srcList.get(i), tmpFile));

        }
    }

    private static List<File> getFileList(String filePath) {
        File file = new File(filePath);
        List<File> fileList;
        if (file.isFile()) {
            fileList = new ArrayList<File>();
            fileList.add(file);
        } else {
            fileList = (List<File>) FileUtils
                .listFiles(file, new String[]{}, true);
        }
        return fileList;
    }

    @SuppressWarnings("static-access")
    private static Options createOptions() {
        Options result = new Options();
        result.addOption("h", "help", false, "Print help message");
        result.addOption("f", "file", true, "Set a configuration property file");
        return result;
    }
}
