package com.udacity.lorianns.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lorianns on 6/25/16.
 */
public class ImageAdapter extends BaseAdapter{

    private Context mContext;
    // references to movie images
    private ArrayList<MovieEntity> mMovieList;

    public ImageAdapter(Context c, ArrayList<MovieEntity> list) {
        mContext = c;
        mMovieList = list;
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(mContext);

            int movieHeight = (int)mContext.getResources().getDimension(R.dimen.movie_height);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, movieHeight));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        } else{
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(mMovieList.get(position).getImagePath()).into(imageView);

        return imageView;
    }

    public void clear(){
        mMovieList.clear();
    }
}
