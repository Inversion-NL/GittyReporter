package com.github.paolorotolo.gitty_reporter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import java.io.IOException;

class reportIssue extends AsyncTask<String, Integer, String> {

    private final Context mContext;
    private final GittyReporter mActivity;
    private ProgressDialog progress;

    reportIssue(Context context, GittyReporter activity){
        mContext = context;
        mActivity = activity;
    }

    // Runs in UI before background thread is called
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(mContext, mContext.getResources().getString(R.string.dialog_progress_pleaseWait_title),
                mContext.getString(R.string.dialog_progress_pleaseWait_message), true);
    }

    // This is run in a background thread
    @Override
    protected String doInBackground(String... params) {
        // get the string from params, which is an array
        String user = params[0];
        String password = params[1];
        String bugTitle = params[2];
        String bugDescription = params[3];
        String deviceInfo = params[4];
        String targetUser = params[5];
        String targetRepository = params[6];
        String extraInfo = params[7];
        String gitToken = params[8];

        IssueService service;

        if (user.equals("")) {
            service = new IssueService(new GitHubClient().setOAuth2Token(gitToken));
        } else {
            service = new IssueService(new GitHubClient().setCredentials(user, password));
        }

        Issue issue = new Issue().setTitle(bugTitle).setBody(bugDescription + "\n\n" + deviceInfo + mContext.getString(R.string.issue_extraInfo) + extraInfo);
        try {
            service.createIssue(targetUser, targetRepository, issue);
            //noinspection HardCodedStringLiteral
            return "ok";
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    // This is called from background thread but runs in UI
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    // This runs in UI when background thread finishes
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        switch (result) {
            //noinspection HardCodedStringLiteral
            case "ok":
                progress.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mActivity.showDoneAnimation();
                } else {
                    ((Activity) mContext).finish();
                }
                break;
            //noinspection HardCodedStringLiteral
            case "org.eclipse.egit.github.core.client.RequestException: Bad credentials (401)":
                progress.dismiss();
                new AlertDialog.Builder(mContext)
                        .setTitle(mContext.getResources().getString(R.string.report_unableToSendReport_title))
                        .setMessage(mContext.getResources().getString(R.string.report_unableToSendReport_messageBadCredentials))
                        .setPositiveButton(mContext.getResources().getString(R.string.report_unableToSendReport_button_tryAgain), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.drawable.gittyreporter_ic_mood_bad_black_24dp)
                        .show();
                break;
            default:
                progress.dismiss();
                new AlertDialog.Builder(mContext)
                        .setTitle(mContext.getResources().getString(R.string.report_unableToSendReport_title))
                        .setMessage(R.string.report_unableToSendReport_messageUnexpectedError)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ((Activity) mContext).finish();
                            }
                        })
                        .setIcon(R.drawable.gittyreporter_ic_mood_bad_black_24dp)
                        .show();
                break;
        }
    }
}