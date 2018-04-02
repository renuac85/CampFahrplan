package nerd.tuxmobil.fahrplan.congress.about;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nerd.tuxmobil.fahrplan.congress.BuildConfig;
import nerd.tuxmobil.fahrplan.congress.MyApp;
import nerd.tuxmobil.fahrplan.congress.R;

public class AboutDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_dialog, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = view.findViewById(R.id.eventVersion);
        text.setText(getString(R.string.fahrplan) + " " + MyApp.version);
        text = view.findViewById(R.id.eventTitle);
        text.setText("Wie wir arbeiten wollen. Das #tazlab zur Zukunft der Arbeit");
        text = view.findViewById(R.id.eventSubtitle);
        String subtitle = MyApp.subtitle;
        if (TextUtils.isEmpty(subtitle)) {
            subtitle = getString(R.string.app_hardcoded_subtitle);
        }
        text.setText(subtitle);
        text = view.findViewById(R.id.appVersion);
        String appVersionText = getString(R.string.appVersion, BuildConfig.VERSION_NAME);
        text.setText(appVersionText);

        Context context = getContext();

        View appDisclaimer = view.findViewById(R.id.app_disclaimer);
        //noinspection ConstantConditions
        appDisclaimer.setVisibility(BuildConfig.SHOW_APP_DISCLAIMER ? View.VISIBLE : View.GONE);

        TextView logo_copyright = view.findViewById(R.id.copyright_logo);
        logo_copyright.setText(Html.fromHtml(getString(R.string.copyright_logo)));
        logo_copyright.setLinkTextColor(ContextCompat.getColor(context, R.color.text_link_color_dark));
        logo_copyright.setMovementMethod(LinkMovementMethod.getInstance());

        TextView conferenceUrl = view.findViewById(R.id.conference_url);
        conferenceUrl.setText(Html.fromHtml(getString(R.string.conference_url)));
        conferenceUrl.setMovementMethod(LinkMovementMethod.getInstance());
        conferenceUrl.setLinkTextColor(ContextCompat.getColor(context, R.color.text_link_color_dark));

        TextView sourceCode = view.findViewById(R.id.source_code);
        sourceCode.setText(Html.fromHtml(getString(R.string.source_code)));
        sourceCode.setMovementMethod(LinkMovementMethod.getInstance());
        sourceCode.setLinkTextColor(ContextCompat.getColor(context, R.color.text_link_color_dark));

        TextView issues = view.findViewById(R.id.issues);
        issues.setText(Html.fromHtml(getString(R.string.issues)));
        issues.setMovementMethod(LinkMovementMethod.getInstance());
        issues.setLinkTextColor(ContextCompat.getColor(context, R.color.text_link_color_dark));

        TextView googlePlayStore = view.findViewById(R.id.google_play_store);
        googlePlayStore.setText(Html.fromHtml(getString(R.string.google_play_store)));
        googlePlayStore.setMovementMethod(LinkMovementMethod.getInstance());
        googlePlayStore.setLinkTextColor(ContextCompat.getColor(context, R.color.text_link_color_dark));

        // Build information

        TextView buildTimeTextView = view.findViewById(R.id.build_time);
        String buildTimeText = getString(R.string.build_info_time, BuildConfig.BUILD_TIME);
        buildTimeTextView.setText(buildTimeText);

        TextView versionCodeTextView = view.findViewById(R.id.build_version_code);
        String versionCodeText = getString(R.string.build_info_version_code, "" + BuildConfig.VERSION_CODE);
        versionCodeTextView.setText(versionCodeText);

        TextView buildHashTextView = view.findViewById(R.id.build_hash);
        String buildHashText = getString(R.string.build_info_hash, BuildConfig.GIT_SHA);
        buildHashTextView.setText(buildHashText);
    }
}
