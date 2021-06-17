package com.example.ammaryasser.mydocsbox.ui.main.adapter;
/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.ui.main.view.fragment.PlaceholderFragment;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final Map<Integer, Integer> bookIdPosMap = new HashMap<>();

    private List<Book> books = new ArrayList<>();


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return books.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return PlaceholderFragment.newInstance(position, books.get(position).getId());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return books.get(position).getTitle();
    }


    public static void addBookPos(int bookId) {
        bookIdPosMap.put(bookId, bookIdPosMap.size());
    }

    public static Integer getBookPos(int bookId) {
        return bookIdPosMap.get(bookId);
    }


    public void setBooks(List<Book> books) {
        clear();
        this.books = books;

        for (Book book : this.books) addBookPos(book.getId());

        notifyDataSetChanged();
    }

    public void clear() {
        books.clear();
        bookIdPosMap.clear();
    }

}
