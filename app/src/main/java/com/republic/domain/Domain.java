package com.republic.domain;

import com.republic.entities.Corruption;

/**
 * Created by aowusu on 7/20/2015.
 */
public interface Domain {

    <T> void initialize( T context);
    void saveCorruption(Corruption corruption);
}
