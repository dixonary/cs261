package team16.cs261.backend.config;

import org.apache.commons.cli.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by martin on 22/02/15.
 */

//@Component
public class Options {

    public String tradesFile;
    public String commsFile;

    //@Autowired
    public Options(String[] cliArgs) {

        System.out.println("Args: " + Arrays.toString(cliArgs));

        CommandLineParser parser = new GnuParser();
        CommandLine line;

        try {
            line = parser.parse(opts, cliArgs);

            tradesFile = line.getOptionValue("trades");
            commsFile = line.getOptionValue("comms");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //Path tradesPath = Paths.get(line.getOptionValue("trades-file"));
        //Path commsPath = Paths.get(line.getOptionValue("comms-file"));


    }

    public void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("backend", opts);
    }

    private static final org.apache.commons.cli.Options opts;

    static {
        opts = new org.apache.commons.cli.Options();

        opts.addOption(OptionBuilder.withLongOpt("trades")
                .withDescription("trade data path")
                .hasArg()
                .withArgName("FILE")
                .create("t"));

        opts.addOption(OptionBuilder.withLongOpt("comms")
                .withDescription("comms data path")
                .hasArg()
                .withArgName("FILE")
                .create("c"));
    }
}
