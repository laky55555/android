package hr.math.android.signme;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;


public class MainIntroActivity extends IntroActivity {

    public static final String EXTRA_FULLSCREEN = "com.heinrichreimersoftware.materialintro.demo.EXTRA_FULLSCREEN";
    public static final String EXTRA_SCROLLABLE = "com.heinrichreimersoftware.materialintro.demo.EXTRA_SCROLLABLE";
    public static final String EXTRA_CUSTOM_FRAGMENTS = "com.heinrichreimersoftware.materialintro.demo.EXTRA_CUSTOM_FRAGMENTS";
    public static final String EXTRA_PERMISSIONS = "com.heinrichreimersoftware.materialintro.demo.EXTRA_PERMISSIONS";
    public static final String EXTRA_SHOW_BACK = "com.heinrichreimersoftware.materialintro.demo.EXTRA_SHOW_BACK";
    public static final String EXTRA_SHOW_NEXT = "com.heinrichreimersoftware.materialintro.demo.EXTRA_SHOW_NEXT";
    public static final String EXTRA_SKIP_ENABLED = "com.heinrichreimersoftware.materialintro.demo.EXTRA_SKIP_ENABLED";
    public static final String EXTRA_FINISH_ENABLED = "com.heinrichreimersoftware.materialintro.demo.EXTRA_FINISH_ENABLED";
    public static final String EXTRA_GET_STARTED_ENABLED = "com.heinrichreimersoftware.materialintro.demo.EXTRA_GET_STARTED_ENABLED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setButtonBackFunction(BUTTON_BACK_FUNCTION_BACK);
        setButtonNextFunction(BUTTON_NEXT_FUNCTION_NEXT_FINISH);
        setButtonBackVisible(true);
        setButtonNextVisible(true);
        setButtonCtaVisible(true);
        // Button color.
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);


        boolean scrollable = true;
        setFullscreen(true);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.welcome_title)
                .description(R.string.welcome_description)
                .image(R.drawable.mutiny_logo)
                .background(R.color.color_material_metaphor)
                .backgroundDark(R.color.color_dark_material_metaphor)
                .scrollable(scrollable)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.about_title)
                .description(R.string.about_description)
                .image(R.drawable.art_material_metaphor)
                .background(R.color.color_material_metaphor)
                .backgroundDark(R.color.color_dark_material_metaphor)
                .scrollable(scrollable)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.usage_title_1)
                .description(R.string.usage_description_1)
                .image(R.drawable.art_material_bold)
                .background(R.color.color_material_metaphor)
                .backgroundDark(R.color.color_dark_material_metaphor)
                .scrollable(scrollable)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.usage_title_2)
                .description(R.string.usage_description_2)
                .image(R.drawable.art_material_motion)
                .background(R.color.color_material_metaphor)
                .backgroundDark(R.color.color_dark_material_metaphor)
                .scrollable(scrollable)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.airplane_mode_title)
                .description(R.string.airplane_mode_description)
                .image(R.drawable.mutiny_logo)
                .background(R.color.color_material_metaphor)
                .backgroundDark(R.color.color_dark_material_metaphor)
                .scrollable(scrollable)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.home_button_title)
                .description(R.string.home_button_description)
                .image(R.drawable.mutiny_logo)
                .background(R.color.color_material_metaphor)
                .backgroundDark(R.color.color_dark_material_metaphor)
                .scrollable(scrollable)
                .build());


        addSlide(new SimpleSlide.Builder()
                .title(R.string.statistic_title)
                .description(R.string.statistic_description)
                .image(R.drawable.mutiny_logo)
                .background(R.color.color_material_metaphor)
                .backgroundDark(R.color.color_dark_material_metaphor)
                .scrollable(scrollable)
                .build());

        addSlide(new SimpleSlide.Builder()
                //.title(R.string.more_title)
                .titleHtml(getString(R.string.web_page))
                .description(R.string.more_description)
                .image(R.drawable.mutiny_logo)
                .background(R.color.color_material_metaphor)
                .backgroundDark(R.color.color_dark_material_metaphor)
                .scrollable(scrollable)
                .build());


        final Slide permissionsSlide = new SimpleSlide.Builder()
                    .title(R.string.title_permissions)
                    .description(R.string.description_permissions)
                    .background(R.color.color_permissions)
                    .backgroundDark(R.color.color_dark_permissions)
                    .scrollable(scrollable)
                    .permissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE})
                    .build();
        addSlide(permissionsSlide);

        final Slide loginSlide = new FragmentSlide.Builder()
                    .background(R.color.color_custom_fragment_1)
                    .backgroundDark(R.color.color_dark_custom_fragment_1)
                    .fragment(PasswordEmailFragment.newInstance(-3, "new"))
                    .build();
        addSlide(loginSlide);


        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {
                View contentView = findViewById(android.R.id.content);
                if (contentView != null) {
                    Slide slide = getSlide(position);

                    if (slide == permissionsSlide) {
                        Snackbar.make(contentView, R.string.label_grant_permissions, Snackbar.LENGTH_LONG).show();
                    } else if (slide == loginSlide) {
                        Snackbar.make(contentView, R.string.label_fill_out_form, Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

}