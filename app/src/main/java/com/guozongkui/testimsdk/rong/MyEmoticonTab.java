package com.guozongkui.testimsdk.rong;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.guozongkui.testimsdk.R;

import io.rong.imkit.emoticon.IEmojiItemClickListener;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.utilities.ExtensionHistoryUtil;

public class MyEmoticonTab implements IEmoticonTab {

    private static final String TAG = "MyEmoticonTab";
    private int mEmojiCountPerPage;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mIndicator;
    private int selected = 0;
    private String mUserId;

    private IEmojiItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(IEmojiItemClickListener clickListener) {
        mOnItemClickListener = clickListener;
    }

    @Override
    public Drawable obtainTabDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.em_default_062);
    }

    @Override
    public View obtainTabPager(Context context) {
        return initView(context);
    }

    @Override
    public void onTableSelected(int i) {

    }


    public View initView(final Context context) {
        int count = QQEmoji.getEmojiSize();

        mEmojiCountPerPage = 20;

        int pages = count / (mEmojiCountPerPage) + ((count % mEmojiCountPerPage) != 0 ? 1 : 0);

        View view = LayoutInflater.from(context).inflate(io.rong.imkit.R.layout.rc_ext_emoji_pager, null);
        ViewPager viewPager = view.findViewById(io.rong.imkit.R.id.rc_view_pager);
        this.mIndicator = view.findViewById(io.rong.imkit.R.id.rc_indicator);
        mLayoutInflater = LayoutInflater.from(context);

        viewPager.setAdapter(new MyEmoticonTab.EmojiPagerAdapter(pages));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ExtensionHistoryUtil.setEmojiPosition(context, mUserId, position);
                onIndicatorChanged(selected, position);
                selected = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(1);

        initIndicator(pages, mIndicator);
        int position = ExtensionHistoryUtil.getEmojiPosition(context, mUserId);
        viewPager.setCurrentItem(position);
        onIndicatorChanged(-1, position);
        return view;
    }


    private class EmojiPagerAdapter extends PagerAdapter {
        int count;

        public EmojiPagerAdapter(int count) {
            super();
            this.count = count;
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GridView gridView = (GridView) mLayoutInflater.inflate(io.rong.imkit.R.layout.rc_ext_emoji_grid_view, null);
            gridView.setAdapter(new MyEmoticonTab.EmojiAdapter(position * mEmojiCountPerPage, QQEmoji.getEmojiSize()));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "onItemClick: position = "+position);
                    if (mOnItemClickListener != null) {
                        int index = position + selected * mEmojiCountPerPage;
                        if (position == mEmojiCountPerPage) {
                            mOnItemClickListener.onDeleteClick();
                        } else {
                            if (index >= QQEmoji.getEmojiSize()) {
                                mOnItemClickListener.onDeleteClick();
                            } else {
                                int code = QQEmoji.getEmojiCode(index);
                                char[] chars = Character.toChars(code);
                                StringBuilder key = new StringBuilder(Character.toString(chars[0]));
                                for (int i = 1; i < chars.length; i++) {
                                    key.append(chars[i]);
                                }
                                mOnItemClickListener.onEmojiClick(key.toString());
                            }
                        }
                    }
                }
            });
            container.addView(gridView);
            return gridView;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            View layout = (View) object;
            container.removeView(layout);
        }
    }


    private class EmojiAdapter extends BaseAdapter {
        int count;
        int index;

        public EmojiAdapter(int index, int count) {
            this.count = Math.min(mEmojiCountPerPage, count - index);
            this.index = index;
        }

        @Override
        public int getCount() {
            return count + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyEmoticonTab.ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new MyEmoticonTab.ViewHolder();
                convertView = mLayoutInflater.inflate(io.rong.imkit.R.layout.rc_ext_emoji_item, null);
                viewHolder.emojiIV = convertView.findViewById(io.rong.imkit.R.id.rc_ext_emoji_item);
                convertView.setTag(viewHolder);
            }
            viewHolder = (MyEmoticonTab.ViewHolder) convertView.getTag();
            if (position == mEmojiCountPerPage || position + index == QQEmoji.getEmojiSize()) {
                viewHolder.emojiIV.setImageResource(io.rong.imkit.R.drawable.rc_icon_emoji_delete);
            } else {
                viewHolder.emojiIV.setImageDrawable(QQEmoji.getEmojiDrawable(parent.getContext(), index + position));
            }

            return convertView;
        }
    }

    private void initIndicator(int pages, LinearLayout indicator) {
        for (int i = 0; i < pages; i++) {
            ImageView imageView = (ImageView) mLayoutInflater.inflate(io.rong.imkit.R.layout.rc_ext_indicator, null);
            imageView.setImageResource(io.rong.imkit.R.drawable.rc_ext_indicator);
            indicator.addView(imageView);
        }
    }

    private void onIndicatorChanged(int pre, int cur) {
        int count = mIndicator.getChildCount();
        if (count > 0 && pre < count && cur < count) {
            if (pre >= 0) {
                ImageView preView = (ImageView) mIndicator.getChildAt(pre);
                preView.setImageResource(io.rong.imkit.R.drawable.rc_ext_indicator);
            }
            if (cur >= 0) {
                ImageView curView = (ImageView) mIndicator.getChildAt(cur);
                curView.setImageResource(io.rong.imkit.R.drawable.rc_ext_indicator_hover);
            }
        }
    }

    private class ViewHolder {
        ImageView emojiIV;
    }
}
