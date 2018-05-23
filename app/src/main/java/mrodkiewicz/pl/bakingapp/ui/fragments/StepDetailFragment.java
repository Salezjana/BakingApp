package mrodkiewicz.pl.bakingapp.ui.fragments;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.adapter.StepsRecycleViewAdapter;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.db.models.Step;
import mrodkiewicz.pl.bakingapp.helper.Config;
import timber.log.Timber;

import static com.google.android.exoplayer2.C.TIME_UNSET;

public class StepDetailFragment extends Fragment {
    @BindView(R.id.vp_step_detail)
    PlayerView vpStepDetail;
    @BindView(R.id.big_tv_step_detail)
    @javax.annotation.Nullable
    TextView bigTvStepDetail;
    @BindView(R.id.medium_1_tv_tv)
    @javax.annotation.Nullable
    TextView medium1TvTv;
    Unbinder unbinder;
    @BindView(R.id.iv_step_detail)
    ImageView ivStepDetail;
    private ArrayList<Step> stepArrayList;
    private StepsRecycleViewAdapter stepsRecycleViewAdapter;
    private int positonStep, position;
    private SimpleExoPlayer player;
    private long positionPlayer;


    public StepDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (stepArrayList == null) {
            stepArrayList = new ArrayList<Step>();
        }
        if (getArguments() != null) {
            Timber.d("StepDetailFragment R" + getArguments().getInt(Config.BUNDLE_KEY_POSITION));
            Timber.d("StepDetailFragment S" + getArguments().getInt(Config.BUNDLE_KEY_POSITION_STEP));
            positonStep = getArguments().getInt(Config.BUNDLE_KEY_POSITION_STEP);
            position = getArguments().getInt(Config.BUNDLE_KEY_POSITION);
            stepArrayList.addAll(getArguments().<Recipe>getParcelableArrayList(Config.BUNDLE_RECIPELIST).get(getArguments().getInt(Config.BUNDLE_KEY_POSITION)).getSteps());

        }

        positionPlayer = TIME_UNSET;
        if (savedInstanceState != null) {
            Timber.d("ten long to " + savedInstanceState.getLong(Config.STATE_KEY_POSITION_VP, TIME_UNSET));
            positionPlayer = savedInstanceState.getLong(Config.STATE_KEY_POSITION_VP, TIME_UNSET);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Timber.d("orientation portaitr");
        } else {
            if (medium1TvTv != null) {
                medium1TvTv.setVisibility(View.GONE);
            }
            if (bigTvStepDetail != null) {
                bigTvStepDetail.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragmnet_step_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void setupView() {
        bigTvStepDetail.setText(stepArrayList.get(positonStep).getShortDescription());
        medium1TvTv.setText(stepArrayList.get(positonStep).getDescription());
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        if (stepArrayList.get(positonStep).getVideoURL() == null || stepArrayList.get(positonStep).getVideoURL().isEmpty()) {
            if (stepArrayList.get(positonStep).getThumbnailURL() != null && stepArrayList.get(positonStep).getThumbnailURL() != " " && !stepArrayList.get(positonStep).getThumbnailURL().isEmpty()) {
                List valid = Arrays.asList("BMP", "IMG", "GIF", "PNG", "JPG", "JPEG", "TIFF");
                String extension = stepArrayList.get(positonStep).getThumbnailURL().substring(stepArrayList.get(positonStep).getThumbnailURL().lastIndexOf("."));
                if (valid.contains(extension)) {
                    Timber.d("ACIDYSSS " + stepArrayList.get(positonStep).getThumbnailURL());
                    ivStepDetail.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(stepArrayList.get(positonStep).getThumbnailURL()).into(ivStepDetail);
                }
            }
            vpStepDetail.setVisibility(View.GONE);
            medium1TvTv.setVisibility(View.VISIBLE);
            bigTvStepDetail.setVisibility(View.VISIBLE);
            Timber.d("getVideoURL empty");
        } else {
            Timber.d("getVideoURL " + stepArrayList.get(positonStep).getVideoURL());
            vpStepDetail.setPlayer(player);
            Uri uri = Uri.parse(stepArrayList.get(positonStep).getVideoURL());
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
            player.seekTo(positionPlayer);
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_fullscreen:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Config.STATE_KEY_POSITION_VP, player.getCurrentPosition());
    }
}
