package team16.cs261.backend.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import team16.cs261.backend.model.MclOutput;
import team16.cs261.backend.service.MclService;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by martin on 22/01/15.
 */

// time,buyer,seller,price,size,currency,symbol,sector,bid,ask
// 2015-02-14 08:43:55.480228,w.hastings@bridgewater.com,a.clare@sorrel.com,925.50,27714,GBX,ARM.L,Technology,932.19,933.05

@Service
public class MclServiceImpl implements MclService {

    // 2.5c / 3i

    @Async
    @Override
    public Future<MclOutput> run(long tick, String mclInput) {

        double i = 3.5D;
        double c = 2.5D;

        try {
            Path mclWd = Paths.get(System.getProperty("user.home"), "fraud", "mcl");

            Files.createDirectories(mclWd);

            String fIn = String.valueOf(tick);
            String fOut = "out." + tick;

            Path path = mclWd.resolve(fIn);
            Path out = mclWd.resolve(fOut);
            System.out.println("p: " + path.toAbsolutePath());


            Files.write(path, mclInput.getBytes());

            Process p = Runtime.getRuntime().exec(new String[]{
                    "mcl",
                    fIn,
                    "--abc",
                    "-o", fOut,
                    "-I", String.valueOf(i),
                    "--sum-loops",
                    "-c", String.valueOf(c)
            }, new String[0], mclWd.toFile());

            p.waitFor();

            List<String> lines = Files.readAllLines(out, Charset.defaultCharset());
            MclOutput output = new MclOutput(lines);

            return new AsyncResult<>(output);
        } catch (NoSuchFileException e) {
            System.out.println("Failed to cluster tick: " + tick);
            e.printStackTrace();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return new AsyncResult<>(null);
    }

}
