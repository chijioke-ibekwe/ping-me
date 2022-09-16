package com.project.pingme.service;

public interface HashService {

    String getHashedValue(String data, String salt);
}
