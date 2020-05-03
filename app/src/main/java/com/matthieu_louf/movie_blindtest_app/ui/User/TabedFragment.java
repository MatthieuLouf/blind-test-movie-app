package com.matthieu_louf.movie_blindtest_app.ui.User;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matthieu_louf.movie_blindtest_app.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class TabedFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private String TAG = "TabedFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new StaredMoviesFragment(), getContext().getResources().getString(R.string.stared));
        viewPagerAdapter.addFragment(new SeenMoviesFragment(), getContext().getResources().getString(R.string.seen));
        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter);


        tabLayout = view.findViewById(R.id.tab_layout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(new String[]{getContext().getResources().getString(R.string.stared),
                        getContext().getResources().getString(R.string.seen)}[position])
        );
        tabLayoutMediator.attach();

    }

    class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(Fragment fragment) {
            super(fragment);
        }

        public void addFragment(Fragment fragment, String fragmentTitle) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(fragmentTitle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);
        }


        @Override
        public int getItemCount() {
            return mFragmentList.size();
        }

    }
}
