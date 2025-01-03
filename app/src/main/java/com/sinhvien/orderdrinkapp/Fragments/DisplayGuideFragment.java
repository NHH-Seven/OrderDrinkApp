package com.sinhvien.orderdrinkapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sinhvien.orderdrinkapp.Activities.ProductDetailActivity;
import com.sinhvien.orderdrinkapp.R;

public class DisplayGuideFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gắn layout huong_dan_su_dung_layout
        View view = inflater.inflate(R.layout.huong_dan_sd, container, false);

        // Tìm nút "Quay Lại"
        ImageView IMG_hdsd_backbtn = view.findViewById(R.id.img_hdsd_back);
        Button btnvideo = view.findViewById(R.id.btnvideo);
        btnvideo.setOnClickListener(v -> openProductDetail("https://www.youtube.com/watch?v=NDBxhikG97o"));


        // Xử lý sự kiện click
        IMG_hdsd_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay về Fragment Home
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentView, new DisplayHomeFragment())
                        .commit();
            }
        });

        return view;
    }
    private void openProductDetail(String url) {
        Intent intent = new Intent(requireActivity(), ProductDetailActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}

