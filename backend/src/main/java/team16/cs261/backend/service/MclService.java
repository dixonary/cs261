package team16.cs261.backend.service;

import team16.cs261.backend.model.MclOutput;

import java.util.concurrent.Future;

/**
 * Created by martin on 11/03/15.
 */


public interface MclService {
    Future<MclOutput> run(long tick, String mclInput);
}
