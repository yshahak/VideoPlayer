package com.ysapps.videoplayer.fragments;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.startapp.android.publish.StartAppAd;
import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.activities.FeedbackActivity;
import com.ysapps.videoplayer.activities.MainActivity;

/**
 * Created by yshahak on 08/09/2016.
 */

public class FragmentMenu extends Fragment implements View.OnClickListener {

    String menuHelp = "<b><font color='red'>How to download</font></b><br/>"
            + "<font color='red'>Show Help</font>";

    String menuShare = "<b>Share this App</b><br/>Having fun with friends";
    String menuRate = "<b>Rate / Review</b><br/>Helping the Developer";
    String menuReport = "<b>Reports</b><br/>Suggestions / Bug Reports";


    public static FragmentMenu newInstance(int page) {
        return new FragmentMenu();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        TextView textViewHelp = ((TextView) root.findViewById(R.id.menu_content_help));
        textViewHelp.setText(Html.fromHtml(menuHelp));
        textViewHelp.setOnClickListener(this);
        TextView textViewShare = ((TextView) root.findViewById(R.id.menu_content_share));
        textViewShare.setText(Html.fromHtml(menuShare));
        textViewShare.setOnClickListener(this);
        TextView textViewRate = ((TextView) root.findViewById(R.id.menu_content_rate));
        textViewRate.setText(Html.fromHtml(menuRate));
        textViewRate.setOnClickListener(this);
        TextView textViewReport = ((TextView) root.findViewById(R.id.menu_content_report));
        textViewReport.setText(Html.fromHtml(menuReport));
        textViewReport.setOnClickListener(this);
        TextView delete = (TextView) root.findViewById(R.id.text_delete);
        delete.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_delete:
                ((MainActivity) getActivity()).viewPager.setCurrentItem(2);
                break;
            case R.id.menu_content_share:
                shareApp();
                break;
            case R.id.menu_content_rate:
                rateApp();
                break;
            case R.id.menu_content_help:
                showHelpDiaolog();
                break;
            case R.id.menu_content_report:
                openReportActivity();
        }
    }

    private void openReportActivity() {
        startActivity(new Intent(getContext(), FeedbackActivity.class));
    }

    private void showHelpDiaolog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Help");
        builder.setMessage(getString(R.string.help));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StartAppAd.showAd(getActivity());
            }
        });
        builder.show();
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getContext().getPackageName());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share " + getString(R.string.app_name) + " with friends!"));

    }

    private void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("http://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getContext().getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
}
