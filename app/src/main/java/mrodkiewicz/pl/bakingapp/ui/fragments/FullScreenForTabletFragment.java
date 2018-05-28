package mrodkiewicz.pl.bakingapp.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mrodkiewicz.pl.bakingapp.R;
import mrodkiewicz.pl.bakingapp.adapter.StepsRecycleViewAdapter;
import mrodkiewicz.pl.bakingapp.db.models.Recipe;
import mrodkiewicz.pl.bakingapp.db.models.Step;
import mrodkiewicz.pl.bakingapp.helper.Config;
import mrodkiewicz.pl.bakingapp.ui.MainActivity;
import timber.log.Timber;


public class FullScreenForTabletFragment extends Fragment {
    @BindView(R.id.vp_step_detail)
    PlayerView vpStepDetail;
    Unbinder unbinder;
    private ArrayList<Step> stepArrayList;
    private StepsRecycleViewAdapter stepsRecycleViewAdapter;
    private int positonStep, position;
    private SimpleExoPlayer player;
    private long positionPlayer;
    private SharedPreferences preferences;
    private boolean isPlaying;

    public FullScreenForTabletFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (stepArrayList == null) {
            stepArrayList = new ArrayList<Step>();
        }
        if (getArguments() != null) {
            Timber.d("FullScreenForTabletFragment R" + getArguments().getInt(Config.BUNDLE_KEY_POSITION));
            Timber.d("FullScreenForTabletFragment S" + getArguments().getInt(Config.BUNDLE_KEY_POSITION_STEP));
            positonStep = getArguments().getInt(Config.BUNDLE_KEY_POSITION_STEP);
            position = getArguments().getInt(Config.BUNDLE_KEY_POSITION);
            stepArrayList.addAll(getArguments().<Recipe>getParcelableArrayList(Config.BUNDLE_RECIPELIST).get(getArguments().getInt(Config.BUNDLE_KEY_POSITION)).getSteps());

        } else {
            Timber.d("FullScreenForTabletFragment getArgumrnts == null");

        }


        preferences = getActivity().getSharedPreferences(Config.PREFERENCES_KEY, Context.MODE_PRIVATE);
        positionPlayer = preferences.getLong(Config.STATE_KEY_POSITION_VP, 0);
        isPlaying = preferences.getBoolean(Config.STATE_KEY_POSITION_VP_IS_PLAYING, false);

        if (savedInstanceState != null) {
            positionPlayer = savedInstanceState.getLong("position", 0);
            isPlaying = savedInstanceState.getBoolean("state", false);
            Timber.d("To nie null XDDD " + positionPlayer + isPlaying);
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragmnet_step_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void setupView() {
        initPlayer();
        setHasOptionsMenu(true);


    }

    private void initPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        vpStepDetail.setPlayer(player);
        Uri uri = Uri.parse(stepArrayList.get(positonStep).getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
        player.setPlayWhenReady(true);
        player.seekTo(positionPlayer);
        setHasOptionsMenu(true);
    }

    private void releasePlayer() {
        if (player != null) {
            positionPlayer = player.getContentPosition();
            isPlaying = player.getPlayWhenReady();
            preferences.edit().putBoolean(Config.STATE_KEY_POSITION_VP_IS_PLAYING, isPlaying).apply();
            preferences.edit().putLong(Config.STATE_KEY_POSITION_VP, positionPlayer).apply();
            player.release();
            player = null;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (player == null) {
            initPlayer();

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (player == null) {
            initPlayer();

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();

    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_screen_for_tablet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_fullscreen:
                MainActivity mainActivity = (MainActivity) getContext();
                mainActivity.setFullScreen(positonStep, positionPlayer);
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
        outState.putLong("position", positionPlayer);
        outState.putBoolean("state", isPlaying);
        Timber.d("zapinae XDDD " + positionPlayer + isPlaying);
        super.onSaveInstanceState(outState);
    }
}
