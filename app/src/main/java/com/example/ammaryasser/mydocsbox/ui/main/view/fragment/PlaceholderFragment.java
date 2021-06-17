package com.example.ammaryasser.mydocsbox.ui.main.view.fragment;
/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ListAdapter;

import com.example.ammaryasser.mydocsbox.data.model.doc.Doc;
import com.example.ammaryasser.mydocsbox.databinding.FragmentMainBinding;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.RecyclerAdapterCompact;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.RecyclerAdapterInformative;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.viewholder.DocViewHolder;
import com.example.ammaryasser.mydocsbox.ui.main.viewmodel.PageViewModel;
import com.example.ammaryasser.mydocsbox.util.Prefs;


public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_BOOK_ID = "book_id";

    private FragmentMainBinding binding;
    private PageViewModel pageViewModel;


    public static PlaceholderFragment newInstance(int index, int bookId) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putInt(ARG_BOOK_ID, bookId);

        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        final int bookId = getArguments() != null ? getArguments().getInt(ARG_BOOK_ID) : -1;

        final FragmentManager supportFragmentManager = requireActivity().getSupportFragmentManager();

        final ListAdapter<Doc, ? extends DocViewHolder> adapter =
                Prefs.getPrefs(getContext()).getViewMode() == Prefs.ViewMode.COMPACT
                        ? new RecyclerAdapterCompact(supportFragmentManager)
                        : new RecyclerAdapterInformative(supportFragmentManager);

        binding.recyclerView.setAdapter(adapter);

        pageViewModel.docsMLiveData.observe(getViewLifecycleOwner(), adapter::submitList);
        pageViewModel.updateDocsMLiveData(getContext(), bookId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
