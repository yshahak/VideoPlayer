package com.downtube.videos.fragments;

/**
 * Created by B.E.L on 01/09/2016.
 */

public class VimeoFragment{//} extends Fragment implements View.OnClickListener, AuthCallback {
//
//    public static final String STAFF_PICKS_VIDEO_URI = "/channels/927/videos"; // 927 == staffpicks
//    public static final String SEARCH_VIDEO_URI = "videos?query=";
//    private static final String DATA_SAVED = "dataSaved";
//    private retrofit2.Call<java.lang.Object> currentCall;
//
//    private RecyclerView recyclerView;
//    private ProgressBar progressBar;
//    private View noInternetContainer;
//    private static VideoList videoList;
//
//    public static VimeoFragment newInstance(int page){
//        return new VimeoFragment();
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_vimeo, container, false);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
//
//        progressBar = (ProgressBar)view.findViewById(R.id.progress_indicator);
//        SearchView mSearchView = (SearchView) view.findViewById(R.id.searchview);
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                Log.d("TAG", query);
//                boolean dataSaved = (savedInstanceState != null) && savedInstanceState.getBoolean(DATA_SAVED, false);
//                if (dataSaved) {
//                    savedInstanceState.putBoolean(DATA_SAVED, false);
//                    setRecyclerViewState();
//                }  else {
//                    setVimeoList( (query.length() > 0) ? SEARCH_VIDEO_URI + query : STAFF_PICKS_VIDEO_URI);
//                }
//                return false;
//            }
//
//        });
//        noInternetContainer = view.findViewById(R.id.no_internet_container);
//        noInternetContainer.setOnClickListener(this);
//        if (savedInstanceState == null) {
//            setUiState();
//        }
//        return view;
//    }
//
//    private void setUiState() {
//        boolean connected = Utils.isNetworkAvailable(getContext());
//        noInternetContainer.setVisibility(connected ? View.GONE : View.VISIBLE);
//        progressBar.setVisibility(connected ? View.VISIBLE : View.GONE);
//        recyclerView.setVisibility(connected ? View.VISIBLE : View.GONE);
//        if (connected) {
//            authenticateWithClientCredentials();
//        }
//    }
//
//    private void authenticateWithClientCredentials() {
//        try {
//            VimeoClient.getInstance();
//            setVimeoList(STAFF_PICKS_VIDEO_URI);
//        } catch (AssertionError e) {
//            Log.d("TAG", "vimeoClient is null");
//            Configuration.Builder configBuilder =
//                    new Configuration.Builder(
//                            "a9f0a34c28db401317819037c9c8ae3601b011f4",
//                            "C+bMDFPFNzsNTYnt9lkwUu1dQKxnLGg/imGiSjlrEbcj4mwZ0ZoDWz7HAw6bA3UlAm14/ud7YemwGD2mz66IqJJOrfUbtFGIvgpHWVsVDlop8yCE8yML2yPXI1b6RUKd",
//                            "public private",
//                            null,
//                            new AndroidGsonDeserializer()
//                    );
//            VimeoClient.initialize(configBuilder.build());
//            VimeoClient.getInstance().authorizeWithClientCredentialsGrant(VimeoFragment.this);
//        }
//
//    }
//
//    public void setVimeoList(String query){
//        progressBar.setVisibility(View.VISIBLE);
//        RecyclerAdapterVimeo adapterVimeo = (RecyclerAdapterVimeo) recyclerView.getAdapter();
//        if (adapterVimeo != null) {
//            adapterVimeo.setVideoList(null);
//        } else {
//            recyclerView.setAdapter(new RecyclerAdapterVimeo(getActivity(), null));
//        }
//        if (currentCall != null) {
//            currentCall.cancel();
//        }
//        currentCall = VimeoClient.getInstance().fetchNetworkContent(query, new ModelCallback<VideoList>(VideoList.class) {
//            @Override
//            public void success(VideoList videoList) {
//                currentCall = null;
//                if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {
//                    VimeoFragment.this.videoList = videoList;
//                    setRecyclerViewState();
//                }
//            }
//
//            @Override
//            public void failure(VimeoError error) {
//                currentCall = null;
//                VimeoFragment.this.videoList = null;
//                String errorMessage = error.getDeveloperMessage();
//                Log.d("TAG", "failure:" + errorMessage);
//            }
//        });
//    }
//
//
//    private void setRecyclerViewState(){
//        progressBar.setVisibility(View.GONE);
//        RecyclerAdapterVimeo adapterVimeo = (RecyclerAdapterVimeo) recyclerView.getAdapter();
//        if (adapterVimeo != null) {
//            adapterVimeo.setVideoList(videoList);
//        } else {
//            recyclerView.setAdapter(new RecyclerAdapterVimeo(getActivity(), videoList));
//        }
//    }
//
//    @Override
//    public void onClick(View view) {
//        setUiState();
//    }
//
//
//    @Override
//    public void success() {
//        setVimeoList(STAFF_PICKS_VIDEO_URI);
//
//    }
//    @Override
//    public void failure(VimeoError error) {
//        String errorMessage = error.getDeveloperMessage();
//        Log.d("TAG", errorMessage);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean(DATA_SAVED, true);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        videoList = null;
//    }
}


