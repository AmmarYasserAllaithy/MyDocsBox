package com.example.ammaryasser.mydocsbox.ui.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.ammaryasser.mydocsbox.data.model.doc.Doc;
import com.example.ammaryasser.mydocsbox.data.repository.DocRepository;
import com.example.ammaryasser.mydocsbox.databinding.BsDocOpsBinding;
import com.example.ammaryasser.mydocsbox.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static com.example.ammaryasser.mydocsbox.util.Constant.EXTRA_DOC;
import static com.example.ammaryasser.mydocsbox.util.Constant.EXTRA_EDIT;


@SuppressWarnings("ALL")
public class BottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "BottomSheetTAG";

    private DocRepository docRepo;
    private Utils utils;

    public Doc doc;


    public BottomSheet(Doc doc) {
        this.doc = doc;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final BsDocOpsBinding binding = BsDocOpsBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);

        docRepo = DocRepository.getInstance(getContext());

        utils = new Utils(getContext());

        binding.bsViewTv.setOnClickListener(v -> view());
        binding.bsEditTv.setOnClickListener(v -> edit());
        binding.bsCopyTv.setOnClickListener(v -> copy());
        binding.bsExportTv.setOnClickListener(v -> export());
        binding.bsShareTv.setOnClickListener(v -> share());
        binding.bsArchiveTv.setOnClickListener(v -> archive());
        binding.bsDeleteTv.setOnClickListener(v -> delete());

        return binding.getRoot();
    }


    private void view() {
        startActivity(new Intent(getContext(), DetailsActivity.class)
                .putExtra(EXTRA_DOC, doc));
        dismiss();
    }

    private void edit() {
        startActivity(new Intent(getContext(), AddActivity.class)
                .putExtra(EXTRA_EDIT, true)
                .putExtra(EXTRA_DOC, doc));
        dismiss();
    }

    private void copy() {
        utils.copyAndToast("doc details", doc.json());
        dismiss();
    }

    private void export() {
        utils.toast("Export was clicked");
        dismiss();
    }

    private void share() {
        utils.toast("Share was clicked");
        dismiss();
    }

    private void archive() {
        utils.toast("Archive was clicked");
        dismiss();
    }

    private void delete() {
        docRepo.delete(doc).subscribe(() -> {
            requireActivity().recreate();
            utils.toast("Deleted!");
        }, e -> {
            utils.toast("Can't retrieve doc!");
            Log.e(TAG, "delete: ", e);
        });
        dismiss();
    }

}