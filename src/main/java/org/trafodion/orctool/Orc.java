package org.trafodion.orctool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
        Properties conf = new Properties();
        conf.load(configFile);
        configFile.close();

    }

    @SuppressWarnings("static-access")
    private static Options createOptions() {
        Options result = new Options();
        result.addOption("h", "help", false, "Print help message");
        result.addOption("f", "file", true, "Set a configuration property file");
        return result;
    }
}
