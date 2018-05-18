package mrodkiewicz.pl.bakingapp.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
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
import timber.log.Timber;

public class StepDetailFragment extends Fragment {
    @BindView(R.id.vp_step_detail)
    PlayerView vpStepDetail;
    @BindView(R.id.big_tv_step_detail)
    TextView bigTvStepDetail;
    @BindView(R.id.medium_1_tv_tv)
    TextView medium1TvTv;
    Unbinder unbinder;
    private ArrayList<Step> stepArrayList;
    private StepsRecycleViewAdapter stepsRecycleViewAdapter;
    private int positonStep,position;
    private ExoPlayer player;

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

    }

    private void setupView() {
        bigTvStepDetail.setText(stepArrayList.get(positonStep).getShortDescription());
        medium1TvTv.setText(stepArrayList.get(positonStep).getDescription());
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        if (stepArrayList.get(positonStep).getVideoURL() == null ||  stepArrayList.get(positonStep).getVideoURL().isEmpty()){
            vpStepDetail.setVisibility(View.GONE);
            Timber.d("getVideoURL empty");
        }else{
            Timber.d("getVideoURL " + stepArrayList.get(positonStep).getVideoURL());
            vpStepDetail.setPlayer(player);
            Uri uri = Uri.parse(stepArrayList.get(positonStep).getVideoURL());
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail_fragmnet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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
}
