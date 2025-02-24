package com.example.deem.adapters;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deem.MainActivity;
import com.example.deem.R;
import com.example.deem.dialogs.FullScreenImageDialog;
import com.example.restful.models.VideoCallback;
import com.example.restful.utils.DateTranslator;
import com.example.deem.utils.ImageUtil;
import com.example.restful.api.APIManager;
import com.example.restful.models.ImageLoadCallback;
import com.example.restful.models.News;
import com.example.restful.models.NewsImage;
import com.example.restful.utils.HLS_ProtocolSystem;
import com.google.android.flexbox.FlexboxLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsListRecycleAdapter extends RecyclerView.Adapter<NewsListRecycleAdapter.ItemNews> {

    private List<News> newsList;
    private Fragment fragment;

    public NewsListRecycleAdapter(List<News> newsList, Fragment fragment) {
        this.newsList = newsList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ItemNews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);

        return new ItemNews(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemNews holder, int position) {
        holder.date.setText("");
        holder.content.setText("");
        holder.name_group.setText("");
        holder.icon.setText("");
        holder.container.removeAllViews();
        holder.container.setVisibility(View.GONE);

        holder.setData(newsList.get(position));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    class ItemNews extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView content;
        private TextView name_group;
        private TextView icon;
        private FlexboxLayout container;
        private final int indent = 50; //test

        //video
        private PlayerView playerView;
        private ExoPlayer exoPlayer;

        public ItemNews(@NonNull View itemView) {
            super(itemView);

            content      = itemView.findViewById(R.id.news_content_info);
            date         = itemView.findViewById(R.id.news_date_info);
            name_group   = itemView.findViewById(R.id.news_namegroup_info);
            container    = itemView.findViewById(R.id.list_images);
            icon         = itemView.findViewById(R.id.icon_group_main);
            playerView   = itemView.findViewById(R.id.player_view);

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", icon.getText().toString());

                    MainActivity mainActivity = (MainActivity) fragment.getActivity();
                    mainActivity.OpenMenu(MainActivity.FragmentType.group, bundle);
                }
            });

        }

        public void setData(News news) {
            date.setText(DateTranslator.getInstance().toString(news.getDate()));
            content.setText(news.getContent());

            if (news.getGroup() != null) {
                name_group.setText(news.getGroup().getName());
                icon      .setText(news.getGroup().getName());
            }

            //Загрузка изображений
            if (fragment.getContext() != null)
            if (news.getImages().getValue() != null && news.getImages().getValue().size() != 0) {
                List<ImageView> imageViews = new ArrayList<>();

                for (NewsImage newsImage : news.getImages().getValue())
                    imageLoad(imageViews, newsImage.getImage().getImgEncode());

            } else if (!news.isCompleted()) { //загружаем новые изображения из сервера или кэша
                List<ImageView> imageViews = new ArrayList<>();

                APIManager.getManager().getNewsImagesLazy(news, new ImageLoadCallback() {
                    @Override
                    public void onImageLoaded(String decodeStr) {
                        imageLoad(imageViews, decodeStr);
                    }
                });

            } else
                container.setVisibility(View.GONE); //если изображений нет то отключаем видимость


            //Video
            DoVideo(news);
        }

        @SuppressLint("UnsafeOptInUsageError")
        private void DoVideo(News news) {

            if (exoPlayer == null) {

                if (!news.isCompleted()) {
                    //В случае если нужно загрузить видео от сервера.
                    if (news.getVideoUUID() != null && !news.getVideoUUID().isEmpty())

                        APIManager.getManager().getVideoManifest(new VideoCallback() {
                            @Override
                            public void loadVideo(String url) {
                                playerView.setVisibility(View.VISIBLE);

                                exoPlayer = new ExoPlayer.Builder(fragment.getActivity()).build();
                                playerView.setPlayer(exoPlayer);

                                exoPlayer.setMediaSource(HLS_ProtocolSystem.getInstance().createHLSMediaSource(url));
                                exoPlayer.prepare();
                                exoPlayer.play();
                            }
                        }, news.getVideoUUID());
                } else
                    //В случае если мы отправители видео и у нас уже оно есть.
                    if (news.isThereVideo() && news.getVideoMetadata() != null) {
                        playerView.setVisibility(View.VISIBLE);

                        try {
                            File tempFile = File.createTempFile("video", ".mp4", fragment.getActivity().getCacheDir());
                            tempFile.deleteOnExit();

                            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                                fos.write(news.getVideoMetadata().getVideo());
                            }

                            exoPlayer = new ExoPlayer.Builder(fragment.getActivity()).build();
                            playerView.setPlayer(exoPlayer);

                            MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(tempFile));
                            exoPlayer.setMediaItem(mediaItem);
                            exoPlayer.prepare();
                            exoPlayer.play();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }

        /** Преобразовываем строку в изображение ImageView */
        private void imageLoad(List<ImageView> imageViews, String decodeStr) {
            if (fragment.getContext() != null && fragment.getContext().getResources() != null) { //баг
                ImageView imageView = new ImageView(fragment.getContext());
                Bitmap bitmap = ImageUtil.getInstance().ConvertToBitmap(decodeStr);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); //Обрезает лишнее и заполняет
                imageView.setImageBitmap(getScaledBitmap(bitmap));
                imageViews.add(imageView);

                container.setVisibility(View.VISIBLE);
                container.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int n = 0;
                        for (int j = 0; j < imageViews.size(); j++)
                            if (imageViews.get(j) == imageView)
                                n = j;

                        FullScreenImageDialog dialog = new FullScreenImageDialog();
                        dialog.initialize(imageViews, n);
                        dialog.show(fragment.getActivity().getSupportFragmentManager(), "full_screen_dialog");
                    }
                });
            }
        }

        /** Задаем размер bitmap соответственно текущему устройству */
        private Bitmap getScaledBitmap(Bitmap bitmap) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            fragment.getActivity().getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);

            int targetWidth = displayMetrics.widthPixels - indent;
            int targetHeight = Math.round((float) bitmap.getHeight() * ((float) targetWidth / (float) bitmap.getWidth()));

            return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
        }

    }
}
