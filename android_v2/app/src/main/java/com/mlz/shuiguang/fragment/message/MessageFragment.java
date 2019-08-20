package com.mlz.shuiguang.fragment.message;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mlz.shuiguang.R;
import com.mlz.shuiguang.model.MessageData;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.util.LocaleHelper;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;

import java.util.ArrayList;
import java.util.Locale;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by dehualai on 12/24/16.
 */

public class MessageFragment extends Fragment {

    String TAG = "MessageFragment";
    Context mContext;
    FragmentUtil fragmentUtil;
    IMainAppHandler mainAppHandler;
    View view;

    LinearLayout bgLayout;
    LinearLayout headerLayout;

    private CardArrayAdapter mCardArrayAdapter;
    private CardListView mListView;

    ArrayList<MessageData> messageDatas;
    ArrayList<Card> cards;

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach");
        super.onAttach(activity);
        mContext = activity;

        fragmentUtil = new FragmentUtil(activity);
        if(activity instanceof IMainAppHandler){
            this.mainAppHandler = (IMainAppHandler) activity;
        } else {
            Log.e(TAG, "The input activity must implement interface IMainAppHandler");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_message, container, false);
        bgLayout = (LinearLayout)view.findViewById(R.id.bg_layout);
        headerLayout = (LinearLayout)view.findViewById(R.id.header_layout);
        createMessageList();

        return view;
    }


    public void onResume(){
        super.onResume();
    }

    private void createMessageList(){
        String localUrlPath = "file:///android_asset/message/";
        String langSubFolder = "zh/";

        String langCode = LocaleHelper.getLanguage(mContext);

        if (langCode.equals("en")){
            langSubFolder = "en/";
        }

        messageDatas = new ArrayList<MessageData>();
        MessageData mData2 = new MessageData();
        mData2.setTitle(mContext.getResources().getString(R.string.message_2_title));
        mData2.setBrief(mContext.getResources().getString(R.string.message_2_brief));
        mData2.setThumbnail(R.drawable.avatar_placeholder);
        mData2.setHtml_url( localUrlPath + langSubFolder + "message_2.html");
        messageDatas.add(mData2);

        MessageData mData3 = new MessageData();
        mData3.setTitle(mContext.getResources().getString(R.string.message_3_title));
        mData3.setBrief(mContext.getResources().getString(R.string.message_3_brief));
        mData3.setThumbnail(R.drawable.avatar_placeholder);
        mData3.setHtml_url( localUrlPath + langSubFolder + "message_3.html");
        messageDatas.add(mData3);

        MessageData mData4 = new MessageData();
        mData4.setTitle(mContext.getResources().getString(R.string.message_4_title));
        mData4.setBrief(mContext.getResources().getString(R.string.message_4_brief));
        mData4.setThumbnail(R.drawable.avatar_placeholder);
        mData4.setHtml_url( localUrlPath + langSubFolder + "message_4.html");
        messageDatas.add(mData4);

        MessageData mData5 = new MessageData();
        mData5.setTitle(mContext.getResources().getString(R.string.message_5_title));
        mData5.setBrief(mContext.getResources().getString(R.string.message_5_brief));
        mData5.setThumbnail(R.drawable.avatar_placeholder);
        mData5.setHtml_url( localUrlPath + langSubFolder + "message_5.html");
        messageDatas.add(mData5);


        MessageData mData6 = new MessageData();
        mData6.setTitle(mContext.getResources().getString(R.string.message_6_title));
        mData6.setBrief(mContext.getResources().getString(R.string.message_6_brief));
        mData6.setThumbnail(R.drawable.avatar_placeholder);
        mData6.setHtml_url( localUrlPath + langSubFolder + "message_6.html");
        messageDatas.add(mData6);

        MessageData mData1 = new MessageData();
        mData1.setTitle(mContext.getResources().getString(R.string.message_1_title));
        mData1.setBrief(mContext.getResources().getString(R.string.message_1_brief));
        mData1.setThumbnail(R.drawable.avatar_placeholder);
        mData1.setHtml_url( localUrlPath + langSubFolder + "message_1.html");
        messageDatas.add(mData1);

        MessageData mData7 = new MessageData();
        mData7.setTitle(mContext.getResources().getString(R.string.message_7_title));
        mData7.setBrief(mContext.getResources().getString(R.string.message_7_brief));
        mData7.setThumbnail(R.drawable.avatar_placeholder);
        mData7.setHtml_url( localUrlPath + langSubFolder + "message_7.html");
        messageDatas.add(mData7);

        MessageData mData8 = new MessageData();
        mData8.setTitle(mContext.getResources().getString(R.string.message_8_title));
        mData8.setBrief(mContext.getResources().getString(R.string.message_8_brief));
        mData8.setThumbnail(R.drawable.avatar_placeholder);
        mData8.setHtml_url( localUrlPath + langSubFolder + "message_8.html");
        messageDatas.add(mData8);

        /*
        MessageData mData2 = new MessageData();
        mData2.setTitle("Title 1");
        mData2.setBrief("brief ....aa...");
        mData2.setHtml_url( localUrlPath + languageCode + "helloworld.html");
        messageDatas.add(mData2);
        */


    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //Set the arrayAdapter

        initCards();

        mCardArrayAdapter = new CardArrayAdapter(mContext,cards);
        mListView = (CardListView) getActivity().findViewById(R.id.carddemo_list_gplaycard);


        //CardListView listView = (CardListView) getActivity().findViewById(R.id.carddemo_list_gplaycard);
        if (mListView!=null){
            mListView.setAdapter(mCardArrayAdapter);
        }



        //Load cards
        //new LoaderAsyncTask().execute();

    }




    private void initCards() {

        //Init an array of Cards
        cards = new ArrayList<Card>();

        for(MessageData messageData: messageDatas){
            GooglePlaySmallCard card = new GooglePlaySmallCard(this.getActivity());
            card.setTitle(messageData.getTitle());
            card.setSecondaryTitle(messageData.getBrief());
            card.setResourceIdThumbnail(messageData.getThumbnail());
            card.setId(messageData.getHtml_url());

            card.init();
            card.setClickable(true);
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {

                    if (fragmentUtil!=null){
                        Fragment fragment = new MessageDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(MessageDetailsFragment.CONTENT_URL, card.getId());
                        fragment.setArguments(bundle);
                        fragmentUtil.showFragment(fragment);
                    }
                }
            });
            cards.add(card);
        }


    }





    /**
     * This class provides a simple card as Google Play
     *
     * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
     */
    public class GooglePlaySmallCard extends Card {

        protected TextView mTitle;
        protected TextView mSecondaryTitle;
        protected int resourceIdThumbnail;
        protected int count;

        protected String title;
        protected String secondaryTitle;
        protected float rating;


        public GooglePlaySmallCard(Context context) {
            this(context, R.layout.carddemo_mycard_inner_content);
        }

        public GooglePlaySmallCard(Context context, int innerLayout) {
            super(context, innerLayout);
            //init();
        }

        private void init() {

            //Add thumbnail
            CardThumbnail cardThumbnail = new CardThumbnail(mContext);

            if (resourceIdThumbnail==0)
                cardThumbnail.setDrawableResource(R.drawable.avatar_placeholder);
            else{
                cardThumbnail.setDrawableResource(resourceIdThumbnail);
            }

            //addCardThumbnail(cardThumbnail);

            setSwipeable(true);

            setOnSwipeListener(new OnSwipeListener() {
                @Override
                public void onSwipe(Card card) {
                    Toast.makeText(getContext(), "Removed card=" + title, Toast.LENGTH_SHORT).show();
                }
            });


            setOnUndoSwipeListListener(new OnUndoSwipeListListener() {
                @Override
                public void onUndoSwipe(Card card) {
                    Toast.makeText(getContext(), "Undo card=" + title, Toast.LENGTH_SHORT).show();
                }
            });

            setOnUndoHideSwipeListListener(new OnUndoHideSwipeListListener() {
                @Override
                public void onUndoHideSwipe(Card card) {
                    Toast.makeText(getContext(), "Hide undo card=" + title, Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            //Retrieve elements
            mTitle = (TextView) parent.findViewById(R.id.carddemo_myapps_main_inner_title);
            mSecondaryTitle = (TextView) parent.findViewById(R.id.carddemo_myapps_main_inner_secondaryTitle);

            if (mTitle != null)
                mTitle.setText(title);

            if (mSecondaryTitle != null)
                mSecondaryTitle.setText(secondaryTitle);


        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSecondaryTitle() {
            return secondaryTitle;
        }

        public void setSecondaryTitle(String secondaryTitle) {
            this.secondaryTitle = secondaryTitle;
        }



        public int getResourceIdThumbnail() {
            return resourceIdThumbnail;
        }

        public void setResourceIdThumbnail(int resourceIdThumbnail) {
            this.resourceIdThumbnail = resourceIdThumbnail;
        }
    }
}
