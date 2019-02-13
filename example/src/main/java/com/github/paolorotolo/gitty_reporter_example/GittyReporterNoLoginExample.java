package com.github.paolorotolo.gitty_reporter_example;

import android.os.Bundle;

import com.github.paolorotolo.gitty_reporter.GittyReporter;

public class GittyReporterNoLoginExample extends GittyReporter {
    @Override
    public void init(Bundle savedInstanceState) {
        //noinspection HardCodedStringLiteral
        setTargetRepository("paolorotolo", "GittyReporter");
        enableUserGitHubLogin(false);
        //noinspection HardCodedStringLiteral
        setGuestOAuth2Token("28f479f73db97d912611b27579aad7a76ad2baf5");
        //noinspection HardCodedStringLiteral
        setExtraInfo("Example string");
    }
}