package com.github.paolorotolo.gitty_reporter_example;

import android.os.Bundle;

import com.github.paolorotolo.gitty_reporter.GittyReporter;

public class GittyReporterExample extends GittyReporter {
    @Override
    public void init(Bundle savedInstanceState) {
        //noinspection HardCodedStringLiteral
        setTargetRepository("Inversion-NL", "GittyReporter");
        //noinspection HardCodedStringLiteral
        setGuestOAuth2Token("a6b7c9af61e91fba5c8dd72987d0f919f54818bb");
    }
}