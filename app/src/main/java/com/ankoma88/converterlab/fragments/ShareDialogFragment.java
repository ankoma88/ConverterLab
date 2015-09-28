package com.ankoma88.converterlab.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ankoma88.converterlab.R;
import com.ankoma88.converterlab.db.OfferDAO;
import com.ankoma88.converterlab.db.TraderDAO;
import com.ankoma88.converterlab.models.Offer;
import com.ankoma88.converterlab.models.Trader;
import com.ankoma88.converterlab.views.ShareInfoView;
import com.ankoma88.converterlab.views.ShareOfferView;

import java.util.List;

/**
 * Dialog for sharing data converted into picture
 */
public class ShareDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = ShareDialogFragment.class.getSimpleName();
    private ImageView mIvShareContent;
    private ImageView mIvShareBtn;
    private Trader mTrader;
    private List<Offer> mOffers;
    private Bitmap mBitmap;

    public ShareDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_dialog_share, container);
        findViews(view);

        return view;
    }

    private void findViews(View view) {
        mIvShareContent = (ImageView) view.findViewById(R.id.iv_share_content_FS);
        mIvShareBtn = (ImageView) view.findViewById(R.id.btn_share_FS);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        createContentPicture();
        mIvShareBtn.setOnClickListener(ShareDialogFragment.this);
    }

    /**
     * Creates content view and transforms it to bitmap
     */
    private void createContentPicture() {
        final LinearLayout llContent = new LinearLayout(getActivity());
        llContent.setOrientation(LinearLayout.VERTICAL);

        ShareInfoView shareInfoView = new ShareInfoView(getActivity());
        shareInfoView.setView(mTrader);

        llContent.addView(shareInfoView);

        for (Offer offer : mOffers) {
            ShareOfferView shareOfferView = new ShareOfferView(getActivity());
            shareOfferView.setView(offer);
            llContent.addView(shareOfferView);
        }

        AsyncBitmapDrawer asyncBitmapDrawer = new AsyncBitmapDrawer(
                llContent, mIvShareContent, mIvShareBtn);
        asyncBitmapDrawer.execute();
    }


    private void init() {
        final TraderDAO traderDao = new TraderDAO(getActivity());
        final OfferDAO offerDAO = new OfferDAO(getActivity());
        Bundle mArgs = getArguments();
        int traderId = mArgs.getInt("traderId");
        mTrader = traderDao.get(traderId);
        mOffers = offerDAO.getAllByTraderOrgId(mTrader.getOrgId());
    }

    @Override
    public void onClick(View v) {

        Uri imageUri = saveImage(mBitmap);

        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType("image/png");
        startActivity(intent);

        dismiss();
    }

    private Uri saveImage(Bitmap bitmap) {
        String pathToBmp = MediaStore.Images.Media
                .insertImage(getActivity().getContentResolver(), bitmap, "title", null);
        return Uri.parse(pathToBmp);
    }

    private Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);

        return bitmap;
    }

    /**
     * Convert view to bitmap async
     */
    private class AsyncBitmapDrawer extends AsyncTask<Void, Void, Bitmap> {
        private View mView;
        private ImageView mImageView;
        private ImageView mIvShareBtn;

        public AsyncBitmapDrawer(View viewToDraw, ImageView imageView, ImageView ivShareBtn) {
            mView = viewToDraw;
            mImageView = imageView;
            mIvShareBtn = ivShareBtn;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return getBitmapFromView(mView);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
            mIvShareBtn.setVisibility(View.VISIBLE);
            mBitmap = bitmap;
        }
    }


}
