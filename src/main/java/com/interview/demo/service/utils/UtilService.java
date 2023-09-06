package com.interview.demo.service.utils;

import com.interview.demo.model.TokenPair;
import io.vavr.control.Option;

public interface UtilService {



    Option<TokenPair> generateTokenPair(String id);
}
